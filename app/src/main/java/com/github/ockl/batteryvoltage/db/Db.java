package com.github.ockl.batteryvoltage.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.github.ockl.batteryvoltage.db.DbSchema.Table;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by greg on 1/2/18.
 */

public class Db {

    private final SQLiteDatabase database;

    private static Db instance;

    public static Db getInstance(Context context) {
        synchronized (Db.class) {
            if (instance == null) {
                instance = new Db(context);
            }
            return instance;
        }
    }

    private Db(Context context) {
        database = new DbHelper(context).getWritableDatabase();
    }

    public void addData(int level, double voltage, double capacity) {
        ContentValues values = new ContentValues();
        values.put(Table.Cols.DATE, Calendar.getInstance().getTime().toString());
        values.put(Table.Cols.LEVEL, Integer.toString(level));
        values.put(Table.Cols.VOLTAGE, Double.toString(voltage));
        values.put(Table.Cols.CAPACITY, Double.toString(capacity));
        database.insert(Table.NAME, null, values);
    }

    public List<DbSchema.Data> getAll() {
        return query(null, null);
    }

    public List<DbSchema.Data> getLatest(Date fromDate) {
        return query(Table.Cols.DATE + " > ?", new String[] { fromDate.toString() });
    }

    private List<DbSchema.Data> query(String whereClause, String[] whereArgs) {
        List<DbSchema.Data> data = new ArrayList<>();
        Cursor cursor = database.query(Table.NAME, null, whereClause,
                whereArgs, null, null, null);
        DataWrapper wrapper = new DataWrapper(cursor);

        try {
            wrapper.moveToFirst();
            while (!wrapper.isAfterLast()) {
                data.add(wrapper.getData());
                wrapper.moveToNext();
            }
        } finally {
            wrapper.close();
        }

        return data;
    }

}
