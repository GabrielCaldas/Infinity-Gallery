package com.infinityco.infinitygallery.Activitys;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.cardview.widget.CardView;

import android.provider.Settings;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import com.infinityco.infinitygallery.R;

public class Start extends AppCompatActivity {


    private Activity activity;
    private Boolean ActivityStarted = false;
    private CardView cvNewUserDialog;

    @Override
    public void onResume(){
        super.onResume();


        ActivityStarted=true;
        Create();
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

        setContentView(R.layout.activity_start);

        cvNewUserDialog =  findViewById(R.id.newUser);
        Button btGo =  findViewById(R.id.btStart);
        btGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestPermission();
            }
        });


        activity= this;
        setupFullscreen();
    }

    private void layoutShow(){
        final TranslateAnimation animate = new TranslateAnimation(
                0,                 // fromXDelta
                0,                 // toXDelta
                 2000,//cvNewUserDialog.getHeight(),               // fromYDelta
                0); // toYDelta
        animate.setDuration(1000);
        animate.setFillAfter(true);


        animate.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
               cvNewUserDialog.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        cvNewUserDialog.startAnimation(animate);

    }

    private int currentApiVersion;
    private void setupFullscreen(){
        currentApiVersion = android.os.Build.VERSION.SDK_INT;

        final int flags ;

        if(android.os.Build.VERSION.SDK_INT >= 19) {
            flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;

        }
        else {
            flags = View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;

        }
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

    public void Create(){


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Starts();
        }
        else start();
    }


    boolean testrequest = false;
    public void Starts(){
        if(checkPermission()) {
            start();
        }
        else {
            if(!testrequest) {
                testrequest = true;
                layoutShow();

            }

        }
    }


    private boolean checkPermission(){
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        int resultwrite = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        boolean retorno = false;
        if (result == PackageManager.PERMISSION_GRANTED && resultwrite == PackageManager.PERMISSION_GRANTED){
            retorno =  true;

        }
        return retorno;
    }

    private static final int WRITE_REQUEST_CODE = 1;
    private static final int READ_REQUEST_CODE = 2;
    private void requestPermission(){

        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},WRITE_REQUEST_CODE);


    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        if(requestCode==WRITE_REQUEST_CODE){

            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},READ_REQUEST_CODE);

        }
        else if(requestCode==READ_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                start();
            } else {
                if (!isShowingAD) {
                    activity.runOnUiThread(new Runnable() {
                        public void run() {
                            AlertDialog();
                        }
                    });
                }
            }
        }
    }

    private boolean isShowingAD = false;
    public void AlertDialog(){
        final android.app.AlertDialog alertDialog;
        final android.app.AlertDialog.Builder RecDialog = new android.app.AlertDialog.Builder(this);
        RecDialog.setTitle(R.string.Allow_access);
        RecDialog.setMessage(R.string.Allow_access_mensage);

        isShowingAD = true;


        RecDialog.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Do nothing here. The listner is bellow;
                    }
                });
        RecDialog.setNegativeButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Do nothing here. The listner is bellow;
                    }
                });

        alertDialog = RecDialog.create();


        //alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.box_layout_player);
        //alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        alertDialog.show();

        alertDialog.getButton(android.app.AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission();
                alertDialog.dismiss();
                isShowingAD = false;
            }
        });
        alertDialog.getButton(android.app.AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                isShowingAD = false;
                finish();
            }
        });
    }

    private void start() {
        startActivity(new Intent(this, MainActivity.class));
        finish();

    }
}
