package mody.logicking.Math;

import java.util.HashMap;
import java.util.Map;

import mody.logicking.Math.Methods.MLS;

public class Solver {

    public static Map<String,Double[]> solve(){
        Map<String, Double[]> result = new HashMap<String, Double[]>();
        result.put("MLS", MLS.resolveSystem(GraphEquation.getPoints()));
        result.put("Lagrange",new Double[]{});
        //Он не должен быть пустым, но программа будет быстрее работать,если я не буду находить
        // многочлен,а потом подставлять значения. Вместо этого сразу буду подставлять значения.
        return result;
    }

}
