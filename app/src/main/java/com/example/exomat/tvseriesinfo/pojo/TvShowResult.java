
package com.example.exomat.tvseriesinfo.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class TvShowResult {

    @SerializedName("score")
    @Expose
    private Double score;
    @SerializedName("show")
    @Expose
    private Show show;

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public Show getShow() {
        return show;
    }

    public void setShow(Show show) {
        this.show = show;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("score", score).append("show", show).toString();
    }

}
