package com.thexma.yicamviewer.adapter;

import android.accounts.Account;
import android.annotation.TargetApi;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.content.SyncResult;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;

import com.thexma.yicamviewer.provider.RecordContract;

import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import dll.Record;
import com.thexma.yicamviewer.net.RecordParser;

/**
 * Created by kle on 2/22/2017.
 */

public class SyncAdapter extends AbstractThreadedSyncAdapter {
    public static final String TAG = "--- SyncAdapter";

    private static final String FEED_URL = "http://android-developers.blogspot.com/atom.xml";

    private final ContentResolver mContentResolver;

    /**
     * Project used when querying content provider. Returns all known fields.
     */
    private static final String[] PROJECTION = new String[]{

            RecordContract.RecordEntry._ID,
            RecordContract.RecordEntry.RECORDS_ID,
            RecordContract.RecordEntry.RECORDS_FOLDERNAME,
            RecordContract.RecordEntry.RECORDS_FILENAME,
            RecordContract.RecordEntry.RECORDS_FILEDATE,
            RecordContract.RecordEntry.RECORDS_FILESIZE,

            RecordContract.RecordEntry.RECORDS_FULLURL,
            RecordContract.RecordEntry.RECORDS_DATE,
            RecordContract.RecordEntry.RECORDS_THUMBNAIL};

    // Constants representing column positions from PROJECTION.
    public static final int COLUMN_ID = 0;
    public static final int RECORDS_ID = 1;
    public static final int RECORDS_FOLDERNAME = 2;
    public static final int RECORDS_FILENAME = 3;
    public static final int RECORDS_FILEDATE = 4;
    public static final int RECORDS_FILESIZE = 5;
    public static final int RECORDS_FULLURL = 6;
    public static final int RECORDS_DATE = 7;
    public static final int RECORDS_THUMBNAIL = 8;


    /**
     * Constructor. Obtains handle to content resolver for later use.
     */
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        mContentResolver = context.getContentResolver();
    }


    /**
     * Constructor. Obtains handle to content resolver for later use.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public SyncAdapter(Context context, boolean autoInitialize, boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        mContentResolver = context.getContentResolver();
    }

    @Override
    public void onPerformSync(Account account, Bundle extras, String authority, ContentProviderClient provider, SyncResult syncResult) {

        Log.i(TAG, "Beginning network synchronization");
        try {
            final URL location = new URL(FEED_URL);
            InputStream stream = null;

            try {
                Log.i(TAG, "Streaming data from network: " + location);
                updateLocalFeedData(location, syncResult);
                // Makes sure that the InputStream is closed after the app is
                // finished using it.
            } finally {
                if (stream != null) {
                    stream.close();
                }
            }
        } catch (MalformedURLException e) {
            Log.e(TAG, "Feed URL is malformed", e);
            syncResult.stats.numParseExceptions++;
            return;
        } catch (IOException e) {
            Log.e(TAG, "Error reading from network: " + e.toString());
            syncResult.stats.numIoExceptions++;
            return;
        } catch (XmlPullParserException e) {
            Log.e(TAG, "Error parsing feed: " + e.toString());
            syncResult.stats.numParseExceptions++;
            return;
        } catch (ParseException e) {
            Log.e(TAG, "Error parsing feed: " + e.toString());
            syncResult.stats.numParseExceptions++;
            return;
        } catch (RemoteException e) {
            Log.e(TAG, "Error updating database: " + e.toString());
            syncResult.databaseError = true;
            return;
        } catch (OperationApplicationException e) {
            Log.e(TAG, "Error updating database: " + e.toString());
            syncResult.databaseError = true;
            return;
        }
        Log.i(TAG, "Network synchronization complete");
    }

    private void updateLocalFeedData(URL locationUrl, SyncResult syncResult) throws IOException, XmlPullParserException, RemoteException,
            OperationApplicationException, ParseException {

        final RecordParser feedParser = new RecordParser();
        final ContentResolver contentResolver = getContext().getContentResolver();

        Log.i(TAG, "Parsing stream as Atom feed");
        final List<Record> entries = feedParser.parse(locationUrl.toString());
        Log.i(TAG, "Parsing complete. Found " + entries.size() + " entries");


        ArrayList<ContentProviderOperation> batch = new ArrayList<ContentProviderOperation>();

        // Build hash table of incoming entries
        HashMap<String, Record> entryMap = new HashMap<String, Record>();
        for (Record e : entries) {
            entryMap.put(Integer.toString(e.getID()), e);
        }

        // Get list of all items
        Log.i(TAG, "Fetching local entries for merge");
        Uri uri = RecordContract.RecordEntry.CONTENT_URI; // Get all entries

        Cursor c = contentResolver.query(uri, PROJECTION, null, null, null);
        assert c != null;
        Log.i(TAG, "Found " + c.getCount() + " local entries. Computing merge solution...");

        // Find stale data
        int id; //autoID

        int entryId;
        String recFolder;
        String recFileName;
        String recFileDate;
        String recFileSize;
        String recFileFullUrl;
        int recDate;
        byte[] image;

        while (c.moveToNext()) {
            syncResult.stats.numEntries++;

            id = c.getInt(COLUMN_ID);

            entryId = Integer.getInteger(c.getString(RECORDS_ID));
            recFolder = c.getString(RECORDS_FOLDERNAME);
            recFileName = c.getString(RECORDS_FILENAME);
            recFileDate = c.getString(RECORDS_FILEDATE);
            recFileSize = c.getString(RECORDS_FILESIZE);
            recFileFullUrl = c.getString(RECORDS_FULLURL);
            recDate = c.getInt(RECORDS_DATE);
            image = c.getBlob(RECORDS_THUMBNAIL);


            Record match = entryMap.get(entryId);
            if (match != null) {
                // Entry exists. Remove from entry map to prevent insert later.
                entryMap.remove(entryId);
                // Check to see if the entry needs to be updated
                Uri existingUri = RecordContract.RecordEntry.CONTENT_URI.buildUpon()
                        .appendPath(Integer.toString(id)).build();
                if ((match.getFullUrl() != null && !match.getFullUrl().equals(recFileFullUrl))
                        //|| (match.link != null && !match.link.equals(link)) ||
                        //(match.published != published)
                        ) {
                    // Update existing record
                    Log.i(TAG, "Scheduling update: " + existingUri);
                    batch.add(ContentProviderOperation.newUpdate(existingUri)
                            .withValue(RecordContract.RecordEntry.RECORDS_FILENAME, match.getFileName() )
                            .withValue(RecordContract.RecordEntry.RECORDS_FILEDATE, match.getFileDate() )
                            .withValue(RecordContract.RecordEntry.RECORDS_FILESIZE, match.getFileSize() )
                            .build());
                    syncResult.stats.numUpdates++;
                } else {
                    Log.i(TAG, "No action: " + existingUri);
                }
            } else {
                // Entry doesn't exist. Remove it from the database.
                Uri deleteUri = RecordContract.RecordEntry.CONTENT_URI.buildUpon()
                        .appendPath(Integer.toString(id)).build();
                Log.i(TAG, "Scheduling delete: " + deleteUri);
                batch.add(ContentProviderOperation.newDelete(deleteUri).build());
                syncResult.stats.numDeletes++;
            }
        }
        c.close();


        // Add new items
        for (Record e : entryMap.values()) {
            Log.i(TAG, "Scheduling insert: entry_id=" + e.getID() );
            batch.add(ContentProviderOperation.newInsert(RecordContract.RecordEntry.CONTENT_URI)
                    .withValue(RecordContract.RecordEntry.RECORDS_ID, e.getID())
                    .withValue(RecordContract.RecordEntry.RECORDS_FOLDERNAME, e.getFolderName())
                    .withValue(RecordContract.RecordEntry.RECORDS_FILENAME, e.getFileName())
                    .withValue(RecordContract.RecordEntry.RECORDS_DATE, e.getFileDate())
                    .withValue(RecordContract.RecordEntry.RECORDS_FILESIZE, e.getFileSize())
                    .withValue(RecordContract.RecordEntry.RECORDS_FULLURL, e.getFullUrl())
                    .withValue(RecordContract.RecordEntry.RECORDS_DATE, e.getDate())
                    .withValue(RecordContract.RecordEntry.RECORDS_THUMBNAIL, e.getThumbnail())
                    .build());
            syncResult.stats.numInserts++;
        }
        Log.i(TAG, "Merge solution ready. Applying batch update");
        mContentResolver.applyBatch(RecordContract.CONTENT_AUTHORITY, batch);
        mContentResolver.notifyChange(
                RecordContract.RecordEntry.CONTENT_URI, // URI where data was modified
                null,                           // No local observer
                false);                         // IMPORTANT: Do not sync to network
        // This sample doesn't support uploads, but if *your* code does, make sure you set
        // syncToNetwork=false in the line above to prevent duplicate syncs.
    }
}
