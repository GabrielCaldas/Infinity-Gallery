package com.infinityco.infinitygallery.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.infinityco.infinitygallery.R;

/**
 * Created by gabrielcaldas on 06/10/16.
 */

public class vpAdapterOptions extends PagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;
    Activity activity;

    private AlertDialog fatherDialog;
    private Button cancel,setas,edit;
    private Uri photoUri;

    public vpAdapterOptions(Context context,Activity activity, Uri photoUri) {
        this.mContext = context;
        this.activity = activity;
        this.photoUri = photoUri;

        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    public void passFatherDialog(AlertDialog alertDialog){
        this.fatherDialog = alertDialog;
    }

    @Override
    public int getCount() {
        return 1;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((RelativeLayout) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mLayoutInflater.inflate(R.layout.fragment_options, container, false);

        cancel = (Button) view.findViewById(R.id.btCancel);
        setas = (Button) view.findViewById(R.id.btSetAs);
        edit = (Button) view.findViewById(R.id.btEdit);


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fatherDialog.dismiss();
            }
        });

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                edit();
                fatherDialog.dismiss();
            }
        });

        setas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fatherDialog.dismiss();
                setAs();
            }
        });


        container.addView(view);

        return view;
    }

    private void edit(){
        Intent editIntent = new Intent(Intent.ACTION_EDIT);
        editIntent.setDataAndType(photoUri, "image/*");
        editIntent.putExtra("mimeType", "image/*");
        editIntent.putExtra("com.infinityco.infinitygallery", mContext.getPackageName());
        editIntent.setFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        activity.startActivityForResult(Intent.createChooser(editIntent, null),1);
    }
    private void setAs(){
        Intent intent = new Intent(Intent.ACTION_ATTACH_DATA);
        intent.setDataAndType(photoUri, "image/*");
        intent.putExtra("mimeType", "image/*");
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        activity.startActivityForResult(Intent.createChooser(intent, mContext.getString(R.string.Set_as)), 200);
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}