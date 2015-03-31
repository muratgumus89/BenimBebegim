package com.example.murat.benimbebegim;

import android.app.FragmentTransaction;
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

import com.example.murat.benimbebegim.adapters.TitleNavigationAdapter;
import com.example.murat.benimbebegim.model.SpinnerNavItem;

import java.util.ArrayList;

public class ActivityHomeScreen extends FragmentActivity implements
        ActionBar.OnNavigationListener,ActionBar.TabListener {
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
    private MenuItem mSpinnerItem = null;

    // Title navigation Spinner data
    private ArrayList<SpinnerNavItem> navSpinner;

    // Navigation adapter
    private TitleNavigationAdapter adapter;

    @Override
    protected void onCreate( Bundle savedInstanceState )
    {
        Log.d("Spinner","Çok yaşa Spin6");
        super.onCreate( savedInstanceState );
        setContentView(R.layout.activity_home_screen);

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
        if(getActionBar().getNavigationMode() == ActionBar.NAVIGATION_MODE_LIST) {
            Log.d("Spinner", "Çok yaşa SpinMODELIST");
            getMenuInflater().inflate(R.menu.spinner, menu);
            mSpinnerItem = menu.findItem(R.id.menu_spinner);
            populateSpinner(mSpinnerItem);
        }else{
            Log.d("Spinner", "Çok yaşa SpinModeTAB");
            getMenuInflater().inflate(R.menu.spinner, menu);
            mSpinnerItem = menu.findItem(R.id.menu_spinner);
            setupSpinner(mSpinnerItem);
        }
        return  true;


    }
    private void populateSpinner(MenuItem item){
        // Spinner title navigation data
        //getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_LIST);
        View view = item.getActionView();
        if (view instanceof Spinner)
        {
            navSpinner = new ArrayList<SpinnerNavItem>();
            navSpinner.add(new SpinnerNavItem("Local", R.drawable.icon_calendar_32));
            navSpinner
                    .add(new SpinnerNavItem("My Places", R.drawable.icon_mood_32));
            navSpinner.add(new SpinnerNavItem("Checkins", R.drawable.icon_photos_32));
            navSpinner.add(new SpinnerNavItem("Latitude", R.drawable.icon_help_32));
            navSpinner.add(new SpinnerNavItem("                                                              ", R.drawable.icon_help_32));


            // title drop down adapter
            adapter = new TitleNavigationAdapter(getApplicationContext(),
                    navSpinner);
            Log.d("Spinner","Çok yaşa Spin10");
            Spinner spinner = (Spinner) view;
            spinner.setAdapter(adapter);
        }

        // assigning the spinner navigation
       // getActionBar().setListNavigationCallbacks(adapter, this);
        // Changing the action bar icon
        // actionBar.setIcon(R.drawable.ico_actionbar);
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
            navSpinner.add(new SpinnerNavItem("Local", R.drawable.icon_calendar_32));
            navSpinner
                    .add(new SpinnerNavItem("My Places", R.drawable.icon_mood_32));
            navSpinner.add(new SpinnerNavItem("Checkins", R.drawable.icon_photos_32));
            navSpinner.add(new SpinnerNavItem("Latitude", R.drawable.icon_help_32));
            navSpinner.add(new SpinnerNavItem("                                                              ", R.drawable.icon_help_32));


            // title drop down adapter
            adapter = new TitleNavigationAdapter(getApplicationContext(),
                    navSpinner);
            Log.d("Spinner","Çok yaşa Spin10");
            Spinner spinner = (Spinner) view;
            spinner.setAdapter(adapter);
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
        return false;
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
}
