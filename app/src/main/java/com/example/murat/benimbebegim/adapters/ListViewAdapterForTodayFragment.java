package com.example.murat.benimbebegim.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.murat.benimbebegim.R;

public class ListViewAdapterForTodayFragment extends BaseAdapter {

	// Declare Variables
	Context context;
	String[] mood;
    String[] time;
    int count;
	int upLogo;
    String[] info;
	LayoutInflater inflater;

	public ListViewAdapterForTodayFragment(Context context, int upLogo, String[] time, String[] mood,String[] info,int count) {
		this.context = context;
		this.upLogo  = upLogo;
        this.time    = time;
        this.mood    = mood;
        this.info    = info;
        this.count   = count;
	}

	public int getCount() {
		return count;
	}

	public Object getItem(int position) {
		return null;
	}

	public long getItemId(int position) {
		return 0;
	}

	public View getView(int position, View convertView, ViewGroup parent) {

		// Declare Variables
        TextView txtTime;
        TextView txtMood;
        TextView txtInfo;
		ImageView imgLogo;

		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View itemView = inflater.inflate(R.layout.list_view_today, parent, false);

		// Locate the ImageView in listview_item.xml
		imgLogo = (ImageView) itemView.findViewById(R.id.logo);

        // Locate the TextViews in listview_item.xml
        txtTime = (TextView) itemView.findViewById(R.id.show_time);
        // Locate the ImageView in listview_item.xml
        txtMood = (TextView) itemView.findViewById(R.id.show_mood_name);
        txtInfo= (TextView) itemView.findViewById(R.id.show_info);


        // Capture position and set to the ImageView
        imgLogo.setImageResource(upLogo);
        txtTime.setText(time[position] + " , ");
        txtMood.setText(mood[position]);
        txtInfo.setText(info[position]);
		return itemView;
	}
}
