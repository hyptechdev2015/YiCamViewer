package com.thexma.yicamviewer;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import dll.Record;

public class ParseHTML extends AsyncTask <String, Integer, String> {

    private TextView textView;
    private EditText editText;
    private String baseURL;
    private SimpleDateFormat sdf  = new SimpleDateFormat("yyyyMMdd", Locale.US);
    private GregorianCalendar cal = new GregorianCalendar(TimeZone.getTimeZone("US/Central"));
    private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ddMMMyyyy HH:mm");
    private SimpleDateFormat databaseDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    private ArrayList<Record> recordList = new ArrayList<Record>();

    private ProgressBar progressBar;
    private         int count ;
    private int countMax = 100;

    public  ParseHTML( ProgressBar pro){
        this.progressBar = pro;
    }

/*    public ParseHTML(TextView textView, EditText editText) {
        this.textView = textView;
        this.editText = editText;
        sdf = new SimpleDateFormat("yyyyMMdd", Locale.US);
    }*/

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        progressBar.setVisibility(View.INVISIBLE);
        progressBar.setProgress(0);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        //count =1;
        progressBar.setVisibility(View.VISIBLE);
        //progressBar.setProgress(0);

        Log.wtf("----------","Task Starting...");
    }

    @Override
    protected void onProgressUpdate(Integer... values) {
        Log.d("------------", "Progress Update: " + values[0].toString());

        super.onProgressUpdate(values[0]);
        progressBar.setProgress(values[0]);
    }

    @Override
    protected String  doInBackground(String... params) {
        baseURL = params[0];
        try {
            Document doc = Jsoup.connect(baseURL).get();
            Elements folders = doc.select("PRE > a");
            String fullPath = "";
            String folder = "";
            String folderText = "";
            count = 0;
            countMax = folders.size();
            for (Element item : folders) {
                count += 1;

                float percentage = ((float)count / (float)countMax) * 100;

                publishProgress( new Float( percentage).intValue());

                Log.d("-----------------", "elements are: " + item);
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
                            String mp4FileName = list.substring(9, 19);
                            String mp4NameDateTime = list.substring(75, 90);
                            String fileSize = list.substring(75,list.length()).split("     ")[1].trim();
                            try {

                                mp4DateTime = simpleDateFormat.parse(mp4NameDateTime);
                                //recordList.add(new Record());
                                String fullUrl = folder + "/" + mp4FileName;

                                //Calendar calendar = Calendar.getInstance();
                                //calendar.setTime(mp4DateTime);
                                //cal = new GregorianCalendar(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                                int dateInt = Integer.parseInt(sdf.format(mp4DateTime.getTime()));// cal.getTime()));

                                Record rec = new Record(0,folder,mp4FileName,databaseDateFormat.format(mp4DateTime).toString(), fileSize,fullUrl,dateInt );
                                MainActivity.datasource.insertRecord(rec);

                            } catch (ParseException ex) {
                                Log.e("-----------------: ", ex.toString());
                                System.out.println("Exception " + ex);
                            }
                        }
                    }
                }

            }

            String st = "";
        } catch (IOException e) {
            Log.e( " kekekekL", e.toString());
            e.printStackTrace();
        }

        return "Task Completed.";
    }

}
