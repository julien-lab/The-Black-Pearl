package fr.unice.polytech.si3.qgl.theblackpearl.shape;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("circle")
public class Circle extends Shape {
    private double radius;

    @JsonCreator
    public Circle(@JsonProperty("radius") double radius) {
        this.type = "circle";
        this.radius = radius;
    }

    public double getRadius() {
        return radius;
    }

    @Override
    public String toString() {
        return "Circle{" +
                "type=" + this.getType() +
                "radius=" + radius +
                '}';
    }
}
