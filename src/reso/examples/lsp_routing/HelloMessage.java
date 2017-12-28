package reso.examples.lsp_routing;

import reso.common.Message;
import reso.ip.IPAddress;
import java.util.List;

public class HelloMessage implements Message {

    public final IPAddress origin;
    public List<IPAddress> neighbours;

    public HelloMessage(IPAddress origin, List<IPAddress> neighbours) {
        this.origin = origin;
        this.neighbours = neighbours;
    }

    public IPAddress getOrigin() {
        return origin;
    }

    public List<IPAddress> getNeighbours() {
        return neighbours;
    }

    public String toString() {
            String ns = "";
            for (IPAddress n : neighbours)
                ns += n.toString() + "; ";
            return "Hello[" + origin + ", neigbours:" + ns + "]";
        }
}
