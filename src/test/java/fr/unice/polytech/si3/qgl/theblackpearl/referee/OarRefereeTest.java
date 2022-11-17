package fr.unice.polytech.si3.qgl.theblackpearl.referee;

import fr.unice.polytech.si3.qgl.theblackpearl.decisions.Sailor;
import fr.unice.polytech.si3.qgl.theblackpearl.engine.InitGame;
import fr.unice.polytech.si3.qgl.theblackpearl.ship.Ship;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OarRefereeTest {
    private OarReferee oarReferee;
    private InitGame initGame;
    private Sailor m1;
    private Sailor m2;
    private Sailor m3;

    @BeforeEach
    void init() {
        oarReferee = new OarReferee(2);
        ArrayList<Sailor> sailors = new ArrayList<>();
        m1 = new Sailor(1,0,0, "m1");
        m2 = new Sailor(2,1,1, "m2");
        m3 = new Sailor(3,2,0, "m3");
        sailors.add(m1);
        sailors.add(m2);
        sailors.add(m3);
        initGame = new InitGame(null, 1, mock(Ship.class), sailors);
    }
    @Test
    void testToString() {
        assertEquals( "OarTest{sailorId=2, type='OAR'}", oarReferee.toString());
    }

    @Test
    void testTryToOar() {
        when(initGame.getShip().isOnOarNotUsed(m2)).thenReturn(true);
        assertTrue(m2.isAvailable());
        oarReferee.tryToOar(initGame);
        assertFalse(m2.isAvailable());

        m2.setAvailable(true);
        when(initGame.getShip().isOnOarNotUsed(m2)).thenReturn(false);
        assertTrue(m2.isAvailable());
        oarReferee.tryToOar(initGame);
        assertTrue(m2.isAvailable());
    }
}