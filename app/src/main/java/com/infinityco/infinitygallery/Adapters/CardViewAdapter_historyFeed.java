package com.infinityco.infinitygallery.Adapters;

import android.app.Activity;
import android.content.Context;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.infinityco.infinitygallery.Activitys.MainActivity;
import com.infinityco.infinitygallery.Objects.Media;
import com.infinityco.infinitygallery.R;

import java.util.ArrayList;
import java.util.List;


public class CardViewAdapter_historyFeed extends RecyclerView.Adapter<CardViewAdapter_historyFeed.DataObjectHolder>{
    private Context context;
    private Activity activity;
    private List<Media> toDayPhotos = new ArrayList<>();
    private List<Media> last7DaysPhotos = new ArrayList<>();
    private CardViewAdapter_historyFeedMedias today,last7;
    private boolean readToday = false,readLast7 = false;



    public class DataObjectHolder extends RecyclerView.ViewHolder{
        TextView Tittle;
        RecyclerView List;
        RelativeLayout noRecents,Recents;

        public DataObjectHolder(View itemView) {
            super(itemView);

            Recents = itemView.findViewById(R.id.rvRecents);
            noRecents = itemView.findViewById(R.id.rvNoRecents);
            Tittle = (TextView) itemView.findViewById(R.id.tvDateTittle);
            List = (RecyclerView) itemView.findViewById(R.id.recyclerview);

        }
    }







    public CardViewAdapter_historyFeed(Activity activity, Context context, List<Media> toDayPhotos, List<Media> last7DaysPhotos) {
        this.activity = activity;
        this.context = context;
        this.toDayPhotos = toDayPhotos;
        this.last7DaysPhotos = last7DaysPhotos;
        this.today = new CardViewAdapter_historyFeedMedias(context,toDayPhotos,CardViewAdapter_historyFeedMedias.FeedToday);
        this.last7 = new CardViewAdapter_historyFeedMedias(context,last7DaysPhotos,CardViewAdapter_historyFeedMedias.FeedLast7);

    }

    @Override
    public CardViewAdapter_historyFeed.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v;
        if(!MainActivity.isDarkTheme()){
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.carditem_history_feed, parent, false);
        }
        else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.carditem_history_feed_dark, parent, false);
        }

        CardViewAdapter_historyFeed.DataObjectHolder dataObjectHolder = new CardViewAdapter_historyFeed.DataObjectHolder(v);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(final CardViewAdapter_historyFeed.DataObjectHolder holder, final int position) {

        if(toDayPhotos.isEmpty()&&last7DaysPhotos.isEmpty()){
            holder.noRecents.setVisibility(View.VISIBLE);
            holder.Recents.setVisibility(View.GONE);
        }
        else {

            holder.Recents.setVisibility(View.VISIBLE);
            holder.noRecents.setVisibility(View.GONE);
            holder.List.setHasFixedSize(true);

            holder.List.setLayoutManager((new GridLayoutManager(context, getNumRoons())));

            if (readToday || toDayPhotos.isEmpty()) {
                readLast7 = true;
                holder.Tittle.setText(context.getString(R.string.Last7));
                holder.List.setAdapter(last7);

            } else {
                readToday = true;
                holder.Tittle.setText(context.getString(R.string.Today));
                holder.List.setAdapter(today);
            }
        }

    }

    private int getNumRoons(){

        DisplayMetrics displayMetrics = activity.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;

        return ((int) dpWidth/120);
    }


    public void UpDateViews(List<Media> toDayPhotos, List<Media> last7DaysPhotos) {

        this.toDayPhotos = toDayPhotos;
        this.last7DaysPhotos = last7DaysPhotos;
        readToday = false;
        notifyDataSetChanged();
        //this.today.notifyDataSetChanged(toDayPhotos);
        //this.last7.notifyDataSetChanged(last7DaysPhotos);

    }


    @Override
    public int getItemCount() {

        int size = 0;
        if(!toDayPhotos.isEmpty()){
            size = size+1;
        }
        if(!last7DaysPhotos.isEmpty()){
            size = size+1;
        }
        if(size==0){
            return  1;
        }
        else {
            return size;
        }
    }

}