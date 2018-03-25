package com.example.jayesh.jumpcontatcts;

import android.location.Location;

/**
 * Created by jayesh on 3/22/2018.
 */

public class Contacts {

    String contactNo, name;
    double latitude,longitude;

    public Contacts(String contactNo, String name, double latitude, double longitude) {
        this.contactNo = contactNo;
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getContactNo() {
        return contactNo;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
