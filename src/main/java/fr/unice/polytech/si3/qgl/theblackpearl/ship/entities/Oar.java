package fr.unice.polytech.si3.qgl.theblackpearl.ship.entities;

import com.fasterxml.jackson.annotation.*;
import fr.unice.polytech.si3.qgl.theblackpearl.engine.InitGame;

import java.util.ArrayList;


@JsonTypeName("oar")
public class Oar extends Entity{
    private boolean isUsed;

    @JsonCreator
    public Oar(@JsonProperty("x") int x, @JsonProperty("y") int y) {
        super(x, y);
        type = "oar";
        this.isUsed=false;
    }

    @Override
    public String toString() {
        return "Oar{" +
                "x=" + this.getX() + "," +
                "y=" + this.getY() +
                "}";
    }

    public static void setOarsUsed(InitGame game, ArrayList<Entity> copyEntityList){
        ArrayList<Entity> listUsedEntities = ((ArrayList<Entity>) game.getShip().getEntities().clone());
        listUsedEntities.removeAll(copyEntityList);
        ArrayList<Entity> listEntities = game.getShip().getEntities();
        for (Entity e : listEntities){
            for (Entity e2 : listUsedEntities){
                if (e==e2){
                    ((Oar) e).setUsed(true);
                }
            }
        }
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }

    public boolean isUsed() {
        return this.isUsed;
    }
}
