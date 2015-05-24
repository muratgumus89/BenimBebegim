package com.example.murat.benimbebegim.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.murat.benimbebegim.R;

/**
 * Created by aytunc on 24.5.2015.
 */
public class StatsActivityAdapter extends BaseAdapter {

    Context context;
    String[] chartName;
    int[] chartResourceIcon;
    String[] iconString;

    public StatsActivityAdapter(Context context, String[] chartName, int[] chartResourceIcon, String[] iconString) {
        this.context = context;
        this.chartName = chartName;
        this.chartResourceIcon = chartResourceIcon;
        this.iconString = iconString;
    }

    @Override
    public int getCount() {
        return chartName.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.listview_item, null);
        }

        TextView fav_name = (TextView) convertView.findViewById(R.id.fav_name);
        TextView showMoodName = (TextView) convertView.findViewById(R.id.show_mood_name);
        TextView clickAdd = (TextView) convertView.findViewById(R.id.click_add);


        ImageView iv = (ImageView) convertView.findViewById(R.id.logo);

        clickAdd.setText("Tap to see the chart");
        fav_name.setText(iconString[position]);
        showMoodName.setText(chartName[position]);
        iv.setImageResource(chartResourceIcon[position]);

        return convertView;
    }
}
