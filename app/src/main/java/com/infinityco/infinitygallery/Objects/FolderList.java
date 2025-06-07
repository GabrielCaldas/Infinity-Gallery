package com.infinityco.infinitygallery.Objects;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import com.infinityco.infinitygallery.Activitys.MainActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;

public class FolderList {

    private List<Folder> folders = new ArrayList<>();
    private List<Media> toDayPhotos = new ArrayList<>();
    private List<Media> last7DaysPhotos = new ArrayList<>();

    private Context context;
    private String[] IgnoreFolder;

    public FolderList(Context context){
        this.context = context;
    }

    public void LoadMedia(Boolean loadRaw){
        try{
            IgnoreFolder = new Berreivement_values(context).getIgnoredFolders();
        }
        catch (Exception e){
            IgnoreFolder = null;
        }

        toDayPhotos = new ArrayList<>();
        last7DaysPhotos = new ArrayList<>();

        LoadMediaImages(loadRaw);
        LoadMediaVideos();
        Collections.sort(folders);
        Collections.sort(toDayPhotos);
        Collections.sort(last7DaysPhotos);
        for(int i=0;i<folders.size();i++){
            folders.get(i).sortMedia();
        }
    }

    private void LoadMediaImages(boolean loadRaw){
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
                    if(IgnoreFolder==null) {
                        path = cursor.getString(Column_index);
                        File file = new File(path);
                        if (file.exists()) {
                            name = file.getName();
                            date = file.lastModified();

                            if(loadRaw) {
                                addMedia(new Media(context, cursor.getPosition(), Media.ImageType, name, date, file));
                            }
                            else{
                                if(!rawTest(file.getPath())){
                                    addMedia(new Media(context, cursor.getPosition(), Media.ImageType, name, date, file));
                                }
                            }
                        }
                    }
                    else {
                        boolean isIgnored= false;
                        for(int i=0;i<IgnoreFolder.length;i++){
                            path = cursor.getString(Column_index);
                            if(path.contains(IgnoreFolder[i])) {
                                isIgnored = true;
                                break;
                            }
                            else {
                                isIgnored = false;
                            }
                        }

                        if(!isIgnored){
                            File file = new File(path);
                            if (file.exists()) {
                                name = file.getName();
                                date = file.lastModified();

                                if(loadRaw) {
                                    addMedia(new Media(context, cursor.getPosition(), Media.ImageType, name, date, file));
                                }
                                else{
                                    if(!rawTest(file.getPath())){
                                        addMedia(new Media(context, cursor.getPosition(), Media.ImageType, name, date, file));
                                    }
                                }
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
        String[] projection = { MediaStore.MediaColumns.DATA};

        sortOrder = MediaStore.Video.VideoColumns.DATE_ADDED;

        cursor = context.getContentResolver().query(uri, projection, null,null, sortOrder);



        try {
            if (null != cursor) {
                Column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);

                while (cursor.moveToNext()) {

                    if(IgnoreFolder==null) {
                        path = cursor.getString(Column_index);
                        File file = new File(path);
                        if (file.exists()) {
                            name = file.getName();
                            date = file.lastModified();

                            addMedia(new Media(context, cursor.getPosition(), Media.VideoType, name, date, file));
                        }
                    }
                    else {
                        boolean isIgnored = false;
                        for(int i=0;i<IgnoreFolder.length;i++){
                            path = cursor.getString(Column_index);
                            if(path.contains(IgnoreFolder[i])) {
                                isIgnored = true;
                                break;
                            }
                            else {
                                isIgnored = false;
                            }
                        }
                        if(!isIgnored){
                            File file = new File(path);
                            if (file.exists()) {
                                name = file.getName();
                                date = file.lastModified();

                                addMedia(new Media(context, cursor.getPosition(), Media.VideoType, name, date, file));
                            }
                        }
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
    private void addMedia(Media media){

        if(cameraPath==null){
            cameraPath= Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).getPath();
        }

        if(MainActivity.getRecentStatus()){//||media.getPath().contains(cameraPath)) {
            if (date == 0) {
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.HOUR_OF_DAY, 0);
                cal.set(Calendar.MINUTE, 0);
                cal.set(Calendar.SECOND, 0);
                cal.set(Calendar.MILLISECOND, 0);
                date = new Date(cal.getTimeInMillis()).getTime();
            }

            long d = new Date(media.getDate()).getTime();


            if (d > date) {
                toDayPhotos.add(media);
            } else if (d > date - (7 * 86400000)) {
                last7DaysPhotos.add(media);
            }
            //else if(d > date-(30*86400000)){
            //    last30DaysPhotos.add(media);
            //}
        }

        if(!folders.isEmpty()) {
            boolean found = false;
            for (int i = 0; i < folders.size(); i++) {
                if (folders.get(i).getPath().equals(media.getFolderPath())) {
                        if(false&&folders.get(i).getMedias().size()>4){
                            folders.get(i).incremetFoldeSize();
                        }
                        else {
                            folders.get(i).addMedia(media.getIndex(), media.getType(), media.getName(), media.getDate(), media.getFile());
                        }
                        found = true;
                        break;
                }

            }
            if (!found){
                Folder folder = new Folder(context,media);
                folders.add(folder);
            }
        }
        else {
            Folder folder = new Folder(context,media);
            folders.add(folder);
        }
    }
    public List<Folder> getFolders() {
        return folders;
    }

    public List<Media> getToDayPhotos(){
        return toDayPhotos;
    }

    public List<Media> getLast7DaysPhotos(){
        return last7DaysPhotos;
    }

    public List<Media> getUpDateToDayPhotos(){
        for(int i=0;i<toDayPhotos.size();i++){
            if(!toDayPhotos.get(i).getFile().exists()){
                toDayPhotos.remove(i);
            }
        }
        return toDayPhotos;
    }

    public List<Media> getUpDateLast7DaysPhotos(){
        for(int i=0;i<last7DaysPhotos.size();i++){
            if(!last7DaysPhotos.get(i).getFile().exists()){
                last7DaysPhotos.remove(i);
            }
        }
        return last7DaysPhotos;
    }

    public Folder getToDayfolder(){
        return  new Folder(context,Folder.TodayCase);
    }

    public Folder getLast7folder(){
        return new Folder(context,Folder.Last7Case);
    }

}
