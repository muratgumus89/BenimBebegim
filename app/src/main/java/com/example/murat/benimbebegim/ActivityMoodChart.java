package com.example.murat.benimbebegim;

import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.example.murat.benimbebegim.Databases.ActivityTable;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Highlight;
import com.github.mikephil.charting.utils.PercentFormatter;

import java.util.ArrayList;
import java.util.HashMap;


public class ActivityMoodChart extends FragmentActivity implements OnChartValueSelectedListener {

    private PieChart mChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_mood_chart);
        mChart = (PieChart) findViewById(R.id.pieChart1);
        mChart.setUsePercentValues(true);
        mChart.setHoleColorTransparent(true);
        mChart.setHoleRadius(60f);
        mChart.setDescription("Moods Charts");
        mChart.setDrawCenterText(true);
        mChart.setDrawHoleEnabled(true);
        mChart.setRotationAngle(0);
        mChart.setRotationEnabled(true);
        mChart.setOnChartValueSelectedListener(this);
        mChart.setCenterText("Moods\nFor Baby One");
        setData(3, 100);
        mChart.animateXY(1500, 1500);

        Legend l = mChart.getLegend();
        l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(5f);
    }

    private void setData(int count, int range) {
        float mult = range;
        ArrayList<HashMap<String, String>> records;
        ActivityTable a_db = new ActivityTable(getApplicationContext());
        records = a_db.getMoodsInfoForMoodPieChart("40");

        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

        int sadCnt = 0, chattyCnt = 0, mischeCnt= 0, sleepyCnt= 0, happyCnt = 0, grumpyCnt = 0, quiteCnt = 0, clingeCnt = 0, fussyCnt = 0, angryCnt = 0, cryingCnt = 0, playfullCnt = 0, bashfullCnt = 0;
        if (records.size() > 0) {
            for (int i = 0; i < records.size(); i++) {
                if (records.get(i).get("m_type").equals("Quite")) {
                    quiteCnt++;
                }else if (records.get(i).get("m_type").equals("Clinge")) {
                    clingeCnt++;
                }else if (records.get(i).get("m_type").equals("Chatty")) {
                    chattyCnt++;
                }else if (records.get(i).get("m_type").equals("Miscehievour")) {
                    mischeCnt++;
                }else if (records.get(i).get("m_type").equals("Sleepy")) {
                    sleepyCnt++;
                }else if (records.get(i).get("m_type").equals("Happy")) {
                    happyCnt++;
                }else if (records.get(i).get("m_type").equals("Fussy")) {
                    fussyCnt++;
                }else if (records.get(i).get("m_type").equals("Angry")) {
                    angryCnt++;
                }else if (records.get(i).get("m_type").equals("Grumpy")) {
                    grumpyCnt++;
                }else if (records.get(i).get("m_type").equals("Sad")) {
                    sadCnt++;
                }else if (records.get(i).get("m_type").equals("Crying")) {
                    cryingCnt++;
                }else if (records.get(i).get("m_type").equals("Playfull")) {
                    playfullCnt++;
                }else if (records.get(i).get("m_type").equals("Bashfull")) {
                    bashfullCnt++;
                }
            }
        }

        yVals1.add(new Entry(quiteCnt, 0));
        yVals1.add(new Entry(clingeCnt, 1));
        yVals1.add(new Entry(chattyCnt, 2));
        yVals1.add(new Entry(mischeCnt, 3));
        yVals1.add(new Entry(sleepyCnt, 4));
        yVals1.add(new Entry(happyCnt, 5));
        yVals1.add(new Entry(fussyCnt, 6));
        yVals1.add(new Entry(angryCnt, 7));
        yVals1.add(new Entry(grumpyCnt, 8));
        yVals1.add(new Entry(sadCnt, 9));
        yVals1.add(new Entry(cryingCnt, 9));
        yVals1.add(new Entry(playfullCnt, 9));
        yVals1.add(new Entry(bashfullCnt, 9));

        xVals.add("Quite");
        xVals.add("Clinge");
        xVals.add("Chatty");
        xVals.add("Miscehievour");
        xVals.add("Sleepy");
        xVals.add("Happy");
        xVals.add("Fussy");
        xVals.add("Angry");
        xVals.add("Grumpy");
        xVals.add("Sad");
        xVals.add("Crying");
        xVals.add("Playfull");
        xVals.add("Bashfull");

        PieDataSet dataSet = new PieDataSet(yVals1, "Baby Moods");
        dataSet.setSliceSpace(3f);

        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        mChart.setData(data);

        // undo all highlights
        mChart.highlightValues(null);

        mChart.invalidate();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_mood_chart, menu);
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
