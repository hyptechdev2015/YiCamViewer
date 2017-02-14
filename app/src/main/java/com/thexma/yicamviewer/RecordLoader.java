package com.thexma.yicamviewer;

import android.net.Network;
import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.TextView;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.io.IOException;
import java.net.URL;

/**
 * Created by kevin on 2/9/2017.
 */


public class RecordLoader extends AsyncTask<String, Void, String> {
    private TextView textView;
    private EditText editText;

    public RecordLoader(TextView textView, EditText editText) {
        this.textView = textView;
        this.editText = editText;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        editText.setText("Loading data...");
    }

    @Override
    protected String doInBackground(String... strings) {
        //String weather = "UNDEFINED";
        StringBuilder builder = new StringBuilder();
        try {

            URL url = new URL(strings[0]);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            InputStream stream = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(stream));


            String inputString;
            while ((inputString = bufferedReader.readLine()) != null) {
                builder.append(inputString);
            }
/*
            JSONObject topLevel = new JSONObject(builder.toString());
            JSONObject main = topLevel.getJSONObject("main");
            weather = String.valueOf(main.getDouble("temp"));
*/
            urlConnection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
        return builder.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        textView.setText("Current Weather: " + s);

        editText.setText("DONE!!!");
    }


}
