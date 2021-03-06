package com.example.murat.benimbebegim.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import com.example.murat.benimbebegim.R;
import com.example.murat.benimbebegim.model.SpinnerNavItem;

public class TitleNavigationAdapter extends BaseAdapter {

	private ImageView imgIcon;
	private TextView txtTitle;
	private ArrayList<SpinnerNavItem> spinnerNavItem;
	private Context context;
    public static final String PREFS_NAME = "MyPrefsFile";
    String baby_id,user_id,baby_name,userName;

    public TitleNavigationAdapter(String baby_name) {
        this.baby_name=baby_name;
    }
	public TitleNavigationAdapter(Context context,
                                  ArrayList<SpinnerNavItem> spinnerNavItem) {
		this.spinnerNavItem = spinnerNavItem;
		this.context = context;

	}


	@Override
	public int getCount() {
		return spinnerNavItem.size();
	}

	@Override
	public Object getItem(int index) {
		return spinnerNavItem.get(index);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
        	LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.list_item_title_navigation, null);
        }

        imgIcon = (ImageView) convertView.findViewById(R.id.imgIcon);
        txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);
        
        imgIcon.setImageDrawable(spinnerNavItem.get(position).getIcon());
        //imgIcon.setVisibility(View.GONE);
        txtTitle.setText(spinnerNavItem.get(position).getTitle());
        return convertView;
	}
	

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		if (convertView == null) {
        	LayoutInflater mInflater = (LayoutInflater)
                    context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
            convertView = mInflater.inflate(R.layout.list_item_title_navigation, null);
        }
        
        imgIcon = (ImageView) convertView.findViewById(R.id.imgIcon);
        txtTitle = (TextView) convertView.findViewById(R.id.txtTitle);

        imgIcon.setImageDrawable(spinnerNavItem.get(position).getIcon());
        txtTitle.setText(spinnerNavItem.get(position).getTitle());
        return convertView;
	}

}
