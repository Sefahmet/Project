package com.example.project.Entity;



public class CreatedEdge {

    private String id;
    private Default_Edge incomingEdge;
    private Default_Edge outgoingEdge;
    private String turningCost;

    public CreatedEdge(String id,Default_Edge incomingEdge,Default_Edge outgoingEdge,String turningCost){
        this.id = id;
        this.incomingEdge = incomingEdge;
        this.outgoingEdge = outgoingEdge;
        this.turningCost = turningCost;
    }
    public CreatedEdge(Default_Edge incomingEdge,Default_Edge outgoingEdge){
        this.id = incomingEdge.getId() +" _ "+ outgoingEdge.getId();
        this.incomingEdge = incomingEdge;
        this.outgoingEdge = outgoingEdge;
        this.turningCost = "No_Turns";
    }
    public void print() {
        System.out.println(id + " " + incomingEdge.getId() + " " + outgoingEdge.getId() + " " + turningCost);
    }

    public Default_Edge getIncomingEdge() {
        return incomingEdge;
    }

    public Default_Edge getOutgoingEdge() {
        return outgoingEdge;
    }

    public String getId() {
        return id;
    }

    public String getTurningCost() {
        return turningCost;
    }

    public void setIncomingEdge(Default_Edge incomingEdge) {
        this.incomingEdge = incomingEdge;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setOutgoingEdge(Default_Edge outgoingEdge) {
        this.outgoingEdge = outgoingEdge;
    }

    public void setTurningCost(String turningCost) {
        this.turningCost = turningCost;
    }

    @Override
    public String toString() {
            return id + " "+incomingEdge.getOsmid()+" "+ outgoingEdge.getOsmid()+ " "+turningCost;
            }



}
