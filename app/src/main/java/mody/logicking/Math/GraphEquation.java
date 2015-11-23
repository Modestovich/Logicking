package mody.logicking.Math;

import android.util.Log;

import com.jjoe64.graphview.series.DataPoint;

import java.util.HashMap;
import java.util.Map;

import mody.logicking.Math.Methods.IMethod;
import mody.logicking.Math.Points.Points;

public class GraphEquation {

    private static Double[] params;
    private static Points points;
    private static int accuracy;
    private static final String methodsPath = "mody.logicking.Math.Methods.";
    private GraphEquation(){}

    public static Map<String,DataPoint[]>
                getDataPoints(Points points,int accuracy){
        GraphEquation.accuracy = accuracy;
        GraphEquation.points = points;
        Map<String,Double[]> equations = Solver.solve();
        Map<String, DataPoint[]> result = new HashMap<String, DataPoint[]>();
        for(Map.Entry<String,Double[]> equation : equations.entrySet()){
            result.put(equation.getKey(), getPointsForMethod(equation.getValue(), equation.getKey()));
        }
        return result;
    }
    private static DataPoint[] getPointsForMethod(Double[] equation,String className){
        params = equation;
        return retrieveDataPoints(className);
    }

    private static DataPoint[] retrieveDataPoints(String className) {
        try {

            Class resolver = Class.forName(methodsPath + className);
            IMethod method = (IMethod)resolver.newInstance();
            return method.resolve();
        }catch(ClassNotFoundException ex){
            Log.i("Exception","Class not found");
        }catch(NullPointerException ex){
            Log.i("Exception","Null pointer");
        }catch(IllegalAccessException ex){
            Log.i("Exception","Illegal access");
        }catch(InstantiationException ex){
            Log.i("Exception","Instantiation");
        }
        return new DataPoint[]{};
    }

    public static int getAccuracy(){
        return accuracy;
    }

    public static Points getPoints(){
        return points;
    }

    public static Double[] getParams(){
        return params;
    }
}