package com.example.murat.benimbebegim.model;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.murat.benimbebegim.Databases.DiaryDatabase;
import com.example.murat.benimbebegim.MainActivity;

import java.util.HashMap;

/**
 * Created by Murat on 18.4.2015.
 */
public class DetailDiary extends Activity {
    TextView txttAd,txtYazar,txtYil,txtFiyat;
    Button btnDuzenle,btnSil;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_kitapdetay);

        ActionBar actionBar = getActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setTitle("Diary List");

/*        btnDuzenle = (Button)findViewById(R.id.btnDüzenle_Detay);
        btnSil     = (Button)findViewById(R.id.btnSil);
        txttAd     = (TextView)findViewById(R.id.adi_detay);
        txtYazar   = (TextView)findViewById(R.id.yazari_detay);
        txtYil     = (TextView)findViewById(R.id.yili_Detay);
        txtFiyat   = (TextView)findViewById(R.id.fiyati_detay);*/

        Intent intent=getIntent();
        id = intent.getIntExtra("id", 0);//id değerini integer olarak aldık. Burdaki 0 eğer değer alınmazsa default olrak verilecek değer

        DiaryDatabase db = new DiaryDatabase(getApplicationContext());
        HashMap<String, String> map = db.diaryDetay(id);//Bu id li row un değerini hashmap e aldık

        txttAd.setText(map.get("kitap_adi"));
        txtYazar.setText(map.get("yazar").toString());
        txtYil.setText(map.get("yil").toString());
        txtFiyat.setText(map.get("fiyat").toString());

        btnDuzenle.setOnClickListener(new View.OnClickListener() {//Kitap düzenle butonuna tıklandıgında tekrardan kitabın id sini gönderdik

            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), EditDiary.class);
                intent.putExtra("id", (int)id);
                startActivity(intent);
            }
        });

        btnSil.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                AlertDialog.Builder alertDialog = new AlertDialog.Builder(DetailDiary.this);
                alertDialog.setTitle("Uyarı");
                alertDialog.setMessage("Kitap Silinsin mi?");
                alertDialog.setPositiveButton("Evet", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {
                        DiaryDatabase db = new DiaryDatabase(getApplicationContext());
                        db.deleteDiary(id);
                        Toast.makeText(getApplicationContext(), "Kitap Başarıyla Silindi", Toast.LENGTH_LONG).show();
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        startActivity(intent);//bu id li kitabı sildik ve Anasayfaya döndük
                        finish();

                    }
                });
                alertDialog.setNegativeButton("Hayır", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog,int which) {

                    }
                });
                alertDialog.show();

            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default: return super.onOptionsItemSelected(item);
        }
    }
}
