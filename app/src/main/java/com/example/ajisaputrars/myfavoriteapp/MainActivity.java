package com.example.ajisaputrars.myfavoriteapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.database.ContentObserver;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.util.Log;
import android.widget.Toast;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements LoadMoviesCallback {

    private FavoriteMovieAdapter adapter;
    private MainActivity.DataObserver myObserver;
    ArrayList<Movie> listMovies;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView rvNotes = findViewById(R.id.rv_favorite_movie);

        adapter = new FavoriteMovieAdapter(this);
        rvNotes.setLayoutManager(new LinearLayoutManager(this));
        rvNotes.setHasFixedSize(true);
        rvNotes.setAdapter(adapter);

        HandlerThread handlerThread = new HandlerThread("DataObserver");
        handlerThread.start();
        Handler handler = new Handler(handlerThread.getLooper());

        myObserver = new DataObserver(handler, this);
        getContentResolver().registerContentObserver(
                MovieDatabaseContract.MovieColumns.CONTENT_URI,
                true,
                myObserver);

        new getData(this, this).execute();
    }

    @Override
    protected void onResume() {
        super.onResume();
        new getData(this, this).execute();
    }

    private static class getData extends AsyncTask<Void, Void, Cursor> {
        private final WeakReference<Context> weakContext;
        private final WeakReference<LoadMoviesCallback> weakCallback;

        private getData(Context context, LoadMoviesCallback callback) {
            weakContext = new WeakReference<>(context);
            weakCallback = new WeakReference<>(callback);

            Log.d("getData", " dijalankan");
        }

        @Override
        protected Cursor doInBackground(Void... voids) {
            Log.d("doInBackground", " dijalankan");
            Log.d("CONTENT_URI", " content uri adalah " + MovieDatabaseContract.MovieColumns.CONTENT_URI);

            return weakContext.get().getContentResolver().query(
                    MovieDatabaseContract.MovieColumns.CONTENT_URI,
                    null, null, null, null);
        }

        @Override
        protected void onPostExecute(Cursor data) {
            Log.d("onPostExecute", " dijalankan");
            weakCallback.get().postExecute(data);
        }
    }

    @Override
    public void postExecute(Cursor cursor) {
        Log.d("postExecute", " dijalankan");

        listMovies = MappingHelper.mapCursorToArrayList(cursor);
        Log.d("ArrayList<Movie>", " dijalankan");

        if (listMovies.size() > 0) {
            adapter.setData(listMovies);
        } else {
            Toast.makeText(this, "Tidak Ada data saat ini", Toast.LENGTH_SHORT).show();
            adapter.setData(new ArrayList<Movie>());
        }
    }

    static class DataObserver extends ContentObserver {
        final Context context;

        DataObserver(Handler handler, Context context) {
            super(handler);
            this.context = context;
        }
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            new getData(context, (MainActivity) context).execute();
        }
    }
}
