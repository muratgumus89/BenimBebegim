package com.example.murat.benimbebegim;


import android.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.murat.benimbebegim.Databases.ActivityTable;
import com.example.murat.benimbebegim.Databases.GrowthDatabase;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.Highlight;
import com.github.mikephil.charting.utils.PercentFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class FragmentGrowth extends Fragment implements View.OnClickListener, OnChartValueSelectedListener {

    public static final String TAG = FragmentGrowth.class.getSimpleName();

    EditText etWeight, etHeight;
    Button btnAddEntry;
    private LineChart mChart;

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		View view = inflater.inflate(R.layout.layout_growthfragment, container,false);
        getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

        etWeight = (EditText) view.findViewById(R.id.etNewWieght);
        etHeight = (EditText) view.findViewById(R.id.etNewHeight);
        btnAddEntry = (Button) view.findViewById(R.id.btnAddNewEntry);
        btnAddEntry.setOnClickListener(this);

        mChart = (LineChart) view.findViewById(R.id.chart1);
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
        mChart.setDrawGridBackground(false);

        // if disabled, scaling can be done on x- and y-axis separately
        mChart.setPinchZoom(true);

        // set an alternative background color
        mChart.setBackgroundColor(Color.LTGRAY);

        // add data
        setData();

        mChart.animateX(2500);

        // get the legend (only possible after setting data)
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
        leftAxis.setAxisMaxValue(50000);
        leftAxis.setAxisMinValue(0f);
        leftAxis.setDrawGridLines(true);

        YAxis rightAxis = mChart.getAxisRight();
        rightAxis.setTextColor(Color.RED);
        rightAxis.setAxisMaxValue(150);
        rightAxis.setStartAtZero(false);
        rightAxis.setAxisMinValue(0f);
        rightAxis.setDrawGridLines(false);

        return view;
	}



    public static FragmentGrowth newInstance() {
        return new FragmentGrowth();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnAddNewEntry:
                String weight = etWeight.getText().toString();
                String height = etHeight.getText().toString();
                String saveDate;

                SharedPreferences pref = this.getActivity().getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE);

                Log.i("Baby id", pref.getString("baby_id", null));
                Log.i("notes", weight + " " + height);

                Calendar c_date = Calendar.getInstance();
                SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
                saveDate = date.format(c_date.getTime());

                GrowthDatabase a_db = new GrowthDatabase(getActivity().getApplicationContext());
                a_db.addWeight(pref.getString("baby_id", null),saveDate,Integer.valueOf(weight));
                a_db.addHeight(pref.getString("baby_id", null),saveDate,Integer.valueOf(height));

                a_db.close();

                setData();

                Toast.makeText(getActivity().getApplicationContext(), getResources().getString(R.string.add_record), Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
    }

    @Override
    public void onValueSelected(Entry entry, int i, Highlight highlight) {

    }

    @Override
    public void onNothingSelected() {

    }

    private void setData() {
        /********* X AXIS VALUES GOES RIGHT IN THIS PLACE*/
        ArrayList<String> xVals = new ArrayList<String>();
        ArrayList<HashMap<String, String>> records, values;
        GrowthDatabase a_db = new GrowthDatabase(getActivity().getApplicationContext());
        SharedPreferences pref = getActivity().getSharedPreferences("MyPrefsFile", Context.MODE_PRIVATE);
        records = a_db.getAllHeightAndWeightCount(pref.getString("baby_id", null));

        for (int i = 0; i < Integer.valueOf(records.get(0).get("count")); i++) {
            xVals.add(i + "");
        }

        /********* Y1 AXIS VALUES GOES RIGHT IN THIS PLACE*/
        /********* Y2 AXIS VALUES GOES RIGHT IN THIS PLACE*/
        ArrayList<Entry> yVals1 = new ArrayList<Entry>(); // Importand Variable
        ArrayList<Entry> yVals2 = new ArrayList<Entry>(); // Important Variable Y2 axis
        values = a_db.getAllHeightAndWeight(pref.getString("baby_id", null));

        for (int i = 0; i < values.size(); i++) {
            yVals1.add(new Entry(Integer.valueOf(values.get(i).get("weight")), i));
            yVals2.add(new Entry(Integer.valueOf(values.get(i).get("height")), i));
        }

        // create a dataset and give it a type
        LineDataSet set1 = new LineDataSet(yVals1, "Kilo");
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

        // create a dataset and give it a type
        LineDataSet set2 = new LineDataSet(yVals2, "Boy");
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
        mChart.invalidate();
    }
}
