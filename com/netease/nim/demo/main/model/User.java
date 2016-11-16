package com.netease.nim.demo.main.model;

import java.io.DataOutput;
import java.io.Serializable;

/**
 * Created by dell on 2016/11/5.
 */
public class User implements Serializable {
    private String username;
    private String photopath;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    private Double latitude;

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    private Double longitude;

    public void setPhotopath(String photopath) {
        this.photopath = photopath;
    }

    public String getPhotopath() {
        return photopath;
    }

}
