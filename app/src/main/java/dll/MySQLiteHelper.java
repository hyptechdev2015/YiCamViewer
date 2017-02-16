package dll;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by kle on 2/15/2017.
 */

public class MySQLiteHelper extends SQLiteOpenHelper {

    //Record Table
    public static final String TABLE_RECORDS = "records";
    public static final String RECORDS_ID = "id";
    public static final String RECORDS_FOLDERNAME = "foldername";
    public static final String RECORDS_FILENAME = "filename";
    public static final String RECORDS_FILEDATE = "filedate";
    public static final String RECORDS_FILESIZE = "filesize";
    public static final String RECORDS_FULLURL = "fullurl";
    public static final String RECORDS_DATE = "date";

    private static final String DATABASE_NAME = "db_yicamviewer";
    private static final int DATABASE_VERSION = 3;

    //table creation statements
    private static final String RECORDS_CREATE = "create table " + TABLE_RECORDS + "( " +
            RECORDS_ID + " integer primary key, " + RECORDS_FOLDERNAME + " text not null, "
            + RECORDS_FILENAME + " text not null, " + RECORDS_FILEDATE + " DATETIME not null, " +
            RECORDS_FILESIZE + " text not null, " + RECORDS_FULLURL + " text not null, " +  RECORDS_DATE + " integer not null);";


    public MySQLiteHelper(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(RECORDS_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECORDS);
        onCreate(db);
    }

}
