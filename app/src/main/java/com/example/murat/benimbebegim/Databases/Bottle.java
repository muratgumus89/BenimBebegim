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
public class Bottle {

    private Context mContext;

    // Database fields
    private SQLiteDatabase mDatabase;
    private ActivityTable mDbHelper;
    private String[] mAllColumns = { ActivityTable.ACTIVITY_ID, ActivityTable.BOTTLE_FORMULA,ActivityTable.BOTTLE_AMOUNT,
                                    ActivityTable.BOTTLE_TIMER};

    public Bottle(Context context) {
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


    public void insertBottle(int activity_id,String formula,int amount,String timer) {
        ContentValues values = new ContentValues();
        values.put(ActivityTable.ACTIVITY_ID, activity_id);
        values.put(ActivityTable.BOTTLE_FORMULA,formula);
        values.put(ActivityTable.BOTTLE_AMOUNT,amount);
        values.put(ActivityTable.BOTTLE_TIMER,timer);
        mDatabase.insert(ActivityTable.TABLE_BOTTLE, null, values);
        mDatabase.close();
}

    public void deleteBottle(int a_id) {
        mDatabase.delete(ActivityTable.TABLE_BOTTLE, ActivityTable.ACTIVITY_ID + " = " + a_id, null);
    }

    public HashMap<String, String> getBottleID(String a_id){

        HashMap<String,String> mood = new HashMap<String,String>();
        String selectQuery = "SELECT * FROM " + ActivityTable.TABLE_BOTTLE+ " WHERE a_id="+a_id;

        Cursor cursor = mDatabase.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            mood.put(ActivityTable.BOTTLE_ID, String.valueOf(cursor.getInt(0)));
        }
        cursor.close();
        mDatabase.close();
        return mood;
    }

    public ArrayList<HashMap<String, String>> getAllBottle(){

        String selectQuery = "SELECT * FROM " + ActivityTable.TABLE_BOTTLE ;
        Cursor cursor = mDatabase.rawQuery(selectQuery, null);
        ArrayList<HashMap<String, String>> bottlelist = new ArrayList<HashMap<String, String>>();

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                for(int i=0; i<cursor.getColumnCount();i++)
                {
                    map.put(cursor.getColumnName(i), cursor.getString(i));
                }
                bottlelist.add(map);
            } while (cursor.moveToNext());
        }
        mDatabase.close();
        return bottlelist;
    }
    public ArrayList<HashMap<String, String>> getSpecificBottle(){

        String selectQuery = "SELECT * FROM " + ActivityTable.TABLE_BOTTLE ;
        Cursor cursor = mDatabase.rawQuery(selectQuery, null);
        ArrayList<HashMap<String, String>> bottlelist = new ArrayList<HashMap<String, String>>();

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                for(int i=0; i<cursor.getColumnCount();i++)
                {
                    map.put(cursor.getColumnName(i), cursor.getString(i));
                }
                bottlelist.add(map);
            } while (cursor.moveToNext());
        }
        mDatabase.close();
        return bottlelist;
    }
    public ArrayList<HashMap<String, String>> getSpecificBottle(String a_id){

        String selectQuery = "SELECT * FROM " + ActivityTable.TABLE_BOTTLE + " WHERE a_id="+ a_id;
        Cursor cursor = mDatabase.rawQuery(selectQuery, null);
        ArrayList<HashMap<String, String>> bottlelist = new ArrayList<HashMap<String, String>>();

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                for(int i=0; i<cursor.getColumnCount();i++)
                {
                    map.put(cursor.getColumnName(i), cursor.getString(i));
                }
                bottlelist.add(map);
            } while (cursor.moveToNext());
        }
        mDatabase.close();
        return bottlelist;
    }
    public ArrayList<HashMap<String, String>> getSpecificBottleRecord(String bottle_id){

        String selectQuery = "SELECT * FROM " + ActivityTable.TABLE_BOTTLE + " WHERE bottle_id="+ bottle_id;
        Cursor cursor = mDatabase.rawQuery(selectQuery, null);
        ArrayList<HashMap<String, String>> bottlelist = new ArrayList<HashMap<String, String>>();

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                for(int i=0; i<cursor.getColumnCount();i++)
                {
                    map.put(cursor.getColumnName(i), cursor.getString(i));
                }
                bottlelist.add(map);
            } while (cursor.moveToNext());
        }
        mDatabase.close();
        return bottlelist;
    }

    public void editMood(String formula,int amount,String timer,int bottle_id) {
        //Bu methodda ise var olan veriyi güncelliyoruz(update)
        ContentValues values = new ContentValues();
        values.put(ActivityTable.BOTTLE_FORMULA,formula);
        values.put(ActivityTable.BOTTLE_AMOUNT,amount);
        values.put(ActivityTable.BOTTLE_TIMER,timer);

        // updating row
        mDatabase.update(ActivityTable.TABLE_BOTTLE, values, ActivityTable.BOTTLE_ID + " = ?",
                new String[]{String.valueOf(bottle_id)});
        mDatabase.close();
    }
    public void resetTables(){
        //Bunuda uygulamada kullanmıyoruz. Tüm verileri siler. tabloyu resetler.
        mDatabase.delete(ActivityTable.TABLE_BOTTLE, null, null);
        mDatabase.close();
    }

}
