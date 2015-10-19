package com.dmtaiwan.alexander.jsontest.Utilities;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.dmtaiwan.alexander.jsontest.Models.Station;
import com.dmtaiwan.alexander.jsontest.R;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * Created by Alexander on 10/19/2015.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private static final String LOG_TAG = RecyclerAdapter.class.getSimpleName();
    final private View mEmptyView;
    private List<Station> mStationList;

    public RecyclerAdapter(View emptyView) {
        mEmptyView = emptyView;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_item_station_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Station station = mStationList.get(position);
        holder.stationId.setText(String.valueOf(station.getId()));
        holder.stationName.setText(station.getSna());

    }

    @Override
    public int getItemCount() {
        if (mStationList != null) {
            return mStationList.size();
        }else {
            return 0;
        }

    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @Bind(R.id.station_id)
        public TextView stationId;
        @Bind(R.id.station_name)
        public TextView stationName;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.i(LOG_TAG, "click");
        }
    }

    public void updateData(List<Station> stationList) {
        mStationList = stationList;
        notifyDataSetChanged();
        mEmptyView.setVisibility(mStationList.size() == 0? View.VISIBLE : View.GONE);
    }

    public void setEmptyView() {
        mEmptyView.setVisibility(View.VISIBLE);
    }
}
