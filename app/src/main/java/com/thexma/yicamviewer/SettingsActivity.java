package com.thexma.yicamviewer;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.support.annotation.IdRes;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {


    public  final static    String PREFS_NAME = "MyPrefsFile";
    public static String HostUrl;
    public static String HostPort;

    private static EditText tV;
    private static EditText tvPort;
    //saving the context, so that we can call all
    //shared pref methods from non activity classes.
    //because getSharedPreferences required the context.
    //but in activity class we can call without this context
    private static Context 	mContext;

    public static void Init(Context context)
    {
        mContext 		= context;
    }

    public static void LoadFromPref()
    {
        SharedPreferences settings 	= mContext.getSharedPreferences(PREFS_NAME, 0);
        // Note here the 2nd parameter 0 is the default parameter for private access,
        //Operating mode. Use 0 or MODE_PRIVATE for the default operation,
        HostUrl 			= settings.getString("HostUrl","192.168.29.168"); // 1st parameter Name is the key and 2nd parameter is the default if data not found
        HostPort 			= settings.getString("HostPort","554");
    }

    public static void StoreToPref()
    {
        // get the existing preference file
        SharedPreferences settings = mContext.getSharedPreferences(PREFS_NAME, 0);
        //need an editor to edit and save values
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("HostUrl",tV.getText().toString()); // Name is the key and mName is holding the value
        editor.putString("HostPort",tvPort.getText().toString());// EmpID is the key and mEid is holding the value

        //final step to commit (save)the changes in to the shared pref
        editor.commit();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        // Restore preferences
        //PREFS_NAME = getResources().getString(R.string.pref_file);

        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String urlRemote = "192.168.29.168";        
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
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("hostUrl", tV.getText().toString());
        editor.putString("hostPort", tvPort.getText().toString());

        // Commit the edits!
        editor.commit();

        Intent intRecord = new Intent(getApplicationContext(), MainActivity.class);
        startActivity(intRecord);
        finish();
    }
}
