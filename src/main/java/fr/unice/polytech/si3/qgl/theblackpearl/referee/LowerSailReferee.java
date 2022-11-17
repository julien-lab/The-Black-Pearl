package fr.unice.polytech.si3.qgl.theblackpearl.referee;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import fr.unice.polytech.si3.qgl.theblackpearl.decisions.Sailor;
import fr.unice.polytech.si3.qgl.theblackpearl.engine.InitGame;

@JsonTypeName("LOWER_SAIL")
public class LowerSailReferee extends ActionRound {
    public LowerSailReferee(@JsonProperty("sailorId") int sailorId) {
        this.type = "LOWER_SAIL";
        this.sailorId= sailorId;
    }

    @Override
    public String toString() {
        return "LowerSailReferee{" +
                "sailorId=" + sailorId +
                ", type='" + type + '\'' +
                '}';
    }

    public void tryToLowerSail(InitGame parsedInitGameReferee) {
        for(Sailor m : parsedInitGameReferee.getSailors()){
            if(m.getId()==this.getSailorId()){
                if(m.isAvailable() && parsedInitGameReferee.getShip().isOnSailNotUsedOppened(m)){
                    m.setAvailable(false);
                }else{
                    System.out.println("ERREUR : " + m.getId() + " NE PEUT PAS AFFALER !");
                }
                break;
            }
        }
    }
}
