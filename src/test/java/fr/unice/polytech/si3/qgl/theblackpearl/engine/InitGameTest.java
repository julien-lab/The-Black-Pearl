package fr.unice.polytech.si3.qgl.theblackpearl.engine;

import fr.unice.polytech.si3.qgl.theblackpearl.decisions.Sailor;
import fr.unice.polytech.si3.qgl.theblackpearl.shape.Position;
import fr.unice.polytech.si3.qgl.theblackpearl.shape.Rectangle;
import fr.unice.polytech.si3.qgl.theblackpearl.shape.Shape;
import fr.unice.polytech.si3.qgl.theblackpearl.ship.Ship;
import fr.unice.polytech.si3.qgl.theblackpearl.ship.Deck;
import fr.unice.polytech.si3.qgl.theblackpearl.ship.entities.Entity;
import fr.unice.polytech.si3.qgl.theblackpearl.ship.entities.Oar;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class InitGameTest {
    private InitGame initGame;
    private ArrayList<Sailor> sailors;
    private Sailor m1;
    private Sailor m2;
    private Sailor m3;

    @BeforeEach
    void init() {
        sailors = new ArrayList<>();
        m1 = new Sailor(1,0,0, "m1");
        m2 = new Sailor(2,1,1, "m2");
        m3 = new Sailor(3,2,0, "m3");
        sailors.add(m1);
        sailors.add(m2);
        sailors.add(m3);
        initGame = new InitGame(null, 1, mock(Ship.class), sailors);
    }

    @Test
    void testClone() {
        ArrayList<Entity> entities = new ArrayList<>();
        entities.add(new Oar(0,0));
        entities.add(new Oar(1,0));
        Position pos = new Position(0, 0, 0);
        Deck deck = new Deck(2, 3);
        Shape shape = new Rectangle( 2, 3, 0 );
        Ship ship = new Ship("ship", 100, pos, "Les copaings d'abord!", deck, entities, shape);
        initGame = new InitGame(null, 1, ship, sailors);
        assertEquals(initGame.toString(), initGame.clone().toString());
    }

    @Test
    void testGetSailorByID() {
        initGame = new InitGame(null, 1, mock(Ship.class), sailors);
        assertEquals(m2,initGame.getSailorById(2));
        assertEquals(m3,initGame.getSailorById(3));
        assertNull(initGame.getSailorById(5));
    }
}