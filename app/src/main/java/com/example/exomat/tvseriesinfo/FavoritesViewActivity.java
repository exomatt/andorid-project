package com.example.exomat.tvseriesinfo;

import android.support.v4.app.Fragment;

public class FavoritesViewActivity extends SingleFragment {


    @Override
    protected Fragment createFragment() {
        return new RecyclerFragment().newInstance();
    }
}
