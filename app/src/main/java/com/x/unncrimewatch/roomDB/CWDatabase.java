package com.x.unncrimewatch.roomDB;


import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {CW.class}, version = 1, exportSchema = false)
@TypeConverters({CWTypeConverters.class})
public abstract class CWDatabase extends RoomDatabase {
    public abstract CWDao cwDAO();
}