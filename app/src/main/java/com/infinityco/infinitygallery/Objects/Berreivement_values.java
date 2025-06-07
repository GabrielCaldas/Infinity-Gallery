package com.infinityco.infinitygallery.Objects;




import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.infinityco.infinitygallery.R;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class Berreivement_values {
    private static final String TAG = "ManageFile";
    private Context context;


    public void setNewFolderDirectory(String newFolder){

        if(testChar(newFolder)){
            Toast.makeText(context, context.getString(R.string.NewFolderpathErro1Message), Toast.LENGTH_SHORT).show();
        }
        else {
            try {
                FileOutputStream out = context.openFileOutput("newFolderDirectory.txt", Context.MODE_PRIVATE);
                out.write(newFolder.getBytes());
                out.flush();
                out.close();

            } catch (Exception e) {

                Toast.makeText(context, context.getString(R.string.NewFolderpathErro2Message), Toast.LENGTH_SHORT).show();
                Log.e(TAG, e.toString());
            }
        }
    }
    public String getNewFolderDirectory(){
        try {
            File file = context.getFilesDir();
            File textfile = new File(file + "/newFolderDirectory.txt");

            FileInputStream input = context.openFileInput("newFolderDirectory.txt");
            byte[] buffer = new byte[(int) textfile.length()];

            input.read(buffer);
            String result = new String(buffer);
            return result;
        }
        catch (Exception e){
            return context.getString(R.string.Images)+" - Infinity "+context.getString(R.string.app_name);
        }
    }
    public String getEditedPhotoDirectory(){
        try {
            File file = context.getFilesDir();
            File textfile = new File(file + "/newFolderDirectory.txt");

            FileInputStream input = context.openFileInput("newFolderDirectory.txt");
            byte[] buffer = new byte[(int) textfile.length()];

            input.read(buffer);
            String result = new String(buffer)+"/"+context.getString(R.string.PhotoEditorPath);;
            return result;
        }
        catch (Exception e){
            return context.getString(R.string.Images)+" - Infinity "+context.getString(R.string.app_name)+"/"+context.getString(R.string.PhotoEditorPath);
        }
    }
    private boolean testChar(String newFolder){
        if(newFolder.contains("/")||newFolder.contains("?")||newFolder.contains("<")||newFolder.contains(">")||newFolder.contains("*")||newFolder.contains("|")||newFolder.contains(":")){
            return true;
        }
        else{
            return false;
        }
    }

    public boolean setGridStatus(Boolean status){
        String text = "false";
        if(status){
            text = "true";
        }
        try {
            FileOutputStream out = context.openFileOutput("GridStatus.txt", Context.MODE_PRIVATE);
            out.write(text.getBytes());
            out.flush();
            out.close();
            return true;

        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return false;
        }
    }
    public boolean getGridStatus() throws IOException{
        File file = context.getFilesDir();
        File textfile = new File(file + "/GridStatus.txt");

        FileInputStream input = context.openFileInput("GridStatus.txt");
        byte[] buffer = new byte[(int)textfile.length()];

        input.read(buffer);
        String result = new String(buffer);
        if(result.equals("false")){
            return false;
        }
        else return true;
    }

    public boolean setRecentStatus(Boolean status){
        String text = "false";
        if(status){
            text = "true";
        }
        try {
            FileOutputStream out = context.openFileOutput("recentStauts.txt", Context.MODE_PRIVATE);
            out.write(text.getBytes());
            out.flush();
            out.close();
            return true;

        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return false;
        }
    }
    public boolean getRecentStatus() {
        try {
            File file = context.getFilesDir();
            File textfile = new File(file + "/recentStauts.txt");

            FileInputStream input = context.openFileInput("recentStauts.txt");
            byte[] buffer = new byte[(int) textfile.length()];

            input.read(buffer);
            String result = new String(buffer);
            if (result.equals("false")) {
                return false;
            } else return true;
        }
        catch (Exception e){
            return false;
        }
    }

    public boolean setFullscreentatus(Boolean status){
        String text = "false";
        if(status){
            text = "true";
        }
        try {
            FileOutputStream out = context.openFileOutput("Fullscreentatus.txt", Context.MODE_PRIVATE);
            out.write(text.getBytes());
            out.flush();
            out.close();
            return true;

        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return false;
        }
    }
    public boolean getFullscreenStatus() {
        File file = context.getFilesDir();
        File textfile = new File(file + "/Fullscreentatus.txt");

        try {
            FileInputStream input = context.openFileInput("Fullscreentatus.txt");
            byte[] buffer = new byte[(int) textfile.length()];

            input.read(buffer);
            String result = new String(buffer);
            if (result.equals("false")) {
                return false;
            } else return true;
        }
        catch (Exception e){
            return true;
        }
    }

    public boolean setNewStyleStatus(Boolean status){
        String text = "false";
        if(status){
            text = "true";
        }
        try {
            FileOutputStream out = context.openFileOutput("newStyleStatus.txt", Context.MODE_PRIVATE);
            out.write(text.getBytes());
            out.flush();
            out.close();
            return true;

        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return false;
        }
    }
    public boolean getNewStyleStatus() throws FileNotFoundException, IOException{
        File file = context.getFilesDir();
        File textfile = new File(file + "/newStyleStatus.txt");

        FileInputStream input = context.openFileInput("newStyleStatus.txt");
        byte[] buffer = new byte[(int)textfile.length()];

        input.read(buffer);
        String result = new String(buffer);
        if(result.equals("false")){
            return false;
        }
        else return true;
    }

    public boolean setDarkThemeStatus(Boolean status){
        String text = "false";
        if(status){
            text = "true";
        }
        try {
            FileOutputStream out = context.openFileOutput("DarkThemeStatus.txt", Context.MODE_PRIVATE);
            out.write(text.getBytes());
            out.flush();
            out.close();
            return true;

        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return false;
        }
    }
    public boolean getDarkThemeStatus() throws FileNotFoundException, IOException{
        File file = context.getFilesDir();
        File textfile = new File(file + "/DarkThemeStatus.txt");

        FileInputStream input = context.openFileInput("DarkThemeStatus.txt");
        byte[] buffer = new byte[(int)textfile.length()];

        input.read(buffer);
        String result = new String(buffer);
        if(result.equals("false")){
            return false;
        }
        else return true;
    }

    public boolean setRawStatus(Boolean status){
        String text = "false";
        if(status){
            text = "true";
        }
        try {
            FileOutputStream out = context.openFileOutput("RawStatus.txt", Context.MODE_PRIVATE);
            out.write(text.getBytes());
            out.flush();
            out.close();
            return true;

        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return false;
        }
    }
    public boolean getRawStatus(){
        try {
            File file = context.getFilesDir();
            File textfile = new File(file + "/RawStatus.txt");

            FileInputStream input = context.openFileInput("RawStatus.txt");
            byte[] buffer = new byte[(int) textfile.length()];

            input.read(buffer);
            String result = new String(buffer);
            if (result.equals("false")) {
                return false;
            } else return true;
        }
        catch(Exception e){
            return  true;
        }
    }

    public boolean setHighQuallityStatus(int pos){

        try {
            FileOutputStream out = context.openFileOutput("HighQuallityStatus.txt", Context.MODE_PRIVATE);
            out.write((pos+"").getBytes());
            out.flush();
            out.close();
            return true;

        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return false;
        }
    }
    public int getHighQuallityStatus() {
        try {
            File file = context.getFilesDir();
            File textfile = new File(file + "/HighQuallityStatus.txt");

            FileInputStream input = context.openFileInput("HighQuallityStatus.txt");
            byte[] buffer = new byte[(int) textfile.length()];

            input.read(buffer);
            String result = new String(buffer);

            if(result.equals("0")){
                return 0;
            }
            else if(result.equals("1")){
                return 1;
            }
            else {
                return 2;
            }

        }
        catch (Exception e){
            return 0;
        }
    }

    private boolean setFirst(){

        try {
            FileOutputStream out = context.openFileOutput("InfinityGallery.config", Context.MODE_PRIVATE);
            out.write(context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName.getBytes());
            out.flush();
            out.close();
            return true;

        } catch (Exception e) {
            Log.e(TAG, e.toString());
            return false;
        }
    }
    public boolean isFirst() {
        try {
            File file = context.getFilesDir();
            File mainFile = new File(file + "/InfinityGallery.config");

            if(mainFile.exists()) {
                FileInputStream input = context.openFileInput("InfinityGallery.config");
                byte[] buffer = new byte[(int) mainFile.length()];

                input.read(buffer);
                String result = new String(buffer);
                if (result.equals(context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName)) {
                    return false;
                }
                else {
                    setFirst();
                    return true;
                }
            }
            else {
                setFirst();
                return true;
            }
        }
        catch (Exception e){
            return false;
        }
    }

    public Berreivement_values(Context context){
        this.context = context;
    }
    public void setIgnoredFolders(String IgnoreFolderPath){

        File ignoredPath = new File(IgnoreFolderPath);

        try {
            if (!new File(context.getFilesDir() + "/" + ignoredPath.getName()+ignoredPath.getParentFile().getName() + ".ig").exists()) {
                FileOutputStream out = context.openFileOutput(ignoredPath.getName()+ignoredPath.getParentFile().getName() + ".ig", Context.MODE_PRIVATE);
                out.write(IgnoreFolderPath.getBytes());
                out.flush();
                out.close();
                Toast.makeText(context, ignoredPath.getName()+" "+context.getString(R.string.hideFolderMessageDone), Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }
    public String[] getIgnoredFolders() throws FileNotFoundException, IOException {
        try{
            File[] IgnoreFolders = finder(context.getFilesDir().getPath());

            if(IgnoreFolders.length>0) {
                String[] ignored = new String[IgnoreFolders.length];

                for (int i = 0; i < ignored.length; i++) {


                    FileInputStream fin = new FileInputStream(IgnoreFolders[i]);
                    String ret = convertStreamToString(fin);
                    //Make sure you close all streams.
                    fin.close();
                    ignored[i] = ret.replace("\n", "");
                }
                return ignored;
            }
            else {
                return null;
            }
        }
        catch (Exception e){
            return null;
        }

    }
    public static String convertStreamToString(InputStream is) throws Exception {
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = reader.readLine()) != null) {
            sb.append(line).append("\n");
        }
        reader.close();
        return sb.toString();
    }
    private File[] finder( String dirName){
        File dir = new File(dirName);

        return dir.listFiles(new FilenameFilter() {
            public boolean accept(File dir, String filename)
            { return filename.endsWith(".ig"); }
        } );

    }
}
