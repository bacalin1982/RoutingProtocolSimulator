package reso.examples.lsp_routing;

import reso.ip.IPAddress;
import reso.ip.IPInterfaceAdapter;

public class Link {
    private IPAddress src;
    private IPAddress dest;
    private IPInterfaceAdapter oif;
    private int cost;

    public Link(IPAddress src, IPAddress dest, IPInterfaceAdapter oif, int cost){
        this.src = src;
        this.dest = dest;
        this.oif = oif;
        this.cost = cost;
    }

    public IPAddress getSrc() {
        return src;
    }

    public void setSrc(IPAddress src) {
        this.src = src;
    }

    public IPAddress getDest() {
        return dest;
    }

    public void setDest(IPAddress dest) {
        this.dest = dest;
    }

    public IPInterfaceAdapter getOif() {
        return oif;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public IPAddress getNeighbourIp(IPAddress nodeIp){
        if(this.src == nodeIp){
            return this.dest;
        }else{
            return this.src;
        }
    }

    @Override
    public String toString() {
        return "Link from " + this.src + " to " + this.dest + " " + oif.getName() + " " + " cost " + this.cost;
    }
}