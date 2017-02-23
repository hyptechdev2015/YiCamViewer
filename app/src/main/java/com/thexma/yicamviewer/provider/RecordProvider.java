package com.thexma.yicamviewer.provider;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.text.TextUtils;

import dll.RecordDB;
import dll.SelectionBuilder;

/**
 * Created by kevin on 2/22/2017.
 */

public class RecordProvider extends ContentProvider {

    /**
     * Content authority for this provider.
     */
    private static final String AUTHORITY = RecordContract.CONTENT_AUTHORITY;

    // The constants below represent individual URI routes, as IDs. Every URI pattern recognized by
    // this ContentProvider is defined using sUriMatcher.addURI(), and associated with one of these
    // IDs.
    //
    // When a incoming URI is run through sUriMatcher, it will be tested against the defined
    // URI patterns, and the corresponding route ID will be returned.
    /**
     * URI ID for route: /entries
     */
    public static final int PATH_TOKEN = 1;

    /**
     * URI ID for route: /entries/{ID}
     */
    public static final int PATH_FOR_ID_TOKEN = 2;

    /**
     * UriMatcher, used to decode incoming URIs.
     */
    private static final UriMatcher URI_MATCHER = new UriMatcher(UriMatcher.NO_MATCH);

    static {
        URI_MATCHER.addURI(AUTHORITY, "records", PATH_TOKEN);
        URI_MATCHER.addURI(AUTHORITY, "records/*", PATH_FOR_ID_TOKEN);
    }

    RecordDB dbHelper;

    @Override
    public boolean onCreate() {
        Context ctx = getContext();

        dbHelper = RecordDB.getInstance(ctx);
        return false;
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        final int match = URI_MATCHER.match(uri);
        switch (match) {
            // retrieve tv shows list
            case PATH_TOKEN: {
                SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
                builder.setTables( RecordContract.RecordEntry.TABLE_NAME);
                return builder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
            }
            case PATH_FOR_ID_TOKEN: {
                //String id = uri.getLastPathSegment();
                //builder.where(FeedContract.Entry._ID + "=?", id);

                int id = (int) ContentUris.parseId(uri);
                SQLiteQueryBuilder builder = new SQLiteQueryBuilder();
                builder.setTables( RecordContract.RecordEntry.TABLE_NAME);
                builder.appendWhere(RecordContract.RecordEntry._ID + "=" + id);
                return builder.query(db, projection, selection,selectionArgs, null, null, sortOrder);
            }
            default:
                return null;
        }
    }

    @Nullable
    @Override
    public String getType(Uri uri) {

        final int match = URI_MATCHER.match(uri);
        switch (match) {
            case PATH_TOKEN:
                return RecordContract.RecordEntry.CONTENT_TYPE;
            case PATH_FOR_ID_TOKEN:
                return RecordContract.RecordEntry.CONTENT_ITEM_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        assert db != null;
        final int match = URI_MATCHER.match(uri);
        Uri result;
        switch (match) {
            case PATH_TOKEN:
                long id = db.insertOrThrow(RecordContract.RecordEntry.TABLE_NAME, null, values);
                result = Uri.parse(RecordContract.RecordEntry.CONTENT_URI + "/" + id);
                break;
            case PATH_FOR_ID_TOKEN:
                throw new UnsupportedOperationException("Insert not supported on URI: " + uri);
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        // Send broadcast to registered ContentObservers, to refresh UI.
        Context ctx = getContext();
        assert ctx != null;
        ctx.getContentResolver().notifyChange(uri, null, false);
        return result;

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        int token = URI_MATCHER.match(uri);
        int rowsDeleted = -1;
        switch (token) {
            case (PATH_TOKEN):
                rowsDeleted = db.delete(RecordContract.RecordEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case (PATH_FOR_ID_TOKEN):
                String tvShowIdWhereClause = RecordContract.RecordEntry._ID + "=" + uri.getLastPathSegment();
                if (!TextUtils.isEmpty(selection))
                    tvShowIdWhereClause += " AND " + selection;
                rowsDeleted = db.delete(RecordContract.RecordEntry.TABLE_NAME, tvShowIdWhereClause, selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
        // Notifying the changes, if there are any
        if (rowsDeleted != -1)
            getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;

    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        SelectionBuilder builder = new SelectionBuilder();
        final SQLiteDatabase db = dbHelper.getWritableDatabase();
        final int match = URI_MATCHER.match(uri);
        int count;
        switch (match) {
            case PATH_TOKEN:
                count = builder.table(RecordContract.RecordEntry.TABLE_NAME)
                        .where(selection, selectionArgs)
                        .update(db, values);
                break;
            case PATH_FOR_ID_TOKEN:
                String id = uri.getLastPathSegment();
                count = builder.table(RecordContract.RecordEntry.TABLE_NAME)
                        .where(RecordContract.RecordEntry._ID + "=?", id)
                        .where(selection, selectionArgs)
                        .update(db, values);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        Context ctx = getContext();
        assert ctx != null;
        ctx.getContentResolver().notifyChange(uri, null, false);
        return count;
    }
}
