package com.infinityco.infinitygallery.Adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Build;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.infinityco.infinitygallery.Activitys.MainActivity;
import com.infinityco.infinitygallery.R;

import java.io.File;

public class rvAdapterHiddenFolders extends RecyclerView.Adapter<rvAdapterHiddenFolders.DataObjectHolder>{
    private String[] hiddenFolders;
    private Context context;

    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private Typeface tittle_type;

    public static class DataObjectHolder extends RecyclerView.ViewHolder  {
        TextView folderName,folderPath;
        Button btHiddenFolder;

        public DataObjectHolder(View itemView) {
            super(itemView);

            folderName = (TextView) itemView.findViewById(R.id.tvFolderName);
            folderPath = (TextView)  itemView.findViewById(R.id.tvFolderPath);
            btHiddenFolder = (Button) itemView.findViewById(R.id.btHiddenFolder);
            //folderName.setTypeface(typeface);


        }

    }


    public rvAdapterHiddenFolders(Context context, String[] hiddenFolders) {

        this.context = context;
        this.hiddenFolders = hiddenFolders;

        //this.tittle_type = tittle_type;
    }

    public int getSize(){
        return hiddenFolders.length;
    }


    @Override
    public rvAdapterHiddenFolders.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        if(MainActivity.isDarkTheme()){ view = LayoutInflater.from(parent.getContext()).inflate(R.layout.carditem_hiddenfolder_dark, parent, false);
        }
        else { view = LayoutInflater.from(parent.getContext()).inflate(R.layout.carditem_hiddenfolder, parent, false);
        }
        rvAdapterHiddenFolders.DataObjectHolder dataObjectHolder = new rvAdapterHiddenFolders.DataObjectHolder(view);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(rvAdapterHiddenFolders.DataObjectHolder holder, final int position) {

        final File file = new File(hiddenFolders[position]);

        holder.folderName.setText(file.getName());

        holder.folderPath.setText(file.getPath());

        holder.btHiddenFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                excludeDialog(position,new File(context.getFilesDir() + "/" + file.getName()+file.getParentFile().getName() + ".ig"), file.getName());
            }
        });

    }


    AlertDialog alertDialogDelete;
    private void excludeDialog(final int pos, final File file,String folderName){
        final AlertDialog.Builder RecDialog = new AlertDialog.Builder(context);

        //Typeface tittle_type = MainActivity.getTyperFace();

        TextView dialogTittle = new TextView(context);
        dialogTittle.setText(R.string.show);
        //dialogTittle.setTypeface(tittle_type);
        dialogTittle.setTextColor(Color.WHITE);
        dialogTittle.setPadding(20,10,10,10);
        dialogTittle.setTextSize(25);
        dialogTittle.setBackgroundResource(R.drawable.datail_gradient);

        final TextView input = new TextView(context);
        input.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.WRAP_CONTENT));
        input.setText(context.getString(R.string.RemoveFromHiddenMessage)+" "+folderName+"?");
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


        RecDialog.setPositiveButton(R.string.show,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        exclude(pos,file);
                        alertDialogDelete.dismiss();
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
        if(android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            alertDialogDelete.getWindow().setTitleColor(context.getColor(R.color.colorDetailDark));
        }
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

    private void exclude(int pos,File file){
        file.delete();
        if(file.exists()){
            Toast.makeText(context, context.getString(R.string.showFolderFailMessage), Toast.LENGTH_SHORT).show();
        }
        else {
            String[] newHidenFolders = new String[hiddenFolders.length - 1];
            int cont = 0;
            for (int i = 0; i < hiddenFolders.length; i++) {
                if (i != pos) {
                    newHidenFolders[cont] = hiddenFolders[i];
                    cont++;
                }
            }
            hiddenFolders = newHidenFolders;
            notifyDataSetChanged();
            Toast.makeText(context, context.getString(R.string.showFolderMessage), Toast.LENGTH_SHORT).show();
        }

    }

    
    @Override
    public int getItemCount() {
        return hiddenFolders.length;
    }

}
