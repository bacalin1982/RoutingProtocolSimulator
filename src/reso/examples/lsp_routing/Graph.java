package reso.examples.lsp_routing;

import reso.ip.IPAddress;

import java.util.ArrayList;
import java.util.List;

public class Graph {
    private List<Link> links;
    private List<Point> points;

    public Graph(List<Link> links){
        this.links = links;
        this.points = this.computePoints(links);
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
                if(l.getSrc().toString().equals(p.getId().toString()))
                    p.addLink(l);
                else if(l.getDest().toString().equals(p.getId().toString())){
                    p.addLink(new Link(l.getDest(), l.getSrc(), l.getCost()));
                }
            }
        }
        return points;
    }

    public void computeShortestDistance(){
        this.points.get(2).setcostTotalFromSrc(0);
        int nextPoint = 2;

        for(Point p: this.points){
            ArrayList<Link> currentPointLinks = this.points.get(nextPoint).getLinks();
            int joinedLink = 0;
            for(Link l: currentPointLinks){
                IPAddress neighbourIP = currentPointLinks.get(joinedLink).getNeighbourIp(this.points.get(nextPoint).getId());
                if (! this.points.get(getIndexOfPointWithIp(neighbourIP)).isVisited()){
                    //System.out.println("Not visited yet --->" + this.points.get(getIndexOfPointWithIp(neighbourIP)).getId());
                    int ftry = this.points.get(nextPoint).getcostTotalFromSrc() + currentPointLinks.get(joinedLink).getCost();
                    //System.out.println("TRY = "+ftry);
                    if (ftry < points.get(getIndexOfPointWithIp(neighbourIP)).getcostTotalFromSrc()){
                        //System.out.println("TRY "+ ftry + " is minus than TRY " + points.get(getIndexOfPointWithIp(neighbourIP)).getcostTotalFromSrc() + " from " + points.get(getIndexOfPointWithIp(neighbourIP)).getId());
                        points.get(getIndexOfPointWithIp(neighbourIP)).resetListOfCostsFromSrc();
                        for (Link li: points.get(nextPoint).getListOfCostsFromSrc()){
                            points.get(getIndexOfPointWithIp(neighbourIP)).addListOfCostsFromSrc(li);
                        }
                        points.get(getIndexOfPointWithIp(neighbourIP)).addListOfCostsFromSrc(l);
                        points.get(getIndexOfPointWithIp(neighbourIP)).setcostTotalFromSrc(ftry);
                    }
                }
                joinedLink ++;
            }
            points.get(nextPoint).setVisited(true);
            nextPoint = getPointShortestDistance();
        }
    }

    private int getPointShortestDistance(){
        int storedPointIndex = 0;
        int storedDist = Integer.MAX_VALUE;

        for (Point p : this.points){
            int currentDist = p.getcostTotalFromSrc();
            if (!p.isVisited() && currentDist < storedDist){
                storedDist = currentDist;
                storedPointIndex = getIndexOfPointWithIp(p.getId());
            }
        }

        return storedPointIndex;
    }

    public void printResult(){
        String output = "";
        for (Point p : this.points){
            output += "\nThe shortest distance from IP " + this.points.get(2).getId().toString() + " to IP " + p.getId().toString() + " is " + p.getcostTotalFromSrc() + " with " + p.getListOfCostsFromSrc();
        }

        System.out.println(output);
    }

    private int getIndexOfPointWithIp(IPAddress ip){
        int index = -1;
        for(Point p: this.points){
            if (p.getId() == ip)
                index = this.points.indexOf(p);
        }
        return index;
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