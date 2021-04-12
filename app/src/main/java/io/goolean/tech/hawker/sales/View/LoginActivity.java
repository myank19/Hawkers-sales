package io.goolean.tech.hawker.sales.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.multidex.MultiDex;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
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
import com.google.firebase.messaging.FirebaseMessaging;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.goolean.tech.hawker.sales.Constant.AppController;
import io.goolean.tech.hawker.sales.Constant.CommonDialog;
import io.goolean.tech.hawker.sales.Constant.ConnectionDetector;
import io.goolean.tech.hawker.sales.Constant.MessageConstant;
import io.goolean.tech.hawker.sales.Constant.Singleton.CallbackSnakebarModel;
import io.goolean.tech.hawker.sales.Constant.Urls;
import io.goolean.tech.hawker.sales.FCM.Config;
import io.goolean.tech.hawker.sales.Lib.FancyToast.FancyToast;
import io.goolean.tech.hawker.sales.Location.GetLatLngActivity;
import io.goolean.tech.hawker.sales.Location.Location_Update;
import io.goolean.tech.hawker.sales.Networking.VolleySingleton;
import io.goolean.tech.hawker.sales.R;
import io.goolean.tech.hawker.sales.Storage.SharedPrefrence_Login;
import io.goolean.tech.hawker.sales.Utililty.AppStatus;


public class LoginActivity extends AppCompatActivity implements View.OnClickListener, GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private TextView tv_explore;
    private RequestQueue requestQueue;
    private EditText edtNumber;
    private Button btnLogin;
    private String device_id;
    private SharedPrefrence_Login sharedPrefrence_login;
    private static final String TAG = LoginActivity.class.getSimpleName();
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private String regId="", strCheckStatusConfirm = "1";
    private Dialog dialogUnderprocessing, dialogOther;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        MultiDex.install(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        if (savedInstanceState == null) {
            funNotification();
            initValue();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (!hasFocus) {
        } else {
            if (AppStatus.getInstance(this).isEnabled()) {
                turnGPSOn();
            } else if (AppStatus.getInstance(this).isOnline()) {
                CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "You are online", MessageConstant.toast_success);
            } else {
                CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Please check your internet connection", MessageConstant.toast_warning);
            }
        }
        super.onWindowFocusChanged(hasFocus);

    }

    private void funNotification() {
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);
                    displayFirebaseRegId();
                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    String message = intent.getStringExtra("message");
                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();
                }
            }
        };
        displayFirebaseRegId();
    }

    private void displayFirebaseRegId() {
        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
        regId = pref.getString("regId", "");
        Log.e(TAG, "Firebase reg id: " + regId);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void initValue() {
        device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(),
                Settings.Secure.ANDROID_ID);
        tv_explore = findViewById(R.id.tv_explore_id);
        tv_explore.setOnClickListener(this);
        edtNumber = findViewById(R.id.edit_text_number);
        btnLogin = findViewById(R.id.btn_login_id);
        btnLogin.setOnClickListener(this);
        sharedPrefrence_login = new SharedPrefrence_Login(getApplicationContext());
        SharedPrefrence_Login.getTokenS(getApplicationContext());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_explore_id:
                break;
            case R.id.btn_login_id:
                Dexter.withActivity(this)
                        .withPermission(Manifest.permission.RECEIVE_SMS)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {

                                String mobile_no = edtNumber.getText().toString().trim();
                                if (mobile_no.equals("")) {
                                    FancyToast.makeText(getApplicationContext(), "Enter mobile number", FancyToast.LENGTH_LONG, FancyToast.WARNING, true).show();
                                } else if (mobile_no.length() < 10) {
                                    FancyToast.makeText(getApplicationContext(), "Enter 10 digit mobile number", FancyToast.LENGTH_LONG, FancyToast.WARNING, true).show();
                                } else if (!SharedPrefrence_Login.getTokenS().equals("")) {
                                    if (ConnectionDetector.getConnectionDetector(getApplicationContext()).isConnectingToInternet() == true) {
                                        fun_Login(mobile_no, SharedPrefrence_Login.getTokenS());
                                    } else {
                                        FancyToast.makeText(getApplicationContext(), "Check your internet connection", FancyToast.LENGTH_LONG, FancyToast.WARNING, true).show();
                                    }
                                } else {
                                    if (ConnectionDetector.getConnectionDetector(getApplicationContext()).isConnectingToInternet() == true) {
                                        fun_Login(mobile_no, regId);
                                    } else {
                                        FancyToast.makeText(getApplicationContext(), "Check your internet connection", FancyToast.LENGTH_LONG, FancyToast.WARNING, true).show();
                                    }
                                }
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {/* ... */}

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {/* ... */}
                        }).check();

        }
    }

    private void cancel(boolean b) {
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void fun_Login(final String mobile_no, final String token) {

        final ProgressDialog _progressDialog = ProgressDialog.show(
                LoginActivity.this, "", "Please wait...", true, false,
                new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        LoginActivity.this.cancel(true);
                        finish();
                    }
                }
        );
        requestQueue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.URL_LOGIN,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("ressalelogin",response);
                            JSONObject obj = new JSONObject(response);
                            String str = obj.getString("data");
                            JSONObject jsoObject = new JSONObject(str);
                            if (jsoObject.getString("status").equals("0")) {
                                _progressDialog.dismiss();
                                CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Internal Error ! Try Again Later", MessageConstant.toast_warning);
                            } else if (jsoObject.getString("status").equals("1")) {
                                String str_type = jsoObject.getString("type");
                                if (str_type.equalsIgnoreCase("sales")) {
                                    //=====active_status = 0 (reject), 1(active), 2(not active)
                                    if (jsoObject.getString("active_status").equals("0")) {
                                        _progressDialog.dismiss();
                                        CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Your Application has been rejected by system.", MessageConstant.toast_warning);
                                    } else if (jsoObject.getString("active_status").equals("1")) {
                                        _progressDialog.dismiss();
                                        String id = jsoObject.getString("id");
                                        String name = jsoObject.getString("name");
                                        SharedPrefrence_Login.saveDataLogin(getApplicationContext(), edtNumber.getText().toString().trim(), device_id, token, name, id, str_type);
                                        Intent ii = new Intent(getApplicationContext(), VerificationActivity.class);
                                        finish();
                                        ii.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                        startActivity(ii);
                                    } else if (jsoObject.getString("active_status").equals("2")) {
                                        _progressDialog.dismiss();
                                        openDialog();
                                    }
                                }
                                _progressDialog.dismiss();
                            } else if (jsoObject.getString("status").equals("2")) {
                                _progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(),"hii",Toast.LENGTH_LONG).show();
                                CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "This number not registered.",
                                        MessageConstant.toast_warning);

                            } else if (jsoObject.getString("status").equals("3")) {
                                Toast.makeText(getApplicationContext(),"hii",Toast.LENGTH_LONG).show();
                                _progressDialog.dismiss();
                                dialogStatus3(mobile_no);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        _progressDialog.dismiss();
                        //Log.d("errorlogin",error.getMessage());
                        if (error.getClass().equals(TimeoutError.class)) {
                            //Toast.makeText(getApplicationContext(),"heya",Toast.LENGTH_LONG).show();
                            CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "It took longer than expected to get the response from Server.",
                                    MessageConstant.toast_warning);
                        } else {
                            //Toast.makeText(getApplicationContext(),"heya2",Toast.LENGTH_LONG).show();
                            CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Something went wrong ! Please try again.",
                                    MessageConstant.toast_warning);
                        }

                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("mobile_no", mobile_no);
                params.put("device_id", device_id);
                params.put("notification_id", token);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(this).addToRequestQue(stringRequest);
        stringRequest.setShouldCache(false);
        AppController.getInstance().addToRequestQueue(stringRequest, Urls.URL_LOGIN);

        /*requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);
        AppController.getInstance().getRequestQueue().getCache().remove(Urls.URL_LOGIN);*/



    }

    private void dialogStatus3(final String mobile_no) {
        dialogOther = new Dialog(LoginActivity.this);
        dialogOther.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogOther.setContentView(R.layout.dialog_sure_layout);
        dialogOther.setCanceledOnTouchOutside(false);
        dialogOther.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogOther.findViewById(R.id.ok_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (ConnectionDetector.getConnectionDetector(getApplicationContext()).isConnectingToInternet() == true) {
                    if (!SharedPrefrence_Login.getTokenS().equals("")) {
                        fun_StatusCheckData(strCheckStatusConfirm, device_id, SharedPrefrence_Login.getTokenS(), mobile_no, dialogOther);
                    } else {
                        fun_StatusCheckData(strCheckStatusConfirm, device_id, regId, mobile_no, dialogOther);
                    }
                } else {
                    FancyToast.makeText(getApplicationContext(), "Check your internet connection", FancyToast.LENGTH_LONG, FancyToast.WARNING, true).show();
                }
            }
        });
        dialogOther.findViewById(R.id.cancel_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogOther.dismiss();
            }
        });
        dialogOther.show();
    }

    private void openDialog() {
        dialogUnderprocessing = new Dialog(LoginActivity.this);
        dialogUnderprocessing.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialogUnderprocessing.setContentView(R.layout.dialog_underprocessing);
        dialogUnderprocessing.setCanceledOnTouchOutside(false);
        dialogUnderprocessing.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialogUnderprocessing.findViewById(R.id.close_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogUnderprocessing.dismiss();
            }
        });
        dialogUnderprocessing.show();
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void fun_StatusCheckData(final String sStatus, final String deviceID, final String notificationID, final String mobile_no, final Dialog dutyDialog) {
        requestQueue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.URL_STATUS_CHECK_DATA,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            String str = obj.getString("data");
                            JSONObject jsoObject = new JSONObject(str);
                            if (jsoObject.getString("status").equals("1")) {
                                dutyDialog.dismiss();
                                String sales_id = jsoObject.getString("sales_id");
                                String name = jsoObject.getString("name");
                                SharedPrefrence_Login.saveDataLogin(getApplicationContext(), edtNumber.getText().toString().trim(), device_id, notificationID, name, sales_id, "sales");
                                Intent ii = new Intent(getApplicationContext(), VerificationActivity.class);
                                finish();
                                ii.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(ii);
                            } else if (jsoObject.getString("status").equals("0")) {
                                dutyDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.getClass().equals(TimeoutError.class)) {
                            CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "It took longer than expected to get the response from Server.",
                                    MessageConstant.toast_warning);
                        } else {
                            CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Something went wrong ! Please try again.",
                                    MessageConstant.toast_warning);
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("status", sStatus);
                params.put("mobile_no", mobile_no);
                params.put("device_id", deviceID);
                params.put("notification_id", notificationID);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(this).addToRequestQue(stringRequest);
    }

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


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
                                status.startResolutionForResult(LoginActivity.this, 1000);
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
            //log("Window has been closed");
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                FancyToast.makeText(getApplicationContext(), "GPS ON", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }
}
