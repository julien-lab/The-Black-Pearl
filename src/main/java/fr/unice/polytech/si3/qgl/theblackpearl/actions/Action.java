package fr.unice.polytech.si3.qgl.theblackpearl.actions;

public abstract class Action {
    private int sailorId;
    private String type;

    public Action(int sailorId, String type) {
        this.sailorId = sailorId;
        this.type = type;
    }

    public int getSailorId() {
        return sailorId;
    }

    public String getType() {
        return type;
    }

}
