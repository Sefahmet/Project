package com.example.project;

import com.example.project.DataHolder.MyDataSingleton;
import com.example.project.Entity.*;
import com.example.project.Model.EdgeCreator;
import com.example.project.Service.Reader;
import com.example.project.Service.WeightedShortestPath;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.locationtech.jts.geom.Coordinate;
import org.springframework.util.ResourceUtils;

import java.io.*;
import java.net.URL;
import java.nio.file.Paths;
import java.util.*;

import static com.example.project.Service.Reader.readNodeGeoJSON;
import static com.example.project.Service.Reader.setElevationOfNodes;

public class Test {
    public static void main(String[] args) {
        try{
            URL url = ResourceUtils.getURL("classpath:static/nodes.geojson");
            HashMap<Long, Default_Node> nodes = readNodeGeoJSON(url.getPath());



            url = ResourceUtils.getURL("classpath:static/edges.geojson");
            File file = new File(url.getPath());
            if (file.exists()){
                HashMap<Long,Double> nodesElev =  readNodeElevFile(url.getPath());
                if(nodes.keySet().size() == nodesElev.keySet().size()){
                    setElevationOfNodes(nodes,nodesElev);
                }else{

                }
            }




        }catch (IOException e ){
            e.printStackTrace();
        }
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

}
