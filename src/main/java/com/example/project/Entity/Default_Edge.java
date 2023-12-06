package com.example.project.Entity;

import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Default_Edge extends DefaultWeightedEdge {

        private int     id;
        private Long    osmid;
        private String  roadType;
        private String  name;
        private Long    u_id;
        private Long    v_id;
        private Default_Node u;
        private Default_Node v;
        private boolean oneWay;
        private Double  maxSpeed;
        private Double  distance;
        private Double  slope;
        private Double  greenness;


        public Default_Edge(int fid, Long osmid,String roadType, String name, Long u_id, Long v_id, Default_Node u,
                            Default_Node v, boolean oneWay, Double maxSpeed, Double  distance,Double slope){
                this.id = fid;
                this.osmid = osmid;
                this.roadType = roadType;
                this.name = name;
                this.u_id = u_id;
                this.v_id = v_id;
                this.u = u;
                this.v = v;
                this.oneWay = oneWay;
                this.maxSpeed = maxSpeed;
                this.distance = distance;
                this.slope = slope;


        }
        public Default_Edge(int id){
                this.id = id;
        }
        public Default_Edge(int fid, Long osmid, Long u_id, Long v_id, Default_Node u, Default_Node v, boolean oneWay, Double maxSpeed, Double  distance){
                this.id = fid;
                this.osmid = osmid;
                this.u_id = u_id;
                this.v_id = v_id;
                this.u = u;
                this.v = v;

                this.oneWay = oneWay;
                this.maxSpeed = maxSpeed;
                this.distance = distance;


        }
        public Map<Default_Edge, Double> getOrderedAngles(List<Default_Edge> outgoings,Boolean isIncomings) {
                // Bu kenarın bitiş noktasının koordinatları
                if(!isIncomings){
                        return this.getOrderedAngles(outgoings);
                }

                double x1 = u.getEast();
                double y1 = u.getNorth();

                // Bu kenarın başlangıç noktasının koordinatları
                double x2 = v.getEast();
                double y2 = v.getNorth();

                // Sonuçları tutacak bir Map oluştur
                Map<Default_Edge, Double> angles = new HashMap<>();

                // Diğer kenarlar üzerinde dön
                for (Default_Edge edge : outgoings) {
                        // Diğer kenarın başlangıç ve bitiş koordinatları
                        double x3 = edge.v.getEast();
                        double y3 = edge.v.getNorth();
                        double x4 = edge.u.getEast();
                        double y4 = edge.u.getNorth();

                        // Açı hesaplama formülü
                        double angle = calculateAngle(x1, y1, x2, y2, x3, y3, x4, y4);

                        angles.put(edge, angle);
                }

                return angles;
        }



        public Map<Default_Edge, Double> getOrderedAngles(List<Default_Edge> outgoings) {
                // Bu kenarın bitiş noktasının koordinatları
                double x1 = u.getEast();
                double y1 = u.getNorth();

                // Bu kenarın başlangıç noktasının koordinatları
                double x2 = v.getEast();
                double y2 = v.getNorth();

                // Sonuçları tutacak bir Map oluştur
                Map<Default_Edge, Double> angles = new HashMap<>();

                // Diğer kenarlar üzerinde dön
                for (Default_Edge edge : outgoings) {
                        // Diğer kenarın başlangıç ve bitiş koordinatları
                        double x3 = edge.u.getEast();
                        double y3 = edge.u.getNorth();
                        double x4 = edge.v.getEast();
                        double y4 = edge.v.getNorth();

                        // Açı hesaplama formülü
                        double angle = calculateAngle(x1, y1, x2, y2, x3, y3, x4, y4);

                        angles.put(edge, angle);
                }

                return angles;
        }

        private static double calculateAngle(double x1, double y1, double x2, double y2, double x3, double y3, double x4, double y4) {
                // İlk kenarın vektörünü hesaplayın
                double vector1x = x2 - x1;
                double vector1y = y2 - y1;

                // İkinci kenarın vektörünü hesaplayın
                double vector2x = x4 - x3;
                double vector2y = y4 - y3;

                // İki vektör arasındaki açıyı hesaplayın
                double angle = Math.toDegrees(Math.atan2(vector2y, vector2x) - Math.atan2(vector1y, vector1x));

                if (angle < 0) {
                        angle += 360;
                }

                // Saatin tersi yönünde açıyı bulmak için 360 dereceden çıkarın
                double counterClockwiseAngle = 360 - angle;
                return counterClockwiseAngle;
        }

        @Override
        public String toString() {
                return id + " "+osmid+" "+roadType+" "+ name +" "+ u_id+ " "+ v_id+ " "+ oneWay;
        }

        public void printEdgeT(){
                System.out.printf("| %-4d | %-10d | %-25s | %-14s | %-12d | %-12d | %-5b |%-10f |%-10f |%-10f |%n",
                                       id, osmid, name, roadType, u_id, v_id, oneWay,maxSpeed,slope,distance);
        }

        public int getId() {
                return id;
        }

        public void setId(int id) {
                this.id = id;
        }

        public Long getOsmid() {
                return osmid;
        }

        public void setOsmid(Long osmid) {
                this.osmid = osmid;
        }

        public String getRoadType() {
                return roadType;
        }

        public void setRoadType(String roadType) {
                this.roadType = roadType;
        }

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public Long getU_id() {
                return u_id;
        }

        public void setU_id(Long u_id) {
                this.u_id = u_id;
        }

        public Long getV_id() {
                return v_id;
        }

        public void setV_id(Long v_id) {
                this.v_id = v_id;
        }

        public Default_Node getU() {
                return u;
        }

        public void setU(Default_Node u) {
                this.u = u;
        }

        public Default_Node getV() {
                return v;
        }

        public void setV(Default_Node v) {
                this.v = v;
        }

        public boolean isOneWay() {
                return oneWay;
        }

        public void setOneWay(boolean oneWay) {
                this.oneWay = oneWay;
        }

        public Double getMaxSpeed() {
                return maxSpeed;
        }

        public void setMaxSpeed(Double maxSpeed) {
                this.maxSpeed = maxSpeed;
        }

        public Double getDistance() {
                return distance;
        }

        public void setDistance(Double distance) {
                this.distance = distance;
        }

        public Double getSlope() {
                return slope;
        }

        public void setSlope(Double slope) {
                this.slope = slope;
        }

        public Double getGreenness() {
                return greenness;
        }

        public void setGreenness(Double greenness) {
                this.greenness = greenness;
        }
}

