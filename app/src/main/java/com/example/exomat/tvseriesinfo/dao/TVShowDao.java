package com.example.exomat.tvseriesinfo.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import com.example.exomat.tvseriesinfo.model.TVShow;

import java.util.List;

@Dao
public interface TVShowDao {
    @Insert
    Long insert(TVShow tvShow);

    @Insert
    void insertAll(TVShow... tvShows);

    @Delete
    void delete(TVShow tvShow);

    @Update
    void update(TVShow tvShow);

    @Query("Select * from tvshow")
    List<TVShow> getAll();
}
