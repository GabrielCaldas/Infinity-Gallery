package com.infinityco.infinitygallery.Activitys;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.infinityco.infinitygallery.Objects.WeakHandler;
import com.infinityco.infinitygallery.R;

public class VideoPlayer extends AppCompatActivity {


    //Layout
    VideoView videoView;
    TextView currenteTime,duration;
    Button btPlay,btBack;
    SeekBar seekBar;
    RelativeLayout rlPlayerVideoTop;
    LinearLayout rlPlayerVideobotton;

    //Object
    String path;
    WeakHandler mHandler;
    int Duration=0;
    long lastTouchUpTime = 0;
    boolean clicked = false;


    @Override
    protected void onPause(){
        super.onPause();

        videoView.pause();
        Duration = videoView.getCurrentPosition();

        if(Duration<2001){
            Duration=1;
        }
        else {
            Duration = Duration-2000;
        }
    }

    @Override
    public void onResume(){
        super.onResume();
        videoView.seekTo(Duration);
    }

    @Override
    protected void onDestroy() {

        mHandler = new WeakHandler();
        super.onDestroy();
    }

    @SuppressLint("NewApi")
    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        if(currentApiVersion >= Build.VERSION_CODES.KITKAT && hasFocus)
        {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_player);

        setupFullscreen();

        Intent iin = getIntent();
        Bundle bundle = iin.getExtras();
        path = (String) bundle.get("video_path");

        setUp();
        setClick();
        start(path);

        mHandler = new WeakHandler();
        mHandler.post(
                new Runnable() {
                    @Override
                    public void run() {
                        try {
                            seekBar.setProgress(videoView.getCurrentPosition());

                            int Duration = videoView.getCurrentPosition();
                            int tst = (Duration / 1000) % 60;
                            if (tst < 10) {
                                currenteTime.setText("" + ((Duration / 60000) % 60) + ":0" + tst);
                            }
                            if (tst >= 10) {
                                currenteTime.setText("" + ((Duration / 60000) % 60) + ":" + tst);
                            }
                        } catch (Exception e) {
                        }
                        mHandler.postDelayed(this, 100);
                    }
                });
    }

    private void setUp(){
        videoView = (VideoView) findViewById(R.id.VideoView);

        rlPlayerVideobotton = (LinearLayout) findViewById(R.id.rlPlayVideoBotton);
        rlPlayerVideoTop = (RelativeLayout) findViewById(R.id.rlPlayVideoTop);

        currenteTime = (TextView) findViewById(R.id.currentduration);
        duration = (TextView) findViewById(R.id.totalduration);

        btBack = (Button) findViewById(R.id.btVideoPlayerBack);
        btPlay = (Button) findViewById(R.id.btplay);

        seekBar = (SeekBar) findViewById(R.id.seekBar);


        videoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                int D = videoView.getDuration();

                int tst = (D / 1000) % 60;

                if (tst < 10) {
                    duration.setText("" + ((D / 60000) % 60) + ":0" + tst);
                }
                if (tst >= 10) {
                    duration.setText("" + ((D / 60000) % 60) + ":" + tst);
                }

                seekBar.setMax(D);
            }
        });


    }

    int touchTime;
    private void setClick(){
        videoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        Button btVideoLayoyt = findViewById(R.id.btVideoLayout);
        btVideoLayoyt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long currentTime = System.currentTimeMillis();
                if (!clicked && currentTime - lastTouchUpTime < 500) {
                    clicked = true;
                    lastTouchUpTime = currentTime;

                    Play();
                } else {
                    lastTouchUpTime = currentTime;

                    clicked = false;
                }
                setLayoutVisibility();
            }
        });

        Button btMinimizar = findViewById(R.id.btMinimizar);
        btMinimizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setLayoutVisibility();
            }
        });

        videoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                finish();
            }
        });

        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Play();
            }
        });

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                videoView.seekTo(seekBar.getProgress());
            }
        });
    }

    private void start(String path){
        videoView.setVideoPath(path);
        Play();
    }

    private void Play(){
        if(videoView.isPlaying()){
            videoView.pause();
            btPlay.setBackgroundResource(R.drawable.btplay);
        }
        else {
            videoView.start();
            btPlay.setBackgroundResource(R.drawable.btpause);
        }
    }

    private void setLayoutVisibility(){
        if(rlPlayerVideoTop.getVisibility()==View.VISIBLE){
            rlPlayerVideoTop.setVisibility(View.GONE);
            rlPlayerVideobotton.setVisibility(View.GONE);
        }
        else {
            rlPlayerVideoTop.setVisibility(View.VISIBLE);
            rlPlayerVideobotton.setVisibility(View.VISIBLE);
        }
    }

    private int currentApiVersion;
    private void setupFullscreen(){
        currentApiVersion = android.os.Build.VERSION.SDK_INT;

        final int flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        // This work only for android 4.4+
        if(currentApiVersion >= Build.VERSION_CODES.KITKAT)
        {

            getWindow().getDecorView().setSystemUiVisibility(flags);

            // Code below is to handle presses of Volume up or Volume down.
            // Without this, after pressing volume buttons, the navigation bar will
            // show up and won't hide
            final View decorView = getWindow().getDecorView();
            decorView
                    .setOnSystemUiVisibilityChangeListener(new View.OnSystemUiVisibilityChangeListener()
                    {

                        @Override
                        public void onSystemUiVisibilityChange(int visibility)
                        {
                            if((visibility & View.SYSTEM_UI_FLAG_FULLSCREEN) == 0)
                            {
                                decorView.setSystemUiVisibility(flags);
                            }
                        }
                    });
        }
    }
}
