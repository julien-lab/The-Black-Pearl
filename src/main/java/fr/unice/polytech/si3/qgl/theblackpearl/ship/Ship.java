package fr.unice.polytech.si3.qgl.theblackpearl.ship;

import com.fasterxml.jackson.annotation.*;
import fr.unice.polytech.si3.qgl.theblackpearl.decisions.Sailor;
import fr.unice.polytech.si3.qgl.theblackpearl.ship.entities.Entity;
import fr.unice.polytech.si3.qgl.theblackpearl.shape.Position;
import fr.unice.polytech.si3.qgl.theblackpearl.shape.Shape;
import fr.unice.polytech.si3.qgl.theblackpearl.ship.entities.Rudder;
import fr.unice.polytech.si3.qgl.theblackpearl.ship.entities.Oar;
import fr.unice.polytech.si3.qgl.theblackpearl.ship.entities.Sail;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

@JsonIgnoreProperties(value = {"gouvernail","listRames", "nbRame"})
@JsonTypeName("ship")
public class Ship {

    private String type;
    private int life;
    private Position position;
    private String name;
    private Deck deck;
    private ArrayList<Entity> entities;
    private Shape shape;

    @JsonCreator
    public Ship(@JsonProperty("type") String type, @JsonProperty("life") int life, @JsonProperty("position")  Position position,
                @JsonProperty("name")  String name, @JsonProperty("deck") Deck deck, @JsonProperty("entities") ArrayList<Entity> entities, @JsonProperty("shape")  Shape shape) {
        this.type = type;
        this.life = life;
        this.position = position;
        this.name = name;
        this.deck = deck;
        this.entities = entities;
        this.shape = shape;
    }

    public String getType() {
        return type;
    }

    public void setType(String aType) {
        type = aType;
    }

    public Position getPosition() {
        return position;
    }

    public Deck getDeck() {
        return deck;
    }

    public ArrayList<Entity> getEntities() {
        return entities;
    }

    public Ship clone(){
        ArrayList<Entity> cloneEntities = new ArrayList<>();
        for(Entity e : entities){
            if(e instanceof Sail){
                cloneEntities.add(((Sail) e).clone());
            }
            cloneEntities.add(e);
        }
        return new Ship(this.type, this.life , this.position.clone(), this.name, this.deck, cloneEntities, this.shape);
    }

    public Rudder getGouvernail(){
        for (Entity e : this.getEntities()) if (e instanceof Rudder) return ((Rudder) e);
        return null;
    }

    public List<Oar> getListRames(){
        List<Oar> listOars = new ArrayList<>();
        for (Entity c : getEntities()){
            if (c instanceof Oar){
                listOars.add((Oar) c);
            }
        }
        return listOars;
    }

    public int[] nbSailorAndOarConfiguration(double angle, List<Oar> nombreOars){
        double calculateAngles=-(Math.PI/2)-Math.PI/ nombreOars.size();
        int i;
        for (i=-nombreOars.size()/2; ; i++){
            calculateAngles += Math.PI/ nombreOars.size();
            if (calculateAngles>=(angle - Math.pow(5.0, -6.0))  && calculateAngles<=(angle + Math.pow(5.0, -6.0))){
                break;
            }
            if (i> nombreOars.size()/2) return null;
        }
        int[] nbSailorsOnPortStarBoard = new int[2];
        int b=0;
        if (i>0) {
            while ( i < (nombreOars.size() / 2) ) {
                i += 1;
                b += 1;
            }
            nbSailorsOnPortStarBoard[0]=b;
            nbSailorsOnPortStarBoard[1]=i;
        }
        else if (i<0){
            i=-i;
            while ( i < (nombreOars.size() / 2) ) {
                i += 1;
                b += 1;
            }
            nbSailorsOnPortStarBoard[0]=i;
            nbSailorsOnPortStarBoard[1]=b;
        }
        else {
            i= nombreOars.size()/2;
            b= nombreOars.size()/2;
            nbSailorsOnPortStarBoard[0]=i;
            nbSailorsOnPortStarBoard[1]=b;
        }
        return nbSailorsOnPortStarBoard;
    }

    public Shape getShape() {
        return shape;
    }

    @Override
    public String toString() {
        return "Bateau{" +
                "type='" + type + '\'' +
                ", life=" + life +
                ", position=" + position +
                ", name='" + name + '\'' +
                ", deck=" + deck +
                ", entities=" + Arrays.toString(entities.toArray()) +
                ", shape=" + shape +
                '}';
    }

    public List<Double> achievableAnglesWithOars() {
        List<Double> achievableAngles = new ArrayList<>();
        int nbOars = getNbRame();
        for( int i=1 ; i<=nbOars/2 ; i++){
            achievableAngles.add(i*Math.PI/nbOars);
            achievableAngles.add(i*-Math.PI/nbOars);
        }
        achievableAngles.add(0.0);
        return achievableAngles;
    }

    public List<Double> optimizedAchievableAngle(double idealAngleTowardsCheckPoint){
        List<Double> optimizedAchievableAngles = achievableAnglesWithOars();

        optimizedAchievableAngles.sort(Comparator.comparingDouble( angle -> Math.abs(idealAngleTowardsCheckPoint-angle)));

        return optimizedAchievableAngles;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public boolean isOnOarNotUsed(Sailor m) {
        for(Entity e: entities){
            if(e instanceof Oar && e.isAvailable()){
                if(e.getX()==m.getX() && e.getY()==m.getY()){
                    e.setAvailable(false);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isOnRudderNotUsed(Sailor m) {
        for(Entity e: entities){
            if(e instanceof Rudder && e.isAvailable()){
                if(e.getX()==m.getX() && e.getY()==m.getY()){
                    e.setAvailable(false);
                    return true;
                }
            }
        }
        return false;
    }

    public int nbSailorsOnStarBoard(){
        int nbSailorsStarBoard=0;
        for(Entity e : entities){
            if(e instanceof Oar && e.getY()==this.getDeck().getWidth()-1 && !e.isAvailable()){
                nbSailorsStarBoard++;
            }
        }
        return nbSailorsStarBoard;
    }

    public int nbSailorsOnPort(){
        int nbSailorsOnPort=0;
        for(Entity e : entities){
            if(e instanceof Oar && e.getY()==0  && !e.isAvailable()){
                nbSailorsOnPort++;
            }
        }
        return nbSailorsOnPort;
    }

    public int getNbRame() {
        int nbRame =0;
        for(Entity e : getEntities()){
            if(e instanceof Oar){
                nbRame++;
            }
        }
        return nbRame;
    }

    public int nbSail() {
        int nbSail = 0;
        for(Entity e : getEntities()){
            if(e instanceof Sail){
                nbSail++;
            }
        }
        return nbSail;
    }


    public int nbOpennedSail() {
        int nbOpenSail = 0;
        for(Entity e : getEntities()){
            if(e instanceof Sail){
                if(((Sail) e).isOpenned()){
                    nbOpenSail++;
                }
            }
        }
        return nbOpenSail;
    }

    public boolean isOnSailNotUsedNotOppened(Sailor m) {
        for(Entity e: entities){
            if(e instanceof Sail && e.isAvailable()){
                if(e.getX()==m.getX() && e.getY()==m.getY() && !((Sail) e).isOpenned()){
                    e.setAvailable(false);
                    ((Sail) e).setOpenned(true);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean isOnSailNotUsedOppened(Sailor m) {
        for(Entity e: entities){
            if(e instanceof Sail && e.isAvailable()){
                if(e.getX()==m.getX() && e.getY()==m.getY() && ((Sail) e).isOpenned()){
                    e.setAvailable(false);
                    ((Sail) e).setOpenned(false);
                    return true;
                }
            }
        }
        return false;
    }
}
