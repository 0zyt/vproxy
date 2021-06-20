package vproxy.vswitch;

import vproxy.base.util.ByteArray;
import vproxy.vfd.IP;
import vproxy.vpacket.*;
import vproxy.vpacket.conntrack.tcp.TcpEntry;
import vproxy.vswitch.iface.Iface;

import java.util.Collection;

public class SocketBuffer {
    public static final int FLAG_VXLAN = 0x00000001;
    public static final int FLAG_IP = 0x00000004;

    public static SocketBuffer fromVXLanBytes(Iface devin, ByteArray buf, int pktOff, int pad) {
        return new SocketBuffer(devin, buf, pktOff, pad);
    }

    public static SocketBuffer fromEtherBytes(Iface devin, int vni, ByteArray buf, int pktOff, int pad) {
        return new SocketBuffer(devin, vni, buf, pktOff, pad);
    }

    public static SocketBuffer fromPacket(VXLanPacket pkt) {
        return new SocketBuffer(pkt);
    }

    public static SocketBuffer fromPacket(Table table, AbstractEthernetPacket pkt) {
        return new SocketBuffer(table, pkt);
    }

    public static SocketBuffer fromPacket(Table table, AbstractIpPacket pkt) {
        return new SocketBuffer(table, pkt);
    }

    // ----- context -----
    public Iface devin; // not null if it's an input packet
    public int vni; // vni or vlan number, must always be valid
    public Table table; // might be null
    public int flags;

    // ----- buffer -----
    public ByteArray buf;
    public int pktOff; // packet offset
    public int pad; // padding length after the end of the packet
    public ByteArray pktBuf; // sub buffer of buf

    // ----- packet -----
    public VXLanPacket vxlan;
    public AbstractEthernetPacket pkt; // not null if it's an input packet
    public AbstractIpPacket ipPkt;
    public TcpPacket tcpPkt;

    // ----- helper fields -----
    // l3
    public Collection<IP> matchedIps;
    // l4
    public TcpEntry tcp = null;
    public boolean needTcpReset = false; // this field is only used in L4.input

    private SocketBuffer(Iface devin, ByteArray buf, int pktOff, int pad) {
        this.devin = devin;
        this.flags = FLAG_VXLAN;
        this.buf = buf;
        this.pktOff = pktOff;
        this.pad = pad;
        if (pktOff == 0 && pad == 0) {
            this.pktBuf = buf;
        } else {
            this.pktBuf = buf.sub(pktOff, buf.length() - pad - pktOff);
        }
    }

    private SocketBuffer(Iface devin, int vni, ByteArray buf, int pktOff, int pad) {
        this.devin = devin;
        this.vni = vni;
        this.flags = 0;
        this.buf = buf;
        this.pktOff = pktOff;
        this.pad = pad;
        if (pktOff == 0 && pad == 0) {
            this.pktBuf = buf;
        } else {
            this.pktBuf = buf.sub(pktOff, buf.length() - pad - pktOff);
        }
    }

    private SocketBuffer(VXLanPacket pkt) {
        this.devin = null;
        this.vni = pkt.getVni();
        this.flags = FLAG_VXLAN;
        this.vxlan = pkt;
        initPackets(true, false, false);
    }

    private SocketBuffer(Table table, AbstractEthernetPacket pkt) {
        this.devin = null;
        this.vni = table.vni;
        this.table = table;
        this.flags = 0;
        this.pkt = pkt;
        initPackets(false, true, false);
    }

    private SocketBuffer(Table table, AbstractIpPacket pkt) {
        this.devin = null;
        this.vni = table.vni;
        this.table = table;
        this.flags = FLAG_IP;
        this.ipPkt = pkt;
        initPackets(false, false, true);
    }

    @SuppressWarnings("ConstantConditions")
    public String init() {
        AbstractPacket pkt;
        if ((flags & FLAG_VXLAN) == FLAG_VXLAN) {
            pkt = new VXLanPacket();
        } else {
            pkt = new EthernetPacket();
        }
        String err = pkt.from(pktBuf);
        if (err != null) {
            return err;
        }
        if ((flags & FLAG_VXLAN) == FLAG_VXLAN) {
            this.vxlan = (VXLanPacket) pkt;
            this.vni = this.vxlan.getVni();
            initPackets(true, false, false);
        } else {
            this.pkt = (AbstractEthernetPacket) pkt;
            initPackets(false, true, false);
        }
        return null;
    }

    public void clearBuffers() {
        this.buf = null;
        this.pktOff = 0;
        this.pad = 0;
        this.pktBuf = null;
    }

    public void clearHelperFields() {
        matchedIps = null;
        tcp = null;
        needTcpReset = false;
    }

    public void replacePacket(EthernetPacket pkt) {
        clearBuffers();
        clearHelperFields();
        this.flags = 0;
        this.pkt = pkt;
        initPackets(false, true, false);
    }

    public void replacePacket(AbstractIpPacket pkt) {
        clearBuffers();
        clearHelperFields();
        this.flags = FLAG_IP;
        this.ipPkt = pkt;
        initPackets(false, false, true);
    }

    private void initPackets(boolean hasVxlan, boolean hasEther, boolean hasIp) {
        if (hasVxlan) {
            this.pkt = vxlan.getPacket();
            hasEther = true;
        } else {
            this.vxlan = null;
        }
        if (hasEther) {
            if (pkt.getPacket() instanceof AbstractIpPacket) {
                this.ipPkt = (AbstractIpPacket) pkt.getPacket();
                hasIp = true;
            } else {
                this.ipPkt = null;
                hasIp = false;
            }
        } else {
            this.pkt = null;
        }
        if (hasIp) {
            if (ipPkt.getPacket() instanceof TcpPacket) {
                this.tcpPkt = (TcpPacket) ipPkt.getPacket();
            } else {
                this.tcpPkt = null;
            }
        } else {
            this.ipPkt = null;
        }
    }

    public void setTable(Table table) {
        this.vni = table.vni;
        this.table = table;
        if (vxlan != null) {
            vxlan.setVni(table.vni);
        }
    }

    public void executeWithVni(int vni, Runnable r) {
        if (this.vni == vni) {
            r.run();
            return;
        }

        Table tableBackup = this.table;

        this.vni = vni;
        this.table = null;
        if (this.vxlan != null) {
            this.vxlan.setVni(vni);
        }

        r.run();

        setTable(tableBackup);
    }

    public void recordMatchedIps(Collection<IP> matchedIps) {
        this.matchedIps = matchedIps;
    }

    @Override
    public String toString() {
        AbstractPacket pkt = this.pkt;
        if (pkt == null) {
            pkt = ipPkt;
        }
        if (pkt == null) {
            pkt = tcpPkt;
        }
        return "SocketBuffer{" +
            "in=" + devin +
            ", vni=" + vni +
            ", pktBuf=" + (pktBuf == null ? "" : pktBuf.toHexString()) +
            ", pkt=" + (pkt == null ? "" : pkt.description()) +
            '}';
    }
}
