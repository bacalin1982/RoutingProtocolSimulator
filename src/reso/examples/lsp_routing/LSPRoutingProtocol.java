package reso.examples.lsp_routing;


import reso.common.AbstractApplication;
import reso.common.Interface;
import reso.common.InterfaceAttrListener;
import reso.common.Message;
import reso.ip.*;

import java.util.ArrayList;
import java.util.List;

public class LSPRoutingProtocol
        extends AbstractApplication
        implements IPInterfaceListener, InterfaceAttrListener {

    public static final String PROTOCOL_LSP_NAME = "LSP_ROUTING";
    public static final int IP_PROTO_LSP = Datagram.allocateProtocolNumber(PROTOCOL_LSP_NAME);

    private final IPRouter router;
    private final IPLayer ip;

    //All ip of network
    private List<IPAddress> neighbours = new ArrayList<>();

    public LSPRoutingProtocol(IPRouter router){
        super(router, PROTOCOL_LSP_NAME);
        this.router = router;
        this.ip = router.getIPLayer();
    }

    @Override
    public void start() throws Exception {
        // Register listener for datagrams with HELLO routing messages
        ip.addListener(IP_PROTO_LSP, this);

        for(IPInterfaceAdapter iface: ip.getInterfaces())
            iface.addListener(this);

        //Send Hello message first
        for(IPInterfaceAdapter iface: ip.getInterfaces()){
            if(iface instanceof IPLoopbackAdapter)
                continue;
            HelloMessage hm = new HelloMessage(getRouterID(), neighbours);
            Datagram d = new Datagram(iface.getAddress(), IPAddress.BROADCAST, IP_PROTO_LSP, 1, hm);
            System.out.println(Constants._I+Constants.SEND(getRouterID().toString(), iface.toString(), d.toString()));
            iface.send(d, null);
        }
    }

    @Override
    public void stop() {
        this.ip.removeListener(IP_PROTO_LSP, this);
        for (IPInterfaceAdapter iface: ip.getInterfaces())
            iface.removeListener(this);
    }

    @Override
    public void attrChanged(Interface iface, String attr) {

    }

    @Override
    public void receive(IPInterfaceAdapter src, Datagram datagram) throws Exception {
        Message msg = datagram.getPayload();
        if(msg instanceof HelloMessage){
            HelloMessage hm = (HelloMessage)msg;
            if(!neighbours.contains(hm.getOrigin())){
                neighbours.add(hm.getOrigin());
            }
            System.out.println(Constants._I+Constants.RECEIVE(getRouterID().toString(), src.toString(), datagram.toString()));
        }
    }

    private IPAddress getRouterID() {
        IPAddress routerID= null;
        for (IPInterfaceAdapter iface: ip.getInterfaces()) {
            IPAddress addr= iface.getAddress();
            if (routerID == null)
                routerID= addr;
            else if (routerID.compareTo(addr) < 0)
                routerID= addr;
        }
        return routerID;
    }
}