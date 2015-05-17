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
public class Pumping {

    private Context mContext;

    // Database fields
    private SQLiteDatabase mDatabase;
    private ActivityTable mDbHelper;
    private String[] mAllColumns = { ActivityTable.ACTIVITY_ID, ActivityTable.PUMPING_TIME,ActivityTable.PUMPING_AMOUNT};

    public Pumping(Context context) {
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


    public void insertPumping(int activity_id,String actual_time,int pumping_amount) {
        ContentValues values = new ContentValues();
        values.put(ActivityTable.ACTIVITY_ID, activity_id);
        values.put(ActivityTable.PUMPING_TIME,actual_time);
        values.put(ActivityTable.PUMPING_AMOUNT,pumping_amount);
        mDatabase.insert(ActivityTable.TABLE_PUMPING, null, values);
        mDatabase.close();
}

    public void deletePumping(int a_id) {
        mDatabase.delete(ActivityTable.TABLE_PUMPING, ActivityTable.ACTIVITY_ID + " = " + a_id, null);
    }

    public HashMap<String, String> getPumpingID(String a_id){

        HashMap<String,String> pumpingID = new HashMap<String,String>();
        String selectQuery = "SELECT * FROM " + ActivityTable.TABLE_PUMPING + " WHERE a_id="+a_id;

        Cursor cursor = mDatabase.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            pumpingID.put(ActivityTable.PUMPING_ID, String.valueOf(cursor.getInt(0)));
        }
        cursor.close();
        mDatabase.close();
        return pumpingID;
    }

    public ArrayList<HashMap<String, String>> getAllPumping(){

        String selectQuery = "SELECT * FROM " + ActivityTable.TABLE_PUMPING ;
        Cursor cursor = mDatabase.rawQuery(selectQuery, null);
        ArrayList<HashMap<String, String>> pumpingList = new ArrayList<HashMap<String, String>>();

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                for(int i=0; i<cursor.getColumnCount();i++)
                {
                    map.put(cursor.getColumnName(i), cursor.getString(i));
                }
                pumpingList.add(map);
            } while (cursor.moveToNext());
        }
        mDatabase.close();
        return pumpingList;
    }
    public ArrayList<HashMap<String, String>> getSpecificPumpingWithAID(String a_id){

        String selectQuery = "SELECT * FROM " + ActivityTable.TABLE_PUMPING + " WHERE a_id="+ a_id;
        Cursor cursor = mDatabase.rawQuery(selectQuery, null);
        ArrayList<HashMap<String, String>> pumpingList = new ArrayList<HashMap<String, String>>();

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                for(int i=0; i<cursor.getColumnCount();i++)
                {
                    map.put(cursor.getColumnName(i), cursor.getString(i));
                }
                pumpingList.add(map);
            } while (cursor.moveToNext());
        }
        mDatabase.close();
        return pumpingList;
    }
    public ArrayList<HashMap<String, String>> getSpecificPumpingWithBID(String breast_id){

        String selectQuery = "SELECT * FROM " + ActivityTable.TABLE_PUMPING + " WHERE breast_id="+ breast_id;
        Cursor cursor = mDatabase.rawQuery(selectQuery, null);
        ArrayList<HashMap<String, String>> pumpingList = new ArrayList<HashMap<String, String>>();

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                for(int i=0; i<cursor.getColumnCount();i++)
                {
                    map.put(cursor.getColumnName(i), cursor.getString(i));
                }
                pumpingList.add(map);
            } while (cursor.moveToNext());
        }
        mDatabase.close();
        return pumpingList;
    }

    public void editPumping(String actual_time,int pumping_id,int pumping_amount) {
        //Bu methodda ise var olan veriyi güncelliyoruz(update)
        ContentValues values = new ContentValues();
        values.put(ActivityTable.PUMPING_TIME,actual_time);
        values.put(ActivityTable.PUMPING_AMOUNT,pumping_amount);

        // updating row
        mDatabase.update(ActivityTable.TABLE_PUMPING, values, ActivityTable.PUMPING_ID + " = ?",
                new String[]{String.valueOf(pumping_id)});
        mDatabase.close();
    }
    public void resetTables(){
        //Bunuda uygulamada kullanmıyoruz. Tüm verileri siler. tabloyu resetler.
        mDatabase.delete(ActivityTable.TABLE_PUMPING, null, null);
        mDatabase.close();
    }

}
