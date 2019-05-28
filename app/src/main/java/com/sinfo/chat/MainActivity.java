package com.sinfo.chat;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ArrayList<ModelloDati> dati = new ArrayList<>();
    private LinearLayoutManager linearLayoutManager;
    private static final String VIDEO_DIRECTORY = "/Chat";
    private myAdapter adapter;
    private VideoView videoView;
    public  Uri passUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        // Elementi Grafici-----------------------------------------

        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        Button video = findViewById(R.id.video);
        Button camera = findViewById(R.id.camera);
        Button send = findViewById(R.id.send);
        videoView =findViewById(R.id.rec);
        final EditText editText = findViewById(R.id.editText);

        // Media Controller----------------------------------------



        // Layout Manager------------------------------------------------

        linearLayoutManager = new LinearLayoutManager(MainActivity.this);
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);



        // Adapter-----------------------------------------

        if(dati.size()> 1){  /*verifica se le liste hanno piu di un elemento, in caso affermativo avvisa l'adattatore che ci sono piu dati*/
            adapter =  new myAdapter(dati, this);
            adapter.notifyDataSetChanged();
            recyclerView.setAdapter(adapter);
            recyclerView.smoothScrollToPosition(dati.size());
            recyclerView.setFocusable(dati.size());
        }


        else{
            adapter =  new myAdapter(dati,this);
            recyclerView.setAdapter(adapter);
            recyclerView.smoothScrollToPosition(dati.size());
        }


        // Listener Click Video----------------------------------------------
        video.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
                startActivityForResult(intent,0);
            }
        });

        // Listener Click Camera----------------------------------------------
        camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                startActivityForResult(intent,1);
            }
        });

        // Listener Click Invio------------------------------------------------
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String string = editText.getText().toString();
                dati.add(new ModelloDati(0,string));
                editText.getText().clear();
                closeKeyboard();
            }
        });



    }

    private void closeKeyboard() {
        View view = getCurrentFocus();
        if(view != null){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 0:
                try {
                    Uri contentURI = data.getData();
                    passUri = contentURI;
                    String recordedVideoPath = getPath(contentURI);
                    Log.d("frrr", recordedVideoPath);
                    saveVideoToInternalStorage(recordedVideoPath);
                    dati.add(new ModelloDati(2, contentURI));
                }catch (Throwable o){Log.i("CAM","User aborted action");}
            case 1:
                try {
                Bitmap bitmap = (Bitmap)data.getExtras().get("data");
                dati.add(new ModelloDati(1,bitmap));
                }catch(Throwable o){
                Log.i("CAM","User aborted action");
                }
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
