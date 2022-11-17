package fr.unice.polytech.si3.qgl.theblackpearl.decisions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.unice.polytech.si3.qgl.theblackpearl.actions.Action;
import fr.unice.polytech.si3.qgl.theblackpearl.actions.MOVING;
import fr.unice.polytech.si3.qgl.theblackpearl.engine.InitGame;
import fr.unice.polytech.si3.qgl.theblackpearl.sea_elements.Wind;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ControlRoomTest {

    private Wind wind;
    private InitGame parsedInitGame;
    private ControlRoom controlRoom;
    private List<Double> meilleurAngleRealisable;
    private List<Action> actionsNextRound;

    @BeforeEach
    public void setUp() throws Exception {
        actionsNextRound = new ArrayList<>();
        wind = new Wind(0,110);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            parsedInitGame = objectMapper.readValue(gameString, InitGame.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        controlRoom = new ControlRoom(parsedInitGame, wind, actionsNextRound);
        controlRoom.createSailorArray();
        Captain captain = new Captain(parsedInitGame, wind);
        captain.determineTargetCheckpoint();
        meilleurAngleRealisable = captain.bestAchievableAngle();
    }


    @Test
    public void testTakeIntoAccountSailorPosition(){
        controlRoom.takeIntoAccountSailorPosition();
        assertEquals(parsedInitGame.getSailors().get(0).getX(),0);
        assertEquals(parsedInitGame.getSailors().get(0).getY(),0);
        assertEquals(parsedInitGame.getSailors().get(1).getX(),0);
        assertEquals(parsedInitGame.getSailors().get(1).getY(),1);
        assertEquals(parsedInitGame.getSailors().get(2).getX(),0);
        assertEquals(parsedInitGame.getSailors().get(2).getY(),2);
        assertEquals(parsedInitGame.getSailors().get(3).getX(),1);
        assertEquals(parsedInitGame.getSailors().get(3).getY(),0);
        assertEquals(parsedInitGame.getSailors().get(4).getX(),1);
        assertEquals(parsedInitGame.getSailors().get(4).getY(),1);
        assertEquals(parsedInitGame.getSailors().get(5).getX(),1);
        assertEquals(parsedInitGame.getSailors().get(5).getY(),2);
    }

    @Test
    public void testResetSailorPosition(){
        controlRoom.takeIntoAccountSailorPosition();
        for (Sailor m : parsedInitGame.getSailors()){
            m.setX(456);m.setY(789);
        }
        controlRoom.resetSailorsPosition();
        assertEquals(parsedInitGame.getSailors().get(0).getX(),0);
        assertEquals(parsedInitGame.getSailors().get(0).getY(),0);
        assertEquals(parsedInitGame.getSailors().get(1).getX(),0);
        assertEquals(parsedInitGame.getSailors().get(1).getY(),1);
        assertEquals(parsedInitGame.getSailors().get(2).getX(),0);
        assertEquals(parsedInitGame.getSailors().get(2).getY(),2);
        assertEquals(parsedInitGame.getSailors().get(3).getX(),1);
        assertEquals(parsedInitGame.getSailors().get(3).getY(),0);
        assertEquals(parsedInitGame.getSailors().get(4).getX(),1);
        assertEquals(parsedInitGame.getSailors().get(4).getY(),1);
        assertEquals(parsedInitGame.getSailors().get(5).getX(),1);
        assertEquals(parsedInitGame.getSailors().get(5).getY(),2);
    }

    @Test
    public void testOarsPreConfiguration() {
        ArrayList<Sailor> marinsOccupes = new ArrayList<>();
        Calculator calculateur = new Calculator();
        calculateur.setNumberSailorsToPlace(parsedInitGame.getShip().nbSailorAndOarConfiguration(meilleurAngleRealisable.get(0), parsedInitGame.getShip().getListRames()));
        calculateur.setNumberSailorsToPlaceCopy(calculateur.getNumberSailorsToPlace().clone());
        controlRoom.oarsPreConfiguration(true, 0, calculateur.getNumberSailorsToPlaceCopy(), meilleurAngleRealisable, calculateur, controlRoom.optimizedAchievableAnglePosition,marinsOccupes);
        assertEquals(controlRoom.optimizedAchievableAnglePosition, 0);
        assertEquals(calculateur.getNumberSailorsToPlace()[0], 3);
        assertEquals(calculateur.getNumberSailorsToPlace()[1], 0);
        calculateur.setNumberSailorsToPlace(new int[]{3, -1});
        controlRoom.oarsPreConfiguration(true, 0, calculateur.getNumberSailorsToPlaceCopy(), meilleurAngleRealisable, calculateur, controlRoom.optimizedAchievableAnglePosition,marinsOccupes);
        assertEquals(controlRoom.optimizedAchievableAnglePosition, 0);
        assertEquals(calculateur.getNumberSailorsToPlace()[0],3);
        calculateur.setNumberSailorsToPlace(new int[]{3, 2});
        calculateur.setNumberSailorsToPlaceCopy(new int[]{3, 2});
        controlRoom.oarsPreConfiguration(false, 1, calculateur.getNumberSailorsToPlaceCopy(), meilleurAngleRealisable, calculateur, controlRoom.optimizedAchievableAnglePosition,marinsOccupes);
        assertEquals(calculateur.getNumberSailorsToPlace()[0],2);
        assertEquals(calculateur.getNumberSailorsToPlace()[1],1);
    }

    @Test
    public void testRudderConfiguration(){
        controlRoom.rudderConfiguration();
        assertEquals((actionsNextRound.get(0)).getSailorId(),0);
        assertEquals((actionsNextRound.get(0)).getType(),"MOVING");
        assertEquals(((MOVING) actionsNextRound.get(0)).getYDistance(),0);
        assertEquals(((MOVING) actionsNextRound.get(0)).getXDistance(),5);
        assertEquals(actionsNextRound.size(),1);
    }

    @Test
    public void testSailUsage(){
        wind.setOrientation(Math.PI);
        controlRoom.useSail();
        assertEquals(((MOVING) actionsNextRound.get(0)).getYDistance(),1);
        assertEquals(((MOVING) actionsNextRound.get(0)).getXDistance(),2);
        assertEquals(actionsNextRound.size(),1);
    }

    @Test
    public void testuseSailDecisionning(){
        assertFalse(controlRoom.useSailDecisionning(parsedInitGame));
        wind.setOrientation(Math.PI);
        assertTrue(controlRoom.useSailDecisionning(parsedInitGame));
    }

    @Test
    public void isThereARudder(){
        assertTrue(controlRoom.isThereARudder());
    }

    @Test
    public void isThereASail(){
        assertTrue(controlRoom.isThereASail());
    }



    private String gameString = "{\n" +
            "  \"goal\": {\n" +
            "    \"mode\": \"REGATTA\",\n" +
            "    \"checkpoints\": [\n" +
            "      {\n" +
            "        \"position\": {\n" +
            "          \"x\": -1000,\n" +
            "          \"y\": -50,\n" +
            "          \"orientation\": 0\n" +
            "        },\n" +
            "        \"shape\": {\n" +
            "          \"type\": \"circle\",\n" +
            "          \"radius\": 50\n" +
            "        }\n" +
            "      },\n" +
            "      {\n" +
            "        \"position\": {\n" +
            "          \"x\": 0,\n" +
            "          \"y\": 0,\n" +
            "          \"orientation\": 0\n" +
            "        },\n" +
            "        \"shape\": {\n" +
            "          \"type\": \"circle\",\n" +
            "          \"radius\": 50\n" +
            "        }\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"ship\": {\n" +
            "    \"type\": \"ship\",\n" +
            "    \"life\": 100,\n" +
            "    \"position\": {\n" +
            "      \"x\": 0,\n" +
            "      \"y\": 0,\n" +
            "      \"orientation\": 0\n" +
            "    },\n" +
            "    \"name\": \"Les copaings d'abord!\",\n" +
            "    \"deck\": {\n" +
            "      \"width\": 3,\n" +
            "      \"length\": 6\n" +
            "    },\n" +
            "    \"entities\": [\n" +
            "      {\n" +
            "        \"x\": 1,\n" +
            "        \"y\": 0,\n" +
            "        \"type\": \"oar\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"x\": 1,\n" +
            "        \"y\": 2,\n" +
            "        \"type\": \"oar\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"x\": 3,\n" +
            "        \"y\": 0,\n" +
            "        \"type\": \"oar\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"x\": 3,\n" +
            "        \"y\": 2,\n" +
            "        \"type\": \"oar\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"x\": 4,\n" +
            "        \"y\": 0,\n" +
            "        \"type\": \"oar\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"x\": 4,\n" +
            "        \"y\": 2,\n" +
            "        \"type\": \"oar\"\n" +
            "      },\n" +
            "      {\n" +
            "        \"x\": 2,\n" +
            "        \"y\": 1,\n" +
            "        \"type\": \"sail\",\n" +
            "        \"openned\": false\n" +
            "      },\n" +
            "      {\n" +
            "        \"x\": 5,\n" +
            "        \"y\": 0,\n" +
            "        \"type\": \"rudder\"\n" +
            "      }\n" +
            "    ],\n" +
            "    \"shape\": {\n" +
            "      \"type\": \"rectangle\",\n" +
            "      \"width\": 3,\n" +
            "      \"height\": 6,\n" +
            "      \"orientation\": 0\n" +
            "    }\n" +
            "  },\n" +
            "  \"sailors\": [\n" +
            "    {\n" +
            "      \"x\": 0,\n" +
            "      \"y\": 0,\n" +
            "      \"id\": 0,\n" +
            "      \"name\": \"Edward Teach\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"x\": 0,\n" +
            "      \"y\": 1,\n" +
            "      \"id\": 1,\n" +
            "      \"name\": \"Edward Pouce\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"x\": 0,\n" +
            "      \"y\": 2,\n" +
            "      \"id\": 2,\n" +
            "      \"name\": \"Tom Pouce\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"x\": 1,\n" +
            "      \"y\": 0,\n" +
            "      \"id\": 3,\n" +
            "      \"name\": \"Jack Teach\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"x\": 1,\n" +
            "      \"y\": 1,\n" +
            "      \"id\": 4,\n" +
            "      \"name\": \"Jack Teach\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"x\": 1,\n" +
            "      \"y\": 2,\n" +
            "      \"id\": 5,\n" +
            "      \"name\": \"Tom Pouce\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";

}
