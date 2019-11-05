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

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.yoseph.re_mind.R;

import java.util.Arrays;
import java.util.List;

public class MapFragment extends Fragment {

    private static final String TAG = MapFragment.class.getSimpleName();
    private static final int REQUEST_PERMISSION_CODE = 1317;

    private MapView mapView;
    private GoogleMap googleMap;

    public MapFragment() {
        // Required empty public constructor.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Initialize Places.
        Places.initialize(getContext(), "AIzaSyB9fkSghArkve3AFZmkxXAY4AIXzw86ywY");

        // Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(getContext());

        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        mapView = rootView.findViewById(R.id.map_view);
        mapView.onCreate(savedInstanceState);

        // Needed to get the map to display immediately.
        mapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        mapView.getMapAsync(map -> {
            googleMap = map;



            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) ==
                    PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) ==
                            PackageManager.PERMISSION_GRANTED) {
                // For showing a 'move to my current location' button.
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);

                // Use fields to define the data types to return.
                List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME, Place.Field.LAT_LNG);

                // Use the builder to create a FindCurrentPlaceRequest.
                FindCurrentPlaceRequest request =
                        FindCurrentPlaceRequest.builder(placeFields).build();
                placesClient.findCurrentPlace(request).addOnSuccessListener(((response) -> {
                    if (response.getPlaceLikelihoods().size() > 0) {
                        PlaceLikelihood placeLikelihood = response.getPlaceLikelihoods().get(0);
                        LatLng currentLocation = placeLikelihood.getPlace().getLatLng();
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 18));
                    } else {
                        LatLng keller = new LatLng(44.974295, -93.232128);
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(keller, 18));
                    }
                })).addOnFailureListener((exception) -> {
                    if (exception instanceof ApiException) {
                        ApiException apiException = (ApiException) exception;
                        Log.e(TAG, "Place not found: " + apiException.getStatusCode());
                    }

                    LatLng keller = new LatLng(44.974295, -93.232128);
                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(keller, 18));
                });
            } else {
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
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
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
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                // Permission denied.
                Toast.makeText(getContext(), "Permission denied to access current location.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
