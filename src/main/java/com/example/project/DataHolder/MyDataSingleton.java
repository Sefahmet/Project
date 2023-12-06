package com.example.project.DataHolder;

import com.example.project.Entity.GraphFeatures;
import com.example.project.Entity.Weight;

import org.springframework.beans.factory.annotation.Autowired;


public class MyDataSingleton {
    private GraphFeatures graphFeatures;
    private Weight weight;

    @Autowired
    public MyDataSingleton()  {
        try {
            this.graphFeatures = GraphFeatures.getInstance();
            this.weight = Weight.getInstance();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public GraphFeatures getGraphFeatures() {
        return graphFeatures;
    }

    public Weight getWeight() {
        return weight;
    }

    public void setGraphFeatures(GraphFeatures graphFeatures) {
        this.graphFeatures = graphFeatures;
    }

    public void setWeight(Weight weight) {
        this.weight = weight;
    }

    public void update(){

    }
}
