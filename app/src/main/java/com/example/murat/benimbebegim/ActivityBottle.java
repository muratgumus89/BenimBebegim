package com.example.murat.benimbebegim;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;
import android.os.Handler;
import android.widget.Toast;

import com.example.murat.benimbebegim.Databases.ActivityTable;
import com.example.murat.benimbebegim.Databases.Bottle;
import com.example.murat.benimbebegim.Databases.Solid;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.TimeZone;


public class ActivityBottle extends FragmentActivity implements View.OnClickListener {

    /****************************************************************************************** **/
    Button btnSave, btnCancel, btnDatePicker, btnTimePicker, btnTypeOfBottle, btnTime, btnAmount;
    EditText etNote;

    final Context context = this;
    public static final String PREFS_NAME = "MyPrefsFile";
    String activity_type="Bottle";
    int act_id[];

    DatePickerDialog.OnDateSetListener dateForDB;
    TimePickerDialog.OnTimeSetListener timeForDB;

    Calendar myCalendar = Calendar.getInstance();

    String selectedDate, selectedTime, strTime, strDate, getBabyName, getUserId,
            selectedGendersForCreateBaby, strSelectedImage = "null";

    String saveDate, saveTime,selected_time,selected_date,baby_id,user_id;

    private TextView tempTextView; //Temporary TextView
    private Button tempBtn; //Temporary Button
    private Handler mHandler = new Handler();
    private long startTime;
    private long elapsedTime;
    private final int REFRESH_RATE = 100;
    private String hours, minutes, seconds, milliseconds;
    private long secs, mins, hrs, msecs;
    private boolean stopped = false;
    boolean state = true; // T: duruyor F: calısıyor
    private Runnable startTimer = new Runnable() {
        public void run() {
            elapsedTime = System.currentTimeMillis() - startTime;
            updateTimer(elapsedTime);
            mHandler.postDelayed(this, REFRESH_RATE);
        }
    };
    /******************************************************************************************* */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activity_bottle);
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
        ((Button) findViewById(R.id.btnBottleActivity_time)).setText(hours + ":" + minutes + ":" + seconds);
    }

    private void init(){
        btnDatePicker   = (Button)findViewById(R.id.btnBottleActivity_Date);
        btnTimePicker   = (Button)findViewById(R.id.btnBottleActivity_Time);
        btnCancel       = (Button)findViewById(R.id.btnBottleActivity_Cancel);
        btnSave         = (Button)findViewById(R.id.btnBottleActivity_Save);
        btnTypeOfBottle = (Button)findViewById(R.id.btnBottleActivity_formula);
        btnTime         = (Button)findViewById(R.id.btnBottleActivity_time);
        btnAmount       = (Button)findViewById(R.id.btnBottleActivity_amount);
        etNote          = (EditText)findViewById(R.id.etBottleActivity_Notes);


        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnTypeOfBottle.setOnClickListener(this);
        btnTime.setOnClickListener(this);
        btnAmount.setOnClickListener(this);

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
                addBottleRecord();
                break;
            case R.id.btnBottleActivity_time:
                if (state == true) {
                    startClicks();
                    state = false;
                } else {
                    stopClicks();
                    state = true;
                }
                break;
            case R.id.btnBottleActivity_formula:
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.dialog_type_of_bottle);
                dialog.setTitle("Select type of solid");
                dialog.setCancelable(true);

                final RadioButton rb1 = (RadioButton)dialog.findViewById(R.id.rbBottleDialog_BreastMilk);
                final RadioButton rb2 = (RadioButton)dialog.findViewById(R.id.rbBottleDialog_Formula);
                final RadioButton rb3 = (RadioButton)dialog.findViewById(R.id.rbBottleDialog_Juice);
                final RadioButton rb4 = (RadioButton)dialog.findViewById(R.id.rbBottleDialog_Milk);
                final RadioButton rb5 = (RadioButton)dialog.findViewById(R.id.rbBottleDialog_Other);
                final RadioButton rb6 = (RadioButton)dialog.findViewById(R.id.rbBottleDialog_Water);

                rb1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        btnTypeOfBottle.setText(rb1.getText());
                        dialog.dismiss();
                    }
                });

                rb2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        btnTypeOfBottle.setText(rb2.getText());
                        dialog.dismiss();
                    }
                });

                rb3.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        btnTypeOfBottle.setText(rb3.getText());
                        dialog.dismiss();
                    }
                });

                rb4.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        btnTypeOfBottle.setText(rb4.getText());
                        dialog.dismiss();
                    }
                });

                rb5.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        btnTypeOfBottle.setText(rb5.getText());
                        dialog.dismiss();
                    }
                });

                rb6.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        btnTypeOfBottle.setText(rb6.getText());
                        dialog.dismiss();
                    }
                });

                dialog.show();
                break;
            case R.id.btnBottleActivity_amount:
                final Dialog dialog1 = new Dialog(context);
                dialog1.setContentView(R.layout.dialog_bottle_amaount);
                dialog1.setTitle("Amount of comsume");
                dialog1.setCancelable(true);

                final Button btnOk, btnCancel;
                final EditText etAmount;

                etAmount = (EditText)dialog1.findViewById(R.id.etDialogAmount);

                btnOk = (Button) dialog1.findViewById(R.id.btnDialog_bottle_amount_ok);
                btnOk.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        btnAmount.setText(etAmount.getText());
                        dialog1.dismiss();
                    }
                });

                btnCancel = (Button) dialog1.findViewById(R.id.btnDialog_bottle_amount_cancel);
                btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog1.dismiss();
                    }
                });

                dialog1.show();
                break;
            default:
                break;
        }
    }

    private void addBottleRecord() {
        String note         = etNote.getText().toString();
        String typeOfBottle = btnTypeOfBottle.getText().toString();
        int amount;


        if(btnAmount.getText().toString().equals("0,00 ml")) {
            amount = 0;
        }else {
            amount = Integer.valueOf(btnAmount.getText().toString());
        }
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
            Log.e("Bottle:ToActivityTable","Error");
        }

        getActiviyId();
        try {
            Bottle b_db = new Bottle(getApplicationContext());
            Log.e("Bottle:Timer=", btnTime.getText().toString());
            b_db.insertBottle(act_id[act_id.length - 1], typeOfBottle, amount, btnTime.getText().toString());
            b_db.close();
        }catch (Exception e){
            Log.e("Bottle:InsertBottle","Error");
        }

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
