package com.thexma.yicamviewer;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.DocumentsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.TextViewCompat;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private VideoView myVideoView;
    private int position = 0;
    private ProgressDialog progressDialog;
    private MediaController mediaControls;
    private String urlRTSP = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //my code
        try {

            SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String url = settings.getString("hostUrl", "");


            if (url.length() > 0)
                LoadRTSP();
            else {
                Intent intRecord = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intRecord);
                finish();
            }

        } catch (Exception ex) {
            Log.e(" KLE Error: ", ex.toString());
        }


    }

    private void LoadRTSP() {
        SharedPreferences settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String url = settings.getString("hostUrl", "none");
        String urlPort = settings.getString("hostPort", "333");
        String urlRTSP = "rtsp://" + url + ":" + urlPort + "/ch0_1.h264";
        //urlRTSP = "rtsp://192.168.29.168:554/ch0_1.h264";

        //SettingsActivity.LoadFromPref();
        //urlRTSP = "rtsp://" + SettingsActivity.HostUrl + ":" + SettingsActivity.HostPort + "/ch0_1.h264";

        TextView etHello = (TextView) findViewById(R.id.textViewHello);
        etHello.setText(urlRTSP);

        //set the media controller buttons
        if (mediaControls == null) {
            mediaControls = new MediaController(this);
        }
        //initialize the VideoView
        myVideoView = (VideoView) findViewById(R.id.videoViewLive);

        // create a progress bar while the video file is loading
        progressDialog = new ProgressDialog(this);
        // set a title for the progress bar
        progressDialog.setTitle("RTSP Stream Channels");
        // set a message for the progress bar
        progressDialog.setMessage(urlRTSP);
        //set the progress bar not cancelable on users' touch
        progressDialog.setCancelable(false);
        // show the progress bar
        progressDialog.show();

        try {
            //set the media controller in the VideoView
            myVideoView.setMediaController(mediaControls);

            //set the uri of the video to be played
            myVideoView.setVideoURI(Uri.parse(urlRTSP));

            myVideoView.requestFocus();

            //we also set an setOnPreparedListener in order to know when the video file is ready for playback

            myVideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {

                public void onPrepared(MediaPlayer mediaPlayer) {
                    // close the progress bar and play the video
                    progressDialog.dismiss();
                    myVideoView.start();
                /*
                //if we have a position on savedInstanceState, the video playback should start from here
                myVideoView.seekTo(position);
                if (position == 0) {
                    myVideoView.start();
                } else {
                    //if we come from a resumed activity, video playback will be paused
                    myVideoView.pause();
                }*/
                }
            });

            myVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
                @Override
                public boolean onError(MediaPlayer mp, int what, int extra) {
                    Log.e(" KLE Error", "STOP");
                    progressDialog.dismiss();
                    myVideoView.stopPlayback();
                    //Toast.makeText(getApplicationContext(), "Can't stream video url", Toast.LENGTH_LONG).show();
                    return false;
                }
            });

        } catch (Exception e) {
            progressDialog.dismiss();
            System.out.println("Video Play Error :"+e.toString());
            Log.e(" KLE Error", e.getMessage());
            //e.printStackTrace();
        }


    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent intRecord = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intRecord);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_records) {
            Intent intRecord = new Intent(getApplicationContext(), RecordActivity.class);
            startActivity(intRecord);
        } else if (id == R.id.nav_camera) {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlRTSP));
            startActivity(intent);
            //finish();
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);


        try {
            savedInstanceState.putString("UrlRTSP", urlRTSP);
            //we use onSaveInstanceState in order to store the video playback position for orientation change
            //savedInstanceState.putInt("Position", myVideoView.getCurrentPosition());
/*            if (myVideoView == null)
                LoadRTSP();
            else
                myVideoView.pause();*/

        } catch (Exception ex) {
            Log.e("KLEonSave: ", ex.toString());
        }

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.e("KLE_restore: ", urlRTSP);

        try {
            //we use onRestoreInstanceState in order to play the video playback from the stored position
            //position = savedInstanceState.getInt("Position");
            //myVideoView.seekTo(position);
/*            if (myVideoView != null)
                myVideoView.start();
            else
                Log.e("KLE_myVideoView: ", "null");*/

 /*           urlRTSP = savedInstanceState.getString("UrlRTSP");
            if(urlRTSP.length() > 0)
                LoadRTSP(urlRTSP);*/

        } catch (Exception ex) {
            Log.e("KLE_onRestore: ", ex.toString());
        }
    }
}
