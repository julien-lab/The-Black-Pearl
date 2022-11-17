package fr.unice.polytech.si3.qgl.theblackpearl.decisions;

import fr.unice.polytech.si3.qgl.theblackpearl.goal.*;
import fr.unice.polytech.si3.qgl.theblackpearl.sea_elements.*;
import fr.unice.polytech.si3.qgl.theblackpearl.shape.*;
import fr.unice.polytech.si3.qgl.theblackpearl.ship.*;

import java.util.ArrayList;
import java.util.List;


public class Calculator {

    private int[] numberSailorsToPlace;
    private int[] numberSailorsToPlaceCopy;

    public int[] getNumberSailorsToPlace() {
        return numberSailorsToPlace;
    }

    public void setNumberSailorsToPlace(int[] numberSailorsToPlace) {
        this.numberSailorsToPlace = numberSailorsToPlace;
    }

    public int[] getNumberSailorsToPlaceCopy() {
        return numberSailorsToPlaceCopy;
    }

    public void setNumberSailorsToPlaceCopy(int[] numberSailorsToPlaceCopy) {
        this.numberSailorsToPlaceCopy = numberSailorsToPlaceCopy;
    }

    public void decrementNumberSailorsToPlace(int decrementingNumber, boolean left, boolean right){
        if (left) {
            numberSailorsToPlace[0] -= decrementingNumber;
        }
        if (right){
            numberSailorsToPlace[1]-=decrementingNumber;
        }
    }

    public double calculateDistanceBetween2Points(Position pos1, Position pos2){
        double x = pos1.getX()-pos2.getX();
        double y = pos1.getY()-pos2.getY();
        return Math.sqrt((x*x)+(y*y));
    }

    public double calculateIdealAngle(Position pos1, Position pos2){
        return Math.atan2(pos2.getY()-pos1.getY(),pos2.getX()-pos1.getX()) - pos1.getOrientation();
    }

    public boolean shipIsInsideCheckpoint(Ship ship, Checkpoint checkpoint) throws Exception {
        return shapesCollide(ship, checkpoint);
    }

    public boolean shapesCollide(Object object1, Object object2) throws Exception {
        Shape shape1 =  setShape(object1);
        Shape shape2  = setShape(object2);
        if (shape1 == null || shape2 == null) return false;
        if (shape1 instanceof Circle && shape2 instanceof Circle){ return collisionCircles((Circle) shape1,(Circle) shape2);}
        List<CartesianVector> myList = testVectors(shape1,shape2);
        for (CartesianVector vector : myList) {
            if (!objectsInCollisionOntoAxis(shape1, shape2, vector)) return false;
        }
        return true;
    }

    private Shape setShape(Object object2) {
        Shape shape2 = null;
        if (object2 instanceof OtherShip ||object2 instanceof Reef || object2 instanceof Stream){
            shape2 = ((VisibleEntity) object2).getShape();
            if (shape2 instanceof Circle)
                shape2.setCenterCoordinates(((VisibleEntity) object2).getPosition());
            else if (shape2 instanceof Rectangle){
                shape2.setCenterCoordinates(((VisibleEntity) object2).getPosition());
                shape2.setOrientationCentre(((VisibleEntity) object2).getPosition().getOrientation());
            }
            else if (shape2 instanceof Polygon){
                ((Polygon) shape2).setPositionRelative(((VisibleEntity) object2).getPosition());
            }
        }
        else if (object2 instanceof Checkpoint){
            shape2 = ((Checkpoint) object2).getShape();
            if (shape2 instanceof Circle)
                shape2.setCenterCoordinates(((Checkpoint) object2).getPosition());
            else if (shape2 instanceof Rectangle){
                shape2.setCenterCoordinates(((Checkpoint) object2).getPosition());
                shape2.setOrientationCentre(((Checkpoint) object2).getPosition().getOrientation());
            }
            else if (shape2 instanceof Polygon){
                ((Polygon) shape2).setPositionRelative(((Checkpoint) object2).getPosition());
            }
        }
        else if (object2 instanceof Ship){
            shape2 = ((Ship) object2).getShape();
            if (shape2 instanceof Rectangle){
                shape2.setCenterCoordinates(((Ship) object2).getPosition());
                shape2.setOrientationCentre(((Ship) object2).getPosition().getOrientation());
            }
        }
        return shape2;
    }

    private boolean objectsInCollisionOntoAxis(Shape shape, Shape shape2, CartesianVector vector) {
        if ((shape instanceof Polygon && shape2 instanceof Rectangle) || (shape instanceof Rectangle && shape2 instanceof Polygon))
            return shadowInCollisionPolygoneRectangle(shape,shape2, vector);
        if ((shape instanceof Circle && shape2 instanceof Rectangle) || (shape instanceof Rectangle && shape2 instanceof Circle))
            return shadowInCollisionRectangleCercle(shape,shape2, vector);
        if ((shape instanceof Polygon && shape2 instanceof Circle) || (shape instanceof Circle && shape2 instanceof Polygon))
            return shadowInCollisionPolygoneCercle(shape,shape2, vector);
        if ((shape) instanceof Rectangle && shape2 instanceof Rectangle)
            return shadowInCollisionRectangleRectangle(shape,shape2,vector);
        if ((shape instanceof Polygon && shape2 instanceof Polygon))
            return shadowInCollisionPolygonePolygone(shape,shape2,vector);
        return false;
    }

    private boolean collisionCircles(Circle a, Circle b) {
        double distanceCBCC = calculateDistanceBetween2Points(a.getCenterCoordinates(),b.getCenterCoordinates());
        return distanceCBCC <= (a.getRadius()+b.getRadius());
    }

    private boolean collisionSegments(List<Point> pointsList1, List<Point> pointsList2, CartesianVector cartesianVector){
        if (cartesianVector.getY()/ cartesianVector.getX() <= 0){
            for (Point point : pointsList1) point.setX(-point.getX());
            for (Point point : pointsList2) point.setX(-point.getX());
        }
        double a = 9999999, b = -9999999, c = 9999999, d = -9999999;
        for (Point point : pointsList1) {
            if ((point.getY() + point.getX()) <= a) a = (point.getY() + point.getX());
            if ((point.getY() + point.getX()) >= b) b = (point.getY() + point.getX());
        }
        for (Point point : pointsList2) {
            if ((point.getY() + point.getX()) <= c) c = (point.getY() + point.getX());
            if ((point.getY() + point.getX()) >= d) d = (point.getY() + point.getX());
        }
        for (Point point : pointsList1) {
            if ((point.getY() + point.getX()) >= c && (point.getY() + point.getX()) <= d) return true;
        }
        for (Point point : pointsList2) {
            if ((point.getY() + point.getX()) >= a && (point.getY() + point.getX()) <= b) return true;
        }
        return false;
    }

    private boolean shadowInCollisionPolygonePolygone(Shape shape, Shape shape2, CartesianVector vecteur){
        List<Point> Liste1 = new ArrayList<>();
        List<Point> Liste2 = new ArrayList<>();
        creationProjectedPointsPolygon((Polygon)shape, Liste1, vecteur);
        creationProjectedPointsPolygon((Polygon)shape2, Liste2, vecteur);
        return collisionSegments(Liste1,Liste2,vecteur);
    }

    private boolean shadowInCollisionRectangleRectangle(Shape shape, Shape shape2, CartesianVector vecteur){
        List<Point> Liste1 = new ArrayList<>();
        List<Point> Liste2 = new ArrayList<>();
        creationProjectedPointsRectangle((Rectangle)shape, Liste1, vecteur);
        creationProjectedPointsRectangle((Rectangle)shape2, Liste2, vecteur);
        return collisionSegments(Liste1,Liste2,vecteur);
    }

    private boolean shadowInCollisionRectangleCercle(Shape shape, Shape shape2, CartesianVector vecteur) {
        List<Point> Liste1 = new ArrayList<>();
        List<Point> Liste2 = new ArrayList<>();
        if (shape instanceof Rectangle) {
            creationProjectedPointsRectangle((Rectangle)shape, Liste1, vecteur);
            creationProjectedPointsCircle((Circle)shape2, Liste2, vecteur);
        }
        else {
            creationProjectedPointsRectangle((Rectangle) shape2, Liste2, vecteur);
            creationProjectedPointsCircle((Circle)shape, Liste1, vecteur);
        }
        return collisionSegments(Liste1,Liste2,vecteur);
    }

    private boolean shadowInCollisionPolygoneCercle(Shape shape, Shape shape2, CartesianVector vector) {
        List<Point> list1 = new ArrayList<>();
        List<Point> list2 = new ArrayList<>();
        if (shape instanceof Polygon) {
            creationProjectedPointsPolygon((Polygon)shape, list1, vector);
            creationProjectedPointsCircle((Circle)shape2, list2, vector);
        }
        else {
            creationProjectedPointsPolygon((Polygon) shape2, list2, vector);
            creationProjectedPointsCircle((Circle)shape,list1, vector);
        }
        return collisionSegments(list1,list2,vector);
    }

    private boolean shadowInCollisionPolygoneRectangle(Shape shape, Shape shape2, CartesianVector vector) {
        List<Point> list1 = new ArrayList<>();
        List<Point> list2 = new ArrayList<>();
        if (shape instanceof Polygon){
            creationProjectedPointsPolygon((Polygon) shape, list1, vector);
            creationProjectedPointsRectangle((Rectangle) shape2, list2, vector);}
        else {
            creationProjectedPointsPolygon((Polygon) shape2, list2, vector);
            creationProjectedPointsRectangle((Rectangle) shape, list1, vector);}
        return collisionSegments(list1,list2,vector);
    }

    private void creationProjectedPointsCircle(Circle circle, List<Point> points, CartesianVector vector){  // pas sur de la fonction
        calculateCoordinatesProjectedPointsCircle(points, vector,0,0,circle.getCenterCoordinates().getX() , circle.getCenterCoordinates().getY(), circle);
    }

    private void creationProjectedPointsPolygon(Polygon polygon, List<Point> points, CartesianVector vector) { // PAS ENCORE GÉRÉ LA ROTATION DES POLYGONES + pas sur de la fonction
        double xb = 0;
        double yb = 0;
        for (int i = 0; i< polygon.getVertices().size(); i++){
            Point point = polygonPointRotation(polygon,i);
            calculateCoordinatesProjectedPoints(points, vector, xb, yb, point.getX(), point.getY());
        }
    }

    private Point polygonPointRotation(Polygon polygon, int i){
        Point point = polygon.getVertices().get(i);
        PolarVector polarVector = new PolarVector(Math.sqrt(Math.pow(point.getX(),2)+Math.pow(point.getY(),2)),Math.atan2(point.getY(),point.getX()));
        polarVector.setAngle(polarVector.getAngle()+polygon.getPositionRelative().getOrientation());
        return new Point(polarVector.getLength()*Math.cos(polarVector.getAngle())+polygon.getPositionRelative().getX(), polarVector.getLength()*Math.sin(polarVector.getAngle())+polygon.getPositionRelative().getY());
    }

    private void creationProjectedPointsRectangle(Rectangle rectangle, List<Point> points, CartesianVector vector){ // Pas sur de la fonction
        double xb = 0;
        double yb = 0;
        List<Point> rectangleCorners = new ArrayList<>();
        rectangleCorners.add(calculateLowerLeftRectanglePoint(rectangle));
        rectangleCorners.add(calculateLowerRightRectanglePoint(rectangle));
        rectangleCorners.add(calculateUpperLeftRectanglePoint(rectangle));
        rectangleCorners.add( calculateUpperRightRectanglePoint(rectangle));
        for (Point point : rectangleCorners){
            double xa = point.getX();
            double ya = point.getY();
            calculateCoordinatesProjectedPoints(points, vector, xb, yb, xa, ya);
        }

    }

    public Point calculateLowerLeftRectanglePoint(Rectangle rectangle){
        double modifiedX = Math.cos(rectangle.getOrientationRectangle()+rectangle.getCenterCoordinates().getOrientation())*-rectangle.getHeight()/2-Math.sin(rectangle.getOrientationRectangle()+rectangle.getCenterCoordinates().getOrientation())*-rectangle.getWidth()/2;
        double modifiedY = Math.sin(rectangle.getOrientationRectangle()+rectangle.getCenterCoordinates().getOrientation())*-rectangle.getHeight()/2+Math.cos(rectangle.getOrientationRectangle()+rectangle.getCenterCoordinates().getOrientation())*-rectangle.getWidth()/2;
        return new Point(modifiedX+rectangle.getCenterCoordinates().getX(),modifiedY+rectangle.getCenterCoordinates().getY());
    }

    public Point calculateLowerRightRectanglePoint(Rectangle rectangle){
        double modifiedX = Math.cos(rectangle.getOrientationRectangle()+rectangle.getCenterCoordinates().getOrientation())*+rectangle.getHeight()/2-Math.sin(rectangle.getOrientationRectangle()+rectangle.getCenterCoordinates().getOrientation())*-rectangle.getWidth()/2;
        double modifiedY = Math.sin(rectangle.getOrientationRectangle()+rectangle.getCenterCoordinates().getOrientation())*+rectangle.getHeight()/2+Math.cos(rectangle.getOrientationRectangle()+rectangle.getCenterCoordinates().getOrientation())*-rectangle.getWidth()/2;
        return new Point(modifiedX+rectangle.getCenterCoordinates().getX(),modifiedY+rectangle.getCenterCoordinates().getY());
    }

    public Point calculateUpperLeftRectanglePoint(Rectangle rectangle){
        double modifiedX = Math.cos(rectangle.getOrientationRectangle()+rectangle.getCenterCoordinates().getOrientation())*-rectangle.getHeight()/2-Math.sin(rectangle.getOrientationRectangle()+rectangle.getCenterCoordinates().getOrientation())*+rectangle.getWidth()/2;
        double modifiedY = Math.sin(rectangle.getOrientationRectangle()+rectangle.getCenterCoordinates().getOrientation())*-rectangle.getHeight()/2+Math.cos(rectangle.getOrientationRectangle()+rectangle.getCenterCoordinates().getOrientation())*+rectangle.getWidth()/2;
        return new Point(modifiedX+rectangle.getCenterCoordinates().getX(),modifiedY+rectangle.getCenterCoordinates().getY());
    }

    public Point calculateUpperRightRectanglePoint(Rectangle rectangle){
        double modifiedX = Math.cos(rectangle.getOrientationRectangle()+rectangle.getCenterCoordinates().getOrientation())*+rectangle.getHeight()/2-Math.sin(rectangle.getOrientationRectangle()+rectangle.getCenterCoordinates().getOrientation())*+rectangle.getWidth()/2;
        double modifiedY = Math.sin(rectangle.getOrientationRectangle()+rectangle.getCenterCoordinates().getOrientation())*+rectangle.getHeight()/2+Math.cos(rectangle.getOrientationRectangle()+rectangle.getCenterCoordinates().getOrientation())*+rectangle.getWidth()/2;
        return new Point(modifiedX+rectangle.getCenterCoordinates().getX(),modifiedY+rectangle.getCenterCoordinates().getY());
    }


    private void calculateCoordinatesProjectedPoints(List<Point> points, CartesianVector vector, double xb, double yb, double xa, double ya) {
        double v = ( (xa - xb) * vector.getX() + (ya - yb) * vector.getY() ) / ( Math.pow(vector.getX(),2) + Math.pow(vector.getY(),2) );
        points.add(new Point(xb + v * vector.getX(),yb + v * vector.getY()));
    }

    private void calculateCoordinatesProjectedPointsCircle(List<Point> points, CartesianVector vector, double xb, double yb, double xa, double ya, Circle circle) {
        double v = ( (xa - xb) * vector.getX() + (ya - yb) * vector.getY() ) / ( Math.pow(vector.getX(),2) + Math.pow(vector.getY(),2) );
        points.add(new Point(xb + v * vector.getX()+circle.getRadius()*Math.cos(Math.atan2(vector.getY(),vector.getX())),yb + v * vector.getY()+circle.getRadius()*Math.sin(Math.atan2(vector.getY(),vector.getX()))));
        points.add(new Point(xb + v * vector.getX()-circle.getRadius()*Math.cos(Math.atan2(vector.getY(),vector.getX())),yb + v * vector.getY()-circle.getRadius()*Math.sin(Math.atan2(vector.getY(),vector.getX()))));
    }

    private List<CartesianVector> testVectors(Shape shape1, Shape shape2) throws Exception {
        List<CartesianVector> cartesianVectors = new ArrayList<>();
        if (shape1 instanceof Circle) {
            cartesianVectors.add(vectorDirectorProjectionCircleAxis((Circle) shape1, shape2));
            cartesianVectors= directorVectorForAxisProjection(shape2,cartesianVectors);
        }
        else if (shape2 instanceof Circle) {
            cartesianVectors.add(vectorDirectorProjectionCircleAxis((Circle) shape2, shape1));
            cartesianVectors= directorVectorForAxisProjection(shape1,cartesianVectors);
        }
        else {
            cartesianVectors= directorVectorForAxisProjection(shape1,cartesianVectors);
            cartesianVectors= directorVectorForAxisProjection(shape2,cartesianVectors);
        }
        return cartesianVectors;
    }

    private CartesianVector vectorDirectorProjectionCircleAxis(Circle circle, Shape shape2) throws Exception {
        Point point = null;
        double d = 9999999.999;
        List<Point> pointList = new ArrayList<>();
        if (shape2 instanceof Rectangle){
            pointList.add(calculateLowerLeftRectanglePoint((Rectangle) shape2));
            pointList.add(calculateLowerRightRectanglePoint((Rectangle) shape2));
            pointList.add(calculateUpperLeftRectanglePoint((Rectangle) shape2));
            pointList.add( calculateUpperRightRectanglePoint((Rectangle) shape2));
            for (Point pointRectangle : pointList){
                if (Math.sqrt(Math.pow(pointRectangle.getX()-circle.getCenterCoordinates().getX(),2)+Math.pow(pointRectangle.getY()-circle.getCenterCoordinates().getY(),2)) < d){
                    point=pointRectangle;
                    d = Math.sqrt(Math.pow(pointRectangle.getX()-circle.getCenterCoordinates().getX(),2)+Math.pow(pointRectangle.getY()-circle.getCenterCoordinates().getY(),2));
                }
            }
        }
        else {
            for (Point pointPolygone : ((Polygon) shape2).getVertices()){
                if (Math.sqrt(Math.pow(pointPolygone.getX()-circle.getCenterCoordinates().getX(),2)+Math.pow(pointPolygone.getY()-circle.getCenterCoordinates().getY(),2)) < d){
                    point=pointPolygone;
                    d = Math.sqrt(Math.pow(pointPolygone.getX()-circle.getCenterCoordinates().getX(),2)+Math.pow(pointPolygone.getY()-circle.getCenterCoordinates().getY(),2));
                }
            }
        }
        return unitVectorForCirclePoint(circle, point);
    }

    private CartesianVector unitVectorForCirclePoint(Circle circle, Point point) throws Exception {
        if (point!=null){
            CartesianVector vector = new CartesianVector((circle.getCenterCoordinates().getX()-point.getX()),circle.getCenterCoordinates().getY()-point.getY());
            double vectorNorm = Math.sqrt(Math.pow(vector.getX(),2)+Math.pow(vector.getY(),2));
            if (vectorNorm !=0){
                return new CartesianVector(vector.getX()/vectorNorm,vector.getY()/vectorNorm);
            }
            else throw new Exception("Can't divide by zero");
        }
        else throw new Exception("Point is null");
    }

    public CartesianVector vectorUnitVector(CartesianVector vector) throws Exception {
        double vectorNorm = Math.sqrt(Math.pow(vector.getX(),2)+Math.pow(vector.getY(),2));
        if (vectorNorm != 0)
            return new CartesianVector(vector.getX()/vectorNorm,vector.getY()/vectorNorm);
        else throw new Exception("Can't divide by zero");
    }

    public PolarVector cartesianPolarVectorConversion(CartesianVector cartesianVector){
        return new PolarVector(Math.sqrt(Math.pow(cartesianVector.getX(),2)+Math.pow(cartesianVector.getY(),2)),Math.atan2(cartesianVector.getY(), cartesianVector.getX()));
    }

    public CartesianVector polarCartesianVectorConversion(PolarVector polarVector){
        return new CartesianVector(polarVector.getLength()*Math.cos(polarVector.getAngle()), polarVector.getLength()*Math.sin(polarVector.getAngle()));
    }

    private List<CartesianVector> directorVectorForAxisProjection(Shape shape, List<CartesianVector> vectors) throws Exception {
        if (shape instanceof Rectangle) {
            CartesianVector vector = new CartesianVector(Math.cos(((Rectangle) shape).getOrientationRectangle()+shape.getCenterCoordinates().getOrientation()),Math.sin(((Rectangle) shape).getOrientationRectangle()+shape.getCenterCoordinates().getOrientation()));
            vector= vectorUnitVector(vector);
            CartesianVector vector2 = new CartesianVector(Math.cos(((Rectangle) shape).getOrientationRectangle()+Math.PI/2+shape.getCenterCoordinates().getOrientation()),Math.sin(((Rectangle) shape).getOrientationRectangle()+Math.PI/2+shape.getCenterCoordinates().getOrientation()));
            vector2= vectorUnitVector(vector2);
            vectors.add(vector);
            vectors.add(vector2);
        }
        if (shape instanceof Polygon) {
            for (int i = 0; i < ((Polygon) shape).getVertices().size(); i++) {
                if (i != (((Polygon) shape).getVertices().size()-1) ){
                    CartesianVector cartesianVector = new CartesianVector(polygonPointRotation((Polygon) shape,i+1).getX() - polygonPointRotation((Polygon) shape,i).getX(),(polygonPointRotation((Polygon) shape,i+1).getY() - polygonPointRotation((Polygon) shape,i).getY()));
                    cartesianVector= vectorUnitVector(cartesianVector);
                    PolarVector polarVector = cartesianPolarVectorConversion(cartesianVector);
                    polarVector.setAngle(polarVector.getAngle()+Math.PI/2);
                    cartesianVector= polarCartesianVectorConversion(polarVector);
                    vectors.add(cartesianVector);
                }
                else{
                    CartesianVector cartesianVector = new CartesianVector(polygonPointRotation((Polygon) shape,i).getX() - polygonPointRotation((Polygon) shape,0).getX(), polygonPointRotation((Polygon) shape,i).getY() - polygonPointRotation((Polygon) shape,0).getY());
                    cartesianVector= vectorUnitVector(cartesianVector);
                    PolarVector polarVector= cartesianPolarVectorConversion(cartesianVector);
                    polarVector.setAngle(polarVector.getAngle()+Math.PI/2);
                    cartesianVector= polarCartesianVectorConversion(polarVector);
                    vectors.add(cartesianVector);
                }
            }
        }
        return vectors;
    }

    public double calculateOarsRotation(int nbLeftOar, int nbRightOar, int nbOars){
        if(nbRightOar>nbLeftOar){
            return ((nbRightOar-nbLeftOar)*Math.PI)/nbOars;
        }else if(nbRightOar<nbLeftOar){
            return -((nbLeftOar-nbRightOar)*Math.PI)/nbOars;
        }else{
            return 0.0;
        }
    }

    public double calculateOarSpeed(int nbActivesOars, int nbOars) {
        return (165*(double)nbActivesOars)/nbOars;
    }

    public Position calculateNewPositionShip(double shipSpeed , double rotationSpeed, Position shipPosition, int nbSteps){
        double speed = shipSpeed/nbSteps;
        double rotation = rotationSpeed/nbSteps;
        double newX = Math.cos(shipPosition.getOrientation())*speed;
        double newY = Math.sin(shipPosition.getOrientation())*speed;
        return new Position(shipPosition.getX()+newX,shipPosition.getY()+newY, shipPosition.getOrientation()+rotation );
    }

    public double calculateWindSpeed(int nbOppenedSail, int nbSail, Wind wind, Ship ship) {
        if (nbSail != 0)
            return ((double)nbOppenedSail/nbSail)*wind.getStrength()*Math.cos(wind.getOrientation()- ship.getPosition().getOrientation());
        else return 0;
    }

    public Position calculateInfluenceOfStream(Position shipPosition, Stream stream, int nbSteps) {
        double streamSpeed = stream.getStrength()/nbSteps;
        double newX = Math.cos(stream.getPosition().getOrientation())*streamSpeed ;
        double newY = Math.sin(stream.getPosition().getOrientation())*streamSpeed ;
        return new Position(shipPosition.getX()+newX,shipPosition.getY()+newY, shipPosition.getOrientation());
    }
}