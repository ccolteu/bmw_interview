package com.bmw.interview.ui;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.bmw.interview.R;
import com.bmw.interview.adapter.LocationsAdapter;
import com.bmw.interview.adapter.SortSpinnerAdapter;
import com.bmw.interview.model.BMWLocation;
import com.bmw.interview.network.RetrofitRestClient;
import com.bmw.interview.utils.DistanceComparator;
import com.bmw.interview.utils.NameComparator;
import com.bmw.interview.utils.TimeComparator;
import com.bmw.interview.utils.Utils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;

public class MainFragment extends Fragment  implements
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final String TAG = MainFragment.class.getSimpleName();

    private Activity mActivity;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private double myLat = -1;
    private double myLon = -1;

    private View mDataView;
    private ListView mListView;
    private View mInfoView;
    private TextView mInfoTextView;
    private ProgressBar mProgressView;
    private Spinner mSortSpinner;
    private static ArrayList<String> mSortArray = new ArrayList<>();
    private static final String SORT_NAME = "Name";
    private static final String SORT_DISTANCE = "Distance";
    private static final String SORT_ARRIVAL_TIME = "Arrival Time";
    private static SortSpinnerAdapter mSortAdapter;
    private static int mSort;

    private static final int STATE_GETTING_DATA = 0;
    private static final int STATE_GOT_DATA_SUCCESS = 1;
    private static final int STATE_GOT_DATA_FAILURE = 2;
    private static final int STATE_NOT_CONNECTED = 3;

    private int state;
    private boolean firstTime = true;

    private final RetrofitRestClient client = new RetrofitRestClient();
    private List<BMWLocation> bmwLocations;
    private RetrofitError retrofitError;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*
        persist state when configuration changes occur
         */
        setRetainInstance(true);
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            myLat = mLastLocation.getLatitude();
            myLon = mLastLocation.getLongitude();
            if (bmwLocations != null && bmwLocations.size() > 0) {
                calculateDistances();
                if (this.getActivity() != null) {
                    mListView.setAdapter(new LocationsAdapter(mActivity, R.layout.location_row, bmwLocations));
                }
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    @Override
    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    @Override
    public void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View contentView = inflater.inflate(R.layout.fragment_main, null);

        mActivity = getActivity();
        mDataView = contentView.findViewById(R.id.data_view);
        mSortSpinner = (Spinner) contentView.findViewById(R.id.spinner);
        mListView = (ListView) contentView.findViewById(R.id.list);
        mInfoView = contentView.findViewById(R.id.info_view);
        mInfoTextView = (TextView) contentView.findViewById(R.id.info_text);
        mProgressView = (ProgressBar) contentView.findViewById(R.id.progress);

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(mActivity)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        initSpinner();

        if (firstTime) {
            firstTime = false;
            getData();
        } else {
            updateUI();
        }

        return contentView;
    }

    private void getData() {
        if (Utils.isConnected(mActivity)) {

            state = STATE_GETTING_DATA;
            updateUI();

            client.getApiUtils().getLocations(new Callback<List<BMWLocation>>() {
                @Override
                public void success(List<BMWLocation> locations, retrofit.client.Response response) {
                    bmwLocations = locations;
                    state = STATE_GOT_DATA_SUCCESS;
                    updateUI();
                }

                @Override
                public void failure(RetrofitError error) {
                    retrofitError = error;
                    state = STATE_GOT_DATA_FAILURE;
                    updateUI();
                }
            });

        } else {
            state = STATE_NOT_CONNECTED;
            updateUI();
        }
    }

    private void updateUI() {
        switch (state) {
            case STATE_GETTING_DATA:
                showInfo(mActivity.getString(R.string.loading_data), true);
                break;
            case STATE_GOT_DATA_SUCCESS:
                showList();
                if (myLat != -1 && myLon != -1) {
                    calculateDistances();
                }
                Collections.sort(bmwLocations, new NameComparator());
                mListView.setAdapter(new LocationsAdapter(mActivity, R.layout.location_row, bmwLocations));
                mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        BMWLocation bmwLocation = bmwLocations.get(position);
                        Intent intent = new Intent(mActivity, MapActivity.class);
                        intent.putExtra("name", bmwLocation.getName());
                        intent.putExtra("lat", bmwLocation.getLatitude());
                        intent.putExtra("lon", bmwLocation.getLongitude());
                        mActivity.startActivity(intent);
                    }
                });
                break;
            case STATE_GOT_DATA_FAILURE:
                showInfo(mActivity.getString(R.string.error_loading_data), false);
                Log.e(TAG, "GET failure: " + retrofitError.toString());
                break;
            case STATE_NOT_CONNECTED:
                showInfo(mActivity.getString(R.string.no_connection), false);
                break;
        }
    }

    private void showInfo(String text, boolean showSpinner) {
        mDataView.setVisibility(View.GONE);
        mInfoView.setVisibility(View.VISIBLE);
        mProgressView.setVisibility(showSpinner ? View.VISIBLE : View.GONE);
        mInfoTextView.setText(text);
    }

    private void showList() {
        mInfoView.setVisibility(View.GONE);
        mDataView.setVisibility(View.VISIBLE);
    }

    private void initSpinner() {
        mSortArray.clear();
        mSortArray.add(SORT_NAME);
        mSortArray.add(SORT_DISTANCE);
        mSortArray.add(SORT_ARRIVAL_TIME);
        mSortAdapter = new SortSpinnerAdapter(mActivity, R.layout.sort_spinner_dropdown_view, mSortArray);
        mSortSpinner.setAdapter(mSortAdapter);
        /*
         * IMPORTANT setSelection is called before setOnItemSelectedListener
         * to prevent setSelection to auto-trigger when the listener is set
         */
        mSortSpinner.setOnItemSelectedListener(null);
        mSortSpinner.setSelection(mSort, false);
        mSortSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, final int pos, long id) {
                mSort = pos;
                SortSpinnerAdapter adapter = (SortSpinnerAdapter) parent.getAdapter();
                String sortString = adapter.getItem(pos);
                if (sortString == SORT_NAME) {
                    Collections.sort(bmwLocations, new NameComparator());
                    mListView.setAdapter(new LocationsAdapter(mActivity, R.layout.location_row, bmwLocations));
                } else if (sortString == SORT_DISTANCE) {
                    Collections.sort(bmwLocations, new DistanceComparator());
                    mListView.setAdapter(new LocationsAdapter(mActivity, R.layout.location_row, bmwLocations));
                } else if (sortString == SORT_ARRIVAL_TIME) {
                    Collections.sort(bmwLocations, new TimeComparator());
                    mListView.setAdapter(new LocationsAdapter(mActivity, R.layout.location_row, bmwLocations));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // do nothing
            }
        });
    }

    private void calculateDistances() {
        for (BMWLocation bmwLocation : bmwLocations) {
            Location location1 = new Location("feed");
            location1.setLatitude(bmwLocation.getLatitude());
            location1.setLongitude(bmwLocation.getLongitude());
            Location location2 = new Location("device");
            location2.setLatitude(myLat);
            location2.setLongitude(myLon);
            bmwLocation.setDistance(Utils.metersToMiles(location2.distanceTo(location1)));
        }
    }
}
