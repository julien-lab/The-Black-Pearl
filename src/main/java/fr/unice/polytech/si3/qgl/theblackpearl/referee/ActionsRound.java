package fr.unice.polytech.si3.qgl.theblackpearl.referee;


import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public class ActionsRound {
    private List<ActionRound> actionsRound;

    @JsonCreator
    public ActionsRound(@JsonProperty("actions") List<ActionRound> actionsRound) {
        this.actionsRound = actionsRound;
    }

    public List<ActionRound> getActionsRound() {
        return actionsRound;
    }
}
