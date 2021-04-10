
package com.example.queu_e.model;

import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Activity {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("day")
    @Expose
    private List<Integer> day = null;
    @SerializedName("starthour")
    @Expose
    private Integer starthour;
    @SerializedName("startmin")
    @Expose
    private Integer startmin;
    @SerializedName("endhour")
    @Expose
    private Integer endhour;
    @SerializedName("endmin")
    @Expose
    private Integer endmin;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Integer> getDay() {
        return day;
    }

    public void setDay(List<Integer> day) {
        this.day = day;
    }

    public Integer getStarthour() {
        return starthour;
    }

    public void setStarthour(Integer starthour) {
        this.starthour = starthour;
    }

    public Integer getStartmin() {
        return startmin;
    }

    public void setStartmin(Integer startmin) {
        this.startmin = startmin;
    }

    public Integer getEndhour() {
        return endhour;
    }

    public void setEndhour(Integer endhour) {
        this.endhour = endhour;
    }

    public Integer getEndmin() {
        return endmin;
    }

    public void setEndmin(Integer endmin) {
        this.endmin = endmin;
    }

}
