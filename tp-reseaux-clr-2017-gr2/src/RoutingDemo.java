package reso.examples.lsp_routing;

import reso.common.Network;
import reso.common.Node;
import reso.ip.*;
import reso.scheduler.AbstractScheduler;
import reso.scheduler.Scheduler;
import reso.utilities.NetworkBuilder;

import java.io.*;

public class RoutingDemo {

    public static final String TOPO_FILE= "reso/data/topology.txt";

    private static IPAddress getRouterID(IPLayer ip) {
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

    public static void main(String [] args) {

        String filename= RoutingDemo.class.getClassLoader().getResource(TOPO_FILE).getFile();

        AbstractScheduler scheduler= new Scheduler();
        int intervalHello = 1;
        int intervalLSP = 1;
        try{
            System.out.println(Constants._T);
            Network network= NetworkBuilder.loadTopology(filename, scheduler);

            // Add routing protocol application to each router
            for (Node n: network.getNodes()) {
                if (!(n instanceof IPRouter))
                    continue;
                IPRouter router= (IPRouter) n;
                router.addApplication(new LSPRoutingProtocol(router, intervalHello, intervalLSP, scheduler));
                router.start();
            }

            // Run simulation
            scheduler.run();
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace(System.err);
        }
    }
}
