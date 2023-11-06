package com.example.project.Entity;

import lombok.Getter;
import lombok.Setter;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.io.IOException;
import java.util.HashMap;

import static com.example.project.Model.GraphCreator.getGraphFeatrues;

public class GraphFeatures {
    @Getter @Setter private Graph<Default_Edge, DefaultWeightedEdge> graph;
    @Getter @Setter private HashMap<Long, Default_Node> nodeHashMap;
    @Getter @Setter private HashMap<String, Default_Edge> edgeHashMap;
    @Getter @Setter private HashMap<String, CreatedEdge> createdEdgesHashMap;
    private static GraphFeatures instance;

    public static GraphFeatures getInstance() throws Exception {
        if (instance == null){
            System.out.println("\n\nFiles Readed\n\n");
            instance = getGraphFeatrues();
        }
        return instance;
    }
    public HashMap<Long, Default_Node> getNodeHashMap() {
        return nodeHashMap;
    }

    public HashMap<String, Default_Edge> getEdgeHashMap() {
        return edgeHashMap;
    }

    public Graph<Default_Edge, DefaultWeightedEdge> getGraph() {return graph;}


    public void setEdgeHashMap(HashMap<String, Default_Edge> edgeHashMap) {
        this.edgeHashMap = edgeHashMap;
    }

    public void setNodeHashMap(HashMap<Long, Default_Node> nodeHashMap) {
        this.nodeHashMap = nodeHashMap;
    }

    public void setGraph(Graph<Default_Edge, DefaultWeightedEdge> graph) {this.graph = graph;}
}
