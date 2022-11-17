package fr.unice.polytech.si3.qgl.theblackpearl.decisions;

import fr.unice.polytech.si3.qgl.theblackpearl.actions.*;
import fr.unice.polytech.si3.qgl.theblackpearl.engine.*;
import fr.unice.polytech.si3.qgl.theblackpearl.goal.*;
import fr.unice.polytech.si3.qgl.theblackpearl.sea_elements.*;
import fr.unice.polytech.si3.qgl.theblackpearl.ship.entities.*;

import java.util.ArrayList;
import java.util.List;


public class Captain {

    private Calculator calculator;
    private ArrayList<Action> actionsNextRound = new ArrayList<>();
    private double perfectAngleToCheckpoint;
    private Checkpoint aimingCheckpoint;
    private ControlRoom controlRoom;
    private List<Sailor> busySailors = new ArrayList<>();
    private InitGame parsedInitGame;

    public Captain(InitGame game, Wind wind) {
        parsedInitGame = game;
        calculator = new Calculator();
        controlRoom = new ControlRoom(game, wind,actionsNextRound);
    }

    public ArrayList<Action> captainOrder() throws Exception {
        if (this.determineTargetCheckpoint()){
            if (controlRoom.isThereASail())
                busySailors.add(controlRoom.useSail());
            if (controlRoom.isThereARudder())
                busySailors.add(controlRoom.rudderConfiguration());
            double angleRealiseRames = controlRoom.OarConfiguration(this.bestAchievableAngle(), 0, busySailors);
            if (controlRoom.isThereARudder()) {
                double resteAngleARealiser = this.perfectAngleToCheckpoint - angleRealiseRames;
                Rudder rudder = parsedInitGame.getShip().getGouvernail();
                rudder.setAngleAchieved(rudder.rudderAngle(resteAngleARealiser));
            }
            return actionsNextRound;
        }
        else return null;
    }

    public boolean determineTargetCheckpoint() throws Exception {
        if(parsedInitGame.getGoal() instanceof RegattaGoal){
            RegattaGoal regatta = (RegattaGoal) parsedInitGame.getGoal();
            Calculator c = new Calculator();
            if (c.shipIsInsideCheckpoint(parsedInitGame.getShip(),regatta.getCheckpoints().get(0))){
                regatta.removeCheckpoint();
            }
            if (regatta.getCheckpoints().size() != 0){
                aimingCheckpoint = regatta.getCheckpoints().get(0);
                return true;
            }
        }
        return false;
    }

    public List<Double> bestAchievableAngle(){
        List<Double> bestAchievableAngles = new ArrayList<>();
        if(aimingCheckpoint !=null){
            perfectAngleToCheckpoint =calculator.calculateIdealAngle(parsedInitGame.getShip().getPosition(), aimingCheckpoint.getPosition());
            bestAchievableAngles = parsedInitGame.getShip().optimizedAchievableAngle(perfectAngleToCheckpoint);
        }
        return bestAchievableAngles;
    }

}
