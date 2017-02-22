package com.thexma.yicamviewer;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import dll.*;

public class RecordActivity extends AppCompatActivity  {
   // public static DatabaseInterface datasource;

    final String TAG = "--- MainActivity";

    public SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.US);
    public GregorianCalendar cal = new GregorianCalendar(TimeZone.getTimeZone("US/Central"));
    public ArrayList<Record> eventList = new ArrayList<Record>();
    private SharedPreferences settings;
    private ListView listview;
    private ListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        //datasource = new DatabaseInterface(this);
       // datasource.open();

        settings = getSharedPreferences(SettingsActivity.PREFS_NAME, MODE_PRIVATE);
        //"http://192.168.29.168/record/"
        String url = "http://" + settings.getString("HostUrl", "") + "/record/";
        String urlPort = settings.getString("HostPort", "333");

        //new RecordLoader(null, null).execute(url);
        //new ParseHTML().execute(url);

/*        eText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(), "datepicker");
            }
        });*/

        //CalendarView load today
        Calendar calendarMin = Calendar.getInstance();
        Calendar calendarMax = Calendar.getInstance();
        calendarMin.add(Calendar.MONTH, -3); // * - subtract one day, i.e. March 7
        calendarMax.add(Calendar.DAY_OF_MONTH, 1);
        final CalendarView c = (CalendarView) findViewById(R.id.calendarViewRecord);
        c.setMinDate(calendarMin.getTimeInMillis());
        c.setMaxDate(calendarMax.getTimeInMillis());

        cal.setTimeInMillis(c.getDate());
        eventList = MainActivity.datasource.getRecord(Integer.parseInt(sdf.format(cal.getTime())));
        adapter = new RecordListAdapter(getApplicationContext(), R.layout.notification_list_item, eventList);
        listview = (ListView) findViewById(R.id.eventListRecord);
        listview.setAdapter(adapter);

        c.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month, int dayOfMonth) {
                try {

                    cal = new GregorianCalendar(year, month, dayOfMonth);
                    int date = Integer.parseInt(sdf.format(cal.getTime()));

                    eventList =  MainActivity.datasource.getRecord(date);
                    adapter = new RecordListAdapter(getApplicationContext(), R.layout.notification_list_item, eventList);
                    listview = (ListView) findViewById(R.id.eventListRecord);
                    listview.setAdapter(adapter);


//                    String str = "day: " + dayOfMonth + " month: " + month + " year: " + year + " Integer: " + Integer.parseInt(sdf.format(cal.getTime()));
                    //Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
                } catch (Exception ex) {
                    Log.e(" here: ", ex.toString());
                }

            }
        });

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Record recSelected = (Record) adapter.getItem(position);
                Log.d("**********", recSelected.toString());
                //Toast.makeText(getApplicationContext(), str, Toast.LENGTH_SHORT).show();
                String urlRTSP = MainActivity.HTTP_URL + recSelected.getFullUrl();
                Log.d("**********", urlRTSP.toString());
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(urlRTSP));
                startActivity(intent);

            }
        });


/*        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapter, View v, int position,
                                    long arg3) {
                String value = (String) adapter.getItemAtPosition(position);
                // assuming string and if you want to get the value on click of list item
                // do what you intend to do on click of listview row

            }
        });*/

    }

    public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int day) {
            String years = "" + year;
            String months = "" + (monthOfYear + 1);
            String days = "" + day;
            if (monthOfYear >= 0 && monthOfYear < 9) {
                months = "0" + (monthOfYear + 1);
            }
            if (day > 0 && day < 10) {
                days = "0" + day;

            }
            //twRecord.setText(days + "/" + months + "/" + years);


        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            //use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), this, year, month, day);
            //datePickerDialog.setButton(DatePickerDialog.BUTTON_POSITIVE, "Button Text", datePickerDialog);

            datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());

            return datePickerDialog;
        }

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

/*    public class RecordDateFragment extends DialogFragment implements CalendarView.OnDateChangeListener{
        @Override
        public void onSelectedDayChange(CalendarView view, int year, int monthOfYear, int day) {
            Log.d("tag", "finally found the listener, the date is: year " + year );
            String years = "" + year;
            String months = "" + (monthOfYear + 1);
            String days = "" + day;
            if (monthOfYear >= 0 && monthOfYear < 9) {
                months = "0" + (monthOfYear + 1);
            }
            if (day > 0 && day < 10) {
                days = "0" + day;

            }
            twRecord.setText(days + "/" + months + "/" + years);
        }

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            CalendarView cv = new CalendarView(getActivity());

            return cvsuper.onCreateDialog(savedInstanceState);
        }
    }*/

}





