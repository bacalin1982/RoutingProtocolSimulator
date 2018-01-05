package reso.examples.lsp_routing;

import reso.common.Message;
import reso.ip.IPAddress;
import reso.ip.IPInterfaceAdapter;

import java.util.Map;

public class LSPMessage implements Message {
    private final IPAddress routerIP;
    private final int numSeq;
    private final Map<IPAddress, Integer> adjRouterIPList;

    private IPInterfaceAdapter oif;

    public LSPMessage(IPAddress routerIP, int numSeq, Map<IPAddress, Integer> adjRouterIPList, IPInterfaceAdapter oif){
        this.routerIP = routerIP;
        this.numSeq = numSeq;
        this.adjRouterIPList = adjRouterIPList;
        this.oif = oif;
    }

    public IPAddress getRouterIP() {
        return routerIP;
    }

    public int getNumSeq() {
        return numSeq;
    }

    public Map<IPAddress, Integer> getAdjRouterIPList() {
        return adjRouterIPList;
    }

    public IPInterfaceAdapter getOif() {
        return oif;
    }

    public void setOif(IPInterfaceAdapter oif) {
        this.oif = oif;
    }

    @Override
    public String toString() {
        return "LSP[FROM="+getRouterIP()+" ; NUMSEQ="+getNumSeq()+" ; ADJROUTER={"+getAdjRouterIPList()+"}]";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LSPMessage)) return false;

        LSPMessage that = (LSPMessage) o;

        if (getNumSeq() != that.getNumSeq()) return false;
        if (getRouterIP() != null ? !getRouterIP().equals(that.getRouterIP()) : that.getRouterIP() != null)
            return false;
        return getAdjRouterIPList() != null ? getAdjRouterIPList().equals(that.getAdjRouterIPList()) : that.getAdjRouterIPList() == null;
    }

    @Override
    public int hashCode() {
        int result = getRouterIP() != null ? getRouterIP().hashCode() : 0;
        result = 31 * result + getNumSeq();
        result = 31 * result + (getAdjRouterIPList() != null ? getAdjRouterIPList().hashCode() : 0);
        return result;
    }
}
