package routing;

import reso.scheduler.AbstractScheduler;
import reso.scheduler.Scheduler;

public class RoutingDemo {

    public static final String TOPO_FILE= "reso/data/topology.txt";

    public static void main(String [] args) {

        String filename= RoutingDemo.class.getClassLoader().getResource(TOPO_FILE).getFile();

        AbstractScheduler scheduler= new Scheduler();

        try{

        }
        catch (Exception e) {
        System.err.println(e.getMessage());
        e.printStackTrace(System.err);
    }
    }
}
