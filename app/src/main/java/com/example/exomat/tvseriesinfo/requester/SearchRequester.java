package com.example.exomat.tvseriesinfo.requester;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.example.exomat.tvseriesinfo.ListSearchAdapter;
import com.example.exomat.tvseriesinfo.dao.TVShowDao;
import com.example.exomat.tvseriesinfo.database.AppDatabase;
import com.example.exomat.tvseriesinfo.model.TVShow;
import com.example.exomat.tvseriesinfo.pojo.pojoEpisode.Episode;
import com.example.exomat.tvseriesinfo.pojo.pojoShowDetails.Nextepisode;
import com.example.exomat.tvseriesinfo.pojo.pojoShowDetails.Previousepisode;
import com.example.exomat.tvseriesinfo.pojo.pojoShowDetails.ShowDetails;
import com.example.exomat.tvseriesinfo.pojo.pojoTVShowResult.TvShowResult;

import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class SearchRequester {
    public static final String url = " http://api.tvmaze.com";

    public static void searchRequest(String searchText, final ListView listView, final Context context) {
        OkHttpClient client = new OkHttpClient.Builder().readTimeout(0, TimeUnit.NANOSECONDS)
                .connectTimeout(60, TimeUnit.SECONDS).writeTimeout(60, TimeUnit.SECONDS).build();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url).client(client)
                .addConverterFactory(ScalarsConverterFactory.create()).addConverterFactory(GsonConverterFactory.create()).build();
        Service service = retrofit.create(Service.class);
        Call<List<TvShowResult>> searchTVShow = service.searchTVShow(searchText);
        searchTVShow.enqueue(new Callback<List<TvShowResult>>() {
            @Override
            public void onResponse(Call<List<TvShowResult>> call, Response<List<TvShowResult>> response) {
                listView.setAdapter(new ListSearchAdapter(response.body(), context));
                Log.e("TAGRESPONSE", response.message());
                Log.e("TAGRESPONSE", response.toString());
                if (response.body() == null) {
                    Log.e("searchRequest", "onResponse: null ");
                    return;
                }
                for (TvShowResult tvShowResult : response.body()) {
                    Log.e("tag", tvShowResult.getShow().getName());
                }
//                Log.e("TAGRESPONSE", response.body().get(0).getShow().toString());
            }

            @Override
            public void onFailure(Call<List<TvShowResult>> call, Throwable t) {
                ConnectivityManager connectivityManager
                        = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
                if (activeNetworkInfo != null && activeNetworkInfo.isConnected()) {
                    Toast.makeText(context, "Probably you don't connect to the Net :(  :(", Toast.LENGTH_SHORT).show();
                    return;
                }
                Log.e("Error tag", "failure " + t.toString());
                Toast.makeText(context, "Some problem with search that TV SHOW :(", Toast.LENGTH_SHORT).show();
            }
        });
        Log.e("tag", "searchRequest: finish ");

    }

    public static void fillEpisodes(final String episode, final TVShow tvShow, final boolean mode, final Context context) {
        OkHttpClient client = new OkHttpClient.Builder().readTimeout(0, TimeUnit.NANOSECONDS)
                .connectTimeout(60, TimeUnit.SECONDS).writeTimeout(60, TimeUnit.SECONDS).build();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url).client(client)
                .addConverterFactory(ScalarsConverterFactory.create()).addConverterFactory(GsonConverterFactory.create()).build();
        Service service = retrofit.create(Service.class);
        Call<Episode> episodeCall = service.episodeDetails(episode);
        episodeCall.enqueue(new Callback<Episode>() {
            @Override
            public void onResponse(Call<Episode> call, Response<Episode> response) {
                Log.i("EPFILL", String.valueOf(response.body()));
                Episode episodeToSave = response.body();
                if (episodeToSave == null) {
                    Log.e("fillEpisodes", "onResponse: null ");
                    return;
                }
                String airdate = episodeToSave.getAirdate();
                String summary = episodeToSave.getSummary();
                String season = "s" + episodeToSave.getSeason() + "e" + episodeToSave.getNumber();
                String name = episodeToSave.getName();
                if (mode) {
                    tvShow.setNextEpisodeDate(airdate);
                    tvShow.setNextEpisodeSummary(summary);
                    tvShow.setNextEpisodeSE(season);
                    tvShow.setNextEpisodeName(name);
                } else {
                    tvShow.setLastEpisodeDate(airdate);
                    tvShow.setLastEpisodeSE(season);
                    tvShow.setLastEpisodeName(name);
                }
                AppDatabase appDatabase = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "database-tvshow").allowMainThreadQueries().build();

                TVShowDao tvShowDao = appDatabase.tvShowDao();
                tvShowDao.update(tvShow);
                appDatabase.close();
            }

            @Override
            public void onFailure(Call<Episode> call, Throwable t) {
                Log.e("Error tag", "failure " + t.toString());
            }
        });
    }

    public static void updateShowInDB(String number, final TVShow tvShow, final Context context) {
        OkHttpClient client = new OkHttpClient.Builder().readTimeout(0, TimeUnit.NANOSECONDS)
                .connectTimeout(60, TimeUnit.SECONDS).writeTimeout(60, TimeUnit.SECONDS).build();
        Retrofit retrofit = new Retrofit.Builder().baseUrl(url).client(client)
                .addConverterFactory(ScalarsConverterFactory.create()).addConverterFactory(GsonConverterFactory.create()).build();
        Service service = retrofit.create(Service.class);
        Call<ShowDetails> tvShowResultCall = service.showDetails(number);
        tvShowResultCall.enqueue(new Callback<ShowDetails>() {
            @Override
            public void onResponse(Call<ShowDetails> call, Response<ShowDetails> response) {
                ShowDetails body = response.body();
                if (body == null) {
                    Log.e("UpdateAll", "onResponse: null ");
                    return;
                }
                TVShow newTVShow = getNewTVShow(body);
                Log.i("UpdateAll", "onResponse: " + newTVShow);
                newTVShow.setId(tvShow.getId());
                newTVShow.setImageByteArray(tvShow.getImageByteArray());
                AppDatabase appDatabase = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "database-tvshow").allowMainThreadQueries().build();
                TVShowDao tvShowDao = appDatabase.tvShowDao();
                tvShowDao.update(newTVShow);
                appDatabase.close();
                if (newTVShow.getLastEpisodeLink() != null) {
                    SearchRequester.fillEpisodes(newTVShow.getLastEpisodeLink(), newTVShow, false, context);
                }
                if (newTVShow.getNextEpisodeLink() != null) {
                    SearchRequester.fillEpisodes(newTVShow.getNextEpisodeLink(), newTVShow, true, context);
                }

            }

            @Override
            public void onFailure(Call<ShowDetails> call, Throwable t) {
                Log.e("Error tag", "failure " + t.toString());
            }
        });
    }

    private static TVShow getNewTVShow(ShowDetails showResult) {
        TVShow tvShow = new TVShow();
        tvShow.setName(showResult.getName());
        tvShow.setStatus(showResult.getStatus());
        tvShow.setPremiere(showResult.getPremiered());
        tvShow.setSummary(showResult.getSummary());
        tvShow.setSelfLink(String.valueOf(showResult.getLinks().getSelf().getHref()));
        tvShow.setImgLink(showResult.getImage().getMedium());
        Nextepisode nextepisode = showResult.getLinks().getNextepisode();
        if (nextepisode != null) {
            String href = nextepisode.getHref();
            tvShow.setNextEpisodeLink(href.substring(href.lastIndexOf("/") + 1));
        }
        Previousepisode previousepisode = showResult.getLinks().getPreviousepisode();
        if (previousepisode != null) {
            String href = previousepisode.getHref();
            tvShow.setLastEpisodeLink(href.substring(href.lastIndexOf("/") + 1));
        }
        //todo async get previous and next episode


        return tvShow;
    }

    private interface Service {
        @GET("search/shows")
        Call<List<TvShowResult>> searchTVShow(@Query("q") String query);

        @GET("episodes/{number}")
        Call<Episode> episodeDetails(@Path("number") String num);

        @GET("shows/{number}")
        Call<ShowDetails> showDetails(@Path("number") String num);
    }


}
