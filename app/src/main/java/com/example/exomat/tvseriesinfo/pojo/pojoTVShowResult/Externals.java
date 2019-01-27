
package com.example.exomat.tvseriesinfo.pojo.pojoTVShowResult;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class Externals implements Serializable {

    @SerializedName("tvrage")
    @Expose
    private Object tvrage;
    @SerializedName("thetvdb")
    @Expose
    private Integer thetvdb;
    @SerializedName("imdb")
    @Expose
    private Object imdb;

    public Object getTvrage() {
        return tvrage;
    }

    public void setTvrage(Object tvrage) {
        this.tvrage = tvrage;
    }

    public Integer getThetvdb() {
        return thetvdb;
    }

    public void setThetvdb(Integer thetvdb) {
        this.thetvdb = thetvdb;
    }

    public Object getImdb() {
        return imdb;
    }

    public void setImdb(Object imdb) {
        this.imdb = imdb;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("tvrage", tvrage).append("thetvdb", thetvdb).append("imdb", imdb).toString();
    }

}
