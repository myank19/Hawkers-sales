package io.goolean.tech.hawker.sales.Services;

import android.app.NotificationChannel;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import io.goolean.tech.hawker.sales.Constant.Urls;
import io.goolean.tech.hawker.sales.Location.Location_Update;
import io.goolean.tech.hawker.sales.Networking.VolleySingleton;
import io.goolean.tech.hawker.sales.Storage.SharedPrefrence_Login;


public class MyJobService extends JobService {
    BackgroundTask backgroundTask;
    public int counter = 0;
    private Timer timer;
    private TimerTask timerTask;
    int service_time = 10000;
    private SMSReceiver mSMSreceiver;
    private IntentFilter mIntentFilter;
    Location_Update location_update;
    private String android_id;
    int deviceStatus;
    int batteryLevel;
    IntentFilter intentfilter;
    private static String uniqueID = null;
    LocationManager locationManager;
    String strStatus, strDuty_status;
    NotificationChannel mChannel;
    private LocationManager mLocationManager = null;
    private static final int LOCATION_INTERVAL = 1000;
    private static final float LOCATION_DISTANCE = 10f;
    private static final String TAG = "MyLocationService";
    String LATTITUDE, LONGITUDE;
    private Runnable runnable;
    private Handler mHandler = new Handler();


    private class LocationListener implements android.location.LocationListener {
        Location mLastLocation;

        public LocationListener(String provider) {
            Log.e(TAG, "LocationListener " + provider);
            mLastLocation = new Location(provider);
        }

        @Override
        public void onLocationChanged(Location location) {
            Log.e(TAG, "onLocationChanged: " + location);
            mLastLocation.set(location);
            LATTITUDE = location.getLatitude() + "";
            LONGITUDE = location.getLongitude() + "";
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.e(TAG, "onProviderDisabled: " + provider);
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.e(TAG, "onProviderEnabled: " + provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.e(TAG, "onStatusChanged: " + provider);
        }
    }

    LocationListener[] mLocationListeners = new LocationListener[]{
            new LocationListener(LocationManager.PASSIVE_PROVIDER)
    };


    @Override
    public boolean onStartJob(@NonNull final JobParameters job) {

        mSMSreceiver = new SMSReceiver();
        mIntentFilter = new IntentFilter();
        mIntentFilter.addAction("android.intent.action.BOOT_COMPLETED");
        registerReceiver(mSMSreceiver, mIntentFilter);

        intentfilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        MyJobService.this.registerReceiver(broadcastreceiver, intentfilter);
        // registerReceiver(mSMSreceiver, mIntentFilter);
        location_update = new Location_Update(MyJobService.this);
        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        SharedPrefrence_Login.getDataLogin(getApplicationContext());
        SharedPrefrence_Login.getONOFF(getApplicationContext());
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        initializeLocationManager();
        try {
            mLocationManager.requestLocationUpdates(
                    LocationManager.PASSIVE_PROVIDER,
                    LOCATION_INTERVAL,
                    LOCATION_DISTANCE,
                    mLocationListeners[0]
            );
        } catch (java.lang.SecurityException ex) {
            Log.i(TAG, "fail to request location update, ignore", ex);
        } catch (IllegalArgumentException ex) {
            Log.d(TAG, "network provider does not exist, " + ex.getMessage());
        }

        startTimer(job);

        return true;
    }

    private void initializeLocationManager() {
        Log.e(TAG, "initializeLocationManager - LOCATION_INTERVAL: " + LOCATION_INTERVAL + " LOCATION_DISTANCE: " + LOCATION_DISTANCE);
        if (mLocationManager == null) {
            mLocationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
        }
    }

    public void startTimer(final JobParameters job) {
        ScheduledExecutorService scheduleTaskExecutor = Executors.newScheduledThreadPool(5);
        // This schedule a runnable task every 2 minutes
        scheduleTaskExecutor.scheduleAtFixedRate(new Runnable() {
            public void run() {
                Log.i("Service", "Job Start");
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    if (SharedPrefrence_Login.getPtype().equals("sales")) {
                        Log.i("DUTY STATUS", "DUTY ON");
                        if (SharedPrefrence_Login.getONOFF().equals("1")) {
                            sendSalesLocation(SharedPrefrence_Login.getPid());
                            // jobFinished(job,false);
                            sendLocationforNearBy(SharedPrefrence_Login.getPid());
                        } else if (SharedPrefrence_Login.getONOFF().equals("0")) {
                            stopTimer(timer);
                        }
                    }
                } else {
                    Log.i("DUTY STATUS", "DUTY OFF");
                    if (SharedPrefrence_Login.getPtype().equals("sales")) {
                        fun_DutyONOFFScreen("0", SharedPrefrence_Login.getPid(), Urls.URL_DUTY_ON_OFF_SALES);
                    }
                }
            }
        }, 0, 10, TimeUnit.SECONDS);
    }

    private void sendLocationforNearBy(final String salesID) {
        //  HttpsTrustManager.allowAllSSL();
        final RequestQueue requestQueue = VolleySingleton.getInstance(MyJobService.this).getRequestQueue();
        //if everything is fine
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.URL_UPDATE_NOTIFICATION_HAWKER,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null && response.length() > 0) {
                            parseData(response);
                        } else {
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.i("ERROR:", error.getMessage() + "");
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("sales_id", salesID);
                params.put("device_id", android_id);
                params.put("latitude", LATTITUDE);
                params.put("longitude", LONGITUDE);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void parseData(String response) {
        try {
            //converting response to json object
            JSONObject obj = new JSONObject(response);
            String str = obj.getString("data");
            JSONObject jsoObject = new JSONObject(str);
            if (jsoObject.getString("status").equals("1")) {
                Log.i("Response", "=========  " + (LATTITUDE + "," + LONGITUDE));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    @Override
    public boolean onStopJob(@NonNull JobParameters job) {
        // stopTimer(timer);
        if (backgroundTask != null) {
            backgroundTask.cancel(true);
        }
        Log.i("TAG", "onStopJob");
        /* true means, we're not done, please reschedule */
        return true;
    }

    private void stopTimer(Timer timer) {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }


    public static class BackgroundTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... voids) {
            return "Hello from background job";
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            // Unregister the SMS receiver
            unregisterReceiver(mSMSreceiver);
            unregisterReceiver(broadcastreceiver);
        } catch (Exception ee) {

        }
    }

    private BroadcastReceiver broadcastreceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            deviceStatus = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
            int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            batteryLevel = (int) (((float) level / (float) scale) * 100.0f);
        }
    };
    //////////////////////////////////////////////////////////////////////////////////////////////////////

    private void sendSalesLocation(final String salesID) {
        //  HttpsTrustManager.allowAllSSL();
        final RequestQueue requestQueue = VolleySingleton.getInstance(MyJobService.this).getRequestQueue();
        //if everything is fine
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.URL_SALES_CURRENT_LOCATION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            String str = obj.getString("data");
                            JSONObject jsoObject = new JSONObject(str);
                            if (jsoObject.getString("status").equals("1")) {
                                Log.i("LATITUDE", "=========  " + (LATTITUDE + "," + LONGITUDE));
                                //  Log.i("LATITUDE", "=========  "+ (appController.getLatitude() +","+appController.getLongitude()));
                                Log.i("API_START", "=========  " + (counter++));
                                Log.i("BATTERY_STATUS", "=========  " + (batteryLevel));
                                // Toast.makeText(MyJobService.this, "Sales Success" + LATTITUDE+","+LONGITUDE, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //   Toast.makeText(getApplicationContext(), "Error : "+error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("sales_id", salesID);
                params.put("device_id", android_id);
                params.put("latitude", LATTITUDE);
                params.put("longitude", LONGITUDE);
                params.put("battery_status", batteryLevel + "");
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    private void fun_DutyONOFFScreen(final String sStatus, final String Sales_ID, final String url) {
        final RequestQueue requestQueue = VolleySingleton.getInstance(MyJobService.this).getRequestQueue();
        StringRequest getRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            String str = obj.getString("data");
                            JSONObject jsoObject = new JSONObject(str);
                            strStatus = jsoObject.getString("status");
                            strDuty_status = jsoObject.getString("duty_status");
                            if (strStatus.equals("1")) {
                                if (!strDuty_status.equals("1")) {
                                    // stopTimer(timer);
                                    mHandler.removeCallbacks(runnable);
                                    SharedPrefrence_Login.saveONOFF(MyJobService.this, "0");
                                } else {
                                    //stopTimer(timer);
                                    mHandler.removeCallbacks(runnable);
                                    SharedPrefrence_Login.saveONOFF(MyJobService.this, "0");
                                }
                            } else {
                                if (!strDuty_status.equals("1")) {
                                    //stopTimer(timer);
                                    mHandler.removeCallbacks(runnable);
                                    SharedPrefrence_Login.saveONOFF(MyJobService.this, "0");
                                } else {
                                    //stopTimer(timer);
                                    mHandler.removeCallbacks(runnable);
                                    SharedPrefrence_Login.saveONOFF(MyJobService.this, "0");
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Toast.makeText(Demo_Activity.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                if (SharedPrefrence_Login.getPtype().equals("sales")) {
                    params.put("sales_id", Sales_ID);
                } else if (SharedPrefrence_Login.getPtype().equals("seller")) {
                    params.put("seller_id", Sales_ID);
                }
                params.put("longitude", LONGITUDE);
                params.put("latitude", LATTITUDE);
                params.put("duty_status", sStatus);
                return params;
            }
        };
        requestQueue.add(getRequest);
    }

}
