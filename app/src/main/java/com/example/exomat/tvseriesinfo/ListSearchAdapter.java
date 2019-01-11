package com.example.exomat.tvseriesinfo;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.exomat.tvseriesinfo.pojo.TvShowResult;

import java.util.List;

public class ListSearchAdapter extends BaseAdapter {
    private List<TvShowResult> tvShows;
    private Context context;

    public ListSearchAdapter(List<TvShowResult> tvShows, Context context) {
        this.tvShows = tvShows;
        this.context = context;
    }

    @Override
    public int getCount() {
        return tvShows.size();
    }

    @Override
    public Object getItem(int position) {
        return tvShows.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = View.inflate(context, R.layout.list_item, null);
        TextView name = view.findViewById(R.id.nameText);
        TextView year = view.findViewById(R.id.yearText);
        TvShowResult tvShowResult = tvShows.get(position);
        name.setText(tvShowResult.getShow().getName());
        year.setText(tvShowResult.getShow().getPremiered());
        return view;
    }
}
