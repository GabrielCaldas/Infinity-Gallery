package com.infinityco.infinitygallery.Objects;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.core.content.FileProvider;

import com.infinityco.infinitygallery.Activitys.MainActivity;
import com.infinityco.infinitygallery.BuildConfig;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Media implements Comparable<Media>{
    private int index;
    private String name;
    long date;
    private int type;
    private Context context;
    private File media;
    private boolean Raw = false;

    static public int VideoType = 02,ImageType = 01;



    private String folderPath;

    public Media(Context context,String name,File media){
        this.folderPath = media.getParentFile().getPath();
        this.name = name;
        this.context = context;
        this.media = media;
        if(media.getPath().endsWith(".mp4")||media.getPath().endsWith(".avi")||media.getPath().endsWith(".mpeg")||media.getPath().endsWith(".mov")||media.getPath().endsWith(".wmv")||media.getPath().endsWith(".flv")) {
            this.type = VideoType;
        }
        else {
            this.type = ImageType;
        }

        if(type == ImageType) {
            if (rawTest(media.getPath())) {
                Raw = true;
            }
            else {
                Raw = false;
            }
        }
        else {
            Raw = false;
        }
    }

    public Media(Context context,int index, int type,String name,long  date,File media){
        this.index = index;
        this.folderPath = media.getParentFile().getPath();
        this.type = type;
        this.name = name;
        this.date = date;
        this.context = context;
        this.media = media;

        if(rawTest(media.getPath())){
            Raw = true;
        }
        else {
            Raw = false;
        }
    }

    private boolean rawTest(String file){
        if(     file.toLowerCase().toLowerCase().endsWith(".raw")||
                file.toLowerCase().toLowerCase().endsWith(".3fr")||
                file.toLowerCase().toLowerCase().endsWith(".arw")||
                file.toLowerCase().toLowerCase().endsWith(".srf")||
                file.toLowerCase().toLowerCase().endsWith(".sr2")||
                file.toLowerCase().toLowerCase().endsWith(".crw")||
                file.toLowerCase().toLowerCase().endsWith(".cr2")||
                file.toLowerCase().toLowerCase().endsWith(".cap")||
                file.toLowerCase().toLowerCase().endsWith(".tif")||
                file.toLowerCase().toLowerCase().endsWith(".iiq")||
                file.toLowerCase().toLowerCase().endsWith(".eiq")||

                file.toLowerCase().toLowerCase().endsWith(".eip")||
                file.toLowerCase().toLowerCase().endsWith(".dcs")||
                file.toLowerCase().toLowerCase().endsWith(".dcr")||
                file.toLowerCase().toLowerCase().endsWith(".drf")||
                file.toLowerCase().toLowerCase().endsWith(".k25")||
                file.toLowerCase().toLowerCase().endsWith(".kdc")||
                file.toLowerCase().toLowerCase().endsWith(".tif")||
                file.toLowerCase().toLowerCase().endsWith(".dng")||
                file.toLowerCase().toLowerCase().endsWith(".erf")||
                file.toLowerCase().toLowerCase().endsWith(".fff")||

                file.toLowerCase().toLowerCase().endsWith(".mef")||
                file.toLowerCase().toLowerCase().endsWith(".mrw")||
                file.toLowerCase().toLowerCase().endsWith(".mos")||
                file.toLowerCase().toLowerCase().endsWith(".nef")||
                file.toLowerCase().toLowerCase().endsWith(".nrw")||
                file.toLowerCase().toLowerCase().endsWith(".orf")||
                file.toLowerCase().toLowerCase().endsWith(".ptx")||
                file.toLowerCase().toLowerCase().endsWith(".pef")||
                file.toLowerCase().toLowerCase().endsWith(".pxn")||
                file.toLowerCase().toLowerCase().endsWith(".r3d")||

                file.toLowerCase().toLowerCase().endsWith(".raf")||
                file.toLowerCase().toLowerCase().endsWith(".raw")||
                file.toLowerCase().toLowerCase().endsWith(".rw2")||
                file.toLowerCase().toLowerCase().endsWith(".r2l")||
                file.toLowerCase().toLowerCase().endsWith("..dn")||
                file.toLowerCase().toLowerCase().endsWith(".si3")||
                file.toLowerCase().toLowerCase().endsWith(".e2z")||
                file.toLowerCase().toLowerCase().endsWith(".x3f")){
            return true;
        }
        else
            return false;
    }

    public Boolean isRaw(){
        return Raw;
    }

    public String getDateString() {
        Date Date=new Date(date);
        SimpleDateFormat df2 = new SimpleDateFormat("dd/MM/yy");
        return df2.format(Date);
    }

    public long getDate() {
        return date;
    }

    protected Date getDateTime(){
        return new Date(date);
    }
    public String getName() {
        return name;
    }
    public String getFolderPath() {
        return folderPath;
    }

    public int getIndex(){
        return index;
    }

    public int getType(){
        return type;
    }

    public String getPath() {
        return media.getPath();
    }

    public File getFile() {
        return media;
    }


    public boolean Delete(){
        boolean b = media.delete();
        scanFile(media.getAbsolutePath());
        return b;
    }

    public boolean DeleteNoScan(){
        boolean b = media.delete();
        if(!b&&media.exists()){
            try {
                b = media.getCanonicalFile().delete();
            }
            catch (Exception e){
                return false;
            }
        }

        return b;
    }

    public Uri getUri(){
        return Uri.fromFile(media);
    }
    public void moveTo(String newPath){

        createDirIfNotExists(newPath);

        File targetLocation = new File (newPath+"/"+name);

        media.renameTo(targetLocation);

        if(!media.exists()){
            scanFile(media.getPath());
        }
        if (targetLocation.exists()){
            this.folderPath = targetLocation.getParentFile().getPath();
            this.media = targetLocation;

            scanFile(targetLocation.getAbsolutePath());
        }
    }

    public static boolean createDirIfNotExists(String path) {
        boolean ret = true;

        File file = new File(path);
        if (!file.exists()) {
            if (!file.mkdirs()) {
                Log.e("TravellerLog :: ", "Problem creating Image folder");
                ret = false;
            }
        }
        return ret;
    }

    private void scanFile(String path) {
        MediaScannerConnection.scanFile(context,
                new String[]{path}, null, new MediaScannerConnection.MediaScannerConnectionClient() {
                    @Override
                    public void onMediaScannerConnected() {

                    }

                    @Override
                    public void onScanCompleted(String s, Uri uri) {

                    }
                });
    }




    @Override
    public int compareTo(Media f) {
        int i = f.getDateTime().compareTo(getDateTime());
        return i;
    }
}

