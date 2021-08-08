package com.shashank.quakewatch.ActivitiesAndFragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.shashank.quakewatch.DialogsAndAnimation.settingsDialog;
import com.shashank.quakewatch.R;
import com.shashank.quakewatch.customAdapterAndLoader.Constant;

import java.util.ArrayList;
import java.util.Objects;
import java.util.concurrent.TimeUnit;


public class AllEarthquakes extends Fragment {

    private ArrayList<LatLng> latLngArrayList = new ArrayList<>();
    private String mMapTheme;
    private SharedPreferences preferences;
    private GoogleMap map;
    public AllEarthquakes() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_all_earthquakes, container, false);
        preferences = PreferenceManager.getDefaultSharedPreferences(requireContext());
        LoadTheme();
//        Toast.makeText(requireContext(), mMapTheme+"1", Toast.LENGTH_SHORT).show();
        SupportMapFragment supportMapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.google_map_1);
        assert supportMapFragment != null;
        supportMapFragment.onLowMemory();

        supportMapFragment.getMapAsync(new OnMapReadyCallback() {

            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                map = googleMap;
                if (Constant.latLngArrayList.isEmpty()) {
                    try {
                        TimeUnit.SECONDS.sleep(2);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                } else {
                    for (int i = 0; i < Constant.earthquakes.size(); i++) {
                        MarkerOptions markerOptions = new MarkerOptions().position(Constant.latLngArrayList.get(i)).title(Constant.earthquakes.get(i).getTitle()).icon(BitmapDescriptorFactory.fromResource(R.drawable.earthquake_4));
                        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(Constant.latLngArrayList.get(Constant.latLngArrayList.size()-1), 4));
                        googleMap.addMarker(markerOptions);
                    }
                }
                try {
                    switch (mMapTheme) {
                        case "Simple":
                            googleMap.setMapStyle(
                                    MapStyleOptions.loadRawResourceStyle(
                                            requireContext(), R.raw.default_style));
                            break;
                        case "Dark":
                            googleMap.setMapStyle(
                                    MapStyleOptions.loadRawResourceStyle(
                                            requireContext(), R.raw.style_dark_json));
                            break;
                        case "Night":
                            googleMap.setMapStyle(
                                    MapStyleOptions.loadRawResourceStyle(
                                            requireContext(), R.raw.style_night_json));
                            break;
                        case "Retro":
                            googleMap.setMapStyle(
                                    MapStyleOptions.loadRawResourceStyle(
                                            requireContext(), R.raw.style_retro_json));
                            break;
                        case "Silver":
                            googleMap.setMapStyle(
                                    MapStyleOptions.loadRawResourceStyle(
                                            requireContext(), R.raw.style_silver_json));
                            break;
                        case "Satellite":
                            googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                            break;
                        case "Aubergine":
                            googleMap.setMapStyle(
                                    MapStyleOptions.loadRawResourceStyle(
                                            requireContext(), R.raw.style_aubergine_json));
                            break;
                        default:
                            //

                    }
                }catch(Exception e){
                    Toast.makeText(getContext(),"Error occurred",Toast.LENGTH_LONG).show();
                    googleMap.setMapStyle(
                            MapStyleOptions.loadRawResourceStyle(
                                    requireContext(), R.raw.default_style));
                }
                UiSettings uiSettings = googleMap.getUiSettings();
                uiSettings.setZoomControlsEnabled(true);
                uiSettings.setZoomGesturesEnabled(true);
                uiSettings.setMapToolbarEnabled(false);
            }

        });

        Handler handler = new Handler(Looper.getMainLooper());
        requireActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (Constant.earthquakes.size() == Integer.parseInt(preferences.getString("want","5")) && map != null){
                    for (int i = 0; i < Constant.earthquakes.size(); i++) {
                        MarkerOptions markerOptions = new MarkerOptions().position(Constant.latLngArrayList.get(i))
                                .title(Constant.earthquakes.get(i).getTitle())
                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.earthquake_4));
                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(Constant.latLngArrayList.get(Constant.latLngArrayList.size()-1), 4));
                        map.addMarker(markerOptions);
                    }
                } else {
                    handler.postDelayed(this,2000);
//                    Toast.makeText(requireContext(), Constant.earthquakes.size()+" "+preferences.getString("want","5") , Toast.LENGTH_SHORT).show();
                }
            }
        });
        return rootView;
    }



    public void LoadTheme() {
        mMapTheme = preferences.getString("mapTheme", "System default");
    }

}
