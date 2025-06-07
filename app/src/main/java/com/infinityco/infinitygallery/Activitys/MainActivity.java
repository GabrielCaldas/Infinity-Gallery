package com.infinityco.infinitygallery.Activitys;

import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.Configuration;
import android.database.ContentObserver;
import android.graphics.Color;
import android.os.Build;
import android.os.Handler;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.infinityco.infinitygallery.Adapters.CardViewAdapter_historyFeed;
import com.infinityco.infinitygallery.Adapters.CustomPagerAdapter;
import com.infinityco.infinitygallery.Adapters.rvAdapterFolders;
import com.infinityco.infinitygallery.Objects.Berreivement_values;
import com.infinityco.infinitygallery.Objects.FolderList;
import com.infinityco.infinitygallery.R;

import java.util.Vector;

public class MainActivity extends AppCompatActivity {

    //Layout
    private ViewPager viewPager;
    private RecyclerView rvFolders, rvHistory;
    private LinearLayout llTabs;
    private RelativeLayout rlBtCamera,rlBtCamera2;
    private ImageView icoFolder,icoRecent;
    private TextView txFolder,txRecent;

    //Objects
    private FolderList folderList;
    public  Context context;
    public rvAdapterFolders rvAdapterFolders;
    private CardViewAdapter_historyFeed feedAdapter;

    private ContentObserver contentObserver;
    private static boolean changeTheme=false,darkTheme=false,gridStatus=false,rawStatus=true,newStyleStatus=true,recentStauts=false,notUpdate=false;
    public static void setChangeTheme(){
        changeTheme = true;
    }

    public static boolean isDarkTheme(){
        return darkTheme;
    }

    public static void changeRawStatus(Boolean RawStatus){
        rawStatus = RawStatus;
    }

    public static boolean getRawStatus(){
        return rawStatus;
    }

    public static boolean getRecentStatus(){
        return recentStauts;
    }

    public void registObserver(){
        notUpdate = false;
    }

    public void unRegistObserver(){
        notUpdate = true;
    }


    @Override
    public void onBackPressed(){
        moveTaskToBack(true);
    }

    @Override
    public void onStart(){
        super.onStart();

        if(changeTheme){
            changeTheme=false;

            Create();
            LoadMedia();
            SetUpRv();

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.i("EXIT", "ondestroy!");

        Intent broadcastIntent = new Intent("com.infinityco.infinitygallery.Service.RestartSensor");
        sendBroadcast(broadcastIntent);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // Checks the orientation of the screen
        rvFolders.setLayoutManager(new GridLayoutManager(this, getNumRoons()));
        feedAdapter = new CardViewAdapter_historyFeed(this,this,folderList.getToDayPhotos(),folderList.getLast7DaysPhotos());
        rvAdapterFolders =new rvAdapterFolders(context,this,feedAdapter,folderList,getNumPhotos(),newStyleStatus,gridStatus);

        rvFolders.setAdapter(rvAdapterFolders);
        rvHistory.setAdapter(feedAdapter);


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Create();

        Berreivement_values bv = new Berreivement_values(this);
        if(bv.isFirst()) {
            welcomeDialog();
        }

        contentObserver = new ContentObserver(new Handler()) {
            @Override
            public void onChange(boolean selfChange) {
                Log.d("your_tag","Internal Media has been changed");
                super.onChange(selfChange);
                //Long timestamp = readLastDateFromMediaStore(context, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
                // comapare with your stored last value and do what you need to do
                if(!notUpdate) {
                    LoadMedia();
                    SetUpRv();

                    if (rvAdapterFolders == null || feedAdapter == null) {
                        SetUpRv();
                    } else {
                        rvAdapterFolders.UpDateViews(folderList);
                        feedAdapter.UpDateViews(folderList.getToDayPhotos(), folderList.getLast7DaysPhotos());
                    }
                }
            }
        };

        getContentResolver().registerContentObserver(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI, true, contentObserver);

    }

    private void Create(){

        Berreivement_values bv = new Berreivement_values(this);
        try{
            darkTheme = bv.getDarkThemeStatus();
        }
        catch (Exception e){
            darkTheme = false;
        }
        try{
            newStyleStatus = bv.getNewStyleStatus();
        }
        catch (Exception e){
            newStyleStatus = true;
        }
        try{
            gridStatus = bv.getGridStatus();
        }
        catch (Exception e){
            gridStatus = newStyleStatus;

        }

        recentStauts = bv.getRecentStatus();
        rawStatus = bv.getRawStatus();
        if(darkTheme){
            setContentView(R.layout.activity_main_dark);
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setNavigationBarColor(Color.BLACK);
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.BLACK);

                getWindow().getDecorView().setSystemUiVisibility(0);
            }
        }
        else {
            setContentView(R.layout.activity_main);
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

        SetUpUI();
    }

    private void SetUpUI(){
        Button btFolder = findViewById(R.id.btFolders);
        Button btRecent =  findViewById(R.id.btRecent);
        Button btOptions = findViewById(R.id.btOptions);
        Button btCamera = findViewById(R.id.btCamera);
        Button btCamera2 = findViewById(R.id.btCamera2);
        rlBtCamera2 = findViewById(R.id.rlBtCamera2);
        rlBtCamera = findViewById(R.id.rlBtCamera);
        viewPager = findViewById(R.id.ViewPager);
        llTabs =   findViewById(R.id.tabs);
        icoFolder = findViewById(R.id.ivIcoFolder);
        icoRecent = findViewById(R.id.ivIcoRecent);
        txFolder =  findViewById(R.id.tvIcoFolder);
        txRecent = findViewById(R.id.tvIcoRecents);

        btFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(0,true);
            }
        });

        btRecent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                viewPager.setCurrentItem(1,true);
            }
        });

        btCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rlBtCamera.getVisibility()==View.VISIBLE) {
                    Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    try {
                        PackageManager pm = getPackageManager();

                        final ResolveInfo mInfo = pm.resolveActivity(i, 0);

                        Intent intent = new Intent();
                        intent.setComponent(new ComponentName(mInfo.activityInfo.packageName, mInfo.activityInfo.name));
                        intent.setAction(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_LAUNCHER);
                        startActivity(intent);
                    } catch (Exception e) {
                    }
                }
            }
        });

        btCamera2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rlBtCamera.getVisibility()!=View.VISIBLE) {
                    Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    try {
                        PackageManager pm = getPackageManager();

                        final ResolveInfo mInfo = pm.resolveActivity(i, 0);

                        Intent intent = new Intent();
                        intent.setComponent(new ComponentName(mInfo.activityInfo.packageName, mInfo.activityInfo.name));
                        intent.setAction(Intent.ACTION_MAIN);
                        intent.addCategory(Intent.CATEGORY_LAUNCHER);
                        startActivity(intent);
                    } catch (Exception e) {
                    }
                }
            }
        });

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if(position==0){
                    icoFolder.setBackgroundResource(R.drawable.folders_on);
                    icoRecent.setBackgroundResource(R.drawable.recents_off);

                    txFolder.setTextColor(Color.parseColor("#cd2129"));
                    txRecent.setTextColor(Color.parseColor("#c7c7c7"));

                }
                else {

                    icoFolder.setBackgroundResource(R.drawable.folder_off);
                    icoRecent.setBackgroundResource(R.drawable.recents_on);

                    txRecent.setTextColor(Color.parseColor("#cd2129"));
                    txFolder.setTextColor(Color.parseColor("#c7c7c7"));
                }
            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        rvHistory = new RecyclerView(this);
        rvFolders = new RecyclerView(this);
        context = this;

        btOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this,OptionsActivity.class));
            }
        });


        LoadMedia();
        SetUpRv();
        rvAdapterFolders.UpDateViews(folderList);
        feedAdapter.UpDateViews(folderList.getToDayPhotos(),folderList.getLast7DaysPhotos());
    }

    private void layoutController(int dy){
        if (dy > 0) {

            if(rlBtCamera.getVisibility()==View.VISIBLE) {
                TranslateAnimation animate = new TranslateAnimation(
                        0,                 // fromXDelta
                        0,                 // toXDelta
                        0,                 // fromYDelta
                        llTabs.getHeight()); // toYDelta
                animate.setDuration(150);
                animate.setFillAfter(true);

                animate.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        rlBtCamera.setVisibility(View.GONE);
                        llTabs.setVisibility(View.GONE);
                        rlBtCamera2.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                rlBtCamera.startAnimation(animate);
            }

        } else {

            if(rlBtCamera.getVisibility()==View.GONE) {
                TranslateAnimation animate = new TranslateAnimation(
                        0,                 // fromXDelta
                        0,                 // toXDelta
                        llTabs.getHeight(),               // fromYDelta
                        0); // toYDelta
                animate.setDuration(150);
                animate.setFillAfter(true);

                animate.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {
                        rlBtCamera2.setVisibility(View.GONE);
                        llTabs.setVisibility(View.VISIBLE);
                        rlBtCamera.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });
                rlBtCamera.startAnimation(animate);
            }
        }
    }

    private void LoadMedia(){
        folderList = new FolderList(context);
        folderList.LoadMedia(rawStatus);
    }

    private void SetUpRv(){
        rvFolders.setHasFixedSize(true);
        rvFolders.setLayoutManager(new GridLayoutManager(context, getNumRoons()));

        rvHistory.setHasFixedSize(true);
        rvHistory.setLayoutManager(new LinearLayoutManager(this));

        feedAdapter = new CardViewAdapter_historyFeed(this,this,folderList.getToDayPhotos(),folderList.getLast7DaysPhotos());
        rvAdapterFolders =new rvAdapterFolders(context,this,feedAdapter,folderList,getNumPhotos(),newStyleStatus,gridStatus);

        rvFolders.setAdapter(rvAdapterFolders);
        rvHistory.setAdapter(feedAdapter);

        Vector<View> pages = new Vector<>();

        pages.add(rvFolders);
        if(new Berreivement_values(this).getRecentStatus()) {
            pages.add(rvHistory);

            rvHistory.addOnScrollListener(new RecyclerView.OnScrollListener() {

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);

                    layoutController(dy);
                }

            });

            rvFolders.addOnScrollListener(new RecyclerView.OnScrollListener() {

                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    layoutController(dy);
                }

                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                }

            });
            if(llTabs != null){
                llTabs.setVisibility(View.VISIBLE);
            }
        }
        else{
            if(llTabs != null){
                llTabs.setVisibility(View.GONE);
            }
        }

        CustomPagerAdapter adapter = new CustomPagerAdapter(this,pages);

        viewPager.setAdapter(adapter);


    }

    private int getNumPhotos(){

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;

        //112

        int retorno = ((int) (dpWidth/getNumRoons())/100);

        if (retorno>4){
            return 4;
        }
        else return retorno;

    }

    private int getNumRoons(){

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;

        if(gridStatus){
            return ((int) dpWidth/300)*2;
        }
        else{
            return ((int) dpWidth/300);
        }
    }

    //Just use it after Ignore an folder
    public void UpdateView(){
        LoadMedia();
        rvAdapterFolders.UpDateViews(folderList);
    }

    AlertDialog alertDialog;
    private void welcomeDialog() {
        final AlertDialog.Builder RecDialog = new AlertDialog.Builder(context);

        //Typeface tittle_type = MainActivity.getTyperFace();

        TextView dialogTittle = new TextView(context);
        dialogTittle.setText(R.string.NewVersionDialogTittle);
        //dialogTittle.setTypeface(tittle_type);
        dialogTittle.setTextColor(Color.WHITE);
        dialogTittle.setPadding(20, 10, 10, 10);
        dialogTittle.setTextSize(25);
        dialogTittle.setBackgroundResource(R.drawable.datail_gradient);


        final TextView Message = new TextView(context);
        Message.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        Message.setText(getString(R.string.NewVersionDialogMessage));
        Message.setPadding(30,10,30,0);
        //Directory.setTypeface(tittle_type);

        final TextView Message2 = new TextView(context);
        Message2.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        Message2.setText(getString(R.string.NewVersionDialogMessage2));
        Message2.setPadding(60,30,60,20);
        //Directory.setTypeface(tittle_type);

        LinearLayout dialogLayout = new LinearLayout(context);
        dialogLayout.setOrientation(LinearLayout.VERTICAL);
        dialogLayout.addView(dialogTittle);
        dialogLayout.addView(Message);
        dialogLayout.addView(Message2);


        RecDialog.setView(dialogLayout);


        RecDialog.setPositiveButton(getString(R.string.NewVersionDialogButton),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });


        alertDialog = RecDialog.create();
        alertDialog.show();


        alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getColor(R.color.colorDetailDark));
        }
        else {
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.GRAY);
        }

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTransformationMethod(null);
    }

}
