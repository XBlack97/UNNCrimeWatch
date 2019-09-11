package com.x.unncrimewatch.model;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.x.unncrimewatch.roomDB.CW;
import com.x.unncrimewatch.roomDB.CWDatabaseAccessor;

import java.util.List;



public class CWViewModel extends AndroidViewModel {

    private LiveData<List<CW>> updates;

    public CWViewModel(Application application) {
        super(application);
    }

    public LiveData<List<CW>> getUpdates() {
        if (updates == null) {
            // Load the Earthquakes from the database.
            updates = CWDatabaseAccessor.getInstance(getApplication()).cwDAO()
                    .loadAllUpdates();
        }
        return updates;
    }
}
