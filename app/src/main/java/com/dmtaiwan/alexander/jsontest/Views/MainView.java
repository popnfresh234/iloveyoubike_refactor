package com.dmtaiwan.alexander.jsontest.Views;

import com.dmtaiwan.alexander.jsontest.Models.Station;

import java.util.List;

/**
 * Created by Alexander on 10/19/2015.
 */
public interface MainView {
    void setData(List<Station> stationList);

    void showProgress();

    void hideProgress();
}
