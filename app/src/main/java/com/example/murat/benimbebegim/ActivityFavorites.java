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

import com.example.murat.benimbebegim.Databases.ActivityTable;
import com.example.murat.benimbebegim.Databases.Mood;
import com.example.murat.benimbebegim.adapters.ListViewAdapterForFavorites;

import net.sourceforge.jtds.jdbc.DateTime;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

public class ActivityFavorites extends Fragment {
    String[] favName;
    int[] upLogo;
    ListView list;
    ListViewAdapterForFavorites adapter;
    ArrayList<HashMap<String, String>> moods;
    ArrayList<HashMap<String, String>> records;
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
            adapter = new ListViewAdapterForFavorites(getActivity(), favName, upLogo, " ", " "," ");
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

        String dateStart = r_date + " " + r_time +":00";
        Log.d("Date: ",dateStart);
        String dateStop =  saveDate + " " + saveTime +":00";
        Log.d("Date2: ",dateStop);
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

        Date d1 = null;
        Date d2 = null;

        try {
            d1 = format.parse(dateStart);
            d2 = format.parse(dateStop);

            //in milliseconds
            long diff = d2.getTime() - d1.getTime();

            //long diffSeconds = diff / 1000 % 60;
            long diffMinutes = diff / (60 * 1000) % 60;
            long diffHours = diff / (60 * 60 * 1000) % 24;
            long diffDays = diff / (24 * 60 * 60 * 1000);
            long diffYear=0;
            long diffMounth=0;

            Log.d("Day: ",diffDays + " days, ");

            if(diffDays > 365){
                diffYear = diffDays / 365 ;
                diffDays = diffDays - (diffYear * 365 );
            }
            if(diffDays > 30){
                diffMounth = diffDays / 30 ;
                diffDays   = diffDays - (diffMounth * 30);
            }
            Log.d("Year: ",diffYear +" years,");
            Log.d("Mounth: ",diffMounth +" mounth,");
            Log.d("Day: ",diffDays + " days, ");
            Log.d("Hour: ",diffHours + " hours, ");
            Log.d("Minutes: ",diffMinutes + " minutes, ");
            //Log.d("Seconds: ",diffSeconds + " seconds.");

            if( diffYear > 0) {
                result = getResources().getString(R.string.about) + " " + (diffYear + 1)+ " " + getResources().getString(R.string.year_ago);
            }else if (diffMounth == 12) {
                result = getResources().getString(R.string.about) + " 1 "  + getResources().getString(R.string.year_ago);
            }else if (diffMounth > 0){
                result = getResources().getString(R.string.about) + " " + (diffMounth)+" " + getResources().getString(R.string.mounth_ago);
            }else if (diffDays > 0){
                result = getResources().getString(R.string.about) + " " + (diffDays) + " " + getResources().getString(R.string.day_ago);
            }else if (diffHours > 0){
                result = getResources().getString(R.string.about) + " " + (diffHours)+ " " + getResources().getString(R.string.hour_ago);
            }else if (diffMinutes > 0){
                result = getResources().getString(R.string.about) + " " + (diffMinutes)+" " + getResources().getString(R.string.minute_ago);
            }else {
                result = getResources().getString(R.string.a_minute_ago);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.d("Result: ", result);
        return result;
    }

    private void getMoodListFromDatabase(String baby_id) {
        //Get values from databases for listview
        ActivityTable a_db = new ActivityTable(getActivity().getApplicationContext());
        records = a_db.showRecordForActivityType("Mood",baby_id);
        if (records.size() != 0) {
            mood_time = new String[records.size()];
            mood_date = new String[records.size()];
            for (int i = 0; i < records.size(); i++) {
                mood_time[i] = records.get(i).get("select_time");
                mood_date[i] = records.get(i).get("select_date");
                Log.i("Mood Time:", mood_time[i]);
                Log.i("Mood Date:", mood_date[i]);
            }
        }

        Mood db = new Mood(getActivity().getApplicationContext()); // Db bağlantısı oluşturuyoruz. İlk seferde database oluşturulur.
        moods = db.getAllRecords();
        if (moods.size() != 0) {//mood listesi boşsa
            mood_name = new String[moods.size()]; // mood adlarını tutucamız string arrayi olusturduk.
            for (int i = 0; i < moods.size(); i++) {
                mood_name[i] = moods.get(i).get("m_type");
                Log.i("Mood Name:", mood_name[i]);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        Toast.makeText(getActivity().getApplicationContext(),"Kontrol",Toast.LENGTH_SHORT).show();
        init();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public static void setValue(String aa, String bb){


    }
}