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
public class Hygiene {

    private Context mContext;

    // Database fields
    private SQLiteDatabase mDatabase;
    private ActivityTable mDbHelper;
    private String[] mAllColumns = { ActivityTable.ACTIVITY_ID, ActivityTable.VACCINATION_TYPE};

    public Hygiene(Context context) {
        mDbHelper = new ActivityTable(context);
        this.mContext = context;
        // open the database
        try {
            open();
        }
        catch(SQLException e) {
            Log.e("Hygiene: ", "SQLException on openning database " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void open() throws SQLException {
        mDatabase = mDbHelper.getWritableDatabase();
    }

    public void close() {
        mDbHelper.close();
    }


    public void insertHygiene(int activity_id,String hygiene_type) {
        ContentValues values = new ContentValues();
        values.put(ActivityTable.ACTIVITY_ID, activity_id);
        values.put(ActivityTable.HYGIENE_TYPE, hygiene_type);
        mDatabase.insert(ActivityTable.TABLE_HYGIENE, null, values);
        mDatabase.close();
}

    public void deleteHygiene(int a_id) {
        mDatabase.delete(ActivityTable.TABLE_HYGIENE, ActivityTable.ACTIVITY_ID + " = " + a_id, null);
    }

    public HashMap<String, String> getHygieneID(String a_id){

        HashMap<String,String> vacID = new HashMap<String,String>();
        String selectQuery = "SELECT * FROM " + ActivityTable.TABLE_HYGIENE + " WHERE a_id="+a_id;

        Cursor cursor = mDatabase.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            vacID.put(ActivityTable.HYGIENE_ID, String.valueOf(cursor.getInt(0)));
        }
        cursor.close();
        mDatabase.close();
        return vacID;
    }

    public ArrayList<HashMap<String, String>> getAllHygienes(){

        String selectQuery = "SELECT * FROM " + ActivityTable.TABLE_HYGIENE ;
        Cursor cursor = mDatabase.rawQuery(selectQuery, null);
        ArrayList<HashMap<String, String>> vacList = new ArrayList<HashMap<String, String>>();

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                for(int i=0; i<cursor.getColumnCount();i++)
                {
                    map.put(cursor.getColumnName(i), cursor.getString(i));
                }
                vacList.add(map);
            } while (cursor.moveToNext());
        }
        mDatabase.close();
        return vacList;
    }
    public ArrayList<HashMap<String, String>> getSpecificHygieneWithAID(String a_id){

        String selectQuery = "SELECT * FROM " + ActivityTable.TABLE_HYGIENE + " WHERE a_id="+ a_id;
        Cursor cursor = mDatabase.rawQuery(selectQuery, null);
        ArrayList<HashMap<String, String>> vacList = new ArrayList<HashMap<String, String>>();

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                for(int i=0; i<cursor.getColumnCount();i++)
                {
                    map.put(cursor.getColumnName(i), cursor.getString(i));
                }
                vacList.add(map);
            } while (cursor.moveToNext());
        }
        mDatabase.close();
        return vacList;
    }
    public ArrayList<HashMap<String, String>> getSpecificHygieneWithBID(String hygiene_id){

        String selectQuery = "SELECT * FROM " + ActivityTable.TABLE_HYGIENE + " WHERE hygiene_id="+ hygiene_id;
        Cursor cursor = mDatabase.rawQuery(selectQuery, null);
        ArrayList<HashMap<String, String>> vacList = new ArrayList<HashMap<String, String>>();

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                for(int i=0; i<cursor.getColumnCount();i++)
                {
                    map.put(cursor.getColumnName(i), cursor.getString(i));
                }
                vacList.add(map);
            } while (cursor.moveToNext());
        }
        mDatabase.close();
        return vacList;
    }

    public void editHygiene(int activity_id,String hygiene_type,String hygiene_id) {
        //Bu methodda ise var olan veriyi güncelliyoruz(update)
        ContentValues values = new ContentValues();
        values.put(ActivityTable.HYGIENE_TYPE,hygiene_type);

        // updating row
        mDatabase.update(ActivityTable.TABLE_HYGIENE, values, ActivityTable.HYGIENE_ID + " = ?",
                new String[]{String.valueOf(hygiene_id)});
        mDatabase.close();
    }
    public void resetTables(){
        //Bunuda uygulamada kullanmıyoruz. Tüm verileri siler. tabloyu resetler.
        mDatabase.delete(ActivityTable.TABLE_HYGIENE, null, null);
        mDatabase.close();
    }

}
