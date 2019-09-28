package com.example.ajisaputrars.myfavoriteapp;

import android.database.Cursor;

public interface LoadMoviesCallback {
    void postExecute(Cursor cursor);
}
