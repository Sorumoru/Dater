package com.example.dater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

public class Spot implements ClusterItem {
    private final String title;
    private final LatLng position;
    private final double latitude;
    private final double longitude;
    private final String snippet;
    private final String description;

    public Spot(String title, double latitude, double longitude, String snippet,
                String description) {
        position = new LatLng(latitude, longitude);
        this.latitude = latitude;
        this.longitude = longitude;
        this.title = title;
        this.snippet = snippet;
        this.description = description;
    }

    public double getLatitude() { return latitude; }

    public double getLongitude() { return longitude; }

    public String getDescription() { return description; }

    public boolean isPosEqual(double latitude, double longitude) {
        return this.latitude == latitude && this.longitude == longitude;
    }
    @NonNull
    @Override
    public LatLng getPosition() { return position; }

    @Nullable
    @Override
    public String getTitle() { return title; }

    @Nullable
    @Override
    public String getSnippet() { return snippet; }
}