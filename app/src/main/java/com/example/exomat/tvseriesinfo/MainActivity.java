package com.example.exomat.tvseriesinfo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.arch.persistence.room.Room;
import android.content.Context;
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

import java.util.Calendar;
import java.util.List;

import lombok.Setter;

public class MainActivity extends AppCompatActivity {
    public static final String MAIN_TAG = "MainTag";
    public static final String SUCCESSFULLY_LOAD_SHOWS = "Successfully load Shows :)";
    private AppDatabase appDatabase;
    private List<TVShow> shows;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 2);
        Intent intent = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), 1000 * 60 * 60, pendingIntent);

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
        CardView settingsCard = findViewById(R.id.settingsCard);
        settingsCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getBaseContext(), SettingsActivity.class);
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
            Log.i(MAIN_TAG, SUCCESSFULLY_LOAD_SHOWS);
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
            Log.i(MAIN_TAG, SUCCESSFULLY_LOAD_SHOWS);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

        }
    }
}
