package com.thexma.yicamviewer;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ParseHTML extends AsyncTask<String, Void, Void> {

    private TextView textView;
    private EditText editText;
    private String baseURL;

    public ParseHTML(TextView textView, EditText editText) {
        this.textView = textView;
        this.editText = editText;
    }

    @Override
    protected Void doInBackground(String... params) {
        baseURL = params[0];
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMMyyyy HH:mm");

        try {
            Document doc = Jsoup.connect(baseURL).get();
            Elements folders = doc.select("PRE > a");
            String fullPath = "";
            String folder = "";
            String folderText = "";
            for (Element item : folders) {
                Log.d(" debug", "elements are: " + item);
                folder = item.attr("href");
                folderText = item.text();
                if (folder.length() > 3) {
                    fullPath = baseURL + folder + "/";
                    Document docSub = Jsoup.connect(fullPath).get();
                    String mp4Path = "";
                    String mp4Name = "";
                    Date mp4DateTime = null;
                    /*
                    Elements foldersSub = docSub.select("PRE > a");
                    for (Element itemSub : foldersSub) {
                        if (itemSub.attr("href").length() > 3) {
                            mp4Path = fullPath + "/" + itemSub.attr("href");
                        }
                    }*/
                    //split method
                    String[] lists = docSub.outerHtml().split("\n");
                    for (String list : lists) {
                        if (list.length() == 105 && list.indexOf("mp4") > 0) {
                            mp4Name = list.substring(75, 90);
                            try {
                                mp4DateTime = simpleDateFormat.parse(mp4Name);
                            } catch (ParseException ex) {
                                System.out.println("Exception " + ex);
                            }
                        }
                    }
                }

            }

            String st = "";
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

}
