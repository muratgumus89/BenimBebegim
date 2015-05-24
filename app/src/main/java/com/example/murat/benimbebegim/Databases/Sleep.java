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
public class Sleep {

    private Context mContext;

    // Database fields
    private SQLiteDatabase mDatabase;
    private ActivityTable mDbHelper;
    private String[] mAllColumns = { ActivityTable.ACTIVITY_ID, ActivityTable.SLEEP_TIME};

    public Sleep(Context context) {
        mDbHelper = new ActivityTable(context);
        this.mContext = context;
        // open the database
        try {
            open();
        }
        catch(SQLException e) {
            Log.e("Sleep", "SQLException on openning database " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void open() throws SQLException {
        mDatabase = mDbHelper.getWritableDatabase();
    }

    public void close() {
        mDbHelper.close();
    }


    public void insertSleep(int activity_id,String actual_time) {
        ContentValues values = new ContentValues();
        values.put(ActivityTable.ACTIVITY_ID, activity_id);
        values.put(ActivityTable.SLEEP_TIME,actual_time);
        mDatabase.insert(ActivityTable.TABLE_SLEEP, null, values);
        mDatabase.close();
}

    public void deleteBreast(int a_id) {
        mDatabase.delete(ActivityTable.TABLE_SLEEP, ActivityTable.ACTIVITY_ID + " = " + a_id, null);
    }

    public HashMap<String, String> getSleepID(String a_id){

        HashMap<String,String> sleepID = new HashMap<String,String>();
        String selectQuery = "SELECT * FROM " + ActivityTable.TABLE_SLEEP + " WHERE a_id="+a_id;

        Cursor cursor = mDatabase.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            sleepID.put(ActivityTable.SLEEP_ID, String.valueOf(cursor.getInt(0)));
        }
        cursor.close();
        mDatabase.close();
        return sleepID;
    }

    public ArrayList<HashMap<String, String>> getAllSleep(){

        String selectQuery = "SELECT * FROM " + ActivityTable.TABLE_SLEEP ;
        Cursor cursor = mDatabase.rawQuery(selectQuery, null);
        ArrayList<HashMap<String, String>> sleepList = new ArrayList<HashMap<String, String>>();

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                for(int i=0; i<cursor.getColumnCount();i++)
                {
                    map.put(cursor.getColumnName(i), cursor.getString(i));
                }
                sleepList.add(map);
            } while (cursor.moveToNext());
        }
        mDatabase.close();
        return sleepList;
    }
    public ArrayList<HashMap<String, String>> getSpecificSleepRecordWıthAID(String a_id){

        String selectQuery = "SELECT * FROM " + ActivityTable.TABLE_SLEEP + " WHERE a_id="+ a_id;
        Cursor cursor = mDatabase.rawQuery(selectQuery, null);
        ArrayList<HashMap<String, String>> sleepList = new ArrayList<HashMap<String, String>>();

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                for(int i=0; i<cursor.getColumnCount();i++)
                {
                    map.put(cursor.getColumnName(i), cursor.getString(i));
                }
                sleepList.add(map);
            } while (cursor.moveToNext());
        }
        mDatabase.close();
        return sleepList;
    }
    public ArrayList<HashMap<String, String>> getSpecificBreastRecordWITHBREID(String sleep_id){

        String selectQuery = "SELECT * FROM " + ActivityTable.TABLE_SLEEP + " WHERE sleep_id="+ sleep_id;
        Cursor cursor = mDatabase.rawQuery(selectQuery, null);
        ArrayList<HashMap<String, String>> sleepList = new ArrayList<HashMap<String, String>>();

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                for(int i=0; i<cursor.getColumnCount();i++)
                {
                    map.put(cursor.getColumnName(i), cursor.getString(i));
                }
                sleepList.add(map);
            } while (cursor.moveToNext());
        }
        mDatabase.close();
        return sleepList;
    }

    public void editSleep(String actual_time,int sleep_id) {
        //Bu methodda ise var olan veriyi güncelliyoruz(update)
        ContentValues values = new ContentValues();
        values.put(ActivityTable.SLEEP_TIME,actual_time);

        // updating row
        mDatabase.update(ActivityTable.TABLE_SLEEP, values, ActivityTable.SLEEP_ID + " = ?",
                new String[]{String.valueOf(sleep_id)});
        mDatabase.close();
    }
    public void resetTables(){
        //Bunuda uygulamada kullanmıyoruz. Tüm verileri siler. tabloyu resetler.
        mDatabase.delete(ActivityTable.TABLE_SLEEP, null, null);
        mDatabase.close();
    }

}
