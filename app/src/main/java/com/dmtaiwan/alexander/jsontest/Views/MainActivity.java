package com.dmtaiwan.alexander.jsontest.Views;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.dmtaiwan.alexander.jsontest.Models.Station;
import com.dmtaiwan.alexander.jsontest.Presenters.MainPresenter;
import com.dmtaiwan.alexander.jsontest.Presenters.MainPresenterImpl;
import com.dmtaiwan.alexander.jsontest.R;
import com.dmtaiwan.alexander.jsontest.Utilities.RecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity implements MainView{

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private MainPresenter mPresenter;
    private RecyclerAdapter mAdapter;

    @Bind(R.id.empty_view)
    View mEmptyView;

    @Bind(R.id.recycler_view_station_list)
    RecyclerView mRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);

        mAdapter = new RecyclerAdapter(mEmptyView);
        mRecyclerView.setAdapter(mAdapter);

        //Create presenter
        mPresenter = new MainPresenterImpl(this, this);
        mPresenter.requestData();


    }


    @Override
    public void fillAdapter(List<Station> stationList) {
        if (stationList != null) {
            mAdapter.updateData(stationList);
        } else {
            mAdapter.setEmptyView();
        }

    }
}
