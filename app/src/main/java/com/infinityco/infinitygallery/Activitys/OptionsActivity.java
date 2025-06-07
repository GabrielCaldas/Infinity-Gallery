package com.infinityco.infinitygallery.Activitys;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import com.infinityco.infinitygallery.Adapters.listViewCustomAdapter_Options;
import com.infinityco.infinitygallery.Objects.HorSwipeAwareRelativeLayout;
import com.infinityco.infinitygallery.R;

public class OptionsActivity extends AppCompatActivity {




    @Override public void finish() {
        super.finish();
        //overridePendingTransition(0,0);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(MainActivity.isDarkTheme()){
            setContentView(R.layout.activity_options_dark);
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().setNavigationBarColor(Color.BLACK);
                Window window = getWindow();
                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                window.setStatusBarColor(Color.BLACK);

                getWindow().getDecorView().setSystemUiVisibility(0);
            }
        }
        else {
            setContentView(R.layout.activity_options);
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

        Button btOptions =findViewById(R.id.btOptions);
        Button back =  findViewById(R.id.btBack);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        ListView Options = findViewById(R.id.lvOptions);

        String[] options = new String[2];
        int[] optionsimg = new int[2];



        options[0]=getString(R.string.Files);
        options[1]=getString(R.string.Berreivment);


        optionsimg[0]= R.drawable.folder;
        optionsimg[1]= R.drawable.beheiver_icon;

        listViewCustomAdapter_Options adp = new listViewCustomAdapter_Options(this,options,optionsimg);
        Options.setAdapter(adp);

        Options.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               if(position==0){
                    startActivity(new Intent(getApplicationContext(),FilesActivity.class));
                }
                else if(position == 1){
                    startActivity(new Intent(getApplicationContext(),BerreivementActivity.class));
                }
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
}
