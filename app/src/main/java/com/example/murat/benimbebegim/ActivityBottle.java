package com.example.murat.benimbebegim;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;

import java.util.Calendar;


public class ActivityBottle extends FragmentActivity implements View.OnClickListener {

    Button btnSave, btnCancel, btnDatePicker, btnTimePicker;
    EditText etNote;

    DatePickerDialog.OnDateSetListener dateForDB;
    TimePickerDialog.OnTimeSetListener timeForDB;

    Calendar myCalendar = Calendar.getInstance();

    String selectedDate, selectedTime, strTime, strDate, getBabyName, getUserId,
            selectedGendersForCreateBaby, strSelectedImage = "null";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_bottle);

        init();
    }

    private void init(){
        btnDatePicker = (Button)findViewById(R.id.btnBottleActivity_Date);
        btnTimePicker = (Button)findViewById(R.id.btnBottleActivity_Time);
        btnCancel = (Button)findViewById(R.id.btnBottleActivity_Cancel);
        btnSave = (Button)findViewById(R.id.btnBottleActivity_Save);

        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnSave.setOnClickListener(this);

        dateForDB = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                selectedDate = dayOfMonth + "/" + (monthOfYear + 1) + "/"
                        + year;
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
        getMenuInflater().inflate(R.menu.menu_activity_bottle, menu);
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
            case R.id.btnBottleActivity_Date:
                new DatePickerDialog(ActivityBottle.this, dateForDB,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

                break;
            case R.id.btnBottleActivity_Time:
                //Time picker dialog opens
                new TimePickerDialog(ActivityBottle.this, timeForDB,
                        myCalendar.get(Calendar.HOUR_OF_DAY),
                        myCalendar.get(Calendar.MINUTE), true).show();

                break;
            case R.id.btnBottleActivity_Cancel:
                onBackPressed();
                break;
            case R.id.btnBottleActivity_Save:
                break;
            default:
                break;
        }
    }
}
