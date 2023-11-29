package com.example.project.DataHolder;

import com.example.project.Entity.GraphFeatures;
import com.example.project.Entity.Weight;
import com.example.project.Test;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.locationtech.jts.index.strtree.STRtree;
import org.springframework.beans.factory.annotation.Autowired;

import static com.example.project.Model.GraphCreator.getGraphFeatrues;

@AllArgsConstructor
public class GreenaryData {
    @Getter @Setter private STRtree buildings;
    @Getter @Setter private STRtree tree;
    private static GreenaryData instance;

    public static GreenaryData getInstance() throws Exception {
        if (instance == null){
            instance = Test.getGreenaryData();
        }
        return instance;
    }

}
