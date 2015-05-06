package com.example.murat.benimbebegim;

import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.MenuItem;
;
import android.app.ActionBar;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.example.murat.benimbebegim.adapters.TitleNavigationAdapter;
import com.example.murat.benimbebegim.model.ChangeTheme;
import com.example.murat.benimbebegim.model.SpinnerNavItem;

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

public class ActivityHomeScreen extends FragmentActivity implements
        ActionBar.OnNavigationListener,ActionBar.TabListener, AdapterView.OnItemSelectedListener {
    private static final String TAG = ActivityHomeScreen.class.getSimpleName();
    Spinner spinner;

    // Sol Slider için Yapılmış özel layout android.support.v4 ün içinde
    private DrawerLayout mDrawerLayout;

    // Sol Slider Açıldığında Görünecek ListView
    private ListView mDrawerList;

    // Navigation Drawer nesnesini ActionBar'da gösterir.
    private ActionBarDrawerToggle mDrawerToggle;

    // ActionBar'ın titlesi dinamik olarak değişecek draweri açıp kapattıkça
    private String mTitle = "";
    private int spinnerPosition = 2;
    private MenuItem mSpinnerItem = null;

    // Title navigation Spinner data
    private ArrayList<SpinnerNavItem> navSpinner;

    // Navigation adapter
    private TitleNavigationAdapter adapter;

    private Menu menu;
    /*
 Variables For Shared Preferences
 */
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    /*
 Variables For MySql Connections
*/
    String image_str;
    InputStream is=null;
    String result=null;
    String line=null;
    String realPath="null";
    int code;
    public static final String PREFS_NAME = "MyPrefsFile";
    String baby_id,user_id,baby_name,userName;
    String[] nameArray;
    private String[] imageArray;
    private Boolean isSpinner;
    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        Log.d("Spinner","Çok yaşa Spin6");
        super.onCreate( savedInstanceState );
        setContentView(R.layout.activity_home_screen);

        SharedPreferences pref = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        userName = pref.getString("username", null);
        baby_name = pref.getString("baby_name", null);
        user_id = pref.getString("user_id", null);
        baby_id = pref.getString("baby_id", null);
        isSpinner=pref.getBoolean("isSpinner",false);
        if(isSpinner==false){
            getBabiesNames();
            isSpinner=true;
        }
        if(pref.getInt("spin_position",0) != 0 && pref.getInt("spin_position",0) != 1){

            spinnerPosition=pref.getInt("spin_position",0);
        }

        mTitle = "Benim Bebeğim";
        getActionBar().setTitle(mTitle);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        mDrawerList = (ListView) findViewById(R.id.drawer_list);

        addDrawerItems();
        setupDrawer();

        // actionbar home butonunu aktif ediyoruz
        getActionBar().setHomeButtonEnabled(true);
        getActionBar().setDisplayShowTitleEnabled(false);

        // navigationu tıklanabilir hale getiriyoruz
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // Açılıp kapanmayı dinlemek için register
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        if(savedInstanceState == null) {
            navigateTo(0);
        }
        mDrawerList.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                navigateTo(position);

                // draweri kapat
                mDrawerLayout.closeDrawer(mDrawerList);

            }
        });
    }

    @Override
    protected void onSaveInstanceState( Bundle outState )
    {
        outState.putInt( "mode", getActionBar().getNavigationMode() );
        super.onSaveInstanceState( outState );
    }

    @Override
    public boolean onCreateOptionsMenu( Menu menu )
    {
        getMenuInflater().inflate(R.menu.main, menu);
        if(getActionBar().getNavigationMode() == ActionBar.NAVIGATION_MODE_STANDARD) {
            Log.d("Spinner", "Çok yaşa SpinMODELIST");
            getMenuInflater().inflate(R.menu.spinner, menu);
            mSpinnerItem = menu.findItem(R.id.menu_spinner);
            setupSpinner(mSpinnerItem);
        }else{
            Log.d("Spinner", "Çok yaşa SpinModeTAB");
            getMenuInflater().inflate(R.menu.spinner, menu);
            mSpinnerItem = menu.findItem(R.id.menu_spinner);
            setupSpinner(mSpinnerItem);
        }
        this.menu = menu;
        return  true;


    }
    private void addDrawerItems() {
        // Navigationdaki Drawer için listview adapteri
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getBaseContext(),
                R.layout.drawer_list_item, getResources().getStringArray(R.array.menu));

        // adapteri listviewe set ediyoruz
        mDrawerList.setAdapter(adapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                navigateTo(position);

                // draweri kapat
                mDrawerLayout.closeDrawer(mDrawerList);
            }
        });
    }
    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {


            // drawer kapatıldığında tetiklenen method
            public void onDrawerClosed(View view) {
                getActionBar().setTitle(mTitle);
                invalidateOptionsMenu();

            }

            // drawer açıldığında tetiklenen method
            public void onDrawerOpened(View drawerView) {
                getActionBar().setTitle("Benim Bebeğim");
                invalidateOptionsMenu();
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
    }
    private void setupSpinner( MenuItem item)
    {
        if(getActionBar().getNavigationMode()== ActionBar.NAVIGATION_MODE_TABS) {
            Log.d("Spinner", "Çok yaşa SpinTAB");
            item.setVisible(getActionBar().getNavigationMode() == ActionBar.NAVIGATION_MODE_TABS);
        }
        View view = item.getActionView();
        if (view instanceof Spinner)
        {
            navSpinner = new ArrayList<SpinnerNavItem>();
            navSpinner.add(new SpinnerNavItem(getResources().getString(R.string.add_baby)+"                         ", getResources().getDrawable(R.drawable.icon_add_baby_32)));
            navSpinner.add(new SpinnerNavItem(getResources().getString(R.string.edit_baby),getResources().getDrawable(R.drawable.edit_baby_info_32)));
            for(int i=0;i<nameArray.length;i++){
                Bitmap image=StringToBitMap(imageArray[i]);
                if(imageArray[i]!=(null)) {
                    Bitmap ic_image=image;
                    Drawable d = new BitmapDrawable(getResources(),ic_image);
                    navSpinner.add(new SpinnerNavItem(nameArray[i],d));
                }
                else{
                    navSpinner.add(new SpinnerNavItem(nameArray[i],getResources().getDrawable(R.drawable.baby_boy_icon)));
                }

            }
            getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                    .edit()
                    .putBoolean("isSpinner",isSpinner)
                    .commit();
            // title drop down adapter
            new TitleNavigationAdapter(baby_name);
            adapter = new TitleNavigationAdapter(getApplicationContext(),
                    navSpinner);
            Log.d("Spinner","Çok yaşa Spin10");
            Spinner spinner = (Spinner) view;
            spinner.setAdapter(adapter);
            spinner.setSelection(spinnerPosition);

            spinner.setOnItemSelectedListener(this);
        }
    }
    private void navigateTo(int position) {
        Log.v(TAG, "List View Item: " + position);

        switch(position) {
            case 0:
			/*getSupportFragmentManager()
				.beginTransaction()
				.add(R.id.content_frame,
						ItemOne.newInstance(),
						ItemOne.TAG).commit();*/
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame, FragmentHome.newInstance(), FragmentHome.TAG).commit();
                break;
            case 1:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame,
                                FragmentHistory.newInstance(),
                                FragmentHistory.TAG).commit();
                break;
            case 2:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame,
                                FragmentStats.newInstance(),
                                FragmentStats.TAG).commit();
                break;
            case 3:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame,
                                FragmentDiary.newInstance(),
                                FragmentDiary.TAG).commit();
                break;
            case 4:
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.content_frame,
                                FragmentGrowth.newInstance(),
                                FragmentGrowth.TAG).commit();
                break;
        }
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //draweri sadece swipe ederek açma yerine sol tepedeki butona basarak açmak için
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        // navigationDrawer açıldığında ayarları gizlemek için
        //menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
    }


    @Override
    public boolean onNavigationItemSelected(int itemPosition, long itemId) {
        return true;
    }

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int index = parent.getSelectedItemPosition();
        switch (index) {
            case 0:
                Intent goCreateBaby = new Intent(getApplicationContext(),
                        ActivityCreateBaby.class);
                startActivity(goCreateBaby);
                break;
            case 1:
                Intent goEditBaby = new Intent(getApplicationContext(),
                        ActivityEditBaby.class);
                startActivity(goEditBaby);
                break;
            default:
                baby_name=navSpinner.get(position).getTitle();
                String baby_id=getBabyID(user_id);
                getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                        .edit()
                        .putString("baby_name", navSpinner.get(position).getTitle())
                        .putString("baby_id",baby_id)
                        .putInt("spin_position",position)
                        .commit();
                break;
        }
    }
    private void getBabiesNames() {
        nameArray = new String[0];
        imageArray = new String[0];
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

        nameValuePairs.add(new BasicNameValuePair("uid",user_id ));


        try {

            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost("http://176.58.88.85/~murat/get_baby_name.php");
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
            //JSONObject jsonResponse = new JSONObject(result);
            //JSONArray jArray = jsonResponse.optJSONArray("babies");
            JSONArray jArray = new JSONArray(result);
            nameArray=new String[jArray.length()];
            imageArray=new String[jArray.length()];
            for(int i=0;i<jArray.length();i++){
                JSONObject json_data = jArray.getJSONObject(i);
                Log.d("Name: ",json_data.getString("Name"));
                Log.d("Image: ",json_data.getString("Image"));
                nameArray[i]=json_data.getString("Name");
                imageArray[i]=json_data.getString("Image");
                realPath=json_data.getString("Image");
            }
            Log.d("log_tag", "parse json data completed!");
        } catch (Exception e) {
            Log.d("log_tag", "Error in http connection " + e.toString());
        } finally {
            Log.d("log_tag", "ALL completed!");
        }
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
    private String getBabyID(String strUserIDOpening)  {
        ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
        nameValuePairs.add(new BasicNameValuePair("uid", strUserIDOpening));
        nameValuePairs.add(new BasicNameValuePair("baby_name",baby_name));
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
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
            Log.e("Babyresult", result);
        } catch (Exception e) {
            Log.e("BabyFail 2", e.toString());
        }
        return result;
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
