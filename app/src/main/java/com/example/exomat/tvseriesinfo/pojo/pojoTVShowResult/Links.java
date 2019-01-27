
package com.example.exomat.tvseriesinfo.pojo.pojoTVShowResult;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class Links implements Serializable {

    @SerializedName("self")
    @Expose
    private Self self;
    @SerializedName("previousepisode")
    @Expose
    private Previousepisode previousepisode;
    @SerializedName("nextepisode")
    @Expose
    private Nextepisode nextepisode;

    public Self getSelf() {
        return self;
    }

    public void setSelf(Self self) {
        this.self = self;
    }

    public Previousepisode getPreviousepisode() {
        return previousepisode;
    }

    public void setPreviousepisode(Previousepisode previousepisode) {
        this.previousepisode = previousepisode;
    }

    public Nextepisode getNextepisode() {
        return nextepisode;
    }

    public void setNextepisode(Nextepisode nextepisode) {
        this.nextepisode = nextepisode;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("self", self).append("previousepisode", previousepisode).append("nextepisode", nextepisode).toString();
    }

}
