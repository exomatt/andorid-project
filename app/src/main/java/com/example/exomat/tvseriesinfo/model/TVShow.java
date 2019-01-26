package com.example.exomat.tvseriesinfo.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TVShow {
    @PrimaryKey(autoGenerate = true)
    private Long id;
    private String name;
    private String premiere;
    private String status;
    private String summary;
    private String nextEpisode;
    private String previousEpisode;
    private String selfLink;
    private String imgLink;
    private byte[] imageByteArray;
}
