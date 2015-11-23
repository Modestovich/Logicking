package mody.logicking;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import mody.logicking.Math.GraphEquation;
import mody.logicking.Math.Points.Point;
import mody.logicking.Math.Points.Points;

public class MainActivity extends Activity {

    GraphView graphView;
    Points pointsData;
    PointsGraphSeries<DataPoint> pointSeries;
    Map<String,LineGraphSeries<DataPoint>> seriesList;
    SharedPreferences sharedPrefs;
    int xBottomBound = 0;
    int xTopBound = 6;
    int yBottomBound = -20;
    int yTopBound = 20;
    int accuracy = 20;
    Random random;

    View.OnTouchListener graphViewTouchListener = new View.OnTouchListener() {

        private void refreshState(View v, MotionEvent event){
            int xPoint = getNearestXPoint(v,event);
            double yPoint = getNearestYPoint(v, event);
            pointsData.getPoints()[xPoint].setY(yPoint);
            updateGraphView();
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            int action = event.getAction();
            if(action==MotionEvent.ACTION_DOWN || action==MotionEvent.ACTION_MOVE){
                refreshState(v,event);
                return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        random = new Random();
        initGraphView();
        drawDefaultValues();
    }

    public void initGraphView(){
        graphView = (GraphView) findViewById(R.id.graph);
        sharedPrefs = PreferenceManager
                .getDefaultSharedPreferences(this);
        setValuesFromPrefs();
        graphView.setBackgroundColor(Color.WHITE);
        graphView.setTitleTextSize(20);
        graphView.setTitleColor(Color.BLACK);
        graphView.getLegendRenderer().setVisible(true);
        graphView.getGridLabelRenderer().setHorizontalAxisTitle("X");
        graphView.getGridLabelRenderer().setVerticalAxisTitle("Y");
        graphView.getViewport().setYAxisBoundsManual(true);
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.setOnTouchListener(graphViewTouchListener);
    }

    public void drawDefaultValues(){
        pointsData =  new Points(new Point[]{
                new Point(0, 0),
                new Point(1, 0),
                new Point(2, 0),
                new Point(3, 0),
                new Point(4, 0),
                new Point(5, 0),
                new Point(6, 0)
        });
        drawPoints();
        drawGraphics();
    }

    public void drawPoints(){
        pointSeries = new PointsGraphSeries<DataPoint>(
                Points.toDataPointsArray(pointsData));
        pointSeries.setSize(5);
        pointSeries.setShape(PointsGraphSeries.Shape.POINT);
        pointSeries.setTitle("Points");
        graphView.addSeries(pointSeries);
    }

    public void drawGraphics(){
        Map<String, DataPoint[]> points = generatePointsForEquation(accuracy);
        Set<Map.Entry<String,DataPoint[]>> pointsAndNames = points.entrySet();
        seriesList =
                new HashMap<String, LineGraphSeries<DataPoint>>();
        for(Map.Entry<String,DataPoint[]> pointsAndName: pointsAndNames){
            addSeriesToGraph(pointsAndName);
        }
    }

    private void addSeriesToGraph(Map.Entry<String, DataPoint[]> pointsAndName) {
        final LineGraphSeries<DataPoint> series = new LineGraphSeries<DataPoint>(
                pointsAndName.getValue());
        series.setTitle(pointsAndName.getKey());
        series.setThickness(3);
        series.setColor(Color.rgb(random.nextInt(255),random.nextInt(255),random.nextInt(255)));
        seriesList.put(pointsAndName.getKey(),series);
        graphView.addSeries(seriesList.get(pointsAndName.getKey()));
    }

    private Map<String,DataPoint[]> generatePointsForEquation(int accuracy) {
        return GraphEquation.getDataPoints(pointsData, accuracy);
    }

    public void updateGraphView(){
        updatePoints();
        updateGraphics();
    }

    public void updatePoints(){
        pointSeries.resetData(Points.toDataPointsArray(pointsData));
    }

    public void updateGraphics(){
        Map<String, DataPoint[]> points = generatePointsForEquation(accuracy);
        Set<Map.Entry<String,DataPoint[]>> pointsAndNames = points.entrySet();
        for(Map.Entry<String,DataPoint[]> pointsAndName: pointsAndNames){
            seriesList.get(pointsAndName.getKey()).resetData(pointsAndName.getValue());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(this, SettingActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        setValuesFromPrefs();
        updateGraphics();
    }

    private void setValuesFromPrefs(){

        graphView.setTitle(sharedPrefs.getString("graphName", "Approximation"));
        String[] boundaries = {"0","6"};
        xBottomBound = Integer.parseInt(boundaries[0]);
        xTopBound = Integer.parseInt(boundaries[1]);
        graphView.getViewport().setMinX(xBottomBound);
        graphView.getViewport().setMaxX(xTopBound);
        boundaries = sharedPrefs.getString("boundariesY", "-10/10").split("/");
        yBottomBound = Integer.parseInt(boundaries[0]);
        yTopBound = Integer.parseInt(boundaries[1]);
        graphView.getViewport().setMinY(yBottomBound);
        graphView.getViewport().setMaxY(yTopBound);

        accuracy = Integer.parseInt(sharedPrefs.getString("accuracy", "10"));
    }
    public void resetGraph(View view){
        pointsData =  new Points(new Point[]{
                new Point(0, 0),
                new Point(1, 0),
                new Point(2, 0),
                new Point(3, 0),
                new Point(4, 0),
                new Point(5, 0),
                new Point(6, 0)
        });
        updateGraphView();
    }

    public int getNearestXPoint(View v, MotionEvent event){
        double xMin = graphView.getViewport().getMinX(false);
        double xMax = graphView.getViewport().getMaxX(false);
        double xCount = Math.abs(xMax) +
                Math.abs(xMin);
        double xDivider = (v.getWidth() - 60)/xCount;
        double x = (event.getX()-40)/xDivider;
        if(x>xTopBound)// more than highest position
            return xTopBound;
        else if(x<xBottomBound)// less than lowest position
            return xBottomBound;
        return (int)Math.round(x);
    }
    private double getNearestYPoint(View v, MotionEvent event) {
        double yMin = graphView.getViewport().getMinY(false);
        double yMax = graphView.getViewport().getMaxY(false);
        double yCount = Math.abs(yMax) +
                Math.abs(yMin);
        double yDivider = (v.getHeight() - 80)/yCount;
        double y = (v.getHeight( ) - event.getY() - 40)/yDivider + yMin;
        if(y>yTopBound)//more than highest position
            return yTopBound;
        else if(y<yBottomBound)// less than lowest position
            return yBottomBound;
        return y;
    }
}