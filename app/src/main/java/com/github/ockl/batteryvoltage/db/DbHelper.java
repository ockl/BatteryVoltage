package com.github.ockl.batteryvoltage.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.github.ockl.batteryvoltage.db.DbSchema.Table;

/**
 * Created by greg on 1/2/18.
 */

class DbHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "crimeBase.db";

    public DbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + Table.NAME
                + "( _id integer primary key autoincrement, "
                + Table.Cols.DATE + ", "
                + Table.Cols.LEVEL + ", "
                + Table.Cols.VOLTAGE + ", "
                + Table.Cols.CAPACITY + ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}
