package io.goolean.tech.hawker.sales.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.goolean.tech.hawker.sales.Constant.ConnectionDetector;
import io.goolean.tech.hawker.sales.Constant.Urls;
import io.goolean.tech.hawker.sales.Controller.HawkerDetailAdapter;
import io.goolean.tech.hawker.sales.Location.GetLatLngActivity;
import io.goolean.tech.hawker.sales.Location.Location_Update;
import io.goolean.tech.hawker.sales.Model.HawkerDetailModel;
import io.goolean.tech.hawker.sales.Networking.VolleySingleton;
import io.goolean.tech.hawker.sales.R;
import io.goolean.tech.hawker.sales.Storage.Constants;
import io.goolean.tech.hawker.sales.Storage.LocalStoreData;
import io.goolean.tech.hawker.sales.Storage.SharedPrefrence_Login;
import io.goolean.tech.hawker.sales.Storage.SharedPrefrence_Seller;

public class HawkerDetailList extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, HawkerDetailAdapter.UpdateClick {
    private RecyclerView rvhawkerlist;
    private TextView tv_notFound;
    private HawkerDetailModel hawkerDetailModel;
    private ArrayList<HawkerDetailModel> hawkerDetailModelArrayList;
    private HawkerDetailAdapter hawkerDetailAdapter;
    private RequestQueue requestQueue;
    private ProgressDialog _progressDialog;
    private SearchView searchView;
    private ImageView ivNav;
    private Location_Update location_update;
    private LocationManager locationManager;
    static String sState, sCity, sPin;
    LocalStoreData localStoreData;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hawker_detail_list);
        getSupportActionBar().hide();

        ivNav = findViewById(R.id.ivNav);
        rvhawkerlist = findViewById(R.id.rvhawkerlist);
        tv_notFound = findViewById(R.id.tv_notFound);
        searchView = findViewById(R.id.searchView);
        SharedPrefrence_Login.getDataLogin(HawkerDetailList.this);

        hawkerDetailModelArrayList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        rvhawkerlist.setLayoutManager(linearLayoutManager);
        hawkerDetailAdapter = new HawkerDetailAdapter(getApplicationContext(), hawkerDetailModelArrayList);
        localStoreData = new LocalStoreData(HawkerDetailList.this);
        LocalStoreData.init(HawkerDetailList.this);

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        location_update = new Location_Update(HawkerDetailList.this);
        new GetLatLngActivity(HawkerDetailList.this);

        ivNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        requestPermission();
    }

    private void requestPermission() {
        Dexter.withActivity(HawkerDetailList.this)
                .withPermissions(
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                                        if (ConnectionDetector.getConnectionDetector(getApplicationContext()).isConnectingToInternet() == true) {
                                            location_update = new Location_Update(HawkerDetailList.this);
                                            new GetLatLngActivity(getApplicationContext());
                                            new Handler().postDelayed(new Runnable() {
                                                @Override
                                                public void run() {
                                                    if (!TextUtils.isEmpty(sPin)) {
                                                        funhawkerdetaillist(sPin, SharedPrefrence_Login.getPid(), SharedPrefrence_Login.getPdevice_id());
                                                    } else {
                                                        String spin = location_update.postalCode;
                                                        if (!TextUtils.isEmpty(spin)) {
                                                            funhawkerdetaillist(spin, SharedPrefrence_Login.getPid(), SharedPrefrence_Login.getPdevice_id());
                                                        } else {
                                                            funhawkerdetaillist("", SharedPrefrence_Login.getPid(), SharedPrefrence_Login.getPdevice_id());
                                                        }
                                                    }
                                                }
                                            }, 1000);

                                            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                                                @Override
                                                public boolean onQueryTextSubmit(String query) {
                                                    return false;
                                                }

                                                @Override
                                                public boolean onQueryTextChange(String query) {
                                                    if (query.equals("")) {
                                                        query = "All";
                                                        hawkerDetailAdapter.getFilter().filter(query.toLowerCase(Locale.getDefault()));
                                                    } else {
                                                        try {
                                                            hawkerDetailAdapter.getFilter().filter(query.toLowerCase(Locale.getDefault()));
                                                        } catch (Exception ee) {

                                                        }
                                                    }
                                                    return false;
                                                }
                                            });
                                        } else {
                                            Toast.makeText(HawkerDetailList.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        //  Toast.makeText(HomeActivity.this, "GPS STATUS", Toast.LENGTH_SHORT).show();
                                        turnGPSOn();
                                    }
                                }
                            }, 500);
                        }
                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                    }
                })
                .onSameThread()
                .check();
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HawkerDetailList.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.show();
    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    public void funLatLng(Context context, String state, String city, String sector, String feature, String pin,
                          double curr_lati, double curr_long, String full_address) {
        try {
            sState = state;
            sCity = city;
            sPin = pin;
        } catch (Exception ee) {

        }
    }

    private void turnGPSOn() {
        GoogleApiClient googleApiClient = null;
        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this).addApi(LocationServices.API).addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).build();
            googleApiClient.connect();
            LocationRequest locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(30 * 1000);
            locationRequest.setFastestInterval(5 * 1000);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().addLocationRequest(locationRequest);
            builder.setAlwaysShow(true); // this is the key ingredient
            PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi
                    .checkLocationSettings(googleApiClient, builder.build());
            result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
                @Override
                public void onResult(LocationSettingsResult result) {
                    final Status status = result.getStatus();
                    final LocationSettingsStates state = result
                            .getLocationSettingsStates();
                    switch (status.getStatusCode()) {
                        case LocationSettingsStatusCodes.SUCCESS:
                            //toast("Success");
                            break;
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            //toast("GPS is not on");
                            try {
                                status.startResolutionForResult(HawkerDetailList.this, 1000);
                            } catch (IntentSender.SendIntentException e) {
                                // Ignore the error.
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            toast("Setting change not allowed");
                            break;
                    }
                }
            });
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {
        toast("Suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        toast("Failed");
    }

    private void toast(String message) {
        try {
            Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        } catch (Exception ex) {
            Log.i("Hawker_Gps", "Window has been closed");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 101) {
            if (resultCode == 0) {
                finish();
            }
        } else if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                Toast.makeText(this, "GPS ON", Toast.LENGTH_SHORT).show();
                location_update = new Location_Update(HawkerDetailList.this);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        } else if (requestCode == 1111) {
            requestPermission();
        }
    }

    private void cancel(boolean b) {
    }

    private void funhawkerdetaillist(final String sPinCode, final String sSalesId, final String sDeviceID) {
        requestQueue = VolleySingleton.getInstance(HawkerDetailList.this).getRequestQueue();
        _progressDialog = ProgressDialog.show(
                HawkerDetailList.this, "", "Please wait...", true, false,
                new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        cancel(true);
                        finish();
                    }
                }
        );
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.URL_ALLOWCATED_HAWKER_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null && response.length() > 0) {
                            _progressDialog.dismiss();
                            parseData(response);
                        } else {
                            tv_notFound.setVisibility(View.VISIBLE);
                            _progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        _progressDialog.dismiss();
                        if (error.getClass().equals(TimeoutError.class)) {
                            Toast.makeText(HawkerDetailList.this, Constants.RESPONSE_FAILURE_MORE_TIME, Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(HawkerDetailList.this, Constants.RESPONSE_FAILURE_NOT_RESPOND, Toast.LENGTH_LONG).show();
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("pincode", sPinCode);
                params.put("sales_id", sSalesId);
                params.put("device_id", sDeviceID);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.REPEAT_TIME, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQue(stringRequest);

    }

    private void parseData(String response) {
        try {
            hawkerDetailModelArrayList.clear();
            JSONObject obj = new JSONObject(response);
            String strFav = obj.getString("data");
            JSONArray jsonArrayFav = new JSONArray(strFav);
            for (int i = 0; i < jsonArrayFav.length(); i++) {
                JSONObject jsonObjectFav = jsonArrayFav.getJSONObject(i);
                if (jsonObjectFav.getString("status").equals("1")) {
                    Log.i("Count", "" + jsonArrayFav.length());
                    tv_notFound.setVisibility(View.GONE);
                    hawkerDetailModel = new HawkerDetailModel();
                    hawkerDetailModel.setName(jsonObjectFav.getString("name"));
                    hawkerDetailModel.setMobile_no_contact(jsonObjectFav.getString("mobile_no_contact"));
                    hawkerDetailModel.setHawker_code(jsonObjectFav.getString("hawker_code"));
                    hawkerDetailModel.setHawker_code(jsonObjectFav.getString("hawker_code"));
                    hawkerDetailModel.setRegistered_time(jsonObjectFav.getString("registered_time"));
                    hawkerDetailModel.setHawker_register_address(jsonObjectFav.getString("hawker_register_address"));
                    hawkerDetailModel.setLatitude(jsonObjectFav.getString("latitude"));
                    hawkerDetailModel.setLongitude(jsonObjectFav.getString("longitude"));
                    hawkerDetailModel.setBusiness_name(jsonObjectFav.getString("business_name"));
                    hawkerDetailModel.setDistance(jsonObjectFav.getString("distance"));
                    hawkerDetailModel.setsAll("All");
                    hawkerDetailModelArrayList.add(hawkerDetailModel);
                } else {
                    tv_notFound.setVisibility(View.VISIBLE);
                }
            }

            if (hawkerDetailModelArrayList.size() > 0) {
                Collections.sort(hawkerDetailModelArrayList, new Comparator<HawkerDetailModel>() {
                    @Override
                    public int compare(HawkerDetailModel lhs, HawkerDetailModel rhs) {
                        return lhs.getDistance().compareTo(rhs.getDistance());
                    }
                });
                rvhawkerlist.setAdapter(hawkerDetailAdapter);
                hawkerDetailAdapter.OnUpdateClickMethod(HawkerDetailList.this);
                hawkerDetailAdapter.notifyDataSetChanged();
            } else {
                rvhawkerlist.setAdapter(hawkerDetailAdapter);
                hawkerDetailAdapter.OnUpdateClickMethod(HawkerDetailList.this);
                hawkerDetailAdapter.notifyDataSetChanged();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpdateClickListener(int position, HawkerDetailModel helper) {
        String sHawkerCode = hawkerDetailModelArrayList.get(position).getHawker_code();
        //Toast.makeText(this, position + ","+sHawkerCode, Toast.LENGTH_SHORT).show();
        localStoreData.saveHawkerCode(sHawkerCode);
        Intent intent = new Intent(HawkerDetailList.this, UpdateDistributionDetail.class);
        startActivityForResult(intent, Constants.UPDATE_REQUEST);
    }
}
