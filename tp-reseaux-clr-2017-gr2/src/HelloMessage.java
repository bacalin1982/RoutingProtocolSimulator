package reso.examples.lsp_routing;

import reso.common.Message;
import reso.ip.IPAddress;
import reso.ip.IPInterfaceAdapter;
import reso.ip.IPRouter;

import java.util.List;

public class HelloMessage implements Message {

    private final IPRouter              router;
    private final IPAddress             routerIp;
    private final IPInterfaceAdapter    routerEth;

    public HelloMessage(IPRouter router, IPAddress routerIp, IPInterfaceAdapter routerEth) {
        this.router     = router;
        this.routerIp   = routerIp;
        this.routerEth  = routerEth;
    }

    public IPRouter getRouter() {
        return router;
    }

    public IPAddress getRouterIp() {
        return routerIp;
    }

    public IPInterfaceAdapter getRouterEth() {
        return routerEth;
    }

    public String toString() {
        return "HELLO[R="+getRouter()+"; FROM="+getRouterIp()+"; ETH="+getRouterEth()+"]";
    }
}
