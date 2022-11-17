package fr.unice.polytech.si3.qgl.theblackpearl.decisions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.unice.polytech.si3.qgl.theblackpearl.actions.MOVING;
import fr.unice.polytech.si3.qgl.theblackpearl.ship.entities.Entity;
import fr.unice.polytech.si3.qgl.theblackpearl.ship.entities.Rudder;
import fr.unice.polytech.si3.qgl.theblackpearl.ship.entities.Oar;
import fr.unice.polytech.si3.qgl.theblackpearl.ship.entities.Sail;

import java.util.List;

public class Sailor {
    private int id;
    private int x;
    private int y;
    private String name;
    private boolean available;
    private String actionToDo="";
    private boolean canMove;

    @JsonCreator
    public Sailor(@JsonProperty("id") int id, @JsonProperty("x") int x, @JsonProperty("y") int y, @JsonProperty("name") String name) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.name = name;
        this.available =true;
        this.canMove=true;
    }

    public Sailor clone(){
        return new Sailor(this.id, this.x, this.y, this.name);
    }

    public String getActionToDo(){
        return actionToDo;
    }

    public boolean isAvailable(){
        return available;
    }

    public void setAvailable(boolean available){
        this.available = available;
    }

    public int getId() {
        return id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public void setCanMove(boolean b) {
        this.canMove=b;
    }

    public boolean canMove() {
        return canMove;
    }

    @Override
    public String toString() {
        return "Marin{" +
                "x=" + x +
                ", y=" + y +
                ", id=" + id +
                ", name='" + name + '\'' +
                '}';
    }

    public void newRoundSailorReset() {
        this.actionToDo ="";
        this.available =true;
    }

    public void setActionToDo(String actionToDo) {
        this.actionToDo = actionToDo;
    }

    public void moveSailor(int xdistance, int ydistance) {
        this.setX(this.x+xdistance);
        this.setY(this.y+ydistance);
        this.canMove=false;
    }

    public MOVING moveSailorToRudder(List<Entity> entities){
        int sailorMovements;
        for (Entity entity : entities) {
            if (entity instanceof Rudder) {
                sailorMovements = (Math.abs(entity.getX() - this.getX()) + Math.abs(entity.getY() - this.getY()));
                if (sailorMovements < 6 && this.available) {
                    actionToDo ="tournerGouvernail";
                    this.available =false;
                    return new MOVING(getId(), "MOVING", entity.getX() - this.getX(), entity.getY() - this.getY());
                }
            }
        }
        return null;
    }

    public MOVING moveSailorToSail(List<Entity> entities, boolean openSail){
        int sailorMovements;
        for (Entity entity : entities) {
            if (entity instanceof Sail) {
                if (this.available) {
                    sailorMovements = (Math.abs(entity.getX() - this.getX()) + Math.abs(entity.getY() - this.getY()));
                    if (sailorMovements < 6) {
                        if (openSail) actionToDo = "HisserVoile";
                        else actionToDo = "BaisserLaVoile";
                        this.available = false;
                        return new MOVING(getId(), "MOVING", entity.getX() - this.getX(), entity.getY() - this.getY());
                    }
                }
            }
        }
        return null;
    }

    public MOVING moveSailorToOar(List<Entity> Entities, int nbMissingSailorsOnLeft, int nbMissingSailorsOnRight, int shipWidth){
        int entityReceivesSailor=-1;
        int sailorMovements;
        int shorterMovement=6;
        for (int i=0;i<Entities.size();i++) {
            if (Entities.get(i) instanceof Oar) {
                sailorMovements = (Math.abs(Entities.get(i).getX() - this.getX()) + Math.abs(Entities.get(i).getY() - this.getY()));
                if (nbMissingSailorsOnLeft > 0 && Entities.get(i).getY() == 0) { // la gauche du bateau est à y = 0
                    if (sailorMovements < 6 && this.available && sailorMovements < shorterMovement) {
                        entityReceivesSailor = i;
                        shorterMovement = sailorMovements;
                    }
                } else if (nbMissingSailorsOnRight > 0 && Entities.get(i).getY() == shipWidth - 1 /*ici normalement */) {
                    if (sailorMovements < 6 && this.available && sailorMovements < shorterMovement) {
                        entityReceivesSailor = i;
                        shorterMovement = sailorMovements;
                    }
                }
            }
        }
        if (entityReceivesSailor!=-1) {
            this.available =false;
            this.actionToDo ="Ramer";
            return new MOVING(getId(),"MOVING",Entities.get(entityReceivesSailor).getX() - this.getX(),Entities.get(entityReceivesSailor).getY() - this.getY());
        }
        else{
            return null;
        }
    }

}