package com.thexma.yicamviewer;

import android.content.SharedPreferences;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {


    public  final static    String PREFS_NAME = "MyPrefsFile";
    private EditText tV;
    private EditText tvPort;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Restore preferences
        //PREFS_NAME = getResources().getString(R.string.pref_file);

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        //String urlRemote = "192.168.29.168";
        String urlRemote = "hyptech.asuscomm.com";
        String url = settings.getString("hostUrl", urlRemote);
        tV = (EditText) findViewById(R.id.editTextHostUrl);
        tV.setText(url);
        String urlPort = settings.getString("hostPort", "554");
        tvPort = (EditText) findViewById(R.id.editTextPort);
        tvPort.setText(urlPort);

    }

    @Override
    protected void onStop() {
        super.onStop();

        // We need an Editor object to make preference changes.
        // All objects are from android.context.Context
        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("hostUrl", tV.getText().toString());
        editor.putString("hostPort", tvPort.getText().toString());

        // Commit the edits!
        editor.commit();

    }
}
