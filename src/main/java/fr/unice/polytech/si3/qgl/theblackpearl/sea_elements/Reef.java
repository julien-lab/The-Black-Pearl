package fr.unice.polytech.si3.qgl.theblackpearl.sea_elements;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import fr.unice.polytech.si3.qgl.theblackpearl.shape.*;

@JsonTypeName("reef")
public class Reef extends VisibleEntity {

    @JsonCreator
    public Reef(@JsonProperty("type") String type, @JsonProperty("position") Position position, @JsonProperty("shape") Shape shape){
        super(type, position,shape);
    }

    @Override
    public String toString() {
        return "Recif{ "+
                " position=" + position +
                ", shape=" + shape +
                '}';
    }
}
