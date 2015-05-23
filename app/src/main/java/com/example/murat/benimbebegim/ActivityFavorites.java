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
import com.example.murat.benimbebegim.Databases.Bottle;
import com.example.murat.benimbebegim.Databases.Breast;
import com.example.murat.benimbebegim.Databases.Diaper;
import com.example.murat.benimbebegim.Databases.Mood;
import com.example.murat.benimbebegim.Databases.Pumping;
import com.example.murat.benimbebegim.Databases.Sleep;
import com.example.murat.benimbebegim.Databases.Solid;
import com.example.murat.benimbebegim.adapters.ListViewAdapterForFavorites;
import com.example.murat.benimbebegim.adapters.ListViewAdapterForMoreEvents;

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
    ArrayList<HashMap<String, String>> diapers;
    ArrayList<HashMap<String, String>> moods;
    ArrayList<HashMap<String, String>> solids;
    ArrayList<HashMap<String, String>> bottles;
    ArrayList<HashMap<String, String>> breasts;
    ArrayList<HashMap<String, String>> sleeps;
    ArrayList<HashMap<String, String>> pumpings;
    ArrayList<HashMap<String, String>> records;

    String mood_name[]   = new String[0] , mood_time[]    = new String[0] , mood_date[]    = new String[0];
    String solid_note[]  = new String[0] , solid_time[]   = new String[0] , solid_date[]   = new String[0];
    String bottle_time[] = new String[0] , bottle_date[]  = new String[0] , bottle_note[]  = new String[0];
    String breast_time[] = new String[0] , breast_date[]  = new String[0] , breast_note[]  = new String[0];
    String sleep_time[]  = new String[0] , sleep_date[]   = new String[0] , sleep_note[]   = new String[0];
    String diaper_time[] = new String[0] , diaper_date[]  = new String[0] , diaper_note[]  = new String[0];
    String pumping_time[]= new String[0] , pumping_date[] = new String[0] , pumping_note[] = new String[0];
    String list_note[]   = new String[8] , list_time[]    = new String[8] , list_ago[]     = new String[8];

    String solid_aid[]  = new String[0];
    String bottle_aid[] = new String[0];
    String breast_aid[] = new String[0];
    String sleep_aid[]  = new String[0];
    String diaper_aid[] = new String[0];
    String pumping_aid[]= new String[0];
    String mood_aid[]   = new String[0];
    String mood_note[]  = new String[0];
    int mood_id[]       = new int[0];
    public static final String PREFS_NAME = "MyPrefsFile";
    String baby_id;
    String bread,fruit,cereal,diary,pasta,eggs,meat,fish,other,vegatable;
    String bottle_type,bottle_amount,bottle_timer,breast_amount,sleep_duration,diaper_type,pumping_amount,pumping_duration;
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
            if(!mood_note[mood_note.length - 1].equals("")) {
                list_note[0] = mood_name[mood_name.length - 1] + " - " + mood_note[mood_note.length - 1];
            }
            else{
                list_note[0] = mood_name[mood_name.length - 1];
            }
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
        if(bottles.size()!=0) {
            String time  = calculateTime(bottle_date[bottle_date.length-1],bottle_time[bottle_time.length-1]);
            list_ago[2]  = time;
            list_time[2] = bottle_time[bottle_time.length-1] + " , ";
            if(!bottle_note[bottle_note.length - 1].equals("")) {
                list_note[2] = bottle_timer + " " + getResources().getString(R.string.duration) + " & " + bottle_amount + " ml " + bottle_type + " - " + bottle_note[bottle_note.length - 1];
            }else{
                list_note[2] = bottle_timer + " " + getResources().getString(R.string.duration) + " & " + bottle_amount + " ml " + bottle_type;
            }
        }
        else{
            list_note[2] = " ";
            list_time[2] = " ";
            list_ago [2] = getResources().getString(R.string.click_add);
        }
        if(breasts.size()!=0) {
            String time  = calculateTime(breast_date[breast_date.length-1],breast_time[breast_time.length-1]);
            list_ago[3]  = time;
            list_time[3] = breast_time[breast_time.length-1] + " , ";
            if(!breast_note[breast_note.length - 1].equals("")) {
                list_note[3] = breast_amount + " " + getResources().getString(R.string.duration) + " - " + breast_note[breast_note.length - 1];
            }else{
                list_note[3] = breast_amount + " " + getResources().getString(R.string.duration);
            }
        }
        else{
            list_note[3] = " ";
            list_time[3] = " ";
            list_ago [3] = getResources().getString(R.string.click_add);
        }
        if(sleeps.size()!=0) {
            String time  = calculateTime(sleep_date[sleep_date.length-1],sleep_time[sleep_time.length-1]);
            list_ago[4]  = time;
            list_time[4] = sleep_time[sleep_time.length-1] + " , ";
            if(!sleep_note[sleep_note.length - 1].equals("")) {
                list_note[4] = sleep_duration + " " + getResources().getString(R.string.duration) + " - " + sleep_note[sleep_note.length - 1];
            }else{
                list_note[4] = sleep_duration + " " + getResources().getString(R.string.duration);
            }
        }
        else{
            list_note[4] = " ";
            list_time[4] = " ";
            list_ago [4] = getResources().getString(R.string.click_add);
        }
        if(diapers.size()!=0) {
            String time  = calculateTime(diaper_date[diaper_date.length-1],diaper_time[diaper_time.length-1]);
            list_ago[5]  = time;
            list_time[5] = diaper_time[diaper_time.length-1] + " , ";
            if(!diaper_note[diaper_note.length-1].equals("")) {
                list_note[5] = diaper_type + " - " + diaper_note[diaper_note.length - 1];
            }else{
                list_note[5] = diaper_type;
            }
        }
        else{
            list_note[5] = " ";
            list_time[5] = " ";
            list_ago [5] = getResources().getString(R.string.click_add);
        }
        if(pumpings.size()!=0) {
            String time  = calculateTime(pumping_date[pumping_date.length-1],pumping_time[pumping_time.length-1]);
            list_ago[6]  = time;
            list_time[6] = pumping_time[pumping_time.length-1] + " , ";
            if(!pumping_note[pumping_note.length-1].equals("")) {
                list_note[6] = pumping_duration + " " + getResources().getString(R.string.duration) + " & " + pumping_amount + " ml" + " - " + pumping_note[pumping_note.length - 1];
            }else{
                list_note[6] = pumping_duration + " " + getResources().getString(R.string.duration) + " & " + pumping_amount + " ml";
            }
        }
        else{
            list_note[6] = " ";
            list_time[6] = " ";
            list_ago [6] = getResources().getString(R.string.click_add);
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
            mood_note = new String[records.size()];
            mood_aid  = new String[records.size()];
            for (int i = 0; i < records.size(); i++) {
                mood_time[i] = records.get(i).get("select_time");
                mood_date[i] = records.get(i).get("select_date");
                mood_note[i] = records.get(i).get("note");
                mood_aid[i]  = records.get(i).get("a_id");
            }
        }

        records = a_db.showRecordForActivityType("Solid",baby_id);
        if (records.size() != 0) {
            solid_time = new String[records.size()];
            solid_date = new String[records.size()];
            solid_note = new String[records.size()];
            solid_aid= new String[records.size()];
            for (int i = 0; i < records.size(); i++) {
                solid_time[i] = records.get(i).get("select_time");
                solid_date[i] = records.get(i).get("select_date");
                solid_note[i] = records.get(i).get("note");
                solid_aid[i]= records.get(i).get("a_id");
            }
        }

        records = a_db.showRecordForActivityType("Bottle",baby_id);
        if (records.size() != 0) {
            bottle_time = new String[records.size()];
            bottle_date = new String[records.size()];
            bottle_aid  = new String[records.size()];
            bottle_note = new String[records.size()];
            for (int i = 0; i < records.size(); i++) {
                bottle_time[i] = records.get(i).get("select_time");
                bottle_date[i] = records.get(i).get("select_date");
                bottle_aid [i] = records.get(i).get("a_id");
                bottle_note[i] = records.get(i).get("note");
            }
        }

        records = a_db.showRecordForActivityType("Breast",baby_id);
        if (records.size() != 0) {
            breast_time = new String[records.size()];
            breast_date = new String[records.size()];
            breast_aid  = new String[records.size()];
            breast_note = new String[records.size()];
            for (int i = 0; i < records.size(); i++) {
                breast_time[i] = records.get(i).get("select_time");
                breast_date[i] = records.get(i).get("select_date");
                breast_aid[i]  = records.get(i).get("a_id");
                breast_note[i] = records.get(i).get("note");
            }
        }

        records = a_db.showRecordForActivityType("Sleep",baby_id);
        if (records.size() != 0) {
            sleep_time = new String[records.size()];
            sleep_date = new String[records.size()];
            sleep_aid  = new String[records.size()];
            sleep_note = new String[records.size()];
            for (int i = 0; i < records.size(); i++) {
                sleep_time[i] = records.get(i).get("select_time");
                sleep_date[i] = records.get(i).get("select_date");
                sleep_aid[i]  = records.get(i).get("a_id");
                sleep_note[i] = records.get(i).get("note");
            }
        }

        records = a_db.showRecordForActivityType("Diaper",baby_id);
        if (records.size() != 0) {
            diaper_time = new String[records.size()];
            diaper_date = new String[records.size()];
            diaper_aid  = new String[records.size()];
            diaper_note = new String[records.size()];
            for (int i = 0; i < records.size(); i++) {
                diaper_time[i] = records.get(i).get("select_time");
                diaper_date[i] = records.get(i).get("select_date");
                diaper_aid[i]  = records.get(i).get("a_id");
                diaper_note[i] = records.get(i).get("note");
            }
        }

        records = a_db.showRecordForActivityType("Pumping",baby_id);
        if (records.size() != 0) {
            pumping_time = new String[records.size()];
            pumping_date = new String[records.size()];
            pumping_aid  = new String[records.size()];
            pumping_note = new String[records.size()];
            for (int i = 0; i < records.size(); i++) {
                pumping_time[i] = records.get(i).get("select_time");
                pumping_date[i] = records.get(i).get("select_date");
                pumping_aid[i]  = records.get(i).get("a_id");
                pumping_note[i] = records.get(i).get("note");
            }
        }
        if(mood_aid.length>0) {
            Mood db = new Mood(getActivity().getApplicationContext()); // Db bağlantısı oluşturuyoruz. İlk seferde database oluşturulur.
            moods = db.getAllMoods();
            if (moods.size() != 0) {//mood listesi boşsa
                mood_name = new String[moods.size()]; // mood adlarını tutucamız string arrayi olusturduk.
                for (int i = 0; i < moods.size(); i++) {
                    mood_name[i] = moods.get(i).get("m_type");
                    Log.i("Mood Name:", mood_name[i]);
                }
            }
        }
        else{
            moods = new ArrayList<HashMap<String, String>>();
        }
        if(solid_aid.length>0) {
            Solid s_db = new Solid(getActivity().getApplicationContext()); // Db bağlantısı oluşturuyoruz. İlk seferde database oluşturulur.
            solids = s_db.getSpecificSolidAsaActId(solid_aid[solid_aid.length - 1]);
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


        if(bottle_aid.length>0) {
            Bottle bottle_db = new Bottle(getActivity().getApplicationContext()); // Db bağlantısı oluşturuyoruz. İlk seferde database oluşturulur.
            bottles = bottle_db.getSpecificBottle(bottle_aid[bottle_aid.length - 1]);
            Log.e("Bottles: ", bottles.toString());
            if (bottles.size() != 0) {//mood listesi boşsa
                for (int i = 0; i < bottles.size(); i++) {
                    bottle_type   = bottles.get(i).get(ActivityTable.BOTTLE_FORMULA);
                    bottle_amount = bottles.get(i).get(ActivityTable.BOTTLE_AMOUNT);
                    bottle_timer  = bottles.get(i).get(ActivityTable.BOTTLE_TIMER);
                }
            }
        }
        else{
            bottles = new ArrayList<HashMap<String, String>>();
        }

        if(breast_aid.length>0) {
            Breast breast_db = new Breast(getActivity().getApplicationContext()); // Db bağlantısı oluşturuyoruz. İlk seferde database oluşturulur.
            breasts = breast_db.getSpecificBreast(breast_aid[breast_aid.length - 1]);
            if (breasts.size() != 0) {//mood listesi boşsa
                for (int i = 0; i < breasts.size(); i++) {
                    breast_amount = breasts.get(i).get(ActivityTable.BREAST_TIME);
                }
            }
        }
        else{
            breasts = new ArrayList<HashMap<String, String>>();
        }

        if(sleep_aid.length>0) {
            Sleep sleep_db = new Sleep(getActivity().getApplicationContext()); // Db bağlantısı oluşturuyoruz. İlk seferde database oluşturulur.
            sleeps = sleep_db.getSpecificSleepRecordWıthAID(sleep_aid[sleep_aid.length - 1]);
            if (sleeps.size() != 0) {//mood listesi boşsa
                for (int i = 0; i < sleeps.size(); i++) {
                    sleep_duration = sleeps.get(i).get(ActivityTable.SLEEP_TIME);
                }
            }
        }
        else{
            sleeps = new ArrayList<HashMap<String, String>>();
        }

        if(diaper_aid.length>0) {
            Diaper diaper_db = new Diaper(getActivity().getApplicationContext()); // Db bağlantısı oluşturuyoruz. İlk seferde database oluşturulur.
            diapers = diaper_db.getSpecificDiaper(diaper_aid[diaper_aid.length - 1]);
            if (diapers.size() != 0) {//mood listesi boşsa
                for (int i = 0; i < diapers.size(); i++) {
                    diaper_type = diapers.get(i).get(ActivityTable.DIAPER_TYPE);
                }
            }
        }
        else{
            diapers = new ArrayList<HashMap<String, String>>();
        }

        if(pumping_aid.length>0) {
            Pumping diaper_db = new Pumping(getActivity().getApplicationContext()); // Db bağlantısı oluşturuyoruz. İlk seferde database oluşturulur.
            pumpings = diaper_db.getSpecificPumpingWithAID(pumping_aid[pumping_aid.length - 1]);
            if (pumpings.size() != 0) {//mood listesi boşsa
                for (int i = 0; i < pumpings.size(); i++) {
                    pumping_duration = pumpings.get(i).get(ActivityTable.PUMPING_TIME);
                    pumping_amount   = pumpings.get(i).get(ActivityTable.PUMPING_AMOUNT);
                }
            }
        }
        else{
            pumpings = new ArrayList<HashMap<String, String>>();
        }
    }

    private void fillSolidInfo(String bread, String fruit, String cereal, String meat, String diary, String pasta, String eggs, String vegatable, String fish, String other, String note) {
        String solid_message = "";
        if(bread.equals("True")){
            solid_message = solid_message + "Bread";
        }
        if(fruit.equals("True")){
            if(solid_message != "") {
                solid_message = solid_message + ",Fruit";
            }else{
                solid_message = solid_message + "Fruit";
            }
        }
        if(cereal.equals("True")){
            if(solid_message != "") {
                solid_message = solid_message + ", Cereal";
            }else {
                solid_message = solid_message + "Cereal";
            }
        }
        if(meat.equals("True")){
            if(solid_message != "") {
                solid_message = solid_message + ", Meat";
            }else {
                solid_message = solid_message + "Meat";
            }

        }
        if(diary.equals("True")){
            if(solid_message != "") {
                solid_message = solid_message + ", Dairy";
            }else {
                solid_message = solid_message + "Dairy";
            }
        }
        if(pasta.equals("True")){
            if(solid_message != "") {
                solid_message = solid_message + ", Pasta";
            }else {
                solid_message = solid_message + "Pasta";
            }
        }
        if(eggs.equals("True")){
            if(solid_message != "") {
                solid_message = solid_message + ", Egg";
            }else {
                solid_message = solid_message + "Egg";
            }
        }
        if(vegatable.equals("True")){
            if(solid_message != "") {
                solid_message = solid_message + ", Vegetable";
            }else {
                solid_message = solid_message + "Vegetable";
            }
        }
        if(fish.equals("True")){
            if(solid_message != "") {
                solid_message = solid_message + ", Fish";
            }else {
                solid_message = solid_message + "Fish";
            }
        }
        if(other.equals("True")){
            if(solid_message != "") {
                solid_message = solid_message + ", Other";
            }else {
                solid_message = solid_message + "Other";
            }
        }
        if(!note.equals("")) {
            solid_message = solid_message + " - " + note;
        }
        Log.i("Solid_Message: ", solid_message);
        list_note[1]=solid_message;
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