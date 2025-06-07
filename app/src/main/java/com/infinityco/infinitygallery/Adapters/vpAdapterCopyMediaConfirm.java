package com.infinityco.infinitygallery.Adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.infinityco.infinitygallery.Objects.Folder;
import com.infinityco.infinitygallery.Objects.Media;
import com.infinityco.infinitygallery.R;

import java.io.File;
import java.util.List;

/**
 * Created by gabrielcaldas on 06/10/16.
 */

public class vpAdapterCopyMediaConfirm extends PagerAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;


    private AlertDialog fatherDialog;
    private List<Media> medias,allmedias;
    private Folder folder;
    private rvAdapterMedias adp;
    private String path;
    public vpAdapterCopyMediaConfirm(Context context, List<Media> medias, Folder folder, rvAdapterMedias adapter, String path, List<Media> allmedias) {
        mContext = context;
        this.medias = medias;
        this.allmedias = allmedias;
        this.folder = folder;
        this.adp = adapter;
        this.path = path;
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
        return view == (object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = mLayoutInflater.inflate(R.layout.fragment_copy_media_confirm, container, false);

        TextView message = view.findViewById(R.id.tvConfirmMessage);
        Button cancel = view.findViewById(R.id.btCancel);
        Button newFolder = view.findViewById(R.id.btNewFolder);

        message.setText(mContext.getString(R.string.moveConfirmeMessage)+" "+new File(path).getName());

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fatherDialog.dismiss();
            }
        });

        newFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog progressDialog = new ProgressDialog(mContext,R.style.AppCompatAlertDialogStyle);
                progressDialog.setTitle(mContext.getString(R.string.MovingDialogTittle));
                progressDialog.setMessage(mContext.getString(R.string.MovingDialogMessage));
                progressDialog.show();


                folder.copyTo(path, medias);
                adp.notifyDataSetChanged(allmedias);
                progressDialog.dismiss();
                fatherDialog.dismiss();



            }
        });


        container.addView(view);

        return view;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}