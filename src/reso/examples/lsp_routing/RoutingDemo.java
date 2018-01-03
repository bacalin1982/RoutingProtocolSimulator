package reso.examples.lsp_routing;

import reso.common.Network;
import reso.common.Node;
import reso.ip.IPRouter;
import reso.scheduler.AbstractScheduler;
import reso.scheduler.Scheduler;
import reso.utilities.FIBDumper;
import reso.utilities.NetworkBuilder;

public class RoutingDemo {

    public static final String TOPO_FILE= "reso/data/topology.txt";

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

            // Display forwarding table for each node
            //FIBDumper.dumpForAllRouters(network);
        }
        catch (Exception e) {
            System.err.println(e.getMessage());
            e.printStackTrace(System.err);
        }
    }
}
