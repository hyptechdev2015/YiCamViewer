package com.thexma.yicamviewer;

import android.accounts.Account;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.thexma.yicamviewer.adapter.SyncService;
import com.thexma.yicamviewer.adapter.SyncUtils;
import com.thexma.yicamviewer.common.accounts.GenericAccountService;

import dll.DatabaseInterface;
import dll.Helper;
import dll.URLCheckTask;
import dll.WebService;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    final String TAG = "--- MainActivity";
    public static DatabaseInterface datasource;
    public static SharedPreferences settings;

    public static String HTTP_HOST;
    public static String HTTP_URL;
    public static String RSTP_URL;
    public static String HTTP_RECORD_URL;
    private VideoView myVideoView;
    private int position = 0;
    private ProgressDialog progressDialog;
    private MediaController mediaControls;
    private ProgressBar progressBar;
    private int count = 1;

    //task
    private ParseHTML htmlTask;

    private String reachable = "0";

    private String finalS = "";
    private Toolbar toolbar;

    private Account mConnectedAccount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
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
        //mConnectedAccount = GenericAccountService.GetAccount(SyncUtils.ACCOUNT_TYPE);
        SyncUtils.CreateSyncAccount(getApplicationContext());

        findViewById(R.id.buttonSync).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
/*                if (mConnectedAccount == null) {
                    Toast.makeText(MainActivity.this, "Connect first time", Toast.LENGTH_SHORT).show();
                    SyncUtils.CreateSyncAccount(getApplicationContext());
                    //return;
                } else*/
                    SyncUtils.TriggerRefresh();

            }
        });
        try {

            //PreferenceManager.setDefaultValues(this, R.xml.app_references, false);

            //settings = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            settings = getSharedPreferences(SettingsActivity.PREFS_NAME.toString(), MODE_PRIVATE);
            //datasoure
            datasource = new DatabaseInterface(this);
            datasource.open();
/*            if (!settings.getBoolean("firstTime", false)) {
                datasource.simulateExternalDatabase();
                SharedPreferences.Editor editor = settings.edit();
                editor.putBoolean("firstTime", true);
                editor.commit();
            }*/


            String host;//= settings.getString("HostUrl", "");

            SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this);
            host = sharedPref.getString("host", "");

            String urlPort;// = settings.getString("HostPort", "");
            urlPort = sharedPref.getString("rtsp_port", "");
            HTTP_HOST = "http://" + host;
            RSTP_URL = "rtsp://" + host + ":" + urlPort + "/ch0_1.h264";
            HTTP_URL = "http://" + host + "/" + sharedPref.getString("record_path", "") + "/";
            HTTP_RECORD_URL = "http://" + host + "/" + sharedPref.getString("record_path", "") + "/";


            try {
                reachable = new URLCheckTask(getApplicationContext()).execute(HTTP_URL).get();
            } catch (Exception e) {
                Log.e("-------------", e.getMessage());
            }
            if (reachable == "1")
                Toast.makeText(this, "Reachable", Toast.LENGTH_SHORT).show();
            else {
                Toast.makeText(this, "Unreachable", Toast.LENGTH_SHORT).show();
                return;
            }

            if (host.length() > 0) {


                TextView etHello = (TextView) findViewById(R.id.textViewHello);

                Log.d("---------------RSTP_URL", RSTP_URL.toString());

                //LoadHTML();
                //LoadRTSP();
                //loadRTSPPreview();
                //testWebService();
                Log.d("---------------HTTP_URL", HTTP_URL.toString());

                etHello.setText(finalS);
                Log.d("---------------2", finalS.toString());

            } else {
/*                Intent intRecord = new Intent(getApplicationContext(), SettingsActivity.class);
                startActivity(intRecord);*/

                loadSettingFragment();
                //finish();

            }


        } catch (Exception ex) {
            Log.e(" ---------: ", ex.toString());
        }


    }


    private void testWebService() {

        WebService s = new WebService(getApplicationContext(), HTTP_URL, new WebService.OnTaskDoneListener() {
            @Override
            public void onTaskDone(String responseData) {

                String s1 = "";
                finalS = responseData.toString();
                Log.d("----------1", finalS.toString());
                String s2 = "";
            }

            @Override
            public void onError() {

            }
        });
        s.execute();

    }

    private void LoadHTML() {
        if (reachable == "0") {
            Toast.makeText(getApplicationContext(), "LoadHTML Not Reachable", Toast.LENGTH_SHORT).show();
            return;
        }
        //progress bar
        progressBar = (ProgressBar) findViewById(R.id.progressBarTask);
        progressBar.setMax(100);

        //progressBar.setVisibility(View.VISIBLE);
        //progressBar.setProgress(0);

        //load data to datasource
        htmlTask = new ParseHTML(progressBar);
        htmlTask.execute(HTTP_URL);
    }

    private void loadRTSPPreview() {

        if (reachable == "0") {
            Toast.makeText(getApplicationContext(), "Ureachable", Toast.LENGTH_LONG).show();
            return;
        }
        //initialize the VideoView
        myVideoView = (VideoView) findViewById(R.id.videoViewLive);
        Bitmap thumbnail = null;
        try {

            thumbnail = Helper.getVideoFrameFromVideo(RSTP_URL);

            Thread.sleep(3000);
            BitmapDrawable bitmapDrawable = new BitmapDrawable(thumbnail);
            myVideoView.setBackground(bitmapDrawable);
            myVideoView.seekTo(0);
        } catch (Exception ex) {

            Log.e("-----------", ex.toString());
        } catch (Throwable t) {

            Log.e("-----------", t.toString());
        }
    }

    private void stopRTSP() {
        if (progressDialog != null)
            progressDialog.dismiss();
        if (myVideoView != null)
            myVideoView.stopPlayback();
        Log.i(TAG, "stop LoadRTSP");
    }

    private void LoadRTSP() {

        if (reachable == "0") {
            Toast.makeText(getApplicationContext(), "URL Not Reachable", Toast.LENGTH_LONG).show();
            return;
        }
        Log.i(TAG, "resume/start LoadRTSP");

        //set the media controller buttons
        if (mediaControls == null) {
            mediaControls = new MediaController(this);
        }
        //initialize the VideoView
        if (myVideoView == null)
            myVideoView = (VideoView) findViewById(R.id.videoViewLive);

        // create a progress bar while the video file is loading
        if (progressDialog == null)
            progressDialog = new ProgressDialog(this);
        // set a title for the progress bar
        progressDialog.setTitle("RTSP Stream Channels");
        // set a message for the progress bar
        progressDialog.setMessage(RSTP_URL);
        //set the progress bar not cancelable on users' touch
        progressDialog.setCancelable(false);
        // show the progress bar
        progressDialog.show();

        try {
            //set the media controller in the VideoView
            myVideoView.setMediaController(mediaControls);

            //set the uri of the video to be played
            myVideoView.setVideoURI(Uri.parse(RSTP_URL));

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
                    Log.e(" ---------------", "STOP:" + RSTP_URL);
                    progressDialog.dismiss();
                    myVideoView.stopPlayback();
                    //Toast.makeText(getApplicationContext(), "Can't stream video url", Toast.LENGTH_LONG).show();
                    return false;
                }
            });


        } catch (Exception e) {
            progressDialog.dismiss();
            myVideoView.stopPlayback();
            System.out.println("Video Play Error :" + e.toString());
            Log.e(" ------------", e.getMessage().toString());
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
/*            Intent intRecord = new Intent(getApplicationContext(), SettingsActivity.class);
            startActivity(intRecord);
            //finish();*/

            loadSettingFragment();

        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        stopRTSP();

        if (id == R.id.nav_records) {

            Intent intRecord = new Intent(getApplicationContext(), RecordActivity.class);
            startActivity(intRecord);

/*            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //CalendarFragment frag = new CalendarFragment();
            fragmentTransaction.add(R.id.activity_record, CalendarFragment.newInstance(this.getApplicationContext()) , "Reocords ");
            fragmentTransaction.commit();*/


        } else if (id == R.id.nav_camera) {

            if (myVideoView != null)
                myVideoView.pause();
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(RSTP_URL));
            startActivity(intent);

            //finish();
        } else if (id == R.id.nav_gallery) {

            fragment = new EmptyFragment();
            toolbar.setTitle("Gallery");
            showFragment(fragment);

        } else if (id == R.id.nav_slideshow) {
            Intent actPlayer = new Intent(getApplicationContext(), PlayerActivity.class);
            startActivity(actPlayer);

        } else if (id == R.id.nav_share) {

            Intent intTest = new Intent(getApplicationContext(), TestFMMRActivity.class);
            startActivity(intTest);

        } else if (id == R.id.nav_send) {

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;

    }

    private void loadSettingFragment() {
        getFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, new SettingsFragment()) //android.R.id.content
                .addToBackStack(null)
                .commit();
    }

    private void showFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack so the user can navigate back
        transaction.replace(R.id.fragment_container, fragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);


        try {
            if (htmlTask != null)
                htmlTask.cancel(true);
            savedInstanceState.putString("HTTP_URL", HTTP_URL + "");
            //we use onSaveInstanceState in order to store the video playback position for orientation change
            //savedInstanceState.putInt("Position", myVideoView.getCurrentPosition());
/*            if (myVideoView == null)
                LoadRTSP();
            else
                myVideoView.pause();*/

        } catch (Exception ex) {
            Log.e("--------: ", ex.toString());
        }

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Log.e("KLE_restore: ", HTTP_URL);

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

    @Override
    protected void onStop() {
        if (htmlTask != null)
            htmlTask.cancel(true);
        super.onStop();
        stopRTSP();
    }

    @Override
    protected void onResume() {
        super.onResume();
        LoadRTSP();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

}
