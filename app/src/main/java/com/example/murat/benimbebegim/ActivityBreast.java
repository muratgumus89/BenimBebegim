package com.example.murat.benimbebegim;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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
import android.widget.TextView;
import android.widget.TimePicker;
import android.os.Handler;

import java.util.Calendar;


public class ActivityBreast extends FragmentActivity implements View.OnClickListener {

    /**********************************************************************************************/
    /**********************************************************************************************/
    /**********************************************************************************************/
    Button btnSave, btnCancel, btnDatePicker, btnTimePicker, btnLeftBreast, btnRightBreast;
    EditText etNote;
    TextView txtTotal, txtPaused, txtActual;

    DatePickerDialog.OnDateSetListener dateForDB;
    TimePickerDialog.OnTimeSetListener timeForDB;

    Calendar myCalendar = Calendar.getInstance();

    String selectedDate, selectedTime, strTime, strDate, getBabyName, getUserId,
            selectedGendersForCreateBaby, strSelectedImage = "null";

    private Handler mHandler = new Handler();
    private long startTime;
    private long elapsedTime;
    private final int REFRESH_RATE = 100;
    private String hours, minutes, seconds, milliseconds;
    private long secs, mins, hrs;
    private boolean stopped = false;
    boolean stateLeft = true; // T: duruyor F: calısıyor

    private Handler mHandler1 = new Handler();
    private long startTime1;
    private long elapsedTime1;
    private final int REFRESH_RATE1 = 100;
    private String hours1, minutes1, seconds1, milliseconds1;
    private long secs1, mins1, hrs1;
    private boolean stopped1 = false;
    boolean stateLeft1 = true; // T: duruyor F: calısıyor

    private Runnable startTimer = new Runnable() {
        public void run() {
            elapsedTime = System.currentTimeMillis() - startTime;
            updateTimer(elapsedTime);
            mHandler.postDelayed(this, REFRESH_RATE);
        }
    };
    private Runnable startTimer1 = new Runnable() {
        public void run() {
            elapsedTime1 = System.currentTimeMillis() - startTime1;
            updateTimer1(elapsedTime1);
            mHandler1.postDelayed(this, REFRESH_RATE1);
        }
    };

    /**********************************************************************************************/
    /**********************************************************************************************/
    /**********************************************************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_breast);
        init();
    }

    public void startClicks() {
        if (stopped) {
            startTime = System.currentTimeMillis() - elapsedTime;
        } else {
            startTime = System.currentTimeMillis();
        }
        mHandler.removeCallbacks(startTimer);
        mHandler.postDelayed(startTimer, 0);
    }

    public void stopClicks() {
        mHandler.removeCallbacks(startTimer);
        stopped = true;
    }

    private void updateTimer(float time) {
        secs = (long) (time / 1000);
        mins = (long) ((time / 1000) / 60);
        hrs = (long) (((time / 1000) / 60) / 60);

		/* Convert the seconds to String
         * and format to ensure it has
		 * a leading zero when required
		 */
        secs = secs % 60;
        seconds = String.valueOf(secs);
        if (secs == 0) {
            seconds = "00";
        }
        if (secs < 10 && secs > 0) {
            seconds = "0" + seconds;
        }

		/* Convert the minutes to String and format the String */

        mins = mins % 60;
        minutes = String.valueOf(mins);
        if (mins == 0) {
            minutes = "00";
        }
        if (mins < 10 && mins > 0) {
            minutes = "0" + minutes;
        }

    	/* Convert the hours to String and format the String */

        hours = String.valueOf(hrs);
        if (hrs == 0) {
            hours = "00";
        }
        if (hrs < 10 && hrs > 0) {
            hours = "0" + hours;
        }

    	/* Although we are not using milliseconds on the timer in this example
    	 * I included the code in the event that you wanted to include it on your own
    	 */
        milliseconds = String.valueOf((long) time);
        Log.i("miliseconds", String.valueOf(milliseconds.length()) + " ::" + milliseconds);


        if (milliseconds.length() == 2) {
            milliseconds = "0" + milliseconds;
        }
        if (milliseconds.length() <= 1) {
            milliseconds = "00";
        }
        if (milliseconds.length() >= 3) {
            milliseconds = milliseconds.substring(milliseconds.length() - 3, milliseconds.length() - 2);
        }
        if (milliseconds.length() > 3 && milliseconds.length() < 4) {
            milliseconds = milliseconds.substring(milliseconds.length() - 3, milliseconds.length() - 2);
        }
        ((Button) findViewById(R.id.btnBreastActivity_LeftBreast)).setText(hours + " : " + minutes + " : " + seconds);
    }

    public void startClicks1() {
        if (stopped1) {
            startTime1 = System.currentTimeMillis() - elapsedTime1;
        } else {
            startTime1 = System.currentTimeMillis();
        }
        mHandler1.removeCallbacks(startTimer1);
        mHandler1.postDelayed(startTimer1, 0);
    }

    public void stopClicks1() {
        mHandler1.removeCallbacks(startTimer1);
        stopped1 = true;
    }

    private void updateTimer1(float time) {
        secs1 = (long) (time / 1000);
        mins1 = (long) ((time / 1000) / 60);
        hrs1 = (long) (((time / 1000) / 60) / 60);

		/* Convert the seconds to String
         * and format to ensure it has
		 * a leading zero when required
		 */
        secs1 = secs1 % 60;
        seconds1 = String.valueOf(secs1);
        if (secs1 == 0) {
            seconds1 = "00";
        }
        if (secs1 < 10 && secs1 > 0) {
            seconds1 = "0" + seconds1;
        }

		/* Convert the minutes to String and format the String */

        mins1 = mins1 % 60;
        minutes1 = String.valueOf(mins1);
        if (mins1 == 0) {
            minutes1 = "00";
        }
        if (mins1 < 10 && mins1 > 0) {
            minutes1 = "0" + minutes1;
        }

    	/* Convert the hours to String and format the String */

        hours1 = String.valueOf(hrs1);
        if (hrs1 == 0) {
            hours1 = "00";
        }
        if (hrs1 < 10 && hrs1 > 0) {
            hours1 = "0" + hours1;
        }

    	/* Although we are not using milliseconds on the timer in this example
    	 * I included the code in the event that you wanted to include it on your own
    	 */
        milliseconds1 = String.valueOf((long) time);
        Log.i("miliseconds", String.valueOf(milliseconds1.length()) + " ::" + milliseconds1);


        if (milliseconds1.length() == 2) {
            milliseconds1 = "0" + milliseconds1;
        }
        if (milliseconds1.length() <= 1) {
            milliseconds1 = "00";
        }
        if (milliseconds1.length() >= 3) {
            milliseconds1 = milliseconds1.substring(milliseconds1.length() - 3, milliseconds1.length() - 2);
        }
        if (milliseconds1.length() > 3 && milliseconds1.length() < 4) {
            milliseconds1 = milliseconds1.substring(milliseconds1.length() - 3, milliseconds1.length() - 2);
        }
        ((Button) findViewById(R.id.btnBreastActivity_RightBreast)).setText(hours1 + " : " + minutes1 + " : " + seconds1);
    }

    private void init(){
        btnDatePicker = (Button)findViewById(R.id.btnBreastActivity_Date);
        btnTimePicker = (Button)findViewById(R.id.btnBreastActivity_Time);
        btnCancel = (Button)findViewById(R.id.btnBreastActivity_Cancel);
        btnSave = (Button)findViewById(R.id.btnBreastActivity_Save);
        btnLeftBreast = (Button)findViewById(R.id.btnBreastActivity_LeftBreast);
        btnRightBreast = (Button)findViewById(R.id.btnBreastActivity_RightBreast);
        btnLeftBreast = (Button)findViewById(R.id.btnBreastActivity_LeftBreast);

        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnRightBreast.setOnClickListener(this);
        btnLeftBreast.setOnClickListener(this);

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
        getMenuInflater().inflate(R.menu.menu_activity_breast, menu);
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
        Log.i("tag0","fsd");
        switch (v.getId()) {
            case R.id.btnBreastActivity_Date:
                new DatePickerDialog(ActivityBreast.this, dateForDB,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

                Log.i("IS IT WORKING", "yes");
                break;
            case R.id.btnBreastActivity_Time:
                //Time picker dialog opens
                new TimePickerDialog(ActivityBreast.this, timeForDB,
                        myCalendar.get(Calendar.HOUR_OF_DAY),
                        myCalendar.get(Calendar.MINUTE), true).show();

                Log.i("IS IT WORKING", "yes");
                break;
            case R.id.btnBreastActivity_Cancel:
                onBackPressed();
                Log.i("IS IT WORKING", "yes");
                break;
            case R.id.btnBreastActivity_Save:
                Log.i("IS IT WORKING", "yes");
                break;
            case R.id.btnBreastActivity_LeftBreast:
                if (stateLeft == true) {
                    startClicks();
                    stateLeft = false;
                } else {
                    stopClicks();
                    stateLeft = true;
                }
                break;
            case R.id.btnBreastActivity_RightBreast:
                if (stateLeft1 == true) {
                    startClicks1();
                    stateLeft1 = false;
                } else {
                    stopClicks1();
                    stateLeft1 = true;
                }
                break;
            default:
                break;
    }
    }
}
