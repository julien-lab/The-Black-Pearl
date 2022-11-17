package fr.unice.polytech.si3.qgl.theblackpearl.referee;

import fr.unice.polytech.si3.qgl.theblackpearl.decisions.Sailor;
import fr.unice.polytech.si3.qgl.theblackpearl.engine.InitGame;
import fr.unice.polytech.si3.qgl.theblackpearl.ship.Ship;
import fr.unice.polytech.si3.qgl.theblackpearl.ship.Deck;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MovingRefereeTest {
    private MovingReferee movingReferee;
    private InitGame initGame;
    private Sailor m1;
    private Sailor m2;
    private Sailor m3;

    @BeforeEach
    void init() {
        movingReferee = new MovingReferee(2, 2,1);
        ArrayList<Sailor> sailors = new ArrayList<>();
        m1 = new Sailor(1,0,0, "m1");
        m2 = new Sailor(2,1,1, "m2");
        m3 = new Sailor(3,2,0, "m3");
        sailors.add(m1);
        sailors.add(m2);
        sailors.add(m3);
        initGame = new InitGame(null, 1, new Ship(null,0,null, null,mock(Deck.class),null, null), sailors);
    }

    @Test
    void testGetXdistance() {
        assertEquals(2, movingReferee.getXdistance());
    }

    @Test
    void testGetYdistance() {
        assertEquals(1, movingReferee.getYdistance());
    }

    @Test
    void testToString() {
        assertEquals( "MovingTest{xdistance=2, ydistance=1, sailorId=2, type='MOVING'}", movingReferee.toString());
    }

    @Test
    void testTryToMoveMarin() {
        when(initGame.getShip().getDeck().getWidth()).thenReturn(3);
        when(initGame.getShip().getDeck().getLength()).thenReturn(6);
        assertEquals(1,m2.getX());
        assertEquals(1,m2.getY());
        movingReferee.tryToMoveSailor(initGame);
        assertEquals(3,m2.getX());
        assertEquals(2,m2.getY());

        movingReferee = new MovingReferee(2, -22,-21);
        movingReferee.tryToMoveSailor(initGame);
        assertEquals(3,m2.getX());
        assertEquals(2,m2.getY());
    }
}