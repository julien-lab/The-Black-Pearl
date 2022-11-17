package fr.unice.polytech.si3.qgl.theblackpearl;

import fr.unice.polytech.si3.qgl.theblackpearl.actions.Action;
import fr.unice.polytech.si3.qgl.theblackpearl.actions.MOVING;
import fr.unice.polytech.si3.qgl.theblackpearl.actions.OAR;
import fr.unice.polytech.si3.qgl.theblackpearl.actions.TURN;
import fr.unice.polytech.si3.qgl.theblackpearl.decisions.Sailor;
import fr.unice.polytech.si3.qgl.theblackpearl.ship.entities.Entity;
import fr.unice.polytech.si3.qgl.theblackpearl.ship.entities.Oar;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static fr.unice.polytech.si3.qgl.theblackpearl.ship.entities.Entity.deleteEntity;
import static org.junit.jupiter.api.Assertions.*;

class CockpitTest {

    private ArrayList<Entity> listeEntite;
    private List<Sailor> listeSailors;
    private Cockpit c = new Cockpit();

    @BeforeEach
    public void setUp(){
        c.initGame("{\"goal\":{\"mode\":\"REGATTA\",\"checkpoints\":[{\"position\":{\"x\":50.0,\"y\":1000.0,\"orientation\":0.0},\"shape\":{\"type\":\"circle\",\"radius\":50.0}},{\"position\":{\"x\":500.0,\"y\":-200.0,\"orientation\":0.0},\"shape\":{\"type\":\"circle\",\"radius\":50.0}},{\"position\":{\"x\":-500.0,\"y\":1250.0,\"orientation\":0.0},\"shape\":{\"type\":\"circle\",\"radius\":60.0}}]},\"ship\":{\"type\":\"ship\",\"position\":{\"x\":0.0,\"y\":0.0,\"orientation\":0.0},\"name\":\"Péquod\",\"deck\":{\"width\":3,\"length\":4},\"entities\":[{\"x\":0,\"y\":0,\"type\":\"oar\"},{\"x\":0,\"y\":2,\"type\":\"oar\"},{\"x\":1,\"y\":0,\"type\":\"oar\"},{\"x\":1,\"y\":2,\"type\":\"oar\"},{\"x\":2,\"y\":0,\"type\":\"oar\"},{\"x\":2,\"y\":2,\"type\":\"oar\"},{\"x\":3,\"y\":1,\"type\":\"rudder\"}],\"life\":300,\"shape\":{\"type\":\"rectangle\",\"width\":3.0,\"height\":4.0,\"orientation\":0.0}},\"sailors\":[{\"x\":0,\"y\":0,\"id\":0,\"name\":\"Jack Pouce\"},{\"x\":0,\"y\":1,\"id\":1,\"name\":\"Tom Pouce\"},{\"x\":0,\"y\":2,\"id\":2,\"name\":\"Tom Pouce\"},{\"x\":1,\"y\":0,\"id\":3,\"name\":\"Edward Pouce\"}],\"shipCount\":1}");
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
        boolean True=false;
        boolean True2=true;
        boolean True3=false;
        Sailor sailor = new Sailor(1, 5 ,6,"Edward Teach");
        MOVING moving = new MOVING(1,"MOVING",2,2);
        assertEquals(5, deleteEntity(listeEntite,True,True2, sailor,moving).size());
        assertEquals(5, deleteEntity(listeEntite,True,True2, sailor,moving).size());
        assertEquals(5, deleteEntity(listeEntite,True3,True2, sailor,moving).size());
        ArrayList<Entity> listeNulle = new ArrayList<>();
        assertEquals(0, deleteEntity(listeNulle,True3,True2, sailor,moving).size());
    }

    @Test
    void testSailorsTasks() {
        c.getParsedInitGame().getSailors().get(0).setActionToDo("Ramer");
        c.getParsedInitGame().getSailors().get(1).setActionToDo("tournerGouvernail");
        c.getParsedInitGame().getSailors().get(2).setActionToDo("HisserVoile");
        c.getParsedInitGame().getSailors().get(3).setActionToDo("BaisserLaVoile");
        c.getParsedInitGame().getSailors().get(0).setAvailable(false);
        c.getParsedInitGame().getSailors().get(1).setAvailable(false);
        c.getParsedInitGame().getSailors().get(2).setAvailable(false);
        c.getParsedInitGame().getSailors().get(3).setAvailable(false);
        List<Action> actionsNextRound = new ArrayList<>();
        c.sailorsTasks(actionsNextRound);
        assertEquals(4, actionsNextRound.size());
    }

    @Test
    public void testSlowJSonModification(){
        List<Action> actions = new ArrayList<>();
        c.setActionsNextRound(actions);
        assertNull(c.slowJSonModification());
        actions.add(new OAR(2));
        actions.add(new OAR(0));
        actions.add(new OAR(1));
        assertEquals("[{\"sailorId\":1,\"type\":\"OAR\"}]" ,c.slowJSonModification().toString());
    }

    @Test
    void testCreationJson() {
        List<Action> actions = new ArrayList<>();
        assertEquals( "[]", c.creationJson(actions).toString());
        actions.add(new OAR(2));
        actions.add(new TURN(3, 0.78));
        assertEquals( "[{\"sailorId\":2,\"type\":\"OAR\"},{\"sailorId\":3,\"type\":\"TURN\",\"rotation\":0.78}]", c.creationJson(actions).toString());
    }

    @Test
    void testResetSailorNewRound() {
        c.getParsedInitGame().getSailors().get(0).setActionToDo("Ramer");
        c.getParsedInitGame().getSailors().get(0).setAvailable(false);
        assertEquals("Ramer",c.getParsedInitGame().getSailors().get(0).getActionToDo());
        assertFalse(c.getParsedInitGame().getSailors().get(0).isAvailable());
        c.resetSailorsNewRound();
        assertEquals("",c.getParsedInitGame().getSailors().get(0).getActionToDo());
        assertTrue(c.getParsedInitGame().getSailors().get(0).isAvailable());
    }

    @Test
    public void testCreateNewRoundLog(){
        assertEquals("[]", c.getLogs().toString());
        c.createNewRoundLog();
        assertEquals("[Marin{x=0, y=0, id=0, name='Jack Pouce'}Marin{x=0, y=1, id=1, name='Tom Pouce'}Marin{x=0, y=2, id=2, name='Tom Pouce'}Marin{x=1, y=0, id=3, name='Edward Pouce'}]" , c.getLogs().toString());
    }

}