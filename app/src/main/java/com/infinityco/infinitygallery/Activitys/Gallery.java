package com.infinityco.infinitygallery.Activitys;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import androidx.core.content.FileProvider;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.infinityco.infinitygallery.Adapters.ListViewCustomAdapter_MediaDetail;
import com.infinityco.infinitygallery.Adapters.vpAdapterGallery;
import com.infinityco.infinitygallery.Helpers.ClickableViewPager;
import com.infinityco.infinitygallery.Objects.Berreivement_values;
import com.infinityco.infinitygallery.Objects.Folder;
import com.infinityco.infinitygallery.Objects.FolderList;
import com.infinityco.infinitygallery.Objects.Media;
import com.infinityco.infinitygallery.R;

import java.io.File;
import java.util.ArrayList;

public class Gallery extends AppCompatActivity {

    //Layout
    private ClickableViewPager vpMedias;
    private RelativeLayout rlButtons,rlRawFlag;

    //Objects
    private Folder folder;
    private int pos;
    private AlertDialog alertDialog; //deleteAlertDialog()
    private boolean imersive = false,fullscreen=false;

    @Override
    public void onResume(){
        super.onResume();
        setImage(pos);
    }

    @Override public void finish() {
        super.finish();
        //overridePendingTransition(0,0);
    }

    @SuppressLint("NewApi")
    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        if(currentApiVersion >= Build.VERSION_CODES.KITKAT && hasFocus&&fullscreen)
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
        setContentView(R.layout.activity_gallery);

        if(new Berreivement_values(this).getFullscreenStatus()){
            setupFullscreen();
            fullscreen=true;
        }

        Intent iin = getIntent();
        if(iin.getData()!=null){
            Uri uri = iin.getData();
            String path;

            if(new File(uri.getPath()).exists()){
                path = uri.getPath();
            }
            else {
                try {
                    path = getRealPathFromURI1(uri);
                } catch (Exception e) {
                    path = getRealPathFromURI2(this, uri);
                }
            }

            LoadFolderFromPath(new File(path).getParentFile().getPath());
            pos = findPosFromPath(path);

            if (pos == -1 || folder.getMedias().size() == 0) {
                File media = new File(path);

                folder.addMedia(new Media(this, media.getName(), media));

                pos = 0;

                emptyMediasDialog(path);
            }

            SetUp();
            setUpVp();
        }else {
            Bundle bundle = iin.getExtras();
            try {

                pos = (int) bundle.get("Pos");

                String path = (String) bundle.get("Folder");


                if (!path.equals("/////////////")) {
                    LoadFolder(path);
                } else {
                    int Case = (int) bundle.get("Feed");
                    switch (Case) {
                        case 0:
                            folder = new FolderList(this).getToDayfolder();
                            break;
                        case 1:
                            folder = new FolderList(this).getLast7folder();
                            break;
                    }
                }
                SetUp();
                setUpVp();
            } catch (Exception em) {

            }
        }
    }

    //Load Medias

    // - Normal
    private void LoadFolder(String path){
        folder = new Folder(this,path);
    }

    // - From extern
    public String getRealPathFromURI1(Uri contentUri) {
        String path = null;
        String[] proj = { MediaStore.MediaColumns.DATA };
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            path = cursor.getString(column_index);
        }
        cursor.close();
        return path;
    }
    private String getRealPathFromURI2(final Context context, final Uri uri) {



        // DocumentProvider
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            if (DocumentsContract.isDocumentUri(context, uri)) {
                // ExternalStorageProvider
                if (isExternalStorageDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    if ("primary".equalsIgnoreCase(type)) {
                        return Environment.getExternalStorageDirectory() + "/" + split[1];
                    }

                    // TODO handle non-primary volumes
                }
                // DownloadsProvider
                else if (isDownloadsDocument(uri)) {

                    final String id = DocumentsContract.getDocumentId(uri);
                    final Uri contentUri = ContentUris.withAppendedId(
                            Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                    return getDataColumn(context, contentUri, null, null);
                }
                // MediaProvider
                else if (isMediaDocument(uri)) {
                    final String docId = DocumentsContract.getDocumentId(uri);
                    final String[] split = docId.split(":");
                    final String type = split[0];

                    Uri contentUri = null;
                    if ("image".equals(type)) {
                        contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                    } else if ("video".equals(type)) {
                        contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                    } else if ("audio".equals(type)) {
                        contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                    }

                    final String selection = "_id=?";
                    final String[] selectionArgs = new String[]{
                            split[1]
                    };

                    return getDataColumn(context, contentUri, selection, selectionArgs);
                }
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }
    private int findPosFromPath(String path){
        int pos= -1;
        for(int i=0;i<folder.getMedias().size();i++){
            if(folder.getMedias().get(i).getPath().equals(path)){
                pos = i;
                break;
            }
        }
        return pos;
    }

    private String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }
    private boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }
    private boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }
    private boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }
    private boolean isGooglePhotosUri(Uri uri){
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public void emptyMediasDialog(final String path){
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.LoadMediaExternNotMappedMessage));
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(getText(R.string.LoadMediaExternNotMappedAction), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mapFolder(new File(path).getParent(),path);
            }
        });
        builder.create().show();
    }
    private void mapFolder(final String path,final  String media){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT){
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, Uri.parse("file://" + path)));
        }
        else {
            sendBroadcast(new Intent(Intent.ACTION_MEDIA_MOUNTED, Uri.parse("file://" + path)));
        }

        LoadFolderFromPath(path);

        if(folder.getMedias().size()==0) {
            folder.findMediasFromEmptyFolder();
        }

        pos = findPosFromPath(media);

        SetUp();
        setUpVp();
    }

    private void LoadFolderFromPath(String path){
        folder = new Folder(this,path);
    }



    //UI
    private int currentApiVersion;
    private void setupFullscreen(){
        currentApiVersion = android.os.Build.VERSION.SDK_INT;


        final int flags;
        if(android.os.Build.VERSION.SDK_INT > 19) {
            flags =View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
        }
        else {

            flags =View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_FULLSCREEN;
        }

        // This work only for android 4.4+
        if(currentApiVersion >= Build.VERSION_CODES.KITKAT)
        {
            imersive=true;
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

    private void SetUp(){
        vpMedias = findViewById(R.id.vpMedias);

        rlButtons =  findViewById(R.id.rlGalleryButtons);
        rlRawFlag =  findViewById(R.id.rlRawFlag);

        Button btBack =  findViewById(R.id.btGalleryBack);
        Button btExclude =  findViewById(R.id.btGalleryDelete);
        Button btOptions =  findViewById(R.id.btGalleryOptions);
        Button btShare =  findViewById(R.id.btGalleryShare);
        Button btRaw =  findViewById(R.id.btRaw);

        btBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btExclude.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exclude();
            }
        });


        btOptions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               options(view);
            }
        });

        btShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaShare();
            }
        });

        btRaw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(rlRawFlag.getVisibility() == RelativeLayout.VISIBLE){
                    rawDialog();
                }
            }
        });


    }

    private void setUpVp(){
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        vpMedias.setOffscreenPageLimit(1);
        Berreivement_values bv = new Berreivement_values(this);
        vpMedias.setAdapter(new vpAdapterGallery(this,folder.getMedias(),rlButtons,imersive,this,bv.getHighQuallityStatus()));

        setUpVpListener();
    }
    private void setUpVpListener() {
        vpMedias.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                rawFlagUpdate(folder.getMedias().get(position));
            }

            @Override
            public void onPageSelected(int position) {
                pos = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
    }

    private void rawFlagUpdate(Media media){
        if(media.isRaw()){
            rlRawFlag.setVisibility(RelativeLayout.VISIBLE);
        }
        else {
            rlRawFlag.setVisibility(RelativeLayout.INVISIBLE);
        }
    }


    //Options
    private void setImage(int pos){
        vpMedias.setCurrentItem(pos,false);

        this.pos = pos;

    }

    private void mediaShare(){
        ArrayList<Uri> imageUris = new ArrayList<>();

        Uri sendUri2;
        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            sendUri2 = FileProvider.getUriForFile(
                    this,
                    getApplicationContext()
                            .getPackageName() + ".provider", folder.getMedias().get(pos).getFile());
        else{
            sendUri2 = Uri.fromFile(folder.getMedias().get(pos).getFile());
        }

        imageUris.add(sendUri2);


        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND_MULTIPLE);
        shareIntent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
        shareIntent.setType("image/*");
        startActivity(Intent.createChooser(shareIntent, getString(R.string.shareto)));
    }

    private void rawDialog(){
        final AlertDialog.Builder RecDialog = new AlertDialog.Builder(this);

        //Typeface tittle_type = MainActivity.getTyperFace();

        TextView dialogTittle = new TextView(this);
        dialogTittle.setText(R.string.RawStatus);
        //dialogTittle.setTypeface(tittle_type);
        dialogTittle.setTextColor(Color.WHITE);
        dialogTittle.setPadding(20,10,10,10);
        dialogTittle.setTextSize(25);
        dialogTittle.setBackgroundResource(R.drawable.datail_gradient);

        final TextView input = new TextView(this);
        input.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        input.setText(R.string.RawDialogMensage);
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

        RecDialog.setNeutralButton(R.string.Done,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });

        RecDialog.setPositiveButton(R.string.RawDialogChange,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(Gallery.this,BerreivementActivity.class));
                        finish();
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
            alertDialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setTextColor(getColor(R.color.colorDetailDark));
        }
        else {
            alertDialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setTextColor(Color.GRAY);
        }

        alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(Color.GRAY);

        alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTransformationMethod(null);
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTransformationMethod(null);
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
        input.setText(R.string.excludeMessage);
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
                        if(folder.getMedias().get(pos).Delete()) {
                            if (pos == 0) {
                                if (folder.getMedias().size() > 1) {
                                    folder.getMedias().remove(pos);
                                    setUpVp();
                                    setImage(0);
                                } else {
                                    finish();
                                }
                            } else {
                                folder.getMedias().remove(pos);
                                setUpVp();
                                setImage(pos - 1);


                                folder.scanFolder();
                            }
                        }
                        else{

                            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.Q){

                                startActivity(new Intent(Gallery.this, RequestFileManagerActivity.class));

                            }
                            else{

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Toast.makeText(Gallery.this,getString(R.string.excludFail), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }
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
        //alertDialog.getWindow().setTitleColor(Color.RED);
        alertDialog.show();



        alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
        alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(Color.GRAY);

        alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTransformationMethod(null);
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTransformationMethod(null);
    }

    private void options(View view){
        //if(true||folder.getMedias().get(pos).getType()==Media.ImageType) {
            PopupMenu popup = new PopupMenu(Gallery.this, view);
            MenuInflater inflater = popup.getMenuInflater();

            if (folder.getMedias().get(pos).getType() == Media.ImageType) {
                inflater.inflate(R.menu.menu_main, popup.getMenu());
            }
            else {
                inflater.inflate(R.menu.menu_main_video, popup.getMenu());
            }
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {

                    int id = item.getItemId();
                    return menuSetClick(id);
                }
            });
            popup.show();
        /*}
        else {
            Toast.makeText(this, getString(R.string.Set_as_fail), Toast.LENGTH_SHORT).show();
        }*/
    }

    private boolean menuSetClick(int id){
        switch (id){
            case R.id.btEdit:
                if (folder.getMedias().get(pos).getType() == Media.ImageType) {

                    startActivity(new Intent(Gallery.this, PhotoEditorActivity.class).putExtra("path",folder.getMedias().get(pos).getPath()));
                } else {
                    Toast.makeText(Gallery.this, getString(R.string.Set_as_fail), Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.btSetAs:
                if (folder.getMedias().get(pos).getType() == Media.ImageType) {
                    Uri sendUri2;
                    if (android.os.Build.VERSION.SDK_INT >= 24)
                        sendUri2 = FileProvider.getUriForFile(
                                Gallery.this,
                                getApplicationContext()
                                        .getPackageName() + ".provider", folder.getMedias().get(pos).getFile());
                    else {
                        sendUri2 = Uri.fromFile(folder.getMedias().get(pos).getFile());
                    }
                    setAs(sendUri2);
                } else {
                    Toast.makeText(Gallery.this, getString(R.string.Set_as_fail), Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.btDetail:
                detail(folder.getMedias().get(pos));
                return true;
            default:
                return false;
        }
    }

    private void detail(final Media media){
        final AlertDialog.Builder RecDialog = new AlertDialog.Builder(this);

        //Typeface tittle_type = MainActivity.getTyperFace();

        TextView dialogTittle = new TextView(this);
        dialogTittle.setText(R.string.details);
        //dialogTittle.setTypeface(tittle_type);
        dialogTittle.setTextColor(Color.WHITE);
        dialogTittle.setPadding(20,10,10,10);
        dialogTittle.setTextSize(25);
        dialogTittle.setBackgroundResource(R.drawable.datail_gradient);

        final ListView details = new ListView(this);
        details.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        details.setAdapter(new ListViewCustomAdapter_MediaDetail(this,media));

        //input.setTypeface(tittle_type);

        LinearLayout dialogLayout = new LinearLayout(this);
        dialogLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout editTextLayout = new LinearLayout(this);
        editTextLayout.setOrientation(LinearLayout.HORIZONTAL);
        editTextLayout.addView(details);

        dialogLayout.addView(dialogTittle);
        dialogLayout.addView(editTextLayout);

        RecDialog.setView(dialogLayout);


        RecDialog.setPositiveButton(R.string.Done,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });



        alertDialog = RecDialog.create();
        alertDialog.show();


        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            alertDialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setTextColor(getColor(R.color.colorDetailDark));
        }
        else {
            alertDialog.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setTextColor(Color.GRAY);
        }
    }


    private void setAs(Uri photoUri){
        Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
        intent.setDataAndType(photoUri, "image/*");
        intent.putExtra("mimeType", "image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.Set_as)), 200);
    }

}
