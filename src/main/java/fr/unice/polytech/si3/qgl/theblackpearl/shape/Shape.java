package fr.unice.polytech.si3.qgl.theblackpearl.shape;

import com.fasterxml.jackson.annotation.*;


@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type",
        visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Circle.class, name = "circle"),
        @JsonSubTypes.Type(value = Rectangle.class, name = "rectangle"),
        @JsonSubTypes.Type(value = Polygon.class, name = "polygon")
})
@JsonIgnoreProperties(value = { "type"})
public abstract class Shape {
    protected String type;
    private Position centerCoordinates;

    public Position getCenterCoordinates() { return centerCoordinates; }

    public void setCenterCoordinates(Position centerCoordinates) { this.centerCoordinates = centerCoordinates; }

    public void setOrientationCentre(double orientationCentre) { this.centerCoordinates = new Position(centerCoordinates.getX(), centerCoordinates.getY(),orientationCentre); }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


}
