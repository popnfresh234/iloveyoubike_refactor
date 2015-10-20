package com.dmtaiwan.alexander.jsontest.Models;

/**
 * Created by Alexander on 10/19/2015.
 */
public class Station {

    private int id;
    private String sna;
    private String snaen;
    private double lat;
    private double lng;
    private long mday;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSna() {
        return sna;
    }

    public void setSna(String sna) {
        this.sna = sna;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public String getSnaen() {
        return snaen;
    }

    public void setSnaen(String snaen) {
        this.snaen = snaen;
    }

    public long getMday() {
        return mday;
    }

    public void setMday(long mday) {
        this.mday = mday;
    }

}
