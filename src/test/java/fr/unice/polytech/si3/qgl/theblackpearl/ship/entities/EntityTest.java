package fr.unice.polytech.si3.qgl.theblackpearl.ship.entities;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class EntityTest {
    private Entity e = new Entity(2, 1) {};

    @Test
    void testEntityToString() {
        assertEquals( "x : 2, y : 1", e.toString());
    }

}