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
    ArrayList<HashMap<String, String>> records,moods;
    String mood_name[]   = new String[0] , mood_time[]    = new String[0] , mood_date[]    = new String[0];
    String mood_note[]  = new String[0];
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
        intHeaderIconResourse = new int[2];
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

    private void prepareListData() {
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();

        // Adding child data
        listDataHeader.add("Top 250");
        listDataHeader.add("Now Showing");
        listDataHeader.add("Coming Soon..");
        listDataHeader.add("moods");
        // Adding child data
        List<String> top250 = new ArrayList<String>();
        top250.add("The Shawshank Redemption");
        top250.add("The Godfather");
        top250.add("The Godfather: Part II");
        top250.add("Pulp Fiction");
        top250.add("The Good, the Bad and the Ugly");
        top250.add("The Dark Knight");
        top250.add("12 Angry Men");

        List<String> nowShowing = new ArrayList<String>();
        nowShowing.add("The Conjuring");
        nowShowing.add("Despicable Me 2");
        nowShowing.add("Turbo");
        nowShowing.add("Grown Ups 2");
        nowShowing.add("Red 2");
        nowShowing.add("The Wolverine");

        List<String> comingSoon = new ArrayList<String>();
        comingSoon.add("2 Guns");
        comingSoon.add("The Smurfs 2");
        comingSoon.add("The Spectacular Now");
        comingSoon.add("The Canyons");
        comingSoon.add("Europa Report");


        //getTodayActivityRecords
        ArrayList<HashMap<String, String>> activityList;
        HashMap<String, String> a;
        List<String> moodstoday = new ArrayList<String>();

        ActivityTable s_db = new ActivityTable(getActivity().getApplicationContext()); // Db bağlantısı oluşturuyoruz. İlk seferde database oluşturulur.
        activityList = s_db.getTodayActivityRecords("Mood",baby_id,"14/05/2015", "13/05/2015"); // üç paremetre
        Log.i("sdgfsfasfd: ", String.valueOf(activityList.size()));
        if (activityList.size() != 0) {//mood listesi boşsa
            for (int i = 0; i < activityList.size(); i++) {
                a = activityList.get(i);
                Log.i("bid\t\t",a.get("b_id"));
                moodstoday.add(a.get("b_id"));
            }
        }

        listDataChild.put(listDataHeader.get(0), top250); // Header, Child data
        listDataChild.put(listDataHeader.get(1), nowShowing);
        listDataChild.put(listDataHeader.get(2), comingSoon);
        listDataChild.put(listDataHeader.get(3), moodstoday);

    }

    private void getTodaysLog(){
        intHeaderIconResourse[0] = R.drawable.ic_mood;
        intHeaderIconResourse[1] = R.drawable.ic_solid;
        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        ArrayList<HashMap<String, String>> activityList;

        //List HeaderLarı
        listDataHeader.add("Mood");
        listDataHeader.add("Solid");
//        listDataHeader.add("Bottle");
//        listDataHeader.add("Breast");
//        listDataHeader.add("Sleep");
//        listDataHeader.add("Diaper");
//        listDataHeader.add("Pumping");
//        listDataHeader.add("Health");
//        listDataHeader.add("Medicine");
//        listDataHeader.add("Vaccination");
//        listDataHeader.add("Hygiene");
//        listDataHeader.add("Teeth");

/*        ActivityTable a_db = new ActivityTable(getActivity().getApplicationContext());
        records = a_db.getTodayActivityRecords("Mood",baby_id,getCurrentDate(),getYesterdayDateString());
        if (records.size() != 0) {
            mood_time = new String[records.size()];
            mood_date = new String[records.size()];
            mood_note = new String[records.size()];
            mood_ActID= new String[records.size()];
            for (int i = 0; i < records.size(); i++) {
                mood_time[i] = records.get(i).get("select_time");
                mood_note[i] = records.get(i).get("note");
                mood_ActID[i]= records.get(i).get("a_id");
            }
        }

        if(mood_ActID.length() != 0) {
            for(int i=0;i<mood_ActID.length();i++) {
                Mood db = new Mood(getActivity().getApplicationContext()); // Db bağlantısı oluşturuyoruz. İlk seferde database oluşturulur.
                moods = db.getTodayMoods(mood_ActId[i],getCurrentDate(),getYesterdayDateString());
                if (moods.size() != 0) {//mood listesi boşsa
                    mood_name = new String[ds.size()]; // mood adlarını tutucamız string arrayi olusturduk.
                    mood_name[i] = moods.get(i).get("m_type");

                }
            }
        }*/
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
    }




//    private void getMoods(String baby_id) {
//        ArrayList<HashMap<String, String>> moods;
//        String mood_name[] = new String[0], mood_time[] = new String[0], mood_info[]= new String[0];
//        //Get values from databases for listview
//        MoodDatabase db = new MoodDatabase(getActivity().getApplicationContext()); // Db bağlantısı oluşturuyoruz. İlk seferde database oluşturulur.
//        String currentDate = getCurrentDate();
//        Log.d("CurrentDate:",currentDate);
//        moods = db.getTodaysData(currentDate,baby_id);//od listesini alıyoruz
//        if (moods.size() != 0) {//mood listesi boşsa
//            mood_name = new String[moods.size()]; // mood adlarını tutucamız string arrayi olusturduk.
//            mood_time = new String[moods.size()];
//            mood_info = new String[moods.size()];
//            for (int i = 0; i < moods.size(); i++) {
//                mood_name[i] = moods.get(i).get("mood");
//                mood_time[i] = moods.get(i).get("time");
//                mood_info[i] = moods.get(i).get("detail");
//                Log.i("Mood Name:", mood_name[i]);
//                Log.i("Mood Time:", mood_time[i]);
//                if(mood_info[i].equals(""))
//                Log.i("Mood Info:", "boş");
//                else
//                Log.i("Mood Info:", mood_info[i]);
//            }
//        }
//            count = count + moods.size();
//            adapter = new ListViewAdapterForTodayFragment(getActivity(),R.drawable.ic_mood_bullet,mood_time,mood_name,mood_info,count);
//            list.setAdapter(adapter);
//
//
//
//        Log.d("Count",String.valueOf(count));
//    }
}
