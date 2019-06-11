package com.sinfo.chat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

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
    Context context = this;


    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen_video);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        final Button button = findViewById(R.id.close_video);
        final Runnable mRunnable;
        final Handler mHandler=new Handler();


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);


        TextView textView = findViewById(R.id.testo_full);
        VideoView videoView = findViewById(R.id.rec2);
        String string = getIntent().getStringExtra("TESTO");

        mRunnable = new Runnable() {
            @Override
            public void run() {
                toolbar.animate().translationY(-toolbar.getBottom()).setInterpolator(new AccelerateInterpolator()).start();
            }
        };


        try {
            uri = Uri.parse(getIntent().getExtras().getString("VIDEO"));
            videoView.setVideoURI(uri);
            final MediaController mediaController = new MediaController(this);
            mediaController.setAnchorView(videoView);
            videoView.setMediaController(mediaController);
            videoView.requestFocus();
            videoView.start();
            videoView.setMediaController(mediaController);
            mediaController.setBackgroundColor(getResources().getColor(R.color.transparent));
            videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    toolbar.animate().translationY(-toolbar.getBottom()).setInterpolator(new AccelerateInterpolator()).start();
                }
            });
            toolbar.animate().translationY(-toolbar.getBottom()).setInterpolator(new AccelerateInterpolator()).start();
            videoView.setOnTouchListener(new View.OnTouchListener() {
                boolean flag = false;
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    switch (motionEvent.getAction()) {
                        case MotionEvent.ACTION_DOWN:
                            if (flag) {
                                mediaController.hide();
                                toolbar.animate().translationY(-toolbar.getBottom()).setInterpolator(new AccelerateInterpolator()).start();
                            }
                            else {
                                mediaController.show(3000);
                                toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator()).start();
                                mHandler.postDelayed(mRunnable,3000);

                            }
                            flag = !flag;
                            return true;

                    }

                    return false;
                }
            });


            videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mediaController.show(0);
                    toolbar.animate().translationY(0).setInterpolator(new DecelerateInterpolator()).start();
                }
            });
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

        }catch (Throwable o){

            textView.setText(string);

        }


    }






}
