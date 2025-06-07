package com.infinityco.infinitygallery.Adapters;

import android.content.ContentUris;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.MediaMetadataRetriever;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.infinityco.infinitygallery.Helpers.Metadata.Detail;
import com.infinityco.infinitygallery.Helpers.Metadata.imaging.ImageMetadataReader;
import com.infinityco.infinitygallery.Helpers.Metadata.imaging.jpeg.JpegMetadataReader;
import com.infinityco.infinitygallery.Helpers.Metadata.metadata.Directory;
import com.infinityco.infinitygallery.Helpers.Metadata.metadata.Metadata;
import com.infinityco.infinitygallery.Helpers.Metadata.metadata.Tag;
import com.infinityco.infinitygallery.Helpers.Metadata.metadata.avi.AviDirectory;
import com.infinityco.infinitygallery.Helpers.Metadata.metadata.bmp.BmpHeaderDirectory;
import com.infinityco.infinitygallery.Helpers.Metadata.metadata.exif.ExifSubIFDDescriptor;
import com.infinityco.infinitygallery.Helpers.Metadata.metadata.exif.ExifSubIFDDirectory;
import com.infinityco.infinitygallery.Helpers.Metadata.metadata.gif.GifHeaderDirectory;
import com.infinityco.infinitygallery.Helpers.Metadata.metadata.ico.IcoDirectory;
import com.infinityco.infinitygallery.Helpers.Metadata.metadata.jpeg.JpegDirectory;
import com.infinityco.infinitygallery.Helpers.Metadata.metadata.mp4.Mp4Dictionary;
import com.infinityco.infinitygallery.Helpers.Metadata.metadata.mp4.Mp4Directory;
import com.infinityco.infinitygallery.Helpers.Metadata.metadata.png.PngDirectory;
import com.infinityco.infinitygallery.Helpers.Metadata.metadata.wav.WavDirectory;
import com.infinityco.infinitygallery.Objects.Media;
import com.infinityco.infinitygallery.R;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by gabrielcaldas on 06/10/16.
 */

public class ListViewCustomAdapter_MediaDetail extends BaseAdapter {

    public Media media;
    public LayoutInflater inflater;
    private Typeface tittle_type;
    private Metadata metadata;
    private List<Detail> Details;
    private Context context;
    public ListViewCustomAdapter_MediaDetail(Context context, Media media) {
        super();

        this.context = context;
        this.media = media;
        this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        try {
            if(media.getPath().endsWith(".jpg")||media.getPath().endsWith(".jpeg")){

                this.metadata = JpegMetadataReader.readMetadata(media.getFile());
            }
            else {
                this.metadata = ImageMetadataReader.readMetadata(media.getFile());
            }

            Details = extractDetails(this.metadata.getDirectoriesList());


        }
        catch (Exception e){}
        //this.tittle_type = Typeface.createFromAsset(context.getAssets(), "tittle_font.ttf");
    }

    private List<Detail> extractDetails(List<Directory> directory){
        List<Detail> details = new ArrayList<>();


        for(int j=0;j<directory.size();j++){
            for (int i = 0; i < directory.get(j).getTagCount(); i++) {
                Tag tag = directory.get(j).getTags().get(i);
                if (tag.getTagName().toLowerCase().contains("width")) {
                    add(details,new Detail(context.getString(R.string.width), tag.getDescription()));
                }
                else if (tag.getTagName().toLowerCase().contains("height")) {
                    add(details,new Detail(context.getString(R.string.Height), tag.getDescription()));
                }
                else if (tag.getTagName().toLowerCase().contains("duration")&&tag.getDescription().contains(":")) {
                    add(details,new Detail(context.getString(R.string.Duration), tag.getDescription()));
                }

                else if (tag.getTagName().toLowerCase().contains("data precision")) {
                    add(details,new Detail(context.getString(R.string.DataPrecision), tag.getDescription()));
                }
                else if (tag.getTagName().toLowerCase().contains("creation")) {
                    add(details,new Detail(context.getString(R.string.CreationDate), tag.getDescription()));
                }
                else if (tag.getTagName().toLowerCase().contains("modification")) {
                    add(details,new Detail(context.getString(R.string.LastModificationDate), tag.getDescription()));
                }
                else if (tag.getTagName().toLowerCase().contains("model")) {
                    add(details,new Detail(context.getString(R.string.CameraModel), tag.getDescription()));
                }
                else if (tag.getTagName().toLowerCase().contains("make")) {
                    add(details,new Detail(context.getString(R.string.CameraFactory), tag.getDescription()));
                }
                else if (tag.getTagName().toLowerCase().contains("exposure")) {
                    add(details,new Detail(context.getString(R.string.Exposure), tag.getDescription()));
                }
                else if (tag.getTagName().toLowerCase().contains("iso") && !tag.getTagName().toLowerCase().contains("language")) {
                    add(details,new Detail(context.getString(R.string.Iso), tag.getDescription()));
                }
                else if (tag.getTagName().toLowerCase().contains("aperture")) {
                    add(details,new Detail(context.getString(R.string.Aperture), tag.getDescription()));
                }
                /*else if (tag.getTagName().toLowerCase().contains("brightness")) {
                    add(details,new Detail(context.getString(R.string.Brightness), tag.getDescription()));
                }*/
                else if (tag.getTagName().toLowerCase().contains("flash")) {
                    add(details,new Detail(context.getString(R.string.Flash), tag.getDescription()));
                }
                else if (tag.getTagName().toLowerCase().equals("focal length")) {
                    add(details,new Detail(context.getString(R.string.Focal), tag.getDescription()));
                }
            }
        }


        return details;
    }
    private void add(List<Detail> details,Detail detail){
        boolean find = false;
        for(int i=0;i<details.size();i++){
            if(details.get(i).getDetailName().equals(detail.getDetailName())){
                find = true;
                break;
            }
        }
        if (!find){
            details.add(detail);
        }
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        if(metadata == null){
            return 3;
        }
        else {
            return 2+Details.size();
        }
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

    public static class ViewHolder {
        TextView tvInfoType, tvInfo;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        ListViewCustomAdapter_MediaDetail.ViewHolder holder;
        if (convertView == null) {
            holder = new ListViewCustomAdapter_MediaDetail.ViewHolder();

            convertView = inflater.inflate(R.layout.tv_mediadetails, null);

            holder.tvInfoType = (TextView) convertView.findViewById(R.id.tvMediaInfoType);
            holder.tvInfo = (TextView) convertView.findViewById(R.id.tvMediaInfo);

            holder.tvInfo.setTypeface(tittle_type);
            holder.tvInfoType.setTypeface(tittle_type);

            convertView.setTag(holder);
        } else
            holder = (ListViewCustomAdapter_MediaDetail.ViewHolder) convertView.getTag();



        if(metadata == null) {
            if (position == 0) {
                holder.tvInfoType.setText(R.string.detailsPath);
                holder.tvInfo.setText(media.getPath());
            } else if (position == 1) {

                holder.tvInfoType.setText(R.string.size);
                holder.tvInfo.setText(mediaSize(media.getFile()));
            } else if (position == 2) {

                holder.tvInfoType.setText(R.string.Date);
                holder.tvInfo.setText(media.getDateString());

            } else {

            }
        }
        else {

            if (position == 0) {
                holder.tvInfoType.setText(R.string.detailsPath);
                holder.tvInfo.setText(media.getPath());
            } else if (position == 1) {

                holder.tvInfoType.setText(R.string.size);
                holder.tvInfo.setText(mediaSize(media.getFile()));

            } else{

                holder.tvInfoType.setText(Details.get(position-2).getDetailName());

                holder.tvInfo.setText(Details.get(position-2).getDetailValue());
            }
        }

        return convertView;
    }

    private String getWidthHeight(){
        String width;



        Class Class = JpegDirectory.class;

        if(media.getType() == Media.ImageType) {
            if (metadata.getFirstDirectoryOfType(Class) != null) {
                width = metadata.getFirstDirectoryOfType(Class).getString(JpegDirectory.TAG_IMAGE_HEIGHT) + " x " + metadata.getFirstDirectoryOfType(Class).getString(JpegDirectory.TAG_IMAGE_WIDTH);
            } else {
                Class = BmpHeaderDirectory.class;

                if (metadata.getFirstDirectoryOfType(Class) != null) {
                    width = metadata.getFirstDirectoryOfType(Class).getString(BmpHeaderDirectory.TAG_IMAGE_HEIGHT) + " x " + metadata.getFirstDirectoryOfType(Class).getString(BmpHeaderDirectory.TAG_IMAGE_WIDTH);
                } else {
                    Class = IcoDirectory.class;

                    if (metadata.getFirstDirectoryOfType(Class) != null) {
                        width = metadata.getFirstDirectoryOfType(Class).getString(IcoDirectory.TAG_IMAGE_HEIGHT) + " x " + metadata.getFirstDirectoryOfType(Class).getString(IcoDirectory.TAG_IMAGE_WIDTH);
                    } else {
                        Class = PngDirectory.class;

                        if (metadata.getFirstDirectoryOfType(Class) != null) {
                            width = metadata.getFirstDirectoryOfType(Class).getString(PngDirectory.TAG_IMAGE_HEIGHT) + " x " + metadata.getFirstDirectoryOfType(Class).getString(PngDirectory.TAG_IMAGE_WIDTH);
                        } else {
                            width = context.getString(R.string.Unknow);
                        }
                    }
                }
            }
        }
        else {
            Class = AviDirectory.class;

            if (metadata.getFirstDirectoryOfType(Class) != null) {
                width = metadata.getFirstDirectoryOfType(Class).getString(AviDirectory.TAG_HEIGHT) + " x " + metadata.getFirstDirectoryOfType(Class).getString(AviDirectory.TAG_WIDTH);
            } else {
                Class = GifHeaderDirectory.class;

                if (metadata.getFirstDirectoryOfType(Class) != null) {
                    width = metadata.getFirstDirectoryOfType(Class).getString(GifHeaderDirectory.TAG_IMAGE_HEIGHT) + " x " + metadata.getFirstDirectoryOfType(Class).getString(GifHeaderDirectory.TAG_IMAGE_WIDTH);
                } else {
                    Class = Mp4Directory.class;

                    if (metadata.getFirstDirectoryOfType(Class) != null) {
                        width = metadata.getFirstDirectoryOfType(Class).getString(Mp4Directory.TAG_COMPATIBLE_BRANDS) + " x " + metadata.getFirstDirectoryOfType(Class).getString(GifHeaderDirectory.TAG_IMAGE_WIDTH);
                    } else {
                        width = context.getString(R.string.Unknow);
                    }
                }
            }
        }

        return width;
    }
    /*

    private void metadata() {

        MediaMetadataRetriever metaRetriver;


        metaRetriver = new MediaMetadataRetriever();
        metaRetriver.setDataSource(media.getPath());


        if ((metaRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM)) == null) {
            passaalbum = "Unknown Album ";
        } else {
            passaalbum = metaRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ALBUM);
        }


        if ((metaRetriver.getEmbeddedPicture() != null)) {
            art = metaRetriver.getEmbeddedPicture();
            int lenght = art.length;
            if(lenght>500000){
                try {

                    if(!equals(lowDefinition,BitmapFactory.decodeByteArray(art, 0, lenght))) {
                        lowDefinition = Bitmap.createScaledBitmap(MediaStore.Images.Media.getBitmap(applicationContext.getContentResolver(), ContentUris.withAppendedId(ART_CONTENT_URI, Playlist.get(pos).getAlbum_id())), 500, 500, false);
                    }
                    songImage = lowDefinition;
                } catch (Exception e) {
                    songImage = BitmapFactory.decodeResource(resources, R.drawable.album_big);
                }
            }
            else {
                songImage = BitmapFactory.decodeByteArray(art, 0, lenght);
            }

            if(songImage==null){
                songImage = BitmapFactory.decodeResource(resources, R.drawable.album_big);
            }
            art = null;
        }
        else {
            songImage = new ImageManager(applicationContext).getimg(Playlist.get(pos).getAlbum_id());
            if(songImage==null) {
                try {
                    songImage = new ImageManager(applicationContext).getimg(Playlist.get(pos).getAlbum());
                } catch (Exception e) {
                    songImage = BitmapFactory.decodeResource(resources, R.drawable.album_big);
                }
                if (songImage == null) {
                    songImage = BitmapFactory.decodeResource(resources, R.drawable.album_big);
                }
            }
        }

        if ((metaRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST)) == null) {
            passartist = "Unknown Artist";
        } else {
            passartist = metaRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_ARTIST);
        }

        if ((metaRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE)) == null) {
            passname = Playlist.get(pos).getMusica().getName().replace(".mp3", "").replace(".wav", "").toString();
        } else {
            passname = metaRetriver.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE).toString();
        }

        updateMediaSession();
        Notification();

        if(PlayerViewGroup != null) {
            PlayerViewGroup.setData(passname, passaalbum, passartist, songImage);
        }
        try {
            vgSongListPhone.setData(passname, songImage);
        }
        catch (Exception e){}
        try {
            vgMainActivityPhone.setData(passname, songImage);
        }
        catch (Exception e){}

        callmetada = true;
    }*/

    public String mediaSize(File directory) {

        double lenght = arredondar(directory.length()/(1000 * 1000));
        if(lenght==0){
            lenght = directory.length()/1000;

            if(lenght*1000<directory.length()){
                return ((int)lenght+1)+"kb";
            }
            else
                return ((int)lenght)+"kb";
        }
        else {
            return lenght+"mb";
        }
    }
    private double arredondar(double valor) {
        double arredondado = valor;
        arredondado *= (Math.pow(10, 2));

        arredondado = Math.ceil(arredondado);

        arredondado /= (Math.pow(10, 2));
        return arredondado;
    }

}

