package io.vproxy.fubuki;

import io.vproxy.pni.*;
import io.vproxy.pni.hack.*;
import io.vproxy.pni.array.*;
import java.lang.foreign.*;
import java.lang.invoke.*;
import java.nio.ByteBuffer;
import io.vproxy.pni.graal.*;
import org.graalvm.nativeimage.*;
import org.graalvm.nativeimage.c.function.*;
import org.graalvm.nativeimage.c.type.VoidPointer;
import org.graalvm.word.WordFactory;

public class FubukiStartOptions extends AbstractNativeObject implements NativeObject {
    public static final MemoryLayout LAYOUT = MemoryLayout.structLayout(
        ValueLayout.ADDRESS.withName("ctx"),
        ValueLayout.ADDRESS.withName("nodeConfigJson"),
        ValueLayout.JAVA_INT.withName("deviceIndex"),
        MemoryLayout.sequenceLayout(4L, ValueLayout.JAVA_BYTE) /* padding */,
        ValueLayout.ADDRESS.withName("fnOnPacket"),
        ValueLayout.ADDRESS.withName("fnAddAddr"),
        ValueLayout.ADDRESS.withName("fnDeleteAddr"),
        ValueLayout.JAVA_INT.withName("tunFd"),
        MemoryLayout.sequenceLayout(4L, ValueLayout.JAVA_BYTE) /* padding */,
        ValueLayout.JAVA_LONG.withName("flags")
    ).withByteAlignment(8);
    public final MemorySegment MEMORY;

    @Override
    public MemorySegment MEMORY() {
        return MEMORY;
    }

    private static final VarHandleW ctxVH = VarHandleW.of(
        LAYOUT.varHandle(
            MemoryLayout.PathElement.groupElement("ctx")
        )
    );

    public MemorySegment getCtx() {
        var SEG = ctxVH.getMemorySegment(MEMORY);
        if (SEG.address() == 0) return null;
        return SEG;
    }

    public void setCtx(MemorySegment ctx) {
        if (ctx == null) {
            ctxVH.set(MEMORY, MemorySegment.NULL);
        } else {
            ctxVH.set(MEMORY, ctx);
        }
    }

    private static final VarHandleW nodeConfigJsonVH = VarHandleW.of(
        LAYOUT.varHandle(
            MemoryLayout.PathElement.groupElement("nodeConfigJson")
        )
    );

    public PNIString getNodeConfigJson() {
        var SEG = nodeConfigJsonVH.getMemorySegment(MEMORY);
        if (SEG.address() == 0) return null;
        return new PNIString(SEG);
    }

    public void setNodeConfigJson(String nodeConfigJson, Allocator ALLOCATOR) {
        this.setNodeConfigJson(new PNIString(ALLOCATOR, nodeConfigJson));
    }

    public void setNodeConfigJson(PNIString nodeConfigJson) {
        if (nodeConfigJson == null) {
            nodeConfigJsonVH.set(MEMORY, MemorySegment.NULL);
        } else {
            nodeConfigJsonVH.set(MEMORY, nodeConfigJson.MEMORY);
        }
    }

    private static final VarHandleW deviceIndexVH = VarHandleW.of(
        LAYOUT.varHandle(
            MemoryLayout.PathElement.groupElement("deviceIndex")
        )
    );

    public int getDeviceIndex() {
        return deviceIndexVH.getInt(MEMORY);
    }

    public void setDeviceIndex(int deviceIndex) {
        deviceIndexVH.set(MEMORY, deviceIndex);
    }

    private static final VarHandleW fnOnPacketVH = VarHandleW.of(
        LAYOUT.varHandle(
            MemoryLayout.PathElement.groupElement("fnOnPacket")
        )
    );

    public MemorySegment getFnOnPacket() {
        var SEG = fnOnPacketVH.getMemorySegment(MEMORY);
        if (SEG.address() == 0) return null;
        return SEG;
    }

    public void setFnOnPacket(MemorySegment fnOnPacket) {
        if (fnOnPacket == null) {
            fnOnPacketVH.set(MEMORY, MemorySegment.NULL);
        } else {
            fnOnPacketVH.set(MEMORY, fnOnPacket);
        }
    }

    private static final VarHandleW fnAddAddrVH = VarHandleW.of(
        LAYOUT.varHandle(
            MemoryLayout.PathElement.groupElement("fnAddAddr")
        )
    );

    public MemorySegment getFnAddAddr() {
        var SEG = fnAddAddrVH.getMemorySegment(MEMORY);
        if (SEG.address() == 0) return null;
        return SEG;
    }

    public void setFnAddAddr(MemorySegment fnAddAddr) {
        if (fnAddAddr == null) {
            fnAddAddrVH.set(MEMORY, MemorySegment.NULL);
        } else {
            fnAddAddrVH.set(MEMORY, fnAddAddr);
        }
    }

    private static final VarHandleW fnDeleteAddrVH = VarHandleW.of(
        LAYOUT.varHandle(
            MemoryLayout.PathElement.groupElement("fnDeleteAddr")
        )
    );

    public MemorySegment getFnDeleteAddr() {
        var SEG = fnDeleteAddrVH.getMemorySegment(MEMORY);
        if (SEG.address() == 0) return null;
        return SEG;
    }

    public void setFnDeleteAddr(MemorySegment fnDeleteAddr) {
        if (fnDeleteAddr == null) {
            fnDeleteAddrVH.set(MEMORY, MemorySegment.NULL);
        } else {
            fnDeleteAddrVH.set(MEMORY, fnDeleteAddr);
        }
    }

    private static final VarHandleW tunFdVH = VarHandleW.of(
        LAYOUT.varHandle(
            MemoryLayout.PathElement.groupElement("tunFd")
        )
    );

    public int getTunFd() {
        return tunFdVH.getInt(MEMORY);
    }

    public void setTunFd(int tunFd) {
        tunFdVH.set(MEMORY, tunFd);
    }

    private static final VarHandleW flagsVH = VarHandleW.of(
        LAYOUT.varHandle(
            MemoryLayout.PathElement.groupElement("flags")
        )
    );

    public long getFlags() {
        return flagsVH.getLong(MEMORY);
    }

    public void setFlags(long flags) {
        flagsVH.set(MEMORY, flags);
    }

    public FubukiStartOptions(MemorySegment MEMORY) {
        MEMORY = MEMORY.reinterpret(LAYOUT.byteSize());
        this.MEMORY = MEMORY;
        long OFFSET = 0;
        OFFSET += ValueLayout.ADDRESS_UNALIGNED.byteSize();
        OFFSET += 8;
        OFFSET += ValueLayout.JAVA_INT_UNALIGNED.byteSize();
        OFFSET += 4; /* padding */
        OFFSET += ValueLayout.ADDRESS_UNALIGNED.byteSize();
        OFFSET += ValueLayout.ADDRESS_UNALIGNED.byteSize();
        OFFSET += ValueLayout.ADDRESS_UNALIGNED.byteSize();
        OFFSET += ValueLayout.JAVA_INT_UNALIGNED.byteSize();
        OFFSET += 4; /* padding */
        OFFSET += ValueLayout.JAVA_LONG_UNALIGNED.byteSize();
    }

    public FubukiStartOptions(Allocator ALLOCATOR) {
        this(ALLOCATOR.allocate(LAYOUT));
    }

    @Override
    public void toString(StringBuilder SB, int INDENT, java.util.Set<NativeObjectTuple> VISITED, boolean CORRUPTED_MEMORY) {
        if (!VISITED.add(new NativeObjectTuple(this))) {
            SB.append("<...>@").append(Long.toString(MEMORY.address(), 16));
            return;
        }
        SB.append("FubukiStartOptions{\n");
        {
            SB.append(" ".repeat(INDENT + 4)).append("ctx => ");
            SB.append(PanamaUtils.memorySegmentToString(getCtx()));
        }
        SB.append(",\n");
        {
            SB.append(" ".repeat(INDENT + 4)).append("nodeConfigJson => ");
            if (CORRUPTED_MEMORY) SB.append("<?>");
            else PanamaUtils.nativeObjectToString(getNodeConfigJson(), SB, INDENT + 4, VISITED, CORRUPTED_MEMORY);
        }
        SB.append(",\n");
        {
            SB.append(" ".repeat(INDENT + 4)).append("deviceIndex => ");
            SB.append(getDeviceIndex());
        }
        SB.append(",\n");
        {
            SB.append(" ".repeat(INDENT + 4)).append("fnOnPacket => ");
            SB.append(PanamaUtils.memorySegmentToString(getFnOnPacket()));
        }
        SB.append(",\n");
        {
            SB.append(" ".repeat(INDENT + 4)).append("fnAddAddr => ");
            SB.append(PanamaUtils.memorySegmentToString(getFnAddAddr()));
        }
        SB.append(",\n");
        {
            SB.append(" ".repeat(INDENT + 4)).append("fnDeleteAddr => ");
            SB.append(PanamaUtils.memorySegmentToString(getFnDeleteAddr()));
        }
        SB.append(",\n");
        {
            SB.append(" ".repeat(INDENT + 4)).append("tunFd => ");
            SB.append(getTunFd());
        }
        SB.append(",\n");
        {
            SB.append(" ".repeat(INDENT + 4)).append("flags => ");
            SB.append(getFlags());
        }
        SB.append("\n");
        SB.append(" ".repeat(INDENT)).append("}@").append(Long.toString(MEMORY.address(), 16));
    }

    public static class Array extends RefArray<FubukiStartOptions> {
        public Array(MemorySegment buf) {
            super(buf, FubukiStartOptions.LAYOUT);
        }

        public Array(Allocator allocator, long len) {
            super(allocator, FubukiStartOptions.LAYOUT, len);
        }

        public Array(PNIBuf buf) {
            super(buf, FubukiStartOptions.LAYOUT);
        }

        @Override
        protected void elementToString(io.vproxy.fubuki.FubukiStartOptions ELEM, StringBuilder SB, int INDENT, java.util.Set<NativeObjectTuple> VISITED, boolean CORRUPTED_MEMORY) {
            ELEM.toString(SB, INDENT, VISITED, CORRUPTED_MEMORY);
        }

        @Override
        protected String toStringTypeName() {
            return "FubukiStartOptions.Array";
        }

        @Override
        protected FubukiStartOptions construct(MemorySegment seg) {
            return new FubukiStartOptions(seg);
        }

        @Override
        protected MemorySegment getSegment(FubukiStartOptions value) {
            return value.MEMORY;
        }
    }

    public static class Func extends PNIFunc<FubukiStartOptions> {
        private Func(io.vproxy.pni.CallSite<FubukiStartOptions> func) {
            super(func);
        }

        private Func(io.vproxy.pni.CallSite<FubukiStartOptions> func, Options opts) {
            super(func, opts);
        }

        private Func(MemorySegment MEMORY) {
            super(MEMORY);
        }

        public static Func of(io.vproxy.pni.CallSite<FubukiStartOptions> func) {
            return new Func(func);
        }

        public static Func of(io.vproxy.pni.CallSite<FubukiStartOptions> func, Options opts) {
            return new Func(func, opts);
        }

        public static Func of(MemorySegment MEMORY) {
            return new Func(MEMORY);
        }

        @Override
        protected String toStringTypeName() {
            return "FubukiStartOptions.Func";
        }

        @Override
        protected FubukiStartOptions construct(MemorySegment seg) {
            return new FubukiStartOptions(seg);
        }
    }
}
// metadata.generator-version: pni 22.0.0.20
// sha256:122d6b21df1006333490153208ea833dc1166789f4a6dcd051d7d7aed2bec9c8
