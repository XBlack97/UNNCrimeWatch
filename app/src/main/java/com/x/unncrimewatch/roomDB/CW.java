package com.x.unncrimewatch.roomDB;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;


@Entity
public class CW {

    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int mId;
    private Date mDate;
    private String mUpdate;
    private ArrayList<String> mImageUris;


    public int getId() {
        return mId;
    }

    public void setId(int id) {
        this.mId = id;
    }

    public Date getDate() {
        return mDate;
    }

    public String getUpdate() {
        return mUpdate;
    }

    public ArrayList<String> getImageUris() {
        return mImageUris;
    }


    public CW(Date date, String update, ArrayList<String> imageUris) {
        mDate = date;
        mUpdate = update;
        mImageUris = imageUris;
    }

    @Override
    public String toString() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm, MMM d ''yy", Locale.US);
        String dateString = sdf.format(mDate);
        return mUpdate + "\n" + "\n" + dateString;
    }


    @Override
    public boolean equals(Object obj) {
        if (obj instanceof CW)
            return (((CW) obj).getId() == (mId));
        else
            return false;
    }
}
