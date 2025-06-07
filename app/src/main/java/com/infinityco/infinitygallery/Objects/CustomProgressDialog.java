package com.infinityco.infinitygallery.Objects;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.infinityco.infinitygallery.R;
import com.squareup.picasso.Picasso;

public class CustomProgressDialog {

    private Context context;
    private AlertDialog alertDialog;
    private AlertDialog.Builder RecDialog;
    private Boolean darkTheme = false,cancelable=false;
    private LinearLayout dialogLayout,textLayout;
    private ImageView loadingGif;
    private TextView Title,Message;

    public CustomProgressDialog(Context context){
        this.context = context;

        RecDialog = new AlertDialog.Builder(context);

        dialogLayout = new LinearLayout(context);
        dialogLayout.setOrientation(LinearLayout.HORIZONTAL);
        dialogLayout.setGravity(Gravity.CENTER_VERTICAL);
        dialogLayout.setPadding(40,20,20,20);

        textLayout = new LinearLayout(context);
        textLayout.setOrientation(LinearLayout.VERTICAL);
        textLayout.setGravity(Gravity.START);
        textLayout.setPadding(40,0,20,0);

        loadingGif = new ImageView(context);
        Glide.with(context).asGif().load(R.drawable.loading).override(150, 150).into(loadingGif);

        dialogLayout.addView(loadingGif);

    }

    public void setDarkTheme(Boolean darkTheme){
        this.darkTheme = darkTheme;
    }

    public void setTitle(String title){

        Title = new TextView(context);
        Title.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        Title.setText(title);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Title.setTextAppearance(R.style.TextAppearance_Compat_Notification_Title);
        }
    }

    public void setMessage(String message){

        Message = new TextView(context);
        Message.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        Message.setText(message);

    }

    public void show(){

        if(Title!= null){
            textLayout.addView(Title);
        }
        if(Message != null){
            textLayout.addView(Message);
        }
        dialogLayout.addView(textLayout);

        RecDialog.setView(dialogLayout);

        alertDialog = RecDialog.create();
        alertDialog.setCancelable(cancelable);
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTransformationMethod(null);
        alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setTransformationMethod(null);
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTransformationMethod(null);
    }

    public void setCancelable(boolean cancelable){
        this.cancelable = cancelable;
    }

    public void setNegativeButton(String buttomMessage, DialogInterface.OnClickListener button){
        RecDialog.setNegativeButton(buttomMessage,button);
    }

    public void setNeutralButton(String buttomMessage, DialogInterface.OnClickListener button){
        RecDialog.setNeutralButton(buttomMessage,button);
    }

    public void setPositiveButton(String buttomMessage, DialogInterface.OnClickListener button){
        RecDialog.setPositiveButton(buttomMessage,button);
    }


    public void cancel(){

        if (alertDialog != null) {
            alertDialog.cancel();
            alertDialog = null;
        }
    }

    public void dismiss() {
        if (alertDialog != null) {
            alertDialog.dismiss();
            alertDialog = null;
        }
    }

    public void setOnShowListener(DialogInterface.OnShowListener onShowListener){
        if (alertDialog != null) {
            alertDialog.setOnShowListener(onShowListener);
        }
    }

    public void setOnCancelListener(DialogInterface.OnCancelListener OnCancelListener){

        if (alertDialog != null) {
            alertDialog.setOnCancelListener(OnCancelListener);
        }
    }

}