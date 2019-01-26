package com.example.exomat.tvseriesinfo;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

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
        initalizeList();
        recyclerView = view.findViewById(R.id.recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(new RecyclerViewAdapter(list));

        return view;
    }

    private void initalizeList() {

    }

    private class RecyclerViewHolder extends RecyclerView.ViewHolder {
        private CardView mCardView;
        private TextView mTextView;
        private ImageView mImageView;
        private Button mButton;

        public RecyclerViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public RecyclerViewHolder(LayoutInflater inflater, ViewGroup conatainer) {
            super(inflater.inflate(R.layout.card_view, conatainer, false));

//            mButton = itemView.findViewById(R.id.button);
            mCardView = itemView.findViewById(R.id.card_container);
//            mTextView = itemView.findViewById(R.id.text_holder);
//            mImageView = itemView.findViewById(R.id.image_holder);
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
            recyclerViewHolder.mTextView.setText(mList.get(i).getName());
//            recyclerViewHolder.mImageView.setImageDrawable(getResources().getDrawable(mList.get(i).));
        }


        @Override
        public int getItemCount() {
            return mList.size();
        }

    }
}
