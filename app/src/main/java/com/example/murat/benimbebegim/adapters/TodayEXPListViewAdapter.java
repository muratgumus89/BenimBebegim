package com.example.murat.benimbebegim.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.murat.benimbebegim.ActivityToday;
import com.example.murat.benimbebegim.Databases.ActivityTable;
import com.example.murat.benimbebegim.Databases.Mood;
import com.example.murat.benimbebegim.Databases.Solid;
import com.example.murat.benimbebegim.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by aytunc on 13.5.2015.
 */
public class TodayEXPListViewAdapter extends BaseExpandableListAdapter {

    private Context _context;
    private int[] intHeaderIconResourse;
    private List<String> _listDataHeader; // header titles
    // child data in format of header title, child title
    private HashMap<String, List<String>> _listDataChild;

    //ForMOOD
    String mood_type,mood_time,mood_note;
    ArrayList<HashMap<String, String>> records,moods, solid;
    public TodayEXPListViewAdapter(Context context, List<String> listDataHeader,
                                   HashMap<String, List<String>> listChildData, int[] intHeaderIconResourse){
            this._context = context;
            this._listDataHeader = listDataHeader;
            this._listDataChild = listChildData;
            this.intHeaderIconResourse = intHeaderIconResourse;

    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).
                get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String headerTitle = (String) getGroup(groupPosition);
        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.today_layout_group_item, null);
        }

        TextView lblListHeader = (TextView) convertView
                .findViewById(R.id.lblListHeader);
        lblListHeader.setTypeface(null, Typeface.BOLD);
        lblListHeader.setText(headerTitle);

        ImageView iv = (ImageView) convertView.findViewById(R.id.ivTodaygroupheaderImage);
        iv.setImageResource(intHeaderIconResourse[groupPosition]);

        return convertView;
    }


    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final String childText = (String) getChild(groupPosition, childPosition);
//        final String Title = getTime();
        String a_id = ActivityToday.listDataChild.get(
                ActivityToday.listDataHeader.get(groupPosition)).get(
                childPosition);
        ActivityTable a_db = new ActivityTable(ActivityToday.myContext);
        records = a_db.getSpesificActivityRecord(a_id);
        if (records.size() != 0) {
                mood_time = records.get(0).get("select_time");
                mood_note = records.get(0).get("note");
        }

        Mood mood_db = new Mood(ActivityToday.myContext);
        moods = mood_db.getSpecificMoodAsaActId(a_id);
        if(moods.size() != 0){
            mood_type = moods.get(0).get("m_type");
        }

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this._context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.today_layout_item, null);
        }

        Log.i("GroupoPosition ", String.valueOf(groupPosition));
        TextView txtListChild = (TextView) convertView.findViewById(R.id.tvActivityIdInvisible);
        TextView tvTitle = (TextView) convertView.findViewById(R.id.tvTodayChildTitle);
        TextView tvInfo = (TextView) convertView.findViewById(R.id.tvTodayChildInfo);

        if (groupPosition == 0) {
            /*************************/
            tvTitle.setText(mood_time);
            tvInfo.setText(mood_type);
        }
        if (groupPosition == 1) {
            Solid solid_db = new Solid(ActivityToday.myContext);
            solid = solid_db.getSpecificMAsaActId(a_id);
            if(solid.size() != 0){
                mood_type = moods.get(0).get("m_type");
            }
        }
        txtListChild.setVisibility(View.INVISIBLE);
        txtListChild.setText(childText);
        return convertView;
    }



}
