package com.example.project.Entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.io.IOException;
public class Weight {
    /*This weight are representing users selections. These are not edge weight.*/

    @Getter @Setter private Double length_weight;
    @Getter @Setter private Double slope_weight;
    @Getter @Setter private Double max_speed_weight_weight;
    @Getter @Setter private Double turning_cost_weight;
    @Getter @Setter private static Weight instance;
    public static Weight getInstance() throws IOException {
        if (instance == null){
            instance = new Weight(0.25,0.25,0.25,0.25);
        }
        return instance;
    }

    public Weight(Double length_weight, Double slope_weight, Double max_speed_weight_weight, Double turning_cost_weight) {
        double sumWeights = length_weight + slope_weight + max_speed_weight_weight + turning_cost_weight;
        if(sumWeights!= 0){
        this.length_weight = length_weight/sumWeights;
        this.slope_weight = slope_weight/sumWeights;
        this.max_speed_weight_weight = max_speed_weight_weight/sumWeights;
        this.turning_cost_weight = turning_cost_weight/sumWeights;
        }else{
            this.length_weight = 0.25;
            this.slope_weight = 0.25;
            this.max_speed_weight_weight = 0.25;
            this.turning_cost_weight = 0.25;

        }

    }

}
