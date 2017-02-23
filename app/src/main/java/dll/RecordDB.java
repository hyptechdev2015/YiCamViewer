package dll;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;

import com.thexma.yicamviewer.provider.RecordContract;

/**
 * Created by kle on 2/15/2017.
 */

public class RecordDB extends SQLiteOpenHelper {
    //Record Table
    public static final String TABLE_RECORDS = RecordContract.RecordEntry.TABLE_NAME;
    public static final String RECORDS_ID = "id";
    public static final String RECORDS_FOLDERNAME = "foldername";
    public static final String RECORDS_FILENAME = "filename";
    public static final String RECORDS_FILEDATE = "filedate";
    public static final String RECORDS_FILESIZE = "filesize";
    public static final String RECORDS_FULLURL = "fullurl";
    public static final String RECORDS_DATE = "date";
    public static final String RECORDS_THUMBNAIL = "image_data";


    private static RecordDB sInstance;
    private static final String DATABASE_NAME = "db_yicamviewer.db";
    private static final int DATABASE_VERSION = 3;

    //table creation statements
    private static final String RECORDS_CREATE = "create table " + TABLE_RECORDS + "( " +
            RecordContract.RecordEntry._ID + " INTEGER primary key AUTOINCREMENT, " +
            RECORDS_ID + " INTEGER not null, " + RECORDS_FOLDERNAME + " text  null, "
            + RECORDS_FILENAME + " text  null, " + RECORDS_FILEDATE + " DATETIME  null, " +
            RECORDS_FILESIZE + " text  null, " + RECORDS_FULLURL + " text  null, " +  RECORDS_DATE + " integer  null," +  RECORDS_THUMBNAIL + " BLOB null);";


    public static synchronized RecordDB getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new RecordDB(context.getApplicationContext());
        }
        return sInstance;
    }

    /**
     * Constructor should be private to prevent direct instantiation.
     * make call to static method "getInstance()" instead.
     */
    private RecordDB(Context context){
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
