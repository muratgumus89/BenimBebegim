/*
package com.example.murat.benimbebegim;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SpinnerDropDownListMode extends Activity {
    private MenuItem mSpinnerItem = null;
    int mode=ActionBar.NAVIGATION_MODE_LIST;
    ActionBar ab;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ab = getActionBar();
        ab.setDisplayShowTitleEnabled(true);
        setListNavigation(ab);
        FragmentStats fStats=FragmentStats.newInstance();
        fStats.setMyActionBar(getActionBar());
    }
    @Override
    public boolean onCreateOptionsMenu( Menu menu )
    {
        getMenuInflater().inflate( R.menu.settings, menu );
        mSpinnerItem = menu.findItem( R.id.menu_spinner );
        setupSpinner( mSpinnerItem );
        return true;
    }

    public void setListNavigation( ActionBar actionBar )
    {
        actionBar.setNavigationMode( ActionBar.NAVIGATION_MODE_LIST );
        final List<Map<String, Object>> data = new ArrayList<Map<String, Object>>();
        SimpleAdapter adapter = new SimpleAdapter( this, data,
                android.R.layout.simple_spinner_dropdown_item,
                new String[] { "title" }, new int[] { android.R.id.text1 } );
        if (mSpinnerItem != null)
        {
            setupSpinner( mSpinnerItem );
        }
    }
    private void setupSpinner( MenuItem item )
    {
        item.setVisible( getActionBar().getNavigationMode() == ActionBar.NAVIGATION_MODE_LIST );
        item.setVisible( getActionBar().getNavigationMode() == ActionBar.NAVIGATION_MODE_TABS );
        View view = item.getActionView();
        if (view instanceof Spinner)
        {
            Spinner spinner = (Spinner) view;
            spinner.setAdapter( ArrayAdapter.createFromResource(this,
                    R.array.spinner_data,
                    android.R.layout.simple_spinner_dropdown_item) );
        }
    }
}
*/
