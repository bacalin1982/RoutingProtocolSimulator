package reso.examples.lsp_routing;

import reso.ip.IPAddress;

import java.util.ArrayList;
import java.util.List;

public class Point {
    private IPAddress id;
    private int costTotalFromSrc = Integer.MAX_VALUE;
    private ArrayList<Link> listOfCostsFromSrc = new ArrayList<>();
    private boolean visited;
    private ArrayList<Link> links = new ArrayList<>();

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

    public List<Link> getListOfCostsFromSrc() {
        return listOfCostsFromSrc;
    }

    public void resetListOfCostsFromSrc() {
        this.listOfCostsFromSrc = new ArrayList<Link>();
    }

    public void addListOfCostsFromSrc(Link l) {
        this.listOfCostsFromSrc.add(l);
    }

    public void delListOfCostsFromSrc(Link l) {
        this.listOfCostsFromSrc.remove(l);
    }

    @Override
    public String toString() {
        return "Point " + this.id + " with links [" + links + "]";
    }
}
