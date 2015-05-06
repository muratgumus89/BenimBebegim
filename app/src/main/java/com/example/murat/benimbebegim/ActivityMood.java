package com.example.murat.benimbebegim;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.murat.benimbebegim.Databases.ActivityTable;
import com.example.murat.benimbebegim.Databases.Mood;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;


public class ActivityMood extends FragmentActivity implements AdapterView.OnItemSelectedListener, OnClickListener{

    Button btnSave, btnCancel, btnDatePicker, btnTimePicker;
    EditText etNote;

    DatePickerDialog.OnDateSetListener dateForDB;
    TimePickerDialog.OnTimeSetListener timeForDB;

    Spinner spinner;
    String mood= "Quite";
    String activity_type="Mood";
    int mood_id[],act_id[];

    public static final String PREFS_NAME = "MyPrefsFile";

    Calendar myCalendar = Calendar.getInstance();

    String saveDate, saveTime,selected_time,selected_date,baby_id,user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_mood);

        SharedPreferences pref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        baby_id = pref.getString("baby_id", null);
        user_id = pref.getString("user_id",null);

        spinner = (Spinner) findViewById(R.id.spnr_MoodActivity_Moods);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.activity_mood_moods, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(ActivityMood.this);
        init();
    }

    private void init(){
        btnDatePicker = (Button)findViewById(R.id.btnMoodActivity_Date);
        btnTimePicker = (Button)findViewById(R.id.btnMoodActivity_Time);
        btnCancel = (Button)findViewById(R.id.btnMoodActivity_Cancel);
        btnSave = (Button)findViewById(R.id.btnMoodActivity_Save);
        etNote  = (EditText)findViewById(R.id.etMoodActivity_Notes);

        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        etNote = (EditText)findViewById(R.id.etMoodActivity_Notes);

        /******************
         * Naming The Date Button
         */
        Calendar c_date = Calendar.getInstance();
        SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
        saveDate = date.format(c_date.getTime());
        btnDatePicker.setText(saveDate);
        selected_date = saveDate;
        /******************
         * Naming The Time Button
         */
        Calendar c_time = Calendar.getInstance(TimeZone.getDefault());
        SimpleDateFormat time = new SimpleDateFormat("HH:mm",
                Locale.getDefault());
        saveTime = time.format(c_time.getTime());
        btnTimePicker.setText(saveTime);
        selected_time = saveTime;


        dateForDB = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                if(monthOfYear<10) {
                    String month = "0" + (monthOfYear + 1 );
                    selected_date = dayOfMonth + "/" + (month) + "/"
                            + year;
                }
                else{
                    selected_date = dayOfMonth + "/" + (monthOfYear + 1) + "/"
                            + year;
                }
                btnDatePicker.setText(selected_date);
                Log.i("selected_date", selected_date);
            }
        };

        timeForDB = new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                myCalendar.set(Calendar.MINUTE, minute);
                selected_time = myCalendar.getTime().toString()
                        .substring(11, 16);
                btnTimePicker.setText(selected_time);
                Log.i("selected_time", selected_time);
            }
        };
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_mood, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int index = parent.getSelectedItemPosition();
        mood = spinner.getSelectedItem().toString();
        Log.i("Mood",mood);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        Log.i("tag0","fsd");
        switch (v.getId()) {
            case R.id.btnMoodActivity_Date:
                new DatePickerDialog(ActivityMood.this, dateForDB,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

                break;
            case R.id.btnMoodActivity_Time:
                //Time picker dialog opens
                new TimePickerDialog(ActivityMood.this, timeForDB,
                        myCalendar.get(Calendar.HOUR_OF_DAY),
                        myCalendar.get(Calendar.MINUTE), true).show();
                break;
            case R.id.btnMoodActivity_Cancel:
                onBackPressed();
                Log.i("IS IT WORKING", "yes");
                break;
            case R.id.btnMoodActivity_Save:
                addMood();
                Log.i("IS IT WORKING", "yes");
                break;
            default:
                break;
        }
    }

    private void addMood() {
        String note=etNote.getText().toString();
        Log.i("notes",note);

        Calendar c_date = Calendar.getInstance();
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        saveDate = date.format(c_date.getTime());

        Calendar c_time = Calendar.getInstance(TimeZone.getDefault());
        SimpleDateFormat time = new SimpleDateFormat("HH:mm",
                Locale.getDefault());
        saveTime = time.format(c_time.getTime());

        ActivityTable a_db = new ActivityTable(getApplicationContext());
        a_db.insertRecord(activity_type,baby_id,user_id,selected_date,selected_time,saveDate,saveTime,note);
        a_db.close();
        getActiviyId();
        Mood db = new Mood(getApplicationContext());
        db.insertMood(act_id[act_id.length-1],mood);
        db.close();
        //Son eklenen mood u sp ye at
        getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                .edit()
                .putString("mood",mood)
                .commit();
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.add_record), Toast.LENGTH_LONG).show();
        onBackPressed();
    }

    private void getActiviyId() {
        ActivityTable db = new ActivityTable(getApplicationContext()); // Db bağlantısı oluşturuyoruz. İlk seferde database oluşturulur.
        ArrayList<HashMap<String, String>> activity_id = db.showRecordForActivityType(activity_type, baby_id);//mood listesini alıyoruz
        if (activity_id.size() != 0) {//mood listesi boşsa
            act_id = new int[activity_id.size()]; // mood id lerini tutucamız string arrayi olusturduk.
            for (int i = 0; i < activity_id.size(); i++) {
                act_id[i] = Integer.parseInt(activity_id.get(i).get("a_id"));
                Log.i("Mood Id  :", String.valueOf(act_id[i]));
            }
        }
        db.close();
    }
}
