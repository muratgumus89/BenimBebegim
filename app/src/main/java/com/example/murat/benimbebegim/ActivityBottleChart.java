package com.example.murat.benimbebegim;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;

import com.example.murat.benimbebegim.Databases.ActivityTable;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.listener.OnChartGestureListener;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Highlight;

import java.util.ArrayList;
import java.util.HashMap;


public class ActivityBottleChart extends FragmentActivity implements OnChartValueSelectedListener, OnChartGestureListener {

    private LineChart mChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_bottle_chart);

        mChart = (LineChart) findViewById(R.id.chart1);
        mChart.setOnChartGestureListener(this);
        mChart.setOnChartValueSelectedListener(this);

        // no description text
        mChart.setDescription("");
        mChart.setNoDataTextDescription("You need to provide data for the chart.");

        // enable value highlighting
        mChart.setHighlightEnabled(true);

        // enable touch gestures
        mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        // mChart.setScaleXEnabled(true);
        // mChart.setScaleYEnabled(true);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(true);

        // set an alternative background color
        // mChart.setBackgroundColor(Color.GRAY);




        // enable/disable highlight indicators (the lines that indicate the
        // highlighted Entry)
        mChart.setHighlightIndicatorEnabled(false);

        // add data
        setData(12, 100);
        mChart.animateX(2500);
        // mChart.setVisibleYRange(30, AxisDependency.LEFT);

        // // restrain the maximum scale-out factor
        // mChart.setScaleMinima(3f, 3f);
        //
        // // center the view to a specific position inside the chart
        // mChart.centerViewPort(10, 50, AxisDependency.LEFT);

        // get the legend (only possible after setting data)
        Legend l = mChart.getLegend();

        // modify the legend ...
        // l.setPosition(LegendPosition.LEFT_OF_CHART);
        l.setForm(Legend.LegendForm.LINE);

        // // dont forget to refresh the drawing
        // mChart.invalidate();

        XAxis xAxis = mChart.getXAxis();
        xAxis.setTextSize(12f);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setSpaceBetweenLabels(1);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTextColor(ColorTemplate.getHoloBlue());
        leftAxis.setAxisMaxValue(700);
        leftAxis.setAxisMinValue(0f);
        leftAxis.setDrawGridLines(true);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_bottle_chart, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onValueSelected(Entry entry, int i, Highlight highlight) {

    }

    @Override
    public void onNothingSelected() {

    }

    private void setData(int count, float range) {

        SharedPreferences pref = getSharedPreferences("MyPrefsFile", MODE_PRIVATE);

        String[] mMonths = new String[] {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec"};
        ArrayList<HashMap<String, String>> Jan, Feb, Mar, Apr, May, Jun, Jul, Aug, Sep, Okt, Nov, Dec;
        ActivityTable a_db = new ActivityTable(getApplicationContext());

        Jan = a_db.getBottleChartInfo(pref.getString("baby_id", null), "01/01/2015");
        Feb = a_db.getBottleChartInfo(pref.getString("baby_id", null), "01/02/2015");
        Mar = a_db.getBottleChartInfo(pref.getString("baby_id", null), "01/03/2015");
        Apr = a_db.getBottleChartInfo(pref.getString("baby_id", null), "01/04/2015");
        May = a_db.getBottleChartInfo(pref.getString("baby_id", null), "01/05/2015");
        Jun = a_db.getBottleChartInfo(pref.getString("baby_id", null), "01/06/2015");
        Jul = a_db.getBottleChartInfo(pref.getString("baby_id", null), "01/07/2015");
        Aug = a_db.getBottleChartInfo(pref.getString("baby_id", null), "01/08/2015");
        Sep = a_db.getBottleChartInfo(pref.getString("baby_id", null), "01/09/2015");
        Okt = a_db.getBottleChartInfo(pref.getString("baby_id", null), "01/10/2015");
        Nov = a_db.getBottleChartInfo(pref.getString("baby_id", null), "01/11/2015");
        Dec = a_db.getBottleChartInfo(pref.getString("baby_id", null), "01/12/2015");



        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            xVals.add(mMonths[i % 12]);
        }

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        /**************************************************************************/
        if(Jan.get(0).get("sum(bottle.[amount])") != null){
            yVals1.add(new Entry(Integer.valueOf(Jan.get(0).get("sum(bottle.[amount])")), 0));
        }else{
            yVals1.add(new Entry(0, 0));
        }
        if(Feb.get(0).get("sum(bottle.[amount])") != null){
            yVals1.add(new Entry(Integer.valueOf(Feb.get(0).get("sum(bottle.[amount])")), 1));
        }else {
            yVals1.add(new Entry(0,1));
        }
        if(Mar.get(0).get("sum(bottle.[amount])") != null){
            yVals1.add(new Entry(Integer.valueOf(Mar.get(0).get("sum(bottle.[amount])")), 2));
        }else {
            yVals1.add(new Entry(0,2));
        }
        if(Apr.get(0).get("sum(bottle.[amount])") != null){
            yVals1.add(new Entry(Integer.valueOf(Apr.get(0).get("sum(bottle.[amount])")), 3));
        }else {
            yVals1.add(new Entry(0,3));
        }
        if(May.get(0).get("sum(bottle.[amount])") != null){
            yVals1.add(new Entry(Integer.valueOf(May.get(0).get("sum(bottle.[amount])")), 4));
        }else {
            yVals1.add(new Entry(0,4));
        }
        if(Jun.get(0).get("sum(bottle.[amount])") != null){
            yVals1.add(new Entry(Integer.valueOf(Jun.get(0).get("sum(bottle.[amount])")), 5));
        }else {
            yVals1.add(new Entry(0,5));
        }
        if(Jul.get(0).get("sum(bottle.[amount])") != null){
            yVals1.add(new Entry(Integer.valueOf(Jul.get(0).get("sum(bottle.[amount])")), 6));
        }else {
            yVals1.add(new Entry(0,6));
        }
        if(Aug.get(0).get("sum(bottle.[amount])") != null){
            yVals1.add(new Entry(Integer.valueOf(Aug.get(0).get("sum(bottle.[amount])")), 7));
        }else {
            yVals1.add(new Entry(0,7));
        }
        if(Sep.get(0).get("sum(bottle.[amount])") != null){
            yVals1.add(new Entry(Integer.valueOf(Sep.get(0).get("sum(bottle.[amount])")), 8));
        }else {
            yVals1.add(new Entry(0,8));
        }
        if(Okt.get(0).get("sum(bottle.[amount])") != null){
            yVals1.add(new Entry(Integer.valueOf(Okt.get(0).get("sum(bottle.[amount])")), 9));
        }else {
            yVals1.add(new Entry(0,9));
        }
        if(Nov.get(0).get("sum(bottle.[amount])") != null){
            yVals1.add(new Entry(Integer.valueOf(Nov.get(0).get("sum(bottle.[amount])")), 10));
        }else {
            yVals1.add(new Entry(0,10));
        }
        if(Dec.get(0).get("sum(bottle.[amount])") != null){
            yVals1.add(new Entry(Integer.valueOf(Dec.get(0).get("sum(bottle.[amount])")), 11));
        }else {
            yVals1.add(new Entry(0,11));
        }
        /***************************************************************************/

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(yVals1, "Amount");
        // set1.setFillColor(Color.RED);

        // set the line to be drawn like this "- - - - - -"
        set1.enableDashedLine(10f, 5f, 0f);
        set1.setColor(Color.BLACK);
        set1.setCircleColor(Color.BLACK);
        set1.setLineWidth(1f);
        set1.setCircleSize(3f);
        set1.setDrawCircleHole(false);
        set1.setValueTextSize(9f);
        set1.setFillAlpha(65);
        set1.setFillColor(Color.BLACK);



        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(set1); // add the datasets

        // create a data object with the datasets
        LineData data = new LineData(xVals, dataSets);

        LimitLine ll1 = new LimitLine(130f, "Upper Limit");
        ll1.setLineWidth(4f);
        ll1.enableDashedLine(10f, 10f, 0f);
        ll1.setLabelPosition(LimitLine.LimitLabelPosition.POS_RIGHT);
        ll1.setTextSize(10f);

        LimitLine ll2 = new LimitLine(-30f, "Lower Limit");
        ll2.setLineWidth(4f);
        ll2.enableDashedLine(10f, 10f, 0f);
        ll2.setLabelPosition(LimitLine.LimitLabelPosition.POS_RIGHT);
        ll2.setTextSize(10f);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.removeAllLimitLines(); // reset all limit lines to avoid overlapping lines
        leftAxis.addLimitLine(ll1);
        leftAxis.addLimitLine(ll2);
        leftAxis.setAxisMaxValue(220f);
        leftAxis.setAxisMinValue(-50f);
        leftAxis.setStartAtZero(false);

        mChart.getAxisRight().setEnabled(false);

        // set data
        mChart.setData(data);
    }

    @Override
    public void onChartLongPressed(MotionEvent motionEvent) {

    }

    @Override
    public void onChartDoubleTapped(MotionEvent motionEvent) {

    }

    @Override
    public void onChartSingleTapped(MotionEvent motionEvent) {

    }

    @Override
    public void onChartFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {

    }
}
