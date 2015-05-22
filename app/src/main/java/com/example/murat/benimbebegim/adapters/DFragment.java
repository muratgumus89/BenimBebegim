package com.example.murat.benimbebegim.adapters;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.murat.benimbebegim.R;

public class DFragment extends DialogFragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.select_theme, container,
                false);
        getDialog().setTitle("DialogFragment Tutorial");
        // Do something else
        return rootView;
        //Murat Gümüş
    }
}