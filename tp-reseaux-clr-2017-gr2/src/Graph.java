package reso.examples.lsp_routing;

import reso.ip.IPAddress;

import java.util.*;

public class Graph {
    private List<Link> links;
    private List<Point> points;
    private List<List<Point>> result;

    public Graph(List<Link> links){
        this.links = links;
        this.points = this.computePoints(links);
        this.result = new ArrayList<>();
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
                    p.addLink(new Link(l.getDest(), l.getSrc(), l.getOif(), l.getCost()));
                }
            }
        }
        return points;
    }

    //Dijkstra Algorithm, You've no idea how much time it costs ...
    public void computeShortestDistance(){
        for (int i = 0; i < this.points.size(); i++){
            this.points.get(i).setcostTotalFromSrc(0);
            this.points.get(i).isSource(true);
            int nextPoint = i;

            for(Point p: this.points){
                ArrayList<Link> currentPointLinks = this.points.get(nextPoint).getLinks();
                int joinedLink = 0;
                for(Link l: currentPointLinks){
                    IPAddress neighbourIP = currentPointLinks.get(joinedLink).getNeighbourIp(this.points.get(nextPoint).getId());
                    if (! this.points.get(getIndexOfPointWithIp(neighbourIP)).isVisited()){
                        //System.out.println("Not visited yet --->" + this.points.get(getIndexOfPointWithIp(neighbourIP)).getId());
                        int ftry = this.points.get(nextPoint).getcostTotalFromSrc() + currentPointLinks.get(joinedLink).getCost();
                        //System.out.println("TRY = "+ftry);
                        if (currentPointLinks.get(joinedLink).getCost() != Integer.MAX_VALUE && ftry < points.get(getIndexOfPointWithIp(neighbourIP)).getcostTotalFromSrc()){
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
            //Saving points done from one source
            this.result.add(this.points);
            //Reset all points distance from source
            this.points = computePoints(this.links);
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

    /*public List<List<Point>> getResult(boolean debug){
        if (debug) {
            String output = "";
            int i = 0;
            for (List<Point> list : this.result) {
                for (Point p : list) {
                    output += Constants._I + "The shortest distance from IP " + this.points.get(i).getId().toString() + " to IP " + p.getId().toString() + " is " + p.getcostTotalFromSrc() + " with " + p.getListOfCostsFromSrc() + "\n";
                }
                i++;
            }
            System.out.println(output);
        }
        return this.result;
    }*/

    public Map<IPAddress, List<Point>> getRouterWithBestRoute(boolean debug){
        String output = "";
        Map<IPAddress, List<Point>> routerWithBestRoute = new HashMap<>();
        int i = 0;
        for (List<Point> list : this.result) {
            for (Point p : list) {
                IPAddress routerIp = this.points.get(i).getId();
                if(!routerWithBestRoute.containsKey(routerIp)){
                    routerWithBestRoute.put(routerIp, new ArrayList<>());
                }
                routerWithBestRoute.get(routerIp).add(p);
                if(debug) {
                    output += Constants._I + "The shortest distance from IP " + routerIp.toString() + " to IP " + p.getId().toString() + " is " + p.getcostTotalFromSrc() + " with " + p.getListOfCostsFromSrc() + "\n";
                }
            }
            i++;
        }
        if(debug) {
            System.out.println(output);
        }
        return routerWithBestRoute;
    }

    private int getIndexOfPointWithIp(IPAddress ip){
        int index = -1;
        for(Point p: this.points){
            if (p.getId() == ip)
                index = this.points.indexOf(p);
        }
        return index;
    }
}