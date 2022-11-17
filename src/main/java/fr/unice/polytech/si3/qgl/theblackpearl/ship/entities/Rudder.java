package fr.unice.polytech.si3.qgl.theblackpearl.ship.entities;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Rudder extends Entity{

    @JsonCreator
    public Rudder(@JsonProperty("x") int x, @JsonProperty("y") int y) {
        super(x, y);
        type = "rudder";
    }
    private double angleAchieved;

    public double getAngleAchieved() {
        return angleAchieved;
    }

    public void setAngleAchieved(double angleAchieved) {
        this.angleAchieved = angleAchieved;
    }

    public double rudderAngle(double angleToDO) {
        if (angleToDO == 0) return 0.0;
        else if ((angleToDO == Math.PI/2) || (angleToDO == Math.PI)) return 45 * Math.PI / 180;
        else if (angleToDO == -Math.PI/2) return -45 * Math.PI / 180;
        else if (angleToDO > 0 && angleToDO > 45 * Math.PI / 180) return 45 * Math.PI / 180;
        else if (angleToDO > 0) return angleToDO % (45 * Math.PI / 180);
        else if (angleToDO < 0 && angleToDO < -45 * Math.PI / 180) return -45 * Math.PI / 180;
        else return angleToDO % (45 * Math.PI / 180);
    }

}
