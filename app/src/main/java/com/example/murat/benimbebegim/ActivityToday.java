package com.example.murat.benimbebegim;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.murat.benimbebegim.Databases.ActivityTable;
import com.example.murat.benimbebegim.Databases.Mood;
import com.example.murat.benimbebegim.adapters.ListViewAdapterForTodayFragment;
import com.example.murat.benimbebegim.adapters.TodayEXPListViewAdapter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;


public class ActivityToday extends Fragment{

    public static Context myContext;
    public static final String TAG = ActivityToday.class.getSimpleName();
    public static ActivityToday newInstance() {
        return new ActivityToday();
    }

    TodayEXPListViewAdapter listAdapter;
    ExpandableListView expListView;
    public static List<String> listDataHeader;
    int[] intHeaderIconResourse;
    public static HashMap<String, List<String>> listDataChild;

    String baby_id;
    int count=0;
    public static final String PREFS_NAME = "MyPrefsFile";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.activity_today, container,false);
        SharedPreferences pref;
        pref = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        baby_id = pref.getString("baby_id", null);
        this.myContext = getActivity().getApplicationContext();
        //**********************************************************************************//
        expListView = (ExpandableListView)view.findViewById(R.id.elvTodayActivities);
        //prepareListData();
        intHeaderIconResourse = new int[7];
        getTodaysLog();
        listAdapter = new TodayEXPListViewAdapter(getActivity().getApplicationContext(), listDataHeader, listDataChild, intHeaderIconResourse);
        expListView.setAdapter(listAdapter);
        expListView.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {

            @Override
            public boolean onGroupClick(ExpandableListView parent, View v,
                                        int groupPosition, long id) {
                // Toast.makeText(getApplicationContext(),
                // "Group Clicked " + listDataHeader.get(groupPosition),
                // Toast.LENGTH_SHORT).show();
                return false;
            }
        });

        // Listview Group expanded listener
        expListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {
                Toast.makeText(getActivity().getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Expanded",
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
                Toast.makeText(getActivity().getApplicationContext(),
                        listDataHeader.get(groupPosition) + " Collapsed",
                        Toast.LENGTH_SHORT).show();

            }
        });

        // Listview on child click listener
        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {

            @Override
            public boolean onChildClick(ExpandableListView parent, View v,
                                        int groupPosition, int childPosition, long id) {
                // TODO Auto-generated method stub
                Toast.makeText(
                        getActivity().getApplicationContext(),
                        listDataHeader.get(groupPosition)
                                + " : "
                                + listDataChild.get(
                                listDataHeader.get(groupPosition)).get(
                                childPosition), Toast.LENGTH_SHORT)
                        .show();
                return false;
            }
        });




        //**********************************************************************************//
        return view;
    }
    private String getCurrentDate() {
        Calendar c_date = Calendar.getInstance();
        SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
        String saveDate = date.format(c_date.getTime());
        return saveDate;
    }

    private String getYesterdayDateString(){

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1);
        return dateFormat.format(cal.getTime());
    }

    private void getTodaysLog(){
        intHeaderIconResourse[0] = R.drawable.ic_mood_bullet;
        intHeaderIconResourse[1] = R.drawable.ic_solid_bullet;
        intHeaderIconResourse[2] = R.drawable.ic_bottle_bullet;
        intHeaderIconResourse[3] = R.drawable.ic_breastfeed_bullet;
        intHeaderIconResourse[4] = R.drawable.ic_sleep_bullet;
        intHeaderIconResourse[5] = R.drawable.ic_diaper_bullet;
        intHeaderIconResourse[6] = R.drawable.ic_pumping_milk_bullet;
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        ArrayList<HashMap<String, String>> activityList;

        //List HeaderLarı
        listDataHeader.add("Mood");
        listDataHeader.add("Solid");
        listDataHeader.add("Bottle");
        listDataHeader.add("Breast");
        listDataHeader.add("Sleep");
        listDataHeader.add("Diaper");
        listDataHeader.add("Pumping");
//        listDataHeader.add("Health");
//        listDataHeader.add("Medicine");
//        listDataHeader.add("Vaccination");
//        listDataHeader.add("Hygiene");
//        listDataHeader.add("Teeth");



        ActivityTable s_db = new ActivityTable(getActivity().getApplicationContext()); // Db bağlantısı oluşturuyoruz. İlk seferde database oluşturulur.
        /*********************************************/
        activityList = s_db.getTodayActivityRecords("Mood",baby_id,getCurrentDate(),getYesterdayDateString()); // üç paremetre
        List<String> moods = new ArrayList<String>();
        if (activityList.size() != 0) {//mood listesi boşsa
            for (int i = 0; i < activityList.size(); i++) {
                HashMap<String, String> a = activityList.get(i);
                moods.add(a.get("a_id"));
            }
            listDataChild.put(listDataHeader.get(0), moods);
        }
        /*********************************************/
        activityList = s_db.getTodayActivityRecords("Solid",baby_id,getCurrentDate(),getYesterdayDateString()); // üç paremetre
        List<String> solids = new ArrayList<String>();
        if (activityList.size() != 0) {//mood listesi boşsa
            for (int i = 0; i < activityList.size(); i++) {
                HashMap<String, String> a = activityList.get(i);
                solids.add(a.get("a_id"));
            }
            listDataChild.put(listDataHeader.get(1), solids);
        }
        /*********************************************/
        activityList = s_db.getTodayActivityRecords("Bottle", baby_id, getCurrentDate(), getYesterdayDateString());
        List<String> bottle = new ArrayList<String>();
        if (activityList.size() != 0) {
            for (int i = 0; i < activityList.size(); i++) {
                HashMap<String, String> a = activityList.get(i);
                bottle.add(a.get("a_id"));
            }
            listDataChild.put(listDataHeader.get(2), bottle);
        }
        /*********************************************/ // ----------------
        activityList = s_db.getTodayActivityRecords("Breast", baby_id, getCurrentDate(), getYesterdayDateString());
        List<String> breast = new ArrayList<String>();
        if (activityList.size() != 0) {
            for (int i = 0; i < activityList.size(); i++) {
                HashMap<String, String> a = activityList.get(i);
                breast.add(a.get("a_id"));
            }
            listDataChild.put(listDataHeader.get(3), breast);
        }
        /*********************************************/
        activityList = s_db.getTodayActivityRecords("Sleep", baby_id, getCurrentDate(), getYesterdayDateString());
        List<String> sleep = new ArrayList<String>();
        if (activityList.size() != 0) {
            for (int i = 0; i < activityList.size(); i++) {
                HashMap<String, String> a = activityList.get(i);
                sleep.add(a.get("a_id"));
            }
            listDataChild.put(listDataHeader.get(4), bottle);
        }
        /*********************************************/
        activityList = s_db.getTodayActivityRecords("Diaper", baby_id, getCurrentDate(), getYesterdayDateString());
        List<String> diaper = new ArrayList<String>();
        if (activityList.size() != 0) {
            for (int i = 0; i < activityList.size(); i++) {
                HashMap<String, String> a = activityList.get(i);
                diaper.add(a.get("a_id"));
            }
            listDataChild.put(listDataHeader.get(5), bottle);
        }
        /*********************************************/
        activityList = s_db.getTodayActivityRecords("Pumping", baby_id, getCurrentDate(), getYesterdayDateString());
        List<String> pumping = new ArrayList<String>();
        if (activityList.size() != 0) {
            for (int i = 0; i < activityList.size(); i++) {
                HashMap<String, String> a = activityList.get(i);
                pumping.add(a.get("a_id"));
            }
            listDataChild.put(listDataHeader.get(6), bottle);
        }
    }
}
