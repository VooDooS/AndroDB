package com.vds.bases;

/**
 * Created by Ulysse on 02/06/2014.
 */

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import com.j256.ormlite.android.apptools.OrmLiteBaseActivity;
import com.j256.ormlite.dao.RuntimeExceptionDao;
import com.vds.bases.entities.Film;

import java.util.List;

public class ListeFilmsActivity extends OrmLiteBaseActivity<DatabaseHelper> {
    private final String LOG_TAG = getClass().getSimpleName();

    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.listefilms);

        Log.i(LOG_TAG, "creating " + getClass() + " at " + System.currentTimeMillis());
        TextView tv = new TextView(this);
        doSampleDatabaseStuff("onCreate", tv);
        setContentView(tv);
    }

    /**
     * Do our sample database stuff.
     */
    private void doSampleDatabaseStuff(String action, TextView tv) {
        // get our dao
        RuntimeExceptionDao<Film, Integer> simpleDao = getHelper().getFilmDao();
// query for all of the data objects in the database
        List<Film> list = simpleDao.queryForAll();
// our string builder for building the content-view
        StringBuilder sb = new StringBuilder();
        sb.append("got ").append(list.size()).append(" entries in ").append(action).append("\n");

// if we already have items in the database
        int simpleC = 0;
        for (Film simple : list) {
            sb.append("------------------------------------------\n");
            sb.append(simple.getTitle()).append("\n");
            simpleC++;
        }
        sb.append("------------------------------------------\n");

        tv.setText(sb.toString());
        Log.i(LOG_TAG, "Done with page at " + System.currentTimeMillis());
    }
}