package fr.unice.polytech.si3.qgl.theblackpearl.referee;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

class ActionsRoundTest {
    private ActionsRound actionsRound;
    private List<ActionRound> aR;
    private OarReferee a1;
    private TurnReferee a2;

    @BeforeEach
    void init() {
        aR = new ArrayList<>();
        a1 = new OarReferee(2);
        a2 = new TurnReferee(1, 0.28);
        aR.add(a1);
        aR.add(a2);
        actionsRound = new ActionsRound(aR);
    }

    @Test
    void testGetActionsRound() {
        List<ActionRound> aR2 = new ArrayList<>();
        aR2.add(a1);
        aR2.add(a2);
        assertThat(actionsRound.getActionsRound(), is(aR2));
    }
}