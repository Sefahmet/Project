package com.example.project.Model;

import com.example.project.Entity.CreatedEdge;
import com.example.project.Entity.Default_Edge;
import com.example.project.Entity.GraphFeatures;

import java.util.*;

public class EdgeCreator {
    public static void isItTurnLeft(Default_Edge incoming, List<Default_Edge> outgoings,
                                    List<Default_Edge> incomings,HashMap<String,CreatedEdge> createdEdgeHashMap) throws Exception {
        // Check the points degree are 2(in non-directed case)
        if (outgoings.size()==1 & incomings.size()<=1){
            if(!outgoings.isEmpty() || incomings.isEmpty() || (outgoings.get(0).getId() == incomings.get(0).getId())){
                createdEdgeHashMap = creatEdge(incoming,outgoings.get(0),"No_Turns",createdEdgeHashMap);
            }
        }
        else if (outgoings.size()>1 & incomings.isEmpty()){
            for(Default_Edge outhoing :outgoings){
                createdEdgeHashMap =creatEdge(incoming,outhoing,"No_Income",createdEdgeHashMap);
            }
        }

        else if(isAllNonConsideredEdge(incoming,outgoings,incomings)){
            for (Default_Edge outgoing: outgoings) {
                createdEdgeHashMap =creatEdge(incoming,outgoing,"Not_Considered",createdEdgeHashMap);
            }
        }
        else{
            Map<Default_Edge, Double> outgoingOrderedAngle = incoming.getOrderedAngles(outgoings);
            Map<Default_Edge, Double> incomingOrderedAngles = incoming.getOrderedAngles(outgoings);
            Double min_incoming_angle = Double.MAX_VALUE;
            for(Double value : incomingOrderedAngles.values()){
                if (value<min_incoming_angle){
                    min_incoming_angle = value;
                }
            }

            for(Default_Edge outgoingEdge:outgoingOrderedAngle.keySet()){
                Double angle = outgoingOrderedAngle.get(outgoingEdge);
                if (180<angle & angle<320 & angle>min_incoming_angle){
                    createdEdgeHashMap =creatEdge(incoming,outgoingEdge,"Left",createdEdgeHashMap);

                }else{
                    createdEdgeHashMap =creatEdge(incoming,outgoingEdge,"Right",createdEdgeHashMap);
                }
            }
        }

    }
    private static boolean isAllNonConsideredEdge(Default_Edge incoming, List<Default_Edge> outgoings, List<Default_Edge> incomings) {
        List<String> nonConsideredRoadType = Arrays.asList("service", "pedestrian","path","cycleway");
        if(!nonConsideredRoadType.contains(incoming.getRoadType())){
            return false;
        }
        for(Default_Edge edge:outgoings){
            if(!nonConsideredRoadType.contains(edge.getRoadType())){
                return false;
            }
        }
        for(Default_Edge edge:incomings){
            if(!nonConsideredRoadType.contains(edge.getRoadType())){
                return false;
            }
        }

        return true;

    }
    private static HashMap<String,CreatedEdge> creatEdge(Default_Edge incoming, Default_Edge outgoing,String tag,HashMap<String,CreatedEdge> createdEdgeHashMap) throws Exception {
        try{
            String inc = incoming.getU_id() + " " + incoming.getV_id();
            String out = outgoing.getU_id() + " " + outgoing.getV_id();
            CreatedEdge created_edge = new CreatedEdge(inc + "_" + out, incoming,
                                                        outgoing, tag);
            createdEdgeHashMap.put(inc + "_" + out,created_edge);
            return createdEdgeHashMap;

        }catch (Exception e){
            e.printStackTrace();
        }
        throw new Exception("edge couldnt create");
    }

}
