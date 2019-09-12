package com.x.unncrimewatch.roomDB;

import android.content.Context;

import androidx.room.Room;

public class CWDatabaseAccessor {

    private static CWDatabase CWDatabaseInstance;
    private static final String CW_DB_NAME = "cw_db";

    private CWDatabaseAccessor() {
    }

    public static CWDatabase getInstance(Context context) {
        if (CWDatabaseInstance == null) {
            // Create or open a new SQLite database, and return it as
            // a Room Database instance.
            CWDatabaseInstance = Room.databaseBuilder(context,
                    CWDatabase.class, CW_DB_NAME).build();
        }
        return CWDatabaseInstance;
    }

}
