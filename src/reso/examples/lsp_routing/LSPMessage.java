package reso.examples.lsp_routing;

import reso.common.Message;
import reso.ip.IPAddress;
import reso.ip.IPInterfaceAdapter;

import java.util.HashMap;
import java.util.Map;

public class LSPMessage implements Message {
    private final IPAddress origin;
    private final int numSeq;
    private Map<IPAddress, Integer> lsp = new HashMap<>();
    private IPInterfaceAdapter oif;

    public LSPMessage(IPAddress origin, int numSeq, Map<IPAddress, Integer> lsp, IPInterfaceAdapter oif){
        this.origin = origin;
        this.numSeq = numSeq;
        this.lsp = lsp;
        this.oif = oif;
    }

    public IPAddress getOrigin() {
        return origin;
    }

    public int getNumSeq() {
        return numSeq;
    }

    public IPInterfaceAdapter getOif() {
        return oif;
    }

    public void setOif(IPInterfaceAdapter oif) {
        this.oif = oif;
    }

    public void addInLSDB(IPAddress src, int numSeq){
        this.lsp.put(src, numSeq);
    }

    @Override
    public String toString() {
        String lsdb = "";
        for(IPAddress key: this.lsp.keySet()){
            //if(origin.equals(key)) {
            lsdb += "(" + key + "; " + this.lsp.get(key) + "),";
            //}
        }
        return "LSP[FROM="+origin+" ; NUMSEQ="+numSeq+" ; LSDB={"+lsdb+"}]";
    }
}
