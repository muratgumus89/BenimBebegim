package com.example.murat.benimbebegim;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import com.example.murat.benimbebegim.adapters.MyDialogFragment;

public class ActivityFeatures extends Fragment implements View.OnClickListener {
    TextView txtTheme,txtCalendar, txtReminder;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        View view = inflater.inflate(R.layout.activity_features, container, false);
        txtTheme = (TextView) view.findViewById(R.id.txtTheme_Home_Screen);
        txtTheme.setOnClickListener(this);
        txtCalendar = (TextView) view.findViewById(R.id.txtCalendar_Home_Screen);
        txtCalendar.setOnClickListener(this);
        txtReminder = (TextView) view.findViewById(R.id.txtReminder_Home_Screen);
        txtReminder.setOnClickListener(this);
        return view;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtTheme_Home_Screen:
                // Create the fragment and show it as a dialog.
                DialogFragment newFragment = MyDialogFragment.newInstance();
                newFragment.show(getFragmentManager(), "dialog");
                break;
            case R.id.txtCalendar_Home_Screen:
                break;
            case R.id.txtReminder_Home_Screen:
                Intent intentRemindME = new Intent(getActivity().getApplicationContext(),
                       ActivityAlarm.class);
                startActivity(intentRemindME);
                break;
            default:
                break;
        }
    }
}
