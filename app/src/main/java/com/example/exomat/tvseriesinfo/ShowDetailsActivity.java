package com.example.exomat.tvseriesinfo;

import android.arch.persistence.room.Room;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.exomat.tvseriesinfo.dao.TVShowDao;
import com.example.exomat.tvseriesinfo.database.AppDatabase;
import com.example.exomat.tvseriesinfo.model.TVShow;
import com.example.exomat.tvseriesinfo.pojo.TvShowResult;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;

public class ShowDetailsActivity extends AppCompatActivity {
    private boolean ifFavorite = false;
    private TVShow tvShowToSave = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppDatabase appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "database-tvshow").build();
        final TVShowDao tvShowDao = appDatabase.tvShowDao();
        setContentView(R.layout.activity_show_details);
        final TvShowResult show = (TvShowResult) getIntent().getSerializableExtra("Show");
        Log.i("SDAI", "TVShow to display: " + show.toString());
        TextView name = findViewById(R.id.tvShowNameText);
        TextView premiere = findViewById(R.id.premiereText);
        TextView status = findViewById(R.id.statusText);
        TextView summary = findViewById(R.id.showSummaryText);
        ImageView image = findViewById(R.id.imageTVView);
        String originalImagePath = show.getShow().getImage().getOriginal();

        if (!originalImagePath.isEmpty())
            Picasso.with(getApplicationContext()).load(originalImagePath).into(image);
        name.setText(show.getShow().getName());
        premiere.setText(show.getShow().getPremiered());
        status.setText(show.getShow().getStatus());
        summary.setText(show.getShow().getSummary().replaceAll("(<[^>]+>)", ""));
        summary.setMovementMethod(new ScrollingMovementMethod());
        final ImageButton favoriteButton = findViewById(R.id.favoriteButton);
        favoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!ifFavorite) {
                    ifFavorite = true;
                    favoriteButton.setBackgroundResource(R.drawable.likefull);
                    if (tvShowToSave == null) {
                        tvShowToSave = getNewTVShow(show);
                    }
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            tvShowToSave.setId(tvShowDao.insert(tvShowToSave));
                        }
                    });
                    Toast.makeText(ShowDetailsActivity.this, "Tv Series add to favorite :)", Toast.LENGTH_SHORT).show();
                } else {
                    ifFavorite = false;
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            List<TVShow> all = tvShowDao.getAll();
                            Log.i("TVINFO", "w bazie " + all.get(0).toString());
                        }
                    });
                    AsyncTask.execute(new Runnable() {
                        @Override
                        public void run() {
                            tvShowDao.delete(tvShowToSave);
                        }
                    });
                    favoriteButton.setBackgroundResource(R.drawable.likeempty);
                    Toast.makeText(ShowDetailsActivity.this, "Tv Series remove from favorite :)", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private TVShow getNewTVShow(TvShowResult showResult) {
        TVShow tvShow = new TVShow();
        tvShow.setName(showResult.getShow().getName());
        tvShow.setStatus(showResult.getShow().getStatus());
        tvShow.setPremiere(showResult.getShow().getPremiered());
        tvShow.setSummary(showResult.getShow().getSummary());
        tvShow.setSelfLink(String.valueOf(showResult.getShow().getLinks().getSelf()));
        tvShow.setImgLink(showResult.getShow().getImage().getOriginal());
        try {
            Bitmap bitmap = Picasso.with(getApplicationContext()).load(showResult.getShow().getImage().getOriginal()).get();

            // save to db
        } catch (IOException e) {
            Log.e("SDetActiv", "getNewTVShow: ", e);
        }
        //todo async get previous and next episode


        return tvShow;
    }
}
