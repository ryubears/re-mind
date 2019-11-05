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

    private PlacesClient placesClient;
    private MapView mapView;
    private GoogleMap googleMap;

    public MapFragment() {
        // Required empty public constructor.
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate view to display for fragment.
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        // Initialize Places and its client instance.
        Places.initialize(getContext(), "AIzaSyB9fkSghArkve3AFZmkxXAY4AIXzw86ywY");
        placesClient = Places.createClient(getContext());

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

                // Use fields to define the data types to return.
                List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME, Place.Field.LAT_LNG);
                // Display current location on map.
                FindCurrentPlaceRequest request =
                        FindCurrentPlaceRequest.builder(placeFields).build();
                placesClient.findCurrentPlace(request).addOnSuccessListener(((response) -> {
                    if (response.getPlaceLikelihoods().size() > 0) {
                        PlaceLikelihood placeLikelihood = response.getPlaceLikelihoods().get(0);
                        LatLng currentLocation = placeLikelihood.getPlace().getLatLng();
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 18));
                    } else {
                        // Default location to display is keller hall.
                        LatLng keller = new LatLng(44.974295, -93.232128);
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(keller, 18));
                    }
                })).addOnFailureListener((exception) -> {
                    if (exception instanceof ApiException) {
                        ApiException apiException = (ApiException) exception;
                        Log.e(TAG, "Place not found: " + apiException.getStatusCode());
                    }
                    // Default location to display is keller hall.
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

                // Show a 'move to my current location' button.
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);

                // Use fields to define the data types to return.
                List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME, Place.Field.LAT_LNG);
                // Display current location on map.
                FindCurrentPlaceRequest request =
                        FindCurrentPlaceRequest.builder(placeFields).build();
                placesClient.findCurrentPlace(request).addOnSuccessListener(((response) -> {
                    if (response.getPlaceLikelihoods().size() > 0) {
                        PlaceLikelihood placeLikelihood = response.getPlaceLikelihoods().get(0);
                        LatLng currentLocation = placeLikelihood.getPlace().getLatLng();
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 18));
                    } else {
                        // Default location to display is keller hall.
                        LatLng keller = new LatLng(44.974295, -93.232128);
                        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(keller, 18));
                    }
                })).addOnFailureListener((exception) -> {
                    if (exception instanceof ApiException) {
                        ApiException apiException = (ApiException) exception;
                        Log.e(TAG, "Place not found: " + apiException.getStatusCode());
                    }
                    // Default location to display is keller hall.
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
