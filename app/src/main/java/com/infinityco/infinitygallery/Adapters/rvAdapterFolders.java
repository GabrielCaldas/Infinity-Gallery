package com.infinityco.infinitygallery.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.infinityco.infinitygallery.Activitys.FolderActivity;
import com.infinityco.infinitygallery.Activitys.MainActivity;
import com.infinityco.infinitygallery.Activitys.RequestFileManagerActivity;
import com.infinityco.infinitygallery.Objects.Berreivement_values;
import com.infinityco.infinitygallery.Objects.CustomProgressDialog;
import com.infinityco.infinitygallery.Objects.FolderList;
import com.infinityco.infinitygallery.Objects.Media;
import com.infinityco.infinitygallery.R;

import java.io.File;
import java.util.List;

public class rvAdapterFolders  extends RecyclerView.Adapter<rvAdapterFolders.DataObjectHolder>{
    private FolderList folders;
    private Context context;
    private boolean newStyle=false,grid=false;
    private int size;
    private CardViewAdapter_historyFeed feed;
    private MainActivity mainActivity;

    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private Typeface tittle_type;


    public static class DataObjectHolder extends RecyclerView.ViewHolder  {
        TextView folderName,folderCount;
        ImageView ivBackground0,ivBackground1,ivBackground2;
        RecyclerView rvPhotos;
        Button btFolder;
        LinearLayout llFolder;

        public DataObjectHolder(View itemView, Typeface typeface,boolean newStyle) {
            super(itemView);

            if(newStyle){

                ivBackground0 = itemView.findViewById(R.id.ivFolderBackground0);
                ivBackground1 = itemView.findViewById(R.id.ivFolderBackground1);
                ivBackground2 = itemView.findViewById(R.id.ivFolderBackground2);
                folderName = itemView.findViewById(R.id.tvFolderName);
                folderCount = itemView.findViewById(R.id.tvFolderCount);
                btFolder = itemView.findViewById(R.id.btFolder);
            }
            else {
                folderName = (TextView) itemView.findViewById(R.id.tvFolderName);
                folderCount = (TextView) itemView.findViewById(R.id.tvFolderCount);
                rvPhotos = (RecyclerView) itemView.findViewById(R.id.rvFoldersPhotos);
                btFolder = (Button) itemView.findViewById(R.id.btFolder);
                llFolder = (LinearLayout) itemView.findViewById(R.id.llFolder);
            }
            //folderName.setTypeface(typeface);


        }


    }

    public rvAdapterFolders(Context context,MainActivity mainActivity,CardViewAdapter_historyFeed feed, FolderList folders,int size,boolean newStyle,boolean grid) {
        this.mainActivity = mainActivity;
        this.feed = feed;
        this.context = context;
        this.folders = folders;
        this.size = size;
        this.newStyle = newStyle;
        this.grid = false;

        //this.tittle_type = tittle_type;
    }

    public int getSize(){
        return folders.getFolders().size();
    }


    @Override
    public rvAdapterFolders.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if(newStyle){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.carditem_folder_new, parent, false);
        }
        else if(MainActivity.isDarkTheme()){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.carditem_folder_dark, parent, false);
        }
        else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.carditem_folder, parent, false);
        }

        return new rvAdapterFolders.DataObjectHolder(view,tittle_type,newStyle);
    }

    @Override
    public void onBindViewHolder(rvAdapterFolders.DataObjectHolder holder, final int position) {


        if(!newStyle){

            LinearLayoutManager horizontalLayoutManagaer = new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false);
            holder.rvPhotos.setLayoutManager(horizontalLayoutManagaer);


            holder.rvPhotos.setAdapter(new rvAdapterFoldersMedias(context,folders.getFolders().get(position).getMedias(),folders.getFolders().get(position),size));
            if(size==1){
                holder.llFolder.setGravity(Gravity.CENTER_HORIZONTAL);
            }

        }
        else {
            if(!grid&&folders.getFolders().get(position).getMedias().size()>1){

                holder.ivBackground0.setVisibility(View.GONE);
                holder.ivBackground1.setVisibility(View.VISIBLE);
                holder.ivBackground2.setVisibility(View.VISIBLE);

                Uri imageUri1 = Uri.fromFile(new File(folders.getFolders().get(position).getMedias().get(0).getPath()));
                Uri imageUri2 = Uri.fromFile(new File(folders.getFolders().get(position).getMedias().get(1).getPath()));


                Glide.with(context).load(imageUri1).into(holder.ivBackground1);
                Glide.with(context).load(imageUri2).into(holder.ivBackground2);

            }
            else {

                holder.ivBackground0.setVisibility(View.VISIBLE);
                holder.ivBackground1.setVisibility(View.GONE);
                holder.ivBackground2.setVisibility(View.GONE);

                Uri imageUri = Uri.fromFile(new File(folders.getFolders().get(position).getMedias().get(0).getPath()));

                Glide.with(context).load(imageUri).into(holder.ivBackground0);
            }
        }


        holder.folderName.setText(folders.getFolders().get(position).getName());

        if(folders.getFolders().get(position).getMedias().size()==1){
            holder.folderCount.setText(folders.getFolders().get(position).folderSize()+" "+context.getString(R.string.media)+" ");

        }
        else{
            holder.folderCount.setText(folders.getFolders().get(position).folderSize()+" "+context.getString(R.string.medias)+" ");
        }





        holder.btFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, FolderActivity.class).putExtra("Folder",folders.getFolders().get(position).getPath()));
            }
        });

        holder.btFolder.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                options(position);
                return false;
            }
        });

    }

    public void UpDateViews(FolderList folders){
        this.folders = folders;
        feed.UpDateViews(folders.getToDayPhotos(),folders.getLast7DaysPhotos());
        notifyDataSetChanged();
    }

    private AlertDialog alertDialogDelete;
    private CustomProgressDialog progress;
    private void exclude(final int pos){
        final AlertDialog.Builder RecDialog = new AlertDialog.Builder(context);

        //Typeface tittle_type = MainActivity.getTyperFace();

        TextView dialogTittle = new TextView(context);
        dialogTittle.setText(R.string.excludeTittle);
        //dialogTittle.setTypeface(tittle_type);
        dialogTittle.setTextColor(Color.WHITE);
        dialogTittle.setPadding(20,10,10,10);
        dialogTittle.setTextSize(25);
        dialogTittle.setBackgroundResource(R.drawable.datail_gradient);

        final TextView input = new TextView(context);
        input.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        input.setText(context.getString(R.string.excludeFolderMessage)+" "+folders.getFolders().get(pos).getName()+"?");
        //input.setTypeface(tittle_type);

        LinearLayout dialogLayout = new LinearLayout(context);
        dialogLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout editTextLayout = new LinearLayout(context);
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
                        progress = new CustomProgressDialog(context);
                        progress.setMessage(context.getString(R.string.deleting));
                        progress.show();
                        alertDialogDelete.dismiss();
                        alertDialog.dismiss();
                        mainActivity.unRegistObserver();

                        new Thread(new Runnable() {
                            @Override
                            public void run() {

                                for(int i=0;i<folders.getFolders().get(pos).getMedias().size();i++){
                                    if(!folders.getFolders().get(pos).getMedias().get(i).DeleteNoScan()){

                                        folders.getFolders().get(pos).scanFolder(mainActivity);
                                        final List<Media> todayPhotos = folders.getUpDateToDayPhotos();
                                        final List<Media> last7daysPhotos = folders.getUpDateLast7DaysPhotos();
                                        mainActivity.runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {

                                                feed.UpDateViews(todayPhotos,last7daysPhotos);
                                                notifyDataSetChanged();
                                                progress.dismiss();

                                            }
                                        });
                                        progress.dismiss();
                                        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.Q){

                                            context.startActivity(new Intent(context, RequestFileManagerActivity.class));

                                        }
                                        else{

                                            mainActivity.runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    Toast.makeText(context,context.getString(R.string.excludFail), Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                        return;

                                    }
                                }
                                folders.getFolders().get(pos).scanFolder(mainActivity);
                                folders.getFolders().remove(pos);
                                final List<Media> todayPhotos = folders.getUpDateToDayPhotos();
                                final List<Media> last7daysPhotos = folders.getUpDateLast7DaysPhotos();
                                mainActivity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {

                                        feed.UpDateViews(todayPhotos,last7daysPhotos);
                                        notifyDataSetChanged();
                                        progress.dismiss();

                                    }
                                });
                            }
                        }).start();
                    }
                });

        RecDialog.setNeutralButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialogDelete.dismiss();
                    }
                });


        alertDialogDelete = RecDialog.create();
        alertDialogDelete.getWindow().setTitleColor(Color.RED);
        alertDialogDelete.show();



        alertDialogDelete.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogDelete.dismiss();
            }
        });

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.BLACK);
        alertDialogDelete.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(Color.GRAY);
        alertDialogDelete.getButton(AlertDialog.BUTTON_NEUTRAL).setTransformationMethod(null);
        alertDialogDelete.getButton(AlertDialog.BUTTON_POSITIVE).setTransformationMethod(null);
    }

    private AlertDialog alertDialog;
    private void options(final int pos) {
        final AlertDialog.Builder RecDialog = new AlertDialog.Builder(context);

        //Typeface tittle_type = MainActivity.getTyperFace();

        TextView dialogTittle = new TextView(context);
        dialogTittle.setText(R.string.details);
        //dialogTittle.setTypeface(tittle_type);
        dialogTittle.setTextColor(Color.WHITE);
        dialogTittle.setPadding(20, 10, 10, 10);
        dialogTittle.setTextSize(25);
        dialogTittle.setBackgroundResource(R.drawable.datail_gradient);


        final TextView Directory = new TextView(context);
        Directory.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        Directory.setText(context.getString(R.string.detailsPath));
        //Directory.setTypeface(tittle_type);

        final TextView directory = new TextView(context);
        directory.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        directory.setText(folders.getFolders().get(pos).getPath());
        directory.setPadding(10, 0, 0, 0);
        //directory.setTypeface(tittle_type);


        final TextView Archive = new TextView(context);
        Archive.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        Archive.setText(R.string.archives);
        //Archive.setTypeface(tittle_type);

        final TextView archive = new TextView(context);
        archive.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        archive.setPadding(10, 0, 0, 0);
        int size = folders.getFolders().get(pos).folderSize();
        if (size == 1) {
            archive.setText(size + " " + context.getString(R.string.media));
        } else {
            archive.setText(size + " " + context.getString(R.string.medias));
        }
        //archive.setTypeface(tittle_type);


        final TextView Size = new TextView(context);
        Size.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        Size.setText(R.string.size);
        //Archive.setTypeface(tittle_type);


        final TextView SizeMb = new TextView(context);
        SizeMb.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        SizeMb.setText(" " + folderSize(new File(folders.getFolders().get(pos).getPath())) + "mb");
        //SizeMb.setTypeface(tittle_type);


        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Directory.setTextAppearance(R.style.TextAppearance_Compat_Notification_Title);
            Archive.setTextAppearance(R.style.TextAppearance_Compat_Notification_Title);
            Size.setTextAppearance(R.style.TextAppearance_Compat_Notification_Title);
        }

        LinearLayout dialogLayout = new LinearLayout(context);
        dialogLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout directoryLayout = new LinearLayout(context);
        directoryLayout.setOrientation(LinearLayout.HORIZONTAL);
        directoryLayout.addView(Directory);
        directoryLayout.addView(directory);

        LinearLayout archiveLayout = new LinearLayout(context);
        archiveLayout.setOrientation(LinearLayout.HORIZONTAL);
        archiveLayout.addView(Archive);
        archiveLayout.addView(archive);

        LinearLayout sizeLayout = new LinearLayout(context);
        sizeLayout.setOrientation(LinearLayout.HORIZONTAL);
        sizeLayout.addView(Size);
        sizeLayout.addView(SizeMb);

        LinearLayout bodyLayout = new LinearLayout(context);
        bodyLayout.setOrientation(LinearLayout.VERTICAL);
        bodyLayout.setPadding(30, 30, 30, 30);
        bodyLayout.addView(directoryLayout);
        bodyLayout.addView(archiveLayout);
        bodyLayout.addView(sizeLayout);

        dialogLayout.addView(dialogTittle);
        dialogLayout.addView(bodyLayout);

        RecDialog.setView(dialogLayout);


        RecDialog.setPositiveButton(R.string.excludeTittle,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        exclude(pos);
                    }
                });

        RecDialog.setNegativeButton(R.string.Ignore,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        ignore(pos);
                    }
                });

        RecDialog.setNeutralButton(R.string.close,
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


        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED);
        alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(Color.GRAY);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(context.getColor(R.color.colorDetailDark));
        }
        else {
            alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.GRAY);
        }

        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTransformationMethod(null);
        alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTransformationMethod(null);
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTransformationMethod(null);
    }
    public double folderSize(File directory) {
        double length = 0;
        for (File file : directory.listFiles()) {
            if (file.isFile())
                length += file.length();
            else
                length += folderSize(file);
        }
        return arredondar(length/(1000 * 1000));
    }

    private double arredondar(double valor) {
        double arredondado = valor;
        arredondado *= (Math.pow(10, 2));

        arredondado = Math.ceil(arredondado);

        arredondado /= (Math.pow(10, 2));
        return arredondado;
    }

    private File newFile;
    private void ignore(final int pos){
        final AlertDialog.Builder RecDialog = new AlertDialog.Builder(context);

        //Typeface tittle_type = MainActivity.getTyperFace();

        TextView dialogTittle = new TextView(context);
        dialogTittle.setText(R.string.Ignore_Tittle);
        //dialogTittle.setTypeface(tittle_type);
        dialogTittle.setTextColor(Color.WHITE);
        dialogTittle.setPadding(20,10,10,10);
        dialogTittle.setTextSize(25);
        dialogTittle.setBackgroundResource(R.drawable.datail_gradient);

        final TextView input = new TextView(context);
        input.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        input.setText(context.getString(R.string.Ignore_Message));
        //input.setTypeface(tittle_type);


        Button btUp = new Button(context);
        btUp.setLayoutParams(new LinearLayout.LayoutParams(105,70));
        btUp.setBackgroundResource(R.drawable.bt_upfolder);

        newFile = new File(folders.getFolders().get(pos).getPath());

        final TextView folder = new TextView(context);
        folder.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        folder.setText(newFile.getName());
        folder.setSingleLine(true);
        folder.setTextSize(17);

        folder.setGravity(Gravity.BOTTOM);
        folder.setEllipsize(TextUtils.TruncateAt.START);
        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            folder.setTextColor(context.getColor(R.color.colorDetailDark));
        }

        final TextView folderPath = new TextView(context);
        LinearLayout.LayoutParams layout3 =new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,LinearLayout.LayoutParams.WRAP_CONTENT);
        layout3.setMargins(10,0,0,0);
        folderPath.setLayoutParams(layout3);
        folderPath.setMaxWidth(300);
        folderPath.setText(newFile.getParentFile().getPath()+"/");
        folderPath.setTextSize(10);
        folderPath.setSingleLine(true);
        folderPath.setGravity(Gravity.BOTTOM);
        folderPath.setEllipsize(TextUtils.TruncateAt.START);
        folderPath.setTextColor(Color.GRAY);


        final Button OriginalFolder = new Button(context);
        LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layout.setMargins(5,0,0,5);
        OriginalFolder.setLayoutParams(layout);
        OriginalFolder.setText(R.string.Orginal);
        OriginalFolder.setTextSize(12);
        OriginalFolder.setVisibility(View.INVISIBLE);
        OriginalFolder.setGravity(Gravity.TOP);
        OriginalFolder.setBackgroundResource(R.drawable.translucido);
        OriginalFolder.setTextColor(Color.GRAY);
        OriginalFolder.setTransformationMethod(null);
        OriginalFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newFile = new File(folders.getFolders().get(pos).getPath());
                folderPath.setText(newFile.getParentFile().getPath()+"/");
                folder.setText(newFile.getName());
                OriginalFolder.setVisibility(View.INVISIBLE);
            }
        });


        btUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(newFile.getParentFile().getParentFile()!=null) {
                    newFile = newFile.getParentFile();
                    folderPath.setText(newFile.getParentFile().getPath() + "/");
                    if(folderPath.getText().toString().equals("//")){
                        folderPath.setText("/");
                    }
                    folder.setText(newFile.getName());
                    OriginalFolder.setVisibility(View.VISIBLE);
                }
                else {
                    Toast.makeText(context, context.getString(R.string.hideFolderDontGetTheParentMessage), Toast.LENGTH_SHORT).show();
                }
            }
        });


        LinearLayout buttonsLayout = new LinearLayout(context);
        LinearLayout.LayoutParams layout2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        layout2.setMargins(0,15,0,0);
        buttonsLayout.setGravity((Gravity.CENTER_VERTICAL | Gravity.START));
        buttonsLayout.setBackgroundResource(R.drawable.box_layout_collor);
        buttonsLayout.setLayoutParams(layout2);
        buttonsLayout.setOrientation(LinearLayout.HORIZONTAL);
        buttonsLayout.addView(btUp);

        LinearLayout pathLayout = new LinearLayout(context);
        pathLayout.setGravity((Gravity.BOTTOM | Gravity.START));
        pathLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        pathLayout.setOrientation(LinearLayout.HORIZONTAL);
        pathLayout.setPadding(5,5,5,5);
        pathLayout.addView(folderPath);
        pathLayout.addView(folder);

        buttonsLayout.addView(pathLayout);


        //input.setTypeface(tittle_type);

        LinearLayout dialogLayout = new LinearLayout(context);
        dialogLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout editTextLayout = new LinearLayout(context);
        editTextLayout.setOrientation(LinearLayout.VERTICAL);
        editTextLayout.setGravity(Gravity.END);
        editTextLayout.setPadding(30,30,30,30);
        editTextLayout.addView(input);
        editTextLayout.addView(buttonsLayout);
        editTextLayout.addView(OriginalFolder);


        dialogLayout.addView(dialogTittle);
        dialogLayout.addView(editTextLayout);

        RecDialog.setView(dialogLayout);


        RecDialog.setPositiveButton(R.string.Ignore,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Berreivement_values bv = new Berreivement_values(context);

                        bv.setIgnoredFolders(newFile.getPath());

                        mainActivity.UpdateView();

                        alertDialogDelete.dismiss();
                        alertDialog.dismiss();
                    }
                });

        RecDialog.setNeutralButton(R.string.cancel,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialogDelete.dismiss();
                    }
                });


        alertDialogDelete = RecDialog.create();
        alertDialogDelete.show();



        alertDialogDelete.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialogDelete.dismiss();
            }
        });


        alertDialogDelete.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.RED);
        alertDialogDelete.getButton(AlertDialog.BUTTON_NEUTRAL).setTextColor(Color.GRAY);
        alertDialogDelete.getButton(AlertDialog.BUTTON_NEUTRAL).setTransformationMethod(null);
        alertDialogDelete.getButton(AlertDialog.BUTTON_POSITIVE).setTransformationMethod(null);
    }

    @Override
    public int getItemCount() {
        return folders.getFolders().size();
    }


}
