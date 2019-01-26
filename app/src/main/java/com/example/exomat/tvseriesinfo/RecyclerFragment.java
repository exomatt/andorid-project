package com.example.exomat.tvseriesinfo;

import android.arch.persistence.room.Room;
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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.exomat.tvseriesinfo.dao.TVShowDao;
import com.example.exomat.tvseriesinfo.database.AppDatabase;
import com.example.exomat.tvseriesinfo.model.TVShow;

import java.util.ArrayList;
import java.util.List;

public class RecyclerFragment extends Fragment {
    List<TVShow> list;
    RecyclerView recyclerView;

    public static Fragment newInstance() {
        return new RecyclerFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_recycler_fragment, container, false);
        list = new ArrayList<>();

        initializeList();
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new RecyclerViewAdapter(list));

        return view;
    }

    private void initializeList() {

        AsyncTask.execute(new Runnable() {
            @Override
            public void run() {
                AppDatabase appDatabase = Room.databaseBuilder(getContext(), AppDatabase.class, "database-tvshow").build();
                final TVShowDao tvShowDao = appDatabase.tvShowDao();
                list = tvShowDao.getAll();
                if (list == null || list.isEmpty()) {
                    return;
                }
                Log.i("TVINFO", "w bazie " + list.get(0).toString());
                Log.d("test", "size of list: " + list.size());
                appDatabase.close();
                recyclerView.setAdapter(new RecyclerViewAdapter(list));
            }
        });
    }

    private class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private CardView mCardView;
        private TextView mTextViewName;
        private TextView mTextViewDate;
        private TextView mTextViewStatus;
        private TextView mTextViewEpisode;
        private ImageView mImageView;
        private Button mButton;

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
//            mCardView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    mCardView.setSelected(false);
//                    mCardView.setAlpha((float) 1.0);
//                    if(selected>0)
//                        selected--;
//                }
//            });
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
            recyclerViewHolder.mTextViewName.setText(list.get(i).getName());
            recyclerViewHolder.mTextViewDate.setText(list.get(i).getPremiere());
            recyclerViewHolder.mTextViewStatus.setText(list.get(i).getStatus());
            String nextEpisode = list.get(i).getNextEpisode();
            if (nextEpisode != null) {
                recyclerViewHolder.mTextViewEpisode.setText(nextEpisode);
            }
//            recyclerViewHolder.mImageView.setImageDrawable(getResources().getDrawable(mList.get(i).));
        }


        @Override
        public int getItemCount() {
            return mList.size();
        }

    }
}
