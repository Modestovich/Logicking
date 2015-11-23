package mody.logicking.Math.Points;

import com.jjoe64.graphview.series.DataPoint;

public class Points {
    private Point[] points;

    public Points(Point[] points){
        this.points = points;
    }

    public Point[] getPoints() {
        return points;
    }

    public double[] getXPoints(){
        double[] xs = new double[points.length];
        for(int i=0;i<points.length;i++)
            xs[i] = points[i].getX();
        return xs;
    }

    public double[] getYPoints(){
        double[] ys = new double[points.length];
        for(int i=0;i<points.length;i++)
            ys[i] = points[i].getY();
        return ys;
    }

    public static DataPoint[] toDataPointsArray(Points points){
        DataPoint[] dps = new DataPoint[points.getPoints().length];
        Point[] pointsArray = points.getPoints();
        for(int i=0;i<pointsArray.length;i++){
            dps[i] = pointsArray[i].toDataPoint();
        }
        return dps;
    }
}