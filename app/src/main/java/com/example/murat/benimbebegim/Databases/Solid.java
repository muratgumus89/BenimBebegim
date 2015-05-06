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
public class Solid {

    private Context mContext;

    // Database fields
    private SQLiteDatabase mDatabase;
    private ActivityTable mDbHelper;
    private String[] mAllColumns = { ActivityTable.ACTIVITY_ID, ActivityTable.SOLID_GRAM,ActivityTable.SOLID_BREAD,
                                    ActivityTable.SOLID_FRUIT,ActivityTable.SOLID_CEREAL,ActivityTable.SOLID_MEAT,
                                    ActivityTable.SOLID_DAIRY,ActivityTable.SOLID_PASTA,ActivityTable.SOLID_EGGS,
                                    ActivityTable.SOLID_VEGETABLE,ActivityTable.SOLID_FISH,ActivityTable.SOLID_OTHER};

    public Solid(Context context) {
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


    public void insertSolid(int activity_id, int gram,String bread,String fruit,String cereal,String meat,String dairy,String pasta
                            ,String eggs,String vegetables,String fish,String other) {
        ContentValues values = new ContentValues();
        values.put(ActivityTable.ACTIVITY_ID, activity_id);
        values.put(ActivityTable.SOLID_GRAM, gram);
        values.put(ActivityTable.SOLID_BREAD, bread);
        values.put(ActivityTable.SOLID_FRUIT, fruit);
        values.put(ActivityTable.SOLID_CEREAL, cereal);
        values.put(ActivityTable.SOLID_MEAT, meat);
        values.put(ActivityTable.SOLID_DAIRY, dairy);
        values.put(ActivityTable.SOLID_PASTA, pasta);
        values.put(ActivityTable.SOLID_EGGS, eggs);
        values.put(ActivityTable.SOLID_VEGETABLE, vegetables);
        values.put(ActivityTable.SOLID_FISH, fish);
        values.put(ActivityTable.SOLID_OTHER, other);
        mDatabase.insert(ActivityTable.TABLE_SOLID, null, values);
        mDatabase.close();
}

    public void deleteSolid(int a_id) {
        mDatabase.delete(ActivityTable.TABLE_SOLID, ActivityTable.ACTIVITY_ID + " = " + a_id, null);
    }

    public HashMap<String, String> getSolidID(int a_id){

        HashMap<String,String> mood = new HashMap<String,String>();
        String selectQuery = "SELECT * FROM " + ActivityTable.TABLE_SOLID+ " WHERE a_id="+a_id;

        Cursor cursor = mDatabase.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            mood.put(ActivityTable.SOLID_ID, String.valueOf(cursor.getInt(0)));
        }
        cursor.close();
        mDatabase.close();
        return mood;
    }

    public ArrayList<HashMap<String, String>> getAllSolids(){

        String selectQuery = "SELECT * FROM " + ActivityTable.TABLE_SOLID ;
        Cursor cursor = mDatabase.rawQuery(selectQuery, null);
        ArrayList<HashMap<String, String>> moodlist = new ArrayList<HashMap<String, String>>();

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                for(int i=0; i<cursor.getColumnCount();i++)
                {
                    map.put(cursor.getColumnName(i), cursor.getString(i));
                }
                moodlist.add(map);
            } while (cursor.moveToNext());
        }
        mDatabase.close();
        return moodlist;
    }
    public ArrayList<HashMap<String, String>> getSpecificSolidAsaActId(String a_id){

        String selectQuery = "SELECT * FROM " + ActivityTable.TABLE_SOLID + " WHERE a_id="+ a_id;
        Cursor cursor = mDatabase.rawQuery(selectQuery, null);
        ArrayList<HashMap<String, String>> moodlist = new ArrayList<HashMap<String, String>>();

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                for(int i=0; i<cursor.getColumnCount();i++)
                {
                    map.put(cursor.getColumnName(i), cursor.getString(i));
                }
                moodlist.add(map);
            } while (cursor.moveToNext());
        }
        mDatabase.close();
        return moodlist;
    }
    public ArrayList<HashMap<String, String>> getSpecificSolidRecord(int solid_id){

        String selectQuery = "SELECT * FROM " + ActivityTable.TABLE_SOLID + " WHERE solid_id="+ solid_id;
        Cursor cursor = mDatabase.rawQuery(selectQuery, null);
        ArrayList<HashMap<String, String>> moodlist = new ArrayList<HashMap<String, String>>();

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                for(int i=0; i<cursor.getColumnCount();i++)
                {
                    map.put(cursor.getColumnName(i), cursor.getString(i));
                }
                moodlist.add(map);
            } while (cursor.moveToNext());
        }
        mDatabase.close();
        return moodlist;
    }

    public void editMood(int gram,String bread,String fruit,String cereal,String meat,String dairy,String pasta
            ,String eggs,String vegetables,String fish,String other,int solid_id) {
        //Bu methodda ise var olan veriyi güncelliyoruz(update)
        ContentValues values = new ContentValues();
        values.put(ActivityTable.SOLID_GRAM, gram);
        values.put(ActivityTable.SOLID_BREAD, bread);
        values.put(ActivityTable.SOLID_FRUIT, fruit);
        values.put(ActivityTable.SOLID_CEREAL, cereal);
        values.put(ActivityTable.SOLID_MEAT, meat);
        values.put(ActivityTable.SOLID_DAIRY, dairy);
        values.put(ActivityTable.SOLID_PASTA, pasta);
        values.put(ActivityTable.SOLID_EGGS, eggs);
        values.put(ActivityTable.SOLID_VEGETABLE, vegetables);
        values.put(ActivityTable.SOLID_FISH, fish);
        values.put(ActivityTable.SOLID_OTHER, other);

        // updating row
        mDatabase.update(ActivityTable.TABLE_SOLID, values, ActivityTable.SOLID_ID + " = ?",
                new String[]{String.valueOf(solid_id)});
        mDatabase.close();
    }
    public void resetTables(){
        //Bunuda uygulamada kullanmıyoruz. Tüm verileri siler. tabloyu resetler.
        mDatabase.delete(ActivityTable.TABLE_SOLID, null, null);
        mDatabase.close();
    }

}
