package com.dmtaiwan.alexander.jsontest.Views;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.dmtaiwan.alexander.jsontest.Utilities.RecyclerAdapter;
import com.dmtaiwan.alexander.jsontest.Utilities.Utilities;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.squareup.otto.Subscribe;


import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity{

    private static final String LOG_TAG = MainActivity.class.getSimpleName();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Launch Fragment
        MainFragment mainFragment = new MainFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.content_main, mainFragment, "TAG")
                .commit();


    }
}
