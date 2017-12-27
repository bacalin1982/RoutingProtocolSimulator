package reso.examples.lsp_routing;

import reso.common.Message;
import reso.ip.IPAddress;

import java.util.ArrayList;
import java.util.List;

public class HelloMessage implements Message {
    public static class Hello {
        public final IPAddress origin;
        public List<IPAddress> neighbours;

        public Hello(IPAddress origin, List<IPAddress> neighbours) {
            this.origin = origin;
            this.neighbours = neighbours;
        }

        public String toString() {
            String ns = "";
            for (IPAddress n : neighbours)
                ns += n.toString() + "; ";
            return "Hello[" + origin + "," + ns + "]";
        }
    }

    public final List<Hello> Hellos;

    public HelloMessage() {
        Hellos = new ArrayList<Hello>();
    }

    public void addHello(IPAddress origin, List<IPAddress> neighbours) {
        Hellos.add(new Hello(origin, neighbours));
    }

    public String toString() {
        String s = "";
        for (Hello m : Hellos)
            s += " " + m.toString();
        return s;
    }
}
