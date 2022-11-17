package fr.unice.polytech.si3.qgl.theblackpearl.referee;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ActionRoundTest {
    private ActionRound actionRound;

    @BeforeEach
    void init() {
        actionRound = new OarReferee(2);
    }

    @Test
    void testGetSailorId() {
        assertEquals(2, actionRound.getSailorId());
    }

    @Test
    void testGetType() {
        assertEquals("OAR", actionRound.getType());
    }
}