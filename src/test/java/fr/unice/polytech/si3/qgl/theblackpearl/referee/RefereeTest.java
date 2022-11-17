package fr.unice.polytech.si3.qgl.theblackpearl.referee;

import fr.unice.polytech.si3.qgl.theblackpearl.Cockpit;
import fr.unice.polytech.si3.qgl.theblackpearl.actions.Action;
import fr.unice.polytech.si3.qgl.theblackpearl.decisions.Sailor;
import fr.unice.polytech.si3.qgl.theblackpearl.engine.InitGame;
import fr.unice.polytech.si3.qgl.theblackpearl.engine.NextRound;
import fr.unice.polytech.si3.qgl.theblackpearl.goal.Checkpoint;
import fr.unice.polytech.si3.qgl.theblackpearl.goal.Goal;
import fr.unice.polytech.si3.qgl.theblackpearl.goal.RegattaGoal;
import fr.unice.polytech.si3.qgl.theblackpearl.sea_elements.Reef;
import fr.unice.polytech.si3.qgl.theblackpearl.sea_elements.VisibleEntity;
import fr.unice.polytech.si3.qgl.theblackpearl.sea_elements.Wind;
import fr.unice.polytech.si3.qgl.theblackpearl.shape.Circle;
import fr.unice.polytech.si3.qgl.theblackpearl.shape.Position;
import fr.unice.polytech.si3.qgl.theblackpearl.shape.Rectangle;
import fr.unice.polytech.si3.qgl.theblackpearl.shape.Shape;
import fr.unice.polytech.si3.qgl.theblackpearl.ship.Deck;
import fr.unice.polytech.si3.qgl.theblackpearl.ship.Ship;
import fr.unice.polytech.si3.qgl.theblackpearl.ship.entities.Entity;
import fr.unice.polytech.si3.qgl.theblackpearl.ship.entities.Oar;
import fr.unice.polytech.si3.qgl.theblackpearl.ship.entities.Sail;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

class RefereeTest {
    private Referee ref ;
    private InitGame initGame;
    String gameString = "{\"goal\":{\"mode\":\"REGATTA\",\"checkpoints\":[{\"position\":{\"x\":50.0,\"y\":1000.0,\"orientation\":0.0},\"shape\":{\"type\":\"circle\",\"radius\":50.0}},{\"position\":{\"x\":500.0,\"y\":-200.0,\"orientation\":0.0},\"shape\":{\"type\":\"circle\",\"radius\":50.0}},{\"position\":{\"x\":-500.0,\"y\":1250.0,\"orientation\":0.0},\"shape\":{\"type\":\"circle\",\"radius\":60.0}}]},\"ship\":{\"type\":\"ship\",\"position\":{\"x\":0.0,\"y\":0.0,\"orientation\":0.0},\"name\":\"Péquod\",\"deck\":{\"width\":3,\"length\":4},\"entities\":[{\"x\":0,\"y\":0,\"type\":\"oar\"},{\"x\":0,\"y\":2,\"type\":\"oar\"},{\"x\":1,\"y\":0,\"type\":\"oar\"},{\"x\":1,\"y\":2,\"type\":\"oar\"},{\"x\":2,\"y\":0,\"type\":\"oar\"},{\"x\":2,\"y\":2,\"type\":\"oar\"},{\"x\":3,\"y\":1,\"type\":\"rudder\"}],\"life\":300,\"shape\":{\"type\":\"rectangle\",\"width\":3.0,\"height\":4.0,\"orientation\":0.0}},\"sailors\":[{\"x\":0,\"y\":0,\"id\":0,\"name\":\"Jack Pouce\"},{\"x\":0,\"y\":1,\"id\":1,\"name\":\"Tom Pouce\"},{\"x\":0,\"y\":2,\"id\":2,\"name\":\"Tom Pouce\"},{\"x\":1,\"y\":0,\"id\":3,\"name\":\"Edward Pouce\"}],\"shipCount\":1}";
    String firstRound = "{\"ship\":{\"type\":\"ship\",\"position\":{\"x\":0.0,\"y\":0.0,\"orientation\":0.0},\"name\":\"Péquod\",\"deck\":{\"width\":3,\"length\":4},\"entities\":[{\"x\":0,\"y\":0,\"type\":\"oar\"},{\"x\":0,\"y\":2,\"type\":\"oar\"},{\"x\":1,\"y\":0,\"type\":\"oar\"},{\"x\":1,\"y\":2,\"type\":\"oar\"},{\"x\":2,\"y\":0,\"type\":\"oar\"},{\"x\":2,\"y\":2,\"type\":\"oar\"},{\"x\":3,\"y\":1,\"type\":\"rudder\"}],\"life\":300,\"shape\":{\"type\":\"rectangle\",\"width\":3.0,\"height\":4.0,\"orientation\":0.0}},\"visibleEntities\":[],\"wind\":{\"orientation\":0.0,\"strength\":80.0}}";
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
    void testResetSailors() {
        initGame = new InitGame(null, 1, null, sailors);
        ref = new Referee(initGame,null);
        ref.resetSailors();
        for(Sailor m : sailors){
            assertTrue(m.canMove());
            assertTrue(m.isAvailable());
        }
    }

    @Test
    void testResetEntities(){
        ArrayList<Entity> entities = new ArrayList<>();
        entities.add(new Oar(0,0));
        entities.add(new Oar(1,0));
        entities.add(new Oar(0,1));
        initGame = new InitGame(null, 1, new Ship(null,0,null,null,null,entities,null), null);
        ref = new Referee(initGame,null);
        ref.resetEntities();
        for(Entity e : entities){
            assertTrue(e.isAvailable());
        }
    }

    @Test
    void initGame() {
        Referee ref = new Referee(gameString, firstRound, new Cockpit());
        ref.initGame();
        assertEquals("ship", ref.getParsedInitGameReferee().getShip().getType());
        assertEquals( 80.0,ref.getParsedNextRoundReferee().getWind().getStrength());
    }

    @Test
    void startRoundFalse() throws Exception {
        ArrayList<Checkpoint> checkpoints = new ArrayList<>();
        checkpoints.add(new Checkpoint(new Position(100, 0, 0), new Circle(10)));
        Goal goal = new RegattaGoal(checkpoints);

        ArrayList<Entity> entities = new ArrayList<>();
        ArrayList<Sailor> listeSailors = new ArrayList<>();
        listeSailors.add(new Sailor(0,0,0,"Edward Teach"));
        listeSailors.add(new Sailor(1,0,1,"Edward Pouce"));
        listeSailors.add(new Sailor(2,1,1,"Tom Pouce"));
        listeSailors.add(new Sailor(3,1,0,"Jack Teach"));
        entities.add(new Oar(0,0));
        entities.add(new Oar(1,0));
        entities.add(new Oar(0,1));
        entities.add(new Oar(1,1));
        Deck deck = new Deck(2, 3);
        Shape shape = new Rectangle( 2, 3, 0 );
        Ship ship = new Ship("ship", 100, new Position(0, 0, 0), "Les copaings d'abord!", deck, entities, shape);

        InitGame initGame = new InitGame(goal,1,ship,listeSailors);
        NextRound nextRound = new NextRound(ship, new ArrayList<>() ,new Wind(Math.PI, 0));

        ref = new Referee(initGame, nextRound);
        String actionsRound = "[{\"sailorId\": 0, \"type\": \"OAR\" },{\"sailorId\": 1, \"type\": \"OAR\" },{\"sailorId\": 2, \"type\": \"OAR\" },{\"sailorId\": 3, \"type\": \"OAR\" }]";
        assertFalse(ref.startRound(actionsRound));
    }


    @Test
    void testStartRoundTrue() throws Exception {
        ArrayList<Checkpoint> checkpoints = new ArrayList<>();
        checkpoints.add(new Checkpoint(new Position(100, 0, 0), new Circle(10)));
        Goal goal = new RegattaGoal(checkpoints);

        ArrayList<Entity> entities = new ArrayList<>();
        ArrayList<Sailor> listeSailors = new ArrayList<>();
        listeSailors.add(new Sailor(0,0,0,"Edward Teach"));
        listeSailors.add(new Sailor(1,0,1,"Edward Pouce"));
        listeSailors.add(new Sailor(2,1,1,"Tom Pouce"));
        listeSailors.add(new Sailor(3,1,0,"Jack Teach"));
        entities.add(new Oar(0,0));
        entities.add(new Oar(1,0));
        entities.add(new Oar(0,1));
        entities.add(new Oar(1,1));
        Deck deck = new Deck(2, 3);
        Shape shape = new Rectangle( 2, 3, 0 );
        Ship ship = new Ship("ship", 100, new Position(0, 0, 0), "Les copaings d'abord!", deck, entities, shape);
        ArrayList<VisibleEntity> visibleEntities = new ArrayList<>();
        visibleEntities.add(new Reef("Reef", new Position(20, 0, 0), new Rectangle(10, 10, 0)));
        InitGame initGame = new InitGame(goal,1,ship,listeSailors);
        NextRound nextRound = new NextRound(ship, visibleEntities ,new Wind(Math.PI, 0));
        String actionsRound = "[{\"sailorId\": 0, \"type\": \"OAR\" },{\"sailorId\": 1, \"type\": \"OAR\" },{\"sailorId\": 2, \"type\": \"OAR\" },{\"sailorId\": 3, \"type\": \"OAR\" }]";

        ref = new Referee(initGame, nextRound);
        assertTrue(ref.startRound(actionsRound));
    }

    @Test
    void testGetFinishGame() throws Exception {
        Referee ref = new Referee(gameString, firstRound, new Cockpit());
        ref.initGame();
        assertFalse(ref.getFinishGame());
        assertFalse(ref.getFinishGame());
    }
}