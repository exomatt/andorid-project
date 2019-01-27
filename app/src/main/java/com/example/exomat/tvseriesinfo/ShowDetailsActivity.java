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

public class ShowDetailsActivity extends AppCompatActivity {
    private boolean ifFavorite = false;
    private TVShow tvShowToSave = null;
    private TVShowDao tvShowDao;
    private String tvShowName;
    private TextView name;
    private TextView premiere;
    private TextView status;
    private TextView summary;
    private ImageView image;
    private ImageButton favoriteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        tvShowName = getIntent().getStringExtra("ShowName");
//        tvShowToSave = getNewTVShow(show);
//        AsyncTask.execute(new Runnable() {
//            @Override
//            public void run() {
//                TVShow byName;
//                if (tvShowToSave==null){
//                    byName = tvShowDao.findByName(tvShowName);
//                }else{
//                    byName = tvShowDao.findByName(tvShowToSave.getName());
//                }
//                if (byName!=null){
//                    tvShowToSave = byName;
//                }
//            }
//        });
        //todo ladowanie przed i sprawdzenie czy przypadkiem nie jest juz likeniety xd
//        System.out.println(tvShowToSave);
//        System.out.println(tvShowToSave);
//        if (tvShowToSave.getId() == null) {
//            ifFavorite = true;
//            favoriteButton.setBackgroundResource(R.drawable.likefull);
//        }
//        Log.i("SDAI", "TVShow to display: " + tvShowToSave.toString());
//        String originalImagePath = tvShowToSave.getImgLink();
//        if (!originalImagePath.isEmpty()) {
//            Picasso.with(getApplicationContext()).load(originalImagePath).into(image);
//        }
//        name.setText(tvShowToSave.getName());
//        premiere.setText(tvShowToSave.getPremiere());
//        status.setText(tvShowToSave.getStatus());
//        summary.setText(tvShowToSave.getSummary().replaceAll("(<[^>]+>)", ""));
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
            Log.e("SDetActiv", "getNewTVShow: ", e);
        }
    }

    public class MyAsyncLoadData extends AsyncTask<Void, Void, String> {
        public AsyncResponse delegate = null;

        @Override
        protected String doInBackground(Void... voids) {
            TVShow byName;
            if (tvShowToSave == null) {
                byName = tvShowDao.findByName(tvShowName);
            } else {
                byName = tvShowDao.findByName(tvShowToSave.getName());
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
            Log.i("SDAI", "TVShow to display: " + tvShowToSave.toString());
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
                        ifFavorite = true;
                        favoriteButton.setBackgroundResource(R.drawable.likefull);
                        AsyncTask.execute(new Runnable() {
                            @Override
                            public void run() {
                                Long insert = tvShowDao.insert(tvShowToSave);
                                tvShowToSave.setId(insert);
                                getImageToDB(tvShowToSave);
                            }
                        });
                        Toast.makeText(ShowDetailsActivity.this, "Tv Series add to favorite :)", Toast.LENGTH_SHORT).show();
                    } else {
                        ifFavorite = false;
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
    }


}
