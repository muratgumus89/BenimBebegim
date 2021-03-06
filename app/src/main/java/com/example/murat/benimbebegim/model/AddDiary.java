package com.example.murat.benimbebegim.model;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.murat.benimbebegim.Databases.DiaryDatabase;
import com.example.murat.benimbebegim.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class AddDiary extends Activity implements View.OnClickListener {
    EditText etTitle,etMessage;
    Button btnStart,btnStop,btnFinish,btnPlay,btnDate,btnSpeech,btnCancel,btnSave;
    TextView txtVoice;
    private MediaRecorder myAudioRecorder;
    private String outputFile = null;
    File mediaFile;
    private MediaRecorder myRecorder;
    private MediaPlayer myPlayer;
    String currentDate,dayOfTheWeek;
    public static final String TAG = "Add-Diary";
    private final int REQ_CODE_SPEECH_INPUT = 100;
    public static final String PREFS_NAME = "MyPrefsFile";
    String baby_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_diary_record);

        //Get babyid from SP
        SharedPreferences pref;
        pref = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        baby_id = pref.getString("baby_id", null);

        init();
    }

    private void init() {
        etTitle   = (EditText)findViewById(R.id.edtTitle_Add_Diary);
        etMessage = (EditText)findViewById(R.id.edtMessage_Add_Diary);
        btnCancel = (Button)findViewById(R.id.btnCancel_Add_Diary);
        btnSave   = (Button)findViewById(R.id.btnSave_Add_Diary);
        btnDate   = (Button)findViewById(R.id.btnDate_Add_Diary);
        btnSpeech = (Button)findViewById(R.id.btnSpeech2Text_Add_Diary);
        btnStart  = (Button)findViewById(R.id.btnStart_Add_Diary);
        btnFinish = (Button)findViewById(R.id.btnFinish_Add_Diary);
        btnPlay   = (Button)findViewById(R.id.btnPlay_Add_Diary);
        btnStop   = (Button)findViewById(R.id.btnStop_Add_Diary);
        txtVoice  = (TextView)findViewById(R.id.txtVoice_Add_Diary);

        btnCancel.setOnClickListener(this);
        btnSave.setOnClickListener(this);
        btnSpeech.setOnClickListener(this);
        btnStart.setOnClickListener(this);
        btnFinish.setOnClickListener(this);
        btnPlay.setOnClickListener(this);
        btnStop.setOnClickListener(this);

        btnFinish.setEnabled(false);
        btnPlay.setEnabled(false);
        btnStop.setEnabled(false);

        saveAudioFile();

        /******************
         * Naming The Date Button
         */
        Calendar c_date = Calendar.getInstance();
        SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
        currentDate = date.format(c_date.getTime());
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE");
        Date d = new Date();
        dayOfTheWeek = sdf.format(d);
        btnDate.setText(currentDate + "\n" + dayOfTheWeek);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnStart_Add_Diary:
                    start(v);
                break;
            case R.id.btnFinish_Add_Diary:
                    finish(v);
                    //saveAudioFile();
                break;
            case R.id.btnPlay_Add_Diary:
                    //fillAudioFile();
                    play(v);
                break;
            case R.id.btnStop_Add_Diary:
                stopPlay(v);
                break;
            case R.id.btnCancel_Add_Diary:
                etMessage.setText("");
                etTitle.setText("");
                onBackPressed();
                break;
            case R.id.btnSpeech2Text_Add_Diary:
                speechToText();
                break;
            case R.id.btnSave_Add_Diary:
                saveToDatabase();
                break;
            default:
                break;
        }
    }

    private void speechToText() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT,
                "Say something");
        try {
            startActivityForResult(intent, REQ_CODE_SPEECH_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Your device doesn\\'t support speech input",
                    Toast.LENGTH_SHORT).show();
        }
    }
    /**
     * Receiving speech input
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_SPEECH_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    etMessage.setText(result.get(0));
                }
                break;
            }

        }
    }

    private void saveToDatabase() {
        String day,month,year,day_name,message,title,path;
        day      = currentDate.substring(0,2);
        month    = currentDate.substring(3,5);
        year     = currentDate.substring(6,10);
        day_name = dayOfTheWeek;
        message  = etMessage.getText().toString();
        title    = etTitle.getText().toString();
        path     = mediaFile.toString();
        DiaryDatabase db = new DiaryDatabase(getApplicationContext());
        db.addDiary(baby_id, day, month, year, day_name, title, message, path);
        db.close();
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.added_record), Toast.LENGTH_LONG).show();
        etMessage.setText("");
        etTitle.setText("");
        onBackPressed();
        //etMessage.setText(day + "/" + month + "/" + year + "-" + day_name + "\n" + title + "\n" + message + "\n" + path);
    }

    private void saveAudioFile() {
        // Check that the SDCard is mounted
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_MUSIC),"Baby_Sitter_Audios");


        // Create the storage directory(MyCameraVideo) if it does not exist
        if (! mediaStorageDir.exists()){

            if (! mediaStorageDir.mkdirs()){

                Toast.makeText(AddDiary.this, "Failed to create directory Baby_Sitter_Audio",
                        Toast.LENGTH_LONG).show();
            }
        }

        java.util.Date date= new java.util.Date();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(date.getTime());

        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "AUDIO_"+ timeStamp + ".3gpp");


        outputFile = mediaFile.toString();

        myRecorder = new MediaRecorder();
        myRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myRecorder.setOutputFile(outputFile);
    }
    private void fillAudioFile() {

        outputFile = mediaFile.toString();

        myRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        myRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        myRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        myRecorder.setOutputFile(outputFile);
    }
    public void start(View view){
        try {
            myRecorder.prepare();
            myRecorder.start();
        } catch (IllegalStateException e) {
            // start:it is called before prepare()
            // prepare: it is called after start() or before setOutputFormat()
            e.printStackTrace();
        } catch (IOException e) {
            // prepare() fails
            e.printStackTrace();
        }
        txtVoice.setText(getResources().getString(R.string.voice_record) + "-->" + getResources().getString(R.string.recording));
        btnStart.setEnabled(false);
        btnFinish.setEnabled(true);
        btnPlay.setEnabled(false);
        btnStop.setEnabled(false);
        Toast.makeText(getApplicationContext(), getResources().getString(R.string.recording_start), Toast.LENGTH_LONG).show();
    }
    public void finish(View view){
        try {
            myRecorder.stop();
            myRecorder.release();
            myRecorder  = null;

            btnStop.setEnabled(false);
            btnPlay.setEnabled(true);
            btnFinish.setEnabled(false);
            btnStart.setEnabled(true);
            txtVoice.setText(getResources().getString(R.string.voice_record) + "-->" + getResources().getString(R.string.ready_to_play));
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.recording_finish),
                    Toast.LENGTH_SHORT).show();
        } catch (IllegalStateException e) {
            //  it is called before start()
            e.printStackTrace();
        } catch (RuntimeException e) {
            // no valid audio/video data has been received
            e.printStackTrace();
        }
    }
    public void play(View view) {
        try{
            myPlayer = new MediaPlayer();
            myPlayer.setDataSource(outputFile);
            myPlayer.prepare();
            myPlayer.start();

            btnPlay.setEnabled(false);
            btnStop.setEnabled(true);
            btnStart.setEnabled(false);
            btnFinish.setEnabled(false);
            txtVoice.setText(getResources().getString(R.string.voice_record) + "-->" + getResources().getString(R.string.playing));

            Toast.makeText(getApplicationContext(), getResources().getString(R.string.recording_play), Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public void stopPlay(View view) {
        try {
            if (myPlayer != null) {
                myPlayer.stop();
                myPlayer.release();
                myPlayer = null;
                btnPlay.setEnabled(true);
                btnStart.setEnabled(true);
                btnStop.setEnabled(false);
                btnFinish.setEnabled(false);
                txtVoice.setText(getResources().getString(R.string.voice_record) + "-->" + getResources().getString(R.string.finished));

                Toast.makeText(getApplicationContext(), getResources().getString(R.string.recording_stop),
                        Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
