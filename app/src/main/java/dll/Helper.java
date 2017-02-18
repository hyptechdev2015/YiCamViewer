package dll;

import android.content.Context;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;

import wseemann.media.FFmpegMediaMetadataRetriever;

/**
 * Created by kle on 2/16/2017.
 */

public class Helper {
    public static Boolean isNetwork(Context Context) {
        ConnectivityManager cm = (ConnectivityManager) Context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return netInfo != null && netInfo.isConnectedOrConnecting();
    }


    public static boolean IsReachable(Context context, String urlHost) {
        // First, check we have any sort of connectivity
        final ConnectivityManager connMgr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final NetworkInfo netInfo = connMgr.getActiveNetworkInfo();
        boolean isReachable = false;

        if (netInfo != null && netInfo.isConnected()) {
            // Some sort of connection is open, check if server is reachable
            try {
                URL url = new URL(urlHost);//"http://www.google.com");
                HttpURLConnection urlc = (HttpURLConnection) url.openConnection();
                urlc.setRequestProperty("User-Agent", "Android Application");
                urlc.setRequestProperty("Connection", "close");
                urlc.setConnectTimeout(10 * 1000);
                urlc.connect();
                isReachable = (urlc.getResponseCode() == 200);
            } catch (IOException e) {
                //Log.e(TAG, e.getMessage());
            }
        }

        return isReachable;
    }

    public static Bitmap getVideoFrameFromVideo(String videoPath)  throws Throwable
    {
        Bitmap bitmap = null;
        MediaMetadataRetriever mediaMetadataRetriever = null;
        try
        {
            mediaMetadataRetriever = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mediaMetadataRetriever.setDataSource(videoPath, new HashMap<String, String>());
            else
                mediaMetadataRetriever.setDataSource(videoPath);
            //   mediaMetadataRetriever.setDataSource(videoPath);
            bitmap = mediaMetadataRetriever.getFrameAtTime();
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new Throwable("Exception in getVideoFrameFromVideo(String videoPath)" + e.getMessage());

        }
        finally
        {
            if (mediaMetadataRetriever != null)
            {
                mediaMetadataRetriever.release();
            }
        }
        return bitmap;
    }

    public static Bitmap getFFmpegMediaMetadataRetriever(Context con, String image_url) throws Throwable
    {
        Bitmap b = null;
        FFmpegMediaMetadataRetriever mmr = null;
        try {
            mmr = new FFmpegMediaMetadataRetriever();
            mmr.setDataSource(con, Uri.parse(image_url));
            b = mmr.getFrameAtTime(2000000, FFmpegMediaMetadataRetriever.OPTION_CLOSEST_SYNC);
        }
        catch(Exception e)
        {
            e.printStackTrace();
            throw new Throwable("Exception in getVideoFrameFromVideo(String videoPath)" + e.getMessage());
        }
        finally
        {
            if (mmr != null)
            {
                mmr.release();
            }
        }
        //Log.e(TAG,"bitmap using FFMpeg is "+scaledBitmap);
        Log.e("-------getFFmpegMedia", "Size of NEW bitmap is " + String.valueOf(b.getByteCount()) );
        //holder.Img.setImageBitmap(b);
        return  b;
    }
}
