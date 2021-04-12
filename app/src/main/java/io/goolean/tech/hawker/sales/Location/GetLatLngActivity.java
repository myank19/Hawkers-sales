package io.goolean.tech.hawker.sales.Location;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;

import io.goolean.tech.hawker.sales.Constant.AppController;


public class GetLatLngActivity {
    private Context context;
    private LocationManager locationManager;
    private Location location = null;
    private String provider;
    private double curr_long, curr_lati;
    private GeoAddress2 geoAddress;
    private AppController appController;


    public GetLatLngActivity(Context context) {
        this.context =context;
        locationManager = (LocationManager)context.getSystemService(Context.LOCATION_SERVICE);
        boolean enabledGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        Criteria criteria = new Criteria();
        provider = locationManager.getBestProvider(criteria, false);
        geoAddress= new GeoAddress2(context);
        //appController = (AppController)context;
        fun_LatLng();
    }

    private void fun_LatLng() {
        boolean permissionGranted = ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED  ;
        if (permissionGranted) {
            if (location != null) {
                curr_lati = location.getLatitude();
                curr_long = location.getLongitude();
                geoAddress.func_GeoAddress(curr_lati, curr_long);
            } else {
                location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                if (location != null) {
                    curr_lati = location.getLatitude();
                    curr_long = location.getLongitude();
                    geoAddress.func_GeoAddress(curr_lati,curr_long);
                }
            }
        }
        else {
            ActivityCompat.requestPermissions((Activity) context, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, 200);
        }
    }

}
