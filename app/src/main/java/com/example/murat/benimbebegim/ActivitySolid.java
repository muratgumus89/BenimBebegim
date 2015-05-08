package com.example.murat.benimbebegim;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.murat.benimbebegim.Databases.ActivityTable;
import com.example.murat.benimbebegim.Databases.Mood;
import com.example.murat.benimbebegim.Databases.Solid;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;


public class ActivitySolid extends FragmentActivity implements View.OnClickListener {

    Button btnSave, btnCancel, btnDatePicker, btnTimePicker;
    EditText etNote,etGram;
    CheckBox cbBread,cbFruit,cbCereal,cbMeat,cbDairy,cbPasta,cbEggs,cbVegetable,cbFish,cbOther;

    DatePickerDialog.OnDateSetListener dateForDB;
    TimePickerDialog.OnTimeSetListener timeForDB;

    Calendar myCalendar = Calendar.getInstance();

    public static final String PREFS_NAME = "MyPrefsFile";

    String isBread="False",isFruit="False",isCereal="False",isMeat="False",isDairy="False"
            ,isPasta="False",isEggs="False",isVegetable="False",isFish="False",isOther="False";
    String saveDate, saveTime,selected_time,selected_date,baby_id,user_id;
    String activity_type="Solid";
    int act_id[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_solid);
        SharedPreferences pref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        baby_id = pref.getString("baby_id", null);
        user_id = pref.getString("user_id",null);
        init();
    }

    private void init(){
        btnDatePicker = (Button)findViewById(R.id.btnSolidActivity_Date);
        btnTimePicker = (Button)findViewById(R.id.btnSolidActivity_Time);
        btnCancel = (Button)findViewById(R.id.btnSolidActivity_Cancel);
        btnSave = (Button)findViewById(R.id.btnSolidActivity_Save);
        etNote  = (EditText)findViewById(R.id.etSolidActivity_Notes);
        etGram  = (EditText)findViewById(R.id.etSolidActivity_gram);

        cbBread = (CheckBox)findViewById(R.id.cbBread);
        cbFruit = (CheckBox)findViewById(R.id.cbFruit);
        cbCereal= (CheckBox)findViewById(R.id.cbCereal);
        cbMeat  = (CheckBox)findViewById(R.id.cbMeat);
        cbDairy = (CheckBox)findViewById(R.id.cbDairy);
        cbPasta = (CheckBox)findViewById(R.id.cbPasta);
        cbEggs  = (CheckBox)findViewById(R.id.cbEggs);
        cbVegetable = (CheckBox)findViewById(R.id.cbVegetables);
        cbFish = (CheckBox)findViewById(R.id.cbFish);
        cbOther= (CheckBox)findViewById(R.id.cbOther);

        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnSave.setOnClickListener(this);

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
                if (dayOfMonth < 10 && (monthOfYear + 1) < 10) {
                    selected_date = "0" + dayOfMonth + "/0" + (monthOfYear + 1) + "/"
                            + year;
                }
                else if(dayOfMonth<10) {
                    selected_date = "0" + dayOfMonth + "/" + (monthOfYear + 1) + "/"
                            + year;
                }else if ((monthOfYear + 1) < 10){
                    selected_date = dayOfMonth + "/0" + (monthOfYear + 1) + "/"
                            + year;
                }else{
                    selected_date = dayOfMonth + "/" + (monthOfYear + 1) + "/"
                            + year;
                }
                btnDatePicker.setText(selected_date);

            }
        };

        timeForDB = new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                myCalendar.set(Calendar.MINUTE, minute);
                selected_time = myCalendar.getTime().toString()
                        .substring(11, 16);
                btnTimePicker.setText(selected_time);
            }
        };
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_activity_solid, menu);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSolidActivity_Date:
                new DatePickerDialog(ActivitySolid.this, dateForDB,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

                break;
            case R.id.btnSolidActivity_Time:
                //Time picker dialog opens
                new TimePickerDialog(ActivitySolid.this, timeForDB,
                        myCalendar.get(Calendar.HOUR_OF_DAY),
                        myCalendar.get(Calendar.MINUTE), true).show();

                break;
            case R.id.btnSolidActivity_Cancel:
                onBackPressed();
                break;
            case R.id.btnSolidActivity_Save:
                addSolid();
                break;
            default:
                break;
        }
    }

    private void addSolid() {
        String note=etNote.getText().toString();
        int gram=0;
        if(etGram.getText().toString().equals("")) {
            gram = 0;
        }else {
            gram = Integer.valueOf(etGram.getText().toString());
        }
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
        checkedControl();

        Solid s_db = new Solid(getApplicationContext());

        s_db.insertSolid(act_id[act_id.length-1],gram,isBread,isFruit,isCereal,isMeat,isDairy,isPasta,isEggs,isVegetable,isFish,isOther);
        s_db.close();

        Toast.makeText(getApplicationContext(), getResources().getString(R.string.add_record), Toast.LENGTH_LONG).show();
        onBackPressed();
    }

    private void checkedControl() {
        if(cbBread.isChecked()) isBread="True";
        if(cbFruit.isChecked()) isFruit="True";
        if(cbCereal.isChecked()) isCereal="True";
        if(cbMeat.isChecked()) isMeat="True";
        if(cbDairy.isChecked()) isDairy="True";
        if(cbPasta.isChecked()) isPasta="True";
        if(cbEggs.isChecked()) isEggs="True";
        if(cbVegetable.isChecked()) isVegetable="True";
        if(cbFish.isChecked()) isFish="True";
        if(cbOther.isChecked()) isOther="True";
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
