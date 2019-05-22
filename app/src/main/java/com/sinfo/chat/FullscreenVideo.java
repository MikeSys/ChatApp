package com.sinfo.chat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import org.w3c.dom.Text;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class FullscreenVideo extends AppCompatActivity {
    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * Some older devices needs a small delay between UI widget updates
     * and a change of the status and navigation bar.
     */
    private static final int UI_ANIMATION_DELAY = 300;
    private final Handler mHideHandler = new Handler();
    private View mContentView;
    private Uri uri;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen_video);


        TextView textView = findViewById(R.id.testo_full);
        VideoView videoView = findViewById(R.id.rec2);
        String string = getIntent().getStringExtra("TESTO");

        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOptions);

        try {
            uri = Uri.parse(getIntent().getExtras().getString("VIDEO"));
            videoView.setVideoURI(uri);
            MediaController mediaController = new MediaController(this);
            mediaController.setAnchorView(videoView);
            videoView.setMediaController(mediaController);
            videoView.requestFocus();
            videoView.start();
            videoView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    View decorView = getWindow().getDecorView();
                    int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN;
                    decorView.setSystemUiVisibility(uiOptions);
                }
            });
        }catch (Throwable o){

            textView.setText(string);

        }


    }

}
