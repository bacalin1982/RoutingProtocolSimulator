package reso.examples.lsp_routing;

import reso.ip.IPAddress;

import java.util.ArrayList;

public class Point {
    private IPAddress id;
    private int costFromSrc = Integer.MAX_VALUE;
    private boolean visited;
    private ArrayList<Link> links = new ArrayList<Link>();

    public Point(IPAddress id, int costFromSrc, boolean visited, ArrayList<Link> links) {
        this.id = id;
        this.costFromSrc = costFromSrc;
        this.visited = visited;
        this.links = links;
    }

    public IPAddress getId() {
        return id;
    }

    public void setId(IPAddress id) {
        this.id = id;
    }

    public int getCostFromSrc(){
        return this.costFromSrc;
    }

    public void setCostFromSrc(int cost){
        this.costFromSrc = cost;
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
