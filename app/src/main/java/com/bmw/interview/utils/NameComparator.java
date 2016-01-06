package com.bmw.interview.utils;

import com.bmw.interview.model.BMWLocation;

import java.util.Comparator;

public class NameComparator implements Comparator<BMWLocation> {
    @Override
    public int compare(BMWLocation o1, BMWLocation o2) {
        return o1.getName().compareTo(o2.getName());
    }
}
