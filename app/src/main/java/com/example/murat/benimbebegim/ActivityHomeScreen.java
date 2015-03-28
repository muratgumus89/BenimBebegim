package com.example.murat.benimbebegim;;

import android.content.res.Configuration;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.widget.Toast;


public class ActivityHomeScreen extends FragmentActivity {

    private static final String TAG = ActivityHomeScreen.class.getSimpleName();

    // Sol Slider için Yapılmış özel layout android.support.v4 ün içinde
    private DrawerLayout mDrawerLayout;

    // Sol Slider Açıldığında Görünecek ListView
    private ListView mDrawerList;

    // Navigation Drawer nesnesini ActionBar'da gösterir.
    private ActionBarDrawerToggle mDrawerToggle;

    // ActionBar'ın titlesi dinamik olarak değişecek draweri açıp kapattıkça
    private String mTitle = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        mTitle = "Benim Bebeğim";
        getActionBar().setTitle(mTitle);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        mDrawerList = (ListView) findViewById(R.id.drawer_list);

        addDrawerItems();
        setupDrawer();


        // actionbar home butonunu aktif ediyoruz
        getActionBar().setHomeButtonEnabled(true);

        // navigationu tıklanabilir hale getiriyoruz
        getActionBar().setDisplayHomeAsUpEnabled(true);

        // Açılıp kapanmayı dinlemek için register
        mDrawerLayout.setDrawerListener(mDrawerToggle);
        if(savedInstanceState == null) {
            navigateTo(0);
        }

    }

    @Override
    public void onBackPressed() {
        //super.onBackPressed();
        Toast.makeText(this, "Back button Pressed" , Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        mDrawerToggle.syncState();
    }
    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.settings, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        /*//draweri sadece swipe ederek açma yerine sol tepedeki butona basarak açmak için
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);*/

        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            finish();
            System.exit(0);
            return true;
        }

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        boolean drawerOpen = mDrawerLayout.isDrawerOpen(mDrawerList);
        // navigationDrawer açıldığında ayarları gizlemek için
        menu.findItem(R.id.action_settings).setVisible(!drawerOpen);
        return super.onPrepareOptionsMenu(menu);
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

}
