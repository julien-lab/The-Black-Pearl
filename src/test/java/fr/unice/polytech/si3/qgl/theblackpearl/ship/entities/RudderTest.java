package fr.unice.polytech.si3.qgl.theblackpearl.ship.entities;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RudderTest {

    private Rudder rudder;

    @BeforeEach
    public void init(){
        rudder = new Rudder(4,5);
    }


    @Test
    public void testRudderAngle(){
        assertEquals(0.0,rudder.rudderAngle(0.0));
        assertEquals(45 * Math.PI / 180,rudder.rudderAngle(70*Math.PI/180));
        assertEquals(40 * Math.PI / 180,rudder.rudderAngle(40*Math.PI/180));
        assertEquals(-40 * Math.PI / 180,rudder.rudderAngle(-40*Math.PI/180));
        assertEquals(17 * Math.PI / 180,rudder.rudderAngle(17*Math.PI/180));
        assertEquals(-17 * Math.PI / 180,rudder.rudderAngle(-17*Math.PI/180));
        assertEquals(-45 * Math.PI / 180,rudder.rudderAngle(-70*Math.PI/180));
    }


}
