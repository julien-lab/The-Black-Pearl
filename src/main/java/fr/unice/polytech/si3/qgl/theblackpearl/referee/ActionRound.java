package fr.unice.polytech.si3.qgl.theblackpearl.referee;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        property = "type",
        visible = true)
@JsonSubTypes({
        @JsonSubTypes.Type(value = MovingReferee.class, name = "MOVING"),
        @JsonSubTypes.Type(value = OarReferee.class, name = "OAR"),
        @JsonSubTypes.Type(value = TurnReferee.class, name = "TURN"),
        @JsonSubTypes.Type(value = LiftSailReferee.class, name = "LIFT_SAIL"),
        @JsonSubTypes.Type(value = LowerSailReferee.class, name = "LOWER_SAIL"),
})
public class ActionRound {
    protected int sailorId;
    protected String type;

    public int getSailorId() {
        return sailorId;
    }

    public String getType() {
        return type;
    }
}
