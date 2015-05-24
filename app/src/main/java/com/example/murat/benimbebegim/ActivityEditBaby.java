
package com.example.murat.benimbebegim;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by aytunc on 16.2.2015.
 */
public class ActivityEditBaby extends Activity implements OnClickListener{
    /*
     Variables For Xml Components
    */
    ImageView imgSelectedPicture_BabyEdit;
    Button btnDatePicker_BabyEdit, btnTimePicker_BabyEdit, btnCancel_BabyEdit,
            btnDelete_BabyEdit, btnOk_BabyEdit,btnTheme_BabyEdit;
    EditText edtGetBabyName_BabyEdit;
    Calendar myCalendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener dateForDB_BabyEdit;
    TimePickerDialog.OnTimeSetListener timeForDB_BabyEdit;
    /*
     Variables For MySql Connections
    */
    String image_str;
    InputStream is=null;
    String result=null;
    String line=null;
    String realPath="null";
    String oldPath="null";
    int code;
    /*
     Variables For Shared Preferences
     */
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    /*
     Variables For GettinValues
    */
    String getUserId_BabyEdit;
    String selectedDate_BabyEdit, selectedTime_BabyEdit, strTime_BabyEdit,
            strDate_BabyEdit, selectedGendersForEditBaby,gettingImage;
    /*
     Variables For Gender
    */
    private String[] genders_EditBaby = { "MALE", "FEMALE" };
    private String[] genders_EditBaby_Choose = { "FEMALE", "MALE" };
    private Spinner spinnerSelectGender_EditBaby;
    private ArrayAdapter<String> dataAdapterForGender_EditBaby;
    /*
     Variables For Baby Picture Capture
    */
    Intent i;
    final static int cameraData = 0;
    private static final int SELECT_PICTURE = 1;
    private String selectedImagePath;
    private Uri selectedImageUri;

    public static final String PREFS_NAME = "MyPrefsFile";
    // Activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;
    Dialog dialogCircle;

    // directory name to store captured images and videos
    private static final String IMAGE_DIRECTORY_NAME = "Hello Camera";
    public static String imageName;

    private Uri fileUri; // file url to store image/video
    Boolean isChanging;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_baby);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        InitUI();
    }// End OnCreate

    private void InitUI() {
        // TODO Auto-generated method stub
        /****************************
         * INIT COMPONENTS
         */
        imgSelectedPicture_BabyEdit = (ImageView) findViewById(R.id.ivBabyPicture_BabyEdit);
        btnDatePicker_BabyEdit = (Button) findViewById(R.id.btnDate_BabyEdit);
        btnTimePicker_BabyEdit = (Button) findViewById(R.id.btnTime_BabyEdit);
        btnCancel_BabyEdit = (Button) findViewById(R.id.btnCancel_BabyEdit);
        btnDelete_BabyEdit = (Button) findViewById(R.id.btnDelete_BabyEdit);
        btnOk_BabyEdit = (Button) findViewById(R.id.btnOk_BabyEdit);
        edtGetBabyName_BabyEdit = (EditText) findViewById(R.id.edtBabyName_BabyEdit);
        spinnerSelectGender_EditBaby = (Spinner) findViewById(R.id.gender_spinner_EditBaby);
        btnTheme_BabyEdit=(Button)findViewById(R.id.btnTheme_BabyEdit);
        /****************************
         * Set Click Listener to Buttons
         */
        btnDatePicker_BabyEdit.setOnClickListener((OnClickListener) this);
        btnTimePicker_BabyEdit.setOnClickListener((OnClickListener) this);
        btnCancel_BabyEdit.setOnClickListener((OnClickListener) this);
        btnDelete_BabyEdit.setOnClickListener((OnClickListener) this);
        btnOk_BabyEdit.setOnClickListener((OnClickListener) this);
        imgSelectedPicture_BabyEdit.setOnClickListener((OnClickListener) this);
        btnTheme_BabyEdit.setOnClickListener((OnClickListener)this);

        getValuesFromDatabases();



        /******************
         * Getting The Selected Date Value
         */
        dateForDB_BabyEdit = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                selectedDate_BabyEdit = dayOfMonth + "/" + (monthOfYear + 1)
                        + "/" + year;
                btnDatePicker_BabyEdit.setText(selectedDate_BabyEdit);

            }
        };
        /******************
         * Get the Selected Time Value
         */
        timeForDB_BabyEdit = new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                myCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                myCalendar.set(Calendar.MINUTE, minute);
                selectedTime_BabyEdit = myCalendar.getTime().toString()
                        .substring(11, 16);
                btnTimePicker_BabyEdit.setText(selectedTime_BabyEdit);
            }
        };

    }

    private void getValuesFromDatabases() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String strBaby_id = preferences.getString("baby_id",null);
        Log.d("strBaby_id",strBaby_id);
        String strUser_id = preferences.getString("user_id",null);
        Log.d("strUser_id",strUser_id);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

        nameValuePairs.add(new BasicNameValuePair("bid",strBaby_id));
        nameValuePairs.add(new BasicNameValuePair("uid",strUser_id ));


        try {

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://176.58.88.85/~murat/select_for_edit.php");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            Log.e("log_tag", "connection success ");
        } catch (Exception e) {
            Log.e("Fail 1", e.toString());
            Toast.makeText(getApplicationContext(), "Invalid IP Address",
                    Toast.LENGTH_LONG).show();
        }

        try {
            BufferedReader reader = new BufferedReader
                    (new InputStreamReader(is, "utf-8"), 8);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
            Log.d("log_tag", "convert response to string completed!");
        } catch (Exception e) {
            Log.d("log_tag", "Error converting result " + e.toString());
        }
        //parse json data
        try {
            JSONArray jArray = new JSONArray(result);
            for(int i=0;i<jArray.length();i++){
                JSONObject json_data = jArray.getJSONObject(i);
                Log.d("log_tag","BID: "+json_data.getInt("BID")+
                                ", Name: "+json_data.getString("Name")+
                                ", Date: "+json_data.getString("Date")
                                +", Time: "+json_data.getString("Time")
                                +", Image: "+json_data.getString("Image")
                                +", UID: "+json_data.getInt("UID")
                                +", Gender: "+json_data.getString("Gender")
                                +", Theme: "+json_data.getString("Theme")


                );
                // json_data.getString("Image")
                realPath=json_data.getString("Image");
                oldPath=json_data.getString("Image");
                Bitmap image=StringToBitMap(realPath);

                if(!json_data.getString("Image").equals("null")) {
                    imgSelectedPicture_BabyEdit.setImageBitmap(image);
                }
                else{
                    imgSelectedPicture_BabyEdit.setImageResource(R.drawable.select_picture_icon_128);
                }
                edtGetBabyName_BabyEdit.setText(json_data.getString("Name"));
                btnDatePicker_BabyEdit.setText(json_data.getString("Date"));
                btnTimePicker_BabyEdit.setText(json_data.getString("Time"));
                if(json_data.getString("Gender").equals("MALE")){
                    SelectionSpinner(genders_EditBaby);
                }else{
                    SelectionSpinner(genders_EditBaby_Choose);
                }

            }
            Log.d("log_tag", "parse json data completed!");
        } catch (Exception e) {
            Log.d("log_tag", "Error in http connection " + e.toString());
        } finally {
            Log.d("log_tag", "ALL completed!");
        }

    }
    private void SelectionSpinner(String[] adapter){
        /****************************
         * Datas For Genders
         */
        dataAdapterForGender_EditBaby = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, adapter);
        dataAdapterForGender_EditBaby
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSelectGender_EditBaby.setAdapter(dataAdapterForGender_EditBaby);
        spinnerSelectGender_EditBaby
                .setOnItemSelectedListener(new OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int position, long id) {
                        // Hangi il seçilmişse onun ilçeleri adapter'e
                        // ekleniyor.
                        if (parent.getSelectedItem().toString()
                                .equals(genders_EditBaby[0]))
                            selectedGendersForEditBaby = "MALE";
                        else if (parent.getSelectedItem().toString()
                                .equals(genders_EditBaby[1]))
                            selectedGendersForEditBaby = "FEMALE";
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });
    }
    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btnDate_BabyEdit:
                /******************
                 * Show the Date Picker Dialog
                 */
                new DatePickerDialog(ActivityEditBaby.this, dateForDB_BabyEdit,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;
            case R.id.btnTime_BabyEdit:
                /******************
                 * Show the Time Picker Dialog
                 */
                new TimePickerDialog(ActivityEditBaby.this, timeForDB_BabyEdit,
                        myCalendar.get(Calendar.HOUR_OF_DAY),
                        myCalendar.get(Calendar.MINUTE), true).show();
                break;
            case R.id.btnCancel_BabyEdit:
                //getValuesFromDatabases();
                getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                        .edit()
                        .putBoolean("isSpinner",false)
                        .commit();
                Intent intentHomeScreen = new Intent(getApplicationContext(),
                        ActivityHomeScreen.class);
                startActivity(intentHomeScreen);

                break;
            case R.id.btnDelete_BabyEdit:
                getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                        .edit()
                        .putBoolean("isSpinner",false)
                        .commit();
                deleteValuesFromDatabase();
                SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
                int spin_position = preferences.getInt("spin_position",0);
                getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                        .edit()
                        .putInt("spin_position",spin_position - 1)
                        .commit();
                babyControl();
                break;
            case R.id.btnOk_BabyEdit:
                getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                        .edit()
                        .putBoolean("isSpinner",false)
                        .commit();
                /******************
                 * Checked Empty Areas For All Records
                 */
                if (edtGetBabyName_BabyEdit.getText().equals("")) {
                    Toast.makeText(getApplicationContext(),
                            R.string.valid_Name, Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                if (selectedGendersForEditBaby.equals("")) {
                    Toast.makeText(getApplicationContext(),
                            R.string.valid_Gender, Toast.LENGTH_LONG)
                            .show();
                    return;
                } else {
                    /******************
                     * Save the datas to Database
                     */
                    updateValuesToDatabase();
                }
                break;
            case R.id.ivBabyPicture_BabyEdit:
                imgSelectedPicture_BabyEdit.showContextMenu();
                registerForContextMenu(imgSelectedPicture_BabyEdit);
                openContextMenu(imgSelectedPicture_BabyEdit);
                unregisterForContextMenu(imgSelectedPicture_BabyEdit);
                Log.i("sdf", "bas");
            default:
                break;
        }
    }

    private void deleteValuesFromDatabase() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String strBaby_id = preferences.getString("baby_id", "");
        String strUser_id = preferences.getString("user_id", "");
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

        nameValuePairs.add(new BasicNameValuePair("bid",strBaby_id));


        try {

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://176.58.88.85/~murat/delete_baby.php");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            Log.e("log_tag", "connection success ");
        } catch (Exception e) {
            Log.e("Fail 1", e.toString());
            Toast.makeText(getApplicationContext(), "Invalid IP Address",
                    Toast.LENGTH_LONG).show();
        }

        try {
            BufferedReader reader = new BufferedReader
                    (new InputStreamReader(is, "utf-8"), 8);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
            Log.d("log_tag", "convert response to string completed!");
        } catch (Exception e) {
            Log.d("log_tag", "Error converting result " + e.toString());
        }
        //parse json data
        try {
            JSONObject json_data = new JSONObject(result);
            code = json_data.getInt("code");
            /******************
             *  Checked record is inserted or not
             */
            if (code == 1) {
                Toast.makeText(getBaseContext(), R.string.delete_account,
                        Toast.LENGTH_SHORT).show();
            }
            else {
                Toast.makeText(getBaseContext(),R.string.sorry,
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.e("CreateBabyFail3", e.toString());
        } finally {
            Log.e("CreateBabyFinally", (String.valueOf(code)));
        }


    }
    private String getBabyID(String strUserIDOpening,String baby_name)  {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("uid", strUserIDOpening));
        nameValuePairs.add(new BasicNameValuePair("baby_name", baby_name));
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://176.58.88.85/~murat/get_baby_id.php");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            Log.e("BabyIdCon", "connection success ");
        } catch (Exception e) {
            Log.e("BabyIdFail", e.toString());
            Toast.makeText(getApplicationContext(), "Invalid IP Address",
                    Toast.LENGTH_LONG).show();
        }

        try {
            BufferedReader reader = new BufferedReader
                    (new InputStreamReader(is, "utf-8"), 8);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            is.close();
            result = sb.toString();
            Log.e("Opening - Baby_ID: ", result);
        } catch (Exception e) {
            Log.e("BabyFail 2", e.toString());
        }
        Log.i("Result: ",result);
        return result;
    }
    private void babyControl() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String strUserIDOpening = preferences.getString("user_id", "");
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("user_id", strUserIDOpening));
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://176.58.88.85/~murat/baby_control.php");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            Log.e("pass 1", "connection success ");
        } catch (Exception e) {
            Log.e("Fail 1", e.toString());
            Toast.makeText(getApplicationContext(), "Invalid IP Address",
                    Toast.LENGTH_LONG).show();
        }

        try {
            BufferedReader reader = new BufferedReader
                    (new InputStreamReader(is, "utf-8"), 8);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
            ;
        } catch (Exception e) {
            Log.e("Fail 2", e.toString());
        }

        try {
            JSONObject json_data = new JSONObject(result);
            code = json_data.getInt("code");
            Log.e("kontrol", (String.valueOf(code)));
            /******************
             *  Checked record is inserted or not
             */
            if (code == 0) {
                Intent intentCreateBaby = new Intent(getApplicationContext(),
                        ActivityCreateBaby.class);
                Bundle b = new Bundle();
                b.putString("userid", strUserIDOpening);
                intentCreateBaby.putExtras(b);
                startActivity(intentCreateBaby);
            } else {
                getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                        .edit()
                        .putString("user_id", strUserIDOpening)
                        .commit();
                getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                        .edit()
                        .putString("baby_id", getBabyID(strUserIDOpening,getBabyName(strUserIDOpening)))
                        .commit();
                getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                        .edit()
                        .putString("baby_name", getBabyName(strUserIDOpening))
                        .commit();
                Intent intentHomeScreen = new Intent(getApplicationContext(),
                        ActivityHomeScreen.class);
                startActivity(intentHomeScreen);
            }
        } catch (Exception e) {
            Log.e("Fail 3", e.toString());
        }
    }
    private String getBabyName(String userid) {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String strUserIDOpening = preferences.getString("user_id", "");
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("uid",strUserIDOpening ));
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://176.58.88.85/~murat/just_one_baby_name.php");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            Log.e("BabyIdCon", "connection success ");
        } catch (Exception e) {
            Log.e("BabyIdFail", e.toString());
            Toast.makeText(getApplicationContext(), "Invalid IP Address",
                    Toast.LENGTH_LONG).show();
        }

        try {
            BufferedReader reader = new BufferedReader
                    (new InputStreamReader(is, "utf-8"), 8);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            is.close();
            result = sb.toString();
            Log.e("Opening - Baby Name: ", result);
        } catch (Exception e) {
            Log.e("BabyFail 2", e.toString());
        }
        return result;
    }
    private void checkedBabyIsExistOrNot() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String strUser_id = preferences.getString("user_id", "");
        Log.e("shared_preferences", strUser_id);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

        nameValuePairs.add(new BasicNameValuePair("user_id",strUser_id ));



        try {

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://176.58.88.85/~murat/baby_control.php");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            Log.e("log_tag", "connection success ");
        } catch (Exception e) {
            Log.e("Fail 1", e.toString());
            Toast.makeText(getApplicationContext(), "Invalid IP Address",
                    Toast.LENGTH_LONG).show();
        }

        try {
            BufferedReader reader = new BufferedReader
                    (new InputStreamReader(is, "utf-8"), 8);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
            Log.d("log_tag", "convert response to string completed!");
        } catch (Exception e) {
            Log.d("log_tag", "Error converting result " + e.toString());
        }
        //parse json data
        try {
            JSONObject json_data = new JSONObject(result);
            code = json_data.getInt("code");
            /******************
             *  Checked record is inserted or not
             */
            if (code == 1) {
                Intent goEditBaby = new Intent(getApplicationContext(),
                        ActivityHomeScreen.class);
                startActivity(goEditBaby);
            }
            else {
                Intent goCreateBaby = new Intent(getApplicationContext(),
                        ActivityCreateBaby.class);
                startActivity(goCreateBaby);
            }
        } catch (Exception e) {
            Log.e("CreateBabyFail3", e.toString());
        } finally {
            Log.e("CreateBabyFinally", (String.valueOf(code)));
        }


    }

    private void updateValuesToDatabase() {
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String strBaby_id = preferences.getString("baby_id", "");
        String strUser_id = preferences.getString("user_id", "");
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        /******************
         * Check Date and Time Picker Values null or not
         */
        Boolean control = changeControl();
        nameValuePairs.add(new BasicNameValuePair("bid",strBaby_id));
        if (control==true) {
            if (edtGetBabyName_BabyEdit.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(),
                        R.string.valid_Name, Toast.LENGTH_LONG)
                        .show();
                return;
            } else {
                nameValuePairs.add(new BasicNameValuePair("name", edtGetBabyName_BabyEdit.getText().toString()));
            }
            if (!realPath.equals("null") && !realPath.equals(oldPath)) {
                // bimatp factory
                BitmapFactory.Options options = new BitmapFactory.Options();

                // downsizing image as it throws OutOfMemory Exception for larger
                // images
                options.inSampleSize = 4;
                Bitmap bitmap = BitmapFactory.decodeFile(realPath, options);
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream); //compress to which format you want.
                byte[] byte_arr = stream.toByteArray();
                image_str = Base64.encodeBytes(byte_arr);
            } else if (realPath.equals(oldPath)) {
                image_str = oldPath.toString();
            } else {
                image_str = "null";
            }
            nameValuePairs.add(new BasicNameValuePair("date"  , btnDatePicker_BabyEdit.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("time"  , btnTimePicker_BabyEdit.getText().toString()));
            nameValuePairs.add(new BasicNameValuePair("image" , image_str));
            nameValuePairs.add(new BasicNameValuePair("uid"   , strUser_id));
            nameValuePairs.add(new BasicNameValuePair("gender", selectedGendersForEditBaby));
            nameValuePairs.add(new BasicNameValuePair("theme" , "Theme"));


            try {

                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost("http://176.58.88.85/~murat/update_create_baby.php");
                httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
                HttpResponse response = httpclient.execute(httppost);
                HttpEntity entity = response.getEntity();
                is = entity.getContent();
                Log.e("log_tag", "connection success ");
            } catch (Exception e) {
                Log.e("Fail 1", e.toString());
                Toast.makeText(getApplicationContext(), "Invalid IP Address",
                        Toast.LENGTH_LONG).show();
            }

            try {
                BufferedReader reader = new BufferedReader
                        (new InputStreamReader(is, "utf-8"), 8);
                StringBuilder sb = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                result = sb.toString();
                Log.d("log_tag", "convert response to string completed!");
            } catch (Exception e) {
                Log.d("log_tag", "Error converting result " + e.toString());
            }
            //parse json data
            try {
                JSONObject json_data = new JSONObject(result);
                code = json_data.getInt("code");
                /******************
                 *  Checked record is inserted or not
                 */
                if (code == 2){
                    String mName=edtGetBabyName_BabyEdit.getText().toString();
                    String msg=mName + " " + getResources().getString(R.string.have_a_baby);
                    Toast.makeText(getBaseContext(), msg,
                            Toast.LENGTH_SHORT).show();
                }
                else if (code == 1) {
                    Toast.makeText(getBaseContext(), R.string.update_account,
                            Toast.LENGTH_SHORT).show();
                    getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                            .edit()
                            .putString("baby_name", edtGetBabyName_BabyEdit.getText().toString())
                            .commit();
                    Intent intentHomeScreen1 = new Intent(getApplicationContext(),
                            ActivityHomeScreen.class);
                    startActivity(intentHomeScreen1);
                } else {
                    Toast.makeText(getBaseContext(), R.string.sorry,
                            Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Log.e("CreateBabyFail3", e.toString());
            } finally {
                Log.e("CreateBabyFinally", (String.valueOf(code)));
            }
        }else{
            Toast.makeText(getBaseContext(),"No Changing",Toast.LENGTH_SHORT).show();
            Intent intentHomeScreen1 = new Intent(getApplicationContext(),
                    ActivityHomeScreen.class);
            startActivity(intentHomeScreen1);
        }

    }

    private Boolean changeControl() {
        String name,date,time,gender,theme,path;
        gender = spinnerSelectGender_EditBaby.getSelectedItem().toString();
        name=edtGetBabyName_BabyEdit.getText().toString();
        date=btnDatePicker_BabyEdit.getText().toString();
        time=btnTimePicker_BabyEdit.getText().toString();
        path=oldPath;
        SharedPreferences preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        String strBaby_id = preferences.getString("baby_id",null);
        Log.d("strBaby_id",strBaby_id);
        String strUser_id = preferences.getString("user_id",null);
        Log.d("strUser_id",strUser_id);
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

        nameValuePairs.add(new BasicNameValuePair("bid",strBaby_id));
        nameValuePairs.add(new BasicNameValuePair("uid",strUser_id ));


        try {

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://176.58.88.85/~murat/select_for_edit.php");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            Log.e("log_tag", "connection success ");
        } catch (Exception e) {
            Log.e("Fail 1", e.toString());
            Toast.makeText(getApplicationContext(), "Invalid IP Address",
                    Toast.LENGTH_LONG).show();
        }

        try {
            BufferedReader reader = new BufferedReader
                    (new InputStreamReader(is, "utf-8"), 8);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
            Log.d("log_tag", "convert response to string completed!");
        } catch (Exception e) {
            Log.d("log_tag", "Error converting result " + e.toString());
        }
        //parse json data
        try {
            JSONArray jArray = new JSONArray(result);
            for(int i=0;i<jArray.length();i++){
                JSONObject json_data = jArray.getJSONObject(i);
                Log.d("log_tag","BID: "+json_data.getInt("BID")+
                                ", Name: "+json_data.getString("Name")+
                                ", Date: "+json_data.getString("Date")
                                +", Time: "+json_data.getString("Time")
                                +", Image: "+json_data.getString("Image")
                                +", UID: "+json_data.getInt("UID")
                                +", Gender: "+json_data.getString("Gender")
                                +", Theme: "+json_data.getString("Theme")

                );
                if(name.equals(json_data.getString("Name"))&&
                   date.equals(json_data.getString("Date"))&&
                   time.equals(json_data.getString("Time"))&&
                   path.equals(realPath)&&
                   gender.equals(json_data.getString("Gender"))){
                    isChanging=false;
                }
                else{
                    isChanging=true;
                }

            }
            Log.d("log_tag", "parse json data completed!");
        } catch (Exception e) {
            Log.d("log_tag", "Error in http connection " + e.toString());
        } finally {
            Log.d("log_tag", "ALL completed!");
        }
        return isChanging;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        // TODO Auto-generated method stub
        super.onCreateContextMenu(menu, v, menuInfo);
        ImageView imgSelectPicture = (ImageView) v
                .findViewById(R.id.ivBabyPicture_BabyEdit);
        menu.setHeaderTitle("ADD PHOTO");

        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_activity_opening, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        // TODO Auto-generated method stub
        super.onContextItemSelected(item);
        switch (item.getItemId()) {
            case R.id.itemTakePicture:
                captureImage();
                break;

            case R.id.itemChooseFromGallery:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(
                        Intent.createChooser(intent, "Select Picture"),
                        SELECT_PICTURE);
                break;
            case R.id.itemCancel:
                // Biraz arastırmamız gerekiyor
                break;

            default:
                break;
        }

        return true;
    }
    /*
 * Capturing Camera Image will lauch camera app requrest image capture
 */
    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }
    /*
 * Creating file uri to store image/video
 */
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }
    /*
 * returning image / video
 */
    private static File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
            setImageName(timeStamp);
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }
    public static void setImageName(String s){
        imageName=s;
    }
    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent data) {
        if (reqCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resCode == RESULT_OK) {
                // successfully captured the image
                // display it in image view
                Log.i("Camerayı Okeyledim","Hahaha");
                previewCapturedImage();
                return;
            } else if (resCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        R.string.cancel_capture, Toast.LENGTH_SHORT)
                        .show();
                return;
            }
        }else {
            if (resCode == Activity.RESULT_OK && data != null) {
                // SDK < API11
                if (Build.VERSION.SDK_INT < 11)
                    realPath = RealPathUtil.getRealPathFromURI_BelowAPI11(this, data.getData());

                    // SDK >= 11 && SDK < 19
                else if (Build.VERSION.SDK_INT < 19)
                    realPath = RealPathUtil.getRealPathFromURI_API11to18(this, data.getData());

                    // SDK > 19 (Android 4.4)
                else if (Build.VERSION.SDK_INT >= 19)
                    realPath = RealPathUtil.getRealPathFromURI_API19(this, data.getData());
            }
            else if (resCode == Activity.RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        R.string.cancel_select, Toast.LENGTH_SHORT)
                        .show();
                return;

            }
        }
        setPath(Build.VERSION.SDK_INT, data.getData().getPath(), realPath);
    }
    /*
* Display image from a path to ImageView
*/
    private void previewCapturedImage() {
        try {
            imgSelectedPicture_BabyEdit.setVisibility(View.VISIBLE);

            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();

            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            //options.inSampleSize = 8;

            final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                    options);
            realPath=fileUri.getPath();
            MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, getImageName() , "Kontrol");
            imgSelectedPicture_BabyEdit.setImageBitmap(bitmap);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }
    public static String getImageName(){
        return imageName.toString();
    }
    private void setPath(int sdk, String uriPath,String realPath){

        Uri uriFromPath = Uri.fromFile(new File(realPath));

        // you have two ways to display selected image

        // ( 1 ) imageView.setImageURI(uriFromPath);

        // ( 2 ) imageView.setImageBitmap(bitmap);
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(uriFromPath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        imgSelectedPicture_BabyEdit.setImageBitmap(bitmap);
        Log.d("Bitmap",bitmap.toString());
        Log.d("HMKCODE", "Build.VERSION.SDK_INT:"+sdk);
        Log.d("HMKCODE", "URI Path:"+uriPath);
        Log.d("HMKCODE", "Real Path: "+realPath);
    }
    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DECODE);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0,
                    encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }
}
