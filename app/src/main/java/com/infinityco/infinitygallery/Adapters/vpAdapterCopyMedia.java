package com.infinityco.infinitygallery.Adapters;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Environment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.InputFilter;
import android.text.Spanned;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.infinityco.infinitygallery.Objects.Berreivement_values;
import com.infinityco.infinitygallery.Objects.Folder;
import com.infinityco.infinitygallery.Objects.FolderList;
import com.infinityco.infinitygallery.Objects.Media;
import com.infinityco.infinitygallery.R;

import java.util.List;

/**
 * Created by gabrielcaldas on 06/10/16.
 */

public class vpAdapterCopyMedia extends PagerAdapter {

    Activity activity;
    Context mContext;
    LayoutInflater mLayoutInflater;


    private AlertDialog fatherDialog;
    private RecyclerView recyclerview;
    private FolderList folderList;
    private Button cancel, newFolder;
    private List<Media> medias,allmedias;
    private Folder folder;
    private rvAdapterMedias adp;

    public vpAdapterCopyMedia(Activity activity,Context context, FolderList folderList, List<Media> medias, Folder folder, rvAdapterMedias adapter,List<Media> allmedias) {
        mContext = context;
        this.activity = activity;
        this.medias = medias;
        this.folder = folder;
        this.adp = adapter;
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.folderList = folderList;
        this.allmedias = allmedias;
    }

    public void passFatherDialog(AlertDialog alertDialog) {
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
        View view = mLayoutInflater.inflate(R.layout.fragment_copy_media, container, false);

        recyclerview = (RecyclerView) view.findViewById(R.id.recyclerview);

        cancel = (Button) view.findViewById(R.id.btCancel);
        newFolder = (Button) view.findViewById(R.id.btNewFolder);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fatherDialog.dismiss();
            }
        });

        newFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                creatFolder();
            }
        });


        recyclerview.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerview.setAdapter(new rvAdapterCopyFolders(mContext, folderList));

        ((rvAdapterCopyFolders) recyclerview.getAdapter()).setOnItemClickListener(new rvAdapterCopyFolders.MyClickListener() {
            @Override
            public void onItemClick(int position) {
                confirmDialog(folderList.getFolders().get(position).getPath());
            }
        });

        container.addView(view);

        return view;
    }


    private void confirmDialog(String path) {
        final AlertDialog.Builder RecDialog = new AlertDialog.Builder(mContext);

        final ViewPager viewPager = new ViewPager(mContext);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        viewPager.setLayoutParams(params);

        vpAdapterCopyMediaConfirm vpAdapterCopyMedia = new vpAdapterCopyMediaConfirm(mContext, medias, folder, adp, path,allmedias);
        viewPager.setAdapter(vpAdapterCopyMedia);
        RecDialog.setView(viewPager);
        alertDialog = RecDialog.create();

        vpAdapterCopyMedia.passFatherDialog(alertDialog);

        alertDialog.getWindow().setBackgroundDrawable(null);
        alertDialog.getWindow().setLayout(ViewPager.LayoutParams.MATCH_PARENT, ViewPager.LayoutParams.WRAP_CONTENT);
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.show();


        alertDialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                fatherDialog.dismiss();
            }
        });
    }

    private AlertDialog alertDialog;

    private void creatFolder() {
        final AlertDialog.Builder RecDialog = new AlertDialog.Builder(mContext);

        //Typeface tittle_type = MainActivity.getTyperFace();

        TextView dialogTittle = new TextView(mContext);
        dialogTittle.setText(R.string.newFolderTittle);
        //dialogTittle.setTypeface(tittle_type);
        dialogTittle.setTextColor(Color.WHITE);
        dialogTittle.setPadding(20, 10, 10, 10);
        dialogTittle.setTextSize(18);
        dialogTittle.setBackgroundResource(R.drawable.datail_gradient);

        final TextView input = new TextView(mContext);
        input.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        input.setText(R.string.newFolderMessage);
        //input.setTypeface(tittle_type);

        LinearLayout dialogLayout = new LinearLayout(mContext);
        dialogLayout.setOrientation(LinearLayout.VERTICAL);

        LinearLayout editTextLayout = new LinearLayout(mContext);
        editTextLayout.setOrientation(LinearLayout.HORIZONTAL);
        editTextLayout.setPadding(30, 30, 30, 30);
        editTextLayout.addView(input);

        final EditText editText = new EditText(mContext);

        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                if (testString(source.toString(),dend)){
                    return null;
                }
                else {
                    Toast.makeText(mContext, mContext.getString(R.string.invalidCaracter), Toast.LENGTH_SHORT).show();
                    return "";
                }


            }
        };
        editText.setFilters(new InputFilter[]{filter});

        dialogLayout.addView(dialogTittle);
        dialogLayout.addView(editTextLayout);
        dialogLayout.addView(editText);

        RecDialog.setView(dialogLayout);


        RecDialog.setPositiveButton(R.string.newFolderButton,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String path = editText.getText().toString();
                        if(String.valueOf(path.toCharArray()[path.toCharArray().length-1]).equals("/")){
                            char[] c = new char[path.toCharArray().length-1];
                            for(int i=0;i<path.toCharArray().length-1;i++){
                                c[i] = path.toCharArray()[i];
                            }
                            path = String.copyValueOf(c);
                        }
                        Berreivement_values bv = new Berreivement_values(mContext);
                        folder.copyTo(Environment.getExternalStorageDirectory().getPath() + "/"+bv.getNewFolderDirectory()+"/"+ editText.getText().toString(), medias);
                        adp.notifyDataSetChanged(allmedias);
                        alertDialog.dismiss();
                        fatherDialog.dismiss();
                    }
                });

        RecDialog.setNeutralButton(R.string.cancel,
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
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTransformationMethod(null);
        alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTransformationMethod(null);
    }

    private boolean testString(String c,int end) {

        if (c.toString().contains(".")
                || c.toString().contains("|")
                || c.toString().contains(".")
                || c.toString().contains("<")
                || c.toString().contains(">")
                || c.toString().contains("'\'")
                || c.toString().contains(":")
                || c.toString().contains("*")
                || c.toString().contains("?")
                )
        {
            return false;
        }
        else if(end==0){
            if(c.equals("/")){
                return false;
            }
            else {
                return true;
            }
        }
        else {
            return true;
        }
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((LinearLayout) object);
    }
}