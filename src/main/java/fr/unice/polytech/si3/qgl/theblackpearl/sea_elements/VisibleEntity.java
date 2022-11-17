package fr.unice.polytech.si3.qgl.theblackpearl.sea_elements;

import com.fasterxml.jackson.annotation.*;
import fr.unice.polytech.si3.qgl.theblackpearl.shape.Position;
import fr.unice.polytech.si3.qgl.theblackpearl.shape.Shape;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = OtherShip.class, name = "ship"),
        @JsonSubTypes.Type(value = Reef.class, name = "reef"),
        @JsonSubTypes.Type(value = Stream.class, name = "stream")
})
public abstract class VisibleEntity {
    protected String type;
    protected Position position;
    protected Shape shape;

    @JsonCreator
    VisibleEntity(@JsonProperty("type") String type, @JsonProperty("position") Position position, @JsonProperty("shape") Shape shape){
        this.type = type;
        this.position = position;
        this.shape = shape;
    }

    public Shape getShape(){
        return shape;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    @Override
    public String toString() {
        return "VisibleEntity{" +
                "type='" + type + '\'' +
                ", position=" + position +
                ", shape=" + shape +
                '}';
    }
}
