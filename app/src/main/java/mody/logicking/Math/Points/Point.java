package mody.logicking.Math.Points;

import com.jjoe64.graphview.series.DataPoint;

public class Point {

    private double x;
    private double y;

    public Point(double x, double y) {
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
        return "x = " + getX() + " : y = " + getY();
    }

    public DataPoint toDataPoint(){
        return new DataPoint(getX(),getY());
    }
}
