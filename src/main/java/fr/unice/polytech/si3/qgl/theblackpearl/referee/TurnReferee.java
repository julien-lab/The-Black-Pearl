package fr.unice.polytech.si3.qgl.theblackpearl.referee;

import com.fasterxml.jackson.annotation.JsonProperty;
import fr.unice.polytech.si3.qgl.theblackpearl.decisions.Sailor;
import fr.unice.polytech.si3.qgl.theblackpearl.engine.InitGame;

public class TurnReferee extends ActionRound {
    private double rotation;

    public TurnReferee(@JsonProperty("sailorId") int sailorId, @JsonProperty("rotation") double rotation) {
        this.type = "TURN";
        this.sailorId=sailorId;
        this.rotation = rotation;
    }

    @Override
    public String toString() {
        return "TurnTest{" +
                "rotation=" + rotation +
                ", sailorId=" + sailorId +
                ", type='" + type + '\'' +
                '}';
    }

    public double tryToTurn(InitGame parsedInitGameReferee) {
        for(Sailor m : parsedInitGameReferee.getSailors()){
            if(m.getId()==this.getSailorId()){
                if(m.isAvailable() && parsedInitGameReferee.getShip().isOnRudderNotUsed(m) && this.rotation<=Math.PI/4 && this.rotation>=-Math.PI/4){
                    m.setAvailable(false);
                    return this.rotation;
                }else{
                    System.out.println("ERREUR : " + m.getId() + " Impossible de tourner.");
                }
                break;
            }
        }
        return 0.0;
    }
}
