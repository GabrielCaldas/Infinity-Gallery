package com.infinityco.infinitygallery.Objects;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import com.infinityco.infinitygallery.Activitys.FolderActivity;
import com.infinityco.infinitygallery.Activitys.MainActivity;
import com.infinityco.infinitygallery.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class Folder implements Comparable<Folder>{

    private List<Media> medias = new ArrayList<>();
    private List<Media> mediasRaw = new ArrayList<>();
    private File folder;
    private Context context;
    public static int defaltCase =0, TodayCase = 1, Last7Case = 2;
    private int Case = defaltCase,size = 0;

    public Folder(Context context,Media photo){
        folder = new File(photo.getFolderPath());
        medias.add(photo);
        size++;
        this.context = context;
    }

    public Folder(Context context,String path){
        this.context = context;
        this.folder = new File(path);
        reload();
    }


    public Folder(Context context,int Case){
        this.Case = Case;
        this.context = context;
        reload();
    }

    public  void findMediasFromEmptyFolder(){
        for (File f : folder.listFiles()) {
            if (f.isFile()) {
                if(!f.getPath().endsWith(".nomedia")) {
                    addMedia(new Media(context, f.getName(), f));
                }
            }

        }
    }
    public void addMedia(int index,int type,String name,long date,File file){
        medias.add(new Media(context,index,type,name,date,file));
        size++;
    }

    public void addMedia(Media media){
        medias.add(media);
        size++;
    }

    public void reload(){


        switch (Case){
            case 0:
                medias = new ArrayList<>();
                LoadMediaImages();
                LoadMediaVideos();
                sortMedia();
                break;
            case 1:
                medias = new ArrayList<>();
                LoadMediaImagesRecents(new Berreivement_values(context).getRawStatus());
                LoadMediaVideosRecents();
                sortMedia();
                break;
            case 2:
                medias = new ArrayList<>();
                LoadMediaImagesRecents(new Berreivement_values(context).getRawStatus());
                LoadMediaVideosRecents();
                sortMedia();
                break;
        }


    }

    private void LoadMediaImages(){
        Uri uri;

        size=0;
        Cursor cursor;
        int Column_index;
        String path = null,sortOrder,name;
        long date;
        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = { MediaStore.MediaColumns.DATA };

        sortOrder = MediaStore.Images.ImageColumns.DATE_ADDED;

        String[] selectionArgs = new String[]{
                "%" + folder + "%",
                "%" + folder + "/%/%"
        };

        cursor = context.getContentResolver().query(uri, projection, MediaStore.Images.Media.DATA + " LIKE ? AND " + MediaStore.Images.Media.DATA + " NOT LIKE ? ",selectionArgs, sortOrder);


        try {
            if (null != cursor) {
                Column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                while (cursor.moveToNext()) {
                    path = cursor.getString(Column_index);
                    File file = new File(path);
                    if(file.exists()) {
                        name = file.getName();
                        date = file.lastModified();

                        if(MainActivity.getRawStatus()) {
                            addMedia(new Media(context, cursor.getPosition(), Media.ImageType, name, date, file));
                        }
                        else{
                            if(!rawTest(file.getPath())){
                                addMedia(new Media(context, cursor.getPosition(), Media.ImageType, name, date, file));
                            }
                        }
                    }
                }
                cursor.close();
            }
        }catch (Exception e){
            e.printStackTrace();
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

    private void LoadMediaVideos(){
        Uri uri;
        Cursor cursor;
        int Column_index;
        String path = null,sortOrder,name;
        long date;
        uri = android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = { MediaStore.MediaColumns.DATA };

        sortOrder = MediaStore.Video.VideoColumns.DATE_ADDED;

        String[] selectionArgs = new String[]{
                "%" + folder + "%",
                "%" + folder + "/%/%"
        };

        cursor = context.getContentResolver().query(uri, projection, MediaStore.Images.Media.DATA + " LIKE ? AND " + MediaStore.Images.Media.DATA + " NOT LIKE ? ",selectionArgs, sortOrder);

        try {
            if (null != cursor) {
                Column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);

                while (cursor.moveToNext()) {
                    path = cursor.getString(Column_index);
                    File file = new File(path);
                    if(file.exists()) {
                        name = file.getName();
                        date = file.lastModified();


                        addMedia(new Media(context, cursor.getPosition(), Media.VideoType, name, date,file));
                    }
                }
                cursor.close();
//imageList gets populated with paths to images by here
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private void LoadMediaImagesRecents(boolean loadRaw){
        Uri uri;
        Cursor cursor;
        int Column_index;
        String path = null,sortOrder,name;
        long date;
        uri = android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
        String[] projection = { MediaStore.MediaColumns.DATA};
//DATA is the path to the corresponding image. We only need this for loading //image into a recyclerview

        sortOrder = MediaStore.Images.ImageColumns.DATE_ADDED;
//This sorts all images such that recent ones appear first

        //cursor = context.getContentResolver().query(uri, projection, MediaStore.Images.Media.DATA + " like ? " ,new String[] {"%Download%"}, sortOrder);


        cursor = context.getContentResolver().query(uri, projection, null,null, sortOrder);


        try {
            if (null != cursor) {
                Column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
                while (cursor.moveToNext()) {
                    path = cursor.getString(Column_index);
                    File file = new File(path);
                    if (file.exists()) {
                        name = file.getName();
                        date = file.lastModified();

                        if(loadRaw) {
                            addMediaRecents(new Media(context, cursor.getPosition(), Media.ImageType, name, date, file));
                        }
                        else{
                            if(!rawTest(file.getPath())){
                                addMediaRecents(new Media(context, cursor.getPosition(), Media.ImageType, name, date, file));
                            }
                        }
                    }
                }
                cursor.close();
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }
    private void LoadMediaVideosRecents(){
        Uri uri;
        Cursor cursor;
        int Column_index;
        String path = null,sortOrder,name;
        long date;
        uri = android.provider.MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
        String[] projection = { MediaStore.MediaColumns.DATA};

        sortOrder = MediaStore.Video.VideoColumns.DATE_ADDED;

        cursor = context.getContentResolver().query(uri, projection, null,null, sortOrder);



        try {
            if (null != cursor) {
                Column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);

                while (cursor.moveToNext()) {

                    path = cursor.getString(Column_index);
                    File file = new File(path);
                    if (file.exists()) {
                        name = file.getName();
                        date = file.lastModified();

                        addMediaRecents(new Media(context, cursor.getPosition(), Media.VideoType, name, date, file));
                    }
                }
                cursor.close();
//imageList gets populated with paths to images by here
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    private long date=0;
    private String cameraPath;
    private void addMediaRecents(Media media){

        if(cameraPath==null){
            cameraPath= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath();
        }

        if(MainActivity.getRecentStatus()||media.getPath().contains(cameraPath)) {
            if (date == 0) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                date = new Date(cal.getTimeInMillis()).getTime();
            }

            long d = new Date(media.getDate()).getTime();


            if(Case == TodayCase){
                if (d > date) {
                    medias.add(media);
                }
            }
            else if(Case == Last7Case){
                if (d > date - (7 * 86400000)&&  d<date) {
                    medias.add(media);
                }
            }
            //else if(d > date-(30*86400000)){
            //    last30DaysPhotos.add(media);
            //}
        }
    }

    public List<Media> getMedias(){
        return medias;
    }


    public String getName(){
        String retorno = "";
        switch (Case){
            case 0:
                retorno = folder.getName();
                break;
            case 1:
                retorno = context.getString(R.string.Today);
                break;
            case 2:
                retorno = context.getString(R.string.Last7);
                break;
        }
        return retorno;
    }

    public String getPath(){
        return folder.getPath();
    }

    public void copyTo(String newFolder,List<Media> medias) {
        for (int i = 0; i < medias.size(); i++) {
            medias.get(i).moveTo(newFolder);
            remove(medias.get(i));
        }
        if(medias.size()==1){
            Toast.makeText(context, medias.size()+" "+context.getString(R.string.moveDoneMessage)+" "+new File(newFolder).getName(), Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(context, medias.size()+" "+context.getString(R.string.moveDonesMessage)+" "+new File(newFolder).getName(), Toast.LENGTH_SHORT).show();
        }
    }

    public int folderSize(){
        return size;
    }

    public void incremetFoldeSize(){
        size++;
    }

    public void sortMedia(){
        Collections.sort(medias);
    }

    private void remove(Media media){
        for(int i=0;i<medias.size();i++){
            if(medias.get(i).getPath().equals(media.getPath())){
                medias.remove(i);
                break;
            }
        }
    }


    public void scanFolder() {
        MediaScannerConnection.scanFile(context,
                new String[]{getPath()}, null, new MediaScannerConnection.MediaScannerConnectionClient() {
                    @Override
                    public void onMediaScannerConnected() {

                    }

                    @Override
                    public void onScanCompleted(String s, Uri uri) {
                    }
                });
    }

    public void scanFolder(final MainActivity mainActivity) {
        MediaScannerConnection.scanFile(context,
                new String[]{getPath()}, null, new MediaScannerConnection.MediaScannerConnectionClient() {
                    @Override
                    public void onMediaScannerConnected() {

                    }

                    @Override
                    public void onScanCompleted(String s, Uri uri) {
                        mainActivity.registObserver();
                    }
                });
    }

    public void scanFolder(final FolderActivity folderActivity) {
        MediaScannerConnection.scanFile(context,
                new String[]{getPath()}, null, new MediaScannerConnection.MediaScannerConnectionClient() {
                    @Override
                    public void onMediaScannerConnected() {

                    }

                    @Override
                    public void onScanCompleted(String s, Uri uri) {
                        folderActivity.registObserver();
                    }
                });
    }

    @Override
    public int compareTo(Folder f) {
        return getName().compareTo(f.getName());
    }
}
