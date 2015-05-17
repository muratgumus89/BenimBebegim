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
public class Diaper {

    private Context mContext;

    // Database fields
    private SQLiteDatabase mDatabase;
    private ActivityTable mDbHelper;
    private String[] mAllColumns = { ActivityTable.ACTIVITY_ID, ActivityTable.SLEEP_TIME};

    public Diaper(Context context) {
        mDbHelper = new ActivityTable(context);
        this.mContext = context;
        // open the database
        try {
            open();
        }
        catch(SQLException e) {
            Log.e("Diaper", "SQLException on openning database " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void open() throws SQLException {
        mDatabase = mDbHelper.getWritableDatabase();
    }

    public void close() {
        mDbHelper.close();
    }


    public void insertDiaper(int activity_id,String diaper_type) {
        ContentValues values = new ContentValues();
        values.put(ActivityTable.ACTIVITY_ID, activity_id);
        values.put(ActivityTable.DIAPER_TYPE, diaper_type);
        mDatabase.insert(ActivityTable.TABLE_DIAPER, null, values);
        mDatabase.close();
}

    public void deleteDiaper(int a_id) {
        mDatabase.delete(ActivityTable.TABLE_DIAPER, ActivityTable.ACTIVITY_ID + " = " + a_id, null);
    }

    public HashMap<String, String> getDiaperID(String a_id){

        HashMap<String,String> diaperID = new HashMap<String,String>();
        String selectQuery = "SELECT * FROM " + ActivityTable.TABLE_DIAPER + " WHERE a_id="+a_id;

        Cursor cursor = mDatabase.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            diaperID.put(ActivityTable.DIAPER_ID, String.valueOf(cursor.getInt(0)));
        }
        cursor.close();
        mDatabase.close();
        return diaperID;
    }

    public ArrayList<HashMap<String, String>> getAllDiaper(){

        String selectQuery = "SELECT * FROM " + ActivityTable.TABLE_DIAPER ;
        Cursor cursor = mDatabase.rawQuery(selectQuery, null);
        ArrayList<HashMap<String, String>> diaperList = new ArrayList<HashMap<String, String>>();

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                for(int i=0; i<cursor.getColumnCount();i++)
                {
                    map.put(cursor.getColumnName(i), cursor.getString(i));
                }
                diaperList.add(map);
            } while (cursor.moveToNext());
        }
        mDatabase.close();
        return diaperList;
    }
    public ArrayList<HashMap<String, String>> getSpecificDiaper(String a_id){

        String selectQuery = "SELECT * FROM " + ActivityTable.TABLE_DIAPER + " WHERE a_id="+ a_id;
        Cursor cursor = mDatabase.rawQuery(selectQuery, null);
        ArrayList<HashMap<String, String>> diaperList = new ArrayList<HashMap<String, String>>();

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                for(int i=0; i<cursor.getColumnCount();i++)
                {
                    map.put(cursor.getColumnName(i), cursor.getString(i));
                }
                diaperList.add(map);
            } while (cursor.moveToNext());
        }
        mDatabase.close();
        return diaperList;
    }
    public ArrayList<HashMap<String, String>> getSpecificDiaperRecord(String diaper_id){

        String selectQuery = "SELECT * FROM " + ActivityTable.TABLE_DIAPER + " WHERE diaper_id="+ diaper_id;
        Cursor cursor = mDatabase.rawQuery(selectQuery, null);
        ArrayList<HashMap<String, String>> diaperList = new ArrayList<HashMap<String, String>>();

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                for(int i=0; i<cursor.getColumnCount();i++)
                {
                    map.put(cursor.getColumnName(i), cursor.getString(i));
                }
                diaperList.add(map);
            } while (cursor.moveToNext());
        }
        mDatabase.close();
        return diaperList;
    }

    public void editDiaper(String actual_time,int diaper_id) {
        //Bu methodda ise var olan veriyi güncelliyoruz(update)
        ContentValues values = new ContentValues();
        values.put(ActivityTable.DIAPER_TYPE,actual_time);

        // updating row
        mDatabase.update(ActivityTable.TABLE_DIAPER, values, ActivityTable.DIAPER_ID + " = ?",
                new String[]{String.valueOf(diaper_id)});
        mDatabase.close();
    }
    public void resetTables(){
        //Bunuda uygulamada kullanmıyoruz. Tüm verileri siler. tabloyu resetler.
        mDatabase.delete(ActivityTable.TABLE_DIAPER, null, null);
        mDatabase.close();
    }
}
