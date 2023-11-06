package com.example.project.Model;

import com.example.project.DataHolder.MyDataSingleton;
import com.example.project.Entity.CreatedEdge;
import com.example.project.Entity.Default_Edge;
import com.example.project.Entity.GraphFeatures;
import com.example.project.Entity.Weight;
import com.example.project.Service.Decider;
import com.example.project.Service.Reader;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.jgrapht.graph.DefaultDirectedWeightedGraph;
import org.jgrapht.graph.DefaultWeightedEdge;

import java.util.Collection;
import java.util.Set;

public class GraphCreator {
    public static GraphFeatures getGraphFeatrues() throws Exception {
        GraphFeatures graphFeatures = Reader.readTheDefaultFiles();


        return graphFeatures;
    }


}
