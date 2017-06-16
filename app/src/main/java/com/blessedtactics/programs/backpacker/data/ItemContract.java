package com.blessedtactics.programs.backpacker.data;


import android.provider.BaseColumns;

public class ItemContract {

    private ItemContract() {
    }

    public static final class ItemsDbColumns implements BaseColumns {
        public final static String TABLE_NAME = "items";

        public final static String _ID = BaseColumns._ID;
        public final static String COLUMN_NAME = "name";
        public final static String COLUMN_TYPE = "type";
    }

}
