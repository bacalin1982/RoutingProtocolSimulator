package reso.examples.lsp_routing;


import reso.common.*;
import reso.ip.*;
import reso.scheduler.AbstractScheduler;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LSPRoutingProtocol extends AbstractApplication implements IPInterfaceListener, InterfaceAttrListener {

    public static boolean verbose = false;

    public static final String PROTOCOL_LSP_NAME = "LSP_ROUTING";
    public static final int IP_PROTO_LSP = Datagram.allocateProtocolNumber(PROTOCOL_LSP_NAME);

    private final IPRouter router;
    private final IPLayer ip;

    private int intervalHello;
    private int intervalLSP;
    private int intervalDijkstra;
    private AbstractScheduler scheduler;

    //Hello
    private Map<IPAddress, Integer> neighbors   = new HashMap<>();
    private Map<IPAddress, HelloMessage> hmMap  = new HashMap<>();//using for debug

    //LSP
    private Map<IPAddress, LSPMessage> LSDB = new HashMap<>();

    /* Constructor */
    public LSPRoutingProtocol(IPRouter router, int intervalHello, int intervalLSP, AbstractScheduler scheduler) {
        super(router, PROTOCOL_LSP_NAME);
        this.router = router;
        this.ip = router.getIPLayer();
        this.intervalHello = intervalHello;
        this.intervalLSP = intervalLSP;
        this.intervalDijkstra = 5;
        this.scheduler = scheduler;
    }

    /* Override Methods */

    @Override
    public void start() throws Exception {
        log(Constants._I + Constants.START(PROTOCOL_LSP_NAME, this.router.toString()));

        // Register listener for LSP protocol
        ip.addListener(IP_PROTO_LSP, this);

        // Register listener for eth
        for (IPInterfaceAdapter iface : ip.getInterfaces()) {
            iface.addListener(this);
            log(Constants._I+Constants.ETH(iface.getName(), iface.getAddress().toString()));
        }

        //Hello Message - Launching neighbours discorvery
        AbstractTimer helloTimer = new AbstractTimer(scheduler, intervalHello, true) {
            @Override
            protected void run() throws Exception {
                log(Constants._I+Constants.HELLO_LOG(router.toString()));
                sendHello();
                //Timer
                Thread.sleep(intervalHello*300);
            }
        };

        //LSP Message - Launching flooding each neighbour
        AbstractTimer lspTimer = new AbstractTimer(scheduler, intervalLSP, true) {
            @Override
            protected void run() throws Exception {
                log(Constants._I+Constants.LSP_LOG(router.toString()));
                sendLSP(null, new LSPMessage(getRouterID(), 0, neighbors, null));
                //Timer
                Thread.sleep(intervalLSP*300);
            }
        };


        AbstractTimer dijkstraTimer = new AbstractTimer(scheduler, this.intervalDijkstra, true) {
            @Override
            protected void run() throws Exception {
                shortestTravelComputation();
                Thread.sleep(intervalDijkstra*300);
            }
        };

        AbstractTimer debugTimer = new AbstractTimer(scheduler, 10, true) {
            @Override
            protected void run() throws Exception {

                log(Constants._I +" ----------------------");

                //Hello debug
                log(Constants._I + Constants.HELLO_DEBUG(router.toString()));
                for(IPAddress neighbour: neighbors.keySet()){
                    HelloMessage hm = hmMap.get(neighbour);
                    log(Constants._I + Constants.NEIGHBOUR_DEBUG(hm.getRouter().toString(), hm.getRouterIp().toString(), hm.getRouterEth().toString(), neighbors.get(neighbour)));
                }

                //LSDB debug
                log(Constants._I + Constants.LSDB_LOG(router.toString()));
                for(LSPMessage lsp: LSDB.values()){
                    log(Constants._I+Constants.LSDB_LSP_LOG(lsp.toString()));
                }

                Thread.sleep(10*300);
            }
        };

        helloTimer.start();
        lspTimer.start();
        debugTimer.start();
        dijkstraTimer.start();

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
            //Hello message
            HelloMessage hm = (HelloMessage) msg;
            //Add neighbour to router
            this.neighbors.put(hm.getRouterIp(), src.getMetric());
            this.hmMap.put(hm.getRouterIp(), hm);
            //Debug
            debug(Constants._I + Constants.RECEIVE(router.toString(), getRouterID().toString(), src.toString(), "HELLO", datagram.toString()));

        } else if (msg instanceof LSPMessage) {
            //LSP message
            LSPMessage lspMsg = (LSPMessage) msg;
            //Debug
            debug(Constants._I + Constants.RECEIVE(router.toString(), getRouterID().toString(), src.toString(), "LSP", datagram.toString()));
            //Check if exist
            if(!this.LSDB.containsKey(lspMsg.getRouterIP())){
                //Add to LSDB
                this.LSDB.put(lspMsg.getRouterIP(), lspMsg);
                //Resend to next neighbors
                sendLSP(src, lspMsg);
            }
        }
    }

    /* Private Methods */

    /**
     * Show log on console if verbose attribute is true
     * @param log
     */
    private void log(String log){
        System.out.println(log);
    }

    /**
     * Show debug on console if verbose attribute is true
     * @param debug
     */
    private void debug(String debug){
        if(verbose){
            System.out.println(debug);
        }
    }

    /**
     * Get router IP
     * @return IPAddress
     */
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
                //Create hello message
                HelloMessage hm = new HelloMessage(this.router, getRouterID(), iface);
                //Create datagram
                Datagram d = new Datagram(iface.getAddress(), IPAddress.BROADCAST, IP_PROTO_LSP, 1, hm);
                //Debug
                debug(Constants._I + Constants.SEND(router.toString(), getRouterID().toString(), iface.toString(), "HELLO", d.toString()));
                //Send
                iface.send(d, null);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void sendLSP(IPInterfaceAdapter src, LSPMessage lspMsg) throws Exception {
        try {
            for (IPInterfaceAdapter iface : ip.getInterfaces()) {
                if (iface instanceof IPLoopbackAdapter || iface.equals(src))
                    continue;
                //Create datagram
                Datagram d = new Datagram(iface.getAddress(), IPAddress.BROADCAST, IP_PROTO_LSP, 1, lspMsg);
                //Debug
                debug(Constants._I + Constants.SEND(router.toString(), getRouterID().toString(), iface.toString(), "LSP", d.toString()));
                //Send
                iface.send(d, null);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

   private Map<IPAddress, Integer> shortestTravelComputation(){
        Map<IPAddress, Integer> bestRoute = new HashMap<>();
        Point[] points;
        int nbPoints;
        List<Link> links = new ArrayList<Link>();
        int nbLinks;

        for(LSPMessage lspMsg: LSDB.values()){
            for(IPAddress ip: lspMsg.getAdjRouterIPList().keySet()){
                links.add(new Link(lspMsg.getRouterIP(), ip, lspMsg.getAdjRouterIPList().get(ip)));
                //System.out.println(getRouterID()+" to "+ip.toString()+" cost "+this.metric.get(ip));
            }
        }

        if(! links.isEmpty()){
            Graph g = new Graph(links);
            g.computeShortestDistance();
            g.printResult();
        }

        return bestRoute;
    }


}