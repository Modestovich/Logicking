package mody.logicking.Math.Methods;

import com.jjoe64.graphview.series.DataPoint;

import mody.logicking.Math.GraphEquation;

public class Lagrange implements IMethod{

    private static double[] xs;
    private static double[] ys;

    public DataPoint[] resolve(){
        DataPoint[] dataPoints = new DataPoint[GraphEquation.getPoints().getPoints().length
                * GraphEquation.getAccuracy()];
        xs = GraphEquation.getPoints().getXPoints();
        ys = GraphEquation.getPoints().getYPoints();
        for(double i=0;i<dataPoints.length;i++){
            dataPoints[(int)i] = new DataPoint(i/GraphEquation.getAccuracy(),
                    getY(i/GraphEquation.getAccuracy()));
        }
        return dataPoints;
    }

    private double getY(double x){
        double L = 0;

        for (int i=0;i<ys.length;i++)
        {
            double l = 1.0;
            for (int j=0;j<xs.length;j++)
                if (j != i)
                    l *= (x - xs[j])/ (xs[i] - xs[j]);
            L += l*ys[i];
        }
        return L;
    }
}
