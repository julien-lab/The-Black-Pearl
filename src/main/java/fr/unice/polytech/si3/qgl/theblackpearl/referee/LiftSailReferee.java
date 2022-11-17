package fr.unice.polytech.si3.qgl.theblackpearl.referee;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import fr.unice.polytech.si3.qgl.theblackpearl.decisions.Sailor;
import fr.unice.polytech.si3.qgl.theblackpearl.engine.InitGame;

@JsonTypeName("LIFT_SAIL")
public class LiftSailReferee extends ActionRound{
    public LiftSailReferee(@JsonProperty("sailorId") int sailorId) {
        this.type = "LIFT_SAIL";
        this.sailorId= sailorId;
    }

    @Override
    public String toString() {
        return "LiftSailReferee{" +
                "sailorId=" + sailorId +
                ", type='" + type + '\'' +
                '}';
    }

    public void tryToLiftSail(InitGame parsedInitGameReferee) {
        for(Sailor m : parsedInitGameReferee.getSailors()){
            if(m.getId()==this.getSailorId()){
                if(m.isAvailable() && parsedInitGameReferee.getShip().isOnSailNotUsedNotOppened(m)){
                    m.setAvailable(false);
                }else{
                    System.out.println("ERREUR : " + m.getId() + " NE PEUT PAS HISSER !" );
                }
                break;
            }
        }
    }
}
