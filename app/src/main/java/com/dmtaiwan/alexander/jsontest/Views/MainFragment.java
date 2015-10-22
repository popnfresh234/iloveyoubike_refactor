package com.dmtaiwan.alexander.jsontest.Views;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.dmtaiwan.alexander.jsontest.Bus.EventBus;
import com.dmtaiwan.alexander.jsontest.Bus.RecyclerClickEvent;
import com.dmtaiwan.alexander.jsontest.Models.Station;
import com.dmtaiwan.alexander.jsontest.Presenters.MainPresenter;
import com.dmtaiwan.alexander.jsontest.Presenters.MainPresenterImpl;
import com.dmtaiwan.alexander.jsontest.R;
import com.dmtaiwan.alexander.jsontest.Utilities.LocationProvider;
import com.dmtaiwan.alexander.jsontest.Utilities.RecyclerAdapter;
import com.dmtaiwan.alexander.jsontest.Utilities.Utilities;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.squareup.otto.Subscribe;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Alexander on 10/22/2015.
 */
public class MainFragment extends Fragment implements MainView, LocationProvider.LocationCallback {

    private static final String LOG_TAG = MainFragment.class.getSimpleName();

    private MainPresenter mPresenter;
    private RecyclerAdapter mAdapter;
    private LocationProvider mLocationProvider;

    @Bind(R.id.empty_view)
    View mEmptyView;

    @Bind(R.id.recycler_view_station_list)
    RecyclerView mRecyclerView;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @Bind(R.id.navigation_view)
    NavigationView mNavigationView;

    @Bind(R.id.toolbar_progress_indicator)
    ProgressBar mProgressBar;



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, rootView);

        setHasOptionsMenu(true);

        //Set toolbar
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        final ActionBar actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //Setup drawer
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                Log.i(LOG_TAG, String.valueOf(menuItem.getItemId()));
                mDrawerLayout.closeDrawers();
                return true;
            }
        });


        //Set layout manager
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);


        //Set adapter
        mAdapter = new RecyclerAdapter(getActivity(), mEmptyView);
        mRecyclerView.setAdapter(mAdapter);


        //Create presenter
        mPresenter = new MainPresenterImpl(this, getActivity());
        mPresenter.requestData();

        //Setup location provider
        mLocationProvider = new LocationProvider(getActivity(), this);

        //Check if GooglePlay is installed
        checkPlayServices();

        //Register Bus
        EventBus.getInstance().register(this);


        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();
        mLocationProvider.connect();
        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mLocationProvider.disconnect();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getInstance().unregister(this);
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }

        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(getActivity(), SettingsActivity.class);
            startActivity(settingsIntent);
        }

        if (id == R.id.action_refresh) {
            mPresenter.requestData();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void fillAdapter(List<Station> stationList) {
        if (stationList != null) {
            mAdapter.updateData(stationList);
        } else {
            mAdapter.setEmptyView();
        }
    }

    @Override
    public void showProgress() {
        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        mProgressBar.setVisibility(View.INVISIBLE);
    }


    @Override
    public void handleNewLocation(Location location) {
        Utilities.setUserLocation(location, getActivity());
        mPresenter.requestData();
    }

    @Subscribe
    public void onRecyclerViewClick(RecyclerClickEvent recyclerClickEvent) {
        Log.i(LOG_TAG, "event");
        Station station = recyclerClickEvent.getStation();
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra("test", station);
        startActivity(intent);
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(),
                        Utilities.PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(LOG_TAG, "This device is not supported.");
                getActivity().finish();
            }
            return false;
        }
        return true;
    }
}
