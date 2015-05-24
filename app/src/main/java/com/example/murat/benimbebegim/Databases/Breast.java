package com.example.murat.benimbebegim.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Murat on 18.4.2015.
 */
public class Breast {

    private Context mContext;

    // Database fields
    private SQLiteDatabase mDatabase;
    private ActivityTable mDbHelper;
    private String[] mAllColumns = { ActivityTable.ACTIVITY_ID, ActivityTable.BREAST_TIME};

    public Breast(Context context) {
        mDbHelper = new ActivityTable(context);
        this.mContext = context;
        // open the database
        try {
            open();
        }
        catch(SQLException e) {
            Log.e("Solid", "SQLException on openning database " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void open() throws SQLException {
        mDatabase = mDbHelper.getWritableDatabase();
    }

    public void close() {
        mDbHelper.close();
    }


    public void insertBreast(int activity_id,String actual_time) {
        ContentValues values = new ContentValues();
        values.put(ActivityTable.ACTIVITY_ID, activity_id);
        values.put(ActivityTable.BREAST_TIME,actual_time);
        mDatabase.insert(ActivityTable.TABLE_BREAST, null, values);
        mDatabase.close();
}

    public void deleteBreast(int a_id) {
        mDatabase.delete(ActivityTable.TABLE_BREAST, ActivityTable.ACTIVITY_ID + " = " + a_id, null);
    }

    public HashMap<String, String> getBreastID(String a_id){

        HashMap<String,String> breastID = new HashMap<String,String>();
        String selectQuery = "SELECT * FROM " + ActivityTable.TABLE_BREAST + " WHERE a_id="+a_id;

        Cursor cursor = mDatabase.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            breastID.put(ActivityTable.BREAST_ID, String.valueOf(cursor.getInt(0)));
        }
        cursor.close();
        mDatabase.close();
        return breastID;
    }

    public ArrayList<HashMap<String, String>> getAllBreast(){

        String selectQuery = "SELECT * FROM " + ActivityTable.TABLE_BREAST ;
        Cursor cursor = mDatabase.rawQuery(selectQuery, null);
        ArrayList<HashMap<String, String>> breastList = new ArrayList<HashMap<String, String>>();

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                for(int i=0; i<cursor.getColumnCount();i++)
                {
                    map.put(cursor.getColumnName(i), cursor.getString(i));
                }
                breastList.add(map);
            } while (cursor.moveToNext());
        }
        mDatabase.close();
        return breastList;
    }
    public ArrayList<HashMap<String, String>> getSpecificBreast(String a_id){

        String selectQuery = "SELECT * FROM " + ActivityTable.TABLE_BREAST + " WHERE a_id="+ a_id;
        Cursor cursor = mDatabase.rawQuery(selectQuery, null);
        ArrayList<HashMap<String, String>> breastList = new ArrayList<HashMap<String, String>>();

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                for(int i=0; i<cursor.getColumnCount();i++)
                {
                    map.put(cursor.getColumnName(i), cursor.getString(i));
                }
                breastList.add(map);
            } while (cursor.moveToNext());
        }
        mDatabase.close();
        return breastList;
    }
    public ArrayList<HashMap<String, String>> getSpecificBreastRecord(String breast_id){

        String selectQuery = "SELECT * FROM " + ActivityTable.TABLE_BREAST + " WHERE breast_id="+ breast_id;
        Cursor cursor = mDatabase.rawQuery(selectQuery, null);
        ArrayList<HashMap<String, String>> breastList = new ArrayList<HashMap<String, String>>();

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                for(int i=0; i<cursor.getColumnCount();i++)
                {
                    map.put(cursor.getColumnName(i), cursor.getString(i));
                }
                breastList.add(map);
            } while (cursor.moveToNext());
        }
        mDatabase.close();
        return breastList;
    }

    public void editBreast(String actual_time,int breast_id) {
        //Bu methodda ise var olan veriyi güncelliyoruz(update)
        ContentValues values = new ContentValues();
        values.put(ActivityTable.BREAST_TIME,actual_time);

        // updating row
        mDatabase.update(ActivityTable.TABLE_BREAST, values, ActivityTable.BREAST_ID + " = ?",
                new String[]{String.valueOf(breast_id)});
        mDatabase.close();
    }
    public void resetTables(){
        //Bunuda uygulamada kullanmıyoruz. Tüm verileri siler. tabloyu resetler.
        mDatabase.delete(ActivityTable.TABLE_BREAST, null, null);
        mDatabase.close();
    }

}
