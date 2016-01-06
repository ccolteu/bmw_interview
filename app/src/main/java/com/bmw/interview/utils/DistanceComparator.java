package com.bmw.interview.utils;

import com.bmw.interview.model.BMWLocation;

import java.util.Comparator;

public class DistanceComparator implements Comparator<BMWLocation> {
    @Override
    public int compare(BMWLocation o1, BMWLocation o2) {
        return Double.compare(o1.getDistance(),o2.getDistance());
    }
}
