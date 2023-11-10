package com.example.project.Service;

import com.example.project.DataHolder.MyDataSingleton;
import com.example.project.Entity.*;
import geotrellis.proj4.CRS;
import geotrellis.proj4.Transform;
import lombok.Builder;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.locationtech.jts.geom.Coordinate;
import org.springframework.stereotype.Service;
import scala.Tuple2;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@Service
public class WeightedShortestPath {
    public static List<Coordinate> shortestPathService( double lat1,
                                                                            double lon1,
                                                                            double lat2,
                                                                            double lon2,
                                                                            double wLength,
                                                                            double wSlope,
                                                                            double wMaxSpeed,
                                                                            double wTurnLeft){

        Weight weight = new Weight(wLength,wSlope,wMaxSpeed,wTurnLeft);
        MyDataSingleton myDataSingleton = new MyDataSingleton();
        if (weight != myDataSingleton.getWeight()){
            myDataSingleton.setWeight(weight);
            myDataSingleton.update();
            GraphFeatures graphFeature = updateGraphFeatures(myDataSingleton.getGraphFeatures(), weight);
            myDataSingleton.setGraphFeatures(graphFeature);
        }

        Coordinate p1 = LatLon2EN(lon1, lat1);
        Coordinate p2 = LatLon2EN(lon2, lat2);
        GraphPath<Default_Edge, CreatedEdge> shoretesPath = getShortestPath(p1, p2);


        return shortestPath2Coordinate(shoretesPath);
    }
    public static List<String>  shortestPathServiceOSMID( double lat1,
                                                        double lon1,
                                                        double lat2,
                                                        double lon2,
                                                        double wLength,
                                                        double wSlope,
                                                        double wMaxSpeed,
                                                        double wTurnLeft){

        Weight weight = new Weight(wLength,wSlope,wMaxSpeed,wTurnLeft);
        MyDataSingleton myDataSingleton = new MyDataSingleton();
        if (weight != myDataSingleton.getWeight()){
            myDataSingleton.setWeight(weight);
            myDataSingleton.update();
            GraphFeatures graphFeature = updateGraphFeatures(myDataSingleton.getGraphFeatures(), weight);
            myDataSingleton.setGraphFeatures(graphFeature);
        }

        Coordinate p1 = LatLon2EN(lon1, lat1);
        Coordinate p2 = LatLon2EN(lon2, lat2);
        GraphPath<Default_Edge, CreatedEdge> shortestPath = getShortestPath(p1, p2);
        List<String> osmids = new ArrayList<>();
        for (Default_Edge edge:shortestPath.getVertexList()){
            if(edge.getOsmid() != null) {
                osmids.add(String.valueOf(edge.getOsmid()));
            }
        }
        return osmids;
    }
    private static List<Coordinate> shortestPath2Coordinate(GraphPath<Default_Edge, CreatedEdge> shoretesPath){
        List<Default_Edge> edges = shoretesPath.getVertexList();
        List<Coordinate> coordinates = new ArrayList<>();
        if (edges.size()>1) {
            Double x;
            Double y;
            Coordinate coordinate;
            for (int i = 1; i < edges.size() - 1; i++) {
                Default_Edge edge = edges.get(i);
                x = edge.getU().getEast();
                y = edge.getU().getNorth();
                coordinate = EN2LatLon(x, y);
                coordinates.add(coordinate);
            }
            Default_Edge lastEdge = edges.get(coordinates.size() - 2);
            x = lastEdge.getV().getEast();
            y = lastEdge.getV().getNorth();
            coordinate = EN2LatLon(x, y);
            coordinates.add(coordinate);
            return coordinates;
        }
        else{
            return null;
        }
    }
    public static GraphPath<Default_Edge, CreatedEdge> getShortestPath(Coordinate startPoint, Coordinate endPoint){

        MyDataSingleton myDataSingleton = new MyDataSingleton();
        List<Default_Node> startAndEnd = getClosestNode(startPoint,endPoint);
        List<List<Default_Edge>> inAndExitEdges = getInAndExitEdges(startAndEnd);
        Default_Edge startEdge = new Default_Edge(1000000);
        Default_Edge endEdge   = new Default_Edge(2000000);
        List<DefaultWeightedEdge> temporaryEdges = addTemporaryEdges2Graph(inAndExitEdges, startEdge, endEdge);
        GraphPath<Default_Edge, CreatedEdge> shortesPath = findShortesPath(startEdge, endEdge);
        removeTheTemporaryEdgeFromGraph(temporaryEdges);
        return shortesPath;


    }
    private static GraphPath<Default_Edge, CreatedEdge > findShortesPath(Default_Edge startEdge,Default_Edge endEdge){
        try{
            Graph graph = GraphFeatures.getInstance().getGraph();
            DijkstraShortestPath<Default_Edge, CreatedEdge> shortestPathAlg = new DijkstraShortestPath<>(graph);
            GraphPath<Default_Edge, CreatedEdge > shortestPath = shortestPathAlg.getPath(startEdge, endEdge);
            return shortestPath;

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;

    }
    private static void removeTheTemporaryEdgeFromGraph(List<DefaultWeightedEdge> temporaryEdges){
        try{
            Graph<Default_Edge, DefaultWeightedEdge> graph = GraphFeatures.getInstance().getGraph();
            for (DefaultWeightedEdge tempEdge:temporaryEdges){
                graph.removeEdge(tempEdge);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    private static List<DefaultWeightedEdge> addTemporaryEdges2Graph(List<List<Default_Edge>> inAndExitEdges,Default_Edge startEdge,Default_Edge endEdge  ){
        List<Default_Edge> inEdges = inAndExitEdges.get(0);
        List<Default_Edge> exitEdges = inAndExitEdges.get(1);
        List<DefaultWeightedEdge> temporaryEdges = new ArrayList<>();
        for(Default_Edge edge : inEdges){
            CreatedEdge createdEdge = new CreatedEdge(startEdge, edge);

            temporaryEdges.add(addEdge2Graph(createdEdge,0.0));
        }
        for(Default_Edge edge : exitEdges){
            CreatedEdge createdEdge = new CreatedEdge(edge,endEdge);
            Double weight = 0.0;
            temporaryEdges.add(addEdge2Graph(createdEdge,Reader.setEdgeWeight(createdEdge)));

        }
        return temporaryEdges;


    }

    private static DefaultWeightedEdge addEdge2Graph(CreatedEdge createdEdge,Double weight){
        try {
            Graph<Default_Edge, DefaultWeightedEdge> graph = GraphFeatures.getInstance().getGraph();
            graph.addVertex(createdEdge.getIncomingEdge());
            graph.addVertex(createdEdge.getOutgoingEdge());
            DefaultWeightedEdge graphEdge = graph.addEdge(createdEdge.getIncomingEdge(), createdEdge.getOutgoingEdge());
            graph.setEdgeWeight(graphEdge,weight);
            return graphEdge;
        }catch (Exception e ){
            e.printStackTrace();
        }
        return null;
    }
    private static List<List<Default_Edge>> getInAndExitEdges(List<Default_Node> startAndEnd){
        try{
            Collection<Default_Edge> defaultEdges = GraphFeatures.getInstance().getEdgeHashMap().values();
            List<Default_Edge> startEdges = new ArrayList<>();
            List<Default_Edge> endEdges = new ArrayList<>();
            Default_Node startNode = startAndEnd.get(0);
            Default_Node endNode = startAndEnd.get(1);
            for(Default_Edge edge: defaultEdges){
                if (edge.getU() == startNode){
                    startEdges.add(edge);
                }
                if(edge.getV() == endNode){
                    endEdges.add(edge);
                }
            }
            List<List<Default_Edge>> returnList = new ArrayList<>();
            returnList.add(startEdges);
            returnList.add(endEdges);
            return returnList;

        }catch (Exception e ){
            e.printStackTrace();
        }
        return null;
    }
    public static Coordinate LatLon2EN (double lat,double lon){

        CRS epsg3044 = CRS.fromEpsgCode(3044);
        CRS wgs84 = CRS.fromEpsgCode(4326);
        var fromWgs84 = Transform.apply(wgs84, epsg3044);
        Tuple2<Object, Object> latlon2EN = fromWgs84.apply(lat  , lon);
        return new Coordinate((double) latlon2EN._1(),(double) latlon2EN._2());


    }
    private static List<Default_Node> getClosestNode(Coordinate coordinate1,Coordinate coordinate2){
        try{
            Collection<Default_Node> nodes = GraphFeatures.getInstance().getNodeHashMap().values();
            Double closestDistanceStart = Double.MAX_VALUE;
            Double closestDistanceEnd = Double.MAX_VALUE;
            Default_Node closestStartNode = null;
            Default_Node closestEndNode = null;
            for(Default_Node node : nodes){
                Double distanceStart = Math.hypot(node.getEast()-coordinate1.x,node.getNorth()-coordinate1.y);
                Double distanceEnd = Math.hypot(node.getEast()-coordinate2.x,node.getNorth()-coordinate2.y);
                if(distanceStart<closestDistanceStart){
                    closestDistanceStart = distanceStart;
                    closestStartNode = node;
                }
                if(distanceEnd<closestDistanceEnd){
                    closestDistanceEnd = distanceEnd;
                    closestEndNode = node;
                }
            }
            List<Default_Node> nodeList = new ArrayList<>();
            nodeList.add(closestStartNode);
            nodeList.add(closestEndNode);
            return nodeList;
        }catch (Exception e ){
            e.printStackTrace();
        }
        return null;
    }

    public static Coordinate EN2LatLon (double x,double y){


        CRS epsg3044 = CRS.fromEpsgCode(3044);
        CRS wgs84 = CRS.fromEpsgCode(4326);

        var toWgs84 = Transform.apply(epsg3044, wgs84);


        Tuple2<Object, Object> EN2LatLon = toWgs84.apply(x,y);

        return new Coordinate((double)EN2LatLon._2(),(double)EN2LatLon._1());


    }

    public static GraphFeatures updateGraphFeatures(GraphFeatures graphFeatures, Weight weight){
        Graph<Default_Edge, DefaultWeightedEdge> graph = graphFeatures.getGraph();
        Collection<CreatedEdge> createdEdges = graphFeatures.getCreatedEdgesHashMap().values();
        for(CreatedEdge createdEdge:createdEdges){
            graph.setEdgeWeight(createdEdge.getIncomingEdge(),createdEdge.getOutgoingEdge(),Reader.setEdgeWeight(createdEdge,weight));
        }

        graphFeatures.setGraph(graph);
        return graphFeatures;
    }

}
