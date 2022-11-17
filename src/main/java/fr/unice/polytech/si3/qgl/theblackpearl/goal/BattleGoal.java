package fr.unice.polytech.si3.qgl.theblackpearl.goal;

import com.fasterxml.jackson.annotation.JsonTypeName;

@JsonTypeName("BATTLE")
class BattleGoal extends Goal {
    BattleGoal(){
        mode = "BATTLE";
    }
}
