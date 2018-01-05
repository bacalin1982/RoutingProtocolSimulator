package reso.examples.lsp_routing;

import reso.ip.IPAddress;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Point {
    private IPAddress id;
    private int costTotalFromSrc = Integer.MAX_VALUE;
    private Map<IPAddress, Integer> listOfCostsFromSrc = new HashMap<>();
    private boolean visited;
    private ArrayList<Link> links = new ArrayList<Link>();

    public Point(IPAddress id, int costFromSrc, boolean visited, ArrayList<Link> links) {
        this.id = id;
        this.costTotalFromSrc = costFromSrc;
        this.visited = visited;
        this.links = links;
    }

    public IPAddress getId() {
        return id;
    }

    public void setId(IPAddress id) {
        this.id = id;
    }

    public int getcostTotalFromSrc(){
        return this.costTotalFromSrc;
    }

    public void setcostTotalFromSrc(int cost){
        this.costTotalFromSrc = cost;
    }

    public boolean isVisited() {
        return visited;
    }

    public void setVisited(boolean visited) {
        this.visited = visited;
    }

    public ArrayList<Link> getLinks() {
        return links;
    }

    public void setLinks(ArrayList<Link> links) {
        this.links = links;
    }

    public void addLink(Link l){
        this.links.add(l);
    }

    @Override
    public String toString() {
        return "Point " + this.id + " with links [" + links + "]";
    }
}
