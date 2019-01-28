package com.example.exomat.tvseriesinfo;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
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
import com.example.exomat.tvseriesinfo.pojo.pojoTVShowResult.TvShowResult;
import com.example.exomat.tvseriesinfo.requester.SearchRequester;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class ShowDetailsActivity extends AppCompatActivity implements SensorEventListener {
    public static final String S_DET_ACTIV = "SDetActiv";
    private boolean ifFavorite = false;
    private TVShow tvShowToSave = null;
    private TVShowDao tvShowDao;
    private String tvShowUrl;
    private TextView name;
    private TextView premiere;
    private TextView status;
    private TextView summary;
    private ImageView image;
    private ImageButton favoriteButton;
    private SensorManager sensorManager;
    private Sensor proximitySensor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        PackageManager PM = this.getPackageManager();
        boolean proximity = PM.hasSystemFeature(PackageManager.FEATURE_SENSOR_PROXIMITY);
        if (proximity) {
            sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
            proximitySensor = sensorManager.getDefaultSensor(Sensor.TYPE_PROXIMITY);
            sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
        AppDatabase appDatabase = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "database-tvshow").build();
        tvShowDao = appDatabase.tvShowDao();
        setContentView(R.layout.activity_show_details);
        name = findViewById(R.id.tvShowNameText);
        premiere = findViewById(R.id.premiereText);
        status = findViewById(R.id.statusText);
        summary = findViewById(R.id.showSummaryText);
        image = findViewById(R.id.imageTVView);
        favoriteButton = findViewById(R.id.favoriteButton);
        tvShowToSave = (TVShow) getIntent().getSerializableExtra("Show");
        tvShowUrl = getIntent().getStringExtra("ShowUrl");
        summary.setMovementMethod(new ScrollingMovementMethod());
        new MyAsyncLoadData().execute();
    }

    private TVShow getNewTVShow(TvShowResult showResult) {
        TVShow tvShow = new TVShow();
        tvShow.setName(showResult.getShow().getName());
        tvShow.setStatus(showResult.getShow().getStatus());
        tvShow.setPremiere(showResult.getShow().getPremiered());
        tvShow.setSummary(showResult.getShow().getSummary());
        tvShow.setSelfLink(String.valueOf(showResult.getShow().getLinks().getSelf()));
        tvShow.setImgLink(showResult.getShow().getImage().getOriginal());

        //todo async get previous and next episode


        return tvShow;
    }

    private void getImageToDB(TVShow tvShow) {
        try {
            Bitmap bitmap = Picasso.with(getApplicationContext()).load(tvShow.getImgLink()).get();
            byte[] bytes = ImageUtils.getBytes(bitmap);
            tvShow.setImageByteArray(bytes);
            tvShowDao.update(tvShow);
            //todo save to db
        } catch (IOException e) {
            Log.e(S_DET_ACTIV, "getNewTVShow image problem: ", e);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;
        if (sensor.getType() == Sensor.TYPE_PROXIMITY) {
            float value = event.values[0];
            if (sensor.getMaximumRange() > value) {
                if (!ifFavorite) {
                    addToDB();
                } else {
                    deleteFromDB();
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        if (sensor.getType() == Sensor.TYPE_PROXIMITY) {
            Log.e(S_DET_ACTIV, "Proximity accuracy change" + accuracy);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (sensorManager != null) {
            sensorManager.unregisterListener(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (sensorManager != null) {
            sensorManager.registerListener(this, proximitySensor, SensorManager.SENSOR_DELAY_NORMAL);
        }
    }

    public class MyAsyncLoadData extends AsyncTask<Void, Void, String> {
        public AsyncResponse delegate = null;

        @Override
        protected String doInBackground(Void... voids) {
            TVShow byName;
            if (tvShowToSave == null) {
                byName = tvShowDao.findByUrl(tvShowUrl);
            } else {
                byName = tvShowDao.findByUrl(tvShowToSave.getSelfLink());
            }
            if (byName != null) {
                tvShowToSave = byName;
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            if (tvShowToSave.getId() != null) {
                ifFavorite = true;
                favoriteButton.setBackgroundResource(R.drawable.likefull);
            }
            Log.i(S_DET_ACTIV, "TVShow to display: " + tvShowToSave.toString());
            String originalImagePath = tvShowToSave.getImgLink();
            if (tvShowToSave.getImageByteArray() != null) {
                image.setImageBitmap(ImageUtils.getImage(tvShowToSave.getImageByteArray()));
            } else {
                if (!originalImagePath.isEmpty()) {
                    Picasso.with(getApplicationContext()).load(originalImagePath).into(image);
                }
            }
            name.setText(tvShowToSave.getName());
            premiere.setText(tvShowToSave.getPremiere());
            status.setText(tvShowToSave.getStatus());
            summary.setText(tvShowToSave.getSummary().replaceAll("(<[^>]+>)", ""));
            summary.setMovementMethod(new ScrollingMovementMethod());
            favoriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!ifFavorite) {

                        addToDB();
                    } else {
                        deleteFromDB();
                    }
                }
            });
        }
    }

    private void deleteFromDB() {
        ifFavorite = false;
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                tvShowDao.delete(tvShowToSave);
            }
        });
        favoriteButton.setBackgroundResource(R.drawable.likeempty);
        Toast.makeText(ShowDetailsActivity.this, getString(R.string.remove_favorite), Toast.LENGTH_SHORT).show();
    }

    private void addToDB() {
        ifFavorite = true;
        favoriteButton.setBackgroundResource(R.drawable.likefull);
        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                Long insert = tvShowDao.insert(tvShowToSave);
                tvShowToSave.setId(insert);
                getImageToDB(tvShowToSave);
                if (tvShowToSave.getLastEpisodeLink() != null) {
                    SearchRequester.fillEpisodes(tvShowToSave.getLastEpisodeLink(), tvShowToSave, false, getApplicationContext());
                }
                if (tvShowToSave.getNextEpisodeLink() != null) {
                    SearchRequester.fillEpisodes(tvShowToSave.getNextEpisodeLink(), tvShowToSave, true, getApplicationContext());
                }
                //todo need to check
            }
        });
        Toast.makeText(ShowDetailsActivity.this, getString(R.string.add_favorite), Toast.LENGTH_SHORT).show();
    }


}
