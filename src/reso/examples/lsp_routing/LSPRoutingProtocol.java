package reso.examples.lsp_routing;


import reso.common.AbstractApplication;
import reso.common.Interface;
import reso.common.InterfaceAttrListener;
import reso.common.Message;
import reso.ip.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LSPRoutingProtocol extends AbstractApplication implements IPInterfaceListener, InterfaceAttrListener {

    public static final String PROTOCOL_LSP_NAME = "LSP_ROUTING";
    public static final int IP_PROTO_LSP = Datagram.allocateProtocolNumber(PROTOCOL_LSP_NAME);

    private final IPRouter router;
    private final IPLayer ip;

    //All ip of network
    private List<IPAddress> neighbours = new ArrayList<>();
    private Map<IPAddress, LSPMessage> LSDB = new HashMap<>();
    private Map<IPAddress, Integer> metric = new HashMap<>();

    /* Constructor */
    public LSPRoutingProtocol(IPRouter router) {
        super(router, PROTOCOL_LSP_NAME);
        this.router = router;
        this.ip = router.getIPLayer();
    }

    /* Override Methods */
    @Override
    public void start() throws Exception {
        // Register listener for datagrams with HELLO routing messages
        ip.addListener(IP_PROTO_LSP, this);

        for (IPInterfaceAdapter iface : ip.getInterfaces())
            iface.addListener(this);

        //Send Hello message first
        for (IPInterfaceAdapter iface : ip.getInterfaces()) {
            if (iface instanceof IPLoopbackAdapter)
                continue;
            HelloMessage hm = new HelloMessage(getRouterID(), neighbours);
            Datagram d = new Datagram(iface.getAddress(), IPAddress.BROADCAST, IP_PROTO_LSP, 1, hm);
            System.out.println(Constants._I + Constants.SEND(getRouterID().toString(), iface.toString(), "HELLO", d.toString()));
            iface.send(d, null);
        }

        //Send LSP message
        sendLSP(null, makeLSP(getRouterID(), null));

    }

    @Override
    public void stop() {
        this.ip.removeListener(IP_PROTO_LSP, this);
        for (IPInterfaceAdapter iface : ip.getInterfaces())
            iface.removeListener(this);
    }

    @Override
    public void attrChanged(Interface iface, String attr) {

    }

    @Override
    public void receive(IPInterfaceAdapter src, Datagram datagram) throws Exception {
        Message msg = datagram.getPayload();
        if (msg instanceof HelloMessage) {
            System.out.println(Constants._I + Constants.RECEIVE(getRouterID().toString(), src.toString(), "HELLO", datagram.toString()));
            HelloMessage hm = (HelloMessage) msg;
            if (!neighbours.contains(hm.getOrigin())) {
                neighbours.add(hm.getOrigin());
                metric.put(hm.getOrigin(), src.getMetric());

                /*for(IPAddress key: metric.keySet()){
                    System.out.println("metric "+key+"["+metric.get(key)+"]");
                }*/
            }
        } else if (msg instanceof LSPMessage) {
            System.out.println(Constants._I + Constants.RECEIVE(getRouterID().toString(), src.toString(), "LSP", datagram.toString()));
            LSPMessage lspMsg = (LSPMessage) msg;
            LSDB.put(datagram.src, lspMsg);
            sendLSP(src, lspMsg);
           /* for(IPInterfaceAdapter iface: ip.getInterfaces()) {
                if (iface instanceof IPLoopbackAdapter)
                    continue;
                LSPMessage newLspMsg = makeLSP(getRouterID(), src);
                Datagram d = new Datagram(iface.getAddress(), IPAddress.BROADCAST, IP_PROTO_LSP, 1, newLspMsg);
                System.out.println(Constants._I+Constants.SEND(getRouterID().toString(), iface.toString(), "LSP", d.toString()));
                iface.send(d, null);
            }*/
        }
    }

    /* Private Methods */
    private IPAddress getRouterID() {
        IPAddress routerID = null;
        for (IPInterfaceAdapter iface : ip.getInterfaces()) {
            IPAddress addr = iface.getAddress();
            if (routerID == null)
                routerID = addr;
            else if (routerID.compareTo(addr) < 0)
                routerID = addr;
        }
        return routerID;
    }

    private LSPMessage makeLSP(IPAddress origin, IPInterfaceAdapter oif) {
        Map<IPAddress, Integer> lsp = new HashMap<>();
        for (IPAddress key : metric.keySet()) {
            lsp.put(key, metric.get(key));
        }
        return new LSPMessage(origin, 0, lsp, oif);
    }

    private void sendLSP(IPInterfaceAdapter src, LSPMessage lspMsg) throws Exception {
        for (IPInterfaceAdapter iface : ip.getInterfaces()) {
            if (iface instanceof IPLoopbackAdapter || iface.equals(src))
                continue;
            lspMsg.setOif(iface);
            Datagram d = new Datagram(iface.getAddress(), IPAddress.BROADCAST, IP_PROTO_LSP, 1, lspMsg);
            System.out.println(Constants._I + Constants.SEND(getRouterID().toString(), iface.toString(), "LSP", d.toString()));
            iface.send(d, null);
        }
    }
}