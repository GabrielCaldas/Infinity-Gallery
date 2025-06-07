package com.infinityco.infinitygallery.Adapters;

import android.content.Context;
import android.graphics.Typeface;

import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.infinityco.infinitygallery.Objects.FolderList;
import com.infinityco.infinitygallery.R;

public class rvAdapterCopyFolders extends RecyclerView.Adapter<rvAdapterCopyFolders.DataObjectHolder>{
    private FolderList folders;
    private Context context;

    private static String LOG_TAG = "MyRecyclerViewAdapter";
    private static rvAdapterCopyFolders.MyClickListener myClickListener;
    private Typeface tittle_type;

    public static class DataObjectHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView folderName,folderCount;
        Button btFolder;

        public DataObjectHolder(View itemView, Typeface typeface) {
            super(itemView);

            folderName = (TextView) itemView.findViewById(R.id.tvFolderName);
            folderCount = (TextView)  itemView.findViewById(R.id.tvFolderCount);
            //btFolder = (Button) itemView.findViewById(R.id.btFolder);
            //folderName.setTypeface(typeface);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            myClickListener.onItemClick(getAdapterPosition());
        }
    }

    public void setOnItemClickListener(rvAdapterCopyFolders.MyClickListener myClickListener) {
        this.myClickListener = myClickListener;
    }

    public rvAdapterCopyFolders(Context context, FolderList folders) {

        this.context = context;
        this.folders = folders;
        //this.tittle_type = tittle_type;
    }



    @Override
    public rvAdapterCopyFolders.DataObjectHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.carditem_copy_folder, parent, false);

        rvAdapterCopyFolders.DataObjectHolder dataObjectHolder = new rvAdapterCopyFolders.DataObjectHolder(view,tittle_type);
        return dataObjectHolder;
    }

    @Override
    public void onBindViewHolder(rvAdapterCopyFolders.DataObjectHolder holder, final int position) {

        holder.folderName.setText(folders.getFolders().get(position).getName());
        if(folders.getFolders().get(position).getMedias().size()==1){
            holder.folderCount.setText(folders.getFolders().get(position).getMedias().size()+" "+context.getString(R.string.media)+" ");
        }
        else{
            holder.folderCount.setText(folders.getFolders().get(position).getMedias().size()+" "+context.getString(R.string.medias)+" ");
        }

        /*
        holder.btFolder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, FolderActivity.class).putExtra("Folder",folders.getFolders().get(position).getPath()));
            }
        });*/

    }

    public void UpDateViews(){
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return folders.getFolders().size();
    }

    public interface MyClickListener {
        void onItemClick(int position);
    }
}
