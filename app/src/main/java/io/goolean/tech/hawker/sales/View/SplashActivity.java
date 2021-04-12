package io.goolean.tech.hawker.sales.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.multidex.MultiDex;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
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
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.firebase.crashlytics.FirebaseCrashlytics;
import com.google.firebase.messaging.FirebaseMessaging;
import com.scottyab.rootbeer.RootBeer;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.goolean.tech.hawker.sales.Constant.MessageConstant;
import io.goolean.tech.hawker.sales.Constant.Singleton.CallbackSnakebarModel;
import io.goolean.tech.hawker.sales.Constant.Urls;
import io.goolean.tech.hawker.sales.FCM.Config;
import io.goolean.tech.hawker.sales.FCM.MyFirebaseInstanceIDService;
import io.goolean.tech.hawker.sales.Location.Location_Update;
import io.goolean.tech.hawker.sales.Networking.VolleySingleton;
import io.goolean.tech.hawker.sales.R;
import io.goolean.tech.hawker.sales.Storage.SharedPrefrence_Login;
import io.goolean.tech.hawker.sales.Storage.SharedPrefrence_Seller;
import io.goolean.tech.hawker.sales.Utililty.AppStatus;

import static io.goolean.tech.hawker.sales.Constant.Dialog_.dialog;

public class SplashActivity extends AppCompatActivity  implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private ImageView imageView;
    private TextView textView;
    private Animation animation1, animation2;
    private boolean checkbool;
    private static final String TAG = SplashActivity.class.getSimpleName();
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private RootBeer rootBeer;
    private RequestQueue requestQueue;
    private TextView textViewVersionInfo, tvd_message;
    private Button btnd_ok, btnd_cancel;
    private String url, strStatus, message;
    Location_Update location_update;
    private Dialog dialogCheckLocation;
    LocationRequest mLocationRequest;
    LocationManager locationManager;
    String latitude, longitude;
    public  String city = "";
    public  String postalCode = "";
    Location mLastLocation;
    FusedLocationProviderClient mFusedLocationClient;
    Button btn;


    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        FirebaseCrashlytics crashlytics = FirebaseCrashlytics.getInstance();
        crashlytics.sendUnsentReports();

        crashlytics.log("my message");

// To log a message to a crash report, use the following syntax:
        crashlytics.log("E/TAG: my message");

        MultiDex.install(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        rootBeer = new RootBeer(SplashActivity.this);
        //turnGPSOn();
        if (savedInstanceState == null) {
            funNotification();
            initValue();
        }

//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mFusedLocationClient= LocationServices.getFusedLocationProviderClient(this);
        requestForGPSUpdates();

        //funPopup();
    }


    private void checkLocationPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,  Manifest.permission.ACCESS_FINE_LOCATION)) {
                // Show an explanation to the user *asynchronously* -- don't block
                // this thread waiting for the user's response! After the user
                // sees the explanation, try again to request the permission.
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs the Location permission, please accept to use location functionality")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //Prompt the user once explanation has been shown
                                ActivityCompat.requestPermissions(SplashActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},MY_PERMISSIONS_REQUEST_LOCATION );
                            }
                        })
                        .create()
                        .show();
            } else {
                // No explanation needed, we can request the permission.
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION );
            }
        }

    }

    public static final int MY_PERMISSIONS_REQUEST_LOCATION = 99;


    private void requestForGPSUpdates() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED) {
                //Location Permission already granted
                mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            } else {
                //Request Location Permission
                checkLocationPermission();
            }
        }
        else {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
            //mMap.setMyLocationEnabled(true);
        }
    }


    LocationCallback mLocationCallback = new LocationCallback() {
        @Override
        public void onLocationResult(LocationResult locationResult) {
            checkbool = true;
            //Toast.makeText(getApplicationContext(),"hello",Toast.LENGTH_LONG).show();
            List<Location> locationList = locationResult.getLocations();
            if (locationList.size() > 0) {
                //The last location in the list is the newest
                Location location = locationList.get(locationList.size() - 1);
                //Toast.makeText(getApplicationContext(),location.getLatitude()+"",Toast.LENGTH_LONG).show();
                Log.i("MapsActivity", "Location: " + location.getLatitude() + " " + location.getLongitude());
                mLastLocation = location;

                //Do something with coordinates

                try {
                    address();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };


    public void address() throws IOException {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        addresses = geocoder.getFromLocation(mLastLocation.getLatitude(), mLastLocation.getLongitude(), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

        String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
        city = addresses.get(0).getLocality();
        if(city.equalsIgnoreCase("Gurugram"))
        {
            checkbool=false;
        }
        else
        {
            checkbool=true;
        }
        String state = addresses.get(0).getAdminArea();
        String country = addresses.get(0).getCountryName();
        postalCode = addresses.get(0).getPostalCode();
        funPopup();
        // Only if available else return NULL
    }


    private void funPopup() {

        final RequestQueue requestQueue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
//        final ProgressDialog logout_progressDialog = ProgressDialog.show(
//                SplashActivity.this, "", "Please wait...", true, false,
//                new DialogInterface.OnCancelListener() {
//                    @Override
//                    public void onCancel(DialogInterface dialog) {
//                        //SplashActivity.this.cancel(true);
//                        finish();
//                    }
//                }
//        );
        StringRequest getRequest = new StringRequest(Request.Method.POST, Urls.URL_LOCATION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("reslocc", response);

                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            String str = obj.getString("data");
                            JSONObject jsoObject = new JSONObject(str);
                            strStatus = jsoObject.getString("status");

                            message = jsoObject.getString("message");
                            if (strStatus.equals("1")) {
                                //logout_progressDialog.dismiss();


                                //jobDispatcher.cancelAll();
                                //checkValidation();

                            } else {
                                //logout_progressDialog.dismiss();

                                openNotiDialog(message);

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            //logout_progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //logout_progressDialog.dismiss();
                        //Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
//                params.put("sPin", location_update.postalCode);
//                params.put("city", location_update.city);

                params.put("sPin", "122001");


                params.put("city", "gurugram");

                //Toast.makeText(getApplicationContext(),postalCode,Toast.LENGTH_SHORT).show();


                return params;
            }
        };
        requestQueue.add(getRequest);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }



    private void openNotiDialog(final String msg) {
        try {

            dialogCheckLocation = new Dialog(SplashActivity.this);
            dialogCheckLocation.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialogCheckLocation.setContentView(R.layout.layout_unserviceable_area);
            dialogCheckLocation.setCanceledOnTouchOutside(false);
            dialogCheckLocation.setCancelable(false);
            dialogCheckLocation.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            dialogCheckLocation.show();
            TextView tv_Info = dialogCheckLocation.findViewById(R.id.tv_info_id);
            tv_Info.setText(msg);
            Button btn_OK = dialogCheckLocation.findViewById(R.id.btn_OK);
            btn_OK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    System.exit(0);
                }
            });

        } catch (WindowManager.BadTokenException e) {
            //use a log message
            e.printStackTrace();
        }
    }




    private void funNotification() {
        FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
                    displayFirebaseRegId();
                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received
                    String message = intent.getStringExtra("message");
                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();
                }
            }
        };
        // displayFirebaseRegId();
    }

    // Fetches reg id from shared preferences
    // and displays on the screen
    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        String regId = pref.getString("regId", null);
        SharedPrefrence_Login.saveToken(SplashActivity.this, regId);
        Log.e(TAG, "Firebase reg id: " + regId);
        if (!TextUtils.isEmpty(regId))
            Toast.makeText(this, "Firebase Reg Id: " + regId, Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "Firebase Reg Id is not received yet!", Toast.LENGTH_SHORT).show();
    }

    private void initValue() {
        imageView = (ImageView) findViewById(R.id.image_logo);
        textView = (TextView) findViewById(R.id.textView_id);
        animation1 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_from_top);
        animation2 = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.slide_from_left);
        dialog = new Dialog(SplashActivity.this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_logout_from_device);
        dialog.setCancelable(true);
        btnd_ok = dialog.findViewById(R.id.button_ok_id);
        btnd_cancel = dialog.findViewById(R.id.button_cancel_id);
        tvd_message = dialog.findViewById(R.id.tv_info_id);

        getVersionInfo();
    }



    private void getVersionInfo() {
        String versionName = "";
        int versionCode;
        try {
            PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
            versionName = packageInfo.versionName;
            versionCode = packageInfo.versionCode;
            if (!AppStatus.getInstance(this).isOnline()) {
                CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Please check your internet connection", MessageConstant.toast_warning);
            } else {
                funNotification();
                //func_CheckVersion(versionName,versionCode);
                checkValidation();
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        textViewVersionInfo = (TextView) findViewById(R.id.tv_versioncode);
        textViewVersionInfo.setText(String.format("V" + versionName));
    }

    private void checkValidation() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        SharedPrefrence_Login.getConfirmStatus(SplashActivity.this);
                        String Status = SharedPrefrence_Login.getHawkerStatus();
//                        if (!Status.equals("")) {
                        SharedPrefrence_Login.getDataLogin(getApplicationContext());
                        String number = SharedPrefrence_Login.getPnumber();
                        if (checkbool == true) {


                            //Toast.makeText(getApplicationContext(),"checking",Toast.LENGTH_LONG).show();

                        } else {

                            //Toast.makeText(getApplicationContext(),"not checking",Toast.LENGTH_LONG).show();
                            if (!number.equals("")) {
                                Intent ii = new Intent(getApplicationContext(), HomeActivity.class);
                                finish();
                                ii.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(ii);
                            }
                            else if (Status.equals("0")) {
                                Intent ii = new Intent(getApplicationContext(), LoginActivity.class);
                                finish();
                                ii.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(ii);
                            } else if (Status.equals("")) {
                                Intent ii = new Intent(getApplicationContext(), LoginActivity.class);
                                finish();
                                ii.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(ii);
                            }
                            else {
                                Intent ii = new Intent(getApplicationContext(), LoginActivity.class);
                                finish();
                                ii.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(ii);
                            }

                        }
                        // }
                    }
                }, 1000);
            }
        }, 100);
    }

    private void func_CheckVersion(final String versionName, final int versionCode) {
        requestQueue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.URL_HAWKER_SALES_VERSION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            String str = obj.getString("data");
                            JSONObject jsoObject = new JSONObject(str);
                            if (jsoObject.getString("status").equals("1")) {
                                String update_status = jsoObject.getString("update_status");
                                if (update_status.equals("1")) {
                                    funNotification();
                                    checkValidation();
                                } else if (update_status.equals("0")) {
                                    String message = jsoObject.getString("message");
                                    //Toast.makeText(Splash.this, "Update Your Application", Toast.LENGTH_SHORT).show();
                                    dialog.show();
                                    tvd_message.setText(message);
                                    btnd_ok.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            // plat store app link
                                            url = "https://play.google.com/store/apps/details?id=" + getPackageName();
                                            if (!url.startsWith("http://") && !url.startsWith("https://"))
                                                url = "http://" + url;
                                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                                            startActivity(browserIntent);
                                        }
                                    });
                                    btnd_cancel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog.dismiss();
                                            checkValidation();
                                        }
                                    });
                                }
                            } else if (jsoObject.getString("status").equals("0")) {
                                String update_status = jsoObject.getString("update_status");
                                if (update_status.equals("1")) {

                                } else if (update_status.equals("0")) {

                                }
                                funNotification();
                                checkValidation();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error.getClass().equals(TimeoutError.class)) {
                    CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "It took longer than expected to get the response from Server.",
                            MessageConstant.toast_warning);
                } else {
                    CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Server Respond Error! Try Again Later",
                            MessageConstant.toast_warning);
                }
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("version_name", versionName);
                params.put("version_code", String.valueOf(versionCode));

                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(this).addToRequestQue(stringRequest);
    }


    @Override
    public void onClick(View v) {

    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}

