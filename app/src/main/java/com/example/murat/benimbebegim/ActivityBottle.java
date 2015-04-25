package com.example.murat.benimbebegim;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
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

import java.util.Calendar;


public class ActivityBottle extends FragmentActivity implements View.OnClickListener {

    /****************************************************************************************** **/
    Button btnSave, btnCancel, btnDatePicker, btnTimePicker, btnTypeOfBottle, btnTime, btnAmount;
    EditText etNote;

    final Context context = this;

    DatePickerDialog.OnDateSetListener dateForDB;
    TimePickerDialog.OnTimeSetListener timeForDB;

    Calendar myCalendar = Calendar.getInstance();

    String selectedDate, selectedTime, strTime, strDate, getBabyName, getUserId,
            selectedGendersForCreateBaby, strSelectedImage = "null";

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
        ((Button) findViewById(R.id.btnBottleActivity_time)).setText(hours + " : " + minutes + " : " + seconds);
    }

    private void init(){
        btnDatePicker = (Button)findViewById(R.id.btnBottleActivity_Date);
        btnTimePicker = (Button)findViewById(R.id.btnBottleActivity_Time);
        btnCancel = (Button)findViewById(R.id.btnBottleActivity_Cancel);
        btnSave = (Button)findViewById(R.id.btnBottleActivity_Save);
        btnTypeOfBottle = (Button)findViewById(R.id.btnBottleActivity_formula);
        btnTime = (Button)findViewById(R.id.btnBottleActivity_time);
        btnAmount = (Button)findViewById(R.id.btnBottleActivity_amount);


        btnDatePicker.setOnClickListener(this);
        btnTimePicker.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnTypeOfBottle.setOnClickListener(this);
        btnTime.setOnClickListener(this);
        btnAmount.setOnClickListener(this);

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
}
