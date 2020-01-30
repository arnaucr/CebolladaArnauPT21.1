package com.example.sonsacr;

import android.content.ClipData;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.media.SoundPool;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SoundPool.OnLoadCompleteListener {


    MediaRecorder recorder;
    File audiofile = null;
    private static final String TAG = "SoundRecordingActivity";
    private View startButton;
    private View stopButton;




    SoundPool soundpool;
    private int soundID1;
    private int soundID2;
    private int soundID3;
    private int soundID4;
    int loaded;
    MediaPlayer mediaPlayer = new MediaPlayer();
    private int sounds;
    boolean on = true;
    ImageButton btn1, btn2, btn3, btn4;
    Boolean bjustPassed = false;

    String songURL;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        btn1 = (ImageButton) findViewById(R.id.btn1);
        btn2 = (ImageButton) findViewById(R.id.btn2);
        btn3 = (ImageButton) findViewById(R.id.btn3);
        btn4 = (ImageButton) findViewById(R.id.btn4);
        startButton = findViewById(R.id.start);
        stopButton = findViewById(R.id.stop);

        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        btn4.setOnClickListener(this);

        soundpool = new SoundPool(1, AudioManager.STREAM_MUSIC, 0);
        soundID1 = soundpool.load(this, R.raw.swamp, 1);
        soundID2 = soundpool.load(this, R.raw.saber, 1);
        soundID3 = soundpool.load(this, R.raw.bomb, 1);
        soundID4 = soundpool.load(this, R.raw.ouch, 1);

        soundpool.setOnLoadCompleteListener(this);
        songURL = "http://programmerguru.com/android-tutorial/wp-content/uploads/2013/04/hosannatelugu.mp3";

        try {
            mediaPlayer.setDataSource(songURL);
        } catch (IllegalArgumentException e) {
            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        } catch (SecurityException e) {
            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        } catch (IllegalStateException e) {
            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            mediaPlayer.prepare();
        } catch (IllegalStateException e) {
            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            Toast.makeText(getApplicationContext(), "You might not set the URI correctly!", Toast.LENGTH_LONG).show();
        }
//        mediaPlayer = MediaPlayer.create(this, R.raw.eg);
//        mediaPlayer.start();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.musica) {
            Toast.makeText(this, "Ok", Toast.LENGTH_SHORT).show();
            mediaPlayer.start();
            on = false;

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {

        int id = v.getId();

        if (id == R.id.btn1) {
            if (!on) {
                mediaPlayer.pause();
            }
            soundpool.play(soundID1, 100, 100, 0, 0, 1);
            soundID1 = soundpool.load(this, R.raw.swamp, 1);

        }
        if (id == R.id.btn2) {
            if (!on) {
                mediaPlayer.pause();
            }
            soundpool.play(soundID2, 100, 100, 0, 0, 1);
            soundID2 = soundpool.load(this, R.raw.saber, 1);

        }
        if (id == R.id.btn3) {
            if (!on) {
                mediaPlayer.pause();
            }
            soundpool.play(soundID3, 100, 100, 0, 0, 1);
            soundID3 = soundpool.load(this, R.raw.bomb, 1);

        }
        if (id == R.id.btn4) {
            if (!on) {
                mediaPlayer.pause();
            }
            soundpool.play(soundID4, 100, 100, 0, 0, 1);
            soundID4 = soundpool.load(this, R.raw.ouch, 1);

        }
    }

    @Override
    public void onLoadComplete(SoundPool soundPool, int sampleId, int status) {

                    loaded++;
    }

    public void startRecording(View view) throws IOException {

        startButton.setEnabled(false);
        stopButton.setEnabled(true);

        File sampleDir = Environment.getExternalStorageDirectory();
        try {
            audiofile = File.createTempFile("sound", ".3gp", sampleDir);
        } catch (IOException e) {
            Log.e(TAG, "sdcard access error");
            return;
        }
        recorder = new MediaRecorder();
        recorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB);
        recorder.setOutputFile(audiofile.getAbsolutePath());
        recorder.prepare();
        recorder.start();
    }
    public void stopRecording(View view) {
        startButton.setEnabled(true);
        stopButton.setEnabled(false);
        recorder.stop();
        recorder.release();
        addRecordingToMediaLibrary();
    }
    protected void addRecordingToMediaLibrary() {
        ContentValues values = new ContentValues(4);
        long current = System.currentTimeMillis();
        values.put(MediaStore.Audio.Media.TITLE, "audio" + audiofile.getName());
        values.put(MediaStore.Audio.Media.DATE_ADDED, (int) (current / 1000));
        values.put(MediaStore.Audio.Media.MIME_TYPE, "audio/3gpp");
        values.put(MediaStore.Audio.Media.DATA, audiofile.getAbsolutePath());
        ContentResolver contentResolver = getContentResolver();

        Uri base = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Uri newUri = contentResolver.insert(base, values);

        sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, newUri));
        Toast.makeText(this, "Added File " + newUri, Toast.LENGTH_LONG).show();
    }
}
