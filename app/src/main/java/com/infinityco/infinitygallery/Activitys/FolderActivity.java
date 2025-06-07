package com.infinityco.infinitygallery.Activitys;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.res.Configuration;
import android.database.ContentObserver;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import androidx.core.content.FileProvider;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.infinityco.infinitygallery.Objects.Folder;
import com.infinityco.infinitygallery.Adapters.rvAdapterMedias;
import com.infinityco.infinitygallery.Objects.FolderList;
import com.infinityco.infinitygallery.Objects.HorSwipeAwareRelativeLayout;
import com.infinityco.infinitygallery.R;


import java.util.ArrayList;
import java.util.List;

public class FolderActivity extends AppCompatActivity {


    private RecyclerView rvMedias;
    private RelativeLayout rlMediaSelected;
    private TextView tvMediaSelected;

    //Objects
    private Folder folder;
    private rvAdapterMedias adapterMedias;
    private AlertDialog alertDialog; //deleteAlertDialog()
    private boolean firsTime = true, notUpdate=false;
    private int Case;

    public void registObserver(){
        notUpdate = false;
    }

    public void unRegistObserver(){
        notUpdate = true;
    }

    @Override
    public void onResume(){
        super.onResume();

        if(!firsTime) {
            if(folder.getMedias().size()==0){
                finish();
            }
        }
        else {
            firsTime = false;
        }

    }

    @Override
    public void onBackPressed(){
        if(adapterMedias.getMediasSelected().size()>0){
            adapterMedias.releaseSelections();
        }
        else {
            super.onBackPressed();
        }
    }

    @Override public void finish() {
        super.finish();
        overridePendingTransition(R.anim.left_to_right,R.anim.right_to_left);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        rvMedias.setLayoutManager(new GridLayoutManager(this, getNumRoons()));
        /*if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {


        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT) {

        }*/
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(MainActivity.isDarkTheme()){
            setContentView(R.layout.activity_folder_dark);
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setNavigationBarColor(Color.BLACK);
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.BLACK);

                getWindow().getDecorView().setSystemUiVisibility(0);
            }
        }
        else {
            setContentView(R.layout.activity_folder);
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setNavigationBarColor(Color.WHITE);
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);

                if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                    getWindow().setStatusBarColor(Color.WHITE);
                    getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                }
                else {
                    getWindow().setStatusBarColor(Color.LTGRAY);
                }
            }
        }

        Intent iin = getIntent();
        Bundle bundle = iin.getExtras();
        if(bundle!=null){

            String path = (String) bundle.get("Folder");
            if(!path.equals("/////////////")) {
                Case = Folder.defaltCase;
                LoadFolder(path);
            }
            else {
                Case = (int) bundle.get("Feed");
                switch (Case){
                    case 0:
                        folder = new FolderList(this).getToDayfolder();
                        break;
                    case 1:
                        folder = new FolderList(this).getLast7folder();
                        break;
                }
            }
            SetUp();
            SetUpRv();
            getContentResolver().registerContentObserver(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, true,
                    new ContentObserver(new Handler()) {
                        @Override
                        public void onChange(boolean selfChange) {
                            Log.d("your_tag","Internal Media has been changed");
                            super.onChange(selfChange);
                            //Long timestamp = readLastDateFromMediaStore(context, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                            // comapare with your stored last value and do what you need to do

                            if(!notUpdate) {

                                folder.reload();
                                if (folder.getMedias().size() == 0) {
                                    finish();
                                } else {
                                    adapterMedias.notifyDataSetChanged(folder.getMedias());
                                }
                            }

                        }
                    }
            );
        }
        else {
            Toast.makeText(this, "erro.", Toast.LENGTH_SHORT).show();
        }
    }

    private void LoadFolder(String path){
        folder = new Folder(this,path);
    }

    private void SetUp(){

        rvMedias =  findViewById(R.id.rvMedias);
        tvMediaSelected =  findViewById(R.id.tvSelectedCount);
        rlMediaSelected =  findViewById(R.id.rlMediaSelected);

        Button folderName = findViewById(R.id.btFolderName);
        Button back =  findViewById(R.id.btBack);
        Button btSelectAll =  findViewById(R.id.btSelectAll);
        Button btDelete =  findViewById(R.id.btDelete);
        Button btShare = findViewById(R.id.btShare);
        Button btCopy = findViewById(R.id.btCopy);


        folderName.setText(folder.getName());

        folderName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rlMediaSelected.getVisibility()!=View.VISIBLE) {
                    finish();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rlMediaSelected.getVisibility()!=View.VISIBLE) {
                    finish();
                }
            }
        });

        btSelectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectAll();
            }
        });

        btShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaShare();
            }
        });

        btDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exclude();
            }
        });

        btCopy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapterMedias.copy();
            }
        });


        HorSwipeAwareRelativeLayout rlActivity = findViewById(R.id.rlActivity);
        rlActivity.setOnHorizontalSwipeListener(new HorSwipeAwareRelativeLayout.OnHorizontalSwipeListener() {
            @Override
            public void onRightSwipe() {
                finish();
            }
        });

    }


    private void selectAll(){
        adapterMedias.selectAll();
    }

    public void mediaShare(){
        ArrayList<Uri> imageUris = new ArrayList<>();


        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            for (int i = 0; i < adapterMedias.getMediasSelected().size(); i++) {
                Uri sendUri2;
                sendUri2 = FileProvider.getUriForFile(
                        this,
                        getApplicationContext()
                                .getPackageName() + ".provider", adapterMedias.getMediasSelected().get(i).getFile());
                imageUris.add(sendUri2);
            }
        }
        else{
            for (int i = 0; i < adapterMedias.getMediasSelected().size(); i++) {
                Uri sendUri2;
                sendUri2 = Uri.fromFile(adapterMedias.getMediasSelected().get(i).getFile());
                imageUris.add(sendUri2);
            }
        }
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
        shareIntent.setType("image/*");
        startActivity(Intent.createChooser(shareIntent, getString(R.string.shareto)));
    }

    private void SetUpRv(){
        rvMedias.setHasFixedSize(true);
        rvMedias.setLayoutManager(new GridLayoutManager(this, getNumRoons()));

        adapterMedias = new rvAdapterMedias(this,this,folder.getMedias(),rlMediaSelected,tvMediaSelected,folder,Case);
        rvMedias.setAdapter(adapterMedias);

    }

    private int getNumRoons(){

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;

        return ((int) dpWidth/95);
    }

    private void exclude(){
        final AlertDialog.Builder RecDialog = new AlertDialog.Builder(this);

        //Typeface tittle_type = MainActivity.getTyperFace();

        TextView dialogTittle = new TextView(this);
        dialogTittle.setText(R.string.excludeTittle);
        //dialogTittle.setTypeface(tittle_type);
        dialogTittle.setTextColor(Color.WHITE);
        dialogTittle.setPadding(20,10,10,10);
        dialogTittle.setTextSize(25);
        dialogTittle.setBackgroundResource(R.drawable.datail_gradient);

        final TextView input = new TextView(this);
        input.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        if(adapterMedias.getMediasSelected().size()==1){
            input.setText(getString(R.string.excludeMessage)+" 1 "+getString(R.string.excludeMessageEnd));
        }
        else {
            input.setText(getString(R.string.excludeMessage)+" "+adapterMedias.getMediasSelected().size()+" "+getString(R.string.excludeMessageEnds));
        }
        //input.setTypeface(tittle_type);

        LinearLayout dialogLayout = new LinearLayout(this);
        dialogLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout editTextLayout = new LinearLayout(this);
        editTextLayout.setOrientation(LinearLayout.HORIZONTAL);
        editTextLayout.setPadding(30,30,30,30);
        editTextLayout.addView(input);

        dialogLayout.addView(dialogTittle);
        dialogLayout.addView(editTextLayout);

        RecDialog.setView(dialogLayout);


        RecDialog.setPositiveButton(R.string.excludeTittle,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        adapterMedias.mediaDelete(new rvAdapterMedias.DeleteMediasListenner() {
                            @Override
                            public void onFinish() {

                                adapterMedias.releaseSelections();

                            }

                            @Override
                            public void onFailDelete() {

                                if(Build.VERSION.SDK_INT > Build.VERSION_CODES.Q){

                                    startActivity(new Intent(FolderActivity.this, RequestFileManagerActivity.class));

                                }
                                else{

                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(FolderActivity.this,getString(R.string.excludFail), Toast.LENGTH_SHORT).show();
                                        }
                                    });

                                }

                            }
                        });
                        alertDialog.dismiss();
                    }
                });

        RecDialog.setNeutralButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });


        alertDialog = RecDialog.create();
        alertDialog.getWindow().setTitleColor(Color.RED);
        alertDialog.show();



        alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });


        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
        alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(Color.GRAY);
    }

}
