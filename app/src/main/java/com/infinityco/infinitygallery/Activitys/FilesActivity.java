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

import com.infinityco.infinitygallery.Adapters.rvAdapterHiddenFolders;
import com.infinityco.infinitygallery.Objects.Berreivement_values;
import com.infinityco.infinitygallery.Objects.HorSwipeAwareRelativeLayout;
import com.infinityco.infinitygallery.R;

public class FilesActivity extends AppCompatActivity {


    @Override public void finish() {
        super.finish();
       // overridePendingTransition(0, 0);
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

        Button back =  findViewById(R.id.btBack);
        Button btFileBack =  findViewById(R.id.btFiles);

        TextView tvNoHidden = findViewById(R.id.tvNoHidden);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btFileBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        RecyclerView rvHiddenFolders = findViewById(R.id.rvFiles);
        rvHiddenFolders.setHasFixedSize(true);
        rvHiddenFolders.setLayoutManager(new LinearLayoutManager(this));
        try {
            String[] hidenfolders = new Berreivement_values(this).getIgnoredFolders();
            if(hidenfolders.length==0){

                rvHiddenFolders.setVisibility(View.GONE);
                tvNoHidden.setText(getString(R.string.noHiddenFolders));
            }
            else {
                rvHiddenFolders.setAdapter(new rvAdapterHiddenFolders(this, hidenfolders));
            }
        }
        catch (Exception e){
            rvHiddenFolders.setVisibility(View.GONE);
            tvNoHidden.setText(getString(R.string.noHiddenFolders));
        }

        HorSwipeAwareRelativeLayout rlActivity = findViewById(R.id.rlActivity);
        rlActivity.setOnHorizontalSwipeListener(new HorSwipeAwareRelativeLayout.OnHorizontalSwipeListener() {
            @Override
            public void onRightSwipe() {
                finish();
            }
        });
    }


}
