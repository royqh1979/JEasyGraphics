package net.royqh.jturtle;

public class TurtleInfo {
    private double x;
    private double y;
    private double angle;

    public TurtleInfo(double x, double y, double angle) {
        this.x = x;
        this.y = y;
        this.angle = angle;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getAngle() {
        return angle;
    }
}
