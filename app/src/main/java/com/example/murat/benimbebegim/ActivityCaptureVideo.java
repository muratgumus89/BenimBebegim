package com.example.murat.benimbebegim;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class ActivityCaptureVideo extends Activity {


    private VideoView myVideoView;
    private int position = 0;
    private MediaController mediaControls;
    public Button capture_video,btnCancel,btnSave;
    public static ActivityCaptureVideo ActivityContext =null;
    public static int CAPTURE_VIDEO = 2;
    public Uri videoUri,fileUri;
    public static final int MEDIA_TYPE_VIDEO = 2;
    private static final int CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE = 200;
    File mediaFile;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Get the layout from video_main.xml
        setContentView(R.layout.activity_capture_video);
        ActivityContext = this;

        btnCancel     = (Button)findViewById(R.id.btnCaptureVideo_Cancel);
        capture_video = (Button)findViewById(R.id.bttn_capturevideo);
        btnSave       = (Button)findViewById(R.id.btnCaptureVideo_Save);

        // Find your VideoView in your video_main.xml layout
        myVideoView = (VideoView) findViewById(R.id.video_view);

        capture_video.setOnClickListener(new OnClickListener() {
          @Override
          public void onClick(View v) {

              Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

              // create a file to save the video
              fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);

              // set the image file name
              intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

              // set the video image quality to high
              intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

              // start the Video Capture Intent
              startActivityForResult(intent, CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE);
          }
        });
        btnSave.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                galleryAddVideo();
                Toast.makeText(getApplicationContext(),getResources().getString(R.string.video_save),Toast.LENGTH_SHORT).show();
            }
        });
        btnCancel.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(),getResources().getString(R.string.video_cancel),Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        });
        myVideoView.requestFocus();
        myVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            // Close the progress bar and play the video
            public void onPrepared(MediaPlayer mp) {
                myVideoView.seekTo(position);
                if (position == 0) {
                    myVideoView.start();
                } else {
                    myVideoView.pause();
                }
            }
        });
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // After camera screen this code will excuted

        if (requestCode == CAPTURE_VIDEO_ACTIVITY_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {
                // Video captured and saved to fileUri specified in the Intent

            } else if (resultCode == RESULT_CANCELED) {
                // User cancelled the video capture
                Toast.makeText(this, "User cancelled the video capture.",
                        Toast.LENGTH_LONG).show();
            } else {

                // Video capture failed, advise user
                Toast.makeText(this, "Video capture failed.",
                        Toast.LENGTH_LONG).show();
            }
                if (mediaControls == null) {
                    mediaControls = new MediaController(ActivityCaptureVideo.this);
                }
                myVideoView.setVideoURI(data.getData());
                myVideoView.setMediaController(mediaControls);
        }
    }
    private void galleryAddVideo() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mediaFile.getPath());
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putInt("Position", myVideoView.getCurrentPosition());
        myVideoView.pause();
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        position = savedInstanceState.getInt("Position");
        myVideoView.seekTo(position);
    }
    /** Create a file Uri for saving an image or video */
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /** Create a File for saving an image or video */
    private File getOutputMediaFile(int type) {
        // Check that the SDCard is mounted
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_MOVIES),"Baby Sitter Videos");


        // Create the storage directory(MyCameraVideo) if it does not exist
        if (! mediaStorageDir.exists()){

            if (! mediaStorageDir.mkdirs()){

                Toast.makeText(ActivityContext, "Failed to create directory MyCameraVideo.",
                        Toast.LENGTH_LONG).show();

                Log.d("MyCameraVideo", "Failed to create directory MyCameraVideo.");
                return null;
            }
        }


        // Create a media file name

        // For unique file name appending current timeStamp with file name
        java.util.Date date= new java.util.Date();
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss")
                .format(date.getTime());


        if(type == MEDIA_TYPE_VIDEO) {

            // For unique video file name appending current timeStamp with file name
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_"+ timeStamp + ".mp4");

        } else {
            return null;
        }

        return mediaFile;
    }
}
