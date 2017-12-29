package reso.examples.lsp_routing;


import reso.common.*;
import reso.ip.*;
import reso.scheduler.AbstractScheduler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LSPRoutingProtocol extends AbstractApplication implements IPInterfaceListener, InterfaceAttrListener {

    public static final String PROTOCOL_LSP_NAME = "LSP_ROUTING";
    public static final int IP_PROTO_LSP = Datagram.allocateProtocolNumber(PROTOCOL_LSP_NAME);

    private final IPRouter router;
    private final IPLayer ip;

    private int intervalHello;
    private int intervalLSP;
    private AbstractScheduler scheduler;

    //All ip of network
    private List<IPAddress> neighbours = new ArrayList<>();
    private Map<IPAddress, LSPMessage> LSDB = new HashMap<>();
    private Map<IPAddress, Integer> metric = new HashMap<>();

    /* Constructor */
    public LSPRoutingProtocol(IPRouter router, int intervalHello, int intervalLSP, AbstractScheduler scheduler) {
        super(router, PROTOCOL_LSP_NAME);
        this.router = router;
        this.ip = router.getIPLayer();
        this.intervalHello = intervalHello;
        this.intervalLSP = intervalLSP;
        this.scheduler = scheduler;
    }

    /* Override Methods */
    @Override
    public void start() throws Exception {
        // Register listener for datagrams with HELLO routing messages
        ip.addListener(IP_PROTO_LSP, this);

        for (IPInterfaceAdapter iface : ip.getInterfaces())
            iface.addListener(this);

        //Launching neighbours discorvery
        AbstractTimer helloTimer = new AbstractTimer(scheduler, intervalHello, false) {
            @Override
            protected void run() throws Exception {
                sendHello();
                //Thread.sleep(intervalHello*1000);
            }
        };

        //Send LSP message
        AbstractTimer lspTimer = new AbstractTimer(scheduler, intervalLSP, false) {
            @Override
            protected void run() throws Exception {
                LSDB.put(getRouterID(), makeLSP(getRouterID(), (IPInterfaceAdapter) router.getInterfaceByName("lo")));
                sendLSP(null, makeLSP(getRouterID(), null));
                //Thread.sleep(intervalLSP*1000);
            }
        };

        helloTimer.start();
        lspTimer.start();
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
            System.out.println(Constants._I + Constants.RECEIVE(router.toString(), getRouterID().toString(), src.toString(), "HELLO", datagram.toString()));
            HelloMessage hm = (HelloMessage) msg;
            if (!this.neighbours.contains(hm.getOrigin())) {
                this.neighbours.add(hm.getOrigin());
                this.metric.put(hm.getOrigin(), src.getMetric());
            }
        } else if (msg instanceof LSPMessage) {
            System.out.println(Constants._I + Constants.RECEIVE(router.toString(), getRouterID().toString(), src.toString(), "LSP", datagram.toString()));
            LSPMessage lspMsg = (LSPMessage) msg;
            lspMsg.addInLSDB(lspMsg.getOrigin(), metric.get(lspMsg.getOrigin()));
            this.LSDB.put(datagram.src, lspMsg);
            System.out.println(this.LSDB);
            sendLSP(src, lspMsg);
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

    private void sendHello(){
        try{
            for (IPInterfaceAdapter iface : this.ip.getInterfaces()) {
                if (iface instanceof IPLoopbackAdapter)
                    continue;
                HelloMessage hm = new HelloMessage(getRouterID(), this.neighbours);
                Datagram d = new Datagram(iface.getAddress(), IPAddress.BROADCAST, IP_PROTO_LSP, 1, hm);
                System.out.println(Constants._I + Constants.SEND(router.toString(), getRouterID().toString(), iface.toString(), "HELLO", d.toString()));
                iface.send(d, null);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private LSPMessage makeLSP(IPAddress origin, IPInterfaceAdapter oif) {
        Map<IPAddress, Integer> lsp = new HashMap<>();
        for (IPAddress key : metric.keySet()) {
            lsp.put(key, metric.get(key));
        }
        return new LSPMessage(origin, 0, lsp, oif);
    }

    private void sendLSP(IPInterfaceAdapter src, LSPMessage lspMsg) throws Exception {
        try {
            for (IPInterfaceAdapter iface : ip.getInterfaces()) {
                if (iface instanceof IPLoopbackAdapter || iface.equals(src))
                    continue;
                lspMsg.setOif(iface);
                Datagram d = new Datagram(iface.getAddress(), IPAddress.BROADCAST, IP_PROTO_LSP, 1, lspMsg);
                System.out.println(Constants._I + Constants.SEND(router.toString(), getRouterID().toString(), iface.toString(), "LSP", d.toString()));
                iface.send(d, iface.getAddress());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}