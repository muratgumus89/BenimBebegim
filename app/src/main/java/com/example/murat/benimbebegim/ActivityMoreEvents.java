package com.example.murat.benimbebegim;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.murat.benimbebegim.Databases.ActivityTable;
import com.example.murat.benimbebegim.Databases.Health;
import com.example.murat.benimbebegim.Databases.Hygiene;
import com.example.murat.benimbebegim.Databases.Medicine;
import com.example.murat.benimbebegim.Databases.Pumping;
import com.example.murat.benimbebegim.Databases.Teeth;
import com.example.murat.benimbebegim.Databases.Vaccination;
import com.example.murat.benimbebegim.adapters.ListViewAdapterForFavorites;
import com.example.murat.benimbebegim.adapters.ListViewAdapterForMoreEvents;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;

public class ActivityMoreEvents extends Fragment {
    String[] favName;
    int[] upLogo;
    ListView list;
    ListViewAdapterForMoreEvents adapter;
    ArrayList<HashMap<String, String>> healths,medicines,vaccinations,hygienes,teeths;
    ArrayList<HashMap<String, String>> records;
    String list_note[]  = new String[5] ,list_time[]   = new String[5] ,list_ago[]  = new String[5];

    String health_time[] = new String[0] , health_date[]  = new String[0];
    String medicin_time[]=new String[0]  , medicin_date[] =new String[0];
    String vac_time[]    = new String[0] , vac_date[]     = new String[0];
    String hygiene_time[]= new String[0] , hygiene_date[] = new String[0];
    String teeth_time[]  = new String[0] , teeth_date[]   = new String[0];
    String health_aid[]  = new String[0] , health_note[]  = new String[0];
    String medicin_aid[] = new String[0] , medicin_note[] = new String[0];
    String vac_aid[]     = new String[0] , vac_note[]     = new String[0];
    String hygiene_aid[] = new String[0] , hygiene_note[] = new String[0];
    String teeth_aid[]   = new String[0] , teeth_note[]   = new String[0];

    public static final String PREFS_NAME = "MyPrefsFile";
    String baby_id;

    String health_type , health_temp,medicine_type,medicine_dose,medicine_dose_type,vaccination_type
            ,hygiene_type,teeth_type;

    String classes[] = {"ActivityHealth",
            "ActivityMedicine",
            "ActivityVaccination",
            "ActivityHygiene",
            "ActivityTeeth"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_moreevents, container,
                false);
        // Generate sample data
        favName = getResources().getStringArray(R.array.MoreEvents);


        upLogo = new int[] {R.drawable.ic_health_bullet,R.drawable.ic_medicine_bullet,R.drawable.ic_vaccination_bullet,
                R.drawable.ic_hygiene_bullet,R.drawable.ic_teeth_bullet};

        // Locate the ListView in fragmenttab1.xml
        list = (ListView) rootView.findViewById(R.id.listMoreEvents);

        //Get babyid from SP
        SharedPreferences pref;
        pref = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        baby_id = pref.getString("baby_id", null);
        init();
        return rootView;
    }

    private void init() {
        getRecordsFromDatabase(baby_id);
        fillToListViewAdapter();
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
    private void fillToListViewAdapter() {
        if(healths.size()!=0) {
            if(!health_note[health_note.length-1].equals("")) {
                list_note[0] = health_type + " " +  health_temp + " °C" + " - " + health_note[health_note.length - 1];
            }else{
                list_note[0] = health_type + " " +  health_temp + " °C";
            }
            String time  = calculateTime(health_date[health_date.length-1],health_time[health_time.length-1]);
            list_ago[0]  = time;
            list_time[0] = health_time[health_time.length-1] + " , ";
        }
        else{
            list_note[0] = " ";
            list_time[0] = " ";
            list_ago [0] = getResources().getString(R.string.click_add);
        }
        if(medicines.size()!=0) {
            if(!medicin_note[medicin_note.length-1].equals("")) {
                list_note[1] = medicine_type +", " + medicine_dose + " " +
                        medicine_dose_type + " - " + medicin_note[medicin_note.length - 1];
            }else{
                list_note[1] = medicine_type +", " + medicine_dose + " " +
                        medicine_dose_type;
            }
            String time  = calculateTime(medicin_date[medicin_date.length-1],medicin_time[medicin_time.length-1]);
            list_ago[1]  = time;
            list_time[1] = medicin_time[medicin_time.length-1] + " , ";
        }
        else{
            list_note[1] = " ";
            list_time[1] = " ";
            list_ago [1] = getResources().getString(R.string.click_add);
        }
        if(vaccinations.size()!=0) {
            if(!vac_note[vac_note.length-1].equals("")) {
                list_note[2] = vaccination_type + " - " + vac_note[vac_note.length - 1];
            }else{
                list_note[2] = vaccination_type;
            }
            String time  = calculateTime(vac_date[vac_date.length-1],vac_time[vac_time.length-1]);
            list_ago[2]  = time;
            list_time[2] = vac_time[vac_time.length-1] + " , ";
        }
        else{
            list_note[2] = " ";
            list_time[2] = " ";
            list_ago [2] = getResources().getString(R.string.click_add);
        }

        if(hygienes.size()!=0) {
            if(!hygiene_note[hygiene_note.length-1].equals("")) {
                list_note[3] = hygiene_type + " - " + hygiene_note[hygiene_note.length - 1];
            }else{
                list_note[3] = hygiene_type;
            }
            String time  = calculateTime(hygiene_date[hygiene_date.length-1],hygiene_time[hygiene_time.length-1]);
            list_ago[3]  = time;
            list_time[3] = hygiene_time[hygiene_time.length-1] + " , ";
        }
        else{
            list_note[3] = " ";
            list_time[3] = " ";
            list_ago [3] = getResources().getString(R.string.click_add);
        }

        if(teeths.size()!=0) {
            if(!teeth_note[teeth_note.length-1].equals("")) {
                list_note[4] = teeth_type + " - " + teeth_note[hygiene_note.length - 1];
            }else{
                list_note[4] = teeth_type;
            }
            String time  = calculateTime(teeth_date[teeth_date.length-1],teeth_time[teeth_time.length-1]);
            list_ago[4]  = time;
            list_time[4] = teeth_time[teeth_time.length-1] + " , ";
        }
        else{
            list_note[4] = " ";
            list_time[4] = " ";
            list_ago [4] = getResources().getString(R.string.click_add);
        }

        adapter = new ListViewAdapterForMoreEvents(getActivity(), favName, upLogo, list_time, list_note,list_ago);
    }

    private void getRecordsFromDatabase(String baby_id) {
        ActivityTable a_db = new ActivityTable(getActivity().getApplicationContext());
        records = a_db.showRecordForActivityType("Health",baby_id);
        if (records.size() != 0) {
            health_time = new String[records.size()];
            health_date = new String[records.size()];
            health_note = new String[records.size()];
            health_aid  = new String[records.size()];
            for (int i = 0; i < records.size(); i++) {
                health_time[i] = records.get(i).get("select_time");
                health_date[i] = records.get(i).get("select_date");
                health_note[i] = records.get(i).get("note");
                health_aid[i]  = records.get(i).get("a_id");
            }
        }
        records = a_db.showRecordForActivityType("Medicine",baby_id);
        if (records.size() != 0) {
            medicin_time = new String[records.size()];
            medicin_date = new String[records.size()];
            medicin_note = new String[records.size()];
            medicin_aid  = new String[records.size()];
            for (int i = 0; i < records.size(); i++) {
                medicin_time[i] = records.get(i).get("select_time");
                medicin_date[i] = records.get(i).get("select_date");
                medicin_note[i] = records.get(i).get("note");
                medicin_aid[i]  = records.get(i).get("a_id");
            }
        }
        records = a_db.showRecordForActivityType("Vaccination",baby_id);
        if (records.size() != 0) {
            vac_time = new String[records.size()];
            vac_date = new String[records.size()];
            vac_note = new String[records.size()];
            vac_aid  = new String[records.size()];
            for (int i = 0; i < records.size(); i++) {
                vac_time[i] = records.get(i).get("select_time");
                vac_date[i] = records.get(i).get("select_date");
                vac_note[i] = records.get(i).get("note");
                vac_aid[i]  = records.get(i).get("a_id");
            }
        }

        records = a_db.showRecordForActivityType("Hygiene",baby_id);
        if (records.size() != 0) {
            hygiene_time = new String[records.size()];
            hygiene_date = new String[records.size()];
            hygiene_note = new String[records.size()];
            hygiene_aid  = new String[records.size()];
            for (int i = 0; i < records.size(); i++) {
                hygiene_time[i] = records.get(i).get("select_time");
                hygiene_date[i] = records.get(i).get("select_date");
                hygiene_note[i] = records.get(i).get("note");
                hygiene_aid[i]  = records.get(i).get("a_id");
            }
        }

        records = a_db.showRecordForActivityType("Teeth",baby_id);
        if (records.size() != 0) {
            teeth_time = new String[records.size()];
            teeth_date = new String[records.size()];
            teeth_note = new String[records.size()];
            teeth_aid  = new String[records.size()];
            for (int i = 0; i < records.size(); i++) {
                teeth_time[i] = records.get(i).get("select_time");
                teeth_date[i] = records.get(i).get("select_date");
                teeth_note[i] = records.get(i).get("note");
                teeth_aid[i]  = records.get(i).get("a_id");
            }
        }

        if(health_aid.length>0) {
            Health health_db = new Health(getActivity().getApplicationContext()); // Db bağlantısı oluşturuyoruz. İlk seferde database oluşturulur.
            healths = health_db.getSpecificHealthWithAID(health_aid[health_aid.length - 1]);
            if (healths.size() != 0) {//mood listesi boşsa
                for (int i = 0; i < healths.size(); i++) {
                    health_type = healths.get(i).get(ActivityTable.HEALTH_TYPE);
                    health_temp = healths.get(i).get(ActivityTable.HEALTH_TEMP);
                }
            }
        }
        else{
            healths = new ArrayList<HashMap<String, String>>();
        }
        if(medicin_aid.length>0) {
            Medicine med_db = new Medicine(getActivity().getApplicationContext()); // Db bağlantısı oluşturuyoruz. İlk seferde database oluşturulur.
            medicines = med_db.getSpecificMedicineWithAID(medicin_aid[medicin_aid.length - 1]);
            if (medicines.size() != 0) {//mood listesi boşsa
                for (int i = 0; i < medicines.size(); i++) {
                    medicine_type       = medicines.get(i).get(ActivityTable.MEDICINE_TYPE);
                    medicine_dose       = medicines.get(i).get(ActivityTable.MEDICINE_DOSE);
                    medicine_dose_type  = medicines.get(i).get(ActivityTable.MEDICINE_DOSE_TYPE);
                }
            }
        }
        else{
            medicines = new ArrayList<HashMap<String, String>>();
        }

        if(vac_aid.length>0) {
            Vaccination vac_db = new Vaccination(getActivity().getApplicationContext()); // Db bağlantısı oluşturuyoruz. İlk seferde database oluşturulur.
            vaccinations = vac_db.getSpecificVaccinationWithAID(vac_aid[vac_aid.length - 1]);
            if (vaccinations.size() != 0) {//mood listesi boşsa
                for (int i = 0; i < vaccinations.size(); i++) {
                    vaccination_type  = vaccinations.get(i).get(ActivityTable.VACCINATION_TYPE);
                }
            }
        }
        else{
            vaccinations = new ArrayList<HashMap<String, String>>();
        }

        if(hygiene_aid.length>0) {
            Hygiene hyg_db = new Hygiene(getActivity().getApplicationContext()); // Db bağlantısı oluşturuyoruz. İlk seferde database oluşturulur.
            hygienes = hyg_db.getSpecificHygieneWithAID(hygiene_aid[hygiene_aid.length - 1]);
            if (hygienes.size() != 0) {//mood listesi boşsa
                for (int i = 0; i < hygienes.size(); i++) {
                    hygiene_type  = hygienes.get(i).get(ActivityTable.HYGIENE_TYPE);
                }
            }
        }
        else{
            hygienes = new ArrayList<HashMap<String, String>>();
        }

        if(teeth_aid.length>0) {
            Teeth teeth_db = new Teeth(getActivity().getApplicationContext()); // Db bağlantısı oluşturuyoruz. İlk seferde database oluşturulur.
            teeths = teeth_db.getSpecificTeethWithAID(teeth_aid[teeth_aid.length - 1]);
            if (teeths.size() != 0) {//mood listesi boşsa
                for (int i = 0; i < teeths.size(); i++) {
                    teeth_type  = teeths.get(i).get(ActivityTable.TEETH_TYPE);
                }
            }
        }
        else{
            teeths = new ArrayList<HashMap<String, String>>();
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