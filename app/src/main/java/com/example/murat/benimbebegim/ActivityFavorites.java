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
import com.example.murat.benimbebegim.Databases.Solid;
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
    ArrayList<HashMap<String, String>> moods,solids;
    ArrayList<HashMap<String, String>> records;
    String mood_name[] = new String[0] , mood_time[] = new String[0] , mood_date[] = new String[0];
    String solid_note[] = new String[0] , solid_time[] = new String[0], solid_date[] = new String[0];
    String list_note[] = new String[8] ,list_time[] = new String[8] ,list_ago[] = new String[8];
    String activity_id[] = new String[0];
    public static final String PREFS_NAME = "MyPrefsFile";
    String baby_id;
    int mood_id[];
    String bread,fruit,cereal,diary,pasta,eggs,meat,fish,other,vegatable;

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

        fillToListViewAdapter();
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

    private void fillToListViewAdapter() {
        if(moods.size()!=0) {
            list_note[0] = mood_name[mood_name.length-1];
            String time  = calculateTime(mood_date[mood_date.length-1],mood_time[mood_time.length-1]);
            list_ago[0]  = time;
            list_time[0] = mood_time[mood_time.length-1] + " , ";
        }
        else{
            list_note[0] = " ";
            list_time[0] = " ";
            list_ago [0] = getResources().getString(R.string.click_add);
        }
        if(solids.size()!=0) {
            String time  = calculateTime(solid_date[solid_date.length-1],solid_time[solid_time.length-1]);
            list_ago[1]  = time;
            list_time[1] = solid_time[solid_time.length-1] + " , ";
        }
        else{
            list_note[1] = " ";
            list_time[1] = " ";
            list_ago [1] = getResources().getString(R.string.click_add);
        }
        adapter = new ListViewAdapterForFavorites(getActivity(), favName, upLogo, list_time, list_note,list_ago);
    }

    private String calculateTime(String r_date, String r_time) {
        String result="";

        Calendar c_date = Calendar.getInstance();
        SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
        String currentDate = date.format(c_date.getTime());

        Calendar c_time = Calendar.getInstance(TimeZone.getDefault());
        SimpleDateFormat time = new SimpleDateFormat("HH:mm",
                Locale.getDefault());
        String currentTime = time.format(c_time.getTime());

        String dateStart = r_date + " " + r_time +":00";
        Log.d("Date: ",dateStart);
        String dateStop =  currentDate + " " + currentTime +":00";
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
        Log.d("Baby ID Son: " , baby_id);
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

        records = a_db.showRecordForActivityType("Solid",baby_id);
        if (records.size() != 0) {
            solid_time = new String[records.size()];
            solid_date = new String[records.size()];
            solid_note = new String[records.size()];
            activity_id= new String[records.size()];
            for (int i = 0; i < records.size(); i++) {
                solid_time[i] = records.get(i).get("select_time");
                solid_date[i] = records.get(i).get("select_date");
                solid_note[i] = records.get(i).get("note");
                activity_id[i]= records.get(i).get("a_id");
                Log.i("Solid Time:", solid_time[i]);
                Log.i("Solid Date:", solid_date[i]);
                if(!solid_note[i].equals(""))
                Log.i("Solid Note:", solid_note[i]);
                Log.i("Activity ID:",activity_id[i]);
            }
        }

        Mood db = new Mood(getActivity().getApplicationContext()); // Db bağlantısı oluşturuyoruz. İlk seferde database oluşturulur.
        moods = db.getAllMoods();
        if (moods.size() != 0) {//mood listesi boşsa
            mood_name = new String[moods.size()]; // mood adlarını tutucamız string arrayi olusturduk.
            for (int i = 0; i < moods.size(); i++) {
                mood_name[i] = moods.get(i).get("m_type");
                Log.i("Mood Name:", mood_name[i]);
            }
        }
        if(activity_id.length>0) {
            Solid s_db = new Solid(getActivity().getApplicationContext()); // Db bağlantısı oluşturuyoruz. İlk seferde database oluşturulur.
            solids = s_db.getSpecificSolidAsaActId(activity_id[activity_id.length - 1]);
            Log.i("Solids: ", solids.toString());
            if (solids.size() != 0) {//mood listesi boşsa
                for (int i = 0; i < solids.size(); i++) {
                    bread = solids.get(i).get(ActivityTable.SOLID_BREAD);
                    fruit = solids.get(i).get(ActivityTable.SOLID_FRUIT);
                    cereal = solids.get(i).get(ActivityTable.SOLID_CEREAL);
                    meat = solids.get(i).get(ActivityTable.SOLID_MEAT);
                    diary = solids.get(i).get(ActivityTable.SOLID_DAIRY);
                    pasta = solids.get(i).get(ActivityTable.SOLID_PASTA);
                    eggs = solids.get(i).get(ActivityTable.SOLID_EGGS);
                    vegatable = solids.get(i).get(ActivityTable.SOLID_VEGETABLE);
                    fish = solids.get(i).get(ActivityTable.SOLID_FISH);
                    other = solids.get(i).get(ActivityTable.SOLID_OTHER);
                    fillSolidInfo(bread, fruit, cereal, meat, diary, pasta, eggs, vegatable, fish, other, solid_note[solid_note.length - 1]);
                }
            }
        }
        else{
            solids = new ArrayList<HashMap<String, String>>();
        }
    }

    private void fillSolidInfo(String bread, String fruit, String cereal, String meat, String diary, String pasta, String eggs, String vegatable, String fish, String other, String note) {
        String solid_message = "";
        if(bread.equals("True")){
            solid_message = solid_message + "Bread";
        }
        if(fruit.equals("True")){
            solid_message = solid_message + ",Fruit";
        }
        if(cereal.equals("True")){
            solid_message = solid_message + ",Cereal";
        }
        if(meat.equals("True")){
            solid_message = solid_message + ",Meat";
        }
        if(diary.equals("True")){
            solid_message = solid_message + ",Dairy";
        }
        if(pasta.equals("True")){
            solid_message = solid_message + ",Pasta";
        }
        if(eggs.equals("True")){
            solid_message = solid_message + ",Egg";
        }
        if(vegatable.equals("True")){
            solid_message = solid_message + ",Vegetable";
        }
        if(fish.equals("True")){
            solid_message = solid_message + ",Fish";
        }
        if(other.equals("True")){
            solid_message = solid_message + ",Other";
        }
        if(!note.equals("")) {
            solid_message = solid_message + " " + note;
        }
        Log.i("Solid_Message: ", solid_message);
        list_note[1]=solid_message;
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