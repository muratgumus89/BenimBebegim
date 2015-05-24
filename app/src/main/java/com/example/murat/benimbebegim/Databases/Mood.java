package com.example.murat.benimbebegim.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Murat on 18.4.2015.
 */
public class Mood {

    private Context mContext;

    // Database fields
    private SQLiteDatabase mDatabase;
    private ActivityTable mDbHelper;
    private String[] mAllColumns = { ActivityTable.ACTIVITY_ID, ActivityTable.MOOD_TYPE };

    public Mood(Context context) {
        mDbHelper = new ActivityTable(context);
        this.mContext = context;
        // open the database
        try {
            open();
        }
        catch(SQLException e) {
            Log.e("Mood", "SQLException on openning database " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void open() throws SQLException {
        mDatabase = mDbHelper.getWritableDatabase();
    }

    public void close() {
        mDbHelper.close();
    }


    public void insertMood(int activity_id, String mood_type) {
        ContentValues values = new ContentValues();
        values.put(ActivityTable.ACTIVITY_ID, activity_id);
        values.put(ActivityTable.MOOD_TYPE, mood_type);
        mDatabase.insert(ActivityTable.TABLE_MOOD, null, values);
        mDatabase.close();
}

    public void deleteSolid(int a_id) {
        mDatabase.delete(ActivityTable.TABLE_MOOD, ActivityTable.ACTIVITY_ID + " = " + a_id, null);
    }

    public HashMap<String, String> getMoodType(int a_id){
        //Databeseden id si belli olan row u çekmek için.
        //Bu methodda sadece tek row değerleri alınır.

        //HashMap bir çift boyutlu arraydir.anahtar-değer ikililerini bir arada tutmak için tasarlanmıştır.
        //mesala map.put("x","300"); mesala burda anahtar x değeri 300.

        HashMap<String,String> mood = new HashMap<String,String>();
        String selectQuery = "SELECT * FROM " + ActivityTable.TABLE_MOOD+ " WHERE a_id="+a_id;

        Cursor cursor = mDatabase.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            mood.put(ActivityTable.MOOD_TYPE, cursor.getString(2));
        }
        cursor.close();
        mDatabase.close();
        return mood;
    }

    public ArrayList<HashMap<String, String>> getAllMoods(){

        //Bu methodda ise tablodaki tüm değerleri alıyoruz
        //ArrayList adı üstünde Array lerin listelendiği bir Array.Burda hashmapleri listeleyeceğiz
        //Herbir satırı değer ve value ile hashmap a atıyoruz. Her bir satır 1 tane hashmap arrayı demek.
        //olusturdugumuz tüm hashmapleri ArrayList e atıp geri dönüyoruz(return).

        String selectQuery = "SELECT * FROM " + ActivityTable.TABLE_MOOD ;
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
    public ArrayList<HashMap<String, String>> getTodayMoods(String act_id,String currenDate,String yesterdayDateString){

        String columns[]={"m_id","a_id","m_type"};

        Cursor cursor = mDatabase.query(ActivityTable.TABLE_MOOD, columns, "a_id=? and select_date<=? and select_date>=?", new String[]{act_id, currenDate, yesterdayDateString}, null, null, null);
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
    public ArrayList<HashMap<String, String>> getSpecificMoodAsaActId(String a_id){

        String selectQuery = "SELECT * FROM " + ActivityTable.TABLE_MOOD+ " WHERE a_id="+ a_id;

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
    public HashMap<String, String> getMoodID(int a_id){

        HashMap<String,String> mood = new HashMap<String,String>();
        String selectQuery = "SELECT * FROM " + ActivityTable.TABLE_MOOD+ " WHERE a_id="+a_id;

        Cursor cursor = mDatabase.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            mood.put(ActivityTable.MOOD_ID, String.valueOf(cursor.getInt(0)));
        }
        cursor.close();
        mDatabase.close();
        return mood;
    }

    public void editMood(String mood_id, String m_type) {
        //Bu methodda ise var olan veriyi güncelliyoruz(update)
        ContentValues values = new ContentValues();
        values.put(ActivityTable.MOOD_TYPE,m_type);

        // updating row
        mDatabase.update(ActivityTable.TABLE_MOOD, values, ActivityTable.MOOD_ID + " = ?",
                new String[] { String.valueOf(mood_id) });
        mDatabase.close();
    }
    public void resetTables(){
        //Bunuda uygulamada kullanmıyoruz. Tüm verileri siler. tabloyu resetler.
        mDatabase.delete(ActivityTable.TABLE_MOOD, null, null);
        mDatabase.close();
    }

}
