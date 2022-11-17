package fr.unice.polytech.si3.qgl.theblackpearl.shape;

public class CartesianVector {

    private double x,y;

    public CartesianVector(double x, double y){
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public String toString() {
        return "CartesianVector{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
