package com.dmtaiwan.alexander.jsontest.Bus;

import com.dmtaiwan.alexander.jsontest.Models.Station;

/**
 * Created by Alexander on 10/21/2015.
 */
public class RecyclerClickEvent {
    private Station station;

    public RecyclerClickEvent(Station station) {
        this.station = station;
    }

    public Station getStation() {
        return station;
    }
}
