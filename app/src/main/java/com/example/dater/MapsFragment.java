package com.example.dater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.maps.android.clustering.ClusterManager;

import java.util.ArrayList;
import java.util.Objects;

public class MapsFragment extends Fragment {

    FirebaseFirestore db;
    private ClusterManager<Spot> clusterManager;
    ArrayList<Spot> spots = new ArrayList<>();

    private final OnMapReadyCallback callback = new OnMapReadyCallback() {

        /**
         * Manipulates the map once available.
         * This callback is triggered when the map is ready to be used.
         * This is where we can add markers or lines, add listeners or move the camera.
         * In this case, we just add a marker near Sydney, Australia.
         * If Google Play services is not installed on the device, the user will be prompted to
         * install it inside the SupportMapFragment. This method will only be triggered once the
         * user has installed Google Play services and returned to the app.
         */
        @Override
        public void onMapReady(@NonNull GoogleMap googleMap) {
            db = FirebaseFirestore.getInstance();
            setUpClusterManager(googleMap);

            LatLng sydney = new LatLng(-34, 151);
            googleMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

            googleMap.setOnMapClickListener (point -> {
                // googleMap.clear(); // check if it clears map
                Marker marker = googleMap.addMarker (new MarkerOptions().position(point));

                assert marker != null;
                ( (MainActivity) requireActivity()).setLatitude(marker.getPosition().latitude);
                ( (MainActivity) requireActivity()).setLongitude(marker.getPosition().longitude);
            });

        }
    };

    @SuppressLint("PotentialBehaviorOverride")
    private void setUpClusterManager(GoogleMap googleMap) {
        clusterManager = new ClusterManager<>(requireActivity(), googleMap);

        googleMap.setOnCameraIdleListener(clusterManager);
        googleMap.setOnMarkerClickListener(clusterManager);

        addSpots();
    }

    private void addSpots() {
        db.collection("spots").
                get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document :
                                Objects.requireNonNull(task.getResult())) {
                            Log.d("Debug", document.getData().toString());
                            Spot spot = new Spot(
                                    Objects.requireNonNull(
                                            document.getData().get("title")).toString(),
                                    Double.parseDouble(Objects.requireNonNull(
                                            document.getData().get("latitude")).toString()),
                                    Double.parseDouble(Objects.requireNonNull(
                                            document.getData().get("longitude")).toString()),
                                    Objects.requireNonNull(
                                            document.getData().get("snippet")).toString(),
                                    Objects.requireNonNull(
                                            document.getData().get("description")).toString());
                            spots.add(spot);
                            clusterManager.addItem(spot);Log.d(
                                    "Debug", "New Spot added to list");
                        }
                    } else {
                        Log.w("Debug", "Error getting documents.", task.getException());
                    }
                });
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_maps, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }
}