
package com.example.exomat.tvseriesinfo.pojo.pojoShowDetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Rating {

    @SerializedName("average")
    @Expose
    private Double average;

    public Double getAverage() {
        return average;
    }

    public void setAverage(Double average) {
        this.average = average;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("average", average).toString();
    }

}
