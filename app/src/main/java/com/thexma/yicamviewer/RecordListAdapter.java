package com.thexma.yicamviewer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.MainThread;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.URI;
import java.util.List;
import com.thexma.*;

import dll.Helper;
import dll.Record;

/**
 * Created by kle on 2/15/2017.
 */

public class RecordListAdapter extends ArrayAdapter<Record> {
    private List<Record> data = null;
    private static LayoutInflater inflater=null;
    private Context cxt;

    public RecordListAdapter(Context context, int layoutResourceId, List<Record> data) {
        super(context, layoutResourceId, data);
        this.data = data;
        this.cxt = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub

        View row = convertView;
        if(convertView == null){
            inflater = LayoutInflater.from(getContext());
            row = inflater.inflate(R.layout.record_list_item, null);
        }
/*        if(row==null){
            LayoutInflater inflater=getLayoutInflater();
            row=inflater.inflate(R.layout.row, parent, false);
        }*/

        Record e = data.get(position);

        String videoUrl = MainActivity.HTTP_HOST + e.getFullUrl().toString();

        TextView textfilePath = (TextView)row.findViewById(R.id.FilePath);
        textfilePath.setText(videoUrl);
        ImageView imageThumbnail = (ImageView)row.findViewById(R.id.Thumbnail);

        Bitmap bmThumbnail = null;
        //bmThumbnail = ThumbnailUtils.createVideoThumbnail(Uri.parse(videoUrl.toString()).toString(), MediaStore.Video.Thumbnails.MICRO_KIND);
        try {
            bmThumbnail = Helper.getFFmpegMediaMetadataRetriever(this.cxt , videoUrl.toString());
        }
        catch(Throwable t)
        {
            if (t instanceof Throwable) {
                Log.e("-----------", t.toString());
            }


        }
        imageThumbnail.setImageBitmap(bmThumbnail);

        return row;
    }



    //@SuppressLint("DefaultLocale")
    //@Override
    public View getView_TEST(int position, View convertView,ViewGroup parent) {
        View row = convertView;
        if(convertView == null){
            inflater = LayoutInflater.from(getContext());
            row = inflater.inflate(R.layout.notification_list_item, null);
        }


        TextView title = (TextView)row.findViewById(R.id.notificationTitle);
        TextView description = (TextView)row.findViewById(R.id.notificationDescription);

        Record e = data.get(position);
        title.setText(e.getFileName() + " | "+ e.getFileDate() + " | " + e.getFolderName());
        description.setText(e.getFullUrl());

        return row;
    }


}
