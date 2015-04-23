package com.example.murat.benimbebegim.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Created by Murat on 18.4.2015.
 */
public class MoodDatabase extends SQLiteOpenHelper{
    // Değişkenleri Tanımlıyoruz.
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "my_database_events.db";//database adı

    private static final String TABLE_NAME = "mood_db";
    private static String BABY_ID = "baby_id";
    private static String MOOD_ID = "m_id";
    private static String TIME = "time";
    private static String DATE = "date";
    private static String SAVE_TIME = "s_time";
    private static String SAVE_DATE = "s_date";
    private static String SAVING_TIMEDATE = "record_timeF";
    private static String DETAIL = "detail";
    private static String MOOD = "mood";

    public MoodDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {  // Databesi oluşturuyoruz.Bu methodu biz çağırmıyoruz. Databese de obje oluşturduğumuzda otamatik çağırılıyor.
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + MOOD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + BABY_ID + " INTEGER,"
                + MOOD + " TEXT,"
                + DETAIL+ " TEXT,"
                + TIME + " TEXT,"
                + DATE + " TEXT,"
                + SAVE_TIME + " TEXT,"
                + SAVING_TIMEDATE + " datetime default current_timestamp,"
                + SAVE_DATE + " TEXT" + ")";
        db.execSQL(CREATE_TABLE);
    }


    public void deleteMood(int id){ //id si belli olan row u silmek için

        SQLiteDatabase db = this.getWritableDatabase();//Okuma Modunda Database i Aç
        db.delete(TABLE_NAME, MOOD_ID + " = ?",
                new String[] { String.valueOf(id) });//mood_id=id olan kaydı sil
        db.close();
    }

    public void addMood(String baby_id, String mood,String detail,String time,
                         String date,String s_time,String s_date) {
        //moodEkle methodu ise adı üstünde Databese veri eklemek için
        SQLiteDatabase db = this.getWritableDatabase();//Yazma modunda databese i aç.
        ContentValues values = new ContentValues();
        values.put(BABY_ID, baby_id);
        values.put(MOOD, mood);
        values.put(DETAIL, detail);
        values.put(TIME, time);
        values.put(DATE, date);
        values.put(SAVE_TIME,s_time);
        values.put(SAVE_DATE,s_date);

        db.insert(TABLE_NAME, null, values);
        db.close(); //Database Bağlantısını kapattık*/
    }


    public HashMap<String, String> moodDetay(int id){
        //Databeseden id si belli olan row u çekmek için.
        //Bu methodda sadece tek row değerleri alınır.

        //HashMap bir çift boyutlu arraydir.anahtar-değer ikililerini bir arada tutmak için tasarlanmıştır.
        //mesala map.put("x","300"); mesala burda anahtar x değeri 300.

        HashMap<String,String> mood = new HashMap<String,String>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME+ " WHERE id="+id;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            mood.put(MOOD, cursor.getString(2));
            mood.put(DETAIL, cursor.getString(3));
            mood.put(TIME, cursor.getString(4));
            mood.put(DATE, cursor.getString(5));
            mood.put(SAVE_DATE, cursor.getString(5));
            mood.put(SAVE_TIME, cursor.getString(5));
        }
        cursor.close();
        db.close();
        return mood;
    }

    public ArrayList<HashMap<String, String>> moods(String babyId){

        //Bu methodda ise tablodaki tüm değerleri alıyoruz
        //ArrayList adı üstünde Array lerin listelendiği bir Array.Burda hashmapleri listeleyeceğiz
        //Herbir satırı değer ve value ile hashmap a atıyoruz. Her bir satır 1 tane hashmap arrayı demek.
        //olusturdugumuz tüm hashmapleri ArrayList e atıp geri dönüyoruz(return).

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE baby_id="+babyId;
        Cursor cursor = db.rawQuery(selectQuery, null);
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
        db.close();
        return moodlist;
    }

    public ArrayList<HashMap<String, String>> getTodaysData(String date,String babyId){
        String columns[]={"mood","detail","time"};
        SQLiteDatabase db = this.getReadableDatabase();
        /*String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE baby_id=" +babyId +"AND date="+date;*/
        Cursor cursor = db.query(TABLE_NAME, columns, "baby_id=? and date=?", new String[] { babyId, date }, null, null, null);
        //Cursor cursor = db.rawQuery(selectQuery, null);
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
        db.close();
        return moodlist;
    }

    public void editMood(String baby_id, String mood,String detail,String time,
                         String date,String s_time,String s_date,int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        //Bu methodda ise var olan veriyi güncelliyoruz(update)
        ContentValues values = new ContentValues();
        values.put(BABY_ID, baby_id);
        values.put(MOOD, mood);
        values.put(DETAIL, detail);
        values.put(TIME, time);
        values.put(DATE, date);
        values.put(SAVE_TIME,s_time);
        values.put(SAVE_DATE,s_date);

        // updating row
        db.update(TABLE_NAME, values, MOOD_ID + " = ?",
                new String[] { String.valueOf(id) });
    }

    public int getRowCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();
        return rowCount;
    }


    public void resetTables(){
        //Bunuda uygulamada kullanmıyoruz. Tüm verileri siler. tabloyu resetler.
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub

    }
}
