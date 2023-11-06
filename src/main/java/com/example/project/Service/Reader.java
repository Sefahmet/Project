package com.example.project.Service;

import com.example.project.DataHolder.MyDataSingleton;
import com.example.project.Entity.*;
import com.example.project.Model.EdgeCreator;
import com.example.project.Model.GraphCreator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.util.ResourceUtils;


import javax.sound.midi.Soundbank;
import java.io.*;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

public class Reader {


    static final String path;

    static {
        try {
            path = new ClassPathResource("static/").getFile().getPath();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static GraphFeatures readTheDefaultFiles() throws Exception {

            HashMap<Long, Default_Node> nodes = readNodeGeoJSON(path+"/nodes.geojson");

            System.out.println("Nodes Elevations are setting...");

            setNodesElev(path+ "/nodesElev.txt",nodes);

            System.out.println("Nodes Elevations set.");



            HashMap<String, Default_Edge> edges = readEdgeGeoJSON(path + "/edges.geojson", nodes);
                

            GraphFeatures graphFeatures = new GraphFeatures();
            graphFeatures.setEdgeHashMap(edges);
            graphFeatures.setNodeHashMap(nodes);
            HashMap<String, CreatedEdge> createdEdgeHashMap = new HashMap<>();
            int i = 0;
            int size = edges.size();
            int d = 0;
            for(Default_Edge edge1:edges.values()){

                Double s = i*100.0/size;
                i++;

                if(s.intValue() == d){
                    System.out.println(d +"%  completed");
                    d += 5;

                }
                List<Default_Edge> outgoings= new ArrayList<>();
                List<Default_Edge> incomings= new ArrayList<>();
                for(Default_Edge edge2:edges.values()){
                    if (edge1.getId()!=edge2.getId()){
                        if(edge1.getV()==edge2.getU()){
                            outgoings.add(edge2);
                        }
                        if(edge1.getV()==edge2.getV()){
                            incomings.add(edge2);
                        }
                    }
                }

                EdgeCreator.isItTurnLeft(edge1,outgoings,incomings,createdEdgeHashMap);




            }

            graphFeatures.setCreatedEdgesHashMap(createdEdgeHashMap);

            createdEdgeWriter(createdEdgeHashMap);
            Graph<Default_Edge, DefaultWeightedEdge> graph = createGraph(edges.values(), createdEdgeHashMap.values());
            graphFeatures.setGraph(graph);
            System.out.println("Graph Created, Program Ready for Running");

            return graphFeatures;

        }

/*        private static void createCreatedEdges(String path,HashMap<String, Default_Edge> edges){
            URL url;
            try{
                url = ResourceUtils.getURL(path);
                File file = new File(url.getPath());


            }catch (FileNotFoundException e){
                }
        }*/
        private static void setNodesElev(String path,HashMap<Long, Default_Node> nodes ){
            URL url;
            try{

                HashMap<Long,Double> nodesElev =  readNodeElevFile(path );
                if(nodes.keySet().size() == nodesElev.keySet().size()){
                    setElevationOfNodes(nodes,nodesElev);
                }else{
                    Map<String, List<Default_Node>> subsetNodes = Reader.getSubsetNodes(nodes);
                    Reader.setElevationOfNodes(subsetNodes);
                    writeNodeElevFile(path,nodes);

                }

            }catch (Exception e){
                Map<String, List<Default_Node>> subsetNodes = Reader.getSubsetNodes(nodes);
                Reader.setElevationOfNodes(subsetNodes);

                writeNodeElevFile(path,nodes);


            }


        }
        public static HashMap<String, Default_Edge> readEdgeGeoJSON(String filename, HashMap<Long, Default_Node> nodes) throws IOException {
            ObjectMapper objectMapper = new ObjectMapper();
            HashMap<String, Default_Edge> edgeHashMap = new HashMap<>();


            try{
                String edgesString = new String(Files.readAllBytes(Paths.get(filename)));
                JsonNode jsonEdge = objectMapper.readTree(edgesString);
                Integer id;
                int i = 0;
                while (jsonEdge.get("features").get(i)!=null){
                    JsonNode feature = jsonEdge.get("features").get(i);
                    if(feature.get("properties").get("fid") != null){
                        id = feature.get("properties").get("fid").asInt();
                    }
                    else{
                        id = i+1;
                    }
                    Long osmid = feature.get("properties").get("osmid").asLong();
                    String roadType = feature.get("properties").get("highway").asText();
                    String name = feature.get("properties").get("name").asText();
                    Long u_id = feature.get("properties").get("from").asLong();
                    Long v_id = feature.get("properties").get("to").asLong();
                    Default_Node u = nodes.get(u_id);
                    Default_Node v = nodes.get(v_id);
                    boolean oneWay= feature.get("properties").get("oneway").asBoolean();
                    Double maxSpeedWeight = 0.0;
                    try{
                        Double maxSpeed = feature.get("properties").get("maxspeed").asDouble();
                        maxSpeedWeight = Decider.maxSpeedDecider(maxSpeed);
                    }catch (Exception e){

                        maxSpeedWeight = Decider.maxSpeedDecider(Decider.nullMaxSpeedDecider(roadType));
                    }
                    Double length = feature.get("properties").get("length").asDouble();
                    Double slope = Decider.slopeDecider((v.getElevation()-u.getElevation())/length);

                    Default_Edge edge = new Default_Edge(id,osmid,roadType,name,u_id,v_id,u,v,oneWay,maxSpeedWeight,length,slope);
                    edgeHashMap.put(u_id +" "+v_id,edge);
                    if(!oneWay){
                        slope = Decider.slopeDecider((u.getElevation()-v.getElevation())/length);
                        edge = new Default_Edge(id,osmid,roadType,name,v_id,u_id,v,u,false,maxSpeedWeight,length,slope);
                        edgeHashMap.put(u_id +" "+v_id,edge);
                    }

                    i++;
                }
                return edgeHashMap;

            }catch (FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return  null;
        }

        public static HashMap<Long, Default_Node> readNodeGeoJSON(String filename) throws IOException {
            ObjectMapper objectMapper = new ObjectMapper();
            HashMap<Long, Default_Node> nodeHashMap = new HashMap<>();


            try{
                String nodes = new String(Files.readAllBytes(Paths.get(filename)));
                JsonNode jsonNode = objectMapper.readTree(nodes);
                int i = 0;
                Integer id;
                while (jsonNode.get("features").get(i)!=null){
                    JsonNode feature = jsonNode.get("features").get(i);
                    if(feature.get("properties").get("fid")!=null){
                        id = feature.get("properties").get("fid").asInt();

                    }else{
                        id = i+1;
                    }
                    Double x = feature.get("geometry").get("coordinates").get(0).asDouble();
                    Double y = feature.get("geometry").get("coordinates").get(1).asDouble();
                    Long osmid = feature.get("properties").get("osmid").asLong();
                    Integer str_count = feature.get("properties").get("street_cou").asInt();
                    Default_Node node = new Default_Node(id,osmid,x,y,str_count);
                    nodeHashMap.put(osmid,node);
                    i++;
                }
                return nodeHashMap;


                } catch (
            FileNotFoundException ex) {
                ex.printStackTrace();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            return null;

        }
        public static Map<String,Double> readElevationFile(String filename)  {

            HashMap<String, Double> elevMap = new HashMap<>();
            try {
                // Dizin içindeki dosyaları listele
                File file = new File(path+filename);



                BufferedReader reader = new BufferedReader(new FileReader(file));

                String line;
                while ((line = reader.readLine()) != null) {
                    String[] parts = line.split(" ");
                    if (parts.length >= 3) {
                        int x = (int) Double.parseDouble(parts[0]);
                        int y = (int) Double.parseDouble(parts[1]);
                        double z = Double.parseDouble(parts[2]);
                        elevMap.put(x+"_"+y,z);

                    }
                }
                reader.close();
                return elevMap;


            } catch (IOException e) {
                e.printStackTrace();
            }


            return  null;
        }

    public static Map<String,List<Default_Node>> getSubsetNodes(HashMap<Long, Default_Node> nodesHashMap ){
        Map<String,List<Default_Node>>  subsetNodes = new HashMap<>();
        Collection<Default_Node> nodes = nodesHashMap.values();
        for(Default_Node node :nodes){
            String key = ((int) (node.getEast() / 1000)) + "_" + ((int) (node.getNorth() / 1000));
            if (subsetNodes.containsKey(key)){
                subsetNodes.get(key).add(node);


            }else{
                List<Default_Node> nodeList = new ArrayList<>();
                nodeList.add(node);
                subsetNodes.put(key, nodeList);
            }

        }
        return subsetNodes;
        }
    public static void setElevationOfNodes(Map<String, List<Default_Node>> subsetNodes){
        Set<String> set = subsetNodes.keySet();
        for(String key:set){
            String s = "dgm1_32_" +key+ "_1_nw.xyz";
            Map<String, Double> elevs = readElevationFile(s);
            List<Default_Node> nodes = subsetNodes.get(key);

            for(Default_Node node:nodes){
                int east = node.getEast().intValue();
                int north = node.getNorth().intValue();
                node.setElevation(elevs.get(east+"_"+north));

            }
        }

    }
    public static void setElevationOfNodes(HashMap<Long, Default_Node> nodes, HashMap<Long, Double> nodesElev){
        for (Default_Node node:nodes.values()) {
            node.setElevation(nodesElev.get(node.getOsmid()));
        }
    }


    private static Graph<Default_Edge, DefaultWeightedEdge> createGraph(Collection<Default_Edge> edges, Collection<CreatedEdge> createdEdges){
        try{
            DefaultDirectedGraph graph = new DefaultDirectedWeightedGraph(DefaultWeightedEdge.class);

            for(Default_Edge edge : edges){
                graph.addVertex(edge);

            }
            for(CreatedEdge createdEdge : createdEdges){
                graph.addEdge(createdEdge.getIncomingEdge(),createdEdge.getOutgoingEdge(),createdEdge);
                graph.setEdgeWeight(createdEdge.getIncomingEdge(),createdEdge.getOutgoingEdge(),setEdgeWeight(createdEdge));
            }
            return graph;




        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }
    public static Double setEdgeWeight(CreatedEdge createdEdge,Weight weight){

        try {
            Default_Edge edge = createdEdge.getIncomingEdge();
            Double maxSpeed = edge.getMaxSpeed(); // Normalized Double weight
            Double distance = edge.getDistance(); // Distance as meter (weight)
            Double slope = edge.getSlope();  // Normalized slope weight
            Double turnLeft = Decider.turningCostDecider(createdEdge.getTurningCost()); // weight of turning cost
            // Weight of user selections
            Double wDistance = weight.getLength_weight();
            Double wMaxSpeed = weight.getMax_speed_weight_weight();
            Double wSlope = weight.getSlope_weight();
            Double wTurnLeft =  weight.getTurning_cost_weight();

            Double combinedWeight = distance *(wDistance+ wMaxSpeed*maxSpeed+ wSlope * slope) + turnLeft*wTurnLeft;

            return combinedWeight;


        }catch (Exception e ){
            e.printStackTrace();
        }
        return null;

    }
    public static Double setEdgeWeight(CreatedEdge createdEdge){
            try{
                Weight weight =  Weight.getInstance();
                return setEdgeWeight(createdEdge,weight);
            }catch (Exception e){

            }
            return null;
    }

    private static void writeNodeElevFile(String path, HashMap<Long,Default_Node> nodeHashMap){
        try {
            String fileName = "/nodesElev.txt";
            File file = new File(path+fileName);
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));

            for(Default_Node node:nodeHashMap.values()){
                String line = node.getOsmid() + ","+node.getElevation();
                writer.write(line);
                writer.newLine();
            }

            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private static HashMap<Long,Double> readNodeElevFile(String path){
        HashMap<Long,Double> nodesElev = new HashMap<>();
        try {
            File file = new File(path);
            Scanner reader = new Scanner(file);

            while (reader.hasNextLine()) {
                String line = reader.nextLine();
                String[] values = line.split(",");
                Long osmid = Long.valueOf(values[0]);
                Double elev = Double.valueOf(values[1]);
                nodesElev.put(osmid,elev);

            }

            reader.close();
            return nodesElev;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
    private static void createdEdgeWriter( HashMap<String, CreatedEdge> createdEdgeHashMap){

        try {
            String fileName = "/createdEdge.txt";
            File file = new File(path+fileName);
            System.out.println(file.getPath());
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));

            // EdgeHashMap'den nesneleri okuyun ve dosyaya yazın
            for (String key : createdEdgeHashMap.keySet()) {
                CreatedEdge edge = createdEdgeHashMap.get(key);
                String line = key +","+edge.getId() +","+edge.getIncomingEdge().getU_id() +"_"+edge.getIncomingEdge().getV_id()
                        +","+edge.getOutgoingEdge().getU_id() +"_"+edge.getOutgoingEdge().getV_id() +","+edge.getTurningCost();
                writer.write(line);
                writer.newLine();
            }

            // Yazıcıyı kapatın
            writer.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
