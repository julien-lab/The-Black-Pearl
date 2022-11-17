package fr.unice.polytech.si3.qgl.theblackpearl.actions;

public class TURN extends Action{

    private double rotation;

    public TURN(int sailorId, double rotation) {
        super(sailorId, "TURN");
        this.rotation=rotation;
    }

    public String toString() {
        return "{\"sailorId\":" + getSailorId() + ",\"type\":\"TURN\",\"rotation\":" + this.rotation + "}";
    }

}
