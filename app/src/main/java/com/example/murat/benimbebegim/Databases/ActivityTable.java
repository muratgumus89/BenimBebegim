package com.example.murat.benimbebegim.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by Murat on 18.4.2015.
 */
public class ActivityTable extends SQLiteOpenHelper{
    // Değişkenleri Tanımlıyoruz.
    // Database Version
    public static final String TAG = "ActivityTable";
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "activity_databases.db";//database adı
    /*
    Areas of Activity Table
     */
    public static final String ACTIVITY_TABLE = "activitiy";
    public static final String ACTIVITY_ID = "a_id";
    public static final String ACTIVITY_TYPE = "a_type";
    public static final String SELECT_TIME = "select_time";
    public static final String SELECT_DATE = "select_date";
    public static final String SAVE_TIME = "save_time";
    public static final String SAVE_DATE = "save_date";
    public static final String TIMESTAMP = "time_stamp";
    public static final String NOTE = "note";
    public static final String BABY_ID = "b_id";
    public static final String USER_ID = "u_id";
    /*
    Areas of Mood Table
        */
    public static final String TABLE_MOOD = "mood";
    public static final String MOOD_ID = "m_id";
    public static final String MOOD_TYPE = "m_type";
    /*
    Areas of Solid Table
     */
    public static final String TABLE_SOLID = "solid";
    public static final String SOLID_ID = "solid_id";
    public static final String SOLID_BREAD = "bread";
    public static final String SOLID_FRUIT = "fruit";
    public static final String SOLID_CEREAL= "cereal";
    public static final String SOLID_MEAT = "meat";
    public static final String SOLID_DAIRY = "dairy";
    public static final String SOLID_PASTA = "pasta";
    public static final String SOLID_EGGS = "eggs";
    public static final String SOLID_VEGETABLE = "vegetable";
    public static final String SOLID_FISH = "fish";
    public static final String SOLID_OTHER = "other";
    public static final String SOLID_GRAM = "gram";
    /*
    Areas of Bottle Table
    */
    public static final String TABLE_BOTTLE   = "bottle";
    public static final String BOTTLE_ID      = "bottle_id";
    public static final String BOTTLE_TIMER   = "timer";
    public static final String BOTTLE_FORMULA = "formula";
    public static final String BOTTLE_AMOUNT  = "amount";

    String CREATE_SOLID = "CREATE TABLE " + TABLE_SOLID +"("
            + SOLID_ID    + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + ACTIVITY_ID + " INTEGER,"
            + SOLID_GRAM  + " INTEGER,"
            + SOLID_BREAD + " TEXT,"
            + SOLID_FRUIT + " TEXT,"
            + SOLID_CEREAL+ " TEXT,"
            + SOLID_MEAT  + " TEXT,"
            + SOLID_DAIRY + " TEXT,"
            + SOLID_PASTA + " TEXT,"
            + SOLID_EGGS  + " TEXT,"
            + SOLID_VEGETABLE + " TEXT,"
            + SOLID_FISH  + " TEXT,"
            + SOLID_OTHER + " TEXT" + ")";


    String CREATE_ACTIVITY = "CREATE TABLE " + ACTIVITY_TABLE + "("
            + ACTIVITY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + ACTIVITY_TYPE + " TEXT,"
            + BABY_ID + " INTEGER,"
            + USER_ID + " INTEGER,"
            + SELECT_DATE + " TEXT,"
            + SELECT_TIME+ " TEXT,"
            + SAVE_DATE + " TEXT,"
            + SAVE_TIME + " TEXT,"
            + NOTE + " TEXT,"
            + TIMESTAMP + " datetime default current_timestamp"+ ")";

    String CREATE_MOOD = "CREATE TABLE " + TABLE_MOOD + "("
            + MOOD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + ACTIVITY_ID + " INTEGER,"
            + MOOD_TYPE + " TEXT" + ")";

    String CREATE_BOTTLE = "CREATE TABLE " + TABLE_BOTTLE + "("
            + BOTTLE_ID      + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + ACTIVITY_ID    + " INTEGER,"
            + BOTTLE_FORMULA + " TEXT,"
            + BOTTLE_AMOUNT  + " INTEGER,"
            + BOTTLE_TIMER   + " TEXT" + ")";

    public ActivityTable(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {  // Databesi oluşturuyoruz.Bu methodu biz çağırmıyoruz. Databese de obje oluşturduğumuzda otamatik çağırılıyor.
        db.execSQL(CREATE_ACTIVITY);
        db.execSQL(CREATE_MOOD);
        db.execSQL(CREATE_SOLID);
        db.execSQL(CREATE_BOTTLE);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(TAG,
                "Upgrading the database from version " + oldVersion + " to " + newVersion);
        // clear all data
        db.execSQL("DROP TABLE IF EXISTS " + ACTIVITY_TABLE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MOOD);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SOLID);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BOTTLE);

        // recreate the tables
        onCreate(db);
    }

    public ActivityTable(Context context, String name, SQLiteDatabase.CursorFactory factory,int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }

    public void deleteRecord(int id){ //id si belli olan row u silmek için

        SQLiteDatabase db = this.getWritableDatabase();//Okuma Modunda Database i Aç
        db.delete(ACTIVITY_TABLE, ACTIVITY_ID + " = ?",
                new String[] { String.valueOf(id) });//mood_id=id olan kaydı sil
        db.close();
    }

    public void insertRecord(String a_type, String baby_id,String user_id,String select_date,
                         String select_time,String save_date,String save_time,String note) {
        //moodEkle methodu ise adı üstünde Databese veri eklemek için
        SQLiteDatabase db = this.getWritableDatabase();//Yazma modunda databese i aç.
        ContentValues values = new ContentValues();
        values.put(ACTIVITY_TYPE, a_type);
        values.put(BABY_ID, baby_id);
        values.put(USER_ID, user_id);
        values.put(SELECT_DATE, select_date);
        values.put(SELECT_TIME, select_time);
        values.put(SAVE_DATE,save_date);
        values.put(SAVE_TIME,save_time);
        values.put(NOTE,note);

        db.insert(ACTIVITY_TABLE, null, values);
        db.close(); //Database Bağlantısını kapattık

    }


    public HashMap<String, String> showDetay(int id){

        HashMap<String,String> activities = new HashMap<String,String>();
        String selectQuery = "SELECT * FROM " + ACTIVITY_TABLE + " WHERE id="+id;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            activities.put(SELECT_DATE, cursor.getString(4));
            activities.put(SELECT_TIME, cursor.getString(5));
            activities.put(SAVE_DATE, cursor.getString(6));
            activities.put(SAVE_TIME, cursor.getString(7));
            activities.put(NOTE, cursor.getString(8));
        }
        cursor.close();
        db.close();
        return activities;
    }

    public ArrayList<HashMap<String, String>> showAllRecord(String babyId){

        //Bu methodda ise tablodaki tüm değerleri alıyoruz
        //ArrayList adı üstünde Array lerin listelendiği bir Array.Burda hashmapleri listeleyeceğiz
        //Herbir satırı değer ve value ile hashmap a atıyoruz. Her bir satır 1 tane hashmap arrayı demek.
        //olusturdugumuz tüm hashmapleri ArrayList e atıp geri dönüyoruz(return).

        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT * FROM " + ACTIVITY_TABLE + " WHERE baby_id="+babyId;
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

    public ArrayList<HashMap<String, String>> showRecordForActivityType(String a_type,String babyId){

        //Bu methodda ise tablodaki tüm değerleri alıyoruz
        //ArrayList adı üstünde Array lerin listelendiği bir Array.Burda hashmapleri listeleyeceğiz
        //Herbir satırı değer ve value ile hashmap a atıyoruz. Her bir satır 1 tane hashmap arrayı demek.
        //olusturdugumuz tüm hashmapleri ArrayList e atıp geri dönüyoruz(return).
        String columns[]={"a_id","a_type","b_id","u_id","select_date","select_time","save_date","save_time","note"};

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(ACTIVITY_TABLE, columns, "a_type=? and b_id=?", new String[] { a_type, babyId }, null, null, null);

        ArrayList<HashMap<String, String>> activityList = new ArrayList<HashMap<String, String>>();

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                for(int i=0; i<cursor.getColumnCount();i++)
                {
                    map.put(cursor.getColumnName(i), cursor.getString(i));
                }

                activityList.add(map);
            } while (cursor.moveToNext());
        }
        db.close();
        return activityList;
    }

    public ArrayList<HashMap<String, String>> getTodaysData(String date,String babyId){
        String columns[]={"mood","detail","time"};
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(ACTIVITY_TABLE, columns, "baby_id=? and date=?", new String[] { babyId, date }, null, null, null);
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

    public void updateRecord(String a_type, String baby_id,String user_id,String select_date,
                             String select_time,String save_time,String save_date,String note,int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        //Bu methodda ise var olan veriyi güncelliyoruz(update)
        ContentValues values = new ContentValues();
        values.put(ACTIVITY_TYPE, a_type);
        values.put(BABY_ID, baby_id);
        values.put(USER_ID, user_id);
        values.put(SELECT_DATE, select_date);
        values.put(SELECT_TIME, select_time);
        values.put(SAVE_DATE,save_date);
        values.put(SAVE_TIME,save_time);
        values.put(NOTE,note);;

        // updating row
        db.update(ACTIVITY_TABLE, values, ACTIVITY_ID + " = ?",
                new String[] { String.valueOf(id) });
    }

    public int getRowCount() {
        String countQuery = "SELECT  * FROM " + ACTIVITY_TABLE;
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
        db.delete(ACTIVITY_TABLE, null, null);
        db.close();
    }

}
