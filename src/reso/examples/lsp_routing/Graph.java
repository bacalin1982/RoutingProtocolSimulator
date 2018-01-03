package reso.examples.lsp_routing;

import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;
import reso.ip.IPAddress;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    private List<Link> links;
    private List<Point> points;

    public Graph(List<Link> links){
        this.links = links;
        this.points = this.computePoints(links);
        //System.out.println(points);
    }

    private List<Point> computePoints(List<Link> links) {
        List<Point> points = new ArrayList<Point>();
        List<IPAddress> pTemp = new ArrayList<IPAddress>();

        for (Link l : links) {
            if (!pTemp.contains(l.getDest()))
                pTemp.add(l.getDest());
            if (!pTemp.contains(l.getSrc()))
                pTemp.add(l.getSrc());
        }

        for (IPAddress ip : pTemp) {
            points.add(new Point(ip, Integer.MAX_VALUE, false, new ArrayList<Link>()));
        }

        for (Point p : points) {
            for (Link l : links) {
                //Maybe this line shall be update, we shall invert Dest and Src in case of l.getDest().toString().equals(p.getId().toString())
                if(l.getSrc().toString().equals(p.getId().toString()))
                    p.addLink(l);
                else if(l.getDest().toString().equals(p.getId().toString())){
                    p.addLink(new Link(l.getDest(), l.getSrc(), l.getCost()));
                }
            }
        }
        return points;
    }

    public List<Link> getLinks() {
        return links;
    }

    public void setLinks(List<Link> links) {
        this.links = links;
    }

    public List<Point> getPoints() {
        return points;
    }

    public void setPoints(List<Point> points) {
        this.points = points;
    }
}

