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
import android.widget.Toast;

import com.example.exomat.tvseriesinfo.dao.TVShowDao;
import com.example.exomat.tvseriesinfo.database.AppDatabase;
import com.example.exomat.tvseriesinfo.model.TVShow;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import lombok.Setter;

public class TodayRecyclerFragment extends Fragment implements AsyncResponse {
    public static final String TODAY_ACT = "TodayAct";
    List<TVShow> list;
    RecyclerView recyclerView;
    RecyclerViewAdapter adapter;

    public static Fragment newInstance() {
        return new TodayRecyclerFragment();
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
        MyAsyncTask myAsyncTask = new MyAsyncTask();
        myAsyncTask.setDelegate(this);
        myAsyncTask.execute();
    }

    @Override
    public void processFinish(String output) {
        Toast.makeText(getContext(), output, Toast.LENGTH_SHORT).show();
    }

    private class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private CardView mCardView;
        private TextView mTextViewName;
        private TextView mTextViewDate;
        private TextView mTextViewEpisode;
        private ImageView mImageView;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public RecyclerViewHolder(LayoutInflater inflater, ViewGroup conatainer) {
            super(inflater.inflate(R.layout.today_card_view, conatainer, false));

            mCardView = itemView.findViewById(R.id.today_card_container);
            mTextViewName = itemView.findViewById(R.id.textCardNameToday);
            mTextViewDate = itemView.findViewById(R.id.textCardDateToday);
            mTextViewEpisode = itemView.findViewById(R.id.textCardEpisodeToday);
            mImageView = itemView.findViewById(R.id.imageViewCardToday);

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
            return new RecyclerViewHolder(inflater, viewGroup);
        }

        @Override
        public void onBindViewHolder(@NonNull RecyclerViewHolder recyclerViewHolder, int i) {
            final TVShow tvShow = list.get(i);
            recyclerViewHolder.mTextViewName.setText(tvShow.getName());
            recyclerViewHolder.mTextViewDate.setText(tvShow.getPremiere());
            final String name = tvShow.getName();
            recyclerViewHolder.mCardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(getContext(), EpisodeActivity.class);
                    intent.putExtra("ShowUrl", tvShow.getSelfLink());
                    startActivity(intent);
                }
            });
            String nextEpisode = tvShow.getNextEpisodeDate();
            if (nextEpisode != null) {
                recyclerViewHolder.mTextViewEpisode.setText(tvShow.getNextEpisodeName() + " " + tvShow.getNextEpisodeSE() + " " + nextEpisode);
            }
            if (tvShow.getImageByteArray() != null) {
                recyclerViewHolder.mImageView.setImageBitmap(ImageUtils.getImage(tvShow.getImageByteArray()));
            }
        }


        @Override
        public int getItemCount() {
            return mList.size();
        }

    }

    @Setter
    public class MyAsyncTask extends AsyncTask<Void, Void, String> {
        public static final String YYYY_MM_DD = "YYYY-MM-DD";
        private AsyncResponse delegate = null;

        @Override
        protected String doInBackground(Void... voids) {
            AppDatabase appDatabase = Room.databaseBuilder(getContext(), AppDatabase.class, "database-tvshow").build();
            Date today = new Date();
            String date = new SimpleDateFormat(YYYY_MM_DD).format(today);
            final TVShowDao tvShowDao = appDatabase.tvShowDao();
            List<TVShow> allShows = tvShowDao.findByDate(date);
            list.addAll(allShows);
            if (list == null || list.isEmpty()) {
                return getString(R.string.nothin_new);
            }
            Log.i(TODAY_ACT, "doInBackground:  " + date);
            Log.i(TODAY_ACT, "w bazie " + list.get(0).toString());
            Log.d(TODAY_ACT, "size of list: " + list.size());
            appDatabase.close();
            return getString(R.string.something_new);
        }

        @Override
        protected void onPostExecute(String result) {
            adapter = new RecyclerViewAdapter(list);
            recyclerView.setAdapter(adapter);
            delegate.processFinish(result);
        }
    }
}
