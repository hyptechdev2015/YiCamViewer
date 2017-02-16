package com.thexma.yicamviewer;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;
import com.thexma.*;

import dll.Record;

/**
 * Created by kle on 2/15/2017.
 */

public class RecordListAdapter extends ArrayAdapter<Record> {
    private List<Record> data = null;
    private static LayoutInflater inflater=null;

    public RecordListAdapter(Context context, int layoutResourceId, List<Record> data) {
        super(context, layoutResourceId, data);
        this.data = data;
    }


    @SuppressLint("DefaultLocale")
    @Override
    public View getView(int position, View convertView,ViewGroup parent) {
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
