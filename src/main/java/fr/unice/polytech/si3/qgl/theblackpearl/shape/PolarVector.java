package fr.unice.polytech.si3.qgl.theblackpearl.shape;

public class PolarVector {

    private double length;
    private double angle;

    public PolarVector(double length, double angle){
        this.length = length;
        this.angle=angle;
    }

    public double getLength() {
        return length;
    }

    public double getAngle() {
        return angle;
    }

    public void setAngle(double angle) {
        this.angle = angle;
    }

    @Override
    public String toString() {
        return "PolarVector{" +
                "length=" + length +
                ", angle=" + angle +
                '}';
    }
}
