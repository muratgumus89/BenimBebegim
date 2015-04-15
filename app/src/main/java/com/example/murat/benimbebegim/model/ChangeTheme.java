package com.example.murat.benimbebegim.model;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.example.murat.benimbebegim.R;

/**
 * Created by Murat on 14.4.2015.
 */
public class ChangeTheme extends Activity {
    private String theme;
    public ChangeTheme(String theme){
        this.theme = theme;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(theme=="Blue")
        setTheme(R.style.My_Custom_Theme_Blue);
    }
}
