package com.example.project.Model;

import com.example.project.Entity.GraphFeatures;
import com.example.project.Service.Reader;

public class GraphCreator {
    public static GraphFeatures getGraphFeatrues() throws Exception {
        GraphFeatures graphFeatures = Reader.readTheDefaultFiles();


        return graphFeatures;
    }


}
