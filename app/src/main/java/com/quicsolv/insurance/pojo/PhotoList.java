package com.quicsolv.insurance.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PhotoList implements Serializable {

    public PhotoList(String path, String description) {
        this.path = path;
        this.description = description;
    }

    @SerializedName("path")
    @Expose
    private String path;
    @SerializedName("description")
    @Expose
    private String description;

    public String getPath() {
    return path;
    }

    public void setPath(String path) {
    this.path = path;
    }

    public String getDescription() {
    return description;
    }

    public void setDescription(String description) {
this.description = description;
}

}