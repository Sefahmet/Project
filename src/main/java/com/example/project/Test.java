package com.example.project;


import com.example.project.DataHolder.GreenaryData;
import com.example.project.Entity.CreatedEdge;
import com.example.project.Entity.Default_Edge;
import com.example.project.Entity.Default_Node;
import com.example.project.Entity.GraphFeatures;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.locationtech.jts.geom.*;
import org.locationtech.jts.geom.impl.CoordinateArraySequence;
import org.locationtech.jts.index.strtree.STRtree;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Test {
    static final Double resolutionOfGreenary = 5.0;
    static final Double visibilityDistance = 200.0;
    static final Double visibleAngle = 150.0;
    static final String path;
    static {
        try {
            path = new ClassPathResource("static/").getFile().getPath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    private static void treeVisibilityAlgo() throws Exception {
        STRtree buildings = GreenaryData.getInstance().getBuildings();
        STRtree trees = GreenaryData.getInstance().getTree();
        HashMap<String, Default_Edge> edgeHashmap = GraphFeatures.getInstance().getEdgeHashMap();
        double idx =0.0;
        int size = edgeHashmap.values().size();
        int perc = 0;
        for(Default_Edge edge:edgeHashmap.values()){
            idx++;
            Double p = idx * 100 / size;
            if(p.intValue()>perc){
                System.out.println(perc + "% completed");
                perc+=5;
            }
            double visibleTreesCount = 0;
            Default_Node u = edge.getU();
            Default_Node v = edge.getV();
            List<Coordinate> coordinates = getSubCoordinates(u, v);
            Double angle = coordinates.get(0).z;
            if(angle<0){
                angle+=2*Math.PI;
            }

            double angleDeg = Math.toDegrees(angle);
            double left = (angleDeg + visibleAngle/2) % 360;
            double right = (angleDeg - visibleAngle/2+360)%360;
            Integer method = getCalculationMethod(left,right,angleDeg);

            for (Coordinate coordinate : coordinates) {
                Envelope envelope = getEnvelopeOfVisibleArea(coordinate, angleDeg, left, right, method);
                List treesPOI = trees.query(envelope);
                List buildinsPOI = buildings.query(envelope);
                double countt = (double) countVisibleTree(coordinate, treesPOI, buildinsPOI);

                visibleTreesCount += countt;

            }
            edge.setGreenness((visibleTreesCount / (double) coordinates.size()));


        }
        treeCounterWriter();
    }
    private static void treeCounterWriter(){
        try {
            String fileName = "/greenary.txt";
            File file = new File(path+fileName);
            HashMap<String, Default_Edge> edgeHashmap = GraphFeatures.getInstance().getEdgeHashMap();
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));

            for (String key : edgeHashmap.keySet()) {
                Default_Edge edge = edgeHashmap.get(key);
                String line =key +","+edge.getGreenness();
                writer.write(line);
                writer.newLine();
            }

            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static Integer countVisibleTree(Coordinate coordinate,List trees, List buildings){
        Double angle = Math.toDegrees(coordinate.z);
        Double x = coordinate.x;
        Double y = coordinate.y;
        int count = 0;
        for (int i = 0;i<trees.size();i++){
            Point tree = (Point) trees.get(i);
            double angle2tree = Math.toDegrees(Math.atan2(tree.getY() - y, tree.getX() - x));
            double ang = Math.abs(angle2tree - angle);
            double distance = Math.hypot(tree.getY() - y, tree.getX() - x);

            if (ang<visibleAngle/2 && distance < visibilityDistance){

                LineString lineString = createLineString(new Coordinate(x, y), new Coordinate(tree.getX(), tree.getY()));

                if (isTreeVisible(lineString,buildings)){
                    count++;
                }

            }

        }
        return count;

    }

    private static boolean isTreeVisible(LineString lineString,List buildings ){

        for(int j = 0;j<buildings.size();j++){
            Polygon building = (Polygon) buildings.get(j);
            boolean intersect = lineString.intersects(building);
            if (intersect){
                return false;
            }
        }
        return true;
    }

    private static LineString createLineString(Coordinate p1, Coordinate p2) {
        Coordinate[] coordinates = new Coordinate[]{p1,p2};

        GeometryFactory geometryFactory = new GeometryFactory();
        return new LineString(new CoordinateArraySequence(coordinates), geometryFactory);
    }
    private static Integer getCalculationMethod(Double left,Double right,Double angleDeg){
        String regionLeft = determineRegion(left);
        String regionRight = determineRegion(right);
        String regionDirection = determineRegion(angleDeg);
        String[] quadrantList = {regionRight, regionDirection, regionLeft};
        List<String> quadrantArray = Arrays.asList(quadrantList);
        Integer method = 0;
        if(regionRight.equals(regionDirection) || regionLeft.equals(regionDirection)){
            if(quadrantArray.contains("Quadrant I") && quadrantArray.contains("Quadrant II")){
                method = 1;
            }else if(quadrantArray.contains("Quadrant III") && quadrantArray.contains("Quadrant II")){
                method = 2;
            }else if(quadrantArray.contains("Quadrant III") && quadrantArray.contains("Quadrant IV")){
                method = 3;
            }else {
                method = 4;
            }

        }else{
            if(!quadrantArray.contains("Quadrant IV")){
                method = 5;
            }else if(!quadrantArray.contains("Quadrant I")){
                method = 6;
            }else if(!quadrantArray.contains("Quadrant II")){
                method = 7;
            }else {
                method = 8;
            }

        }
        return method;
    }
    private static Envelope getEnvelopeOfVisibleArea(Coordinate coordinate,Double angle,Double left,Double right,Integer method){
        double x = coordinate.x;
        double y = coordinate.y;
        Double xmax=0.0;Double ymax=0.0;Double xmin=0.0;Double ymin=0.0;
        switch (method) {
            case 4:
                xmax = x+visibilityDistance;
                ymin = y-Math.abs(visibilityDistance*Math.sin(Math.toRadians(right)));
                ymax = y+Math.abs(visibilityDistance*Math.sin(Math.toRadians(left)));
                xmin = x;
                break;
            case 1:
                xmax = x+Math.abs(visibilityDistance*Math.cos(Math.toRadians(right)));
                ymin = y;
                ymax = y+visibilityDistance;
                xmin = x-Math.abs(visibilityDistance*Math.cos(Math.toRadians(left)));
                break;
            case 2:
                xmax = x;
                ymin = y-Math.abs(visibilityDistance*Math.sin(Math.toRadians(left)));
                ymax = y+Math.abs(visibilityDistance*Math.sin(Math.toRadians(right)));
                xmin = x-visibilityDistance;
                break;
            case 3:
                xmax = x+Math.abs(visibilityDistance*Math.cos(Math.toRadians(left)));
                ymin = y-visibilityDistance;
                ymax = y;
                xmin = x-Math.abs(visibilityDistance*Math.cos(Math.toRadians(right)));
                break;
            case 5:
                xmax = x + Math.abs(visibilityDistance*Math.cos(Math.toRadians(right)));
                ymin = y - Math.abs(visibilityDistance*Math.sin(Math.toRadians(left)));
                ymax = y + visibilityDistance;
                xmin = x - visibilityDistance;
                break;
            case 6:
                xmax = x + Math.abs(visibilityDistance*Math.cos(Math.toRadians(left)));
                ymin = y - visibilityDistance;
                ymax = y + Math.abs(visibilityDistance*Math.sin(Math.toRadians(right)));
                xmin = x - visibilityDistance;
                break;
            case 7:
                xmax = x + visibilityDistance;
                ymin = y - visibilityDistance;
                ymax = y + Math.abs(visibilityDistance*Math.sin(Math.toRadians(left)));
                xmin = x - Math.abs(visibilityDistance*Math.cos(Math.toRadians(right)));
                break;
            case 8:
                xmax = x + visibilityDistance;
                ymin = y - Math.abs(visibilityDistance*Math.sin(Math.toRadians(right)));
                ymax = y + visibilityDistance;
                xmin = x - Math.abs(visibilityDistance*Math.cos(Math.toRadians(left)));
                break;
            default:
                System.out.println("Geçersiz değer");
        }



        return new Envelope(xmin,xmax,ymin,ymax);
    }
    private static String determineRegion(double angle) {
        // Açının hangi bölgeye düştüğünü belirle
        if (angle >= 0 && angle < 90) {
            return "Quadrant I";
        } else if (angle >= 90 && angle < 180) {
            return "Quadrant II";
        } else if (angle >= 180 && angle < 270) {
            return "Quadrant III";
        } else if (angle >= 270 && angle < 360) {
            return "Quadrant IV";
        } else {
            return "Invalid Angle";
        }
    }




    public static List<Coordinate> getSubCoordinates(Default_Node u,Default_Node v){
        Double x1 = u.getEast();
        Double y1 = u.getNorth();
        Double x2 = v.getEast();
        Double y2 = v.getNorth();
        Double length = Math.hypot(x2 - x1, y2 - y1);
        Double val = length / resolutionOfGreenary;
        Double slopeAngle = Math.atan2 (y2 - y1 ,x2 - x1);
        Double pointsDistance = length / (val.intValue()+1);
        List<Coordinate> coordinates = new ArrayList<>();
        coordinates.add(new Coordinate(x1,y1,slopeAngle));
        for(int i = 1;i<val.intValue()+1;i++){
            Double x = x1+ i * pointsDistance * Math.cos(slopeAngle);
            Double y = y1+ i * pointsDistance * Math.sin(slopeAngle);
            coordinates.add(new Coordinate(x,y,slopeAngle));
        }
        coordinates.add(new Coordinate(x2,y2,slopeAngle));




        return coordinates;

    }

    public static GreenaryData getGreenaryData() {
        STRtree buildings = buildingReader();
        STRtree tree =treeReader();
        return new GreenaryData(buildings,tree);

    }
    public static STRtree buildingReader(){
        ObjectMapper objectMapper = new ObjectMapper();
        HashMap<Long, Default_Node> buildingHashMap = new HashMap<>();


        try{
            String buildingsFile = new String(Files.readAllBytes(Paths.get(path+"/buildingsUnaryUnion.geojson")));
            JsonNode jsonNode = objectMapper.readTree(buildingsFile);

            GeometryFactory geometryFactory = new GeometryFactory();
            int i = 0;
            Integer id;
            STRtree spatialIndex = new STRtree();
            while (jsonNode.get("features").get(i)!=null){
                JsonNode feature = jsonNode.get("features").get(i);
                String coordinates = feature.get("geometry").get("coordinates").toString();
                String s = coordinates.replaceAll("\\[|\\]", "");
                String[] coords = s.split(",");
                Coordinate[] c = new Coordinate[coords.length/2];
                int k = 0;
                for(int j = 0 ;j<coords.length;j+=2){
                    Double x = Double.valueOf(coords[j]);
                    Double y = Double.valueOf(coords[j+1]);
                    c[k] = new Coordinate(x,y);
                    k++;
                }
                Polygon building = geometryFactory.createPolygon(c);
                spatialIndex.insert(building.getEnvelopeInternal(), building);
                i++;

            }


            return spatialIndex;

        } catch (
                FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

    return null;

    }
    public static STRtree treeReader(){
        ObjectMapper objectMapper = new ObjectMapper();

        try{
            String treeFile = new String(Files.readAllBytes(Paths.get(path+"/tree.geojson")));
            JsonNode jsonNode = objectMapper.readTree(treeFile);

            GeometryFactory geometryFactory = new GeometryFactory();
            int i = 0;
            Integer id;
            STRtree spatialIndex = new STRtree();
            while (jsonNode.get("features").get(i)!=null){
                JsonNode feature = jsonNode.get("features").get(i);
                String coordinates = feature.get("geometry").get("coordinates").toString();
                String s = coordinates.replaceAll("\\[|\\]", "");
                String[] coords = s.split(",");
                Double x = Double.valueOf(coords[0]);
                Double y = Double.valueOf(coords[1]);


                Point tree = geometryFactory.createPoint(new Coordinate(x,y));
                spatialIndex.insert(tree.getEnvelopeInternal(), tree);
                i++;
            }


            return spatialIndex;

        } catch (
                FileNotFoundException ex) {
            ex.printStackTrace();
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return null;

    }
}
