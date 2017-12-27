package reso.examples.lsp_routing;

import com.sun.tools.javac.code.Attribute;
import org.omg.PortableServer.POA;
import reso.common.AbstractApplication;
import reso.common.Interface;
import reso.common.InterfaceAttrListener;
import reso.ip.*;

import java.util.ArrayList;

public class LSPRoutingProtocol
        extends AbstractApplication
        implements IPInterfaceListener, InterfaceAttrListener {

    public static final String PROTOCOL_LSP_NAME = "LSP_ROUTING";
    public static final String PROTOCOL_HELLO_NAME = "=HELLO_ROUTING";
    public static final int IP_PROTO_LSP = Datagram.allocateProtocolNumber(PROTOCOL_LSP_NAME);
    public static final int IP_PROTO_HELLO = Datagram.allocateProtocolNumber(PROTOCOL_HELLO_NAME);

    private final IPLayer ip;
    private final boolean advertise;

    public LSPRoutingProtocol(IPRouter router, boolean advertise){
        super(router, PROTOCOL_LSP_NAME);
        this.ip = router.getIPLayer();
        this.advertise = advertise;
    }

    @Override
    public void start() throws Exception {
        // Register listener for datagrams with HELLO routing messages
        ip.addListener(IP_PROTO_HELLO, this);

        for(IPInterfaceAdapter iface: ip.getInterfaces())
            iface.addListener(this);

        //Send Hello message first
        for(IPInterfaceAdapter iface: ip.getInterfaces()){
            if(iface instanceof IPLoopbackAdapter)
                    continue;
            HelloMessage hm = new HelloMessage();
            hm.addHello(getRouterID(), new ArrayList<IPAddress>());
            Datagram d = new Datagram(iface.getAddress(), IPAddress.BROADCAST, IP_PROTO_HELLO, 1, hm);
            System.out.println(Constants._I+Constants.SEND(getRouterID().toString(), iface.toString(), d.toString()));
            iface.send(d, null);
        }
    }

    @Override
    public void stop() {

    }

    @Override
    public void attrChanged(Interface iface, String attr) {

    }

    @Override
    public void receive(IPInterfaceAdapter src, Datagram datagram) throws Exception {
        if(datagram.getProtocol() == IP_PROTO_HELLO){
            System.out.println(Constants._I+Constants.RECEIVE(getRouterID().toString(), src.toString(), datagram.toString()));
            // Here, we have to check if the source ip that sent the message is not known id neighbours list and add it if not.
            // And resent Hello messsage with Neighnours list complete
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
