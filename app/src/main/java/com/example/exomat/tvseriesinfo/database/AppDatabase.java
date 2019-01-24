package com.example.exomat.tvseriesinfo.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.exomat.tvseriesinfo.dao.TVShowDao;
import com.example.exomat.tvseriesinfo.model.TVShow;

@Database(entities = {TVShow.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract TVShowDao tvShowDao();
}
