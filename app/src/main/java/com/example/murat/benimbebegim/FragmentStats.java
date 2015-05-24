package com.example.murat.benimbebegim;


import android.app.ActionBar;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.murat.benimbebegim.adapters.StatsActivityAdapter;
import com.example.murat.benimbebegim.listviewitems.BarChartItem;
import com.example.murat.benimbebegim.listviewitems.ChartItem;
import com.example.murat.benimbebegim.listviewitems.LineChartItem;
import com.example.murat.benimbebegim.listviewitems.PieChartItem;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;
import java.util.List;

public class FragmentStats extends Fragment {

    public static final String TAG = FragmentStats.class.getSimpleName();
    ListView lv;
    String classes[] = {"ActivityMoodChart",
            "ActivitySolidChart",
            "ActivityBottleChart",
            "ActivityGrowthChart"};

    public static FragmentStats newInstance() {
        return new FragmentStats();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        Log.d("Spinner", "Çok yaşa Spin4");
        View view = inflater.inflate(R.layout.layout_statsfragment, container, false);
        getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);

        int[] iconResource = new int[4];
        iconResource[0] = R.drawable.ic_mood_bullet;
        iconResource[1] = R.drawable.ic_solid_bullet;
        iconResource[2] = R.drawable.ic_bottle_bullet;
        iconResource[3] = R.drawable.icon32_chart;

        String[] iconString = new String[4];
        iconString[0] = "Mood";
        iconString[1] = "Solid";
        iconString[2] = "Bottle";
        iconString[3] = "Growth";

        String[] title = new String[4];
        title[0] = "Mood Activities Chart";
        title[1] = "Solid  Activities Chart";
        title[2] = "Bottle Activities Chart";
        title[3] = "Growth Chart";

        StatsActivityAdapter adapter = new StatsActivityAdapter(getActivity().getApplicationContext(), title, iconResource, iconString);

        lv = (ListView) view.findViewById(R.id.listView1);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                try {
                    Class ourClass = Class.forName("com.example.murat.benimbebegim." + classes[position]);
                    Intent intent = new Intent(getActivity(), ourClass);
                    startActivity(intent);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        });

        return view;
    }


}

