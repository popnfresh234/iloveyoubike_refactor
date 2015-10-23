package com.dmtaiwan.alexander.jsontest.Views;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.Space;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.dmtaiwan.alexander.jsontest.Models.Station;
import com.dmtaiwan.alexander.jsontest.R;
import com.dmtaiwan.alexander.jsontest.Utilities.Utilities;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Alexander on 10/21/2015.
 */
public class DetailActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_station_detail);
        if (savedInstanceState == null) {
            Station station = getIntent().getParcelableExtra(Utilities.EXTRA_STATION);
            DetailFragment detailFragment = new DetailFragment();
            Bundle args = new Bundle();
            args.putParcelable(Utilities.EXTRA_STATION, station);
            detailFragment.setArguments(args);
            getSupportFragmentManager().beginTransaction().add(R.id.station_detail_container, detailFragment).commit();
        }

    }

}

