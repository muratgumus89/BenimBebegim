package com.example.murat.benimbebegim.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.murat.benimbebegim.R;

public class ListViewAdapterForDiaries extends BaseAdapter {

	// Declare Variables
	Context context;
	String[] day,month,year,day_name,title,message,path;
    int count;
	LayoutInflater inflater;

	public ListViewAdapterForDiaries(Context context, String[] day, String[] month, String[] year, String[] day_name, String[] title, String[] message, String[] path, int count) {
		this.context = context;
		this.day       = day;
		this.month     = month;
        this.year      = year;
        this.day_name  = day_name;
        this.title     = title;
        this.message   = message;
        this.path      = path;
        this.count     = count;
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
		TextView txtDay,txtMonth,txtYear,txtDayName,txtTitle,txtMessage;

		inflater = (LayoutInflater) context
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

		View itemView = inflater.inflate(R.layout.diary_listview_item, parent, false);

		// Locate the TextViews in listview_item.xml
		txtDay     = (TextView) itemView.findViewById(R.id.txtDay);
		// Locate the ImageView in listview_item.xml
		txtMonth   = (TextView) itemView.findViewById(R.id.txtMonth);
        // Locate the TextViews in listview_item.xml
        txtYear    = (TextView) itemView.findViewById(R.id.txtYear);
        // Locate the ImageView in listview_item.xml
        txtDayName = (TextView) itemView.findViewById(R.id.txtDay_name);

        txtTitle   = (TextView) itemView.findViewById(R.id.txtTitle);
		txtMessage = (TextView) itemView.findViewById(R.id.txtMessage);

        txtDay.setText(day[position]);
        txtMonth.setText(month[position]);
        txtYear.setText(year[position]);
        txtDayName.setText(day_name[position]);
        txtTitle.setText(title[position]);
        txtMessage.setText(message[position]);

		return itemView;
	}
}
