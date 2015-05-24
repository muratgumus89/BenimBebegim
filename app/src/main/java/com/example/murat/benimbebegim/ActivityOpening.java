package com.example.murat.benimbebegim;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Message;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Locale;

import android.os.Handler;


/**
 * Created by aytunc on 16.2.2015.
 */
public class ActivityOpening extends Activity implements View.OnClickListener {
    /*
     Variables For Xml Components
    */
    TextView txtSign;
    EditText etUserName, etPassword;
    Button btnLogin;
    ImageButton btnTurkish,btnEnglish;
    String userNameforLogin, passwordforLogin;
    CheckBox cbRememberMe;
    /*
    Variables For MySql Connections
    */
    InputStream is = null;
    String result = null;
    String strUserIDOpening;
    String line = null;
    int code;
    String strLocale ="";
    //For RememberMe
    public static final String PREFS_NAME = "MyPrefsFile";
    private static final String PREF_USERNAME = "username";
    private static final String PREF_PASSWORD = "password";
    private static final String PREF_USERID = "user_id";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        SharedPreferences pref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        if(pref.getString("locale", null)!= null)
        strLocale = pref.getString("locale", null);
        Locale local = new Locale(strLocale);
        Locale.setDefault(local);
        Configuration config = new Configuration();
        config.locale = local;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
/*        finish();//mevcut acivity i bitir.
        startActivity(getIntent());//activity i baştan yükle*/
        setContentView(R.layout.activity_activity_opening);
        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }
        initUI();
    }
    private void initUI() {

        SharedPreferences pref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        /*strLocale = pref.getString("locale", null);
        Locale local = new Locale(strLocale);
        Locale.setDefault(local);
        Configuration config = new Configuration();
        config.locale = local;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
        finish();//mevcut acivity i bitir.
        startActivity(getIntent());//activity i baştan yükle
*/
        txtSign = (TextView) findViewById(R.id.txtSign_Opening);
        txtSign.setOnClickListener(this);
        etUserName = (EditText) findViewById(R.id.edtEmail_Opening);
        etPassword = (EditText) findViewById(R.id.edtPassword_Opening);
        btnLogin = (Button) findViewById(R.id.btnLogin_Opening);
        btnLogin.setOnClickListener(this);
        btnTurkish = (ImageButton)findViewById(R.id.turkish_lang_Opening);
        btnEnglish = (ImageButton)findViewById(R.id.english_lang_Opening);
        btnTurkish.setOnClickListener(this);
        btnEnglish.setOnClickListener(this);
        cbRememberMe = (CheckBox) findViewById(R.id.check_Remember_Me_Opening);
        cbRememberMe.setOnClickListener(this);
        /*
         Save Remember User or Not
         */
        String username = pref.getString(PREF_USERNAME, null);
        String password = pref.getString(PREF_PASSWORD, null);
        boolean b = pref.getBoolean("remembers", false);
        if (b == true) {
            etUserName.setText(username);
            etPassword.setText(password);
            cbRememberMe.setChecked(true);
        }
    }

    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.txtSign_Opening:
                /*****************************
                 * Signup Button
                 */
                Intent intentSignUP = new Intent(getApplicationContext(),
                        ActivitySignUp.class);
                startActivity(intentSignUP);
                break;
            case R.id.btnLogin_Opening:
                userNameforLogin = etUserName.getText().toString();
                passwordforLogin = etPassword.getText().toString();
                if (userNameforLogin.equals("")) {
                    Toast.makeText(getApplicationContext(), R.string.valid_User_name, Toast.LENGTH_LONG).show();
                    return;
                }
                else if(passwordforLogin.equals("")) {
                    Toast.makeText(getApplicationContext(), R.string.valid_Password, Toast.LENGTH_LONG).show();
                    return;
                }else
                {
                    loginControl();
                }
                break;
            case R.id.english_lang_Opening:

                        Locale locale = new Locale("en");  //locale en yaptık. Artık değişkenler values-en paketinden alınacak
                        Locale.setDefault(locale);
                        getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                        .edit()
                        .putString("locale", "en")
                        .commit();
                        Configuration config = new Configuration();
                        config.locale = locale;
                        getBaseContext().getResources().updateConfiguration(config,
                                getBaseContext().getResources().getDisplayMetrics());
                        finish();//mevcut acivity i bitir.
                        startActivity(getIntent());//activity i baştan yükle
                break;
            case R.id.turkish_lang_Opening:
                Locale locale2 = new Locale(""); //locale i default locale yani türkçe yaptık. Artık değişkenler values paketinden alınacak
                Locale.setDefault(locale2);
                getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                        .edit()
                        .putString("locale","")
                        .commit();
                Configuration config2 = new Configuration();
                config2.locale = locale2;
                getBaseContext().getResources().updateConfiguration(config2,
                        getBaseContext().getResources().getDisplayMetrics());
                finish();//mevcut acivity i bitir.
                startActivity(getIntent());//activity i baştan yükle
            default:
                break;
        }
    }
    private void loginControl() {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

        nameValuePairs.add(new BasicNameValuePair("user_name", userNameforLogin));
        nameValuePairs.add(new BasicNameValuePair("password", passwordforLogin));

        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://176.58.88.85/~murat/login.php");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            Log.e("LoginButtonCon", "connection success ");
        } catch (Exception e) {
            Log.e("LoginButtonFail", e.toString());
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
            Log.e("LoginButtonFail2", e.toString());
        }

        try {
            JSONObject json_data = new JSONObject(result);
            code = json_data.getInt("code");
            Log.e("LoginBottonContentCode", (String.valueOf(code)));
            /******************
             *  Checked record is inserted or not
             */
            if (code == 2) {
                Toast.makeText(getBaseContext(), R.string.wrong_User_Name,
                        Toast.LENGTH_SHORT).show();
            }
            /******************
             *  Chech userName is exist or not
             */
            else if (code == 1) {
                Toast.makeText(getBaseContext(),R.string.wrong_Password,
                        Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getBaseContext(), R.string.login_succesfully,
                        Toast.LENGTH_SHORT).show();
                strUserIDOpening = getUserId(userNameforLogin);
                getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                        .edit()
                        .putBoolean("isSpinner",false)
                        .commit();
                getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                        .edit()
                        .putString(PREF_USERID, strUserIDOpening)
                        .commit();
                if (cbRememberMe.isChecked()) {
                    getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                            .edit()
                            .putString(PREF_USERNAME, userNameforLogin)
                            .putString(PREF_PASSWORD, passwordforLogin)
                            .putBoolean("remembers", true)
                            .commit();
                } else {
                    getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                            .edit()
                            .putString(PREF_USERNAME, "")
                            .putString(PREF_PASSWORD, "")
                            .putBoolean("remembers", false)
                            .commit();
                }
                new BackgroundTask().execute();
            }
        } catch (Exception e) {
            Log.e("LoginButtonFail 3", e.toString());
        } finally {
            Log.e("LoginButtonFinally", (String.valueOf(code)));
        }
    }
    private void babyControl() {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        strUserIDOpening = getUserId(userNameforLogin);
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
                        .putString(PREF_USERID, strUserIDOpening)
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

    private String getUserId(String userNameforLogin){
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

        nameValuePairs.add(new BasicNameValuePair("email",userNameforLogin));
        nameValuePairs.add(new BasicNameValuePair("user_name",userNameforLogin));
        try
        {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://176.58.88.85/~murat/get_user_id.php");
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs,"UTF-8"));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
            Log.e("UserIdCon", "connection success ");
        }
        catch(Exception e)
        {
            Log.e("UserIdFail", e.toString());
            Toast.makeText(getApplicationContext(), "Invalid IP Address",
                    Toast.LENGTH_LONG).show();
        }

        try
        {
            BufferedReader reader = new BufferedReader
                    (new InputStreamReader(is,"utf-8"),8);
            StringBuilder sb = new StringBuilder();
            while ((line = reader.readLine()) != null)
            {
                sb.append(line);
            }
            is.close();
            result = sb.toString();
            Log.e("Opening - User ID: ", result);
        }
        catch(Exception e)
        {
            Log.e("Fail 2", e.toString());
        }
        return result;
    }

    public class BackgroundTask extends AsyncTask<Void, Integer, Void> {
        Dialog dialog = MyDialog.displayProgress(ActivityOpening.this);
        //ProgressDialog pd = new ProgressDialog(ActivityOpening.this);
        @Override
        protected void onPreExecute() {
            //AsyncTask'ın ilk çalışan metodu.
            //Burada Progressdialogun mes set ediyoruz ve show diyerek gösteriyoruz
/*            pd.setMessage("Loading ...");
            pd.show();*/

            dialog.show();
            super.onPreExecute();

        }

        @Override
        protected Void doInBackground(Void... params) {
            babyControl();
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            //En son çalışan fonskiyonumuz Tüm işlemler
            //bittikten sonra çalışıyor ve Progress dialogu kapatıyor
            //pd.dismiss();
            dialog.dismiss();
            super.onPostExecute(result);
        }

        @Override
        protected void onCancelled(Void result) {
            super.onCancelled(result);

        }
    }
}