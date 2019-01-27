package com.example.exomat.tvseriesinfo;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;

import com.example.exomat.tvseriesinfo.dao.TVShowDao;
import com.example.exomat.tvseriesinfo.database.AppDatabase;
import com.example.exomat.tvseriesinfo.model.TVShow;
import com.example.exomat.tvseriesinfo.requester.SearchRequester;

import java.util.List;

import lombok.Setter;

public class MainActivity extends AppCompatActivity {
    private AppDatabase appDatabase;
    private List<TVShow> shows;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        CardView searchCard = findViewById(R.id.searchCard);
        searchCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), SearchActivity.class);
                startActivity(intent);
            }
        });
        CardView favoriteCard = findViewById(R.id.favoriteCard);
        favoriteCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), FavoritesViewActivity.class);
                startActivity(intent);
            }
        });
        CardView todayCard = findViewById(R.id.todayCard);
        todayCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), TodayActivity.class);
                startActivity(intent);
            }
        });
        new MyAsyncTaskUpdateDB().execute();
    }

    @Setter
    private class MyAsyncTaskUpdateDB extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            appDatabase = Room.databaseBuilder(getBaseContext(), AppDatabase.class, "database-tvshow").build();
            TVShowDao tvShowDao = appDatabase.tvShowDao();
            shows = tvShowDao.getAll();
            appDatabase.close();
            if (shows == null) {
                return null;
            }
            for (TVShow show : shows) {
                String path = show.getSelfLink().substring(show.getSelfLink().lastIndexOf("/") + 1).replace("]", "");
                SearchRequester.updateShowInDB(path, show, getApplicationContext());
            }
            Log.i("MainTag", "Successfully load Shows :)");
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

        }
    }

    @Setter
    private class MyAsyncTaskUpdateImage extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            appDatabase = Room.databaseBuilder(getBaseContext(), AppDatabase.class, "database-tvshow").build();
            TVShowDao tvShowDao = appDatabase.tvShowDao();
            shows = tvShowDao.getAll();
            appDatabase.close();
            if (shows == null) {
                return null;
            }
            for (TVShow show : shows) {
                String path = show.getSelfLink().substring(show.getSelfLink().lastIndexOf("/") + 1).replace("]", "");
                SearchRequester.updateShowInDB(path, show, getApplicationContext());
            }
            Log.i("MainTag", "Successfully load Shows :)");
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

        }
    }
}
