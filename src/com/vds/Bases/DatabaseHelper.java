package com.vds.bases;

/**
 * Created by Ulysse on 02/06/2014.
 */

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.vds.bases.com.vds.bases.entities.Film;
import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.exception.ZipException;

/**
 * Database helper class used to manage the creation and upgrading of the database.
 */
public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    // name of the database file for your application -- change to something appropriate for your app
    private static final String DATABASE_NAME = "helloAndroid.db";
    // any time you make changes to your database objects, you may have to increase the database version
    private static final int DATABASE_VERSION = 1;

    // the DAO object we use to access the Film table
    private Dao<Film, Integer> simpleDao = null;
    private RuntimeExceptionDao<Film, Integer> simpleRuntimeDao = null;

    //Paths for zip download and extracting:
    private final String basePath = "/sdcard/";
    private final String fileName = "db.zip";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION, R.raw.ormlite_config);
    }

    /**
     * Creating tables on first launch
     */
    @Override
    public void onCreate(SQLiteDatabase db, ConnectionSource connectionSource) {
        try {
            download();
            Log.i(DatabaseHelper.class.getName(), "onCreate");

            //Creating Film table
            TableUtils.createTable(connectionSource, Film.class);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }

// here we try inserting data in the on-create as a test
        RuntimeExceptionDao<Film, Integer> dao = getFilmDao();
        long millis = System.currentTimeMillis();
        int real = 1, id = 2, date = 2653;
        String t = "totofilm";
        Film simple = new Film(id, real, real, real, date, t);
        dao.create(simple);
        Log.i(DatabaseHelper.class.getName(), "created new entries in onCreate");
    }

    /**
     * Drop tables and re-create
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            Log.i(DatabaseHelper.class.getName(), "onUpgrade");
            TableUtils.dropTable(connectionSource, Film.class, true);
            // after we drop the old databases, we create the new ones
            onCreate(db, connectionSource);
        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the Database Access Object (DAO) for our Film class. It will create it or just give the cached
     * value.
     */
    public Dao<Film, Integer> getDao() throws SQLException {
        if (simpleDao == null) {
            simpleDao = getDao(Film.class);
        }
        return simpleDao;
    }

    /**
     * Returns the RuntimeExceptionDao (Database Access Object) version of a Dao for our Film class. It will
     * create it or just give the cached value. RuntimeExceptionDao only through RuntimeExceptions.
     */
    public RuntimeExceptionDao<Film, Integer> getFilmDao() {
        if (simpleRuntimeDao == null) {
            simpleRuntimeDao = getRuntimeExceptionDao(Film.class);
        }
        return simpleRuntimeDao;
    }

    /**
     * Close the database connections and clear any cached DAOs.
     */
    @Override
    public void close() {
        super.close();
        simpleDao = null;
        simpleRuntimeDao = null;
    }

    // Reads an InputStream and converts it to a String.
    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

    /**
     * Download the latest db from u31.fr
     */
    private void download() {
        int count;

        try {
            URL url = new URL("http://u31.fr/db.zip");
            URLConnection conexion = url.openConnection();
            conexion.connect();

            int lenghtOfFile = conexion.getContentLength();
            Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);

            InputStream input = new BufferedInputStream(url.openStream());
            OutputStream output = new FileOutputStream(basePath+fileName);
            ZipInputStream zipInputStream = new ZipInputStream(
                    conexion.getInputStream());
            byte data[] = new byte[1024];

            while ((count = input.read(data)) != -1) {
                output.write(data, 0, count);
            }

            output.flush();
            output.close();
            input.close();
        } catch (Exception e) {}

        unzip(basePath+fileName);
    }

    /**
     * Unzip it, using zip4j
     */
    private void unzip(String path) {
        try {
            ZipFile zipFile = new ZipFile(path);
            zipFile.extractAll(basePath);
        } catch (ZipException e) {
            e.printStackTrace();
        }
    }
}