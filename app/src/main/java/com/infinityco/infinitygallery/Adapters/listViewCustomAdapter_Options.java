package com.infinityco.infinitygallery.Adapters;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.infinityco.infinitygallery.Activitys.MainActivity;
import com.infinityco.infinitygallery.R;


public class listViewCustomAdapter_Options extends BaseAdapter {
    public String Options[];
    public int[] Optionsimgs;
    public Activity context;
    public LayoutInflater inflater;

    public listViewCustomAdapter_Options(Activity context, String[] Options, int[] Optionsimgs) {
        super();

        this.context = context;
        this.Options = Options;
        this.Optionsimgs = Optionsimgs;
        this.inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return Options.length+1;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return 0;
    }

    public static class ViewHolder
    {
        TextView Options;
        ImageView Optionsimgs;
        LinearLayout ll1,ll2,llop,llms;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        ViewHolder holder;
        if(convertView==null)
        {
            holder = new ViewHolder();

            if(MainActivity.isDarkTheme()){
                convertView = inflater.inflate(R.layout.carditem_options_dark, null);
            }
            else {
                convertView = inflater.inflate(R.layout.carditem_options, null);
            }



            holder.Optionsimgs = (ImageView) convertView.findViewById(R.id.optionsimg);
            holder.Options = (TextView) convertView.findViewById(R.id.optionsname);
            holder.ll1 = convertView.findViewById(R.id.layout);
            holder.ll2 = convertView.findViewById(R.id.layout2);
            holder.llop = convertView.findViewById(R.id.llOnePlusBrasil);
            holder.llms = convertView.findViewById(R.id.llManolos);

            convertView.setTag(holder);
        }
        else holder=(ViewHolder)convertView.getTag();

        if(position<Optionsimgs.length) {
            holder.ll1.setVisibility(View.VISIBLE);
            holder.ll2.setVisibility(View.GONE);

            holder.Optionsimgs.setBackgroundResource(Optionsimgs[position]);
            holder.Options.setText(Options[position]);
        }
        else {

            holder.ll2.setVisibility(View.VISIBLE);
            holder.ll1.setVisibility(View.GONE);

            holder.llop.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url = "https://www.facebook.com/oneplus5br/";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    context.startActivity(i);
                }
            });

            holder.llms.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String url = "https://www.facebook.com/groups/manolosdasam2.0/";
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    context.startActivity(i);
                }
            });
        }

        return convertView;
    }
}
