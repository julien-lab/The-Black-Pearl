package fr.unice.polytech.si3.qgl.theblackpearl.ship.entities;

import com.fasterxml.jackson.annotation.*;
import fr.unice.polytech.si3.qgl.theblackpearl.decisions.Sailor;
import fr.unice.polytech.si3.qgl.theblackpearl.actions.MOVING;

import java.util.ArrayList;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type",
        visible = false
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Rudder.class, name = "rudder"),
        @JsonSubTypes.Type(value = Oar.class, name = "oar"),
        @JsonSubTypes.Type(value = Watch.class, name = "watch"),
        @JsonSubTypes.Type(value = Sail.class, name = "sail"),
})
@JsonIgnoreProperties(value = { "isUsed" , "used", "type", "angleRealise"})
public abstract class Entity {
    protected String type;
    private int x;
    private int y;
    @JsonIgnore
    private boolean available;
    @JsonCreator
    public Entity(@JsonProperty("x") int x, @JsonProperty("y") int y) {
        this.x = x;
        this.y = y;
        this.available =true;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isAvailable(){
        return available;
    }

    public void setAvailable(boolean available){
        this.available = available;
    }

    @Override
    public String toString() {
        return "x : " + this.x +", y : " + this.y;
    }

    public static ArrayList<Entity> deleteEntity(ArrayList<Entity> entities, boolean sailorLeftPlace, boolean sailorRightPlace, Sailor sailor, MOVING moving){
        if (entities!=null) {
            for (int b = 0; b < entities.size(); b++) {
                if (entities.get(b) instanceof Oar && (sailorLeftPlace || sailorRightPlace)) {
                    if ((entities.get(b).getY() - sailor.getY()) == moving.getYDistance() && (entities.get(b).getX() - sailor.getX()) == moving.getXDistance()) {
                        entities.remove(b);
                        break;
                    }
                }
            }
            return entities;
        }
        else return null;
    }
}
