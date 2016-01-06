package com.bmw.interview.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;

import com.bmw.interview.R;
import com.bmw.interview.widgets.MySupportMapFragment;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapActivity extends FragmentActivity {

    public static final String MAP_FRAGMENT_TAG = "MAP_FRAGMENT_TAG";
    private static final int ZOOM = 16;
    private MySupportMapFragment mMapFragment;
    private static GoogleMap mGoogleMap;
    private String name = "";
    private double lat, lon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        Intent intent = getIntent();
        if (intent != null) {
            name = intent.getStringExtra("name");
            lat = intent.getDoubleExtra("lat", 0);
            lon = intent.getDoubleExtra("lon", 0);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        setupMapFragment();
    }

    private void setupMapFragment() {
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (status == ConnectionResult.SUCCESS) {
            mMapFragment = new MySupportMapFragment();
            mMapFragment.itsMapViewCreatedListener = new MySupportMapFragment.MapViewCreatedListener() {
                @Override
                public void onMapCreated() {
                    handleMapCreated();
                }
            };
            FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
            fragmentTransaction.add(R.id.mapcontainer, mMapFragment, MAP_FRAGMENT_TAG);
            fragmentTransaction.commitAllowingStateLoss();
        } else {
            // TODO handle no service availability via some UI cue
        }
    }

    private void handleMapCreated() {
        mGoogleMap = mMapFragment.getMap();

        if (mGoogleMap == null) {
            return;
        }

        setMapOptions();
        addPin();
    }

    private void setMapOptions() {
        mGoogleMap.getUiSettings().setMyLocationButtonEnabled(false);
        mGoogleMap.setMyLocationEnabled(true);
        mGoogleMap.getUiSettings().setCompassEnabled(false);
        mGoogleMap.getUiSettings().setZoomControlsEnabled(false);
        mGoogleMap.getUiSettings().setAllGesturesEnabled(true);
        mGoogleMap.getUiSettings().setTiltGesturesEnabled(true);
        mGoogleMap.getUiSettings().setRotateGesturesEnabled(true);
    }

    private void addPin() {
        mGoogleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lon), ZOOM), 1000, new GoogleMap.CancelableCallback() {

            @Override
            public void onFinish() {
                MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(lat, lon)).title(name);
                Marker marker = mGoogleMap.addMarker(markerOptions);
                marker.showInfoWindow();
            }

            @Override
            public void onCancel() {

            }
        });
    }
}
