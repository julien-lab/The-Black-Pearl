package fr.unice.polytech.si3.qgl.theblackpearl.sea_elements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
public class Wind {
    private double orientation;
    private double strength;

    @JsonCreator
    public Wind(@JsonProperty("orientation")double orientation, @JsonProperty("strength") double strength) {
        this.orientation = orientation;
        this.strength = strength;
    }

    public void setOrientation(double orientation){
        this.orientation=orientation;
    }

    public double getOrientation() {
        return this.orientation;
    }

    public double getStrength() {
        return this.strength;
    }

    @Override
    public String toString() {
        return "Vent{" +
                "orientation=" + orientation +
                ", strength=" + strength +
                '}';
    }
}
