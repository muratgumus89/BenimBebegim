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
public class Health {

    private Context mContext;

    // Database fields
    private SQLiteDatabase mDatabase;
    private ActivityTable mDbHelper;
    private String[] mAllColumns = { ActivityTable.ACTIVITY_ID, ActivityTable.HEALTH_TYPE,ActivityTable.HEALTH_TEMP};

    public Health(Context context) {
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


    public void insertHealth(int activity_id,String health_type,String health_temp) {
        ContentValues values = new ContentValues();
        values.put(ActivityTable.ACTIVITY_ID, activity_id);
        values.put(ActivityTable.HEALTH_TEMP, health_temp);
        values.put(ActivityTable.HEALTH_TYPE, health_type);
        mDatabase.insert(ActivityTable.TABLE_HEALTH, null, values);
        mDatabase.close();
}

    public void deleteHealth(int a_id) {
        mDatabase.delete(ActivityTable.TABLE_HEALTH, ActivityTable.ACTIVITY_ID + " = " + a_id, null);
    }

    public HashMap<String, String> getHealthID(String a_id){

        HashMap<String,String> healthID = new HashMap<String,String>();
        String selectQuery = "SELECT * FROM " + ActivityTable.TABLE_HEALTH + " WHERE a_id="+a_id;

        Cursor cursor = mDatabase.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            healthID.put(ActivityTable.HEALTH_ID, String.valueOf(cursor.getInt(0)));
        }
        cursor.close();
        mDatabase.close();
        return healthID;
    }

    public ArrayList<HashMap<String, String>> getAllHealth(){

        String selectQuery = "SELECT * FROM " + ActivityTable.TABLE_HEALTH ;
        Cursor cursor = mDatabase.rawQuery(selectQuery, null);
        ArrayList<HashMap<String, String>> healthList = new ArrayList<HashMap<String, String>>();

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                for(int i=0; i<cursor.getColumnCount();i++)
                {
                    map.put(cursor.getColumnName(i), cursor.getString(i));
                }
                healthList.add(map);
            } while (cursor.moveToNext());
        }
        mDatabase.close();
        return healthList;
    }
    public ArrayList<HashMap<String, String>> getSpecificHealthWithAID(String a_id){

        String selectQuery = "SELECT * FROM " + ActivityTable.TABLE_HEALTH + " WHERE a_id="+ a_id;
        Cursor cursor = mDatabase.rawQuery(selectQuery, null);
        ArrayList<HashMap<String, String>> healthList = new ArrayList<HashMap<String, String>>();

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                for(int i=0; i<cursor.getColumnCount();i++)
                {
                    map.put(cursor.getColumnName(i), cursor.getString(i));
                }
                healthList.add(map);
            } while (cursor.moveToNext());
        }
        mDatabase.close();
        return healthList;
    }
    public ArrayList<HashMap<String, String>> getSpecificHealthWithBID(String health_id){

        String selectQuery = "SELECT * FROM " + ActivityTable.TABLE_HEALTH + " WHERE health_id="+ health_id;
        Cursor cursor = mDatabase.rawQuery(selectQuery, null);
        ArrayList<HashMap<String, String>> healthList = new ArrayList<HashMap<String, String>>();

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                for(int i=0; i<cursor.getColumnCount();i++)
                {
                    map.put(cursor.getColumnName(i), cursor.getString(i));
                }
                healthList.add(map);
            } while (cursor.moveToNext());
        }
        mDatabase.close();
        return healthList;
    }

    public void editHealth(int activity_id,String health_type,String health_temp,String health_id) {
        //Bu methodda ise var olan veriyi güncelliyoruz(update)
        ContentValues values = new ContentValues();
        values.put(ActivityTable.HEALTH_TYPE,health_type);
        values.put(ActivityTable.HEALTH_TEMP,health_temp);

        // updating row
        mDatabase.update(ActivityTable.TABLE_HEALTH, values, ActivityTable.HEALTH_ID + " = ?",
                new String[]{String.valueOf(health_id)});
        mDatabase.close();
    }
    public void resetTables(){
        //Bunuda uygulamada kullanmıyoruz. Tüm verileri siler. tabloyu resetler.
        mDatabase.delete(ActivityTable.TABLE_HEALTH, null, null);
        mDatabase.close();
    }

}
