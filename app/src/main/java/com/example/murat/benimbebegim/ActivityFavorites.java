package com.example.murat.benimbebegim;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.murat.benimbebegim.Databases.MoodDatabase;
import com.example.murat.benimbebegim.adapters.ListViewAdapterForFavorites;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

public class ActivityFavorites extends Fragment {
    String[] favName;
    int[] upLogo;
    ListView list;
    ListViewAdapterForFavorites adapter;
    ArrayList<HashMap<String, String>> moods;
    String mood_name[], mood_time[], mood_date[];
    public static final String PREFS_NAME = "MyPrefsFile";
    String baby_id;
    int mood_id[];

    private static String a;

    String classes[] = {"ActivityMood",
            "ActivitySolid",
            "ActivityBottle",
            "ActivityBreast",
            "ActivitySleep",
            "ActivityDiaper",
            "ActivityPumping"};

    LayoutInflater inf;
    ViewGroup cntr;
    Bundle bndl;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.activity_favorites, container, false);
        // Generate sample data
        favName = getResources().getStringArray(R.array.Favorites);

        upLogo = new int[]{R.drawable.ic_mood_bullet, R.drawable.ic_solid_bullet, R.drawable.ic_bottle_bullet,
                R.drawable.ic_breastfeed_bullet, R.drawable.ic_sleep_bullet, R.drawable.ic_diaper_bullet, R.drawable.ic_pumping_milk_bullet};

        // Locate the ListView in fragmenttab1.xml
        list = (ListView) rootView.findViewById(R.id.listFavorites);

        //Get babyid from SP
        SharedPreferences pref;
        pref = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        baby_id = pref.getString("baby_id", null);
        init();
            // Capture clicks on ListView items
            return rootView;
    }

    private void init(){
        getMoodListFromDatabase(baby_id);

        if(moods.size()!=0) {
            // Pass results to ListViewAdapter Class
            String click_add = calculateTime(mood_date[mood_date.length-1],mood_time[mood_time.length-1]);
            adapter = new ListViewAdapterForFavorites(getActivity(), favName, upLogo, mood_time[mood_time.length - 1], mood_name[mood_name.length - 1],click_add);
        }
        else{
            adapter = new ListViewAdapterForFavorites(getActivity(), favName, upLogo, " ", " "," ListViewAdapterForMoreEvents");
        }
        // Binds the Adapter to the ListView
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                try {
                    Class ourClass = Class.forName("com.example.murat.benimbebegim." + classes[position]);
                    Intent intent = new Intent(getActivity(), ourClass);
                    startActivity(intent);
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Toast.makeText(getActivity(), classes[position] + " aktivitesi yok", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private String calculateTime(String r_date, String r_time) {
        String year,mounth,day,hour,minute;
        String c_year,c_mounth,c_day,c_hour,c_minute;
        String result="";
        Log.d("r_date",r_date);
        Log.d("r_time",r_time);
        // Split values from getting Database
        day = r_date.substring(0,2);
        mounth = r_date.substring(3,5);
        year = r_date.substring(6,10);
        hour = r_time.substring(0,2);
        minute = r_time.substring(3,5);

        //Split Current Values
        Calendar c_date = Calendar.getInstance();
        SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
        String saveDate = date.format(c_date.getTime());
        Log.d("C_Date",saveDate);
        c_year = saveDate.substring(6,10);
        c_mounth = saveDate.substring(3,5);
        c_day = saveDate.substring(0,2);
        Calendar c_time = Calendar.getInstance(TimeZone.getDefault());
        SimpleDateFormat time = new SimpleDateFormat("HH:mm",
                Locale.getDefault());
        String saveTime = time.format(c_time.getTime());
        Log.d("C_Time",saveTime);
        c_hour = saveTime.substring(0,2);
        c_minute = saveTime.substring(3,5);

        if(year == c_year){
            result ="";
        }else if(Integer.parseInt(year) < Integer.parseInt(c_year))
        {
            int yearr = Integer.parseInt(c_year) - Integer.parseInt(year);
            result = getResources().getString(R.string.about) + " " + String.valueOf(yearr) + " " +getResources().getString(R.string.year_ago);
        }else if(mounth == c_mounth){
            result="";
        }else if(Integer.parseInt(mounth) < Integer.parseInt(c_mounth)){
            result = getResources().getString(R.string.about) +  " "
                     + String.valueOf((Integer.parseInt(c_mounth) - Integer.parseInt(mounth)))
                     + " " +getResources().getString(R.string.mounth_ago);
        }else if(day == c_day){
            result = "";
        }else if(Integer.parseInt(day) < Integer.parseInt(c_day)){
            result = getResources().getString(R.string.about) + " "
                     + String.valueOf((Integer.parseInt(c_day) - Integer.parseInt(day))) + " "
                     + getResources().getString(R.string.day_ago);
        }else if(hour == c_hour){
            result = "";
        }else if(Integer.parseInt(hour) < Integer.parseInt(c_hour)){
            result = getResources().getString(R.string.about) + " "
                     + String.valueOf((Integer.parseInt(c_hour) - Integer.parseInt(hour))) + " "
                     + getResources().getString(R.string.hour_ago);
        }else if(minute == c_minute){
            result ="";
        }else if(Integer.parseInt(minute) < Integer.parseInt(c_minute)){
            result = getResources().getString(R.string.about) + " "
                     + String.valueOf((Integer.parseInt(c_minute) - Integer.parseInt(minute))) + " "
                     + getResources().getString(R.string.minute_ago);
        }
        return result;
    }

    private void getMoodListFromDatabase(String baby_id) {
        //Get values from databases for listview
        MoodDatabase db = new MoodDatabase(getActivity().getApplicationContext()); // Db bağlantısı oluşturuyoruz. İlk seferde database oluşturulur.
        moods = db.moods(baby_id);//mood listesini alıyoruz
        if (moods.size() != 0) {//mood listesi boşsa
            mood_name = new String[moods.size()]; // mood adlarını tutucamız string arrayi olusturduk.
            mood_id = new int[moods.size()]; // mood id lerini tutucamız string arrayi olusturduk.
            mood_time = new String[moods.size()];
            mood_date = new String[moods.size()];
            for (int i = 0; i < moods.size(); i++) {
                mood_name[i] = moods.get(i).get("mood");
                mood_id[i] = Integer.parseInt(moods.get(i).get("m_id"));
                mood_time[i] = moods.get(i).get("time");
                mood_date[i] = moods.get(i).get("date");
                Log.i("Mood Name:", mood_name[i]);
                Log.i("Mood Id  :", String.valueOf(mood_id[i]));
                Log.i("Mood Time:", mood_time[i]);
                Log.i("Mood Date:", mood_date[i]);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        init();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public static void setValue(String aa, String bb){


    }
}