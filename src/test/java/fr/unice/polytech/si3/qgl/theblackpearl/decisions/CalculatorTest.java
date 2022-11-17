package fr.unice.polytech.si3.qgl.theblackpearl.decisions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fr.unice.polytech.si3.qgl.theblackpearl.engine.InitGame;
import fr.unice.polytech.si3.qgl.theblackpearl.engine.NextRound;
import fr.unice.polytech.si3.qgl.theblackpearl.goal.Checkpoint;
import fr.unice.polytech.si3.qgl.theblackpearl.sea_elements.Stream;
import fr.unice.polytech.si3.qgl.theblackpearl.sea_elements.Wind;
import fr.unice.polytech.si3.qgl.theblackpearl.shape.*;
import fr.unice.polytech.si3.qgl.theblackpearl.ship.Ship;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {
    private String gameString ="{\"goal\":{\"mode\":\"REGATTA\",\"checkpoints\":[{\"position\":{\"x\":300.0,\"y\":1800.0,\"orientation\":0.0},\"shape\":{\"type\":\"circle\",\"radius\":150.0}},{\"position\":{\"x\":1900.0,\"y\":3800.0,\"orientation\":0.0},\"shape\":{\"type\":\"circle\",\"radius\":150.0}},{\"position\":{\"x\":2000.0,\"y\":5240.0,\"orientation\":0.0},\"shape\":{\"type\":\"circle\",\"radius\":150.0}},{\"position\":{\"x\":3600.0,\"y\":5160.0,\"orientation\":0.0},\"shape\":{\"type\":\"circle\",\"radius\":150.0}},{\"position\":{\"x\":3300.0,\"y\":3500.0,\"orientation\":0.0},\"shape\":{\"type\":\"circle\",\"radius\":150.0}},{\"position\":{\"x\":2400.0,\"y\":2000.0,\"orientation\":0.0},\"shape\":{\"type\":\"circle\",\"radius\":150.0}},{\"position\":{\"x\":1800.0,\"y\":360.0,\"orientation\":0.0},\"shape\":{\"type\":\"circle\",\"radius\":150.0}},{\"position\":{\"x\":360.0,\"y\":320.0,\"orientation\":0.0},\"shape\":{\"type\":\"circle\",\"radius\":150.0}}]},\"ship\":{\"type\":\"ship\",\"position\":{\"x\":360.0,\"y\":320.0,\"orientation\":1.5708},\"name\":\"Eskif\",\"deck\":{\"width\":3,\"length\":4},\"entities\":[{\"x\":1,\"y\":0,\"type\":\"oar\"},{\"x\":2,\"y\":0,\"type\":\"oar\"},{\"x\":1,\"y\":2,\"type\":\"oar\"},{\"x\":2,\"y\":2,\"type\":\"oar\"},{\"x\":3,\"y\":1,\"type\":\"rudder\"},{\"x\":0,\"y\":1,\"type\":\"watch\"},{\"x\":1,\"y\":1,\"type\":\"sail\",\"openned\":false}],\"life\":600,\"shape\":{\"type\":\"rectangle\",\"width\":3.0,\"height\":4.0,\"orientation\":0.0}},\"sailors\":[{\"x\":0,\"y\":0,\"id\":0,\"name\":\"Tom Teach\"},{\"x\":0,\"y\":1,\"id\":1,\"name\":\"Edward Pouce\"},{\"x\":0,\"y\":2,\"id\":2,\"name\":\"Jack Pouce\"},{\"x\":1,\"y\":0,\"id\":3,\"name\":\"Edward Teach\"},{\"x\":1,\"y\":1,\"id\":4,\"name\":\"Tom Pouce\"},{\"x\":1,\"y\":2,\"id\":5,\"name\":\"Edward Teach\"},{\"x\":2,\"y\":0,\"id\":6,\"name\":\"Luffy Teach\"}],\"shipCount\":1}";
    private String firstRound = "{\"ship\":{\"type\":\"ship\",\"position\":{\"x\":360.0,\"y\":320.0,\"orientation\":1.5708},\"name\":\"Eskif\",\"deck\":{\"width\":3,\"length\":4},\"entities\":[{\"x\":1,\"y\":0,\"type\":\"oar\"},{\"x\":2,\"y\":0,\"type\":\"oar\"},{\"x\":1,\"y\":2,\"type\":\"oar\"},{\"x\":2,\"y\":2,\"type\":\"oar\"},{\"x\":3,\"y\":1,\"type\":\"rudder\"},{\"x\":0,\"y\":1,\"type\":\"watch\"},{\"x\":1,\"y\":1,\"type\":\"sail\",\"openned\":false}],\"life\":600,\"shape\":{\"type\":\"rectangle\",\"width\":3.0,\"height\":4.0,\"orientation\":0.0}},\"visibleEntities\":[ { \"type\": \"reef\", \"position\": { \"x\": 1015.0, \"y\": 835.0, \"orientation\": 0.0 }, \"shape\": { \"type\":\"polygon\", \"orientation\": 0.0, \"vertices\":[ { \"x\": 285.0, \"y\": -435.0 }, { \"x\": 185.0, \"y\": -35.0 }, { \"x\": -255.0, \"y\": 465.0 }, { \"x\": -215.0, \"y\": 5.0 } ] } }, { \"type\": \"reef\", \"position\": { \"x\": 1655.0, \"y\": 1145.0, \"orientation\": 0.0 }, \"shape\": { \"type\":\"polygon\", \"orientation\": 0.0, \"vertices\":[ { \"x\": -455.0, \"y\": -345.0 }, { \"x\": 145.0, \"y\": -105.0 }, { \"x\": 225.0, \"y\": 95.0 }, { \"x\": 85.0, \"y\": 355.0 } ] } }, { \"type\": \"reef\", \"position\": { \"x\": 1172.0, \"y\": 1548.0, \"orientation\": 0.0 }, \"shape\": { \"type\":\"polygon\", \"orientation\": 0.0, \"vertices\":[ { \"x\": 28.0, \"y\": -748.0 }, { \"x\": 568.0, \"y\": -48.0 }, { \"x\": 68.0, \"y\": 712.0 }, { \"x\": -252.0, \"y\": 332.0 }, { \"x\": -412.0, \"y\": -248.0 } ] } }, { \"type\": \"reef\", \"position\": { \"x\": 1876.0, \"y\": 2096.0, \"orientation\": 0.0 }, \"shape\": { \"type\":\"polygon\", \"orientation\": 0.0, \"vertices\":[ { \"x\": -136.0, \"y\": -596.0 }, { \"x\": 164.0, \"y\": -336.0 }, { \"x\": 324.0, \"y\": 64.0 }, { \"x\": 284.0, \"y\": 704.0 }, { \"x\": -636.0, \"y\": 164.0 } ] } }, { \"type\": \"reef\", \"position\": { \"x\": 1950.0, \"y\": 3065.0, \"orientation\": 0.0 }, \"shape\": { \"type\":\"polygon\", \"orientation\": 0.0, \"vertices\":[ { \"x\": 210.0, \"y\": -265.0 }, { \"x\": 330.0, \"y\": 335.0 }, { \"x\": 170.0, \"y\": 735.0 }, { \"x\": -710.0, \"y\": -805.0 } ] } }, { \"type\": \"reef\", \"position\": { \"x\": 2265.0, \"y\": 3790.0, \"orientation\": 0.0 }, \"shape\": { \"type\":\"polygon\", \"orientation\": 0.0, \"vertices\":[ { \"x\": 15.0, \"y\": -390.0 }, { \"x\": 95.0, \"y\": 10.0 }, { \"x\": 35.0, \"y\": 370.0 }, { \"x\": -145.0, \"y\": 10.0 } ] } }, { \"type\": \"reef\", \"position\": { \"x\": 2160.0, \"y\": 4175.0, \"orientation\": 0.0 }, \"shape\": { \"type\":\"polygon\", \"orientation\": 0.0, \"vertices\":[ { \"x\": -40.0, \"y\": -375.0 }, { \"x\": 140.0, \"y\": -15.0 }, { \"x\": 100.0, \"y\": 285.0 }, { \"x\": -200.0, \"y\": 105.0 } ] } }, { \"type\": \"reef\", \"position\": { \"x\": 2196.0, \"y\": 4684.0, \"orientation\": 0.0 }, \"shape\": { \"type\":\"polygon\", \"orientation\": 0.0, \"vertices\":[ { \"x\": 64.0, \"y\": -224.0 }, { \"x\": 564.0, \"y\": 576.0 }, { \"x\": -96.0, \"y\": 276.0 }, { \"x\": -296.0, \"y\": -224.0 }, { \"x\": -236.0, \"y\": -404.0 } ] } }, { \"type\": \"reef\", \"position\": { \"x\": 2724.0, \"y\": 4940.0, \"orientation\": 0.0 }, \"shape\": { \"type\":\"polygon\", \"orientation\": 0.0, \"vertices\":[ { \"x\": -284.0, \"y\": -440.0 }, { \"x\": 336.0, \"y\": 140.0 }, { \"x\": 376.0, \"y\": 460.0 }, { \"x\": 36.0, \"y\": 320.0 }, { \"x\": -464.0, \"y\": -480.0 } ] } }, { \"type\": \"reef\", \"position\": { \"x\": 3023.3333333333335, \"y\": 4403.333333333333, \"orientation\": 0.0 }, \"shape\": { \"type\":\"polygon\", \"orientation\": 0.0, \"vertices\":[ { \"x\": -583.3333333333335, \"y\": 96.66666666666697 }, { \"x\": -223.33333333333348, \"y\": -643.333333333333 }, { \"x\": 16.666666666666515, \"y\": -283.33333333333303 }, { \"x\": 376.6666666666665, \"y\": -43.33333333333303 }, { \"x\": 376.6666666666665, \"y\": 196.66666666666697 }, { \"x\": 36.666666666666515, \"y\": 676.666666666667 } ] } }, { \"type\": \"reef\", \"position\": { \"x\": 2592.0, \"y\": 3408.0, \"orientation\": 0.0 }, \"shape\": { \"type\":\"polygon\", \"orientation\": 0.0, \"vertices\":[ { \"x\": -112.0, \"y\": -208.0 }, { \"x\": 168.0, \"y\": -408.0 }, { \"x\": 148.0, \"y\": 352.0 }, { \"x\": -52.0, \"y\": 272.0 }, { \"x\": -152.0, \"y\": -8.0 } ] } },{\"type\":\"stream\",\"position\":{\"x\":1000.0,\"y\":1000.0,\"orientation\":0.78539816339},\"shape\":{\"type\":\"rectangle\",\"width\":100.0,\"height\":1400.0,\"orientation\":0.0},\"strength\":150.0}],\"wind\":{\"orientation\":0.0,\"strength\":150.0}}";
    private Calculator calcul = new Calculator();
    private Position posBateau;
    private Position posCheckpoint;
    private InitGame parsedInitGame;
    private NextRound parsedNextRound;

    @BeforeEach
    public void init() {
        posBateau = new Position(947, 1000,0);
        posCheckpoint = new Position(1000, 1000,0);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            parsedInitGame = objectMapper.readValue(gameString, InitGame.class);
            parsedNextRound = objectMapper.readValue(firstRound, NextRound.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    @Test
    void testCalculateDistanceBetween2Points() {
        assertEquals(53.0,calcul.calculateDistanceBetween2Points(posBateau, posCheckpoint));
        posBateau = new Position(853, 202,0);
        posCheckpoint = new Position(32, -627,0);
        assertEquals(1166.7399024632696, calcul.calculateDistanceBetween2Points(posBateau, posCheckpoint) );
        posBateau = new Position(0, 0,0);
        posCheckpoint = new Position(0, 0,0);
        assertEquals(0,calcul.calculateDistanceBetween2Points(posBateau, posCheckpoint));
        posBateau = new Position(2, -1,0);
        posCheckpoint = new Position(-4 , -1,0);
        assertEquals(6.0, calcul.calculateDistanceBetween2Points(posBateau, posCheckpoint));
    }


    @Test
    void testCalculateIdealAngle() {
        assertEquals(calcul.calculateIdealAngle(posBateau, posCheckpoint), 0.0);
        posBateau = new Position(0, 0,0);
        posCheckpoint = new Position(1000, 1000,0);
        assertEquals(calcul.calculateIdealAngle(posBateau, posCheckpoint), Math.PI/4);
        posCheckpoint = new Position(1000, -1000,0);
        assertEquals(calcul.calculateIdealAngle(posBateau, posCheckpoint), -Math.PI/4);
        posCheckpoint = new Position(-1000, -1000,0);
        assertEquals(calcul.calculateIdealAngle(posBateau, posCheckpoint), -3*Math.PI/4);

        posBateau = new Position(10, 10,Math.PI/4);
        posCheckpoint = new Position(-11, -11,0);
        assertEquals(calcul.calculateIdealAngle(posBateau, posCheckpoint), -Math.PI);
        posBateau = new Position(853, 202,0.9834);
        posCheckpoint = new Position(32, -627,0);
        assertEquals(-3.3347460433357368, calcul.calculateIdealAngle(posBateau, posCheckpoint));
    }

    @Test
    void testCalculateOarsRotation() {
        assertEquals(-2*Math.PI/6, calcul.calculateOarsRotation(3,1,6));
        assertEquals(Math.PI/2, calcul.calculateOarsRotation(0,3,6));
        assertEquals(-Math.PI/2, calcul.calculateOarsRotation(18,0,36));
        assertEquals(Math.PI/4, calcul.calculateOarsRotation(1,2,4));
        assertEquals(0.0, calcul.calculateOarsRotation(2,2,4));
        assertEquals(Math.PI/4, calcul.calculateOarsRotation(1,2,4));
    }


    @Test
    void testCalculateNewPositionShip(){
        posBateau = new Position(0, 0,0);
        posBateau = calcul.calculateNewPositionShip(100, 1, posBateau, 10);
        assertEquals(10.0, posBateau.getX());
        assertEquals(0.0, posBateau.getY());
        assertEquals(0.1, posBateau.getOrientation());
        posBateau = calcul.calculateNewPositionShip(100, 1, posBateau, 10);
        assertEquals(19.950041652780257, posBateau.getX());
        assertEquals(0.9983341664682815, posBateau.getY());
        assertEquals(0.2, posBateau.getOrientation());
        posBateau = calcul.calculateNewPositionShip(100, 1, posBateau, 10);
        assertEquals(29.750707431192673, posBateau.getX());
        assertEquals(2.9850274744188936, posBateau.getY());
        assertEquals(0.30000000000000004, posBateau.getOrientation());
    }

    @Test
    public void testShipIsInsideCheckpoint() throws Exception {
        parsedNextRound.getShip().setPosition(new Position(10,5,9));
        assertTrue(calcul.shipIsInsideCheckpoint(parsedNextRound.getShip(),new Checkpoint(new Position(48,32,0),new Circle(50))));
        parsedNextRound.getShip().setPosition(new Position(30,25,9));
        assertTrue(calcul.shipIsInsideCheckpoint(parsedNextRound.getShip(),new Checkpoint(new Position(48,32,0),new Circle(50))));
        parsedNextRound.getShip().setPosition(new Position(50,8,9));
        assertTrue(calcul.shipIsInsideCheckpoint(parsedNextRound.getShip(),new Checkpoint(new Position(48,32,0),new Circle(50))));
        parsedNextRound.getShip().setPosition(new Position(15,56,9));
        assertTrue(calcul.shipIsInsideCheckpoint(parsedNextRound.getShip(),new Checkpoint(new Position(48,32,0),new Circle(50))));
        parsedNextRound.getShip().setPosition(new Position(7,20,9));
        assertTrue(calcul.shipIsInsideCheckpoint(parsedNextRound.getShip(),new Checkpoint(new Position(48,32,0),new Circle(50))));
        parsedNextRound.getShip().setPosition(new Position(20,30,9));
        assertTrue(calcul.shipIsInsideCheckpoint(parsedNextRound.getShip(),new Checkpoint(new Position(48,32,0),new Circle(50))));
    }

    @Test
    public void testShapesCollide() throws Exception {
        parsedNextRound.getShip().setPosition(new Position(2012.9130229585658,4116.056423252195,1.5794560650965999));
        assertTrue(calcul.shapesCollide(parsedNextRound.getShip(),parsedNextRound.getVisibleEntities().get(6)));
        parsedNextRound.getShip().setPosition(new Position(2128.58646,3892.33699,1.5794560650965999));
        assertTrue(calcul.shapesCollide(parsedNextRound.getShip(),parsedNextRound.getVisibleEntities().get(6)));
        parsedNextRound.getShip().setPosition(new Position(2120.23487,3838.0516,1.5794560650965999));
        assertTrue(calcul.shapesCollide(parsedNextRound.getShip(),parsedNextRound.getVisibleEntities().get(6)));
        parsedNextRound.getShip().setPosition(new Position(2003.31248,4261.89523,1.5794560650965999));
        assertTrue(calcul.shapesCollide(parsedNextRound.getShip(),parsedNextRound.getVisibleEntities().get(6)));
        parsedNextRound.getShip().setPosition(new Position(2241.3330,4401.78451,1.5794560650965999));
        assertTrue(calcul.shapesCollide(parsedNextRound.getShip(),parsedNextRound.getVisibleEntities().get(6)));
        parsedNextRound.getShip().setPosition(new Position(2270.5636,4165.85185,1.5794560650965999));
        assertTrue(calcul.shapesCollide(parsedNextRound.getShip(),parsedNextRound.getVisibleEntities().get(6)));
        parsedNextRound.getShip().setPosition(new Position(2262.2120,43969.58928,1.5794560650965999));
        assertFalse(calcul.shapesCollide(parsedNextRound.getShip(),parsedNextRound.getVisibleEntities().get(6)));
    }


    @Test
    public void testCalculateOarSpeed(){
        Calculator c = new Calculator();
        assertEquals(165.0, c.calculateOarSpeed(2,2));
        assertEquals(0, c.calculateOarSpeed(0,2));
        assertEquals(82.5, c.calculateOarSpeed(1,2));
    }


    @Test
    public void testCalculateWindSpeed(){
        Calculator c = new Calculator();
        Position shipPos = new Position(0, 0, Math.PI/4);
        assertEquals(0, c.calculateWindSpeed(0,0,null, null));
        assertEquals(4.898587196589413E-15, c.calculateWindSpeed(2,2,new Wind((3*Math.PI)/4, 80), new Ship("ship", 100, shipPos, "", null, null, null) ));
        assertEquals(0, c.calculateWindSpeed(0,2,new Wind((3*Math.PI)/4, 80), new Ship("ship", 100, shipPos, "", null, null, null) ));
        assertEquals(0, c.calculateWindSpeed(2,2,new Wind((3*Math.PI)/4, 0), new Ship("ship", 100, shipPos, "", null, null, null) ));
    }

    @Test
    public void testCalculateInfluenceOfStream(){
        Calculator c = new Calculator();
        Position p = c.calculateInfluenceOfStream(new Position(0, 0, Math.PI/4), new Stream("", new Position(0,0, Math.PI), null, 90), 10);
        assertEquals(-9.0, p.getX());
        assertEquals(1.102182119232618E-15, p.getY());
        p = c.calculateInfluenceOfStream(new Position(0, 0, Math.PI/4), new Stream("", new Position(0,0, Math.PI), null, 0), 10);
        assertEquals(0, p.getX());
        assertEquals(0, p.getY());
    }

    @Test
    void testPoints(){
        Rectangle s = new Rectangle(2, 10, 0);
        s.setCenterCoordinates(new Position(0,0,0));
        Point BG = calcul.calculateLowerLeftRectanglePoint(s);
        Point BD = calcul.calculateLowerRightRectanglePoint(s);
        Point HD = calcul.calculateUpperRightRectanglePoint(s);
        Point HG = calcul.calculateUpperLeftRectanglePoint(s);
        assertEquals(-5 ,BG.getX());
        assertEquals(-1 ,BG.getY());
        assertEquals(5 ,BD.getX());
        assertEquals(-1 ,BD.getY());
        assertEquals(5 ,HD.getX());
        assertEquals(1 ,HD.getY());
        assertEquals(-5 ,HG.getX());
        assertEquals(1,HG.getY());
    }
}

