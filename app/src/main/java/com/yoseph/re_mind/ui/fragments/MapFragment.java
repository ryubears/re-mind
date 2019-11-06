package com.yoseph.re_mind.ui.fragments;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
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
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.yoseph.re_mind.R;
import com.yoseph.re_mind.ui.activities.TaskDetailActivity;
import com.yoseph.re_mind.ui.activities.TaskDetailFragment;

public class MapFragment extends Fragment implements GoogleMap.OnInfoWindowClickListener {

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
            addMarkers();

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

    private void addMarkers() {
        LatLng position1 = new LatLng(44.973338, -93.236183);
        MarkerOptions marker1 = new MarkerOptions().position(position1).title("Send Mail").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon));
        marker1.snippet("Coffman Memorial Union");
        googleMap.addMarker(marker1);
        CircleOptions circle1 = new CircleOptions().center(position1).radius(100).strokeWidth(0.1f).fillColor(ContextCompat.getColor(getContext(), R.color.markerAreaColor));
        googleMap.addCircle(circle1);

        LatLng position2 = new LatLng(44.971163, -93.241845);
        MarkerOptions marker2 = new MarkerOptions().position(position2).title("Turn in Piano Worksheet").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon));
        marker2.snippet("Ferguson Hall");
        googleMap.addMarker(marker2);
        CircleOptions circle2 = new CircleOptions().center(position2).radius(100).strokeWidth(0.1f).fillColor(ContextCompat.getColor(getContext(), R.color.markerAreaColor));
        googleMap.addCircle(circle2);

        LatLng position3 = new LatLng(44.974306, -93.232181);
        MarkerOptions marker3 = new MarkerOptions().position(position3).title("Demo UI App").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon));
        marker3.snippet("Keller Hall");
        googleMap.addMarker(marker3);
        CircleOptions circle3 = new CircleOptions().center(position3).radius(100).strokeWidth(0.1f).fillColor(ContextCompat.getColor(getContext(), R.color.markerAreaColor));
        googleMap.addCircle(circle3);

        LatLng position4 = new LatLng(44.973536, -93.228941);
        MarkerOptions marker4 = new MarkerOptions().position(position4).title("Buy Milk").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon));
        marker4.snippet("Walgreens");
        googleMap.addMarker(marker4);
        CircleOptions circle4 = new CircleOptions().center(position4).radius(100).strokeWidth(0.1f).fillColor(ContextCompat.getColor(getContext(), R.color.markerAreaColor));
        googleMap.addCircle(circle4);

        LatLng position5 = new LatLng(44.973467, -93.224730);
        MarkerOptions marker5 = new MarkerOptions().position(position5).title("Throw Out Trash").icon(BitmapDescriptorFactory.fromResource(R.drawable.marker_icon));
        marker5.snippet("Home");
        googleMap.addMarker(marker5);
        CircleOptions circle5 = new CircleOptions().center(position5).radius(100).strokeWidth(0.1f).fillColor(ContextCompat.getColor(getContext(), R.color.markerAreaColor));
        googleMap.addCircle(circle5);

        googleMap.setOnInfoWindowClickListener(this);
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        Intent intent = new Intent(getContext(), TaskDetailActivity.class);
        intent.putExtra(TaskDetailFragment.ARG_ITEM_ID, "1");
        startActivity(intent);
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
