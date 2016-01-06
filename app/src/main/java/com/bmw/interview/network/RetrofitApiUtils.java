package com.bmw.interview.network;

import com.bmw.interview.model.BMWLocation;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;

public interface RetrofitApiUtils {

    @GET("/api/Locations")
    public void getLocations(Callback<List<BMWLocation>> callback);

}
