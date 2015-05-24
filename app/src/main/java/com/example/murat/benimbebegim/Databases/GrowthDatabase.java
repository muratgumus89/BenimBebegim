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
public class GrowthDatabase extends SQLiteOpenHelper {
    // Değişkenleri Tanımlıyoruz.
    // Database Version
    public static final String TAG = "Growth Database";
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "babysitter_growth.db";//database adı

    public static final String TABLE_WEIGHT = "weight_table";
    public static String WEIGHT_ID          = "weight_id";
    public static String WEIGHT_DATE        = "weight_date";
    public static String WEIGHT             = "weight";


    public static final String TABLE_HEIGHT = "height_table";
    public static String HEIGHT_ID          = "height_id";
    public static String HEIGHT_DATE        = "height_date";
    public static String HEIGHT             = "height";

    public static String BAYB_ID          = "baby_id";

    String CREATE_WEIGHT = "CREATE TABLE " + TABLE_WEIGHT +"("
            + WEIGHT_ID    + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + BAYB_ID + " TEXT,"
            + WEIGHT_DATE + " TEXT,"
            + WEIGHT + " INTEGER)";

    String CREATE_HEIGHT = "CREATE TABLE " + TABLE_HEIGHT +"("
            + HEIGHT_ID    + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + BAYB_ID + " TEXT,"
            + HEIGHT_DATE + " TEXT,"
            + HEIGHT + " INTEGER)";

    public GrowthDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {  // Databesi oluşturuyoruz.Bu methodu biz çağırmıyoruz. Databese de obje oluşturduğumuzda otamatik çağırılıyor.
        db.execSQL(CREATE_WEIGHT);
        db.execSQL(CREATE_HEIGHT);
    }


    public void deleteWeight(int id){ //id si belli olan row u silmek için

        SQLiteDatabase db = this.getWritableDatabase();//Okuma Modunda Database i Aç
        db.delete(TABLE_WEIGHT, WEIGHT_ID + " = ?",
                new String[]{String.valueOf(id)});//kitap_id=id olan kaydı sil
        db.close();
    }

    public void deleteHeight(int id){ //id si belli olan row u silmek için

        SQLiteDatabase db = this.getWritableDatabase();//Okuma Modunda Database i Aç
        db.delete(TABLE_HEIGHT, HEIGHT_ID + " = ?",
                new String[]{String.valueOf(id)});//kitap_id=id olan kaydı sil
        db.close();
    }

    public void addWeight(String baby_id,String date,int weight) {

        SQLiteDatabase db = this.getWritableDatabase();//Yazma modunda databese i aç.
        ContentValues values = new ContentValues();
        values.put(BAYB_ID         , baby_id);
        values.put(WEIGHT_DATE, date);
        values.put(WEIGHT, weight);
        db.insert(TABLE_WEIGHT, null, values);
        db.close(); //Database Bağlantısını kapattık*/
    }

    public void addHeight(String baby_id,String date,int height) {

        SQLiteDatabase db = this.getWritableDatabase();//Yazma modunda databese i aç.
        ContentValues values = new ContentValues();
        values.put(BAYB_ID         , baby_id);
        values.put(HEIGHT_DATE, date);
        values.put(HEIGHT, height);
        db.insert(TABLE_HEIGHT, null, values);
        db.close(); //Database Bağlantısını kapattık*/
    }

    public ArrayList<HashMap<String,String>> getWeight(String baby_id, String date1,String date2){ /**Solid Information gathers here*/
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select sum(bottle.[amount]), bottle.[timer], activitiy.[b_id] " +
                "from activitiy, bottle " +
                "where activitiy.[a_type] = 'Bottle' " +
                "and activitiy.[a_id] = bottle.[a_id] " +
                "and activitiy.[select_date] like '%" + date2.substring(2) + "' and activitiy.[b_id] = '" + baby_id + "'";

        Cursor cursor = db.rawQuery(query, null);
        ArrayList<HashMap<String, String>> moodlist = new ArrayList<HashMap<String, String>>();

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    map.put(cursor.getColumnName(i), cursor.getString(i));
                }
                moodlist.add(map);
            }while (cursor.moveToNext());
        }
        db.close();
        return moodlist;
    }
    public ArrayList<HashMap<String,String>> getHeight(String baby_id, String date1,String date2){ /**Solid Information gathers here*/
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select sum(bottle.[amount]), bottle.[timer], activitiy.[b_id] " +
                "from activitiy, bottle " +
                "where activitiy.[a_type] = 'Bottle' " +
                "and activitiy.[a_id] = bottle.[a_id] " +
                "and activitiy.[select_date] like '%" + date1.substring(2) + "' and activitiy.[b_id] = '" + baby_id + "'";

        Cursor cursor = db.rawQuery(query, null);
        ArrayList<HashMap<String, String>> moodlist = new ArrayList<HashMap<String, String>>();

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    map.put(cursor.getColumnName(i), cursor.getString(i));
                }
                moodlist.add(map);
            }while (cursor.moveToNext());
        }
        db.close();
        return moodlist;
    }

    public HashMap<String, String> getWeightId(String baby_id) {
        HashMap<String,String> diary = new HashMap<String,String>();
        String countQuery = "SELECT  * FROM " + TABLE_WEIGHT + " WHERE baby_id=" + baby_id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            diary.put(WEIGHT_ID, String.valueOf(cursor.getInt(0)));
        }
        db.close();
        cursor.close();
        // return row count
        return diary;
    }

    public HashMap<String, String> getHeightId(String baby_id) {
        HashMap<String,String> diary = new HashMap<String,String>();
        String countQuery = "SELECT  * FROM " + TABLE_HEIGHT + " WHERE baby_id=" + baby_id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            diary.put(HEIGHT_ID, String.valueOf(cursor.getInt(0)));
        }
        db.close();
        cursor.close();
        // return row count
        return diary;
    }
    public void resetTables(){
        //Bunuda uygulamada kullanmıyoruz. Tüm verileri siler. tabloyu resetler.
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_WEIGHT, null, null);
        db.delete(TABLE_HEIGHT, null, null);
        db.close();
    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
        // TODO Auto-generated method stub
    }

    public ArrayList<HashMap<String,String>> getAllHeightAndWeight(String a_id){
        //select weight_table.[baby_id],weight_table.[weight], height_table.[height]
        // from weight_table, height_table
        // where height_table.[height_id] = weight_table.[weight_id]
        // and [weight_table].[baby_id] = height_table.[baby_id]
        // and height_table.[baby_id] = '40'
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select weight_table.[baby_id],weight_table.[weight], height_table.[height] " +
                "from weight_table, height_table " +
                "where height_table.[height_id] = weight_table.[weight_id] " +
                "and [weight_table].[baby_id] = height_table.[baby_id] " +
                "and height_table.[baby_id] = '" + a_id + "'";

        Cursor cursor = db.rawQuery(query, null);
        ArrayList<HashMap<String, String>> moodlist = new ArrayList<HashMap<String, String>>();

        Log.i("coloumn count", String.valueOf(cursor.getColumnCount()));
        Log.i("row Count", String.valueOf(cursor.getCount()));
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    map.put(cursor.getColumnName(i), cursor.getString(i));
                }
                moodlist.add(map);
            }while (cursor.moveToNext());
        }
        db.close();
        return moodlist;
    }

    public ArrayList<HashMap<String,String>> getAllHeightAndWeightCount(String a_id){
        //select COUNT(weight_table.[baby_id]) as count
        // from weight_table, height_table
        // where height_table.[height_id] = weight_table.[weight_id]
        // and [weight_table].[baby_id] = height_table.[baby_id]
        // and height_table.[baby_id] = '40'
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "select COUNT(weight_table.[baby_id]) as count " +
                "from weight_table, height_table where height_table.[height_id] = weight_table.[weight_id] " +
                "and [weight_table].[baby_id] = height_table.[baby_id] " +
                "and height_table.[baby_id] = '" + a_id + "'";

        Cursor cursor = db.rawQuery(query, null);
        ArrayList<HashMap<String, String>> moodlist = new ArrayList<HashMap<String, String>>();

        Log.i("coloumn count", String.valueOf(cursor.getColumnCount()));
        Log.i("row Count", String.valueOf(cursor.getCount()));
        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    map.put(cursor.getColumnName(i), cursor.getString(i));
                }
                moodlist.add(map);
            }while (cursor.moveToNext());
        }
        db.close();
        return moodlist;
    }
}
