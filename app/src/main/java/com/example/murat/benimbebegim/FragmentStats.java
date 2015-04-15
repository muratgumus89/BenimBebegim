package com.example.murat.benimbebegim;


import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.SimpleAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FragmentStats extends Fragment {
    public static final String TAG = FragmentStats.class.getSimpleName();
    private static ActionBar actionBar;
    public static FragmentStats newInstance() {
        return new FragmentStats();
    }

    //@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		// TODO Auto-generated method stub
        Log.d("Spinner","Çok yaşa Spin4");
		View view = inflater.inflate(R.layout.layout_statsfragment, container,false);
        getActivity().getActionBar().setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
		return view;
	}
}

