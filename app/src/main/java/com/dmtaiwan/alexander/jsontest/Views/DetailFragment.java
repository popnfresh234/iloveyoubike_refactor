package com.dmtaiwan.alexander.jsontest.Views;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.Space;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
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
 * Created by Alexander on 10/23/2015.
 */
public class DetailFragment extends Fragment {
    private static final String LOG_TAG = DetailFragment.class.getSimpleName();

    private Station mStation;

    private double mStationLat;
    private double mStationLong;
    private String mStationName;
    private int mBikesAvailable;
    private int mSpacesAvailable;
    private String mLastUpdate;
    private ShareActionProvider mShareActionProvider;

    @Bind(R.id.image_view_station_detail_status)
    ImageView mStatus;

    @Bind(R.id.text_view_station_detail_station_name)
    TextView mStationNameTextView;

    @Bind(R.id.text_view_station_detail_distance)
    TextView mDistanceTextView;

    @Bind(R.id.text_view_station_detail_district)
    TextView mDistrictTextView;

    @Bind(R.id.text_view_station_detail_bikes)
    TextView mBikesAvailableTextView;

    @Bind(R.id.text_view_station_detail_spaces)
    TextView mSpacesAvailableTextView;

    @Bind(R.id.station_detail_container)
    LinearLayout mStationDetailContainer;

    @Bind(R.id.linear_layout_station_title)
    LinearLayout mTitleView;

    @Bind(R.id.linear_layout_station_body)
    LinearLayout mBodyView;

    @Bind(R.id.text_view_station_detail_empty)
    TextView mEmptyView;
    //End for empty view

    @Bind(R.id.button_station_detail_favorite)
    ImageButton mFavoriteButton;

    @Bind(R.id.button_station_detail_map)
    ImageButton mMapButton;

    @Nullable
    @Bind(R.id.toolbar_detail)
    Toolbar mToolbar;

    //Spacers for Nearest Station Fragment in Tablet Mode
    @Nullable
    @Bind(R.id.grid_space_left)
    Space mGridSpaceLeft;

    @Nullable
    @Bind(R.id.grid_space_right)
    Space mGridSpaceRight;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_station_detail, container, false);
        ButterKnife.bind(this, rootView);

        //Get the station from the intent
        if (getArguments() != null) {
            mStation = getArguments().getParcelable(Utilities.EXTRA_STATION);
            loadDetails();
        }

        //Set action bar
        ((AppCompatActivity)getActivity()).setSupportActionBar(mToolbar);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setDisplayShowTitleEnabled(false);
        setHasOptionsMenu(true);
        return rootView;


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detail, menu);
        MenuItem menuItem = menu.findItem(R.id.action_share);
        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        setShareIntent();
    }

    private void loadDetails() {
        //Get spaces available and set status icon
        mBikesAvailable = mStation.getSbi();
        mSpacesAvailable = mStation.getBemp();
        mBikesAvailableTextView.setText(String.valueOf(mBikesAvailable));
        mSpacesAvailableTextView.setText(String.valueOf(mSpacesAvailable));
        mStatus.setImageResource(Utilities.getStatusIconDrawable(mBikesAvailable, mSpacesAvailable, Utilities.ICON_SIZE_LARGE));


        //Get the preferred language and set language based fields
        String language = PreferenceManager.getDefaultSharedPreferences(getActivity())
                .getString(getString(R.string.pref_key_language), getString(R.string.pref_language_english));

        if (language.equals(getString(R.string.pref_language_english))) {
            mStationName = mStation.getSnaen();
            mStationNameTextView.setText(mStationName);
            mDistrictTextView.setText(mStation.getSareaen());
            //If pinyin
//        } else if (language.equals(getActivity().getString(R.string.pref_language_pinyin))) {
//            int stringId = getResources().getIdentifier("station" + String.valueOf(mStationId), "string", getActivity().getPackageName());
//            String stationName = getString(stringId);
//            mStationNameTextView.setText(stationName);
//            mDistrictTextView.setText(cursor.getString(StationContract.COL_STATION_DISTRICT_EN));
        } else {
            mStationName = mStation.getSna();
            mStationNameTextView.setText(mStationName);
            mDistrictTextView.setText(mStation.getSarea());
        }

        //Set the distance from user's location.  If no user location, set to no data
        Location userLocation = Utilities.getUserLocation(getActivity());
        mStationLat = mStation.getLat();
        mStationLong = mStation.getLng();

        if (userLocation != null) {
            float distance = Utilities.calculateDistance(mStationLat, mStationLong, userLocation);
            mDistanceTextView.setText(Utilities.formatDistance(distance));
        } else {
            mDistanceTextView.setText(getString(R.string.text_view_station_detail_no_data));
        }

        //Set the last updated time
        mLastUpdate = Utilities.formatTime(String.valueOf(mStation.getMday()));

        //Set share intent
        if (mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(createShareIntent());
        }
    }

    public void setShareIntent() {
        if (mStationName != null) {
            mShareActionProvider.setShareIntent(createShareIntent());
        }
    }

    public Intent createShareIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        shareIntent.setType("text/plain");
        String shareText = Utilities.buildShareText(getActivity(), String.valueOf(mBikesAvailable), String.valueOf(mSpacesAvailable), mStationName, mLastUpdate);
        shareIntent.putExtra(Intent.EXTRA_TEXT, shareText);

        return shareIntent;
    }
}
