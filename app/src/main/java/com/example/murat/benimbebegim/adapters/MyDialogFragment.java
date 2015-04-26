package com.example.murat.benimbebegim.adapters;


import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.murat.benimbebegim.R;

/**
 * Created by Murat on 15.4.2015.
 */
public class MyDialogFragment extends DialogFragment implements View.OnClickListener {
    public static MyDialogFragment newInstance() {
        return new MyDialogFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.select_theme, container, false);
        getDialog().setTitle("Select Theme");
        // set the custom dialog components - text, image and button
        Button blue = (Button) v.findViewById(R.id.blue);
        blue.setOnClickListener(this);
        return v;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.blue:
                Log.d("Blue", "Blue");
                getActivity().setTheme(R.style.My_Custom_Theme_Blue);
                break;

        }
    }
}
