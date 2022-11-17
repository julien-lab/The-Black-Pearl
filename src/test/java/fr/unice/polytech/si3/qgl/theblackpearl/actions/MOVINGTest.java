package fr.unice.polytech.si3.qgl.theblackpearl.actions;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MOVINGTest {
    private Action move = new MOVING(2, "MOVING", 4,8);

    @Test
    void testMovingToString() {
        assertEquals( "{\"sailorId\": 2\",type\": MOVING\",xdistance\": 4\",ydistance\": 8}", move.toString());
    }
}