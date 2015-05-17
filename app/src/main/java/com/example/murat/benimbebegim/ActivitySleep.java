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
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.os.Handler;
import android.widget.Toast;

import com.example.murat.benimbebegim.Databases.ActivityTable;
import com.example.murat.benimbebegim.Databases.Breast;
import com.example.murat.benimbebegim.Databases.Sleep;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;


public class ActivitySleep extends FragmentActivity implements View.OnClickListener {

    Button btnSave, btnCancel, btnDatePicker, btnTimePicker, btnHowLong;
    EditText etNote;
    TextView txtTotal,txtPause;

    DatePickerDialog.OnDateSetListener dateForDB;
    TimePickerDialog.OnTimeSetListener timeForDB;

    Calendar myCalendar = Calendar.getInstance();
    String activity_type="Sleep";
    public static final String PREFS_NAME = "MyPrefsFile";
    int act_id[];

    String selectedDate, selectedTime, strTime, strDate, getBabyName, getUserId,
            selectedGendersForCreateBaby, strSelectedImage = "null",baby_id,user_id;

    private Handler mHandler = new Handler();
    private long startTime;
    private long elapsedTime;
    private final int REFRESH_RATE = 100;
    private String hours, minutes, seconds, milliseconds;
    private long secs, mins, hrs, msecs;
    private boolean stopped = false;
    boolean state = true; // T: duruyor F: calısıyor

    private Handler mHandler2 = new Handler();
    private long startTime2;
    private long elapsedTime2;
    private final int REFRESH_RATE2 = 100;
    private String hours2, minutes2, seconds2, milliseconds2;
    private long secs2, mins2, hrs2;
    private boolean stopped2 = false;
    boolean isTotal = false;

    boolean isPaused = false;

    private Runnable startTimer = new Runnable() {
        public void run() {
            elapsedTime = System.currentTimeMillis() - startTime;
            updateTimer(elapsedTime);
            mHandler.postDelayed(this, REFRESH_RATE);
        }
    };

    private Runnable startTimer2 = new Runnable() {
        public void run() {
            elapsedTime2 = System.currentTimeMillis() - startTime2;
            updateTimer2(elapsedTime2);
            txtTotal.setText(" " + hours2 + ":" + minutes2 + ":" + seconds2);
            if(isPaused)
                calculatePause();
            mHandler2.postDelayed(this, REFRESH_RATE2);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_sleep);
        SharedPreferences pref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        baby_id = pref.getString("baby_id", null);
        user_id = pref.getString("user_id",null);
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
        ((Button) findViewById(R.id.btnSleepActivity_HowLong)).setText(hours + ":" + minutes + ":" + seconds);
    }
    public void startClicks2() {
        if (stopped2) {
            startTime2 = System.currentTimeMillis() - elapsedTime2;
        } else {
            startTime2 = System.currentTimeMillis();
        }
        mHandler2.removeCallbacks(startTimer2);
        mHandler2.postDelayed(startTimer2, 0);
    }

    public void stopClicks2() {
        mHandler2.removeCallbacks(startTimer2);
        stopped2 = true;
    }

    private void updateTimer2(float time) {
        secs2 = (long) (time / 1000);
        mins2 = (long) ((time / 1000) / 60);
        hrs2 = (long) (((time / 1000) / 60) / 60);

		/* Convert the seconds to String
         * and format to ensure it has
		 * a leading zero when required
		 */
        secs2 = secs2 % 60;
        seconds2 = String.valueOf(secs2);
        if (secs2 == 0) {
            seconds2 = "00";
        }
        if (secs2 < 10 && secs2 > 0) {
            seconds2 = "0" + seconds2;
        }

		/* Convert the minutes to String and format the String */

        mins2 = mins2 % 60;
        minutes2 = String.valueOf(mins2);
        if (mins2 == 0) {
            minutes2 = "00";
        }
        if (mins2 < 10 && mins2 > 0) {
            minutes2 = "0" + minutes2;
        }

    	/* Convert the hours to String and format the String */

        hours2 = String.valueOf(hrs2);
        if (hrs2 == 0) {
            hours2 = "00";
        }
        if (hrs2 < 10 && hrs2 > 0) {
            hours2 = "0" + hours2;
        }

    	/* Although we are not using milliseconds on the timer in this example
    	 * I included the code in the event that you wanted to include it on your own
    	 */
        milliseconds2 = String.valueOf((long) time);
        Log.i("miliseconds", String.valueOf(milliseconds2.length()) + " ::" + milliseconds2);


        if (milliseconds2.length() == 2) {
            milliseconds2 = "0" + milliseconds2;
        }
        if (milliseconds2.length() <= 1) {
            milliseconds2 = "00";
        }
        if (milliseconds2.length() >= 3) {
            milliseconds2 = milliseconds2.substring(milliseconds2.length() - 3, milliseconds2.length() - 2);
        }
        if (milliseconds2.length() > 3 && milliseconds2.length() < 4) {
            milliseconds2 = milliseconds2.substring(milliseconds2.length() - 3, milliseconds2.length() - 2);
        }
    }
    private void init() {
        btnDatePicker = (Button) findViewById(R.id.btnSleepActivity_Date);
        btnTimePicker = (Button) findViewById(R.id.btnSleepActivity_Time);
        btnCancel     = (Button) findViewById(R.id.btnSleepActivity_Cancel);
        btnSave       = (Button) findViewById(R.id.btnSleepActivity_Save);
        btnHowLong    = (Button) findViewById(R.id.btnSleepActivity_HowLong);
        etNote        = (EditText)findViewById(R.id.etSleepActivity_Notes);
        txtTotal      = (TextView)findViewById(R.id.txtSleep_Total);
        txtPause      = (TextView)findViewById(R.id.txtSleep_Pause);

        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnHowLong.setOnClickListener(this);

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
        getMenuInflater().inflate(R.menu.menu_activity_sleep, menu);
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
        Log.i("tag0", "fsd");
        switch (v.getId()) {
            case R.id.btnSleepActivity_Date:
                new DatePickerDialog(ActivitySleep.this, dateForDB,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

                Log.i("IS IT WORKING", "yes");
                break;
            case R.id.btnSleepActivity_Time:
                //Time picker dialog opens
                new TimePickerDialog(ActivitySleep.this, timeForDB,
                        myCalendar.get(Calendar.HOUR_OF_DAY),
                        myCalendar.get(Calendar.MINUTE), true).show();

                Log.i("IS IT WORKING", "yes");
                break;
            case R.id.btnSleepActivity_Cancel:
                onBackPressed();
                Log.i("IS IT WORKING", "yes");
                break;
            case R.id.btnSleepActivity_Save:
                saveToDatabase();
                break;
            case R.id.btnSleepActivity_HowLong:
                if (state == true) {
                    startClicks();
                    isPaused = false;
                    if( isTotal == false){
                        startClicks2();
                        isTotal = true;
                    }
                    state = false;
                } else {
                    stopClicks();
                    isPaused = true;
                    state    = true;
                }
                break;
            default:
                break;
        }
    }

    private void saveToDatabase() {
        String note         = etNote.getText().toString();
        String totalSleep   = btnHowLong.getText().toString();

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
            Log.e("Sleep:ToActivityTable","Error");
        }
        getActiviyId();
        try {
            Sleep s_db = new Sleep(getApplicationContext());
            Log.e("Sleep:Timer=", totalSleep);
            if(!totalSleep.equals("00.00.00"))
                s_db.insertSleep(act_id[act_id.length - 1],totalSleep);
            else
                s_db.insertSleep(act_id[act_id.length - 1],"00:00:00");
            s_db.close();
        }catch (Exception e){
            Log.e("Sleep:InsertSleep","Error");
        }
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.add_record), Toast.LENGTH_LONG).show();
        onBackPressed();
    }

    private void calculatePause(){

        java.text.DateFormat df = new java.text.SimpleDateFormat("hh:mm:ss");
        String time1 = hours2 + ":" + minutes2 + ":" + seconds2 ;
        String time2 = hours  + ":" + minutes  + ":" + seconds;
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Date date1 = null;
        Date date2 = null;
        try {
            date1 = format.parse(time1);
            date2 = format.parse(time2);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long diff = Math.abs(date2.getTime() - date1.getTime());


        long timeInSeconds = (diff / 1000);
        long hour, minute, second;
        hour = timeInSeconds / 3600;
        timeInSeconds = timeInSeconds - (hour * 3600);
        minute = timeInSeconds / 60;
        timeInSeconds = timeInSeconds - (minute * 60);
        second = timeInSeconds;

        String pauseTime = " " + (hour<10 ? "0" + hour : hour) + ":" + (minute < 10 ? "0" + minute : minute) + ":" + (second < 10 ? "0" + second : second);

        txtPause.setText(pauseTime);

    }
    private void getActiviyId() {
        ActivityTable db = new ActivityTable(getApplicationContext()); // Db bağlantısı oluşturuyoruz. İlk seferde database oluşturulur.
        ArrayList<HashMap<String, String>> activity_id = db.showRecordForActivityType(activity_type, baby_id);//mood listesini alıyoruz
        if (activity_id.size() != 0) { //mood listesi boşsa
            act_id = new int[activity_id.size()]; // mood id lerini tutucamız string arrayi olusturduk.
            for (int i = 0; i < activity_id.size(); i++) {
                act_id[i] = Integer.parseInt(activity_id.get(i).get("a_id"));
                Log.i("Sleep Id  :", String.valueOf(act_id[i]));
            }
        }
        db.close();
    }
}
