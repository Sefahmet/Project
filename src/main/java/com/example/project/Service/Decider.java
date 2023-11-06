package com.example.project.Service;


public class Decider {
    public static Double maxSpeedDecider(Double maxSpeed){
        if (maxSpeed<10){
            return 0.1;
        }
        else if (maxSpeed<30){
            return 0.25;
        }else if(maxSpeed<50) {
            return 0.50;
        }else if(maxSpeed<70) {
            return 0.75;
        }else{
            return 1.0;
        }
    }
    public static Double nullMaxSpeedDecider(String highwayType){
        switch (highwayType){
            case "residential":
                return 30.0;
            case"secondary":
                return 50.0;
            case "secondary_link":
                return 50.0;
            case "primary":
                return 50.0;
            case "primary_link":
                return 50.0;
            default:
                return 1.0;
        }
    }
    public static Double turningCostDecider(String turningType){
        Double turningCost;
        Double Constant = 0.1;
        Double leftCost = 30.0;

        switch (turningType) {
            case "Right":
                turningCost = Constant;
                break;
            case "Left":
                turningCost = Constant+leftCost;
                break;
            case "Continous":
                turningCost = Constant ;
                break;
            case "No_Turns":
                turningCost = 0.0;
                break;
            case "Cross":
                turningCost = Constant ;
                break;
            case "Second_Right":
                turningCost = Constant ;
                break;
            case "Cross_Right":
                turningCost = Constant ;
                break;
            case "Cross_Left":
                turningCost = Constant;
                break;
            case "Not_Considered":
                turningCost = Constant;
                break;
            case "No_Income":
                turningCost = Constant;
                break;
            default:
                turningCost = Constant;
                break;
        }

        return turningCost;


    }
    public  static Double slopeDecider(Double slope){

        double slope_per = slope *100;
        if (slope_per<=-3){
            return 0.1;
        }
        else if (slope_per<15) {
            return 0.1+(slope_per+3.0)/18.0;
        }
        else{
            return 1.0;
        }
    }


}

