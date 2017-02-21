package com.thexma.yicamviewer;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.ImageView;

import java.lang.ref.WeakReference;

import dll.DatabaseInterface;
import dll.Helper;

import static dll.Helper.getVideoFrameFromVideo;

/**
 * Created by kevin on 2/19/2017.
 */

public class ImageDownloaderTask extends AsyncTask<String, Void, Bitmap> {
    private final WeakReference<ImageView> imageViewReference;
    //private final ImageView imageThumbnail;
    private DatabaseInterface db =null;
    private  int recordID;

    public ImageDownloaderTask(ImageView imageView) {
        imageViewReference = new WeakReference<ImageView>(imageView);
        //imageThumbnail =imageView;
    }

    public ImageDownloaderTask(ImageView imageView, DatabaseInterface database, int recordID) {
        imageViewReference = new WeakReference<ImageView>(imageView);
        this.db = database;
        this.recordID = recordID;

    }

    @Override
    protected Bitmap doInBackground(String... params) {
        Bitmap bitmap = null;
        try{
            bitmap = getVideoFrameFromVideo(params[0]);
        }
        catch (Throwable t)
        {
            if (t instanceof Throwable) {
                Log.e("-----------", t.toString());
            }
        }
        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (isCancelled()) {
            bitmap = null;
        }




        if (imageViewReference != null) {
            ImageView imageView = imageViewReference.get();
            if (imageView != null) {
                if (bitmap != null) {
                    imageView.setImageBitmap(bitmap);
                    if(db != null)
                    {
                        byte[] image = Helper.getBytes(bitmap);
                        db.updateRecordThumbnail(recordID,image);
                        Log.d("------------","save iamge" );
                    }
                } else {
                    Drawable placeholder = imageView.getContext().getResources().getDrawable(R.drawable.placeholder);
                    imageView.setImageDrawable(placeholder);
                }
            }
        }
    }
}
