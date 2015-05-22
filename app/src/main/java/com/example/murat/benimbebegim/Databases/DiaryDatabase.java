package com.example.murat.benimbebegim.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Murat on 18.4.2015.
 */
public class DiaryDatabase extends SQLiteOpenHelper {
    // Değişkenleri Tanımlıyoruz.
    // Database Version
    public static final String TAG = "Diary Database";
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "babysitter_diary.db";//database adı

    public static final String TABLE_NAME = "diary";
    public static String DIARY_TITLE      = "diary_title";
    public static String DIARY_ID         = "diary_id";
    public static String DIARY_MESSAGE    = "diary_message";
    public static String DIARY_AUDIO_PATH = "diary_audio";
    public static String BAYB_ID          = "baby_id";
    public static String DIARY_DAY        = "day";
    public static String DIARY_MONTH      = "month";
    public static String DIARY_DAY_NAME   = "day_name";
    public static String DIARY_YEAR       = "year";


    public DiaryDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {  // Databesi oluşturuyoruz.Bu methodu biz çağırmıyoruz. Databese de obje oluşturduğumuzda otamatik çağırılıyor.
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + DIARY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + BAYB_ID  + " TEXT,"
                + DIARY_DAY + " TEXT,"
                + DIARY_MONTH + " TEXT,"
                + DIARY_YEAR + " TEXT,"
                + DIARY_DAY_NAME + " TEXT,"
                + DIARY_TITLE + " TEXT,"
                + DIARY_MESSAGE + " TEXT,"
                + DIARY_AUDIO_PATH + " TEXT" + ")";
        db.execSQL(CREATE_TABLE);
    }


    public void deleteDiary(int id){ //id si belli olan row u silmek için

        SQLiteDatabase db = this.getWritableDatabase();//Okuma Modunda Database i Aç
        db.delete(TABLE_NAME, DIARY_ID + " = ?",
                new String[] { String.valueOf(id) });//kitap_id=id olan kaydı sil
        db.close();
    }

    public void addDiary(String baby_id,String day,String month,String year,String day_name,String diary_title, String diary_message,String diary_audio_path) {

        SQLiteDatabase db = this.getWritableDatabase();//Yazma modunda databese i aç.
        ContentValues values = new ContentValues();
        values.put(BAYB_ID         , baby_id);
        values.put(DIARY_DAY       , day);
        values.put(DIARY_MONTH     , month);
        values.put(DIARY_YEAR      , year);
        values.put(DIARY_DAY_NAME  , day_name);
        values.put(DIARY_TITLE     , diary_title);
        values.put(DIARY_MESSAGE   , diary_message);
        values.put(DIARY_AUDIO_PATH, diary_audio_path);


        db.insert(TABLE_NAME, null, values);
        db.close(); //Database Bağlantısını kapattık*/
    }


    public HashMap<String, String> diaryDetay(int id){

        HashMap<String,String> diary = new HashMap<String,String>();
        String selectQuery = "SELECT * FROM " + TABLE_NAME+ " WHERE diary_id="+id;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            diary.put(BAYB_ID         , cursor.getString(1));
            diary.put(DIARY_DAY       , cursor.getString(2));
            diary.put(DIARY_MONTH     , cursor.getString(3));
            diary.put(DIARY_YEAR      , cursor.getString(4));
            diary.put(DIARY_DAY_NAME  , cursor.getString(5));
            diary.put(DIARY_TITLE     , cursor.getString(6));
            diary.put(DIARY_MESSAGE   , cursor.getString(7));
            diary.put(DIARY_AUDIO_PATH, cursor.getString(8));
        }
        cursor.close();
        db.close();
        // return kitap
        return diary;
    }

    public ArrayList<HashMap<String, String>> diaries(String baby_id){

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " WHERE baby_id=" + baby_id;
        Cursor cursor = db.rawQuery(selectQuery, null);
        ArrayList<HashMap<String, String>> diaryList = new ArrayList<HashMap<String, String>>();

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                for(int i=0; i<cursor.getColumnCount();i++)
                {
                    map.put(cursor.getColumnName(i), cursor.getString(i));
                }

                diaryList.add(map);
            } while (cursor.moveToNext());
        }
        db.close();
        // return kitap liste
        return diaryList;
    }

    public void editDiary(String baby_id,String day,String month,String year,String day_name,String diary_title, String diary_message,String diary_audio_path,int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        //Bu methodda ise var olan veriyi güncelliyoruz(update)
        ContentValues values = new ContentValues();
        values.put(BAYB_ID         , baby_id);
        values.put(DIARY_DAY       , day);
        values.put(DIARY_MONTH     , month);
        values.put(DIARY_YEAR      , year);
        values.put(DIARY_DAY_NAME  , day_name);
        values.put(DIARY_TITLE     , diary_title);
        values.put(DIARY_MESSAGE   , diary_message);
        values.put(DIARY_AUDIO_PATH, diary_audio_path);

        // updating row
        db.update(TABLE_NAME, values, DIARY_ID + " = ?",
                new String[] { String.valueOf(id) });
    }

    public int getRowCount(String baby_id) {
        String countQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE baby_id=" + baby_id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int rowCount = cursor.getCount();
        db.close();
        cursor.close();
        // return row count
        return rowCount;
    }
    public HashMap<String, String> getId(String path) {
        HashMap<String,String> diary = new HashMap<String,String>();
        String countQuery = "SELECT  * FROM " + TABLE_NAME + " WHERE diary_audio=" + path;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            diary.put(DIARY_ID, String.valueOf(cursor.getInt(0)));
        }
        db.close();
        cursor.close();
        // return row count
        return diary;
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
