package com.thexma.yicamviewer;


import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.FormatFlagsConversionMismatchException;

import dll.Helper;
import wseemann.media.FFmpegMediaMetadataRetriever;


/**
 * A simple {@link Fragment} subclass.
 */
public class EmptyFragment extends Fragment {

    final String TAG = "--- EmptyFragment";
    private byte[] artwork = null;
    FFmpegMediaMetadataRetriever mmr;

    public EmptyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_empty, container, false);


        ImageView imgView = (ImageView) rootView.findViewById(R.id.imageViewFFMMR);

        if (artwork != null) {
            Log.i(TAG, "not bytes");
            Bitmap newBit = Helper.getImage(artwork);
            imgView.setImageBitmap(newBit);
        }


        // Inflate the layout for this fragment
        return rootView;
    }

}
