package com.thexma.yicamviewer;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

public class RecordActivity extends AppCompatActivity {
    private EditText eText;
    static TextView twRecord;
    private Context mContext;

    //public ArrayList<Event> eventList = new ArrayList<Event>();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.US);
    GregorianCalendar cal = new GregorianCalendar(TimeZone.getTimeZone("US/Central"));

    public ArrayList<Record> eventList = new ArrayList<Record>();

    public RecordActivity(Context context) {
        this.mContext = context;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record);

        eText = (EditText) findViewById(R.id.editTextRecord);
        twRecord = (TextView) findViewById(R.id.textViewRecord);

        SharedPreferences settings = getSharedPreferences(SettingsActivity.PREFS_NAME.toString(), 0);
        //"http://192.168.29.168/record/"
        String url = "http://" + settings.getString("hostUrl", "") + "/record/";
        String urlPort = settings.getString("hostPort", "333");

        //new RecordLoader(twRecord, eText).execute(url);
        //new ParseHTML(twRecord, eText).execute(url);

/*        eText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new DatePickerFragment();
                newFragment.show(getFragmentManager(), "datepicker");
            }
        });*/

        //CalendarView
        final CalendarView c = (CalendarView) findViewById(R.id.calendarViewRecord);
        cal.setTimeInMillis(c.getDate());
        //eventList = MainActivity.datasource.getEvent(Integer.parseInt(sdf.format(cal.getTime())));

        //istAdapter adapter = new EventListAdapter(getActivity(), R.layout.notification_list_item, eventList);
        //ListView listview = (ListView)findViewById(R.id.eventListRecord);
        //listview.setAdapter(adapter);

        c.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,int dayOfMonth) {
                cal.setTimeInMillis(c.getDate());
                eventList = MainActivity.datasource.getRecord(Integer.parseInt(sdf.format(cal.getTime())));
                ListAdapter adapter = new RecordListAdapter(   mContext  , R.layout.notification_list_item, eventList);
                ListView listview = (ListView)findViewById(R.id.eventListRecord);
                listview.setAdapter(adapter);

                String str = "day: " + dayOfMonth + " month: " + month + " year: " + year;
                Toast.makeText(getApplicationContext(), str , Toast.LENGTH_SHORT).show();
            }
        });



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
            twRecord.setText(days + "/" + months + "/" + years);


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





