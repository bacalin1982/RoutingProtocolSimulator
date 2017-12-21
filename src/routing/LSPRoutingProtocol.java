package routing;

import reso.common.AbstractApplication;
import reso.common.Interface;
import reso.common.InterfaceAttrListener;
import reso.ip.*;

public class LSPRoutingProtocol
        extends AbstractApplication
        implements IPInterfaceListener, InterfaceAttrListener {

    public static final String PROTOCOL_NAME = "LSP_ROUTING";
    public static final int IP_PROTO_LSP = Datagram.allocateProtocolNumber(PROTOCOL_NAME);

    private final IPLayer ip;
    private final boolean advertise;

    public LSPRoutingProtocol(IPRouter router, boolean advertise){
        super(router, PROTOCOL_NAME);
        this.ip = router.getIPLayer();
        this.advertise = advertise;
    }

    @Override
    public void start() throws Exception {

    }

    @Override
    public void stop() {

    }

    @Override
    public void attrChanged(Interface iface, String attr) {

    }

    @Override
    public void receive(IPInterfaceAdapter src, Datagram datagram) throws Exception {

    }
}
