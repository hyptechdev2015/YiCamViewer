package com.thexma.yicamviewer;

import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;

public class RecordActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        EditText eText = (EditText)findViewById(R.id.editTextRecord);
        TextView twRecord = (TextView) findViewById(R.id.textViewRecord);

        SharedPreferences settings = getSharedPreferences(SettingsActivity.PREFS_NAME.toString() , 0);
        String url = settings.getString("hostUrl", "http://192.168.29.168/record/default/");

        new RecordLoader(twRecord, eText).execute(url);



    }
}
