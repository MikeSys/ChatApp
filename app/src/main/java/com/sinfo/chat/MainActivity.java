package com.sinfo.chat

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.PersistableBundle;
import android.os.SystemClock;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Chronometer;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;


public class MainActivity extends AppCompatActivity{



    private ArrayList<ModelloDati> dati = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private myAdapter adapter;
    private RecyclerView recyclerView;
    public  Uri passUri;
    MediaRecorder recorder = new MediaRecorder();
    private static final String VIDEO_DIRECTORY = "/Chat";
    private              String outputFile;
    private static final int tlTEXT = 0;
    private static final int tlIMAGE = 1;
    private static final int tlVIDEO = 2;
    private static final int tlAUDIO = 3;
    private static final int tlDATE = 4;
    private              int counter_audio = 0 ;
    Boolean testoBarra = false;
    Boolean barraVuota = false;



    @SuppressLint("ClickableViewAccessibility")
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);



        // Variables-----------------------------------------



        recyclerView = findViewById(R.id.recyclerView);
        final EditText editText = findViewById(R.id.editText);
        final ImageButton video = findViewById(R.id.video);
        final ImageButton camera = findViewById(R.id.camera);
        final ImageButton send = findViewById(R.id.send);
        send.setVisibility(View.GONE);
        final ImageButton record = findViewById(R.id.record);
        final CardView normal_bar = findViewById(R.id.normal_bar);
        final CardView rec_bar = findViewById(R.id.bar_rec);
        final Chronometer chronometer = findViewById(R.id.cronometro);
        recorder = new MediaRecorder();





        // Layout Manager------------------------------------------------



        linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        linearLayoutManager.setStackFromEnd(true);
        RecyclerView.ItemAnimator itemAnimator = new DefaultItemAnimator();
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemAnimator(itemAnimator);
        final LinearLayout bar = findViewById(R.id.bar);






        // Adapter-----------------------------------------



        //adapter.notifyDataSetChanged();

        adapter =  new myAdapter(dati, this);

        recyclerView.setAdapter(adapter);



        // Click Listener Video button----------------------------------------------

        video.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {


                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

                startActivityForResult(intent,0);

            }

        });



        // Click Listener Camera button----------------------------------------------

        camera.setOnClickListener(new View.OnClickListener() {

            @Override

            public void onClick(View v) {

                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                startActivityForResult(intent,1);

            }

        });





        // Click Listener Send/Record button------------------------------------------------


        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (!barraVuota) {
                    if ((editText.getText().toString() == "") || (editText.getText().toString() == " ")) {
                        testoBarra = false;
                        record.setVisibility(View.VISIBLE);
                        send.setVisibility(View.GONE);
                    } else {
                        testoBarra = true;
                        record.setVisibility(View.GONE);
                        send.setVisibility(View.VISIBLE);
                    }

                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(editText.equals("")){
                    testoBarra = false;
                    record.setVisibility(View.VISIBLE);
                    send.setVisibility(View.GONE);
                    barraVuota = true;

                }else{
                    barraVuota = false;
                }

            }
        });



        send.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {


                if (testoBarra) {
                    String string = editText.getText().toString();
                    dati.add(new ModelloDati(tlDATE, ""));
                    dati.add(new ModelloDati(tlTEXT, string));
                    adapter.notifyItemInserted(dati.size());
                    editText.getText().clear();
                    testoBarra = false;
                    record.setVisibility(View.VISIBLE);
                    send.setVisibility(View.GONE);
                }
                recyclerView.smoothScrollToPosition(dati.size());
                closeKeyboard();
                return true;
            }
        });


         record.setOnTouchListener(new View.OnTouchListener() {
             @Override
             public boolean onTouch(View v, MotionEvent event) {

                 if (event.getAction() == MotionEvent.ACTION_DOWN ) {

                     bar.setVisibility(View.GONE);
                     rec_bar.setVisibility(View.VISIBLE);
                     counter_audio++;
                     outputFile = Environment.getExternalStorageDirectory().getAbsolutePath() + "/recording" + counter_audio + ".3gp";
                     recorder.setAudioSource(MediaRecorder.AudioSource.DEFAULT);
                     recorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
                     recorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
                     recorder.setOutputFile(outputFile);
                     try {
                         recorder.prepare();
                     } catch (IOException e) {
                         e.printStackTrace();
                     }
                     chronometer.setBase(SystemClock.elapsedRealtime());
                     chronometer.start();
                     recorder.start();

                 } else if (event.getAction() == MotionEvent.ACTION_UP) {
                     bar.setVisibility(View.VISIBLE);
                     rec_bar.setVisibility(View.GONE);
                     recorder.stop();
                     recorder.release();
                     recorder = null;
                     chronometer.setBase(SystemClock.elapsedRealtime());
                     dati.add(new ModelloDati(tlAUDIO, outputFile));
                     adapter.notifyDataSetChanged();
                 }

                 recyclerView.smoothScrollToPosition(dati.size());
                 closeKeyboard();
                 return true;
             }
         });



        if (savedInstanceState != null)

        { adapter.restorestate(savedInstanceState);}



    }



    private boolean checkPermission (){
        int WRITE_EXTERNAL_STORAGE = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int READ_EXTERNAL_STORAGE = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int RECORD_AUDIO = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        return WRITE_EXTERNAL_STORAGE == PackageManager.PERMISSION_GRANTED &&
                READ_EXTERNAL_STORAGE == PackageManager.PERMISSION_GRANTED &&
                RECORD_AUDIO == PackageManager.PERMISSION_GRANTED;
    } //da fìnire;


    @Override

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){

            case 0:

                try {

                    Uri contentURI = data.getData();

                    passUri = contentURI;

                    String recordedVideoPath = getPath(contentURI);

                    saveVideoToInternalStorage(recordedVideoPath);

                    dati.add(new ModelloDati(tlVIDEO, contentURI));

                    adapter.notifyItemInserted(dati.size());

                    recyclerView.smoothScrollToPosition(dati.size());



                }catch (Throwable o){Log.i("CAM","User aborted action");}

            case 1:

                try {

                    Bitmap bitmap = (Bitmap)data.getExtras().get("data");

                    dati.add(new ModelloDati(tlIMAGE,bitmap));

                    adapter.notifyItemInserted(dati.size());

                    recyclerView.smoothScrollToPosition(dati.size());





                }catch(Throwable o){

                    Log.i("CAM","User aborted action");

                }

        }




    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        if(adapter != null){
            adapter.saveState(outState);
            recyclerView.setSaveEnabled(true);
        }
    }

    private void saveVideoToInternalStorage (String filePath) {



        File newfile;



        try {



            File currentFile = new File(filePath);

            File wallpaperDirectory = new File(Environment.getExternalStorageDirectory() + VIDEO_DIRECTORY);

            newfile = new File(wallpaperDirectory, Calendar.getInstance().getTimeInMillis() + ".mp4");



            if (!wallpaperDirectory.exists()) {

                wallpaperDirectory.mkdirs();

            }



            if(currentFile.exists()){



                InputStream in = new FileInputStream(currentFile);

                OutputStream out = new FileOutputStream(newfile);



                // Copy the bits from instream to outstream

                byte[] buf = new byte[1024];

                int len;



                while ((len = in.read(buf)) > 0) {

                    out.write(buf, 0, len);

                }

                in.close();

                out.close();

                Log.v("vii", "Video file saved successfully.");

            }else{

                Log.v("vii", "Video saving failed. Source file missing.");

            }

        } catch (Exception e) {

            e.printStackTrace();

        }



    }
    private void closeKeyboard() {

        View view = getCurrentFocus();

        if(view != null){

            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);

            imm.hideSoftInputFromWindow(view.getWindowToken(),0);

        }

    }

    public String getPath(Uri uri) {

        String[] projection = { MediaStore.Video.Media.DATA };

        Cursor cursor = getContentResolver().query(uri, projection, null, null, null);

        if (cursor != null) {

            // HERE YOU WILL GET A NULLPOINTER IF CURSOR IS NULL

            // THIS CAN BE, IF YOU USED OI FILE MANAGER FOR PICKING THE MEDIA

            int column_index = cursor

                    .getColumnIndexOrThrow(MediaStore.Video.Media.DATA);

            cursor.moveToFirst();

            return cursor.getString(column_index);

        } else

            return null;

    }







}

