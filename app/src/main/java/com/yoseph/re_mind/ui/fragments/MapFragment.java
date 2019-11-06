package com.yoseph.re_mind.ui.fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.yoseph.re_mind.R;

public class MapFragment extends Fragment {

    private static final int REQUEST_PERMISSION_CODE = 1317;

    private FusedLocationProviderClient fusedLocationProviderClient;

    private MapView mapView;
    private GoogleMap googleMap;

    public MapFragment() {
        // Required empty public constructor.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate view to display for fragment.
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        // FusedLocationProviderClient is needed to track user location.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        // Find map view reference and instantiate it.
        mapView = rootView.findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);
        // Needed to get the map to display immediately.
        mapView.onResume();
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Get google map instance.
        mapView.getMapAsync(map -> {
            googleMap = map;

            // Check if location permission has been granted.
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) ==
                            PackageManager.PERMISSION_GRANTED) {

                // Show a 'move to my current location' button.
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);

                // Get last known location and use it as starting point for google map to display.
                fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(getActivity(), location -> {
                        if (location != null) {
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 18));
                        } else {
                            LatLng keller = new LatLng(44.974295, -93.232128);
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(keller, 18));
                        }
                    })
                    .addOnFailureListener(getActivity(), e -> {
                        LatLng keller = new LatLng(44.974295, -93.232128);
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(keller, 18));
                    });
            } else {
                // Ask for permission if permission has not yet been granted.
                requestPermissions(new String[] {
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                    }, REQUEST_PERMISSION_CODE);
            }
        });

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Log.e("Map Permission", String.valueOf(requestCode));
        if (requestCode == REQUEST_PERMISSION_CODE) {
            // If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted.

                // Show a 'move to my current location' button.
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);

                // Get last known location and use it as starting point for google map to display.
                fusedLocationProviderClient.getLastLocation()
                    .addOnSuccessListener(getActivity(), location -> {
                        if (location != null) {
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), 18));
                        } else {
                            LatLng keller = new LatLng(44.974295, -93.232128);
                            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(keller, 18));
                        }
                    })
                    .addOnFailureListener(getActivity(), e -> {
                        LatLng keller = new LatLng(44.974295, -93.232128);
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(keller, 18));
                    });
            } else {
                // Permission denied.
                Toast.makeText(getContext(), "Permission denied to access current location.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
