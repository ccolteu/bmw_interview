package com.bmw.interview.utils;

import com.bmw.interview.model.BMWLocation;

import java.util.Comparator;

public class TimeComparator implements Comparator<BMWLocation> {
    @Override
    public int compare(BMWLocation o1, BMWLocation o2) {
        return Long.compare(o1.getTime(), o2.getTime());
    }
}
