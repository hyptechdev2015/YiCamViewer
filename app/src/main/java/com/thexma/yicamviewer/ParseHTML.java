package com.thexma.yicamviewer;
import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class ParseHTML extends AsyncTask<String,Void, Void>{

    @Override
    protected Void doInBackground(String... params) {

        try {
            Document doc = Jsoup.connect("http://192.168.29.168/record/").get();
            String st = "";
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
