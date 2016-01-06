package com.bmw.interview.model;

import com.bmw.interview.utils.Utils;

import org.parceler.Parcel;

//        ID: 1986,
//        Name: "Doughnut Vault Canal",
//        Latitude: 41.883976,
//        Longitude: -87.639346,
//        Address: "11 N Canal St L30 Chicago, IL 60606",
//        ArrivalTime: "2016-01-06T13:20:30.937"

@Parcel
public class BMWLocation {
    private int ID;
    private String Name;
    private double Latitude;
    private double Longitude;
    private String Address;
    private String ArrivalTime;
    private double distance = -1;

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getArrivalTime() {
        return ArrivalTime;
    }

    public void setArrivalTime(String arrivalTime) {
        ArrivalTime = arrivalTime;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public long getTime() {
        return Utils.getTime(this.getArrivalTime());
    }
}
