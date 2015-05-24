package com.example.murat.benimbebegim;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.murat.benimbebegim.Databases.ActivityTable;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Highlight;

import java.util.ArrayList;
import java.util.HashMap;


public class ActivityBottleChart extends FragmentActivity implements OnChartValueSelectedListener {

    private LineChart mChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_bottle_chart);

        mChart = (LineChart) findViewById(R.id.chart1);
        mChart.setOnChartValueSelectedListener(this);

        mChart.setDescription("");
        mChart.setNoDataTextDescription("You need to provide data for the chart.");// enable value highlighting
        mChart.setHighlightEnabled(true);

        // enable touch gestures
        mChart.setTouchEnabled(true);

        // enable scaling and dragging
        mChart.setDragEnabled(true);
        mChart.setScaleEnabled(true);
        mChart.setDrawGridBackground(false);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(true);

        // set an alternative background color
        mChart.setBackgroundColor(Color.LTGRAY);
        // add data
        setData(12, 30);

        mChart.animateX(2500);

        Legend l = mChart.getLegend();

        // modify the legend ...
        // l.setPosition(LegendPosition.LEFT_OF_CHART);
        l.setForm(Legend.LegendForm.LINE);
        l.setTextSize(11f);
        l.setTextColor(Color.WHITE);
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);

        XAxis xAxis = mChart.getXAxis();
        xAxis.setTextSize(12f);
        xAxis.setTextColor(Color.WHITE);
        xAxis.setDrawGridLines(false);
        xAxis.setDrawAxisLine(false);
        xAxis.setSpaceBetweenLabels(1);

        YAxis leftAxis = mChart.getAxisLeft();
        leftAxis.setTextColor(ColorTemplate.getHoloBlue());
        leftAxis.setAxisMaxValue(200f);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setTextColor(Color.RED);
        rightAxis.setAxisMaxValue(900);
        rightAxis.setStartAtZero(false);
        rightAxis.setAxisMinValue(-200);
        rightAxis.setDrawGridLines(false);

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
        if(Jan.get(0).get("[SUM](solid.[gram])") != null){
            yVals1.add(new Entry(Integer.valueOf(Jan.get(0).get("[SUM](solid.[gram])")), 0));
        }else{
            yVals1.add(new Entry(0, 0));
        }
        if(Feb.get(0).get("[SUM](solid.[gram])") != null){
            yVals1.add(new Entry(Integer.valueOf(Feb.get(0).get("[SUM](solid.[gram])")), 1));
        }else {
            yVals1.add(new Entry(0,1));
        }
        if(Mar.get(0).get("[SUM](solid.[gram])") != null){
            yVals1.add(new Entry(Integer.valueOf(Mar.get(0).get("[SUM](solid.[gram])")), 2));
        }else {
            yVals1.add(new Entry(0,2));
        }
        if(Apr.get(0).get("[SUM](solid.[gram])") != null){
            yVals1.add(new Entry(Integer.valueOf(Apr.get(0).get("[SUM](solid.[gram])")), 3));
        }else {
            yVals1.add(new Entry(0,3));
        }
        if(May.get(0).get("[SUM](solid.[gram])") != null){
            yVals1.add(new Entry(Integer.valueOf(May.get(0).get("[SUM](solid.[gram])")), 4));
        }else {
            yVals1.add(new Entry(0,4));
        }
        if(Jun.get(0).get("[SUM](solid.[gram])") != null){
            yVals1.add(new Entry(Integer.valueOf(Jun.get(0).get("[SUM](solid.[gram])")), 5));
        }else {
            yVals1.add(new Entry(0,5));
        }
        if(Jul.get(0).get("[SUM](solid.[gram])") != null){
            yVals1.add(new Entry(Integer.valueOf(Jul.get(0).get("[SUM](solid.[gram])")), 6));
        }else {
            yVals1.add(new Entry(0,6));
        }
        if(Aug.get(0).get("[SUM](solid.[gram])") != null){
            yVals1.add(new Entry(Integer.valueOf(Aug.get(0).get("[SUM](solid.[gram])")), 7));
        }else {
            yVals1.add(new Entry(0,7));
        }
        if(Sep.get(0).get("[SUM](solid.[gram])") != null){
            yVals1.add(new Entry(Integer.valueOf(Sep.get(0).get("[SUM](solid.[gram])")), 8));
        }else {
            yVals1.add(new Entry(0,8));
        }
        if(Okt.get(0).get("[SUM](solid.[gram])") != null){
            yVals1.add(new Entry(Integer.valueOf(Okt.get(0).get("[SUM](solid.[gram])")), 9));
        }else {
            yVals1.add(new Entry(0,9));
        }
        if(Nov.get(0).get("[SUM](solid.[gram])") != null){
            yVals1.add(new Entry(Integer.valueOf(Nov.get(0).get("[SUM](solid.[gram])")), 10));
        }else {
            yVals1.add(new Entry(0,10));
        }
        if(Dec.get(0).get("[SUM](solid.[gram])") != null){
            yVals1.add(new Entry(Integer.valueOf(Dec.get(0).get("[SUM](solid.[gram])")), 11));
        }else {
            yVals1.add(new Entry(0,11));
        }
        /***************************************************************************/

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(yVals1, "Amount");
        set1.setAxisDependency(YAxis.AxisDependency.LEFT);
        set1.setColor(ColorTemplate.getHoloBlue());
        set1.setCircleColor(Color.WHITE);
        set1.setLineWidth(2f);
        set1.setCircleSize(3f);
        set1.setFillAlpha(65);
        set1.setFillColor(ColorTemplate.getHoloBlue());
        set1.setHighLightColor(Color.rgb(244, 117, 117));
        set1.setDrawCircleHole(false);
//        set1.setCircleHoleColor(Color.WHITE);

        ArrayList<Entry> yVals2 = new ArrayList<Entry>();

        yVals2.add(new Entry(120, 0));
        yVals2.add(new Entry(130, 1));
        yVals2.add(new Entry(140, 2));
        yVals2.add(new Entry(150, 3));
        yVals2.add(new Entry(160, 4));
        yVals2.add(new Entry(170, 5));
        yVals2.add(new Entry(180, 6));
        yVals2.add(new Entry(190, 7));
        yVals2.add(new Entry(200, 8));
        yVals2.add(new Entry(210, 9));
        yVals2.add(new Entry(220, 10));
        yVals2.add(new Entry(230, 11));


        // create a dataset and give it a type
        LineDataSet set2 = new LineDataSet(yVals2, "Time amount");
        set2.setAxisDependency(YAxis.AxisDependency.RIGHT);
        set2.setColor(Color.RED);
        set2.setCircleColor(Color.WHITE);
        set2.setLineWidth(2f);
        set2.setCircleSize(3f);
        set2.setFillAlpha(65);
        set2.setFillColor(Color.RED);
        set2.setDrawCircleHole(false);
        set2.setHighLightColor(Color.rgb(244, 117, 117));

        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        dataSets.add(set2);
        dataSets.add(set1); // add the datasets

        // create a data object with the datasets
        LineData data = new LineData(xVals, dataSets);
        data.setValueTextColor(Color.WHITE);
        data.setValueTextSize(9f);

        // set data
        mChart.setData(data);
    }
}
