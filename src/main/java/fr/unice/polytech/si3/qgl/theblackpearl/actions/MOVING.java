package fr.unice.polytech.si3.qgl.theblackpearl.actions;

public class MOVING extends Action{

    private int xdistance;
    private int ydistance;

    public MOVING(int sailorId, String type, int xdistance, int ydistance){
        super(sailorId,type);
        this.xdistance = xdistance;
        this.ydistance = ydistance;
    }

    public int getXDistance(){
        return xdistance;
    }

    public int getYDistance(){
        return ydistance;
    }

    @Override
    public String toString(){
        return "{" + "\"sailorId\": " + getSailorId()
                + "\",type\": " + getType()
                + "\",xdistance\": " + xdistance
                + "\",ydistance\": " + ydistance
                + "}";
    }

}
