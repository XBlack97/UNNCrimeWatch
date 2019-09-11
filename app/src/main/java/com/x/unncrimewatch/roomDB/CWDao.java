package com.x.unncrimewatch.roomDB;



import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface CWDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertUpdates(List<CW> updates);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public void insertUpdate(CW update);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    public void update(CW update);

    @Delete
    public void deleteUpdate(CW update);

    @Query("SELECT * FROM cw ORDER BY mDate DESC")
    public LiveData<List<CW>> loadAllUpdates();

}
