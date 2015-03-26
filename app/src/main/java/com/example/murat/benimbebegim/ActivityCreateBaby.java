package com.example.murat.benimbebegim;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
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
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import org.json.JSONObject;

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


public class ActivityCreateBaby extends Activity implements OnClickListener {
    /*
     Variables For Xml Components
    */
    ImageView imgSelectBabyPicture;
    EditText edtNameCreateBaby, edtWeightCreateBaby, edtHeightCreateBaby;
    Button btnDatePicker, btnTimePicker, btnCancelCreateBaby, btnOkCreateBaby, btnTheme;
    DatePickerDialog.OnDateSetListener dateForDB;
    TimePickerDialog.OnTimeSetListener timeForDB;
    LinearLayout colorlayout;
    /*
        Variables For Calendar
    */
    Calendar myCalendar = Calendar.getInstance();
    /*
        Variables For SelectTheme
    */
    final Context context = this;
    /*
        Variables For GettingValues
    */
    String selectedDate, selectedTime, strTime, strDate, getBabyName, getUserId,
            selectedGendersForCreateBaby, strSelectedImage = "null";

    /*
    Variables for Database
     */
    String image_str;
    InputStream is = null;
    String result = null;
    String line = null;
    int code;
    /*
    Variables For SharedPreferences
     */
    SharedPreferences preferences;
    SharedPreferences.Editor editor;

    private String[] genders = {"MALE", "FEMALE"};
    private Spinner spinnerSelectGender;
    private ArrayAdapter<String> dataAdapterForGender;

    /*
   Variables For BabyPicture Capture
    */
    Intent i;
    final static int cameraData = 0;
    private static final int SELECT_PICTURE = 1;
    private String selectedImagePath;
    public static Uri selectedImageUri = null;
    String getUserIDBabyCreate;
    public static String realPath = "null";


    // Activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    // directory name to store captured images and videos
    private static final String IMAGE_DIRECTORY_NAME = "Hello Camera";
    public static String imageName;

    private Uri fileUri; // file url to store image/video

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_baby);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        initUI();
    }

    private void initUI() {
        // TODO Auto-generated method stub
        /****************************
         * INIT COMPONENTS
         */
        btnDatePicker = (Button) findViewById(R.id.btnDate_createBaby);
        btnTimePicker = (Button) findViewById(R.id.btnTime_createBaby);
        btnCancelCreateBaby = (Button) findViewById(R.id.btnCancel_createBaby);
        btnOkCreateBaby = (Button) findViewById(R.id.btnOk_createBaby);
        edtNameCreateBaby = (EditText) findViewById(R.id.etBabyName_createBaby);
        imgSelectBabyPicture = (ImageView) findViewById(R.id.ivBabyPicture_createBaby);
        spinnerSelectGender = (Spinner) findViewById(R.id.gender_spinner_createBaby);
        btnTheme = (Button) findViewById(R.id.btnTheme_createBaby);

        /****************************
         * Set Click Listener to Buttons
         */
        btnDatePicker.setOnClickListener((OnClickListener) this);
        btnTimePicker.setOnClickListener((OnClickListener) this);
        btnCancelCreateBaby.setOnClickListener((OnClickListener) this);
        btnOkCreateBaby.setOnClickListener((OnClickListener) this);
        imgSelectBabyPicture.setOnClickListener((OnClickListener) this);
        btnTheme.setOnClickListener((OnClickListener) this);
        /****************************
         * Datas For Genders
         */
        dataAdapterForGender = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, genders);
        dataAdapterForGender
                .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerSelectGender.setAdapter(dataAdapterForGender);
        spinnerSelectGender
                .setOnItemSelectedListener(new OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent,
                                               View view, int position, long id) {

                        if (parent.getSelectedItem().toString()
                                .equals(genders[0]))
                            selectedGendersForCreateBaby = "MALE";
                        else if (parent.getSelectedItem().toString()
                                .equals(genders[1]))
                            selectedGendersForCreateBaby = "FEMALE";
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                    }
                });

        /******************
         * Naming The Date Button
         */
        Calendar c_date = Calendar.getInstance();
        SimpleDateFormat date = new SimpleDateFormat("dd/MM/yy");
        strDate = date.format(c_date.getTime());
        btnDatePicker.setText(strDate);

        /******************
         * Naming The Time Button
         */
        Calendar c_time = Calendar.getInstance(TimeZone.getDefault());
        SimpleDateFormat time = new SimpleDateFormat("HH:mm",
                Locale.getDefault());
        strTime = time.format(c_time.getTime());
        btnTimePicker.setText(strTime);

        /******************
         * Getting The Selected Date Value
         */
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
        /******************
         * Get the Selected Time Value
         */
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

    public void setGetBabyName(String getBabyName) {
        this.getBabyName = getBabyName;
    }

    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btnDate_createBaby:
                /******************
                 * Show the Date Picker Dialog
                 */
                new DatePickerDialog(ActivityCreateBaby.this, dateForDB,
                        myCalendar.get(Calendar.YEAR),
                        myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                break;

            case R.id.btnTime_createBaby:
                /******************
                 * Show the Time Picker Dialog
                 */
                new TimePickerDialog(ActivityCreateBaby.this, timeForDB,
                        myCalendar.get(Calendar.HOUR_OF_DAY),
                        myCalendar.get(Calendar.MINUTE), true).show();
                break;
            case R.id.btnCancel_createBaby:
                /******************
                 * Refress all areas
                 */
                edtNameCreateBaby.setText(null);
                Calendar c_date = Calendar.getInstance();
                SimpleDateFormat date = new SimpleDateFormat("dd/MM/yy");
                strDate = date.format(c_date.getTime());
                btnDatePicker.setText(strDate);
                Calendar c_time = Calendar.getInstance(TimeZone.getDefault());
                SimpleDateFormat time = new SimpleDateFormat("HH:mm",
                        Locale.getDefault());
                strTime = time.format(c_time.getTime());
                btnTimePicker.setText(strTime);
                imgSelectBabyPicture.setImageResource(R.drawable.select_picture_icon_128);
                break;

            case R.id.btnOk_createBaby:
                /******************
                 * Get the values on the screen
                 */
                getBabyName = edtNameCreateBaby.getText().toString();
                //Bundle basket = getIntent().getExtras();//nereden gönderiliyor ? ACtivity opening.
                //getUserIDBabyCreate = basket.getString("userid");

                /******************
                 * Check Date and Time Picker Values null or not
                 */
                if (btnDatePicker.getText().equals(strDate)) {
                    selectedDate = btnDatePicker.getText().toString();
                }
                if (btnTimePicker.getText().equals(strTime)) {
                    selectedTime = btnTimePicker.getText().toString();
                }
                /******************
                 * Checked Empty Areas For All Records
                 */
                if (getBabyName.equals("")) {
                    Toast.makeText(getApplicationContext(),
                            R.string.valid_Name, Toast.LENGTH_LONG)
                            .show();
                    return;
                }
                if (selectedGendersForCreateBaby.equals("")) {
                    Toast.makeText(getApplicationContext(),
                            R.string.valid_Gender, Toast.LENGTH_LONG)
                            .show();
                    return;
                } else {
                    /******************
                     * Save the datas to Database
                     */
                    insert();
                }

                break;
            case R.id.ivBabyPicture_createBaby:
                imgSelectBabyPicture.showContextMenu();
                registerForContextMenu(imgSelectBabyPicture);
                openContextMenu(imgSelectBabyPicture);
                unregisterForContextMenu(imgSelectBabyPicture);
                Log.i("sdf", "bas");

                break;

            case R.id.btnTheme_createBaby:
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.select_theme);
                dialog.setTitle("Select Theme");

                // set the custom dialog components - text, image and button
                Button blue = (Button) dialog.findViewById(R.id.blue);
                Button green = (Button) dialog.findViewById(R.id.green);
                Button yellow = (Button) dialog.findViewById(R.id.yellow);
                Button black = (Button) dialog.findViewById(R.id.black);
                Button white = (Button) dialog.findViewById(R.id.white);
                Button gray = (Button) dialog.findViewById(R.id.gray);
                Button orange = (Button) dialog.findViewById(R.id.orange);
                Button pink = (Button) dialog.findViewById(R.id.pink);
                Button purple = (Button) dialog.findViewById(R.id.purple);
                Button cancel = (Button) dialog.findViewById(R.id.cancel);

                blue.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Hepsini birden nasıl değiştiririz onu bul.
                        LinearLayout createBaby = (LinearLayout) findViewById(R.id.layout_Create_Baby);
                        Drawable drawable = getResources().getDrawable(R.drawable.bg_gradient_blue);
                        //Drawable reddrawable=getResources().getDrawable(R.drawable.bg_gradient);
                        //reddrawable=drawable;
                        createBaby.setBackgroundDrawable(drawable);
                        dialog.dismiss();

                    }
                });

                dialog.show();

            default:
                break;
        }
    }

    private void insert() {
        preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());//preferences nesnesi oluşturuluyor ve prefernces referansına bağlanıyor
        editor = preferences.edit(); //aynı şekil editor nesnesi oluşturuluyor
        String strUser_id = preferences.getString("user_id", "");
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

        if (!realPath.equals("null")) {
            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();

            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;
            Bitmap bitmap = BitmapFactory.decodeFile(realPath, options);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, stream); //compress to which format you want.
            byte[] byte_arr = stream.toByteArray();
            image_str = Base64.encodeBytes(byte_arr);
        } else {
            image_str = "null";
        }

        nameValuePairs.add(new BasicNameValuePair("name", getBabyName));
        Log.e("name", getBabyName);
        nameValuePairs.add(new BasicNameValuePair("date", selectedDate));
        Log.e("date", selectedDate);
        nameValuePairs.add(new BasicNameValuePair("time", selectedTime));
        Log.e("time", selectedTime);
        nameValuePairs.add(new BasicNameValuePair("image", image_str));
        Log.e("image", realPath);
        nameValuePairs.add(new BasicNameValuePair("UID", strUser_id));
        Log.e("uid", strUser_id);
        nameValuePairs.add(new BasicNameValuePair("gender", selectedGendersForCreateBaby));
        Log.e("gender", selectedGendersForCreateBaby);
        nameValuePairs.add(new BasicNameValuePair("theme", "Şimdilik Boş"));
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://176.58.88.85/~murat/insert_create_baby.php");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs, "UTF-8"));
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
            Log.e("KontrolÖnceCreateBaby", result);
        } catch (Exception e) {
            Log.e("CreateBabyFail2", e.toString());
        }

        try {
            JSONObject json_data = new JSONObject(result);
            code = json_data.getInt("code");
            Log.e("CreateBabyCode", (String.valueOf(code)));
            /******************
             *  Checked record is inserted or not
             */
            if (code == 1) {
                preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());//preferences nesnesi oluşturuluyor ve prefernces referansına bağlanıyor
                editor = preferences.edit(); //aynı şekil editor nesnesi oluşturuluyor

                // Buraya bak murat
                editor.putString("baby_name", getBabyName); //isim değeri
                Log.d("EditörBabyName", preferences.getString("baby_name", ""));
                editor.putString("user_id", getUserIDBabyCreate);//email değeri
                Log.d("EditörUserId", preferences.getString("user_id", ""));
                //editor.putString("sifre", sifre_string);//şifre değeri
                //editor.putBoolean("login", true);//uygulamaya tekrar girdiğinde kontrol için kullanılcak
                //editor.putInt("sayısalDeger", 1000);// uygulamamızda kullanılmıyor ama göstermek amacıyla

                editor.commit();//yapılan değişiklikler kaydedilmesi için editor nesnesinin commit() metodu çağırılır.
                //Değerlerimizi sharedPreferences a kaydettik.Artık bu bilgiler ile giriş yapabiliriz.
                Toast.makeText(getBaseContext(), R.string.create_succesfully,
                        Toast.LENGTH_SHORT).show();
                Intent intentHomeScreen = new Intent(getApplicationContext(),
                        ActivityHomeScreen.class);
                startActivity(intentHomeScreen);
            }
            /******************"
             *  Chech userName is exist or not
             */
            else if (code == 2) {
                Toast.makeText(getBaseContext(), getBabyName + R.string.have_A_Baby,
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getBaseContext(), R.string.sorry,
                        Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            Log.e("CreateBabyFail3", e.toString());
        } finally {
            Log.e("CreateBabyFinally", (String.valueOf(code)));
        }
    }

    public Uri getSelectedImageUri() {

        return selectedImageUri;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenuInfo menuInfo) {
        // TODO Auto-generated method stub
        super.onCreateContextMenu(menu, v, menuInfo);
        ImageView imgSelectPicture = (ImageView) v
                .findViewById(R.id.ivBabyPicture_createBaby);
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
                // Biraz arastırmamız gerekiyor
                // En son açılan resimler seçilemiyor !!!!!!.....
                Log.i("burdayim", "buragya gelebiliyorum");
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
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

    /**
     * Checking device has camera hardware or not
     */
    private boolean isDeviceSupportCamera() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
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

    public static String getImageName() {
        return imageName.toString();
    }

    public static void setImageName(String s) {
        imageName = s;
    }

    public static String getImagePath() {
        return realPath.toString();
    }

    public static void setImagePath(String s) {
        imageName = s;
    }

    /*
	 * Here we store the file url as it will be null after returning from camera
	 * app
	 */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on scren orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    /*
	 * Creating file uri to store image/video
	 */
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /*
 * Display image from a path to ImageView
 */
    private void previewCapturedImage() {
        try {
            imgSelectBabyPicture.setVisibility(View.VISIBLE);

            // bimatp factory
            BitmapFactory.Options options = new BitmapFactory.Options();

            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            //options.inSampleSize = 8;

            final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                    options);
            realPath = fileUri.getPath();
            MediaStore.Images.Media.insertImage(getContentResolver(), bitmap, getImageName(), "Kontrol");
            imgSelectBabyPicture.setImageBitmap(bitmap);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

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


    @Override
    protected void onActivityResult(int reqCode, int resCode, Intent data) {
        if (reqCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resCode == RESULT_OK) {
                // successfully captured the image
                // display it in image view
                Log.i("Camerayı Okeyledim", "Hahaha");
                previewCapturedImage();
                return;
            } else if (resCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        R.string.cancel_capture, Toast.LENGTH_SHORT)
                        .show();
                return;
            }
        } else {
            if (resCode == Activity.RESULT_OK && data != null) {
                // SDK < API11
                if (Build.VERSION.SDK_INT < 11)
                    realPath = RealPathUtil.getRealPathFromURI_BelowAPI11(this, data.getData());

                    // SDK >= 11 && SDK < 19
                else if (Build.VERSION.SDK_INT < 19)
                    realPath = RealPathUtil.getRealPathFromURI_API11to18(this, data.getData());

                    // SDK > 19 (Android 4.4)
                else if (Build.VERSION.SDK_INT > 19)
                    realPath = RealPathUtil.getRealPathFromURI_API19(this, data.getData());
            } else if (resCode == Activity.RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        R.string.cancel_select, Toast.LENGTH_SHORT)
                        .show();
                return;

            }
        }
        setPath(Build.VERSION.SDK_INT, data.getData().getPath(), realPath);
    }

    private void setPath(int sdk, String uriPath, String realPath) {

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
        imgSelectBabyPicture.setImageBitmap(bitmap);
        Log.d("Bitmap", bitmap.toString());
        Log.d("HMKCODE", "Build.VERSION.SDK_INT:" + sdk);
        Log.d("HMKCODE", "URI Path:" + uriPath);
        Log.d("HMKCODE", "Real Path: " + realPath);
    }
   /*@SuppressLint("NewApi")
    public static String getRealPathFromURI_API19(Context context, Uri uri){
    Log.d("FilePath:","PATH içinceyim");
    String filePath = "";
    String wholeID = DocumentsContract.getDocumentId(uri);
    // Split at colon, use second item in the array
    String id = wholeID.split(":")[1];
    String[] column = { MediaStore.Images.Media.DATA };
    // where id is equal to
    String sel = MediaStore.Images.Media._ID + "=?";
    Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
    column, sel, new String[]{ id }, null);
    int columnIndex = cursor.getColumnIndex(column[0]);
    if (cursor.moveToFirst()) {
    filePath = cursor.getString(columnIndex);
    }
    cursor.close();
    Log.d("FilePath:",filePath);
    return filePath;
    }*/

    /**
     * Displays Toast with RGB values of given color.
     *
     * @param color the color
     */
    private void showToast(int color) {
        String rgbString = "R: " + Color.red(color) + " B: " + Color.blue(color) + " G: " + Color.green(color) + "Yazdı";
        Toast.makeText(this, rgbString, Toast.LENGTH_SHORT).show();
        ActivityCreateBaby.this.findViewById(android.R.id.content)
                .setBackgroundColor(color);

    }
}