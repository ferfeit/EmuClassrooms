package com.example.esra.myapplication;

import com.google.android.gms.maps.model.LatLng;

public class Department {

    private String name, code;
    private double longitude, latitude;

    public Department(String departmentName, String departmentCode, double latitude, double longitude) {
        this.name = departmentName;
        this.code = departmentCode;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public LatLng getLocation() {
        return new LatLng(latitude, longitude);
    }
}
