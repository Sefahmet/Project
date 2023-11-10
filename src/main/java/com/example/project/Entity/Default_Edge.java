package com.example.project.Entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@NoArgsConstructor
public class Default_Edge extends DefaultWeightedEdge {

        @Getter @Setter private int     id;
        @Getter @Setter private Long    osmid;
        @Getter @Setter private String  roadType;
        @Getter @Setter private String  name;
        @Getter @Setter private Long    u_id;
        @Getter @Setter private Long    v_id;
        @Getter @Setter private Default_Node u;
        @Getter @Setter private Default_Node v;
        @Getter @Setter private boolean oneWay;
        @Getter @Setter private Double  maxSpeed;
        @Getter @Setter private Double  distance;
        @Getter @Setter private Double  slope;
        @Getter @Setter private Double  greenness;


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
}

