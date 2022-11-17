package fr.unice.polytech.si3.qgl.theblackpearl.decisions;

import fr.unice.polytech.si3.qgl.theblackpearl.actions.Action;
import fr.unice.polytech.si3.qgl.theblackpearl.actions.MOVING;
import fr.unice.polytech.si3.qgl.theblackpearl.engine.InitGame;
import fr.unice.polytech.si3.qgl.theblackpearl.goal.RegattaGoal;
import fr.unice.polytech.si3.qgl.theblackpearl.sea_elements.Wind;
import fr.unice.polytech.si3.qgl.theblackpearl.shape.Position;
import fr.unice.polytech.si3.qgl.theblackpearl.shape.Rectangle;
import fr.unice.polytech.si3.qgl.theblackpearl.ship.entities.Entity;
import fr.unice.polytech.si3.qgl.theblackpearl.ship.entities.Rudder;
import fr.unice.polytech.si3.qgl.theblackpearl.ship.entities.Sail;

import java.util.ArrayList;
import java.util.List;

import static fr.unice.polytech.si3.qgl.theblackpearl.ship.entities.Entity.deleteEntity;

public class ControlRoom {

    private int[][] initialSailorPositions;
    private Wind wind;
    private InitGame parsedInitGame;
    int optimizedAchievableAnglePosition =0;
    private boolean continueOarConfiguration =true;
    private List<Action> actionsNextRound;
    private ArrayList<Entity> entityListCopy;
    private ArrayList<Action> temporaryNextRoundActions;
    private List<Sailor> busySailors;

    public ControlRoom(InitGame game, Wind wind, List<Action> actionsNextRound){
        this.parsedInitGame =game;
        this.wind = wind;
        this.actionsNextRound=actionsNextRound;
    }

    public void createSailorArray(){
        initialSailorPositions = new int[parsedInitGame.getSailors().size()][2];
    }
    public void takeIntoAccountSailorPosition(){
        for (int b = 0; b< parsedInitGame.getSailors().size(); b++){
            initialSailorPositions[b][0] = parsedInitGame.getSailors().get(b).getX();
            initialSailorPositions[b][1] = parsedInitGame.getSailors().get(b).getY();
        }
    }

    public void resetSailorsPosition(){
        int positionnementMarins =0;
        for (Sailor sailor : parsedInitGame.getSailors()) {
            sailor.setX(initialSailorPositions[positionnementMarins][0]);
            sailor.setY(initialSailorPositions[positionnementMarins][1]);
            positionnementMarins++;
        }
    }

    public void oarsPreConfiguration(boolean doesNotWorkFirstRound, int nbRounds, int[] nbSailorsToPlaceCopy, List<Double> optimizedAchievableAngle, Calculator calculator, int optimzedAchievableAnglePosition, List<Sailor> busySailors) {
        if (nbRounds != 0) {
            if (this.optimizedAchievableAnglePosition < optimizedAchievableAngle.size() - 1) {
                if (!doesNotWorkFirstRound) {
                    resetSailorsPosition();
                    for (Sailor m : parsedInitGame.getSailors()) if (!busySailors.contains(m)) m.setAvailable(true);
                    calculator.setNumberSailorsToPlace(nbSailorsToPlaceCopy.clone());
                    calculator.decrementNumberSailorsToPlace(nbRounds, true, true);
                }
                if (calculator.getNumberSailorsToPlace()[0] < 0 || calculator.getNumberSailorsToPlace()[1] < 0) {
                    ++optimzedAchievableAnglePosition;
                    this.optimizedAchievableAnglePosition = optimzedAchievableAnglePosition;
                    calculator.setNumberSailorsToPlace(parsedInitGame.getShip().nbSailorAndOarConfiguration(optimizedAchievableAngle.get(optimzedAchievableAnglePosition), parsedInitGame.getShip().getListRames()));
                } else this.continueOarConfiguration = false;
            }
        }
    }

    private Calculator calculatorConfiguration(List<Double> optimizedAchievableAngle){
        Calculator calculator = new Calculator();
        calculator.setNumberSailorsToPlace(parsedInitGame.getShip().nbSailorAndOarConfiguration(optimizedAchievableAngle.get(0), parsedInitGame.getShip().getListRames()));
        calculator.setNumberSailorsToPlaceCopy(calculator.getNumberSailorsToPlace().clone());
        return calculator;
    }

    private void initializeOarConfiguration(int optimizedAchievableAnglePosition, List<Sailor> busySailors){
        this.busySailors =busySailors;
        this.optimizedAchievableAnglePosition =optimizedAchievableAnglePosition;
        createSailorArray();
        this.continueOarConfiguration =true;
        takeIntoAccountSailorPosition();
        entityListCopy = new ArrayList<>();
        temporaryNextRoundActions = new ArrayList<>();
    }

    public double OarConfiguration(List<Double> optimizedAchievableAngle, int optimizedAchievableAnglePosition, List<Sailor> busySailors){
        initializeOarConfiguration(optimizedAchievableAnglePosition,busySailors);
        Calculator calculateur = calculatorConfiguration(optimizedAchievableAngle);
        optimizedSailorPositionning(calculateur,optimizedAchievableAngle);
        actionsNextRound.addAll(temporaryNextRoundActions);
        return optimizedAchievableAngle.get(this.optimizedAchievableAnglePosition);
    }

    public void optimizedSailorPositionning(Calculator calculator , List<Double> optimizedAchievableAngle){
        boolean doesNotWorkForFirstRound = true;
        int nbRounds=0;
        do {
            boolean leftSailor=false;
            boolean rightSailor=false;
            temporaryNextRoundActions = new ArrayList<>();
            oarsPreConfiguration(doesNotWorkForFirstRound,nbRounds,calculator.getNumberSailorsToPlaceCopy(),optimizedAchievableAngle,calculator,this.optimizedAchievableAnglePosition, busySailors);
            entityListCopy = (ArrayList<Entity>) parsedInitGame.getShip().getEntities().clone();
            for (Sailor m : parsedInitGame.getSailors()){
                if (m.isAvailable()) {
                    MOVING moving = m.moveSailorToOar(entityListCopy, calculator.getNumberSailorsToPlace()[0], calculator.getNumberSailorsToPlace()[1], (int) ((Rectangle) parsedInitGame.getShip().getShape()).getWidth());
                    if (moving != null && (calculator.getNumberSailorsToPlace()[0] != 0 || calculator.getNumberSailorsToPlace()[1] != 0)) {
                        if (moving.getYDistance() + m.getY() <= (parsedInitGame.getShip().getDeck().getWidth()/2-1) && calculator.getNumberSailorsToPlace()[0] > 0) { //marin placé à Tribord
                            calculator.decrementNumberSailorsToPlace(1,true,false);
                            temporaryNextRoundActions.add(moving);leftSailor = true;
                            entityListCopy = deleteEntity(entityListCopy, leftSailor, rightSailor, m, moving);
                            m.setX(moving.getXDistance() + m.getX());m.setY(moving.getYDistance() + m.getY());

                        } else if (moving.getYDistance() + m.getY() >= (parsedInitGame.getShip().getDeck().getWidth()/2-1) && calculator.getNumberSailorsToPlace()[1] > 0) { //marin placé à Babord
                            calculator.decrementNumberSailorsToPlace(1,false,true);
                            temporaryNextRoundActions.add(moving);rightSailor = true;
                            entityListCopy = deleteEntity(entityListCopy, leftSailor, rightSailor, m, moving);
                            m.setX(moving.getXDistance() + m.getX());m.setY(moving.getYDistance() + m.getY());

                        } else{
                            m.newRoundSailorReset();
                        }
                    }
                }
            }
            doesNotWorkForFirstRound=false;nbRounds++;
        } while ((calculator.getNumberSailorsToPlace()[0] != 0 || calculator.getNumberSailorsToPlace()[1] != 0) && this.continueOarConfiguration);
    }

    public Sailor openSail(){
        for (Sailor sailor : parsedInitGame.getSailors()) {
            if (sailor.isAvailable()) {
                for (Entity e : parsedInitGame.getShip().getEntities()) {
                    if (e instanceof Sail) {
                        if (!((Sail) e).isOpenned()) {
                            MOVING moving = sailor.moveSailorToSail(parsedInitGame.getShip().getEntities(), true);
                            if (moving != null) {
                                actionsNextRound.add(moving);
                                sailor.setX(moving.getXDistance() + sailor.getX());
                                sailor.setY(moving.getYDistance() + sailor.getY());
                                ((Sail) e).setOpenned(true);
                                parsedInitGame.getShip().setPosition(new Position(this.parsedInitGame.getShip().getPosition().getX() + wind.getStrength() * Math.cos(wind.getOrientation()), this.parsedInitGame.getShip().getPosition().getY() + wind.getStrength() * Math.sin(wind.getOrientation()), parsedInitGame.getShip().getPosition().getOrientation()));
                                return sailor;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public Sailor closeSail(){
        for (Sailor sailor : parsedInitGame.getSailors()) {
            if (sailor.isAvailable()) {
                for (Entity e : parsedInitGame.getShip().getEntities()) {
                    if (e instanceof Sail) {
                        if (((Sail) e).isOpenned()) {
                            MOVING moving = sailor.moveSailorToSail(parsedInitGame.getShip().getEntities(), false);
                            if (moving != null) {
                                actionsNextRound.add(moving);
                                sailor.setX(moving.getXDistance() + sailor.getX());
                                sailor.setY(moving.getYDistance() + sailor.getY());
                                ((Sail) e).setOpenned(false);
                                return sailor;
                            }
                        }
                    }
                }
            }
        }
        return null;
    }

    public Sailor useSail(){
        if (useSailDecisionning(parsedInitGame)) return openSail();
        else return closeSail();
    }

    public boolean useSailDecisionning(InitGame game){
        double xWithSail=game.getShip().getPosition().getX() + wind.getStrength()*Math.cos(wind.getOrientation());
        double yWithSail=game.getShip().getPosition().getY() + wind.getStrength()*Math.sin(wind.getOrientation());
        Position positionSiRameactive = new Position(xWithSail,yWithSail, game.getShip().getPosition().getOrientation());
        return Math.sqrt(Math.pow(2,positionSiRameactive.getX()-((RegattaGoal) game.getGoal()).getCheckpoints().get(0).getPosition().getX()) + Math.pow(2,positionSiRameactive.getY()-((RegattaGoal) game.getGoal()).getCheckpoints().get(0).getPosition().getY())) <=
                Math.sqrt(Math.pow(2,game.getShip().getPosition().getX()-((RegattaGoal) game.getGoal()).getCheckpoints().get(0).getPosition().getX()) + Math.pow(2,game.getShip().getPosition().getY()-((RegattaGoal) game.getGoal()).getCheckpoints().get(0).getPosition().getY()));
    }

    public boolean isThereARudder(){
        for (Entity e : parsedInitGame.getShip().getEntities()) {
            if (e instanceof Rudder){
                return true;
            }
        }
        return false;
    }

    public boolean isThereASail(){
        for (Entity e : parsedInitGame.getShip().getEntities()) {
            if (e instanceof Sail){
                return true;
            }
        }
        return false;
    }

    public Sailor rudderConfiguration(){
        for (Sailor m : parsedInitGame.getSailors()){
            if (m.isAvailable()){
                MOVING moving = m.moveSailorToRudder(parsedInitGame.getShip().getEntities());
                if (moving!=null){
                    actionsNextRound.add(moving);
                    m.setX(moving.getXDistance() + m.getX());
                    m.setY(moving.getYDistance() + m.getY());
                    return m;
                }
            }
        }
        return null;
    }

}