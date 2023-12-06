package com.example.project.Entity;


import java.io.IOException;
public class Weight {
    /*This weight are representing users selections. These are not edge weight.*/

    private Double length_weight;
    private Double slope_weight;
    private Double max_speed_weight_weight;
    private Double turning_cost_weight;
    private Double greenary_weight;
    private static Weight instance;
    public static Weight getInstance() throws IOException {
        if (instance == null){
            instance = new Weight(0.2,0.2,0.2,0.2,0.2);
        }
        return instance;
    }

    public static void setInstance(Weight instance) {
        Weight.instance = instance;
    }

    public Weight(Double length_weight, Double slope_weight, Double max_speed_weight_weight, Double turning_cost_weight, Double greenary_weight) {
        double sumWeights = length_weight + slope_weight + max_speed_weight_weight + turning_cost_weight+greenary_weight;
        if(length_weight + slope_weight + max_speed_weight_weight + greenary_weight == 0 && turning_cost_weight>0){
            this.length_weight = 0.0;
            this.slope_weight = 0.0;
            this.max_speed_weight_weight = 0.0;
            this.turning_cost_weight = 0.99;
            this.greenary_weight = 0.01;
        }
        else if(sumWeights!= 0){
            this.length_weight = length_weight/sumWeights;
            this.slope_weight = slope_weight/sumWeights;
            this.max_speed_weight_weight = max_speed_weight_weight/sumWeights;
            this.turning_cost_weight = turning_cost_weight/sumWeights;
            this.greenary_weight =greenary_weight;
        }else{
            this.length_weight = 0.2;
            this.slope_weight = 0.2;
            this.max_speed_weight_weight = 0.2;
            this.turning_cost_weight = 0.2;
            this.greenary_weight = 0.2;

        }

    }

    public Double getGreenary_weight() {
        return greenary_weight;
    }

    public Double getLength_weight() {
        return length_weight;
    }

    public Double getMax_speed_weight_weight() {
        return max_speed_weight_weight;
    }

    public Double getSlope_weight() {
        return slope_weight;
    }

    public Double getTurning_cost_weight() {
        return turning_cost_weight;
    }
}
