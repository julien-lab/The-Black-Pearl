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

class TurnRefereeTest {
    private TurnReferee turnReferee;
    private InitGame initGame;
    private Sailor m1;
    private Sailor m2;
    private Sailor m3;

    @BeforeEach
    void init() {
        turnReferee = new TurnReferee(2, 0.43);
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
        assertEquals( "TurnTest{rotation=0.43, sailorId=2, type='TURN'}", turnReferee.toString());
    }

    @Test
    void testTryToTurn() {
        when(initGame.getShip().isOnRudderNotUsed(m2)).thenReturn(true);
        assertTrue(m2.isAvailable());
        turnReferee.tryToTurn(initGame);
        assertFalse(m2.isAvailable());

        m2.setAvailable(true);
        when(initGame.getShip().isOnRudderNotUsed(m2)).thenReturn(false);
        turnReferee = new TurnReferee(2, -Math.PI/4);
        assertTrue(m2.isAvailable());
        turnReferee.tryToTurn(initGame);
        assertTrue(m2.isAvailable());

        m2.setAvailable(true);
        when(initGame.getShip().isOnRudderNotUsed(m2)).thenReturn(true);
        turnReferee = new TurnReferee(2, Math.PI/4);
        assertTrue(m2.isAvailable());
        turnReferee.tryToTurn(initGame);
        assertFalse(m2.isAvailable());

        m2.setAvailable(true);
        when(initGame.getShip().isOnRudderNotUsed(m2)).thenReturn(true);
        turnReferee = new TurnReferee(2, 0.0);
        assertTrue(m2.isAvailable());
        assertEquals(0.0,turnReferee.tryToTurn(initGame));
        assertFalse(m2.isAvailable());

        m2.setAvailable(true);
        when(initGame.getShip().isOnRudderNotUsed(m2)).thenReturn(true);
        turnReferee = new TurnReferee(2, -Math.PI/4);
        assertTrue(m2.isAvailable());
        turnReferee.tryToTurn(initGame);
        assertFalse(m2.isAvailable());
    }
}