package fr.unice.polytech.si3.qgl.theblackpearl.engine;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import fr.unice.polytech.si3.qgl.theblackpearl.decisions.Sailor;
import fr.unice.polytech.si3.qgl.theblackpearl.goal.Goal;
import fr.unice.polytech.si3.qgl.theblackpearl.goal.RegattaGoal;
import fr.unice.polytech.si3.qgl.theblackpearl.ship.Ship;

import java.util.ArrayList;

public class InitGame {
    private Goal goal;
    private int shipCount;
    private Ship ship;
    private ArrayList<Sailor> sailors;

    @JsonCreator
    public InitGame(@JsonProperty("goal") Goal goal, @JsonProperty("shipCount") int shipCount, @JsonProperty("ship") Ship ship, @JsonProperty("sailors") ArrayList<Sailor> sailors){
        this.goal = goal;
        this.shipCount  = shipCount;
        this.ship = ship;
        this.sailors = sailors;
    }

    public Ship getShip() {
        return this.ship;
    }

    public Goal getGoal(){
        return this.goal;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }

    public ArrayList<Sailor> getSailors() {
        return sailors;
    }

    @Override
    public String toString() {
        return "InitGame{" +
                "goal=" + goal +
                ", shipCount=" + shipCount +
                ", ship=" + ship +
                ", sailors=" + sailors +
                '}';
    }

    public InitGame clone(){
        Goal cloneGoal;
        if(goal instanceof RegattaGoal){
            cloneGoal = ((RegattaGoal) goal).clone();
        }else{
            cloneGoal = goal;
        }
        Ship cloneShip = ship.clone();
        ArrayList<Sailor> cloneSailors = new ArrayList<>();
        for(Sailor m : sailors){
            cloneSailors.add(m.clone());
        }
        return new InitGame(cloneGoal, shipCount, cloneShip, cloneSailors);
    }

    public Sailor getSailorById(int sailorId) {
        for(Sailor m : sailors){
            if(m.getId() == sailorId){
                return m;
            }
        }
        return null;
    }
}
