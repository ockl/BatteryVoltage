package com.github.ockl.batteryvoltage.db;

import java.util.Date;

/**
 * Created by greg on 1/2/18.
 */

public class DbSchema {

    static final class Table {
        public static final String NAME = "DataEntries";

        public static final class Cols {
            public static final String DATE = "date";
            public static final String LEVEL = "level";
            public static final String VOLTAGE = "voltage";
            public static final String CAPACITY = "capacity";
        }
    }

    public static final class Data {
        public Date date;
        public int level;
        public double voltage;
        public double capacity;
    }

}
