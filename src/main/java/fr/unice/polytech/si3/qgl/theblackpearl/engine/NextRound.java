package fr.unice.polytech.si3.qgl.theblackpearl.engine;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import fr.unice.polytech.si3.qgl.theblackpearl.sea_elements.Wind;
import fr.unice.polytech.si3.qgl.theblackpearl.sea_elements.VisibleEntity;
import fr.unice.polytech.si3.qgl.theblackpearl.ship.Ship;

import java.util.List;

@JsonIgnoreProperties(value = { "bateau","vent"})
public class NextRound {
    @JsonSerialize
    private Ship ship;
    @JsonSerialize
    private List<VisibleEntity> visibleEntities;
    @JsonSerialize
    private Wind wind;

    @JsonCreator
    public NextRound(@JsonProperty("ship") Ship ship, @JsonProperty("visibleEntities") List<VisibleEntity> visibleEntities, @JsonProperty("wind") Wind wind){
        this.ship = ship;
        this.wind = wind;
        this.visibleEntities = visibleEntities;
    }

    public Ship getShip() {
        return this.ship;
    }

    public Wind getWind() { return this.wind; }

    public void setShip(Ship ship) {
        this.ship = ship;
    }

    public List<VisibleEntity> getVisibleEntities() {
        return visibleEntities;
    }

    public NextRound clone(){
        return new NextRound(this.ship.clone(), this.visibleEntities, this.wind);
    }
}
