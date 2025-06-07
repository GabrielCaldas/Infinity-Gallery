package com.infinityco.infinitygallery.Adapters;

import android.content.Context;
import android.net.Uri;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.infinityco.infinitygallery.Objects.Folder;
import com.infinityco.infinitygallery.Objects.Media;
import com.infinityco.infinitygallery.R;

import java.io.File;
import java.util.List;

public class rvAdapterFoldersMedias extends RecyclerView.Adapter<rvAdapterFoldersMedias.DataObjectHolder>{

    private List<Media> Medias;
    private Context context;
    private boolean bigger = false;
    private int size,folderSize=0;

    private static String LOG_TAG = "MyRecyclerViewAdapter";

    public static class DataObjectHolder extends RecyclerView.ViewHolder{

        ImageView media, videoTumb, rawTumb;
        RelativeLayout rlMore;
        TextView tvMore;


        public DataObjectHolder(final View itemView) {
            super(itemView);

            media = (ImageView) itemView.findViewById(R.id.ivFolderMedia);
            videoTumb = (ImageView) itemView.findViewById(R.id.ivVideoTumb);
            rawTumb = (ImageView) itemView.findViewById(R.id.ivRawTumb);
            rlMore = (RelativeLayout) itemView.findViewById(R.id.rlMore);
            tvMore = (TextView) itemView.findViewById(R.id.tvMoreCount);
        }

    }


    public rvAdapterFoldersMedias(Context context, List<Media> Medias, Folder folder, int size) {
        this.context = context;
        this.Medias = Medias;
        this.size=size;
        this.folderSize = folder.folderSize();
    }




    @Override
    public rvAdapterFoldersMedias.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;

        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.carditem_folder_media, parent, false);



        rvAdapterFoldersMedias.DataObjectHolder dataObjectHolder = new rvAdapterFoldersMedias.DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(rvAdapterFoldersMedias.DataObjectHolder holder, final int position) {


        if(bigger){
            if(position<size-1){
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
                holder.rlMore.setVisibility(RelativeLayout.INVISIBLE);
            }
            else {
                Uri imageUri = Uri.fromFile(new File(Medias.get(position).getPath()));

                if(size > 1) {
                    holder.rlMore.setVisibility(RelativeLayout.VISIBLE);
                    holder.tvMore.setText(context.getString(R.string.more) + " " + (folderSize - size));
                }
                Glide.with(context).load(imageUri).into(holder.media);
            }
        }
        else {
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
        }

    }

    @Override
    public int getItemCount() {
        if(Medias.size()>size){
            bigger = true;
            return size;
        }
        else {
            return Medias.size();
        }
    }

}

