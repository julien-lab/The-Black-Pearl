package fr.unice.polytech.si3.qgl.theblackpearl.referee;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import fr.unice.polytech.si3.qgl.theblackpearl.decisions.Sailor;
import fr.unice.polytech.si3.qgl.theblackpearl.engine.InitGame;

@JsonTypeName("OAR")
public class OarReferee extends ActionRound {

    public OarReferee(@JsonProperty("sailorId") int sailorId) {
        this.type = "OAR";
        this.sailorId= sailorId;
    }

    @Override
    public String toString() {
        return "OarTest{" +
                "sailorId=" + sailorId +
                ", type='" + type + '\'' +
                '}';
    }

    public void tryToOar(InitGame parsedInitGameReferee) {
        for(Sailor m : parsedInitGameReferee.getSailors()){
            if(m.getId()==this.getSailorId()){
                if(m.isAvailable() && parsedInitGameReferee.getShip().isOnOarNotUsed(m)){
                    m.setAvailable(false);
                }else{
                    System.out.println("ERREUR : " + m.getId() + " NE PEUT PAS RAMER !");
                }
                break;
            }
        }
    }
}
