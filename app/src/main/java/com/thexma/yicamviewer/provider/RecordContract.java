package com.thexma.yicamviewer.provider;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by kle on 2/22/2017.
 */

public class RecordContract {

    public RecordContract(){}
    /**
     * Content provider authority.
     */
    public static final String CONTENT_AUTHORITY = "com.thexma.yicamviewer";

    /**
     * Base URI. (content://com.example.android.basicsyncadapter)
     */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    /**
     * Path component for "entry"-type resources..
     */
    private static final String PATH_ENTRIES = "records";

    /**
     * Columns supported by "entries" records.
     */
    public static class RecordEntry implements BaseColumns {
        /**
         * MIME type for lists of entries.
         */
        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/vnd.yicamviewer.records";
        /**
         * MIME type for individual entries.
         */
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE + "/vnd.yicamviewer.record";

        /**
         * Fully qualified URI for "entry" resources.
         */
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_ENTRIES).build();

        public static final String TABLE_NAME = "tbl_records";

        public static final String RECORDS_ID = "id";
        public static final String RECORDS_FOLDERNAME = "foldername";
        public static final String RECORDS_FILENAME = "filename";
        public static final String RECORDS_FILEDATE = "filedate";
        public static final String RECORDS_FILESIZE = "filesize";
        public static final String RECORDS_FULLURL = "fullurl";
        public static final String RECORDS_DATE = "date";
        public static final String RECORDS_THUMBNAIL = "image_data";

    }

}
