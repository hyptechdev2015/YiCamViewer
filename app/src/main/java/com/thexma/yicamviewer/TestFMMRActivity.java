package com.thexma.yicamviewer;

import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.os.Build;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.HashMap;
import java.util.Map;

import dll.Helper;
import wseemann.media.FFmpegMediaMetadataRetriever;

public class TestFMMRActivity extends AppCompatActivity {

    final String TAG = "--- TestFMMRActivity";
    private byte[] artwork = null;
    FFmpegMediaMetadataRetriever                 mmr ;
    Bitmap newBit = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_empty);

        String url = MainActivity.HTTP_RECORD_URL + "2017Y01M27D10H/29M00S.mp4";
        Log.i(TAG, url);
        Bitmap artworkBit = null;

        ImageView imgView = (ImageView) this.findViewById(R.id.imageViewFFMMR);




        try {

            mmr = new FFmpegMediaMetadataRetriever();
            mmr.setDataSource(url);//"http://prclive1.listenon.in:9948/");

            newBit = mmr.getFrameAtTime(2000000, FFmpegMediaMetadataRetriever.OPTION_CLOSEST); // frame at 2 seconds


            FFmpegMediaMetadataRetriever.Metadata md = mmr.getMetadata();
            //artwork = mmr.getEmbeddedPicture();

          Map<String, String > map = md.getAll();
            if (map != null) {
                for (final String key : map.keySet()) {
                    Log.d("MainActivity", "Key:" + key + " Val:" + map.get(key));
                }
            }

            mmr.release();
/*
            mmr = new FFmpegMediaMetadataRetriever();
            //mmr.setDataSource(URLDecoder.decode(params[0], "UTF-8"));
            if (Build.VERSION.SDK_INT >= 14)
                mmr.setDataSource( url, new HashMap<String, String>());
            else
                mmr.setDataSource(url);
            //mmr.setDataSource(mUri);
            //mmr.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_ALBUM);
            //mmr.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_ARTIST);
            //Bitmap b = mmr.getFrameAtTime(2000000, FFmpegMediaMetadataRetriever.OPTION_CLOSEST); // frame at 2 seconds
            artwork = mmr.getEmbeddedPicture();

            if (artwork != null) {
                Log.i(TAG, "not bytes");
                newBit = Helper.getImage(artwork);
            }
*/

            //artworkBit = new TakeScreenshot(mmr).execute(url).get();
        } catch (Exception ex) {
            Log.d(TAG, ex.toString());
        }
        if (artworkBit != null) {
            ///Log.i(TAG, "not bytes");
            //Bitmap newBit = Helper.getImage(artwork);
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

    class takeThumbNail extends AsyncTask<String, Void, Bitmap>{

        @Override
        protected Bitmap doInBackground(String... params) {

            Log.i(TAG, "params:"+params[0]);

            MediaMetadataRetriever retriever = new MediaMetadataRetriever();
            retriever.setDataSource(params[0]); // Enter Full File Path Here
            artwork = retriever.getEmbeddedPicture();

            if (artwork != null) {
                Log.i(TAG, "not bytes");
                newBit = Helper.getImage(artwork);
            }

            return newBit;
        }
    }

    class TakeScreenshot extends AsyncTask<String, Void, Bitmap> {
        FFmpegMediaMetadataRetriever mffmp;
        public TakeScreenshot(FFmpegMediaMetadataRetriever ffmp) {
            mffmp = ffmp;
        }


        @Override
        protected Bitmap doInBackground(String... params) {



            try {


                //mmr.setDataSource(URLDecoder.decode(params[0], "UTF-8"));
                if (Build.VERSION.SDK_INT >= 14)
                    mffmp.setDataSource(params[0], new HashMap<String, String>());
                else
                    mffmp.setDataSource(params[0]);
                //mmr.setDataSource(mUri);
                //mmr.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_ALBUM);
                //mmr.extractMetadata(FFmpegMediaMetadataRetriever.METADATA_KEY_ARTIST);
                //Bitmap b = mmr.getFrameAtTime(2000000, FFmpegMediaMetadataRetriever.OPTION_CLOSEST); // frame at 2 seconds
                artwork = mffmp.getEmbeddedPicture();

                if (artwork != null) {
                    Log.i(TAG, "not bytes");
                    newBit = Helper.getImage(artwork);
                }

            } catch (IllegalArgumentException ex) {
                Log.d(TAG, ex.toString());
                mffmp.release();
/*            } catch (UnsupportedEncodingException ex) {
                Log.d(TAG, ex.toString());
                mmr.release();*/
            } catch (Exception ex) {
                Log.d(TAG, ex.toString());
                mffmp.release();
            }

            mffmp.release();

            return newBit;
        }

        @Override
        protected void onPreExecute() {

        }


    }
}
