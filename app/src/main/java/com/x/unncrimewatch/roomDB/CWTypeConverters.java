package com.x.unncrimewatch.roomDB;


import android.net.Uri;

import androidx.room.TypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;

public class CWTypeConverters {

    @TypeConverter
    public static Date dateFromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static Uri StringToUri(String uri) {
        return uri == null ? null : Uri.parse(uri);
    }

    @TypeConverter
    public static String UriToString(Uri uri) {
        return uri == null ? null : uri.toString();
    }

    @TypeConverter
    public static ArrayList<String> fromString(String value) {

        Type listType = new TypeToken<ArrayList<String>>() {
        }.getType();

        return value == null ? null : new Gson().fromJson(value, listType);
    }

    @TypeConverter
    public static String fromArrayList(ArrayList<String> list) {
        Gson gson = new Gson();
        String json = gson.toJson(list);

        return list == null ? null : json;
    }
}
