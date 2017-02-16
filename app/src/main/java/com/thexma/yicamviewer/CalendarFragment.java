package com.thexma.yicamviewer;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TimeZone;

import  dll.*;

/**
 * A simple {@link Fragment} subclass.
 */
public class CalendarFragment extends Fragment {

    public ArrayList<Record> eventList = new ArrayList<Record>();
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd", Locale.US);
    GregorianCalendar cal = new GregorianCalendar(TimeZone.getTimeZone("US/Central"));

    public CalendarFragment() {

    }

    public static Fragment newInstance(Context context) {
        CalendarFragment f = new CalendarFragment();
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final ViewGroup root = (ViewGroup) inflater.inflate(R.layout.activity_record, null);
        final CalendarView c = (CalendarView) root.findViewById(R.id.calendarViewRecord);
        cal.setTimeInMillis(c.getDate());
        eventList = MainActivity.datasource.getRecord(Integer.parseInt(sdf.format(cal.getTime())));

        ListAdapter adapter = new RecordListAdapter(getActivity(), R.layout.notification_list_item, eventList);
        ListView listview = (ListView)root.findViewById(R.id.eventListRecord);
        listview.setAdapter(adapter);

        c.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,int dayOfMonth) {
                cal.setTimeInMillis(c.getDate());
                eventList = MainActivity.datasource.getRecord(Integer.parseInt(sdf.format(cal.getTime())));

                ListAdapter adapter = new RecordListAdapter(getActivity(), R.layout.notification_list_item, eventList);
                ListView listview = (ListView)root.findViewById(R.id.eventListRecord);
                listview.setAdapter(adapter);
            }
        });
        return root;
    }

}
