package mody.logicking.Math.Methods;

import com.jjoe64.graphview.series.DataPoint;

import mody.logicking.Math.GraphEquation;
import mody.logicking.Math.Points.Point;
import mody.logicking.Math.Points.Points;

public class MLS implements IMethod{

    private static Points points;
    private static double[][] equations;
    private static Double[] as;
    private static final int power = 3;
    private static int size;

    public DataPoint[] resolve(){
        DataPoint[] dataPoints = new DataPoint[GraphEquation.getPoints().getPoints().length
                                                * GraphEquation.getAccuracy()];
        for(double i=0;i<dataPoints.length;i++){
            dataPoints[(int)i] = new DataPoint(i/GraphEquation.getAccuracy(),
                    getY(i/GraphEquation.getAccuracy(), GraphEquation.getParams()));
        }
        return dataPoints;
    }

    private static double getY(double x,Double[] params){
        double result = 0;
        for(int i=0;i<params.length;i++){
            result += params[i] * Math.pow(x,i);
        }
        return result;
    }

    public static Double[] resolveSystem(Points points){
        MLS.points = points;
        size = points.getPoints().length;

        makeEquations();
        resolveTillLastIsFound();
        findAllValues();
        return as;
    }

    private static void makeEquations() {
        equations = new double[power+1][power+2];
        for(int i=0;i<=power;i++){
            for(int j=0;j<=power;j++){
                equations[i][j] = makeSumOfVarsWithPow(i+j,0);
            }
            equations[i][power+1] = makeSumOfVarsWithPow(i,1);
        }
    }

    private static double makeSumOfVarsWithPow(int xPower, int yPower){
        double result = 0;
        if(yPower==0 && xPower==0)
            return size;
        if(yPower==0 && xPower>0){
            double[] temp = points.getXPoints();
            for(int i=0;i<size;i++)
                result += Math.pow(temp[i], xPower);
        }else if(yPower==1 && xPower==0){
            double[] temp = points.getYPoints();
            for(int i=0;i<size;i++)
                result += temp[i];
        }else if(xPower>0 && yPower==1){
            for(Point el : points.getPoints()){
                result += el.getY() * Math.pow(el.getX(), xPower);
            }
        }
        return result;
    }

    private static void resolveTillLastIsFound(){
        double coef;
        for(int i = 0 ; i<=power;i++){
            for(int j = i + 1;j<=power;j++){
                //Making coefficient
                coef = -equations[j][i] / equations[i][i];
                for(int z = i;z<=power+1;z++){
                    //Making sum j-th row with
                    equations[j][z] += coef*equations[i][z];
                }
            }
        }
    }

    private static void findAllValues() {
        as = new Double[power+1];
        for(int i=power;i>=0;i--){
            as[i] = 0d;
            for(int j=power;i<j;j--){
                as[i] -= equations[i][j]*as[j];
            }
            as[i] = (as[i] + equations[i][power+1])/equations[i][i] ;
        }
    }
}