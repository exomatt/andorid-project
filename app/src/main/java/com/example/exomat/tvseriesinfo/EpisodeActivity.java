package com.example.exomat.tvseriesinfo;

import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exomat.tvseriesinfo.dao.TVShowDao;
import com.example.exomat.tvseriesinfo.database.AppDatabase;
import com.example.exomat.tvseriesinfo.model.TVShow;
import com.squareup.picasso.Picasso;

import lombok.Setter;

public class EpisodeActivity extends AppCompatActivity implements AsyncResponse {
    private TVShowDao tvShowDao;
    private TextView name;
    private TextView premiere;
    private TextView summary;
    private TextView info;
    private TextView number;
    private ImageView image;
    private String tvShowUrl;
    private TVShow tvShow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episode);
        tvShowUrl = getIntent().getStringExtra("ShowUrl");
        AppDatabase appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "database-tvshow").build();
        tvShowDao = appDatabase.tvShowDao();
        premiere = findViewById(R.id.premiereTextEpisode);
        summary = findViewById(R.id.showSummaryTextEpisode);
        summary.setMovementMethod(new ScrollingMovementMethod());
        info = findViewById(R.id.textTittleEpisode);
        number = findViewById(R.id.textNumberEpisode);
        name = findViewById(R.id.tvShowNameTextEpisode);
        image = findViewById(R.id.imageEpisode);
        MyAsyncLoadEpisodeData myAsyncLoadEpisodeData = new MyAsyncLoadEpisodeData();
        myAsyncLoadEpisodeData.setDelegate(this);
        myAsyncLoadEpisodeData.execute();
    }

    @Override
    public void processFinish(String output) {
        Toast.makeText(this, output, Toast.LENGTH_SHORT).show();
    }

    @Setter
    private class MyAsyncLoadEpisodeData extends AsyncTask<Void, Void, String> {
        public AsyncResponse delegate = null;

        @Override
        protected String doInBackground(Void... voids) {
            tvShow = tvShowDao.findByUrl(tvShowUrl);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (tvShow == null) {
                delegate.processFinish(getString(R.string.DBLoadProblem));
                return;
            }
            name.setText(tvShow.getName());
            premiere.setText(tvShow.getPremiere());
            summary.setText(tvShow.getNextEpisodeSummary().replaceAll("(<[^>]+>)", ""));
            number.setText(getString(R.string.EpisodeNumber) + tvShow.getNextEpisodeSE());
            info.setText(getString(R.string.EpisodeTittle) + tvShow.getNextEpisodeName());
            if (tvShow.getImageByteArray() != null) {
                image.setImageBitmap(ImageUtils.getImage(tvShow.getImageByteArray()));
            } else {
                if (!tvShow.getImgLink().isEmpty()) {
                    Picasso.with(getApplicationContext()).load(tvShow.getImgLink()).into(image);
                }
            }
        }

    }
}
