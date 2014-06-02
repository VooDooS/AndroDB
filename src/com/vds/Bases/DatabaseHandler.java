package com.vds.Bases;

/**
 * Created by Ulysse on 02/06/2014.
 */

import android.content.Context;
import android.database.sqlite.*;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;


public class DatabaseHandler extends SQLiteOpenHelper {
    private static final String TAG = "DB Handler";

    public DatabaseHandler(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //On télécharge la dernière version de la base:
        Log.v(TAG, "Creating db.");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Même action que oncreate:
        Log.v(TAG, "Updating db.");
        onCreate(db);
    }
}