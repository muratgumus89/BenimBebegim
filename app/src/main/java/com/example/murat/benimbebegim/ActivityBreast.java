package com.example.murat.benimbebegim;

import android.content.SharedPreferences;
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
import android.widget.Toast;

import com.example.murat.benimbebegim.Databases.ActivityTable;
import com.example.murat.benimbebegim.Databases.Bottle;
import com.example.murat.benimbebegim.Databases.Breast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;


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
    public static final String PREFS_NAME = "MyPrefsFile";

    String activity_type="Breast";
    int act_id[];

    String selectedDate, selectedTime, strTime, strDate, getBabyName, getUserId,
            selectedGendersForCreateBaby, strSelectedImage = "null",baby_id,user_id;

    private Handler mHandler = new Handler();
    private long startTime;
    private long elapsedTime;
    private final int REFRESH_RATE = 100;
    private String hours ="0", minutes="0", seconds="0", milliseconds;
    private long secs, mins, hrs;
    private boolean stopped = false;
    boolean stateLeft = true; // T: duruyor F: calısıyor

    private Handler mHandler1 = new Handler();
    private long startTime1;
    private long elapsedTime1;
    private final int REFRESH_RATE1 = 100;
    private String hours1 = "0", minutes1 = "0", seconds1 ="0", milliseconds1;
    private long secs1, mins1, hrs1;
    private boolean stopped1 = false;
    boolean stateLeft1 = true; // T: duruyor F: calısıyor

    private Handler mHandler2 = new Handler();
    private long startTime2;
    private long elapsedTime2;
    private final int REFRESH_RATE2 = 100;
    private String hours2, minutes2, seconds2, milliseconds2;
    private long secs2, mins2, hrs2;
    private boolean stopped2 = false;
    boolean stateLeft2 = false; // T: duruyor F: calısıyor
    boolean stateRight2= false;

    boolean isPaused = false ;

    private Runnable startTimer = new Runnable() {
        public void run() {
            elapsedTime = System.currentTimeMillis() - startTime;
            updateTimer(elapsedTime);
            addTimes();
            mHandler.postDelayed(this, REFRESH_RATE);
        }
    };
    private Runnable startTimer1 = new Runnable() {
        public void run() {
            elapsedTime1 = System.currentTimeMillis() - startTime1;
            updateTimer1(elapsedTime1);
            addTimes();
            mHandler1.postDelayed(this, REFRESH_RATE1);
        }
    };
    private Runnable startTimer2 = new Runnable() {
        public void run() {
            elapsedTime2 = System.currentTimeMillis() - startTime2;
            updateTimer2(elapsedTime2);
            txtTotal.setText(hours2 + ":" + minutes2 + ":" + seconds2);
            if(isPaused)
                calculatePause();
            mHandler2.postDelayed(this, REFRESH_RATE2);
        }
    };

    /**********************************************************************************************/
    /**********************************************************************************************/
    /**********************************************************************************************/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_breast);
        SharedPreferences pref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        baby_id = pref.getString("baby_id", null);
        user_id = pref.getString("user_id",null);
        init();
    }

    public void startClicks() {
        if (stopped) {
            startTime = System.currentTimeMillis() - elapsedTime;
        } else {
            startTime  = System.currentTimeMillis();
        }
        mHandler.removeCallbacks(startTimer);
        mHandler.postDelayed(startTimer, 0);
        isPaused = false;
    }

    public void stopClicks() {
        mHandler.removeCallbacks(startTimer);
        stopped  = true;
        isPaused = true;
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
        ((Button) findViewById(R.id.btnBreastActivity_LeftBreast)).setText(hours + ":" + minutes + ":" + seconds);
    }

    public void startClicks1() {
        if (stopped1) {
            startTime1 = System.currentTimeMillis() - elapsedTime1;
        } else {
            startTime1 = System.currentTimeMillis();
        }
        mHandler1.removeCallbacks(startTimer1);
        mHandler1.postDelayed(startTimer1, 0);
        isPaused = false;
    }

    public void stopClicks1() {
        mHandler1.removeCallbacks(startTimer1);
        stopped1 = true;
        isPaused = true;
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
        ((Button) findViewById(R.id.btnBreastActivity_RightBreast)).setText(hours1 + ":" + minutes1 + ":" + seconds1);
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
    private void addTimes(){
        String leftTime = btnLeftBreast.getText().toString();
        String rightTime= btnRightBreast.getText().toString();

        int totalHours   = Integer.valueOf(hours)   + Integer.valueOf(hours1);
        int totalMinutes = Integer.valueOf(minutes) + Integer.valueOf(minutes1);
        int totalSeconds = Integer.valueOf(seconds) + Integer.valueOf(seconds1);
        if (totalSeconds >= 60) {
            totalMinutes ++;
            totalSeconds = totalSeconds % 60;
        }
        if (totalMinutes >= 60) {
            totalHours ++;
            totalMinutes = totalMinutes % 60;
        }
        if(totalHours < 10 && totalMinutes < 10 && totalSeconds < 10){
            txtActual.setText("0" + totalHours + ":0" + totalMinutes + ":0" + totalSeconds);
        }else if(totalHours < 10 && totalMinutes < 10){
            txtActual.setText("0" + totalHours + ":0" + totalMinutes + ":" + totalSeconds);
        }else if(totalHours < 10 && totalSeconds < 10){
            txtActual.setText("0" + totalHours + ":" + totalMinutes + ":0" + totalSeconds);
        }else if(totalMinutes < 10 && totalSeconds < 10) {
            txtActual.setText(totalHours + ":0" + totalMinutes + ":0" + totalSeconds);
        }else if(totalHours < 10) {
            txtActual.setText("0" + totalHours + ":" + totalMinutes + ":" + totalSeconds);
        }else if(totalMinutes < 10 ) {
            txtActual.setText(totalHours + ":0" + totalMinutes + ":" + totalSeconds);
        }else if(totalSeconds < 10 ) {
            txtActual.setText(totalHours + ":" + totalMinutes + ":0" + totalSeconds);
        }else{
            txtActual.setText(totalHours + ":" + totalMinutes + ":" + totalSeconds);
        }
    }
    private void calculatePause(){
        int totalHours   = Integer.valueOf(hours)   + Integer.valueOf(hours1);
        int totalMinutes = Integer.valueOf(minutes) + Integer.valueOf(minutes1);
        int totalSeconds = Integer.valueOf(seconds) + Integer.valueOf(seconds1);

        if (totalSeconds >= 60) {
            totalMinutes ++;
            totalSeconds = totalSeconds % 60;
        }
        if (totalMinutes >= 60) {
            totalHours ++;
            totalMinutes = totalMinutes % 60;
        }

        int pauseHours   = Integer.valueOf(hours2)    - Integer.valueOf(totalHours);
        int pauseMinutes = Integer.valueOf(minutes2)  - Integer.valueOf(totalMinutes);
        int pauseSeconds = Integer.valueOf(seconds2)  - Integer.valueOf(totalSeconds);

        if (pauseSeconds >= 60) {
            pauseMinutes ++;
            pauseSeconds = pauseSeconds % 60;
        }
        if (pauseMinutes >= 60) {
            pauseHours ++;
            pauseMinutes = pauseMinutes % 60;
        }

        java.text.DateFormat df = new java.text.SimpleDateFormat("hh:mm:ss");
        String time1 = hours2 + ":" + minutes2 + ":" + seconds2 ;
        String time2 = totalHours + ":" + totalMinutes + ":" + totalSeconds;
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

        String pauseTime = (hour<10 ? "0" + hour : hour) + ":" + (minute < 10 ? "0" + minute : minute) + ":" + (second < 10 ? "0" + second : second);

        txtPaused.setText(pauseTime);

    }
    private void init(){
        btnDatePicker  = (Button)findViewById(R.id.btnBreastActivity_Date);
        btnTimePicker  = (Button)findViewById(R.id.btnBreastActivity_Time);
        btnCancel      = (Button)findViewById(R.id.btnBreastActivity_Cancel);
        btnSave        = (Button)findViewById(R.id.btnBreastActivity_Save);
        btnLeftBreast  = (Button)findViewById(R.id.btnBreastActivity_LeftBreast);
        btnRightBreast = (Button)findViewById(R.id.btnBreastActivity_RightBreast);
        btnLeftBreast  = (Button)findViewById(R.id.btnBreastActivity_LeftBreast);
        txtTotal       = (TextView)findViewById(R.id.tVTotalBreastTimer);
        txtPaused      = (TextView)findViewById(R.id.tVPauseBreastTimer);
        txtActual      = (TextView)findViewById(R.id.tVActualBreastTimer);
        etNote         = (EditText)findViewById(R.id.etBreastActivity_Notes);

        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnRightBreast.setOnClickListener(this);
        btnLeftBreast.setOnClickListener(this);

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
                stateLeft2 = false ;
                stateRight2= false ;
                onBackPressed();
                Log.i("IS IT WORKING", "yes");
                break;
            case R.id.btnBreastActivity_Save:
                Log.i("IS IT WORKING", "yes");
                saveToDatabase();
                break;
            case R.id.btnBreastActivity_LeftBreast:
                if (stateLeft == true && stateLeft1==true) {
                    startClicks();
                    stateLeft = false;
                    if(stateLeft2 == false ){
                        startClicks2();
                        stateLeft2 = true ;
                    }
                } else {
                    stopClicks();
                    stateLeft = true;
                }
                break;
            case R.id.btnBreastActivity_RightBreast:
                if (stateLeft1 == true && stateLeft == true) {
                    startClicks1();
                    stateLeft1 = false;
                    if(stateLeft2 == false ){
                        startClicks2();
                        stateLeft2 = true ;
                    }
                } else {
                    stopClicks1();
                    stateLeft1 = true;
                }
                break;
            default:
                break;
    }
    }

    private void saveToDatabase() {
        String note         = etNote.getText().toString();
        String breastTime   = txtActual.getText().toString();

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
            Breast bre_db = new Breast(getApplicationContext());
            Log.e("Breast:Timer=", txtActual.getText().toString());
            if(!txtActual.getText().toString().equals(""))
            bre_db.insertBreast(act_id[act_id.length - 1],txtActual.getText().toString());
            else
                bre_db.insertBreast(act_id[act_id.length - 1],"00 : 00 : 00");
            bre_db.close();
        }catch (Exception e){
            Log.e("Breast:InsertBreast","Error");
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
                Log.i("Breast Id  :", String.valueOf(act_id[i]));
            }
        }
        db.close();
    }
}
