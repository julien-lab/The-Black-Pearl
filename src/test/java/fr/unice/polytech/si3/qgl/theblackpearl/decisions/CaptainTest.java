package fr.unice.polytech.si3.qgl.theblackpearl.decisions;

import fr.unice.polytech.si3.qgl.theblackpearl.actions.MOVING;
import fr.unice.polytech.si3.qgl.theblackpearl.ship.entities.Entity;
import fr.unice.polytech.si3.qgl.theblackpearl.ship.entities.Oar;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static fr.unice.polytech.si3.qgl.theblackpearl.ship.entities.Entity.deleteEntity;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class CaptainTest {

    private ArrayList<Entity> listeEntite;
    private List<Sailor> listeSailors;
    private Captain captain;

    @BeforeEach
    public void setUp(){
        captain = new Captain(null,null);
        listeEntite=new ArrayList<>();
        listeSailors =new ArrayList<>();
        listeEntite.add(new Oar(0,0));
        listeEntite.add(new Oar(1,0));
        listeEntite.add(new Oar(2,0));
        listeEntite.add(new Oar(7,8));
        listeEntite.add(new Oar(1,8));
        listeEntite.add(new Oar(2,8));
        listeSailors.add(new Sailor(0,0,0,"Edward Teach"));
        listeSailors.add(new Sailor(1,0,0,"Edward Pouce"));
        listeSailors.add(new Sailor(2,0,0,"Tom Pouce"));
        listeSailors.add(new Sailor(3,0,0,"Jack Teach"));
        listeSailors.add(new Sailor(4,0,0,"Marschall D Teach"));
        listeSailors.add(new Sailor(5,0,0,"Edward New Gate"));
    }

    @Test
    public void testDeleteEntity(){
        Sailor sailor = new Sailor(1, 5 ,6,"Edward Teach");
        MOVING moving = new MOVING(1,"MOVING",2,2);
        assertEquals((deleteEntity(listeEntite,false,true, sailor,moving).size()),5);
        assertEquals((deleteEntity(listeEntite,false,true, sailor,moving).size()),5);
        assertEquals((deleteEntity(listeEntite,false,true, sailor,moving).size()),5);
        ArrayList<Entity> listeNulle = new ArrayList<>();
        assertEquals((deleteEntity(listeNulle,false,true, sailor,moving)).size(),0);
    }

}
