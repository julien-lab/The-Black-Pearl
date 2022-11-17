package fr.unice.polytech.si3.qgl.theblackpearl.ship;

import fr.unice.polytech.si3.qgl.theblackpearl.decisions.Sailor;
import fr.unice.polytech.si3.qgl.theblackpearl.shape.Position;
import fr.unice.polytech.si3.qgl.theblackpearl.shape.Rectangle;
import fr.unice.polytech.si3.qgl.theblackpearl.shape.Shape;
import fr.unice.polytech.si3.qgl.theblackpearl.ship.entities.Entity;
import fr.unice.polytech.si3.qgl.theblackpearl.ship.entities.Rudder;
import fr.unice.polytech.si3.qgl.theblackpearl.ship.entities.Oar;
import fr.unice.polytech.si3.qgl.theblackpearl.ship.entities.Sail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ShipTest {
    private ArrayList<Entity> entities;
    private Shape shape;
    private Ship ship;
    private List<Sailor> listeSailors;

    ShipTest() {
    }

    @BeforeEach
    public void init() {
        entities = new ArrayList<>();
        listeSailors = new ArrayList<>();
        listeSailors.add(new Sailor(0,0,0,"Edward Teach"));
        listeSailors.add(new Sailor(1,0,2,"Edward Pouce"));
        listeSailors.add(new Sailor(2,0,2,"Tom Pouce"));
        listeSailors.add(new Sailor(3,0,0,"Jack Teach"));
        listeSailors.add(new Sailor(4,9,0,"Marschall D Teach"));
        listeSailors.add(new Sailor(5,9,0,"Edward New Gate"));
        entities.add(new Oar(0,0));
        entities.add(new Oar(1,0));
        entities.add(new Oar(0,1));
        entities.add(new Oar(1,1));
        entities.add(new Oar(2,0));
        entities.add(new Oar(2,1));
        Position pos = new Position(0, 0, 0);
        Deck deck = new Deck(2, 3);
        shape = new Rectangle( 2, 3, 0 );
        ship = new Ship("ship", 100, pos, "Les copaings d'abord!", deck, entities, shape);
    }

    @Test
    void anglesPossiblesAvec3RamesDeChaqueCote() {
        List<Double> anglesPossibles = new ArrayList<>();
        anglesPossibles.add(Math.PI/6);
        anglesPossibles.add(-Math.PI/6);
        anglesPossibles.add(Math.PI/3);
        anglesPossibles.add(-Math.PI/3);
        anglesPossibles.add(Math.PI/2);
        anglesPossibles.add(-Math.PI/2);
        anglesPossibles.add(0.0);
        assertThat(ship.achievableAnglesWithOars(), is(anglesPossibles));
    }

    @Test
    void testGetNbRame() {
        assertEquals(6 , ship.getNbRame());
    }

    @Test
    void testGetNbVoile() {
        assertEquals(0 , ship.nbSail());
    }

    @Test
    void testGetNbVoileOuverte() {
        assertEquals(0 , ship.nbSail());
        Sail v = new Sail(0,0, false);
        entities.add(v);
        assertEquals(1, ship.nbSail());
        assertEquals(0, ship.nbOpennedSail());
        v.setOpenned(true);
        assertEquals(1, ship.nbOpennedSail());
    }

    @Test
    void testNbSailorAndOarConfiguration(){
        int nombreMarinAplacer[];
        nombreMarinAplacer = this.ship.nbSailorAndOarConfiguration(-Math.PI/2, ship.getListRames());
        assertEquals(3,nombreMarinAplacer[0]);
        assertEquals(0,nombreMarinAplacer[1]);
        nombreMarinAplacer = this.ship.nbSailorAndOarConfiguration((-Math.PI/2 + Math.PI/6), ship.getListRames());
        assertEquals(3,nombreMarinAplacer[0]);
        assertEquals(1,nombreMarinAplacer[1]);
        nombreMarinAplacer = this.ship.nbSailorAndOarConfiguration((-Math.PI/2 + 2*Math.PI/6), ship.getListRames());
        assertEquals(3,nombreMarinAplacer[0]);
        assertEquals(2,nombreMarinAplacer[1]);
        nombreMarinAplacer = this.ship.nbSailorAndOarConfiguration((-Math.PI/2 + 3*Math.PI/6), ship.getListRames());
        assertEquals(3,nombreMarinAplacer[0]);
        assertEquals(3,nombreMarinAplacer[1]);
        nombreMarinAplacer = this.ship.nbSailorAndOarConfiguration((-Math.PI/2 + 4*Math.PI/6), ship.getListRames());
        assertEquals(2,nombreMarinAplacer[0]);
        assertEquals(3,nombreMarinAplacer[1]);
        nombreMarinAplacer = this.ship.nbSailorAndOarConfiguration((-Math.PI/2 + 5*Math.PI/6), ship.getListRames());
        assertEquals(1,nombreMarinAplacer[0]);
        assertEquals(3,nombreMarinAplacer[1]);
        nombreMarinAplacer = this.ship.nbSailorAndOarConfiguration((-Math.PI/2 + 6*Math.PI/6), ship.getListRames());
        assertEquals(0,nombreMarinAplacer[0]);
        assertEquals(3,nombreMarinAplacer[1]);
        ArrayList<Entity> listeRameEnlever = new ArrayList<>();
        listeRameEnlever.add(entities.get(4));
        listeRameEnlever.add(entities.get(5));
        entities.removeAll(listeRameEnlever);
        nombreMarinAplacer = this.ship.nbSailorAndOarConfiguration((-Math.PI/2 + 1*Math.PI/6), ship.getListRames());
        assertNull(nombreMarinAplacer);
        nombreMarinAplacer = this.ship.nbSailorAndOarConfiguration((-Math.PI/2 + 2*Math.PI/6), ship.getListRames());
        assertNull(nombreMarinAplacer);
        nombreMarinAplacer = this.ship.nbSailorAndOarConfiguration((-Math.PI/2 + 4*Math.PI/6), ship.getListRames());
        assertNull(nombreMarinAplacer);
        nombreMarinAplacer = this.ship.nbSailorAndOarConfiguration((-Math.PI/2 + 5*Math.PI/6), ship.getListRames());
        assertNull(nombreMarinAplacer);
    }


    @Test
    void testGetGouvernail(){
         assertNull(ship.getGouvernail());
         entities.add(new Rudder(0,2));
         assertNotNull(ship.getGouvernail());
    }

    @Test
    void testIsOnRudderNotUsed(){
        entities.add(new Rudder(0,2));
        assertFalse(ship.isOnRudderNotUsed(listeSailors.get(0)));
        assertTrue(ship.isOnRudderNotUsed(listeSailors.get(1)));
        assertFalse(ship.isOnRudderNotUsed(listeSailors.get(2)));
    }


    @Test
    void testIsOnOarNotUsed(){
        assertFalse(ship.isOnOarNotUsed(listeSailors.get(5)));
        assertTrue(ship.isOnOarNotUsed(listeSailors.get(0)));
        assertFalse(ship.isOnOarNotUsed(listeSailors.get(2)));
    }

    @Test
    void testIsOnSailNotUsedNotOppened(){
        assertFalse(ship.isOnSailNotUsedNotOppened(listeSailors.get(0)));
        Sail v = new Sail(9,0, true);
        entities.add(v);
        assertFalse(ship.isOnSailNotUsedNotOppened(listeSailors.get(5)));
        v.setOpenned(false);
        assertTrue(ship.isOnSailNotUsedNotOppened(listeSailors.get(5)));
        assertFalse(ship.isOnSailNotUsedNotOppened(listeSailors.get(4)));
    }

    @Test
    void testIsOnSailNotUsedOppened(){
        assertFalse(ship.isOnSailNotUsedOppened(listeSailors.get(0)));
        Sail v = new Sail(9,0, false);
        entities.add(v);
        assertFalse(ship.isOnSailNotUsedOppened(listeSailors.get(5)));
        v.setOpenned(true);
        assertTrue(ship.isOnSailNotUsedOppened(listeSailors.get(5)));
        assertFalse(ship.isOnSailNotUsedOppened(listeSailors.get(4)));
    }


    @Test
    void testNbSailorsOnStarBoard(){
        assertEquals(0, ship.nbSailorsOnStarBoard());
        entities.add(new Rudder(8,8));
        for(Entity e : entities){
            e.setAvailable(false);
        }
        assertEquals(3, ship.nbSailorsOnStarBoard());
    }

    @Test
    void testNbSailorsOnPort(){
        assertEquals(0, ship.nbSailorsOnPort());
        entities.add(new Rudder(8,8));
        for(Entity e : entities){
            e.setAvailable(false);
        }
        assertEquals(3, ship.nbSailorsOnPort());
    }

    @Test
    void testGetterSetter(){
        assertEquals(shape, ship.getShape());
        assertEquals("ship", ship.getType());
        ship.setType("");
        assertEquals("", ship.getType());
        ship.setPosition(null);
        assertNull(ship.getPosition());
        entities=null;
        assertNotNull(ship.toString());
    }


}