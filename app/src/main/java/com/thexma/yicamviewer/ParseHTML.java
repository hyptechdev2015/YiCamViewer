package com.thexma.yicamviewer;

import android.os.AsyncTask;
import android.widget.EditText;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import java.io.IOException;

public class ParseHTML extends AsyncTask<String, Void, Void> {

    private TextView textView;
    private EditText editText;

    public ParseHTML(TextView textView, EditText editText) {
        this.textView = textView;
        this.editText = editText;
    }

    @Override
    protected Void doInBackground(String... params) {

        try {
            Document doc = Jsoup.connect(params[0]).get();
            Elements folder = doc.select("PRE > a");

            String st = "";
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }
}
