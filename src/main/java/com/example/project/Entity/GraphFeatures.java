package com.example.project.Entity;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.HashMap;

import static com.example.project.Model.GraphCreator.getGraphFeatrues;

public class GraphFeatures {
    private Graph<Default_Edge, DefaultWeightedEdge> graph;
    private HashMap<Long, Default_Node> nodeHashMap;
    private HashMap<String, Default_Edge> edgeHashMap;
    private HashMap<String, CreatedEdge> createdEdgesHashMap;
    private static GraphFeatures instance;

    public static GraphFeatures getInstance() throws Exception {
        if (instance == null){
            System.out.println("\n\nFiles Readed\n\n");
            instance = getGraphFeatrues();
        }
        return instance;
    }

    public Graph<Default_Edge, DefaultWeightedEdge> getGraph() {
        return graph;
    }

    public HashMap<Long, Default_Node> getNodeHashMap() {
        return nodeHashMap;
    }

    public HashMap<String, CreatedEdge> getCreatedEdgesHashMap() {
        return createdEdgesHashMap;
    }

    public HashMap<String, Default_Edge> getEdgeHashMap() {
        return edgeHashMap;
    }

    public void setCreatedEdgesHashMap(HashMap<String, CreatedEdge> createdEdgesHashMap) {
        this.createdEdgesHashMap = createdEdgesHashMap;
    }

    public void setEdgeHashMap(HashMap<String, Default_Edge> edgeHashMap) {
        this.edgeHashMap = edgeHashMap;
    }

    public void setGraph(Graph<Default_Edge, DefaultWeightedEdge> graph) {
        this.graph = graph;
    }

    public void setNodeHashMap(HashMap<Long, Default_Node> nodeHashMap) {
        this.nodeHashMap = nodeHashMap;
    }
}
