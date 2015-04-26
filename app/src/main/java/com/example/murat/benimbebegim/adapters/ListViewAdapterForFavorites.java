package com.example.murat.benimbebegim.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.murat.benimbebegim.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

public class ListViewAdapterForFavorites extends BaseAdapter {

	// Declare Variables
	Context context;
	String mood;
    String time;
    String clickAdd;
    String[] favName;
	int[] upLogo;
	LayoutInflater inflater;

	public ListViewAdapterForFavorites(Context context, String[] favName, int[] upLogo, String time, String mood,String clickAdd) {
		this.context = context;
		this.favName = favName;
		this.upLogo  = upLogo;
        this.time    = time;
        this.mood    = mood;
        this.clickAdd= clickAdd;
	}

	public int getCount() {
		return favName.length;
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		// Declare Variables
		TextView txtfavName;
        TextView txtTime;
        TextView txtMood;
        TextView txtClick;
		ImageView imgLogo;

		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View itemView = inflater.inflate(R.layout.listview_item, parent, false);

		// Locate the TextViews in listview_item.xml
		txtfavName = (TextView) itemView.findViewById(R.id.fav_name);
		// Locate the ImageView in listview_item.xml
		imgLogo = (ImageView) itemView.findViewById(R.id.logo);

        // Locate the TextViews in listview_item.xml
        txtTime = (TextView) itemView.findViewById(R.id.show_time);
        // Locate the ImageView in listview_item.xml
        txtMood = (TextView) itemView.findViewById(R.id.show_mood_name);
        txtClick= (TextView) itemView.findViewById(R.id.click_add);
		// Capture position and set to the TextViews
		txtfavName.setText(favName[position]);
		// Capture position and set to the ImageView
		imgLogo.setImageResource(upLogo[position]);
        if(position==0) {
            if(time.equals(" "))
            txtTime.setText(time);
            else
            txtTime.setText(time + ", ");
            txtMood.setText(mood);
            if(!clickAdd.equals(" "))
            txtClick.setText(clickAdd);
        }
        if(position==1) {
            if(time.equals(" "))
                txtTime.setText(time);
            else
                txtTime.setText(time + ", ");
                txtMood.setText(mood);
            if(!clickAdd.equals(" "))
                txtClick.setText(clickAdd);
        }
		return itemView;
	}
}
