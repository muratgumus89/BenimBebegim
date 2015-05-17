package com.example.murat.benimbebegim;

import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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
import com.example.murat.benimbebegim.Databases.Breast;
import com.example.murat.benimbebegim.Databases.Diaper;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;


public class ActivityDiaper extends FragmentActivity implements  AdapterView.OnItemSelectedListener, OnClickListener {

    Button btnSave, btnCancel, btnDatePicker, btnTimePicker;
    EditText etNote;
    Spinner spnDiaper;

    DatePickerDialog.OnDateSetListener dateForDB;
    TimePickerDialog.OnTimeSetListener timeForDB;

    Calendar myCalendar = Calendar.getInstance();
    public static final String PREFS_NAME = "MyPrefsFile";

    String activity_type="Diaper";
    int act_id[];

    String selectedDate, selectedTime, strTime, strDate, getBabyName, getUserId,
            selectedGendersForCreateBaby, strSelectedImage = "null",baby_id,user_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_diaper);

        SharedPreferences pref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        baby_id = pref.getString("baby_id", null);
        user_id = pref.getString("user_id",null);

        Spinner spinner = (Spinner) findViewById(R.id.spnr_DiaperActivity_Moods);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.activity_diaper_poop, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(ActivityDiaper.this);
        init();
    }

    private void init(){
        btnDatePicker = (Button)findViewById(R.id.btnDiaperActivity_Date);
        btnTimePicker = (Button)findViewById(R.id.btnDiaperActivity_Time);
        btnCancel     = (Button)findViewById(R.id.btnDiaperActivity_Cancel);
        btnSave       = (Button)findViewById(R.id.btnDiaperActivity_Save);
        spnDiaper     = (Spinner)findViewById(R.id.spnr_DiaperActivity_Moods);
        etNote        = (EditText)findViewById(R.id.etDiaperActivity_Notes);

        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnSave.setOnClickListener(this);

        /******************
        * Naming The Date Button
        */
        Calendar c_date = Calendar.getInstance();
        SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
        String saveDate = date.format(c_date.getTime());
        btnDatePicker.setText(saveDate);

        /******************
         * Naming The Time Button
         */
        Calendar c_time = Calendar.getInstance(TimeZone.getDefault());
        SimpleDateFormat time = new SimpleDateFormat("HH:mm",
                Locale.getDefault());
        String saveTime = time.format(c_time.getTime());
        btnTimePicker.setText(saveTime);

        dateForDB = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                if (dayOfMonth < 10 && (monthOfYear + 1) < 10) {
                    selectedDate = "0" + dayOfMonth + "/0" + (monthOfYear + 1) + "/"
                            + year;
                }
                else if(dayOfMonth<10) {
                    selectedDate = "0" + dayOfMonth + "/" + (monthOfYear + 1) + "/"
                            + year;
                }else if ((monthOfYear + 1) < 10){
                    selectedDate = dayOfMonth + "/0" + (monthOfYear + 1) + "/"
                            + year;
                }else{
                    selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/"
                            + year;
                }
                btnDatePicker.setText(selectedDate);

            }
        };

        timeForDB = new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                myCalendar.set(Calendar.MINUTE, minute);
                selectedTime = myCalendar.getTime().toString()
                        .substring(11, 16);
                btnTimePicker.setText(selectedTime);
            }
        };
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_diaper, menu);
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

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void onClick(View v) {
        Log.i("tag0","fsd");
        switch (v.getId()) {
            case R.id.btnDiaperActivity_Date:
                new DatePickerDialog(ActivityDiaper.this, dateForDB,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

                Log.i("IS IT WORKING", "yes");
                break;
            case R.id.btnDiaperActivity_Time:
                //Time picker dialog opens
                new TimePickerDialog(ActivityDiaper.this, timeForDB,
                        myCalendar.get(Calendar.HOUR_OF_DAY),
                        myCalendar.get(Calendar.MINUTE), true).show();

                Log.i("IS IT WORKING", "yes");
                break;
            case R.id.btnDiaperActivity_Cancel:
                onBackPressed();
                Log.i("IS IT WORKING", "yes");
                break;
            case R.id.btnDiaperActivity_Save:
                saveToDatabase();
                Log.i("IS IT WORKING", "yes");
                break;
            default:
                break;
        }
    }

    private void saveToDatabase() {
        String note         = etNote.getText().toString();
        String diaperType   = spnDiaper.getSelectedItem().toString();

        Calendar c_date = Calendar.getInstance();
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        String saveDate = date.format(c_date.getTime());

        Calendar c_time = Calendar.getInstance(TimeZone.getDefault());
        SimpleDateFormat time = new SimpleDateFormat("HH:mm",
                Locale.getDefault());
        String saveTime = time.format(c_time.getTime());
        try {
            ActivityTable a_db = new ActivityTable(getApplicationContext());
            a_db.insertRecord(activity_type, baby_id, user_id,btnDatePicker.getText().toString(), btnTimePicker.getText().toString(), saveDate, saveTime, note);
            a_db.close();
        }catch (Exception e){
            Log.e("Breast:ToActivityTable","Error");
        }
        getActiviyId();
        try {
            Diaper diaper_db = new Diaper(getApplicationContext());
            diaper_db.insertDiaper(act_id[act_id.length - 1],diaperType);
            diaper_db.close();
        }catch (Exception e){
            Log.e("Diaper:InsertDiaper","Error");
        }
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.add_record), Toast.LENGTH_LONG).show();
        onBackPressed();
    }
    private void getActiviyId() {
        ActivityTable db = new ActivityTable(getApplicationContext()); // Db bağlantısı oluşturuyoruz. İlk seferde database oluşturulur.
        ArrayList<HashMap<String, String>> activity_id = db.showRecordForActivityType(activity_type, baby_id);//mood listesini alıyoruz
        if (activity_id.size() != 0) { //mood listesi boşsa
            act_id = new int[activity_id.size()]; // mood id lerini tutucamız string arrayi olusturduk.
            for (int i = 0; i < activity_id.size(); i++) {
                act_id[i] = Integer.parseInt(activity_id.get(i).get("a_id"));
                Log.i("Dıaper Id  :", String.valueOf(act_id[i]));
            }
        }
        db.close();
    }
}
