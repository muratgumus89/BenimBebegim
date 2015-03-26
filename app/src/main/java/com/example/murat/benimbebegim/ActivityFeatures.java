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


public class ActivityFeatures extends Fragment implements View.OnClickListener {
    TextView txtTheme;
    final Context context = this.context;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        Log.e("Deneme:","Aytunç is a sikko");
        View view = inflater.inflate(R.layout.activity_features, container, false);
        txtTheme = (TextView) view.findViewById(R.id.txtTheme_Home_Screen);
        txtTheme.setOnClickListener(this);
        return view;
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.txtTheme_Home_Screen:
                Log.e("Deneme:","Aytunç is a sik");
                Intent intentHomeScreen = new Intent(getActivity().getApplicationContext(),
                        ActivityAlarm.class);
                startActivity(intentHomeScreen);
                break;
            default:
                break;
        }
    }
}
