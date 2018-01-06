package reso.examples.lsp_routing;

import reso.examples.dv_routing.DVMessage;
import reso.examples.dv_routing.DVRoutingProtocol;
import reso.ip.IPAddress;
import reso.ip.IPInterfaceAdapter;
import reso.ip.IPRouteEntry;

public class LSPRoutingEntry extends IPRouteEntry {

    public final LSPMessage lsp;

    public LSPRoutingEntry(IPAddress dst, IPInterfaceAdapter oif, LSPMessage lsp) {
        super(dst, oif, LSPRoutingProtocol.PROTOCOL_LSP_NAME);
        this.lsp= lsp;
    }

    public String toString() {
        String s= super.toString();
        s+= ", metric=" + lsp.getAdjRouterIPList().get(dst);
        return s;
    }
}
