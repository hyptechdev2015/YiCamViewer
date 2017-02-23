package com.thexma.yicamviewer.net;

import android.util.Log;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


import dll.Record;

/**
 * Created by kle on 2/22/2017.
 */

public class RecordParser {

    public static final String TAG = "--- RecordParser";

    private String stringFromStream;
    private List<Record> recordList;


    private SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.US);
    private SimpleDateFormat sdfUnique = new SimpleDateFormat("yyyyMMddHHmm", Locale.US);
    //    private GregorianCalendar cal = new GregorianCalendar(TimeZone.getTimeZone("US/Central"));
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMMyyyy HH:mm");
    private SimpleDateFormat databaseDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public List<Record> parse(String url) throws XmlPullParserException, IOException, ParseException {
        recordList = new ArrayList<Record>();

        int uniquDateID;
        int dateIntSearch;
        String fullPath = "";
        String folder = "";
        String folderText = "";
        Document doc = null;
        Document docSub = null;


        try {
            doc = Jsoup.connect(url).get();
            Log.i(TAG, "started");
            Elements folders = doc.select("PRE > a");


            for (Element item : folders) {

                //Log.d("-----------------", "elements are: " + item);
                folder = item.attr("href");
                folderText = item.text();
                if (folder.length() > 3) {
                    fullPath = url + folder + "/";
                    docSub = Jsoup.connect(fullPath).get();
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
                            String mp4FileName = list.substring(9, 19);
                            String mp4NameDateTime = list.substring(75, 90);
                            String fileSize = list.substring(75, list.length()).split("     ")[1].trim();
                            try {

                                mp4DateTime = simpleDateFormat.parse(mp4NameDateTime);

                                String fullUrl = url + folder + "/" + mp4FileName;

                                dateIntSearch = Integer.parseInt(sdf.format(mp4DateTime.getTime()));
                                //id
                                uniquDateID = Integer.parseInt(sdfUnique.format(mp4DateTime.getTime()));

                                Record rec = new Record(uniquDateID, folder, mp4FileName, databaseDateFormat.format(mp4DateTime).toString(), fileSize, fullUrl, dateIntSearch, null);
                                recordList.add(rec);

                            } catch (ParseException ex) {
                                Log.e(TAG, ex.toString());
//                                System.out.println("Exception " + ex);
                            }
                        }
                    }

                }

            }

        } finally {
            if (docSub != null)
                docSub.remove();
            if (doc != null)
                doc.remove();

        }
        return recordList;
    }
}
