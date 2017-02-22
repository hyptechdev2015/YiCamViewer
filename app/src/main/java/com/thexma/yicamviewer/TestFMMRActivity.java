package com.thexma.yicamviewer;

import android.graphics.Bitmap;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;

import dll.Helper;
import wseemann.media.FFmpegMediaMetadataRetriever;

public class TestFMMRActivity extends AppCompatActivity {

    final String TAG = "--- TestFMMRActivity";
    private byte[] artwork = null;
    FFmpegMediaMetadataRetriever mmr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_empty);

        String url = MainActivity.HTTP_RECORD_URL + "2017Y01M27D10H/29M00S.mp4";
        Log.i(TAG, url);
        Bitmap artworkBit = null;

        ImageView imgView = (ImageView) this.findViewById(R.id.imageViewFFMMR);
        try {
            artworkBit = new TakeScreenshot().execute(url).get();
        } catch (Exception ex) {
            Log.d(TAG, ex.toString());
        }
        if (artwork != null) {
            Log.i(TAG, "not bytes");
            Bitmap newBit = Helper.getImage(artwork);
            imgView.setImageBitmap(artworkBit);
        }




/*        FFmpegMediaMetadataRetriever mmr = new FFmpegMediaMetadataRetriever();
        try {
            mmr.setDataSource(URLDecoder.decode(mUri, "UTF-8"));
            //mmr.setDataSource(mUri);
            //mmr.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_ALBUM);
            //mmr.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_ARTIST);
            //Bitmap b = mmr.getFrameAtTime(2000000, FFmpegMediaMetadataRetriever.OPTION_CLOSEST); // frame at 2 seconds
            //artwork = mmr.getEmbeddedPicture();

        } catch (IllegalArgumentException ex) {
            Log.d(TAG, ex.toString());
            mmr.release();
        } catch (UnsupportedEncodingException ex) {
            Log.d(TAG, ex.toString());
            mmr.release();
        } catch (Exception ex) {
            Log.d(TAG, ex.toString());
            mmr.release();
        }

        mmr.release();*/


    }

    class TakeScreenshot extends AsyncTask<String, Void, Bitmap> {
/*
        public TakeScreenshot() {
        }
*/

        @Override
        protected Bitmap doInBackground(String... params) {

            Bitmap newBit = null;

            try {


                mmr = new FFmpegMediaMetadataRetriever();
                //mmr.setDataSource(URLDecoder.decode(params[0], "UTF-8"));
                if (Build.VERSION.SDK_INT >= 14)
                    mmr.setDataSource(params[0], new HashMap<String, String>());
                else
                    mmr.setDataSource(params[0]);
                //mmr.setDataSource(mUri);
                //mmr.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_ALBUM);
                //mmr.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_ARTIST);
                //Bitmap b = mmr.getFrameAtTime(2000000, FFmpegMediaMetadataRetriever.OPTION_CLOSEST); // frame at 2 seconds
                artwork = mmr.getEmbeddedPicture();

                if (artwork != null) {
                    Log.i(TAG, "not bytes");
                    newBit = Helper.getImage(artwork);
                }

            } catch (IllegalArgumentException ex) {
                Log.d(TAG, ex.toString());
                mmr.release();
/*            } catch (UnsupportedEncodingException ex) {
                Log.d(TAG, ex.toString());
                mmr.release();*/
            } catch (Exception ex) {
                Log.d(TAG, ex.toString());
                mmr.release();
            }

            mmr.release();

            return newBit;
        }

        @Override
        protected void onPreExecute() {

        }


    }
}
