package io.vproxy.vfd.windows;

import io.vproxy.base.util.LogType;
import io.vproxy.base.util.Logger;
import io.vproxy.base.util.Utils;
import io.vproxy.base.util.ringbuffer.SimpleRingBuffer;
import io.vproxy.base.util.ringbuffer.SimpleRingBufferPreserveEPos;
import io.vproxy.base.util.thread.VProxyThread;
import io.vproxy.pni.Allocator;
import io.vproxy.pni.PNIRef;
import io.vproxy.pni.PooledAllocator;
import io.vproxy.vfd.IPPort;
import io.vproxy.vfd.IPv4;
import io.vproxy.vfd.IPv6;
import io.vproxy.vfd.posix.PosixNative;
import io.vproxy.vfd.posix.SocketAddressIPv4;
import io.vproxy.vfd.posix.SocketAddressIPv6;

import java.io.IOException;
import java.lang.foreign.MemorySegment;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class WinSocket {
    private final AtomicBoolean closed = new AtomicBoolean(false);
    private final Allocator allocator;
    private final PNIRef<WinSocket> ref;

    public final SOCKET fd;
    private WinSocket listenSocket; // optional
    private final boolean datagram;
    private final boolean isListenSocket;
    public Object ud = null;

    public final MemorySegment recvMemSeg;
    public final MemorySegment sendMemSeg;
    public final SimpleRingBuffer recvRingBuffer;
    public final SimpleRingBuffer sendRingBuffer;

    public final VIOContext recvContext;
    public final VIOContext sendContext;

    private final AtomicInteger refCnt = new AtomicInteger(1); // decr when close

    WinIOCP iocp;
    private final ConcurrentLinkedQueue<WinIOCP.Notification> notifications = new ConcurrentLinkedQueue<>();

    public static WinSocket ofStream(int fd) {
        return new WinSocket(fd, null, false, false);
    }

    public static WinSocket ofAcceptedStream(int fd, WinSocket listenSocket) {
        return new WinSocket(fd, listenSocket, false, false);
    }

    public static WinSocket ofDatagram(int fd) {
        return new WinSocket(fd, null, true, false);
    }

    public static WinSocket ofServer(int fd) {
        return new WinSocket(fd, null, false, true);
    }

    private WinSocket(int fd, WinSocket listenSocket, boolean datagram, boolean isListenSocket) {
        allocator = PooledAllocator.ofUnsafePooled();
        this.ref = PNIRef.of(this);
        this.fd = new SOCKET(MemorySegment.ofAddress(fd));
        this.listenSocket = listenSocket;
        this.datagram = datagram;
        this.isListenSocket = isListenSocket;

        if (isListenSocket) {
            recvMemSeg = null;
            recvRingBuffer = null;
        } else if (datagram) {
            recvMemSeg = allocator.allocate(65536); // max udp packet size is 65535
            recvRingBuffer = SimpleRingBuffer.wrap(recvMemSeg);
        } else {
            recvMemSeg = allocator.allocate(24576);
            recvRingBuffer = SimpleRingBufferPreserveEPos.wrap(recvMemSeg);
        }

        if (datagram || isListenSocket) {
            sendMemSeg = null;
            sendRingBuffer = null;
        } else {
            sendMemSeg = allocator.allocate(24576);
            sendRingBuffer = SimpleRingBufferPreserveEPos.wrap(sendMemSeg);
        }

        if (isListenSocket) {
            recvContext = null;
        } else {
            recvContext = new VIOContext(allocator);
            recvContext.setSocket(this.fd);
            recvContext.setRef(ref);
            if (listenSocket == null) {
                recvContext.setIoType(IOType.READ.code);
            } else {
                recvContext.setIoType(IOType.ACCEPT.code);
            }
            recvContext.getBuffers().get(0).setBuf(recvMemSeg);
            recvContext.getBuffers().get(0).setLen(24576);
            recvContext.setBufferCount(1);
            recvContext.setCtxType(IOCPUtils.VPROXY_CTX_TYPE);
            recvContext.setAddrLen((int) SockaddrStorage.LAYOUT.byteSize());
            IOCPUtils.setPointer(recvContext);
        }
        if (datagram || isListenSocket) {
            sendContext = null;
        } else {
            sendContext = new VIOContext(allocator);
            sendContext.setSocket(this.fd);
            sendContext.setRef(ref);
            if (listenSocket == null) {
                sendContext.setIoType(IOType.CONNECT.code);
            } else {
                sendContext.setIoType(IOType.WRITE.code);
            }
            sendContext.getBuffers().get(0).setBuf(recvMemSeg);
            sendContext.getBuffers().get(0).setLen(0);
            sendContext.setBufferCount(1);
            sendContext.setCtxType(IOCPUtils.VPROXY_CTX_TYPE);
            IOCPUtils.setPointer(sendContext);
        }

        UnderlyingIOCP.get().associate(this.fd);
    }

    public WinSocket getListenSocket() {
        return listenSocket;
    }

    public void updateAcceptContext() throws IOException {
        WindowsNative.get().updateAcceptContext(VProxyThread.current().getEnv(),
            listenSocket.fd, fd);
        listenSocket = null;
    }

    WinIOCP getIocp() {
        var iocp = this.iocp;
        if (iocp == null) {
            return null;
        }
        if (iocp.isClosed()) {
            this.iocp = null;
            return null;
        }
        return iocp;
    }

    public void incrIORefCnt() {
        refCnt.incrementAndGet();
    }

    public void decrIORefCnt() {
        if (refCnt.decrementAndGet() == 0) {
            release();
        }
    }

    public boolean isClosed() {
        return closed.get();
    }

    public void close() {
        if (closed.get()) {
            return;
        }
        if (!closed.compareAndSet(false, true)) {
            return;
        }

        try {
            WindowsNative.get().cancelIo(VProxyThread.current().getEnv(), fd);
        } catch (IOException e) {
            Logger.error(LogType.SYS_ERROR, "failed to cancelIo for " + this, e);
        }

        int refCntAfterDec = refCnt.decrementAndGet();
        assert Logger.lowLevelDebug(this + " is closing, current ioRefCnt is " + refCntAfterDec + ", notifications.size = " + notifications.size());
        if (refCntAfterDec == 0) {
            release();
            return;
        }
        //noinspection WhileCanBeDoWhile
        while (notifications.poll() != null) {
            if (refCnt.decrementAndGet() == 0) {
                release();
                return;
            }
        }
    }

    private void release() {
        assert Logger.lowLevelDebug("calling release on " + this);
        if (isStreamSocket() && listenSocket == null) {
            try {
                WindowsNative.get().wsaSendDisconnect(VProxyThread.current().getEnv(), fd);
            } catch (IOException e) {
                assert Logger.lowLevelDebug("failed to disconnect: " + this + ": " + Utils.formatErr(e));
            }
        }
        try {
            WindowsNative.get().closeHandle(VProxyThread.current().getEnv(), fd);
        } catch (IOException e) {
            Logger.error(LogType.SOCKET_ERROR, "failed to close win socket: " + this, e);
        }
        ref.close();
        allocator.close();
    }

    public boolean isDatagramSocket() {
        return datagram;
    }

    public boolean isListenSocket() {
        return isListenSocket;
    }

    public boolean isStreamSocket() {
        return !datagram;
    }

    @Override
    public String toString() {
        var sb = new StringBuilder();
        sb.append("WinSocket(").append(fd.MEMORY.address())
            .append("#").append(refCnt.get());
        if (localAddress != null || remoteAddress != null) {
            sb.append("#");
        }
        if (localAddress != null) {
            sb.append(localAddress.formatToIPPortString()).append("->");
        }
        if (remoteAddress != null) {
            if (localAddress == null) {
                sb.append("->");
            }
            sb.append(remoteAddress.formatToIPPortString());
        }
        sb.append(")");
        if (closed.get()) {
            sb.append("[closed]");
        } else {
            sb.append("[open]");
        }
        return sb.toString();
    }

    IPPort localAddress;
    IPPort remoteAddress;

    public IPPort getLocalAddress(boolean v4) throws IOException {
        if (localAddress != null) {
            if (v4) {
                if (localAddress.getAddress() instanceof IPv4) {
                    return localAddress;
                }
                throw new IOException(this + " is not an ipv4 socket");
            } else {
                if (localAddress.getAddress() instanceof IPv6) {
                    return localAddress;
                }
                throw new IOException(this + " is not an ipv6 socket");
            }
        }
        try (var allocator = Allocator.ofConfined()) {
            if (v4) {
                var st = PosixNative.get().getIPv4Local(VProxyThread.current().getEnv(),
                    (int) fd.MEMORY.address(), allocator);
                var addr = new SocketAddressIPv4(st.getIp(), st.getPort() & 0xffff);
                localAddress = addr.toIPPort();
            } else {
                var st = PosixNative.get().getIPv6Local(VProxyThread.current().getEnv(),
                    (int) fd.MEMORY.address(), allocator);
                var addr = new SocketAddressIPv6(st.getIp(), st.getPort() & 0xffff);
                localAddress = addr.toIPPort();
            }
        }
        return localAddress;
    }

    public IPPort getRemoteAddress(boolean v4) throws IOException {
        if (remoteAddress != null) {
            if (v4) {
                if (remoteAddress.getAddress() instanceof IPv4) {
                    return remoteAddress;
                }
                throw new IOException(this + " is not an ipv4 socket");
            } else {
                if (remoteAddress.getAddress() instanceof IPv6) {
                    return remoteAddress;
                }
                throw new IOException(this + " is not an ipv6 socket");
            }
        }
        try (var allocator = Allocator.ofConfined()) {
            if (v4) {
                var st = PosixNative.get().getIPv4Remote(VProxyThread.current().getEnv(),
                    (int) fd.MEMORY.address(), allocator);
                var addr = new SocketAddressIPv4(st.getIp(), st.getPort() & 0xffff);
                remoteAddress = addr.toIPPort();
            } else {
                var st = PosixNative.get().getIPv6Remote(VProxyThread.current().getEnv(),
                    (int) fd.MEMORY.address(), allocator);
                var addr = new SocketAddressIPv6(st.getIp(), st.getPort() & 0xffff);
                remoteAddress = addr.toIPPort();
            }
        }
        return remoteAddress;
    }

    WinIOCP.Notification pollNotification() {
        return notifications.poll();
    }

    void postNotification(WinIOCP.Notification n) {
        if (isClosed()) {
            assert Logger.lowLevelDebug(this + " is closed, while pending io operation is done");
            decrIORefCnt();
        } else {
            notifications.add(n);
        }
    }
}
