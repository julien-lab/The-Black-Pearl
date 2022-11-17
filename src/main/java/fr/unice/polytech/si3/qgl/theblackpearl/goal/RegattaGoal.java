package fr.unice.polytech.si3.qgl.theblackpearl.goal;

import com.fasterxml.jackson.annotation.*;

import java.util.ArrayList;
import java.util.List;

@JsonTypeName("REGATTA")
public class RegattaGoal extends Goal {
    private List<Checkpoint> checkpoints;

    @JsonCreator
    public RegattaGoal(@JsonProperty("checkpoints") List<Checkpoint> checkpoints) {
        this.checkpoints = checkpoints;
        mode = "REGATTA";
    }

    public List<Checkpoint> getCheckpoints() {
        return checkpoints;
    }

    public void removeCheckpoint() {
        this.checkpoints.remove(0);
    }

    @Override
    public String toString() {
        return "RegattaGoal{" +
                "checkpoints=" + checkpoints +
                '}';
    }

    public RegattaGoal clone(){
        ArrayList<Checkpoint> cloneCheckpoints = new ArrayList<>(checkpoints);
        return new RegattaGoal(cloneCheckpoints);

    }
}
