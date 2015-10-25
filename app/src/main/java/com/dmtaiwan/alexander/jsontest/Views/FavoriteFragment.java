package com.dmtaiwan.alexander.jsontest.Views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.dmtaiwan.alexander.jsontest.Bus.EventBus;
import com.dmtaiwan.alexander.jsontest.Bus.FavoritesEvent;
import com.dmtaiwan.alexander.jsontest.Models.Station;
import com.dmtaiwan.alexander.jsontest.R;
import com.dmtaiwan.alexander.jsontest.Utilities.RecyclerAdapter;
import com.dmtaiwan.alexander.jsontest.Utilities.Utilities;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Alexander on 10/22/2015.
 */
public class FavoriteFragment extends Fragment {

    private static final String LOG_TAG = FavoriteFragment.class.getSimpleName();


    private RecyclerAdapter mAdapter;
    private ArrayList<String> mFavoritesArray;

    @Bind(R.id.empty_view)
    View mEmptyView;

    @Bind(R.id.recycler_view_station_list)
    RecyclerView mRecyclerView;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Get favorites array
        mFavoritesArray = Utilities.getFavoriteArray(getActivity());
        //Register event bus
        EventBus.getInstance().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, rootView);

        //Set layout manager
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);

        //Set adapter
        mAdapter = new RecyclerAdapter(getActivity(), mEmptyView);
        mRecyclerView.setAdapter(mAdapter);

        //Request data from MainActivity
        if (getActivity() instanceof MainActivity) {
            ((MainActivity) getActivity()).requestData();
        }


        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getInstance().unregister(this);
    }

    public void fillAdapter(List<Station> stationList) {
        if (stationList != null && mFavoritesArray!= null) {
            List<Station> favoriteStations = new ArrayList<Station>();
            for (String id : mFavoritesArray) {
                for (int i = 0; i < stationList.size(); i++) {
                    Station station = stationList.get(i);
                    if (String.valueOf(station.getId()).equals(id)) {
                        favoriteStations.add(station);
                    }
                }
            }
            mAdapter.updateData(favoriteStations);
        }else{
            mAdapter.setEmptyView();
        }
    }

    @Subscribe
    public void onFavoritesEvent(FavoritesEvent favoritesEvent) {
        mFavoritesArray = favoritesEvent.getFavoritesArray();
    }
}
