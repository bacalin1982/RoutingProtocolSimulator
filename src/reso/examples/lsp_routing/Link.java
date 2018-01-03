package reso.examples.lsp_routing;

import reso.ip.IPAddress;

public class Link {
    private IPAddress src;
    private IPAddress dest;
    private int cost;

    public Link(IPAddress src, IPAddress dest, int cost){
        this.src = src;
        this.dest = dest;
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

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    @Override
    public String toString() {
        return "Link from " + this.src + " to " + this.dest + " cost " + this.cost;
    }
}