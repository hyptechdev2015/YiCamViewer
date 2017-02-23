package dll;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

/**
 * Created by kle on 2/15/2017.
 */

public class DatabaseInterface {
    private SQLiteDatabase database;

    //instance
    private RecordDB dbhelper;


    public DatabaseInterface(Context context) {
        //dbhelper = new RecordDB(context);

        dbhelper = RecordDB.getInstance(context);
    }

    public void open() throws SQLException {
        database = dbhelper.getWritableDatabase();
    }

    public void close() {
        dbhelper.close();
    }

    public ArrayList<Record> getRecordList() {
        ArrayList<Record> res = new ArrayList<Record>();
        Cursor cursor = database.query("records", null, null, null, null, null, "id");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Record t = cursorToRecord(cursor);
            res.add(t);
            cursor.moveToNext();
        }
        cursor.close();
        return res;
    }

    public ArrayList<Record> getRecord(int date) {
        ArrayList<Record> res = new ArrayList<Record>();
        Cursor cursor;
        if (date == -1)
            cursor = database.query("records", null, null, null, null, null, "id");
        else
            cursor = database.query("records", null, "date = " + date, null, null, null, "id");
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Record e = cursorToRecord(cursor);
            res.add(e);
            cursor.moveToNext();
        }
        cursor.close();
        return res;
    }

    private Record cursorToRecord(Cursor c) {
        Record t = new Record(c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getString(5), c.getInt(6), c.getBlob(7));
        return t;
    }

    public Record getLastestRecord() {
        Record rec = new Record();
        String selectQuery = "SELECT * FROM records ORDER BY column DESC LIMIT 1;";
        Cursor cursor = database.rawQuery(selectQuery, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            rec = cursorToRecord(cursor);
        }
        cursor.close();
        return rec;

    }

    public boolean updateRecordThumbnail(long rowId, byte[] image)
    {
        ContentValues args = new ContentValues();
        args.put(RecordDB.RECORDS_ID, rowId);
        args.put( RecordDB.RECORDS_THUMBNAIL, image);
        int i =  database.update(RecordDB.TABLE_RECORDS, args, RecordDB.RECORDS_ID + "=" + rowId, null);
        return i > 0;
    }

    public void insertRecord(Record rec) {
        ContentValues values = new ContentValues();
        //Populate Teacher tables
        //values.put("id", 0);
        values.put("foldername", rec.getFolderName());
        values.put("filename", rec.getFileName());
        values.put("filedate", rec.getFileDate());
        values.put("filesize", rec.getFileSize());
        values.put("fullurl", rec.getFullUrl());
        values.put("date", rec.getDate());
        values.put("image_data", rec.getThumbnail());
        database.insert("records", null, values);
        values.clear();

    }

    public void simulateExternalDatabase() {
        ContentValues values = new ContentValues();
        //Populate Teacher tables
        //values.put("id", 0);
        values.put("foldername", "2017Y02M15D16H");
        values.put("filename", "20M00S.mp4");
        values.put("filedate", "15Feb2017 16:21");
        values.put("filesize", "2155868");
        values.put("fullurl", "/record/2017Y02M15D16H/20M00S.mp4");
        values.put("date", 20170215);
        values.put("image_data", "null");
        database.insert("records", null, values);
        values.clear();
        //values.put("id", 1);
        values.put("foldername", "2017Y02M13D18H");
        values.put("filename", "01M00S.mp4");
        values.put("filedate", "13Feb2017 18:02");
        values.put("filesize", "850861");
        values.put("fullurl", "/record/2017Y02M13D18H/01M00S.mp4");
        values.put("date", 20170214);
        values.put("image_data", "null");
        database.insert("records", null, values);
        values.clear();

    }

}


