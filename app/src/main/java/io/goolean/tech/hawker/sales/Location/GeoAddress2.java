package io.goolean.tech.hawker.sales.Location;

import android.app.Activity;
import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import io.goolean.tech.hawker.sales.View.HawkerDetailList;
import io.goolean.tech.hawker.sales.View.HawkerInfo;
import io.goolean.tech.hawker.sales.View.HomeActivity;


public class GeoAddress2 {

    private Activity context;
    private Context mcontext;
    private List<Address> addresses = null;
    private HomeActivity demoActvity;
    private HawkerInfo hawkerInfo;
    private HawkerDetailList hawkerDetailList;
    String latitude,longitude;


    public GeoAddress2(Context context) {
        this.mcontext = context;
        demoActvity = new HomeActivity();
        hawkerInfo = new HawkerInfo();
        hawkerDetailList = new HawkerDetailList();
    }

    public String func_GeoAddress(double curr_lati, double curr_long) {
        StringBuilder result = new StringBuilder();
        try {
            Geocoder geocoder = new Geocoder(mcontext, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(curr_lati, curr_long, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                String state = address.getAdminArea();
                String city=address.getLocality();
                String sector = address.getSubLocality();
                String featurName= address.getFeatureName();
                String pin = address.getPostalCode();
               // String address1 = String.valueOf(address.getMaxAddressLineIndex());
                String[] addres = new String[]{address.getAddressLine(0)};
                String ass = addres[0];
                demoActvity.funLatLng(mcontext,state,city,sector,featurName,pin,curr_lati,curr_long,ass);
                hawkerInfo.funLatLng(mcontext,state,city,sector,featurName,pin,curr_lati,curr_long,ass);
                hawkerDetailList.funLatLng(mcontext,state,city,sector,featurName,pin,curr_lati,curr_long,ass);
              //  Toast.makeText(mcontext, curr_lati+","+curr_long, Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            Log.e("tag", e.getMessage());
        }
        return result.toString();
    }


}
