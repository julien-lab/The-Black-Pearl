package fr.unice.polytech.si3.qgl.theblackpearl.shape;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;

import java.util.ArrayList;
import java.util.Arrays;


@JsonTypeName("polygon")
public class Polygon extends Shape {
    private double orientation;
    private ArrayList<Point> vertices;
    private Position positionRelative;


    @JsonCreator
    public Polygon(@JsonProperty("orientation") double orientation, @JsonProperty("vertices") ArrayList<Point> vertices) {
        this.type = "polygon";
        this.orientation = orientation;
        this.vertices = vertices;
    }

    public ArrayList<Point> getVertices(){ return vertices; }

    public Position getPositionRelative() {
        return positionRelative;
    }

    public void setPositionRelative(Position positionRelative) {
        this.positionRelative = positionRelative;
    }

    @Override
    public String toString() {
        return "Polygon{" +
                "orientation=" + orientation +
                ", vertices=" + Arrays.toString(vertices.toArray()) +
                '}';
    }
}
