package com.infinityco.infinitygallery.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.infinityco.infinitygallery.Activitys.FolderActivity;
import com.infinityco.infinitygallery.Activitys.Gallery;
import com.infinityco.infinitygallery.R;
import com.infinityco.infinitygallery.Objects.Media;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class CardViewAdapter_historyFeedMedias extends RecyclerView.Adapter<CardViewAdapter_historyFeedMedias.DataObjectHolder>{

    private List<Media> Medias;
    private Context context;
    private int FeedStatus,maxFeedSize=9;
    private boolean tooLong = false;

    public static int FeedToday=0,FeedLast7=1,FeedLast30=2;
    private static String LOG_TAG = "MyRecyclerViewAdapter";

    public static class DataObjectHolder extends RecyclerView.ViewHolder{

        RelativeLayout rlMore;
        ImageView media, videoTumb;
        Button btMedia;
        TextView tvMoreCount;


        public DataObjectHolder(final View itemView) {
            super(itemView);

            media =  itemView.findViewById(R.id.ivFolderMedia);
            videoTumb =  itemView.findViewById(R.id.ivVideoTumb);
            btMedia =  itemView.findViewById(R.id.btMedia);
            rlMore = itemView.findViewById(R.id.rlMore);
            tvMoreCount = itemView.findViewById(R.id.tvMoreCount);
        }

    }



    public CardViewAdapter_historyFeedMedias(Context context, List<Media> Medias, int feedStatus) {
        this.context = context;
        this.Medias = Medias;
        this.FeedStatus = feedStatus;
    }




    @Override
    public CardViewAdapter_historyFeedMedias.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.carditem_media_history_feed, parent, false);

        return new CardViewAdapter_historyFeedMedias.DataObjectHolder(view);
    }

    @Override
    public void onBindViewHolder(final CardViewAdapter_historyFeedMedias.DataObjectHolder holder, final int position) {


        if(Medias.get(position).getType()== Media.VideoType){
            holder.videoTumb.setVisibility(View.VISIBLE);
        }
        else {
            holder.videoTumb.setVisibility(View.INVISIBLE);
        }

        if(tooLong&&position == maxFeedSize-1){
            holder.rlMore.setVisibility(View.VISIBLE);
            holder.tvMoreCount.setText((Medias.size() - maxFeedSize )+" "+context.getString(R.string.more));
        }
        else {
            holder.rlMore.setVisibility(View.GONE);
        }

        Uri imageUri = Uri.fromFile(new File(Medias.get(position).getPath()));

        Glide.with(context).load(imageUri).into(holder.media);

        holder.btMedia.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(tooLong){
                        if(position==maxFeedSize-1){
                            context.startActivity(new Intent(context, FolderActivity.class).putExtra("Folder","/////////////").putExtra("Feed",FeedStatus).putExtra("Pos",position));
                        }
                        else {
                            context.startActivity(new Intent(context, Gallery.class).putExtra("Folder","/////////////").putExtra("Feed",FeedStatus).putExtra("Pos",position));
                        }
                    }
                    else {
                        context.startActivity(new Intent(context, Gallery.class).putExtra("Folder","/////////////").putExtra("Feed",FeedStatus).putExtra("Pos",position)); }
                    }
            });


    }


    @Override
    public int getItemCount() {
        if(Medias.size() > maxFeedSize){
            tooLong = true;
            return maxFeedSize;
        }
        else {
            return Medias.size();
        }

    }


    public void notifyDataSetChanged(List<Media> medias){

        Medias = new ArrayList<>();
        Medias.addAll(medias);
        notifyDataSetChanged();
    }

}

