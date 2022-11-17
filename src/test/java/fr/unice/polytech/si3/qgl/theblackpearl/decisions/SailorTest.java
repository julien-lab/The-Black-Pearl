package fr.unice.polytech.si3.qgl.theblackpearl.decisions;
import fr.unice.polytech.si3.qgl.theblackpearl.ship.entities.Entity;
import fr.unice.polytech.si3.qgl.theblackpearl.ship.entities.Oar;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SailorTest {

    private Sailor sailor1 = mock(Sailor.class);
    private Sailor sailor2 = new Sailor(2,0, 0, "Chef");
    private Sailor sailor3 = new Sailor(3,0, 0, "General");
    private Sailor sailor4 = new Sailor(4,0, 0, "Commandant");
    private ArrayList<Entity> Entities = new ArrayList<>();
    private Entity e1 = new Oar(1,0);
    private Entity e2 = new Oar(0,1);
    private Entity e3 = new Oar(1, 1);
    private Entity e4 = new Oar(0, 0);

    @BeforeEach
    void setUp(){
        when(sailor1.isAvailable()).thenReturn(false);
        Entities.add(e1);
        Entities.add(e2);
        Entities.add(e3);
        Entities.add(e4);
    }

    @Test
    public void testMoveSailorToOarPlanification(){
        assertFalse(sailor1.isAvailable());
        assertTrue(sailor2.isAvailable());
        assertTrue(sailor3.isAvailable());
        assertTrue(sailor4.isAvailable());
        assertNull(sailor1.moveSailorToOar(Entities, 2,2,2));
        assertNull(sailor2.moveSailorToOar(Entities, 0,0,2));
        assertNotNull(sailor2.moveSailorToOar(Entities, 1,0,2));
        assertNull(sailor2.moveSailorToOar(Entities, 0,0,2));
        assertNotNull(sailor3.moveSailorToOar(Entities, 0,1,2));
        sailor2.setAvailable(true);
        ArrayList<Entity> aucuneEntite = new ArrayList<>();
        assertNull(sailor2.moveSailorToOar(aucuneEntite, 1,0,2));
        ArrayList<Entity> entitesPlaceLoin = new ArrayList<>();
        entitesPlaceLoin.add(new Oar(6,0));
        entitesPlaceLoin.add(new Oar(5, 1));
        assertNull(sailor2.moveSailorToOar(entitesPlaceLoin, 1,1,2));
        ArrayList<Entity> entitesDistancesDifferentes = new ArrayList<>();
        entitesDistancesDifferentes.add(new Oar(3,1));
        entitesDistancesDifferentes.add(new Oar(3, 0));
        assertEquals((sailor2.moveSailorToOar(entitesDistancesDifferentes,1,1,2)).getYDistance(),0);
        sailor2.setAvailable(true);
        entitesDistancesDifferentes.add(new Oar(1,1));
        assertEquals((sailor2.moveSailorToOar(entitesDistancesDifferentes,1,1,2)).getYDistance(),1);
    }


}
