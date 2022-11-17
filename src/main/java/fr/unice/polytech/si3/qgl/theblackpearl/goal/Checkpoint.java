package fr.unice.polytech.si3.qgl.theblackpearl.goal;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.unice.polytech.si3.qgl.theblackpearl.shape.Position;
import fr.unice.polytech.si3.qgl.theblackpearl.shape.Shape;

public class Checkpoint {
    private Position position;
    private Shape shape;

    @JsonCreator
    public Checkpoint(@JsonProperty("position") Position position, @JsonProperty("shape") Shape shape) {
        this.position = position;
        this.shape = shape;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public Shape getShape() {
        return shape;
    }

    public void setShape(Shape shape) {
        this.shape = shape;
    }

    @Override
    public String toString() {
        return "Checkpoint{" +
                "position=" + position +
                ", shape=" + shape +
                '}';
    }

}
