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
public class Medicine {

    private Context mContext;

    // Database fields
    private SQLiteDatabase mDatabase;
    private ActivityTable mDbHelper;
    private String[] mAllColumns = { ActivityTable.ACTIVITY_ID, ActivityTable.MEDICINE_TYPE,ActivityTable.MEDICINE_DOSE,ActivityTable.MEDICINE_DOSE_TYPE};

    public Medicine(Context context) {
        mDbHelper = new ActivityTable(context);
        this.mContext = context;
        // open the database
        try {
            open();
        }
        catch(SQLException e) {
            Log.e("Medicine", "SQLException on openning database " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void open() throws SQLException {
        mDatabase = mDbHelper.getWritableDatabase();
    }

    public void close() {
        mDbHelper.close();
    }


    public void insertMedicine(int activity_id,String medicine_type,String medicine_dose,String medicine_doseType) {
        ContentValues values = new ContentValues();
        values.put(ActivityTable.ACTIVITY_ID, activity_id);
        values.put(ActivityTable.MEDICINE_TYPE, medicine_type);
        values.put(ActivityTable.MEDICINE_DOSE, medicine_dose);
        values.put(ActivityTable.MEDICINE_DOSE_TYPE,medicine_doseType);
        mDatabase.insert(ActivityTable.TABLE_MEDICINE, null, values);
        mDatabase.close();
}

    public void deleteMedicine(int a_id) {
        mDatabase.delete(ActivityTable.TABLE_MEDICINE, ActivityTable.ACTIVITY_ID + " = " + a_id, null);
    }

    public HashMap<String, String> getMedicineID(String a_id){

        HashMap<String,String> medicineID = new HashMap<String,String>();
        String selectQuery = "SELECT * FROM " + ActivityTable.TABLE_MEDICINE + " WHERE a_id="+a_id;

        Cursor cursor = mDatabase.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if(cursor.getCount() > 0){
            medicineID.put(ActivityTable.MEDICINE_ID, String.valueOf(cursor.getInt(0)));
        }
        cursor.close();
        mDatabase.close();
        return medicineID;
    }

    public ArrayList<HashMap<String, String>> getAllMedicines(){

        String selectQuery = "SELECT * FROM " + ActivityTable.TABLE_MEDICINE ;
        Cursor cursor = mDatabase.rawQuery(selectQuery, null);
        ArrayList<HashMap<String, String>> medicineList = new ArrayList<HashMap<String, String>>();

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                for(int i=0; i<cursor.getColumnCount();i++)
                {
                    map.put(cursor.getColumnName(i), cursor.getString(i));
                }
                medicineList.add(map);
            } while (cursor.moveToNext());
        }
        mDatabase.close();
        return medicineList;
    }
    public ArrayList<HashMap<String, String>> getSpecificMedicineWithAID(String a_id){

        String selectQuery = "SELECT * FROM " + ActivityTable.TABLE_MEDICINE + " WHERE a_id="+ a_id;
        Cursor cursor = mDatabase.rawQuery(selectQuery, null);
        ArrayList<HashMap<String, String>> medicineList = new ArrayList<HashMap<String, String>>();

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                for(int i=0; i<cursor.getColumnCount();i++)
                {
                    map.put(cursor.getColumnName(i), cursor.getString(i));
                }
                medicineList.add(map);
            } while (cursor.moveToNext());
        }
        mDatabase.close();
        return medicineList;
    }
    public ArrayList<HashMap<String, String>> getSpecificMedicineWithBID(String medicine_id){

        String selectQuery = "SELECT * FROM " + ActivityTable.TABLE_HEALTH + " WHERE medicine_id="+ medicine_id;
        Cursor cursor = mDatabase.rawQuery(selectQuery, null);
        ArrayList<HashMap<String, String>> medicineList = new ArrayList<HashMap<String, String>>();

        if (cursor.moveToFirst()) {
            do {
                HashMap<String, String> map = new HashMap<String, String>();
                for(int i=0; i<cursor.getColumnCount();i++)
                {
                    map.put(cursor.getColumnName(i), cursor.getString(i));
                }
                medicineList.add(map);
            } while (cursor.moveToNext());
        }
        mDatabase.close();
        return medicineList;
    }

    public void editMedicine(int activity_id,String medicine_type,String medicine_dose,String medicine_doseType,String medicine_id) {
        //Bu methodda ise var olan veriyi güncelliyoruz(update)
        ContentValues values = new ContentValues();
        values.put(ActivityTable.ACTIVITY_ID, activity_id);
        values.put(ActivityTable.MOOD_TYPE, medicine_type);
        values.put(ActivityTable.MEDICINE_DOSE, medicine_dose);
        values.put(ActivityTable.MEDICINE_DOSE_TYPE,medicine_doseType);
        mDatabase.update(ActivityTable.TABLE_MEDICINE, values, ActivityTable.MEDICINE_ID + " = ?",
                new String[]{String.valueOf(medicine_id)});
        mDatabase.close();
    }
    public void resetTables(){
        //Bunuda uygulamada kullanmıyoruz. Tüm verileri siler. tabloyu resetler.
        mDatabase.delete(ActivityTable.TABLE_MEDICINE, null, null);
        mDatabase.close();
    }

}
