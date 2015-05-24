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
import com.example.murat.benimbebegim.Databases.Bottle;
import com.example.murat.benimbebegim.Databases.Breast;
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
    String mood_type, mood_time, mood_note;
    ArrayList<HashMap<String, String>> records, moods, solid, bottle, breast;

    public TodayEXPListViewAdapter(Context context, List<String> listDataHeader,
                                   HashMap<String, List<String>> listChildData, int[] intHeaderIconResourse) {
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
        if (moods.size() != 0) {
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
            String solids, solidnote;

            Solid solid_db = new Solid(ActivityToday.myContext);
            solid = solid_db.getSpecificSolidAsaActId(a_id);
            if (solid.size() != 0) {
                solids = solid.get(0).get("gram");
                tvTitle.setText(mood_time);
                tvInfo.setText("Gram : " + solids + " ;");
                Log.i("Bread ", solid.get(0).get("bread"));
                String strSolid = "";
                if (solid.get(0).get("bread").equals("True")) {
                    strSolid = "bread,";
                }
                if (solid.get(0).get("fruit").equals("True")) {
                    strSolid = strSolid + "fruit, ";
                }
                if (solid.get(0).get("cereal").equals("True")) {
                    strSolid = strSolid + "cereal, ";
                }
                if (solid.get(0).get("meat").equals("True")) {
                    strSolid = strSolid + "meat, ";
                }
                if (solid.get(0).get("dairy").equals("True")) {
                    strSolid = strSolid + "dairy, ";
                }
                if (solid.get(0).get("pasta").equals("True")) {
                    strSolid = strSolid + "pasta, ";
                }
                if (solid.get(0).get("eggs").equals("True")) {
                    strSolid = strSolid + "eggs, ";
                }
                if (solid.get(0).get("vegetable").equals("True")) {
                    strSolid = strSolid + "vegetable, ";
                }
                if (solid.get(0).get("other").equals("True")) {
                    strSolid = strSolid + "other, ";
                }
                tvInfo.append(strSolid);
            }
        }
        if (groupPosition == 2) { //Bottle icin
            String strBottle;

            Bottle bottle_db = new Bottle(ActivityToday.myContext);
            bottle = bottle_db.getSpecificBottle(a_id);
            if (bottle.size() != 0) {
                strBottle = bottle.get(0).get("formula");
                tvTitle.setText(mood_time);
                tvTitle.append(": Amount >" + bottle.get(0).get("amount") + " gram");
                tvTitle.append(" " + bottle.get(0).get("timer"));
                tvInfo.setText(strBottle);
            }
        }
        if (groupPosition == 3) { //Breast
            Breast breast_db = new Breast(ActivityToday.myContext);
            breast = breast_db.getSpecificBreast(a_id);
            if (breast.size() != 0) {
                tvTitle.setText(mood_time);
                tvInfo.setText(": " + "Duration = " + breast.get(0).get("total_time"));
            }

        }
        if (groupPosition == 4) { //Sleep

        }
        if (groupPosition == 5) { //Diaper

        }
        if (groupPosition == 6) { //Pumping

        }

//        txtListChild.setVisibility(View.INVISIBLE);
        txtListChild.setText(childText);
        return convertView;
    }


}
