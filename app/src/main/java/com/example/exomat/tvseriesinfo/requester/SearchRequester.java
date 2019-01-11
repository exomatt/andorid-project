package com.example.exomat.tvseriesinfo.requester;

import android.content.Context;
import android.util.Log;
import android.widget.ListView;
import android.widget.Toast;

import com.example.exomat.tvseriesinfo.ListSearchAdapter;
import com.example.exomat.tvseriesinfo.pojo.TvShowResult;

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
                for (TvShowResult tvShowResult : response.body()) {
                    Log.e("tag", tvShowResult.getShow().getName().toString());
                }
                Log.e("TAGRESPONSE", response.body().get(0).getShow().toString());
            }

            @Override
            public void onFailure(Call<List<TvShowResult>> call, Throwable t) {
                Log.e("Error tag", "failure " + t.toString());
                Toast.makeText(context, "Some problem with search that TV SHOW :(", Toast.LENGTH_SHORT).show();
            }
        });
        Log.e("tag", "searchRequest: finish ");

    }

    private interface Service {
        @GET("search/shows")
        Call<List<TvShowResult>> searchTVShow(@Query("q") String query);
    }
}
