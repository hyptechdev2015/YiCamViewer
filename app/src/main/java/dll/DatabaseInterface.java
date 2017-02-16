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
    private MySQLiteHelper dbhelper;

    public DatabaseInterface(Context context){
        dbhelper = new MySQLiteHelper(context);
    }

    public void open() throws SQLException {
        database = dbhelper.getWritableDatabase();
    }

    public void close() {
        dbhelper.close();
    }

    public ArrayList<Record> getRecordList(){
        ArrayList<Record> res = new ArrayList<Record>();
        Cursor cursor = database.query("records", null, null, null, null, null, "id");
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            Record t = cursorToRecord(cursor);
            res.add(t);
            cursor.moveToNext();
        }
        cursor.close();
        return res;
    }
    public ArrayList<Record> getRecord(int date){
        ArrayList<Record> res = new ArrayList<Record>();

        Cursor cursor = database.query("records", null, "date = " + date, null, null, null, "id");
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            Record e = cursorToRecord(cursor);
            res.add(e);
            cursor.moveToNext();
        }
        cursor.close();
        return res;
    }

    private Record cursorToRecord(Cursor c){
        Record t = new Record(c.getInt(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getString(5), c.getInt(6));
        return t;
    }

    public void simulateExternalDatabase(){
        ContentValues values = new ContentValues();
        //Populate Teacher tables
        values.put("id", 0);
        values.put("foldername", "2017Y02M15D16H");
        values.put("filename", "20M00S.mp4");
        values.put("filedate", "15Feb2017 16:21");
        values.put("filesize", "2155868");
        values.put("fullurl", "/record/2017Y02M15D16H/20M00S.mp4");
        values.put("date", 20170215);
        database.insert("records", null, values);
        values.clear();
        values.put("id", 1);
        values.put("foldername", "2017Y02M13D18H");
        values.put("filename", "01M00S.mp4");
        values.put("filedate", "13Feb2017 18:02");
        values.put("filesize", "850861");
        values.put("fullurl", "/record/2017Y02M13D18H/01M00S.mp4");
        values.put("date", 20170214);
        database.insert("records", null, values);
        values.clear();

    }

}


