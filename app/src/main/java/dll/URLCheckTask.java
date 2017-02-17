package dll;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.Toast;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * Created by kle on 2/16/2017.
 */

public class URLCheckTask extends AsyncTask<String, Void, String> {

    private Context context;

    public URLCheckTask(Context ctx) {
        context = ctx;
    }

    @Override
    protected String doInBackground(String... url) {
        boolean reachable = false;

        reachable = Helper.IsReachable(context, url[0]);


        if (reachable)
            return "1";
        else
            return "0";
    }

    @Override
    protected void onPostExecute(String result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);
        if (result.equals("1"))
            Toast.makeText(context.getApplicationContext(), "reachable", Toast.LENGTH_SHORT).show();
        else {
            Toast.makeText(context.getApplicationContext(), "unreachable", Toast.LENGTH_SHORT).show();
        }
    }

}