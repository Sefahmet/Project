package com.example.project.DataHolder;

import com.example.project.Test;

import org.locationtech.jts.index.strtree.STRtree;

public class GreenaryData {
    private STRtree buildings;
    private STRtree tree;
    private static GreenaryData instance;
    public GreenaryData(STRtree buildings, STRtree tree){
        this.buildings = buildings;
        this.tree = tree;
    }
    public STRtree getBuildings() {
        return buildings;
    }

    public STRtree getTree() {
        return tree;
    }


    public static GreenaryData getInstance() throws Exception {
        if (instance == null){
            instance = Test.getGreenaryData();
        }
        return instance;
    }

}
