package com.bmw.interview.utils;

import com.bmw.interview.model.BMWLocation;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public final class MyObjectMapper {

    public static List<BMWLocation> parseLocations(String json) {
        Type listType = new TypeToken<List<BMWLocation>>() {}.getType();
        return new Gson().fromJson(json, listType);
    }
}

