package com.example.exomat.tvseriesinfo.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TVShow implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private Long id;
    private String name;
    private String premiere;
    private String status;
    private String summary;
    private String nextEpisodeLink;
    private String nextEpisodeDate;
    private String nextEpisodeName;
    private String nextEpisodeSE;
    private String nextEpisodeSummary;
    private String lastEpisodeLink;
    private String lastEpisodeDate;
    private String lastEpisodeSE;
    private String selfLink;
    private String imgLink;
    private byte[] imageByteArray;
}
