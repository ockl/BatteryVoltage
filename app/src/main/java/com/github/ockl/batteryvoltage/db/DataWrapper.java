package com.github.ockl.batteryvoltage.db;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import com.github.ockl.batteryvoltage.db.DbSchema.*;

/**
 * Created by greg on 1/2/18.
 */

class DataWrapper extends android.database.CursorWrapper {

    public DataWrapper(Cursor cursor) {
        super(cursor);
    }

    public Date getDate() {
        try {
            return DateFormat.getInstance().parse(
                    getString(getColumnIndex(Table.Cols.DATE)));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    public Data getData() {
        Data data = new Data();
        data.level = getInt(getColumnIndex(Table.Cols.LEVEL));
        data.voltage = getDouble(getColumnIndex(Table.Cols.VOLTAGE));
        data.capacity = getDouble(getColumnIndex(Table.Cols.CAPACITY));
        return data;
    }

}
