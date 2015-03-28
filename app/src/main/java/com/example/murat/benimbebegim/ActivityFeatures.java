package com.example.murat.benimbebegim;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;


public class ActivityFeatures extends Fragment implements View.OnClickListener {
    TextView txtTheme, txtSettings, txtCalender, txtPhotos, txtReminders, txtHelp;
    final Context context = this.context;
    Intent intentHomeScreen;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        Log.e("Deneme:","Aytunç is a sikko");
        View view = inflater.inflate(R.layout.activity_features, container, false);
        txtTheme = (TextView) view.findViewById(R.id.txtTheme_Home_Screen);
        txtSettings = (TextView)view.findViewById(R.id.txtSettings_Home_Screen);
        txtCalender = (TextView)view.findViewById(R.id.txtCalendar_Home_Screen);
        txtPhotos = (TextView)view.findViewById(R.id.txtPhotos_Home_Screen);
        txtReminders = (TextView)view.findViewById(R.id.txtReminder_Home_Screen);
        txtHelp = (TextView)view.findViewById(R.id.txtHelp_Home_Screen);
        txtTheme.setOnClickListener(this);
        txtSettings.setOnClickListener(this);
        txtCalender.setOnClickListener(this);
        txtPhotos.setOnClickListener(this);
        txtReminders.setOnClickListener(this);
        txtHelp.setOnClickListener(this);

        return view;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtTheme_Home_Screen:
                Toast.makeText(getActivity(), "Theme pressed" , Toast.LENGTH_SHORT).show();
                break;
            case R.id.txtSettings_Home_Screen:
                Toast.makeText(getActivity(), "Settings pressed" , Toast.LENGTH_SHORT).show();
                break;
            case R.id.txtCalendar_Home_Screen:
                Toast.makeText(getActivity(), "Calendar pressed" , Toast.LENGTH_SHORT).show();
                break;
            case R.id.txtPhotos_Home_Screen:
                Toast.makeText(getActivity(), "Photos pressed" , Toast.LENGTH_SHORT).show();
                break;
            case R.id.txtReminder_Home_Screen:
                intentHomeScreen = new Intent(getActivity().getApplicationContext(),
                        ActivityAlarm.class);
                startActivity(intentHomeScreen);
                break;
            case R.id.txtHelp_Home_Screen:
                Toast.makeText(getActivity(), "Home pressed" , Toast.LENGTH_SHORT).show();
                break;
            default:
                break;
        }
    }
}
