package com.infinityco.infinitygallery.Adapters;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Build;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.infinityco.infinitygallery.Activitys.MainActivity;
import com.infinityco.infinitygallery.Objects.Berreivement_values;
import com.infinityco.infinitygallery.R;

import java.util.List;


public class CardViewAdapter_BerreivermentOptions extends RecyclerView.Adapter<CardViewAdapter_BerreivermentOptions.ViewHolder> {
    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private List<Berreiverment> Options;
    private Activity context;
    private static CardViewAdapter_BerreivermentOptions.MyClickListener myClickListener;
    private boolean newStyle = true;
    private int newFolderPathBerreiverment = 0,darkthemeBerreiverment=1,highQualityBerreiverment=2,GridBerreiverment=3,RawBerreiverment=4,FullScreenBerreiverment=5,newStyleBerreiverment=6,recentPhotosBehaviorment=7;

    public static class Berreiverment{

        private String Name,Text;
        public Berreiverment(String Name, String Text){
            this.Name = Name;
            this.Text = Text;
        }
        public String getName() {
            return Name;
        }

        public String getText() {
            return Text;
        }

    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View v) {
            super(v);
        }
    }

    public class DefautBerreivermentHolder extends ViewHolder implements View.OnClickListener {
        TextView optionsText,optionsTittle,optionHighQualityMessage;
        Switch switchStatus;
        Button btSaveNewFolder;
        RelativeLayout rlNewFolder;
        LinearLayout rlSpinner;
        Spinner spinner;

        private DefautBerreivermentHolder(View itemView) {
            super(itemView);

            switchStatus =  itemView.findViewById(R.id.berreivement_switch);
            optionsText =  itemView.findViewById(R.id.berreivement_text);
            optionsTittle =   itemView.findViewById(R.id.berreivement_Tittle);
            btSaveNewFolder =   itemView.findViewById(R.id.btSaveNewFolderPath);
            rlNewFolder =  itemView.findViewById(R.id.rlEditTextLayout);
            rlSpinner = itemView.findViewById(R.id.rlSpinnerLayout);
            spinner =   itemView.findViewById(R.id.Spinner);
            optionHighQualityMessage =   itemView.findViewById(R.id.tvHighQualityMessage);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition());

        }
    }




    public void setOnItemClickListener(CardViewAdapter_BerreivermentOptions.MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public CardViewAdapter_BerreivermentOptions(Activity context, List<Berreiverment> Options) {
        this.Options = Options;
        this.context = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        View v;
        if (
                    viewType == newFolderPathBerreiverment||
                        viewType==darkthemeBerreiverment||
                        viewType == highQualityBerreiverment ||
                        viewType == GridBerreiverment || viewType == RawBerreiverment||
                        viewType == FullScreenBerreiverment||
                        viewType == newStyleBerreiverment||
                        viewType == recentPhotosBehaviorment
                ) {

            if(MainActivity.isDarkTheme()){
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.carditem_berreiverment_options_dark, viewGroup, false);
            }
            else {
                v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.carditem_berreiverment_options, viewGroup, false);
            }
            return new DefautBerreivermentHolder(v);
        } else{
            return null;
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position) {
        if (viewHolder.getItemViewType() == newFolderPathBerreiverment) {
            final DefautBerreivermentHolder holder = (DefautBerreivermentHolder) viewHolder;
            final Berreivement_values berreivement_values = new Berreivement_values(context);

            holder.switchStatus.setVisibility(View.INVISIBLE);
            holder.rlNewFolder.setVisibility(View.VISIBLE);
            holder.rlSpinner.setVisibility(View.GONE);

            holder.btSaveNewFolder.setTransformationMethod(null);
            holder.btSaveNewFolder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    changePath(berreivement_values.getNewFolderDirectory());
                }
            });



            holder.optionsTittle.setText(Options.get(position).getName());
            holder.optionsText.setText(Options.get(position).getText());

        }
        else if (viewHolder.getItemViewType() == darkthemeBerreiverment) {
            final DefautBerreivermentHolder holder = (DefautBerreivermentHolder) viewHolder;
            final Berreivement_values berreivement_values = new Berreivement_values(context);


            holder.switchStatus.setVisibility(View.VISIBLE);
            holder.rlNewFolder.setVisibility(View.GONE);
            holder.rlSpinner.setVisibility(View.GONE);

            try {
                holder.switchStatus.setChecked(berreivement_values.getDarkThemeStatus());
            }
            catch (Exception e){
                holder.switchStatus.setChecked(false);
            }
            holder.switchStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    MainActivity.setChangeTheme();
                    if(berreivement_values.setDarkThemeStatus(isChecked)){
                        Toast.makeText(context, context.getText(R.string.DartThemeApplyMensage), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            holder.optionsTittle.setText(Options.get(position).getName());
            holder.optionsText.setText(Options.get(position).getText());

        }

        else if (viewHolder.getItemViewType() == recentPhotosBehaviorment) {
            final DefautBerreivermentHolder holder = (DefautBerreivermentHolder) viewHolder;
            final Berreivement_values berreivement_values = new Berreivement_values(context);


            holder.switchStatus.setVisibility(View.VISIBLE);
            holder.rlNewFolder.setVisibility(View.GONE);
            holder.rlSpinner.setVisibility(View.GONE);

            try {
                holder.switchStatus.setChecked(berreivement_values.getRecentStatus());
            }
            catch (Exception e){
                holder.switchStatus.setChecked(newStyle);
            }
            holder.switchStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    MainActivity.setChangeTheme();
                    if(berreivement_values.setRecentStatus(isChecked)){
                        Toast.makeText(context, context.getText(R.string.DartThemeApplyMensage), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            holder.optionsTittle.setText(Options.get(position).getName());
            holder.optionsText.setText(Options.get(position).getText());

        }

        else if (viewHolder.getItemViewType() == GridBerreiverment) {
            final DefautBerreivermentHolder holder = (DefautBerreivermentHolder) viewHolder;
            final Berreivement_values berreivement_values = new Berreivement_values(context);


            holder.switchStatus.setVisibility(View.VISIBLE);
            holder.rlNewFolder.setVisibility(View.GONE);
            holder.rlSpinner.setVisibility(View.GONE);

            try {
                holder.switchStatus.setChecked(berreivement_values.getGridStatus());
            }
            catch (Exception e){
                holder.switchStatus.setChecked(newStyle);
            }
            holder.switchStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    MainActivity.setChangeTheme();
                    if(berreivement_values.setGridStatus(isChecked)){
                        Toast.makeText(context, context.getText(R.string.DartThemeApplyMensage), Toast.LENGTH_SHORT).show();
                    }
                }
            });

            holder.optionsTittle.setText(Options.get(position).getName());
            holder.optionsText.setText(Options.get(position).getText());

        }

        else if (viewHolder.getItemViewType() == RawBerreiverment) {
            final DefautBerreivermentHolder holder = (DefautBerreivermentHolder) viewHolder;
            final Berreivement_values berreivement_values = new Berreivement_values(context);


            holder.switchStatus.setVisibility(View.VISIBLE);
            holder.rlNewFolder.setVisibility(View.GONE);
            holder.rlSpinner.setVisibility(View.GONE);

            try {
                holder.switchStatus.setChecked(berreivement_values.getRawStatus());
            }
            catch (Exception e){
                holder.switchStatus.setChecked(false);
            }
            holder.switchStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    berreivement_values.setRawStatus(isChecked);
                    MainActivity.changeRawStatus(isChecked);
                }
            });

            holder.optionsTittle.setText(Options.get(position).getName());
            holder.optionsText.setText(Options.get(position).getText());

        }

        else if (viewHolder.getItemViewType() == FullScreenBerreiverment) {
            final DefautBerreivermentHolder holder = (DefautBerreivermentHolder) viewHolder;
            final Berreivement_values berreivement_values = new Berreivement_values(context);


            holder.switchStatus.setVisibility(View.VISIBLE);
            holder.rlNewFolder.setVisibility(View.GONE);
            holder.rlSpinner.setVisibility(View.GONE);

            try {
                holder.switchStatus.setChecked(berreivement_values.getFullscreenStatus());
            }
            catch (Exception e){
                holder.switchStatus.setChecked(false);
            }
            holder.switchStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    berreivement_values.setFullscreentatus(isChecked);
                }
            });

            holder.optionsTittle.setText(Options.get(position).getName());
            holder.optionsText.setText(Options.get(position).getText());

        }

        else if (viewHolder.getItemViewType() == newStyleBerreiverment) {
            final DefautBerreivermentHolder holder = (DefautBerreivermentHolder) viewHolder;
            final Berreivement_values berreivement_values = new Berreivement_values(context);


            holder.switchStatus.setVisibility(View.VISIBLE);
            holder.rlNewFolder.setVisibility(View.GONE);
            holder.rlSpinner.setVisibility(View.GONE);


            try {
                newStyle = berreivement_values.getNewStyleStatus();
                holder.switchStatus.setChecked(newStyle);
            }
            catch (Exception e){
                holder.switchStatus.setChecked(true);
            }
            holder.switchStatus.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    MainActivity.setChangeTheme();
                    berreivement_values.setNewStyleStatus(isChecked);
                }
            });

            holder.optionsTittle.setText(Options.get(position).getName());
            holder.optionsText.setText(Options.get(position).getText());

        }

        else if (viewHolder.getItemViewType() == highQualityBerreiverment) {
            final DefautBerreivermentHolder holder = (DefautBerreivermentHolder) viewHolder;
            final Berreivement_values berreivement_values = new Berreivement_values(context);


            holder.switchStatus.setVisibility(View.INVISIBLE);
            holder.rlNewFolder.setVisibility(View.GONE);
            holder.rlSpinner.setVisibility(View.VISIBLE);

            final String[] status = new String[3];
            status[0] = context.getString(R.string.HiQuilitySpinner1);
            status[1] = context.getString(R.string.HiQuilitySpinner2);
            status[2] = context.getString(R.string.HiQuilitySpinner3);

            ArrayAdapter<String> adp = new ArrayAdapter<>(context.getApplicationContext(), R.layout.spinner_layout, R.id.textView,status);
            holder.spinner.setAdapter(adp);

            holder.spinner.setSelection(berreivement_values.getHighQuallityStatus());
            if(berreivement_values.getHighQuallityStatus()==2){
                holder.optionHighQualityMessage.setText(context.getText(R.string.HighQualityMessage));
            }

            holder.spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    berreivement_values.setHighQuallityStatus(i);
                    if(i==2){
                        holder.optionHighQualityMessage.setText(context.getText(R.string.HighQualityMessage));
                    }
                    else {
                        holder.optionHighQualityMessage.setText("");
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });

            holder.optionsTittle.setText(Options.get(position).getName());
            holder.optionsText.setText(Options.get(position).getText());

        }
    }


    public interface MyClickListener {
        void onItemClick(int clickPositon);
    }

    private AlertDialog alertDialog;
    private void changePath(String path) {
        final AlertDialog.Builder RecDialog = new AlertDialog.Builder(context);

        //Typeface tittle_type = MainActivity.getTyperFace();

        TextView dialogTittle = new TextView(context);
        dialogTittle.setText(R.string.NewFolderPath);
        //dialogTittle.setTypeface(tittle_type);
        dialogTittle.setTextColor(Color.WHITE);
        dialogTittle.setPadding(20, 10, 10, 10);
        dialogTittle.setTextSize(25);
        dialogTittle.setBackgroundResource(R.drawable.datail_gradient);


        final EditText Message = new EditText(context);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(30,40,30,0);
        Message.setPadding(20, 5, 20, 5);
        Message.setLayoutParams(params);
        Message.setHint(path);
        Message.setBackgroundResource(R.drawable.box_layout2);
        //Directory.setTypeface(tittle_type);

        TextView message1 = new TextView(context);
        message1.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        message1.setPadding(20, 10, 10, 10);
        message1.setText(context.getText(R.string.NewFolderpathMessage));


        LinearLayout dialogLayout = new LinearLayout(context);
        dialogLayout.setOrientation(LinearLayout.VERTICAL);
        dialogLayout.addView(dialogTittle);
        dialogLayout.addView(message1);
        dialogLayout.addView(Message);


        RecDialog.setView(dialogLayout);

        RecDialog.setNegativeButton(context.getString(R.string.cancel),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        alertDialog.dismiss();
                    }
                });

        RecDialog.setPositiveButton(context.getString(R.string.Save),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if(!Message.getText().toString().equals("")) {
                            Berreivement_values berreivement_values = new Berreivement_values(context);
                            berreivement_values.setNewFolderDirectory(Message.getText().toString());
                            alertDialog.dismiss();
                        }
                    }
                });


        alertDialog = RecDialog.create();
        //alertDialog.getWindow().setTitleColor(Color.RED);
        alertDialog.show();


        alertDialog.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(context.getColor(R.color.colorDetailDark));
        }
        else {
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.GRAY);
        }

        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(Color.RED);
        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTransformationMethod(null);
        alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTransformationMethod(null);
    }


    @Override
    public int getItemCount() {
        return Options.size();
    }


    @Override
    public int getItemViewType(int position) {
        if(Options.get(position).Name.equals(context.getString(R.string.NewFolderPath))){
            return newFolderPathBerreiverment;
        }
        else  if(Options.get(position).Name.equals(context.getString(R.string.DarkTheme))){
            return darkthemeBerreiverment;
        }
        else if (Options.get(position).Name.equals(context.getString(R.string.HighQualityTittle))){
            return highQualityBerreiverment;
        }
        else if (Options.get(position).Name.equals(context.getString(R.string.GridTheme))){
            return GridBerreiverment;
        }
        else if(Options.get(position).Name.equals(context.getString(R.string.RawStatus))){
            return RawBerreiverment;
        }
        else if(Options.get(position).Name.equals(context.getString(R.string.FullScreenTittle))){
            return FullScreenBerreiverment;
        }
        else if(Options.get(position).Name.equals(context.getString(R.string.NewStyleTittle))){
            return newStyleBerreiverment;
        }
        else if(Options.get(position).Name.equals(context.getString(R.string.RecentPhotos))){
            return recentPhotosBehaviorment;
        }
        else return 1;
    }
}