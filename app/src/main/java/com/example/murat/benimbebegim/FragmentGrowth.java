package com.example.murat.benimbebegim;


import android.app.ActionBar;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

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
import java.util.List;
import java.util.Map;

public class FragmentGrowth extends Fragment implements OnChartValueSelectedListener {

    public static final String TAG = FragmentGrowth.class.getSimpleName();

    private PieChart mChart;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.layout_growthfragment, container,false);
        getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

        mChart = (PieChart) view.findViewById(R.id.pieChart1);
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
        return view;
	}

    private void setData(int count, int range) {
        float mult = range;
        ArrayList<HashMap<String, String>> records;
        ActivityTable a_db = new ActivityTable(getActivity().getApplicationContext());
        records = a_db.getMoodsInfoForMoodPieChart("40");

        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<Entry> yVals1 = new ArrayList<Entry>();

//        yVals1.add(new Entry(3, 0));
//        yVals1.add(new Entry(40, 0));
//        yVals1.add(new Entry(5, 0));
//        yVals1.add(new Entry(5, 0));
//        yVals1.add(new Entry(5, 0));

        int sadCnt = 0, chattyCnt = 0, mischeCnt= 0, sleepyCnt= 0, happyCnt = 0, grumpyCnt = 0, quiteCnt = 0, clingeCnt = 0, fussyCnt = 0, angryCnt = 0;
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

        xVals.add("Quite");
        xVals.add("Clinge");
        xVals.add("Chatty");
        xVals.add("Miscehievour");
        xVals.add("Sleepy");
        xVals.add("Happy");
        xVals.add("Fussy");
        xVals.add("Angry");

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

    public static FragmentGrowth newInstance() {
        return new FragmentGrowth();
    }

    @Override
    public void onValueSelected(Entry entry, int i, Highlight highlight) {

    }

    @Override
    public void onNothingSelected() {

    }
}
