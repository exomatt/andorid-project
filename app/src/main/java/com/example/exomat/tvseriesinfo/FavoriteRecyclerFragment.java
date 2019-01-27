package com.example.exomat.tvseriesinfo;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.exomat.tvseriesinfo.dao.TVShowDao;
import com.example.exomat.tvseriesinfo.database.AppDatabase;
import com.example.exomat.tvseriesinfo.model.TVShow;

import java.util.ArrayList;
import java.util.List;

public class FavoriteRecyclerFragment extends Fragment {
    List<TVShow> list;
    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;

    public static Fragment newInstance() {
        return new FavoriteRecyclerFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_recycler_fragment, container, false);
        list = new ArrayList<>();
        initializeList();
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new RecyclerViewAdapter(list);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void initializeList() {
        new MyAsyncTask().execute();
    }

    private class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private CardView mCardView;
        private TextView mTextViewName;
        private TextView mTextViewDate;
        private TextView mTextViewStatus;
        private TextView mTextViewEpisode;
        private ImageView mImageView;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public RecyclerViewHolder(LayoutInflater inflater, ViewGroup conatainer) {
            super(inflater.inflate(R.layout.card_view, conatainer, false));

            mCardView = itemView.findViewById(R.id.card_container);
            mTextViewName = itemView.findViewById(R.id.textCardName);
            mTextViewDate = itemView.findViewById(R.id.textCardDate);
            mTextViewStatus = itemView.findViewById(R.id.textCardStatus);
            mTextViewEpisode = itemView.findViewById(R.id.textCardEpisode);
            mImageView = itemView.findViewById(R.id.imageViewCard);

        }
    }

    private class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewHolder> {
        private List<TVShow> mList;

        public RecyclerViewAdapter(List<TVShow> list) {
            this.mList = list;
        }

        @NonNull
        @Override
        public RecyclerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            LayoutInflater inflater = LayoutInflater.from(getActivity());

            //            viewGroup.setOnClickListener(new View.OnClickListener() {

//                @Override
//                public void onClick(View v) {
//                    Intent intent = new Intent(getContext(), ShowDetailsActivity.class);
//                    intent.putExtra("Show", currentItem);
//                    startActivity(intent);
//                }
//            });
            return new RecyclerViewHolder(inflater, viewGroup);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerViewHolder recyclerViewHolder, int i) {
            final TVShow tvShow = list.get(i);
            recyclerViewHolder.mTextViewName.setText(tvShow.getName());
            recyclerViewHolder.mTextViewDate.setText(tvShow.getPremiere());
            recyclerViewHolder.mTextViewStatus.setText(tvShow.getStatus());
            final String url = tvShow.getSelfLink();
            recyclerViewHolder.mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ShowDetailsActivity.class);
                    intent.putExtra("ShowUrl", url);
                    getActivity().startActivity(intent);
                }
            });
            String lastEpisode = tvShow.getLastEpisodeDate();
            if (lastEpisode != null) {
                recyclerViewHolder.mTextViewEpisode.setText(tvShow.getLastEpisodeName() + " " + tvShow.getLastEpisodeSE() + " " + lastEpisode);
            }
            if (tvShow.getImageByteArray() != null) {
//                Picasso.with(getContext()).
                recyclerViewHolder.mImageView.setImageBitmap(ImageUtils.getImage(tvShow.getImageByteArray()));
            }
//            recyclerViewHolder.mImageView.setImageDrawable(getResources().getDrawable(mList.get(i).));
        }


        @Override
        public int getItemCount() {
            return mList.size();
        }

    }

    private void onCardClickMethod(TVShow tvShow) {
        Intent intent = new Intent(getContext(), ShowDetailsActivity.class);
        intent.putExtra("Show", tvShow);
        startActivity(intent);
    }

    public class MyAsyncTask extends AsyncTask<Void, Void, String> {
        public AsyncResponse delegate = null;

        @Override
        protected String doInBackground(Void... voids) {
            AppDatabase appDatabase = Room.databaseBuilder(getContext(), AppDatabase.class, "database-tvshow").build();
            final TVShowDao tvShowDao = appDatabase.tvShowDao();
            list.addAll(tvShowDao.getAll());
            if (list == null || list.isEmpty()) {
                return null;
            }
            Log.i("TVINFO", "w bazie " + list.get(0).toString());
            Log.d("test", "size of list: " + list.size());
            appDatabase.close();
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            adapter = new RecyclerViewAdapter(list);
            recyclerView.setAdapter(adapter);
        }
    }
}
