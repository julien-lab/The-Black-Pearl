package fr.unice.polytech.si3.qgl.theblackpearl.shape;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("rectangle")
public class Rectangle extends Shape {
    private double width;
    private double height;
    private double orientationRectangle;

    @JsonCreator
    public Rectangle(@JsonProperty("width") double width,
                     @JsonProperty("height") double height, @JsonProperty("orientation") double orientationRectangle) {
        this.type = "rectangle";
        this.width = width;
        this.height = height;
        this.orientationRectangle = orientationRectangle;
    }

    public double getWidth() {
        return width;
    }

    public double getHeight() {
        return height;
    }

    public double getOrientationRectangle() {
        return orientationRectangle;
    }


    @Override
    public String toString() {
        return "Rectangle{" +
                "type=" + this.getType() +
                ",width=" + width +
                ", height=" + height +
                ", orientation=" + orientationRectangle +
                '}';
    }
}

