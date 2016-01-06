package com.bmw.interview.network;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

public class RetrofitRestClient {
    private static final String BASE_URL = "http://localsearch.azurewebsites.net";
    private RetrofitApiUtils apiUtils;

    public RetrofitRestClient() {
        Gson gson = new GsonBuilder()
                .setDateFormat("yyyy'-'MM'-'dd'T'HH':'mm':'ss'.'SSS'Z'")
                .create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.FULL)
                .setEndpoint(BASE_URL)
                .setConverter(new GsonConverter(gson))
                .build();

        apiUtils = restAdapter.create(RetrofitApiUtils.class);
    }

    public RetrofitApiUtils getApiUtils() {
        return apiUtils;
    }
}
