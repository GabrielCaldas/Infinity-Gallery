package com.infinityco.infinitygallery.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Build;

import androidx.viewpager.widget.PagerAdapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.bumptech.glide.Glide;
import com.infinityco.infinitygallery.Activitys.VideoPlayer;
import com.infinityco.infinitygallery.Helpers.PhotoView;
import com.infinityco.infinitygallery.Objects.Media;
import com.infinityco.infinitygallery.R;
import com.squareup.picasso.Picasso;


import java.util.List;

/**
 * Created by gabrielcaldas on 06/10/16.
 */

public class vpAdapterGallery extends PagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;
    Button btPlayVideo;
    ImageView ivVideo;
    private List<Media> medias;
    RelativeLayout buttonsLayout;
    private Boolean imersive;
    private Activity activity;
    boolean zoonIn = false;
    int highQuallity = 0;

    private PhotoView image;
    public vpAdapterGallery(Context context, List<Media> medias,RelativeLayout buttonsLayout,boolean imersive,Activity activity,int highQuallityStatus) {
        mContext = context;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.medias = medias;
        this.buttonsLayout = buttonsLayout;
        this.activity = activity;
        this.imersive = imersive;
        this.highQuallity = highQuallityStatus;
    }

    @Override
    public int getCount() {
        return medias.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View view;

        view = mLayoutInflater.inflate(R.layout.item_gallery_image, container, false);

        image = (PhotoView) view.findViewById(R.id.ivImage);
        ivVideo = (ImageView) view.findViewById(R.id.ivVideo);
        btPlayVideo = (Button) view.findViewById(R.id.btPlayVideo);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setButtonsVisibility();
            }
        });
        ivVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                setButtonsVisibility();
            }
        });

        btPlayVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });


        if(medias.get(position).getType()==Media.ImageType) {
            btPlayVideo.setVisibility(View.GONE);
            ivVideo.setVisibility(View.GONE);
            image.setVisibility(View.VISIBLE);

            if(highQuallity==2){
                Picasso.with(mContext).load("file://" + medias.get(position).getPath()).into(image);
            }
            else if(highQuallity == 1){
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Picasso.with(mContext).load("file://" + medias.get(position).getPath()).config(Bitmap.Config.HARDWARE).into(image);
                } else {
                    Picasso.with(mContext).load("file://" + medias.get(position).getPath()).config(Bitmap.Config.RGB_565).into(image);
                }
            }
            else {
                if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    Picasso.with(mContext).load("file://" + medias.get(position).getPath()).config(Bitmap.Config.HARDWARE).into(image);
                } else {
                    Glide.with(mContext).load("file://" + medias.get(position).getPath()).into(image);
                }
            }




        }
        else {
            btPlayVideo.setVisibility(View.VISIBLE);
            ivVideo.setVisibility(View.VISIBLE);
            image.setVisibility(View.GONE);
            Glide.with(mContext).load(medias.get(position).getPath()).into(ivVideo);

            btPlayVideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mContext.startActivity(new Intent(mContext, VideoPlayer.class).putExtra("video_path",medias.get(position).getPath()).setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION));
                }
            });
        }

        container.addView(view);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((RelativeLayout) object);
    }

    private void setButtonsVisibility(){
        if(buttonsLayout.getVisibility()== View.VISIBLE){
            buttonsLayout.setVisibility(View.INVISIBLE);
            if(imersive) {
                activity.getWindow().getDecorView().setSystemUiVisibility(
                        View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            }
        }
        else {
            buttonsLayout.setVisibility(View.VISIBLE);
                if(imersive) {
                    activity.getWindow().getDecorView().setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                }
        }
    }
}