package com.example.murat.benimbebegim;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;

import com.example.murat.benimbebegim.adapters.EarlierLayoutAdapter;
import com.example.murat.benimbebegim.stickHeader.StickyListHeadersListView;


/**
 * Created by Murat on 9.3.2015.
 */
public class ActivityEarlier extends Fragment  {



    public static ActivityEarlier newInstance() {
        return new ActivityEarlier();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.activity_earlier, container,false);


        return view;
    }
}
