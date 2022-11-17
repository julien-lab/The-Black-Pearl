package fr.unice.polytech.si3.qgl.theblackpearl.referee;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.unice.polytech.si3.qgl.theblackpearl.decisions.Calculator;
import fr.unice.polytech.si3.qgl.theblackpearl.Cockpit;
import fr.unice.polytech.si3.qgl.theblackpearl.decisions.Sailor;
import fr.unice.polytech.si3.qgl.theblackpearl.shape.Position;
import fr.unice.polytech.si3.qgl.theblackpearl.engine.InitGame;
import fr.unice.polytech.si3.qgl.theblackpearl.engine.NextRound;
import fr.unice.polytech.si3.qgl.theblackpearl.goal.Checkpoint;
import fr.unice.polytech.si3.qgl.theblackpearl.goal.RegattaGoal;
import fr.unice.polytech.si3.qgl.theblackpearl.sea_elements.OtherShip;
import fr.unice.polytech.si3.qgl.theblackpearl.sea_elements.Stream;
import fr.unice.polytech.si3.qgl.theblackpearl.sea_elements.Reef;
import fr.unice.polytech.si3.qgl.theblackpearl.sea_elements.VisibleEntity;
import fr.unice.polytech.si3.qgl.theblackpearl.ship.entities.Entity;

import java.util.List;

public class Referee {
    private String initGame;
    private String nextRound;
    private Cockpit cockpit;
    private Calculator c;
    private int tour;
    private InitGame parsedInitGameReferee;
    private NextRound parsedNextRoundReferee;
    private ActionsRound parsedActionsRound;
    private ObjectMapper objectMapperReferee;
    private double rotationShip;
    private double speedShip;
    private boolean goThroughCheckpoint;

    public Referee(String initGame, String firstRound, Cockpit cockpit) {
        this.initGame = initGame;
        this.nextRound = firstRound;
        this.cockpit = cockpit;
        this.tour = 0;
        this.objectMapperReferee = new ObjectMapper();
        this.c = new Calculator();
    }

    public Referee(InitGame initGame, NextRound nextRound) {
        this.parsedInitGameReferee = initGame;
        this.parsedNextRoundReferee = nextRound;
        this.objectMapperReferee = new ObjectMapper();
        this.goThroughCheckpoint = false;
        this.c = new Calculator();
    }

    public void startGame(int nbTour) throws Exception {
        this.initGame();
        this.nextRound(nbTour);
    }

    public InitGame getParsedInitGameReferee() {
        return parsedInitGameReferee;
    }

    public NextRound getParsedNextRoundReferee() {
        return parsedNextRoundReferee;
    }

    public boolean startRound(String actions) throws Exception {
        this.rotationShip=0.;
        this.speedShip=0.0;
        this.resetEntities();
        this.resetSailors();
        this.getActions(actions);
        this.executeActions();
        return this.moveShip();
    }

    public void resetEntities() {
        for (Entity e : parsedInitGameReferee.getShip().getEntities()) {
            e.setAvailable(true);
        }
    }

    public void resetSailors() {
        for (Sailor m : parsedInitGameReferee.getSailors()) {
            m.setCanMove(true);
            m.setAvailable(true);
        }
    }


    public void initGame() {
        try {
            parsedInitGameReferee = objectMapperReferee.readValue(initGame, InitGame.class);
            this.cockpit.initGame(initGame);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        try {
            parsedNextRoundReferee = objectMapperReferee.readValue(nextRound, NextRound.class);
            this.cockpit.initGame(initGame);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void nextRound(int nbTour) throws Exception {
        while (!this.getFinishGame() && this.tour < nbTour) {
            System.out.println(this.tour + " : " + parsedInitGameReferee.getShip().getPosition());
            this.rotationShip=0.;
            this.speedShip=0.0;
            this.resetEntities();
            this.resetSailors();
            this.getActions(this.cockpit.nextRound(nextRound));
            this.executeActions();
            this.moveShip();
            this.updateNextRound();
            this.tour++;
        }
        System.out.println("SCORE DE LA PARTIE : " + ((nbTour-this.tour)+1));
    }

    public boolean getFinishGame() throws Exception {
        RegattaGoal regatta =  (RegattaGoal) parsedInitGameReferee.getGoal();
        List<Checkpoint> checkpoints = regatta.getCheckpoints();
        if(c.shipIsInsideCheckpoint(parsedInitGameReferee.getShip(), checkpoints.get(0))){
            regatta.removeCheckpoint();
        }
        return regatta.getCheckpoints().isEmpty();
    }

    public void getActions(String actionsRound) {
        actionsRound = "{" + "\"actions\":" + actionsRound + "}";
        try {
            this.parsedActionsRound = objectMapperReferee.readValue(actionsRound, ActionsRound.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public void executeActions() {
        for (ActionRound a : this.parsedActionsRound.getActionsRound()) {
            if (a instanceof MovingReferee) {
                ((MovingReferee) a).tryToMoveSailor(parsedInitGameReferee);
            }
        }

        for (ActionRound a : this.parsedActionsRound.getActionsRound()) {
            if (a instanceof OarReferee) {
                ((OarReferee) a).tryToOar(parsedInitGameReferee);
            }
            if (a instanceof TurnReferee) {
                this.rotationShip+=(((TurnReferee) a).tryToTurn(parsedInitGameReferee));
            }
            if(a instanceof LiftSailReferee){
                ((LiftSailReferee) a).tryToLiftSail(parsedInitGameReferee);
            }
            if(a instanceof LowerSailReferee){
                ((LowerSailReferee) a).tryToLowerSail(parsedInitGameReferee);
            }
        }
    }

    public boolean moveShip() throws Exception {
        //Calculs rames
        int nbOarOnPort = parsedInitGameReferee.getShip().nbSailorsOnPort();
        int nbOarOnStarBoard = parsedInitGameReferee.getShip().nbSailorsOnStarBoard();
        int nbOars = parsedInitGameReferee.getShip().getNbRame();
        this.rotationShip += c.calculateOarsRotation(nbOarOnPort,nbOarOnStarBoard, nbOars);

        //Calculs voiles
        int nbOpenSail = parsedInitGameReferee.getShip().nbOpennedSail();
        int nbSail = parsedInitGameReferee.getShip().nbSail();

        int N=0;
        int nbSteps=100;

        while(N<nbSteps){
            for(VisibleEntity v : parsedNextRoundReferee.getVisibleEntities()){
                if(v instanceof Stream){
                    if(c.shapesCollide(parsedInitGameReferee.getShip(), v)){
                        Position positionAfterStream = c.calculateInfluenceOfStream(parsedInitGameReferee.getShip().getPosition(), (Stream) v, nbSteps);
                        parsedInitGameReferee.getShip().setPosition(positionAfterStream);
                    }
                }
            }

            this.speedShip=0.;
            this.speedShip += c.calculateOarSpeed(nbOarOnPort+nbOarOnStarBoard,nbOars);
            if(nbSail>0)
                this.speedShip += c.calculateWindSpeed(nbOpenSail,nbSail,parsedNextRoundReferee.getWind(), parsedInitGameReferee.getShip());

            Position positionShipThisStep = c.calculateNewPositionShip(this.speedShip, this.rotationShip ,parsedInitGameReferee.getShip().getPosition(), nbSteps);
            parsedInitGameReferee.getShip().setPosition(positionShipThisStep);

            if(this.cockpit==null){
                RegattaGoal regatta = (RegattaGoal) parsedInitGameReferee.getGoal();
                if(c.shipIsInsideCheckpoint(parsedInitGameReferee.getShip(), regatta.getCheckpoints().get(0))){
                    this.goThroughCheckpoint = true;
                }
                if(this.testCollision()){
                    return true;
                }
            }
            N++;
        }
        return false;
    }

    public boolean testCollision() throws Exception {
        for(VisibleEntity v : parsedNextRoundReferee.getVisibleEntities()){
            if(v instanceof Reef || v instanceof OtherShip){
                if (c.shapesCollide(parsedInitGameReferee.getShip(), v)){
                    return true;
                }
            }
        }
        return false;
    }

    public void updateNextRound() {
        parsedNextRoundReferee.setShip(parsedInitGameReferee.getShip());
        try {
            this.nextRound =objectMapperReferee.writeValueAsString(parsedNextRoundReferee);
        }catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public boolean getGoThroughCheckpoint() {
        return this.goThroughCheckpoint;
    }
}
