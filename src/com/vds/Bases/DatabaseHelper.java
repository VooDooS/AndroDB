package com.vds.bases;

/**
 * Created by Ulysse on 02/06/2014.
 */

import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.sql.SQLException;
import java.util.List;
import java.util.zip.ZipInputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import au.com.bytecode.opencsv.CSVReader;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.vds.bases.entities.Film;
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
            //We download and unzip the db files:
            download();

            //For each table, we parse the file and fill the database:
            List<String[]> res = parse(basePath+"TF_Films.txt");


            //Creating Film table
            TableUtils.createTable(connectionSource, Film.class);
            RuntimeExceptionDao<Film, Integer> dao = getFilmDao();
            for(int i=0; i < res.size(); i++) {
                try {
                    int date = Integer.parseInt(res.get(i)[4]);
                    Film f = new Film(Integer.parseInt(res.get(i)[0]), Integer.parseInt(res.get(i)[1]), Integer.parseInt(res.get(i)[5]), date,  Integer.parseInt(res.get(i)[3]),  res.get(i)[2],  res.get(i)[6]);
                    dao.create(f);
                } catch (NumberFormatException e) {}
                //Log.i(DatabaseHelper.class.getName(), "onCreate"+Integer.parseInt(res.get(i)[0])+Integer.parseInt(res.get(i)[1])+Integer.parseInt(res.get(i)[5])+Integer.parseInt(res.get(i)[4])+Integer.parseInt(res.get(i)[3])+res.get(i)[2]+res.get(i)[6]);

            }



        } catch (SQLException e) {
            Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
            throw new RuntimeException(e);
        }
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
            URLConnection connexion = url.openConnection();
            connexion.connect();

            int lenghtOfFile = connexion.getContentLength();
            Log.d("ANDRO_ASYNC", "Lenght of file: " + lenghtOfFile);

            InputStream input = new BufferedInputStream(url.openStream());
            OutputStream output = new FileOutputStream(basePath+fileName);
            ZipInputStream zipInputStream = new ZipInputStream(
                    connexion.getInputStream());
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
     * Unzip, using zip4j
     */
    private void unzip(String path) {
        try {
            ZipFile zipFile = new ZipFile(path);
            zipFile.extractAll(basePath);
        } catch (ZipException e) {
            e.printStackTrace();
        }
    }

    /**
     * Parse CSV file using OpenCSV
     */
    private List<String[]> parse(String path) {
        List<String[]> entries = null;
        CSVReader reader = null;

        try {
            reader = new CSVReader(new InputStreamReader(new FileInputStream(path), "ISO-8859-1"), ';', '"');
            entries = reader.readAll();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return entries;
    }
}