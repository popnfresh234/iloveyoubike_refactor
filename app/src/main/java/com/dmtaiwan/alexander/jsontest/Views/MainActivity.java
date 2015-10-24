package com.dmtaiwan.alexander.jsontest.Views;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.dmtaiwan.alexander.jsontest.Bus.EventBus;
import com.dmtaiwan.alexander.jsontest.Bus.RecyclerClickEvent;
import com.dmtaiwan.alexander.jsontest.Models.Station;
import com.dmtaiwan.alexander.jsontest.Presenters.MainPresenter;
import com.dmtaiwan.alexander.jsontest.Presenters.MainPresenterImpl;
import com.dmtaiwan.alexander.jsontest.R;
import com.dmtaiwan.alexander.jsontest.Utilities.LocationProvider;
import com.dmtaiwan.alexander.jsontest.Utilities.Utilities;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.squareup.otto.Subscribe;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainView, LocationProvider.LocationCallback {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();

    private LocationProvider mLocationProvider;
    private MainPresenter mPresenter;
    private List<Station> mStationsList;
    private ShareActionProvider mShareActionProvider;
    private MenuItem mShareItem;

    @Bind(R.id.toolbar)
    Toolbar mToolbar;

    @Bind(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;

    @Bind(R.id.navigation_view)
    NavigationView mNavigationView;

    @Bind(R.id.toolbar_progress_indicator)
    ProgressBar mProgressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        //Set toolbar
        setSupportActionBar(mToolbar);
        final ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_menu_white_24dp);
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        //Create presenter
        mPresenter = new MainPresenterImpl(this, this);

        //Setup location provider
        mLocationProvider = new LocationProvider(this, this);

        //Check if GooglePlay is installed
        checkPlayServices();

        //Register Bus
        EventBus.getInstance().register(this);

        //Setup drawer
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                int id = menuItem.getItemId();

                if (id == R.id.drawer_home) {
                    MainFragment mainFragment = new MainFragment();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.content_main, mainFragment, Utilities.FRAGMENT_TAG_MAIN)
                            .commit();
                }

                if (id == R.id.drawer_favourite) {
                    FavoriteFragment favoriteFragment = new FavoriteFragment();
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.content_main, favoriteFragment, Utilities.FRAGMENT_TAG_MAIN)
                            .commit();
                }

                mDrawerLayout.closeDrawers();
                return true;
            }
        });

        //If no fragment in content frame, create main fragment
        if (getSupportFragmentManager().findFragmentById(R.id.content_main) == null) {
            MainFragment mainFragment = new MainFragment();
            getSupportFragmentManager().beginTransaction().replace(R.id.content_main, mainFragment, Utilities.FRAGMENT_TAG_MAIN)
                    .commit();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        mLocationProvider.connect();
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
    public boolean onCreateOptionsMenu(Menu menu) {
        if (getResources().getBoolean(R.bool.isTablet)) {
            getMenuInflater().inflate(R.menu.menu_tablet, menu);
            mShareItem = menu.findItem(R.id.action_share);
            mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(mShareItem);
            DetailFragment detailFragment = (DetailFragment) getSupportFragmentManager().findFragmentById(R.id.content_detail);
            if (detailFragment == null) {
                mShareItem.setVisible(false);
            }

        } else {
            getMenuInflater().inflate(R.menu.menu_main, menu);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }

        if (id == R.id.action_settings) {
            Intent settingsIntent = new Intent(this, SettingsActivity.class);
            startActivity(settingsIntent);
        }

        if (id == R.id.action_refresh) {
            mPresenter.requestData();
        }
        return super.onOptionsItemSelected(item);
    }

    public void loadDetailFragment(Station station) {
        if (getResources().getBoolean(R.bool.isTablet)) {
            Bundle args = new Bundle();
            args.putParcelable(Utilities.EXTRA_STATION, station);
            DetailFragment detailFragment = new DetailFragment();
            detailFragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().replace(R.id.content_detail, detailFragment, "DETAIL").commit();
        }
    }

    @Override
    public void fillAdapter(List<Station> stationList) {
        mStationsList = stationList;
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.content_main);
        if (fragment instanceof MainFragment) {
            ((MainFragment) fragment).fillAdapter(stationList);
        }else if (fragment instanceof FavoriteFragment) {
            ((FavoriteFragment) fragment).fillAdapter(stationList);
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
        Utilities.setUserLocation(location, this);
        mPresenter.requestData();
    }

    @Subscribe
    public void onRecyclerViewClick(RecyclerClickEvent recyclerClickEvent) {
        Log.i(LOG_TAG, "event");
        if (getResources().getBoolean(R.bool.isTablet)) {
            loadDetailFragment(recyclerClickEvent.getStation());
        } else {
            Station station = recyclerClickEvent.getStation();
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra(Utilities.EXTRA_STATION, station);
            startActivity(intent);
        }
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        Utilities.PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(LOG_TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }

    public void setShareIntent(Intent shareIntent) {
        if (mShareActionProvider != null) {
            mShareItem.setVisible(true);
            mShareActionProvider.setShareIntent(shareIntent);
        }
    }

    public void requestData(boolean isFavorites) {
        mPresenter.requestData();
    }
}
