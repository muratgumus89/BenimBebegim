package com.example.murat.benimbebegim;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.murat.benimbebegim.Databases.MoodDatabase;
import com.example.murat.benimbebegim.adapters.ListViewAdapterForTodayFragment;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;


public class ActivityToday extends Fragment{
    public static final String TAG = ActivityToday.class.getSimpleName();
    public static ActivityToday newInstance() {
        return new ActivityToday();
    }
    ListView list;
    ListViewAdapterForTodayFragment adapter;
    String baby_id;
    int count=0;
    public static final String PREFS_NAME = "MyPrefsFile";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.activity_today, container,false);
        list = (ListView)view.findViewById(R.id.listTodayFragment);
        //Get babyid from SP
        SharedPreferences pref;
        pref = getActivity().getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        baby_id = pref.getString("baby_id", null);
        getMoods(baby_id);
        return view;
    }
    private void getMoods(String baby_id) {
        ArrayList<HashMap<String, String>> moods;
        String mood_name[] = new String[0], mood_time[] = new String[0], mood_info[]= new String[0];
        //Get values from databases for listview
        MoodDatabase db = new MoodDatabase(getActivity().getApplicationContext()); // Db bağlantısı oluşturuyoruz. İlk seferde database oluşturulur.
        String currentDate = getCurrentDate();
        Log.d("CurrentDate:",currentDate);
        moods = db.getTodaysData(currentDate,baby_id);//mood listesini alıyoruz
        if (moods.size() != 0) {//mood listesi boşsa
            mood_name = new String[moods.size()]; // mood adlarını tutucamız string arrayi olusturduk.
            mood_time = new String[moods.size()];
            mood_info = new String[moods.size()];
            for (int i = 0; i < moods.size(); i++) {
                mood_name[i] = moods.get(i).get("mood");
                mood_time[i] = moods.get(i).get("time");
                mood_info[i] = moods.get(i).get("detail");
                Log.i("Mood Name:", mood_name[i]);
                Log.i("Mood Time:", mood_time[i]);
                if(mood_info[i].equals(""))
                Log.i("Mood Info:", "boş");
                else
                Log.i("Mood Info:", mood_info[i]);
            }
        }
            count = count + moods.size();
            adapter = new ListViewAdapterForTodayFragment(getActivity(),R.drawable.ic_mood_bullet,mood_time,mood_name,mood_info,count);
            list.setAdapter(adapter);



        Log.d("Count",String.valueOf(count));
    }

    private String getCurrentDate() {
        Calendar c_date = Calendar.getInstance();
        SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
        String saveDate = date.format(c_date.getTime());
        return saveDate;
    }
}
