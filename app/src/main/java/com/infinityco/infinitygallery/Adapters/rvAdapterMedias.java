package com.infinityco.infinitygallery.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;

import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.infinityco.infinitygallery.Activitys.FolderActivity;
import com.infinityco.infinitygallery.Activitys.Gallery;
import com.infinityco.infinitygallery.Objects.Berreivement_values;
import com.infinityco.infinitygallery.Objects.CustomProgressDialog;
import com.infinityco.infinitygallery.Objects.Folder;
import com.infinityco.infinitygallery.Objects.FolderList;
import com.infinityco.infinitygallery.Objects.Media;
import com.infinityco.infinitygallery.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class rvAdapterMedias extends RecyclerView.Adapter<rvAdapterMedias.DataObjectHolder>{

    private List<Media> Medias;
    private Folder folder;
    private List<Media> MediasSelected,notSelectedMedias;
    private FolderActivity folderActivity;
    private Context context;
    private RelativeLayout rlSelections;
    private TextView tvSelectionsCount;
    private int Case;


    private static String LOG_TAG = "MyRecyclerViewAdapter";

    public static class DataObjectHolder extends RecyclerView.ViewHolder{

        ImageView media, videoTumb,rawTumb;
        RelativeLayout rlSelect;
        Button btMedia;


        public DataObjectHolder(final View itemView) {
            super(itemView);

            media = (ImageView) itemView.findViewById(R.id.ivFolderMedia);
            videoTumb = (ImageView) itemView.findViewById(R.id.ivVideoTumb);
            rawTumb = (ImageView) itemView.findViewById(R.id.ivRawTumb);
            rlSelect = (RelativeLayout) itemView.findViewById(R.id.rlMediaSelected);
            btMedia = (Button) itemView.findViewById(R.id.btMedia);
        }

    }


    public rvAdapterMedias(FolderActivity activity,Context context, List<Media> Medias,RelativeLayout rlSelections,TextView tvSelections,Folder folder,int Case) {
        this.Case = Case;
        this.context = context;
        this.folderActivity = activity;
        this.folder = folder;
        this.Medias = Medias;
        this.MediasSelected = new ArrayList<>();
        this.tvSelectionsCount = tvSelections;
        this.rlSelections = rlSelections;
    }





    @Override
    public rvAdapterMedias.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.carditem_media, parent, false);

        rvAdapterMedias.DataObjectHolder dataObjectHolder = new rvAdapterMedias.DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final rvAdapterMedias.DataObjectHolder holder, final int position) {


        if(Medias.get(position).getType()== Media.VideoType){
            holder.videoTumb.setVisibility(View.VISIBLE);
        }
        else {
            holder.videoTumb.setVisibility(View.INVISIBLE);
        }

        if(Medias.get(position).isRaw()){
            holder.rawTumb.setVisibility(View.VISIBLE);
        }
        else {
            holder.rawTumb.setVisibility(View.INVISIBLE);
        }

        Uri imageUri = Uri.fromFile(new File(Medias.get(position).getPath()));

        Glide.with(context).load(imageUri).into(holder.media);

        if(!MediasSelected.isEmpty()) {
            for (int i = 0; i < MediasSelected.size(); i++) {
                if (MediasSelected.get(i).getPath().equals(Medias.get(position).getPath())) {
                    holder.rlSelect.setVisibility(View.VISIBLE);
                    break;
                } else {
                    holder.rlSelect.setVisibility(View.INVISIBLE);
                }
            }
        }
        else {
            holder.rlSelect.setVisibility(View.INVISIBLE);
        }

        holder.btMedia.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                select(Medias.get(position), holder);
                return true;
            }
        });

        holder.btMedia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!MediasSelected.isEmpty()){
                    select(Medias.get(position),holder);
                }
                else {
                    if(Case == Folder.defaltCase) {

                        context.startActivity(new Intent(context, Gallery.class).putExtra("Folder",Medias.get(position).getFolderPath()).putExtra("Pos",position));
                    }
                    else {
                        context.startActivity(new Intent(context, Gallery.class).putExtra("Folder","/////////////").putExtra("Feed",Case).putExtra("Pos",position));
                    }

                }
            }
        });
    }

    private void select(Media media,DataObjectHolder holder){
        Boolean found = false;
        for(int i=0;i<MediasSelected.size();i++){
            if(MediasSelected.get(i).getPath().equals(media.getPath())){
                holder.rlSelect.setVisibility(View.INVISIBLE);
                MediasSelected.remove(i);
                found = true;
                break;
            }
        }
        if (!found){
            holder.rlSelect.setVisibility(View.VISIBLE);
            MediasSelected.add(media);
        }

        if(!MediasSelected.isEmpty()){
            selectionsCountUpdate(MediasSelected.size());
            setSelectionLayoutVisibility(View.VISIBLE);
        }
        else {
            if (rlSelections.getVisibility() == View.VISIBLE){
                setSelectionLayoutVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return Medias.size();

    }

    public List<Media> getMediasSelected() {
        return MediasSelected;
    }

    public void releaseSelections(){
        MediasSelected = new ArrayList<>();
        selectionsCountUpdate(MediasSelected.size());
        setSelectionLayoutVisibility(View.INVISIBLE);
        notifyDataSetChanged();
    }

    public void selectAll(){

        if(MediasSelected.size()==Medias.size()){
            releaseSelections();
        }
        else {
            MediasSelected = new ArrayList<>();
            MediasSelected.addAll(Medias);
            if (!MediasSelected.isEmpty()) {
                selectionsCountUpdate(MediasSelected.size());
                setSelectionLayoutVisibility(View.VISIBLE);
            } else {
                if (rlSelections.getVisibility() == View.VISIBLE) {
                    setSelectionLayoutVisibility(View.GONE);
                }
            }
            notifyDataSetChanged();
        }
    }

    public void copy(){

        copyDialogSelectFolder();
    }

    public void notifyDataSetChanged(List<Media> medias){

        Medias = new ArrayList<>();
        Medias.addAll(medias);
        notifyDataSetChanged();
    }

     public interface DeleteMediasListenner{
        public void onFinish();
        public void onFailDelete();
    }
    private CustomProgressDialog progress;
    public void mediaDelete(final DeleteMediasListenner deleteMediasListenner){

        progress = new CustomProgressDialog(context);
        progress.setMessage(context.getString(R.string.deleting));
        progress.show();
        folderActivity.unRegistObserver();

        new Thread(new Runnable() {
            @Override
            public void run() {
                for(int i=0;i<Medias.size();i++){

                    for(int j=0;j<MediasSelected.size();j++){
                        if(MediasSelected.get(j).getPath().equals(Medias.get(i).getPath())){
                            if(!Medias.get(i).DeleteNoScan()){

                                deleteMediasListenner.onFailDelete();
                                progress.dismiss();
                                return;
                            }

                            MediasSelected.remove(j);
                            Medias.remove(i);
                        }
                    }

                }
                folderActivity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        folder.scanFolder(folderActivity);
                        progress.dismiss();
                        notifyDataSetChanged();
                        MediasSelected.clear();
                        setSelectionLayoutVisibility(View.INVISIBLE);
                        deleteMediasListenner.onFinish();

                    }
                });
            }
        }).start();

    }

    private void selectionsCountUpdate(int selectedCount){
        if(selectedCount==1){
            tvSelectionsCount.setText(selectedCount+" "+context.getString(R.string.selection));
        }
        else {
            tvSelectionsCount.setText(selectedCount+" "+context.getString(R.string.selections));
        }
    }
    private void setSelectionLayoutVisibility(int visibility){
        rlSelections.setVisibility(visibility);
    }

    AlertDialog alertDialog;
    private void copyDialogSelectFolder(){
        final AlertDialog.Builder RecDialog = new AlertDialog.Builder(context);

        final ViewPager viewPager = new ViewPager(context);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        viewPager.setLayoutParams(params);

        notSelectedMedias = new ArrayList<>();
        for(int i=0;i<Medias.size();i++){
            boolean found = false;
            for(int j=0; j< MediasSelected.size();j++){
                if(MediasSelected.get(j).getPath().equals(Medias.get(i).getPath())){
                    found = true;
                    break;
                }
            }
            if(!found){
                notSelectedMedias.add(Medias.get(i));
            }
        }
        FolderList folderList = new FolderList(context);
        folderList.LoadMedia(new Berreivement_values(context).getRawStatus());
        vpAdapterCopyMedia vpAdapterCopyMedia = new vpAdapterCopyMedia(folderActivity,context, folderList,MediasSelected,folder,this,notSelectedMedias);
        viewPager.setAdapter(vpAdapterCopyMedia);
        RecDialog.setView(viewPager);
        alertDialog = RecDialog.create();

        vpAdapterCopyMedia.passFatherDialog(alertDialog);

        alertDialog.getWindow().setBackgroundDrawable(null);
        alertDialog.getWindow().setLayout(ViewPager.LayoutParams.MATCH_PARENT,ViewPager.LayoutParams.WRAP_CONTENT);
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();


        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                setSelectionLayoutVisibility(View.INVISIBLE);
                releaseSelections();
            }
        });
    }
}

