package com.infinityco.infinitygallery.Activitys;

import android.graphics.Color;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import com.infinityco.infinitygallery.Adapters.CardViewAdapter_BerreivermentOptions;
import com.infinityco.infinitygallery.Objects.HorSwipeAwareRelativeLayout;
import com.infinityco.infinitygallery.R;

import java.util.ArrayList;
import java.util.List;

public class BerreivementActivity extends AppCompatActivity {

    private Button back,btBerreivementBack;
    private TextView tvNoFolders;
    private RecyclerView rvBerreivement;
    private List<CardViewAdapter_BerreivermentOptions.Berreiverment> options;

    @Override public void finish() {
        super.finish();
        //overridePendingTransition(0, 0);
    }

    @Override
    public void onResume(){
        super.onResume();

    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(MainActivity.isDarkTheme()){
            setContentView(R.layout.activity_files_dark);
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setNavigationBarColor(Color.BLACK);
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.BLACK);

                getWindow().getDecorView().setSystemUiVisibility(0);
            }
        }
        else {
            setContentView(R.layout.activity_files);
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


        tvNoFolders = (TextView) findViewById(R.id.tvNoHidden);
        tvNoFolders.setVisibility(View.GONE);
        back = (Button) findViewById(R.id.btBack);
        btBerreivementBack = (Button) findViewById(R.id.btFiles);

        btBerreivementBack.setText(getText(R.string.Berreivment));



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btBerreivementBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        options = new ArrayList<>();
        options.add(new CardViewAdapter_BerreivermentOptions.Berreiverment(getString(R.string.NewFolderPath),getString(R.string.NewFolderpathMessage)));
        options.add(new CardViewAdapter_BerreivermentOptions.Berreiverment(getString(R.string.DarkTheme),getString(R.string.DarkThemeMensage)));
        options.add(new CardViewAdapter_BerreivermentOptions.Berreiverment(getString(R.string.FullScreenTittle),getString(R.string.FullScreenMensage)));
        options.add(new CardViewAdapter_BerreivermentOptions.Berreiverment(getString(R.string.NewStyleTittle),getString(R.string.NewStyleMensage)));
        options.add(new CardViewAdapter_BerreivermentOptions.Berreiverment(getString(R.string.RecentPhotos),getString(R.string.RecentPhotosMensage)));
        options.add(new CardViewAdapter_BerreivermentOptions.Berreiverment(getString(R.string.GridTheme),getString(R.string.GridThemeMensage)));
        options.add(new CardViewAdapter_BerreivermentOptions.Berreiverment(getString(R.string.RawStatus),getString(R.string.RawStatusMensage)));
        options.add(new CardViewAdapter_BerreivermentOptions.Berreiverment(getString(R.string.HighQualityTittle),getString(R.string.HighQualitySpinnerMessage)));

        rvBerreivement = (RecyclerView) findViewById(R.id.rvFiles);
        rvBerreivement.setHasFixedSize(true);
        rvBerreivement.setLayoutManager(new LinearLayoutManager(this));

        rvBerreivement.setAdapter(new CardViewAdapter_BerreivermentOptions(this,options));

        ((CardViewAdapter_BerreivermentOptions) rvBerreivement.getAdapter()).setOnItemClickListener(new CardViewAdapter_BerreivermentOptions.MyClickListener() {
            @Override
            public void onItemClick(int clickPostion) {
            }
        });

        HorSwipeAwareRelativeLayout rlActivity = (HorSwipeAwareRelativeLayout) findViewById(R.id.rlActivity);
        rlActivity.setOnHorizontalSwipeListener(new HorSwipeAwareRelativeLayout.OnHorizontalSwipeListener() {
            @Override
            public void onRightSwipe() {
                finish();
            }
        });
    }
}
