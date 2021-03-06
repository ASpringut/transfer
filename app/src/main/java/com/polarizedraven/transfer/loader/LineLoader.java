package com.polarizedraven.transfer.loader;

import android.content.AsyncTaskLoader;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.util.Log;

import com.polarizedraven.transfer.Line;
import com.polarizedraven.transfer.StopsDatabase;

/**
 * Created by aaron on 2/15/18.
 */

public class LineLoader extends AsyncTaskLoader<Cursor> {

    private static final String TAG = LineLoader.class.toString();

    //Cursor adapters require a rowid
    public static final String[] COLUMNS = {StopsDatabase.ROW_ID, StopsDatabase.STOP_COLUMN, StopsDatabase.LINE_ID_COLUMN};
    private static final String SELECTION = String.format("%s IN (SELECT DISTINCT line_id FROM lines WHERE line_name = ?)", StopsDatabase.LINE_ID_COLUMN);

    private final Line line;

    public LineLoader (Context context, Line line){
        super(context);
        this.line = line;
        onContentChanged();
    }

    @Override
    public Cursor loadInBackground() {
        String[] selectionArgs = {line.toString()};
        StopsDatabase sd = new StopsDatabase(super.getContext());
        Cursor cursor = sd.getReadableDatabase().query(StopsDatabase.STOP_TABLE,
                COLUMNS,
                SELECTION,
                selectionArgs,
                null,
                null,
                StopsDatabase.STOP_NUMBER_COLUMN,
                null);

        return cursor;
    }

    @Override
    protected void onStartLoading() {
        if (takeContentChanged()) {
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }
}
