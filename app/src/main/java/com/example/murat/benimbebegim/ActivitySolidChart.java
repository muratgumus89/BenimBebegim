package com.example.murat.benimbebegim;

import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.murat.benimbebegim.Databases.ActivityTable;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.Highlight;

import java.util.ArrayList;
import java.util.HashMap;


public class ActivitySolidChart extends FragmentActivity implements OnChartValueSelectedListener {

    protected HorizontalBarChart mChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_solid_activity_chart);

        mChart = (HorizontalBarChart) findViewById(R.id.hbcSolidActivityChart);
        mChart.setOnChartValueSelectedListener(this);
        mChart.setDrawBarShadow(false);
        mChart.setDrawValueAboveBar(true);
        mChart.setDescription("Solid Activities");
        mChart.setMaxVisibleValueCount(60);
        mChart.setPinchZoom(false);
        mChart.setDrawGridBackground(false);

        XAxis xl = mChart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setDrawAxisLine(true);
        xl.setDrawGridLines(true);
        xl.setGridLineWidth(0.3f);

        YAxis yl = mChart.getAxisLeft();
        yl.setDrawAxisLine(true);
        yl.setDrawGridLines(true);
        yl.setGridLineWidth(0.3f);

        YAxis yr = mChart.getAxisRight();
        yr.setDrawAxisLine(true);
        yr.setDrawGridLines(false);

        setData(12, 1000);
        mChart.animateY(2500);

        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.BELOW_CHART_LEFT);
        l.setFormSize(8f);
        l.setXEntrySpace(4f);


    }

    private void setData(int count, float range) {

        SharedPreferences pref = getSharedPreferences("MyPrefsFile", MODE_PRIVATE);

        String[] mMonths = new String[] {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Okt", "Nov", "Dec"};
        ArrayList<HashMap<String, String>> Jan, Feb, Mar, Apr, May, Jun, Jul, Aug, Sep, Okt, Nov, Dec;
        ActivityTable a_db = new ActivityTable(getApplicationContext());
        Log.i("Baby id ", pref.getString("baby_id", null));
        Jan = a_db.getSolidChartInfoByMonths(pref.getString("baby_id", null), "01/01/2015", "30/01/2015");
        Feb = a_db.getSolidChartInfoByMonths(pref.getString("baby_id", null), "01/02/2015", "30/02/2015");
        Mar = a_db.getSolidChartInfoByMonths(pref.getString("baby_id", null), "01/03/2015", "30/03/2015");
        Apr = a_db.getSolidChartInfoByMonths(pref.getString("baby_id", null), "01/04/2015", "30/04/2015");
        May = a_db.getSolidChartInfoByMonths(pref.getString("baby_id", null), "01/05/2015", "30/05/2015");
        Jun = a_db.getSolidChartInfoByMonths(pref.getString("baby_id", null), "01/06/2015", "30/06/2015");
        Jul = a_db.getSolidChartInfoByMonths(pref.getString("baby_id", null), "01/07/2015", "30/07/2015");
        Aug = a_db.getSolidChartInfoByMonths(pref.getString("baby_id", null), "01/08/2015", "30/08/2015");
        Sep = a_db.getSolidChartInfoByMonths(pref.getString("baby_id", null), "01/09/2015", "30/09/2015");
        Okt = a_db.getSolidChartInfoByMonths(pref.getString("baby_id", null), "01/10/2015", "30/10/2015");
        Nov = a_db.getSolidChartInfoByMonths(pref.getString("baby_id", null), "01/12/2015", "30/11/2015");
        Dec = a_db.getSolidChartInfoByMonths(pref.getString("baby_id", null), "01/12/2015", "30/12/2015");



        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < count; i++) {
            xVals.add(mMonths[i % 12]);
        }

        ArrayList<BarEntry> yVals1 = new ArrayList<BarEntry>();

//        Log.i("january", Jan.get(0).get("[SUM].(solid.[gram])"));

        if(Jan.get(0).get("[SUM](solid.[gram])") != null){
            yVals1.add(new BarEntry(Integer.valueOf(Jan.get(0).get("[SUM](solid.[gram])")), 0));
        }else{
            yVals1.add(new BarEntry(0, 0));
        }
        if(Feb.get(0).get("[SUM](solid.[gram])") != null){
            yVals1.add(new BarEntry(Integer.valueOf(Feb.get(0).get("[SUM](solid.[gram])")), 1));
        }else {
            yVals1.add(new BarEntry(0,1));
        }
        if(Mar.get(0).get("[SUM](solid.[gram])") != null){
            yVals1.add(new BarEntry(Integer.valueOf(Mar.get(0).get("[SUM](solid.[gram])")), 2));
        }else {
            yVals1.add(new BarEntry(0,2));
        }
        if(Apr.get(0).get("[SUM](solid.[gram])") != null){
            yVals1.add(new BarEntry(Integer.valueOf(Apr.get(0).get("[SUM](solid.[gram])")), 3));
        }else {
            yVals1.add(new BarEntry(0,3));
        }
        if(May.get(0).get("[SUM](solid.[gram])") != null){
            yVals1.add(new BarEntry(Integer.valueOf(May.get(0).get("[SUM](solid.[gram])")), 4));
        }else {
            yVals1.add(new BarEntry(0,4));
        }
        if(Jun.get(0).get("[SUM](solid.[gram])") != null){
            yVals1.add(new BarEntry(Integer.valueOf(Jun.get(0).get("[SUM](solid.[gram])")), 5));
        }else {
            yVals1.add(new BarEntry(0,5));
        }
        if(Jul.get(0).get("[SUM](solid.[gram])") != null){
            yVals1.add(new BarEntry(Integer.valueOf(Jul.get(0).get("[SUM](solid.[gram])")), 6));
        }else {
            yVals1.add(new BarEntry(0,6));
        }
        if(Aug.get(0).get("[SUM](solid.[gram])") != null){
            yVals1.add(new BarEntry(Integer.valueOf(Aug.get(0).get("[SUM](solid.[gram])")), 7));
        }else {
            yVals1.add(new BarEntry(0,7));
        }
        if(Sep.get(0).get("[SUM](solid.[gram])") != null){
            yVals1.add(new BarEntry(Integer.valueOf(Sep.get(0).get("[SUM](solid.[gram])")), 8));
        }else {
            yVals1.add(new BarEntry(0,8));
        }
        if(Okt.get(0).get("[SUM](solid.[gram])") != null){
            yVals1.add(new BarEntry(Integer.valueOf(Okt.get(0).get("[SUM](solid.[gram])")), 9));
        }else {
            yVals1.add(new BarEntry(0,9));
        }
        if(Nov.get(0).get("[SUM](solid.[gram])") != null){
            yVals1.add(new BarEntry(Integer.valueOf(Nov.get(0).get("[SUM](solid.[gram])")), 10));
        }else {
            yVals1.add(new BarEntry(0,10));
        }
        if(Dec.get(0).get("[SUM](solid.[gram])") != null){
            yVals1.add(new BarEntry(Integer.valueOf(Dec.get(0).get("[SUM](solid.[gram])")), 11));
        }else {
            yVals1.add(new BarEntry(0,11));
        }


        BarDataSet set1 = new BarDataSet(yVals1, "Solid Activity");
        set1.setBarSpacePercent(35f);

        ArrayList<BarDataSet> dataSets = new ArrayList<BarDataSet>();
        dataSets.add(set1);

        BarData data = new BarData(xVals, dataSets);
        data.setValueTextSize(10f);

        mChart.setData(data);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_solid_activity_chart, menu);
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
}
