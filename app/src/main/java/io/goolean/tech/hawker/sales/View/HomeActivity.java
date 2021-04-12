package io.goolean.tech.hawker.sales.View;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;
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

import android.os.CountDownTimer;
import android.provider.Settings;
import android.text.InputFilter;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.PopupMenu;
import androidx.core.app.NotificationCompat;
import androidx.core.view.GravityCompat;
import androidx.appcompat.app.ActionBarDrawerToggle;

import android.view.MenuItem;

import com.google.android.material.navigation.NavigationView;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.wajahatkarim3.longimagecamera.LongImageCameraActivity;
import com.wajahatkarim3.longimagecamera.TouchImageView;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.multidex.MultiDex;

import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.goolean.tech.hawker.sales.Constant.CommonDialog;
import io.goolean.tech.hawker.sales.Constant.ConnectionDetector;
import io.goolean.tech.hawker.sales.Constant.InterfacModel.Validation;
import io.goolean.tech.hawker.sales.Constant.MessageConstant;
import io.goolean.tech.hawker.sales.Constant.Singleton.CallbackSnakebarModel;
import io.goolean.tech.hawker.sales.Constant.Urls;
import io.goolean.tech.hawker.sales.Lib.FancyToast.FancyToast;
import io.goolean.tech.hawker.sales.Lib.ImageCompression.imageCompression.ImageCompressionListener;
import io.goolean.tech.hawker.sales.Lib.ImageCompression.imagePicker.ImagePicker;
import io.goolean.tech.hawker.sales.Lib.RackMonth.RackMonthPicker;
import io.goolean.tech.hawker.sales.Lib.RackMonth.listener.DateMonthDialogListener;
import io.goolean.tech.hawker.sales.Lib.RackMonth.listener.OnCancelMonthDialogListener;
import io.goolean.tech.hawker.sales.Location.GetLatLngActivity;
import io.goolean.tech.hawker.sales.Location.Location_Update;
import io.goolean.tech.hawker.sales.Networking.DataPart;
import io.goolean.tech.hawker.sales.Networking.VolleyMultipartRequest;
import io.goolean.tech.hawker.sales.Networking.VolleySingleton;
import io.goolean.tech.hawker.sales.R;
import io.goolean.tech.hawker.sales.Services.MyJobService;
import io.goolean.tech.hawker.sales.Storage.SharedPrefrence_Login;
import io.goolean.tech.hawker.sales.Storage.SharedPrefrence_Seller;
import io.goolean.tech.hawker.sales.Utililty.AppStatus;


import static io.goolean.tech.hawker.sales.Constant.Urls.URL_SALES_REGISTRATION;

/*Copyright (c) 2019 Goolean Technologies Pvt. Ltd
All Rights Reserved

This product is protected by copyright and distributed under
licenses restricting copying, distribution and decompilation.*/

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,
        CompoundButton.OnCheckedChangeListener, View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    Context mcontext, mmContext;
    DrawerLayout drawer;
    private static final String TAG = "";
    private RadioButton rbMoving, rbFix, rbHybrid, rbOwnar, rbOther, rbPartner, rbEmployee, rbApplication,
            rbGPSTracker, rbTemporary, rbSeasonal, rbSmartNO, rbSubMoving, rbSubFix, rbMale, rbFemale;
    private LinearLayout llPersonal_info, llShop_Info, llMoreImage, llDateSelction;
    private RelativeLayout rlSelectService;
    private EditText edt_name_id, edt_residential_address_id, edt_bussiness_name, etHawkerTypeID, edt_gst_number,
            edt_other_sub_hawker_type, edt_shop_name_id, edt_shop_gps_id;
    private RelativeLayout rlProImage;
    private ImagePicker imagePicker;
    private CircularImageView iv_profile_id;
    private String str_profileImage = "", strIndentityProofImage = "", strIndentityProofImage2 = "", strAddressProofImage = "", strShopImageOne = "", strShopImageTwo = "",
            strDoubleImage = "", strShopImage = "", strUserType = "Moving", strSelectedService, strGPSMODE = "App", strRegisterAddress, android_id,
            strGPSID, strSubSelectedService, strSuperSubSelectedService, strSubHawkerName, strSubUserType = "", strMenuImage = "",
            strMenuImage1 = "", strMenuImage2 = "", strMenuImage3 = "";
    private ImageView ivAddressProofImage, ivIdentityProofImage, ivIdentityProofImage2, ivShopImageOne, ivShopImageTwo, ivMenu, ivNav;
    private RequestQueue requestQueue;
    private Button btnSend;
    public static TextView tvPinCode;
    public static EditText edt_shopMobileNumber_id, edt_mobile_number_contact_id;
    static EditText edt_shop_state_id, edt_city_id, edt_shop_address_id;
    static String sState, sCity, sPin;
    private TextView tv_auto_detect_id;
    private Validation validation;
    private double latitude, longitude;
    private String strHowkerName, strHowkerResidentialAddress, strHowkerMobileNumber, strHowkerBussinessName, strHowkerShopName,
            strHowkerShopAdress, strHowkerShopeMobileNumber, strHowkerStateCity, strHowkerGSTNUMBER, strHowkerTypeOtherDetail, strHowkerContactNumber;
    private Switch swActiveInactive, swActiveInactiveScreen;
    boolean bSwitch = true, aBoolean = true;
    private FirebaseJobDispatcher jobDispatcher;
    private static final String Job_Tag = "my_job_tag";
    Location_Update location_update;
    LocationManager locationManager;
    TextView tvName;
    static TextView tvRegisteredAddress;
    private String strStatus, strDuty_status, strFromTime = "", strToTime = "", strDays, logout_status = "", strNameType = "Owner", strMobileType = "Permanent", strString1 = "",
            strSmartPhone = "YES", strGender = "Male", strDutyStatus;
    Multimap<String, Object> myMultimap;
    private Button btnStartDate, btnEndDate;
    Spinner spHawkerSubType;
    private String[] sHawkerName;

    /*Date Work */
    Date currentDate, selectedDateFrom;
    SimpleDateFormat dateFormat, dateFormat_two;
    String finalStringDate, strStartDate, strEndDate;
    private DatePickerDialog dialogfrom = null;
    private DatePickerDialog dialogEnd = null;
    Calendar calendar;
    int currentmonth;
    String currentDateString, iSubHawkerNamePosition;
    private String strHawker_type_Code = "11", strClearData = "", strOther_Sub_HawkerType = "", strPhoneType = "Android", latlonProfile, latlonShop1,
            latlonShop2, latlonShop3,
            PRO_LAT = "", PRO_LON = "", SHOP1_LAT = "", SHOP1_LON = "", SHOP2_LAT = "", SHOP2_LON = "", IDENTITY_LAT = "", IDENTITY_LON = "",
            ADDRESS_LAT = "", ADDRESS_LON = "", MENU_LAT = "", MENU_LON = "";
    ProgressDialog _progressDialog;
    private PopupMenu popupMenu;
    private RadioButton rbPhoneAndroid, rbPhoneJio, rbPhoneOther;
    //NotificationChannel mChannel;
    private LinearLayout llFixLayout, llBottomLayout;
    private RadioButton rbSeasonalYes, rbSeasonalNo;
    private String strFixSeasonalyesno = "", strOtherBusinessDetails;
    private EditText edt_other_business_detail_id;
    RegistrationConfirmationActivity registrationConfirmationActivity = new RegistrationConfirmationActivity();
    private RackMonthPicker rackMonthPicker1, rackMonthPicker2;
    private int iyear, day;
    private ImageView ivMenuImage, ivMenuImage1, ivMenuImage2, ivMenuImage3;
    private RadioButton rbMobilePhone, rbLandlinePhone;
    private String PHONE_TYPE = "PH";
    private String sPinCode;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        MultiDex.install(this);
        if (savedInstanceState == null) {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            location_update = new Location_Update(HomeActivity.this);
            jobDispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(HomeActivity.this));
            Toolbar toolbar = findViewById(R.id.toolbar);
            setSupportActionBar(toolbar);
            drawer = findViewById(R.id.drawer_layout);
            NavigationView navigationView = findViewById(R.id.nav_view);
            View headerView = navigationView.getHeaderView(0);
            tvName = headerView.findViewById(R.id.tvName);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            drawer.addDrawerListener(toggle);
            toggle.syncState();
            navigationView.setNavigationItemSelectedListener(this);
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (!hasFocus) {
        } else {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                if (AppStatus.getInstance(this).isEnabled()) {
                    turnGPSOn();
                } else if (AppStatus.getInstance(this).isOnline()) {
                    CommonDialog.Show_Internt_Dialog(HomeActivity.this, "1");
                    Runtime_Permission();
                    CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "You are online", MessageConstant.toast_success);
                } else {
                    CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Please check your internet connection", MessageConstant.toast_warning);
                }
            } else {
                turnGPSOn();
            }
        }
        super.onWindowFocusChanged(hasFocus);
    }

    @Override
    public void onResume() {
        super.onResume();
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (strClearData.equals("")) {
            Runtime_Permission();
        }
    }

    /*Manifest.permission.WRITE_CALL_LOG,*/
    private void Runtime_Permission() {
        Dexter.withActivity(HomeActivity.this)
                .withPermissions(Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.CHANGE_NETWORK_STATE,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.READ_CALL_LOG,
                        Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            if (ConnectionDetector.getConnectionDetector(getApplicationContext()).isConnectingToInternet() == true) {
                                String device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
                                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {

                                    /* Intialization of ID's*/
                                    initID();

                                    /*Open GPS Dialog and get Lat/Lon*/
                                    new GetLatLngActivity(getApplicationContext());

                                    if (sCity == null) {
                                        /* sCity = location_update.city;*/
                                        String spin = tvPinCode.getText().toString();
                                        if (TextUtils.isEmpty(spin)) {
                                            spin = spin;
                                        } else {
                                            //Toast.makeText(HomeActivity.this, "Please check your gps", Toast.LENGTH_SHORT).show();
                                        }
                                        /* API for Check Active/ inactive Status*/
                                        fun_Active_Inactive(SharedPrefrence_Login.getPid(), device_id, spin);
                                        /* Calling Duty Toggle / Switch Button*/
                                        funDutyOnOFF();
                                        /* Restore data from Shared Preferance*/
                                        fucn_ScreenDataRestore();

                                    } else if (!sCity.equals("")) {
                                        String spin = tvPinCode.getText().toString();
                                        if (TextUtils.isEmpty(spin)) {
                                            spin = spin;
                                        } else {
                                            //Toast.makeText(HomeActivity.this, "Please check your gps", Toast.LENGTH_SHORT).show();
                                        }

                                        /* API for Check Active/ inactive Status*/
                                        fun_Active_Inactive(SharedPrefrence_Login.getPid(), device_id, spin);
                                        /* Calling Duty Toggle / Switch Button*/
                                        funDutyOnOFF();
                                        /* Restore data from Shared Preferance*/
                                        fucn_ScreenDataRestore();

                                    } else {
                                        new GetLatLngActivity(getApplicationContext());
                                    }
                                } else {
                                    //  Toast.makeText(HomeActivity.this, "GPS STATUS", Toast.LENGTH_SHORT).show();
                                    turnGPSOn();
                                }
                            } else {
                                // FancyToast.makeText(getApplicationContext(),"Check your internet connection",FancyToast.LENGTH_LONG,FancyToast.WARNING,true).show();
                                CommonDialog.Show_Internt_Dialog(HomeActivity.this, "");
                                //Dialog_.getInstanceDialog(HomeActivity.this).dialog_Internet();
                            }
                        }
                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                })
                .onSameThread()
                .check();
    }

    public void funLatLng(Context context, String state, String city, String sector, String feature, String pin, double curr_lati, double curr_long, String full_address) {
        try {
            sState = state;
            sCity = city;
            sPin = pin;

            if (pin != null) {
                tvPinCode.setText(sPin);
            }

            if (state != null) {
                edt_shop_state_id.setText(state);
            }
            if (city != null) {
                edt_city_id.setText(city);
            }
            if (full_address != null) {
                tvRegisteredAddress.setText(full_address);
            }
            latitude = curr_lati;
            longitude = curr_long;
            if (!SharedPrefrence_Seller.getHowkerShopAdress().equals("")) {
                edt_shop_address_id.setText(SharedPrefrence_Seller.getHowkerShopAdress());
            } else {
                if (!TextUtils.isEmpty(full_address)) {
                    edt_shop_address_id.setText(full_address);
                }
            }
        } catch (Exception ee) {

        }
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
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

    private void initID() {
        /*Intialize ID*/
        ivNav = findViewById(R.id.ivNav);
        tvPinCode = findViewById(R.id.tvPinCode);
        rbMoving = findViewById(R.id.rbMoving);
        rbFix = findViewById(R.id.rbFix);
        rbHybrid = findViewById(R.id.rbHybrid);
        rbTemporary = findViewById(R.id.rbTemporary);
        rbSeasonal = findViewById(R.id.rbSeasonal);
        rbSmartNO = findViewById(R.id.rbSmartNO);
        rbSubMoving = findViewById(R.id.rbSubMoving);
        rbSubFix = findViewById(R.id.rbSubFix);
        rbMale = findViewById(R.id.rbMale);
        rbFemale = findViewById(R.id.rbFemale);
        llDateSelction = findViewById(R.id.llDateSelction);
        rbOwnar = findViewById(R.id.rbOwnar);
        rbPartner = findViewById(R.id.rbPartner);
        rbEmployee = findViewById(R.id.rbEmployee);
        rbOther = findViewById(R.id.rbOther);
        llPersonal_info = findViewById(R.id.llPersonal_info);
        llShop_Info = findViewById(R.id.llShop_Info);
        llMoreImage = findViewById(R.id.llMoreImage);
        rlSelectService = findViewById(R.id.rlSelectService);
        btnSend = findViewById(R.id.btnSend);
        rbApplication = findViewById(R.id.rbApplication);
        rbGPSTracker = findViewById(R.id.rbGPSTracker);
        edt_name_id = findViewById(R.id.edt_name_id);
        edt_residential_address_id = findViewById(R.id.edt_residential_address_id);
        edt_bussiness_name = findViewById(R.id.edt_bussiness_name);
        edt_shop_gps_id = findViewById(R.id.edt_shop_gps_id);
        rlProImage = findViewById(R.id.rlProImage);
        iv_profile_id = findViewById(R.id.iv_profile_id);
        edt_shop_name_id = findViewById(R.id.edt_shop_name_id);
        edt_shop_state_id = findViewById(R.id.edt_shop_state_id);
        edt_city_id = findViewById(R.id.edt_city_id);
        edt_shop_address_id = findViewById(R.id.edt_shop_address_id);
        edt_shopMobileNumber_id = findViewById(R.id.edt_shopMobileNumber_id);
        tv_auto_detect_id = findViewById(R.id.tv_auto_detect_id);
        ivAddressProofImage = findViewById(R.id.ivAddressProofImage);
        ivIdentityProofImage = findViewById(R.id.ivIdentityProofImage);
        ivIdentityProofImage2 = findViewById(R.id.ivIdentityProofImage2);
        ivShopImageOne = findViewById(R.id.ivShopImageOne);
        ivShopImageTwo = findViewById(R.id.ivShopImageTwo);
        tvRegisteredAddress = findViewById(R.id.tvRegisteredAddress);
        etHawkerTypeID = findViewById(R.id.etHawkerTypeID);
        edt_mobile_number_contact_id = findViewById(R.id.edt_mobile_number_contact_id);
        edt_gst_number = findViewById(R.id.edt_gst_number);
        swActiveInactiveScreen = findViewById(R.id.swActiveInactiveScreen);
        ivMenu = findViewById(R.id.ivMenu);
        edt_other_sub_hawker_type = findViewById(R.id.edt_other_sub_hawker_type);
        btnStartDate = findViewById(R.id.btnStartDate);
        btnEndDate = findViewById(R.id.btnEndDate);
        this.spHawkerSubType = findViewById(R.id.spHawkerSubType);
        rbPhoneAndroid = findViewById(R.id.rbPhoneAndroid);
        rbPhoneJio = findViewById(R.id.rbPhoneJio);
        rbPhoneOther = findViewById(R.id.rbPhoneOther);
        llFixLayout = findViewById(R.id.llFixLayout);
        llBottomLayout = findViewById(R.id.llBottomLayout);
        rbSeasonalYes = findViewById(R.id.rbSeasonalYes);
        rbSeasonalNo = findViewById(R.id.rbSeasonalNo);
        edt_other_business_detail_id = findViewById(R.id.edt_other_business_detail_id);
        ivMenuImage = findViewById(R.id.ivMenuImage);
        ivMenuImage1 = findViewById(R.id.ivMenuImage1);
        ivMenuImage2 = findViewById(R.id.ivMenuImage2);
        ivMenuImage3 = findViewById(R.id.ivMenuImage3);


        /* Click Listener*/
        ivNav.setOnClickListener(this);
        rbMoving.setOnCheckedChangeListener(this);
        rbFix.setOnCheckedChangeListener(this);
        rbHybrid.setOnCheckedChangeListener(this);
        rbTemporary.setOnCheckedChangeListener(this);
        rbSeasonal.setOnCheckedChangeListener(this);
        rbSmartNO.setOnCheckedChangeListener(this);
        rbSubMoving.setOnCheckedChangeListener(this);
        rbSubFix.setOnCheckedChangeListener(this);
        rbMale.setOnCheckedChangeListener(this);
        rbFemale.setOnCheckedChangeListener(this);
        rbOwnar.setOnCheckedChangeListener(this);
        rbPartner.setOnCheckedChangeListener(this);
        rbEmployee.setOnCheckedChangeListener(this);
        rbOther.setOnCheckedChangeListener(this);
        rbApplication.setOnCheckedChangeListener(this);
        rbGPSTracker.setOnCheckedChangeListener(this);
        rlSelectService.setOnClickListener(this);
        btnSend.setOnClickListener(this);
        rlProImage.setOnClickListener(this);
        ivAddressProofImage.setOnClickListener(this);
        ivIdentityProofImage.setOnClickListener(this);
        ivIdentityProofImage2.setOnClickListener(this);
        ivShopImageOne.setOnClickListener(this);
        ivShopImageTwo.setOnClickListener(this);
        tv_auto_detect_id.setOnClickListener(this);
        ivMenu.setOnClickListener(this);
        btnStartDate.setOnClickListener(this);
        btnEndDate.setOnClickListener(this);
        rbPhoneAndroid.setOnCheckedChangeListener(this);
        rbPhoneJio.setOnCheckedChangeListener(this);
        rbPhoneOther.setOnCheckedChangeListener(this);
        rbSeasonalYes.setOnCheckedChangeListener(this);
        rbSeasonalNo.setOnCheckedChangeListener(this);
        ivMenuImage.setOnClickListener(this);
        ivMenuImage1.setOnClickListener(this);
        ivMenuImage2.setOnClickListener(this);
        ivMenuImage3.setOnClickListener(this);

        rbMobilePhone = findViewById(R.id.rbMobilePhone);
        rbLandlinePhone = findViewById(R.id.rbLandlinePhone);
        rbMobilePhone.setOnCheckedChangeListener(this);
        rbLandlinePhone.setOnCheckedChangeListener(this);

        PHONE_TYPE = "PH";
        edt_mobile_number_contact_id.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});

        /*Date intialize*/
        funDateIntialize();
        /*Other Class Intialization*/
        validation = new Validation();
        android_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        SharedPrefrence_Seller.getSharedPrefrencesFixerShopCategoryId(HomeActivity.this);
        SharedPrefrence_Seller.getSharedPrefrencesFixerShopSubCategoryId(HomeActivity.this);
        SharedPrefrence_Seller.getSharedPrefrencesFixerShopSuperSubCategoryId(HomeActivity.this);
        SharedPrefrence_Login.getDataLogin(getApplicationContext());
        SharedPrefrence_Seller.getHowkerRegistration(HomeActivity.this);
        SharedPrefrence_Seller.getIncommingNumber(HomeActivity.this);
        SharedPrefrence_Login.getONOFF(getApplicationContext());
        SharedPrefrence_Seller.getIMAGELATITUDELOGITUDE(HomeActivity.this);
        SharedPrefrence_Seller.getHawkerOtherServiceData(HomeActivity.this);

        if (!SharedPrefrence_Seller.getHAWKERINCOMMINGNUMBER().equals("") && SharedPrefrence_Seller.getHAWKERINCOMMINGNUMBER() != null) {
            edt_shopMobileNumber_id.setText(SharedPrefrence_Seller.getHAWKERINCOMMINGNUMBER());
            edt_mobile_number_contact_id.setText(SharedPrefrence_Seller.getHAWKERINCOMMINGNUMBER());
        }
        SharedPrefrence_Seller.saveHawkerNumberMatch(HomeActivity.this, "Home");

        prefs = getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE);
        strSelectedService = prefs.getString("catitem", "");
        strSubSelectedService = prefs.getString("subitem","");
//        Toast.makeText(getApplicationContext(),strSelectedService,Toast.LENGTH_LONG).show();
//        Toast.makeText(getApplicationContext(),strSubSelectedService,Toast.LENGTH_LONG).show();
        Log.d("strSelectedServicehome",strSelectedService);
        Log.d("strSubSelecServicehome",strSubSelectedService);
    }

    private void funDutyOnOFF() {
        swActiveInactiveScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    if (ConnectionDetector.getConnectionDetector(getApplicationContext()).isConnectingToInternet() == true) {
                        if (bSwitch == true) {
                            if (!location_update.LATTITUDE.equals("0.0")) {
                                Log.i("Duty Status", "ON");
                                strDutyStatus = "1";
                                //Toast.makeText(getApplicationContext(), "hii"+location_update.LATTITUDE, Toast.LENGTH_LONG).show();
                                fun_DutyONOFFScreen(strDutyStatus, SharedPrefrence_Login.getPid());
                            } else {
                                //Toast.makeText(getApplicationContext(), "hello"+location_update.LATTITUDE, Toast.LENGTH_LONG).show();
                                if (bSwitch == true) {
                                    swActiveInactiveScreen.setChecked(false);
                                } else {
                                    swActiveInactiveScreen.setChecked(true);
                                }
                                CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Yet! Your GPS not work properly.", MessageConstant.toast_warning);
                            }
                        } else {
                            Log.i("Duty Status", "OFF");
                            strDutyStatus = "0";
                            fun_DutyONOFFScreen(strDutyStatus, SharedPrefrence_Login.getPid());
                        }
                    } else {
                        if (bSwitch == true) {
                            swActiveInactiveScreen.setChecked(false);
                        } else {
                            swActiveInactiveScreen.setChecked(true);
                        }
                        CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Check your internet connection", MessageConstant.toast_warning);
                    }
                } else {
                    if (bSwitch == true) {
                        swActiveInactiveScreen.setChecked(false);
                    } else {
                        swActiveInactiveScreen.setChecked(true);
                    }
                    turnGPSOn();
                }
            }
        });
        if (ConnectionDetector.getConnectionDetector(getApplicationContext()).isConnectingToInternet() == true) {
            Log.i("Duty Status", "CHECK ON/OFF");
            strDutyStatus = "2";
            fun_DutyONOFFScreen(strDutyStatus, SharedPrefrence_Login.getPid());
        } else {
            CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Check your internet connection", MessageConstant.toast_warning);
        }

        if (ConnectionDetector.getConnectionDetector(getApplicationContext()).isConnectingToInternet() == true) {
            bindSubHawkerType(strHawker_type_Code);
        }
        spHawkerSubType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                strSubHawkerName = sHawkerName[i];
                iSubHawkerNamePosition = i + "";
                if (strSubHawkerName.equals("Others")) {
                    edt_other_sub_hawker_type.setVisibility(View.VISIBLE);
                } else {
                    edt_other_sub_hawker_type.setText("");
                    edt_other_sub_hawker_type.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_Profile) {
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        } else if (id == R.id.nav_Registration_Detail) {
            startActivity(new Intent(getApplicationContext(), CountDetailActivity.class));
        } else if (id == R.id.navDistributonsDetailList) {
            if (SharedPrefrence_Login.getONOFF().equals("1")) {
                startActivity(new Intent(getApplicationContext(), HawkerDetailList.class));
            } else {
                openDialog("");
            }
        } else if (id == R.id.nav_Logout) {
            if (ConnectionDetector.getConnectionDetector(getApplicationContext()).isConnectingToInternet() == true) {
                logout_status = "LogOut";
                fun_DutyONOFF("0", SharedPrefrence_Login.getPid(), "");
            } else {
                FancyToast.makeText(getApplicationContext(), "Check your internet connection", FancyToast.LENGTH_LONG, FancyToast.WARNING, true).show();
            }
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void openDrawer() {
        if(!(drawer==null))
        drawer.openDrawer(Gravity.LEFT);
    }

    private void closeDrawer() {
        drawer.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int id = buttonView.getId();

        if (id == R.id.rbMobilePhone) {
            if (isChecked == true) {
                PHONE_TYPE = "PH";
                edt_mobile_number_contact_id.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
                func_getEditValue();
            }
        } else if (id == R.id.rbLandlinePhone) {
            if (isChecked == true) {
                PHONE_TYPE = "LL";
                edt_mobile_number_contact_id.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13)});
                func_getEditValue();
            }
        } else if (id == R.id.rbSmartNO) {
            if (isChecked == true) {
                if (SharedPrefrence_Login.getONOFF().equals("1")) {
                    //  startActivity(new Intent(getApplicationContext(),HawkerInfo.class));
                } else {
                    openDialog("Temp");
                }
            }
        } else if (id == R.id.rbPhoneAndroid) {
            if (isChecked == true) {
                strSmartPhone = "YES";
                rbSubMoving.setChecked(true);
                rbSubMoving.setVisibility(View.VISIBLE);
                rbPhoneAndroid.setChecked(true);
                strPhoneType = "Android";
                edt_mobile_number_contact_id.setVisibility(View.VISIBLE);
                edt_shopMobileNumber_id.setVisibility(View.VISIBLE);
                rbMobilePhone.setVisibility(View.GONE);
                rbLandlinePhone.setVisibility(View.GONE);
                strFixSeasonalyesno = SharedPrefrence_Seller.getHawkerFixSeasonalYesNo();
                func_getEditValue();
            }
        } else if (id == R.id.rbPhoneJio) {
            if (isChecked == true) {
                strSmartPhone = "NO";
                rbSubMoving.setChecked(true);
                rbSubMoving.setVisibility(View.VISIBLE);
                rbPhoneAndroid.setChecked(true);
                strPhoneType = "Jio";
                edt_mobile_number_contact_id.setVisibility(View.VISIBLE);
                edt_shopMobileNumber_id.setVisibility(View.VISIBLE);
                rbFix.setChecked(true);
                rbMobilePhone.setVisibility(View.GONE);
                rbLandlinePhone.setVisibility(View.GONE);
                strFixSeasonalyesno = SharedPrefrence_Seller.getHawkerFixSeasonalYesNo();
                func_getEditValue();
            }


           /* if(isChecked == true){
                if(SharedPrefrence_Login.getONOFF().equals("1")){
                    SharedPrefrence_Login.savePhoneType(HomeActivity.this,"Jio");
                    startActivity(new Intent(getApplicationContext(),HawkerInfo.class));
                }else {
                    openDialog("Temp");
                }
            }
            */

        } else if (id == R.id.rbPhoneOther) {
            if (isChecked == true) {
                strSmartPhone = "NO";
                rbSubMoving.setChecked(true);
                rbSubMoving.setVisibility(View.VISIBLE);
                rbPhoneAndroid.setChecked(true);
                strPhoneType = "Other";
                edt_mobile_number_contact_id.setVisibility(View.VISIBLE);
                edt_shopMobileNumber_id.setVisibility(View.VISIBLE);
                rbFix.setChecked(true);
                rbFix.setChecked(true);
                rbMobilePhone.setVisibility(View.VISIBLE);
                rbLandlinePhone.setVisibility(View.VISIBLE);
                strFixSeasonalyesno = SharedPrefrence_Seller.getHawkerFixSeasonalYesNo();
                func_getEditValue();
            }
            /*if(isChecked == true){
                if(SharedPrefrence_Login.getONOFF().equals("1")){
                    SharedPrefrence_Login.savePhoneType(HomeActivity.this,"Other");
                    startActivity(new Intent(getApplicationContext(),HawkerInfo.class));
                }else {
                    openDialog("Temp");
                }
            }*/
        } else if (id == R.id.rbMoving) {
            if (strPhoneType.equals("Jio")) {
                rbMoving.setChecked(false);
            } else if (strPhoneType.equals("Other")) {
                rbMoving.setChecked(false);
            } else {
                if (isChecked == true) {
                    rbMoving.setChecked(true);
                    rbFix.setChecked(false);
                    rbHybrid.setChecked(false);
                    rbTemporary.setChecked(false);
                    rbSeasonal.setChecked(false);
                    strStartDate = "";
                    strEndDate = "";
                    btnStartDate.setText("");
                    btnEndDate.setText("");
                    llDateSelction.setVisibility(View.GONE);
                    new GetLatLngActivity(getApplicationContext());
                    strUserType = "Moving";
                    strFixSeasonalyesno = "YES";
                    strSubUserType = "";
                    edt_shop_address_id.setText("");
                    llFixLayout.setVisibility(View.GONE);
                    llShop_Info.setVisibility(View.GONE);
                    edt_other_business_detail_id.setText("");
                    strHawker_type_Code = "11";
                    bindSubHawkerType(strHawker_type_Code);
                    btnStartDate.setHint("12/Aug/2019");
                    btnEndDate.setHint("12/Aug/2019");

                    //SharedPreferences prefs = getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE);
                    strSelectedService = prefs.getString("catitem", "");
                    strSubSelectedService = prefs.getString("subitem","");
                    Log.d("strSelectedServicehome",strSelectedService);
                    Log.d("strSubSelecServicehome",strSubSelectedService);



                    /*strSelectedService = SharedPrefrence_Seller.getFixr_shop_Subcategoryid().replace("[", "").replace("]", "");
                    strSelectedService = strSelectedService.replaceAll("\\s+", "");*/
                    Log.d("strSelectedService","hbdvs: "+strSelectedService);

                    if (!TextUtils.isEmpty(strSelectedService)) {
                        strSelectedService = "";
                        SharedPrefrence_Seller.clearCategoryData(HomeActivity.this);
                        SharedPrefrence_Seller.saveSharedPrefrencesFixerShopCategoryId(HomeActivity.this, "");
                        SharedPrefrence_Seller.saveSharedPrefrencesFixerShopSubCategoryId(HomeActivity.this, "");
                        SharedPrefrence_Seller.saveSharedPrefrencesFixerShopSuperSubCategoryId(HomeActivity.this, "");
                    }
                    func_getEditValue();
                    isChecked = false;
                }
            }

        } else if (id == R.id.rbFix) {
            if (isChecked == true) {
                rbFix.setChecked(true);
                rbMoving.setChecked(false);
                rbHybrid.setChecked(false);
                rbTemporary.setChecked(false);
                rbSeasonal.setChecked(false);
                new GetLatLngActivity(getApplicationContext());
                strUserType = "Fix";
                strSubUserType = "";
                strStartDate = "";
                strEndDate = "";
                strFixSeasonalyesno = "YES";
                rbSeasonalYes.setChecked(true);
                btnStartDate.setText("");
                btnEndDate.setText("");
                llDateSelction.setVisibility(View.VISIBLE);
                llFixLayout.setVisibility(View.VISIBLE);
                llShop_Info.setVisibility(View.VISIBLE);
                edt_other_business_detail_id.setVisibility(View.VISIBLE);
                llBottomLayout.setVisibility(View.GONE);
                strHawker_type_Code = "12";
                bindSubHawkerType(strHawker_type_Code);
                btnStartDate.setHint("Aug,2019");
                btnEndDate.setHint("Aug,2019");

                strSelectedService = SharedPrefrence_Seller.getFixr_shop_categoryid().replace("[", "").replace("]", "");
                strSelectedService = strSelectedService.replaceAll("\\s+", "");
                if (!TextUtils.isEmpty(strSelectedService)) {
                    strSelectedService = "";
                    SharedPrefrence_Seller.clearCategoryData(HomeActivity.this);
                    SharedPrefrence_Seller.saveSharedPrefrencesFixerShopCategoryId(HomeActivity.this, "");
                    SharedPrefrence_Seller.saveSharedPrefrencesFixerShopSubCategoryId(HomeActivity.this, "");
                    SharedPrefrence_Seller.saveSharedPrefrencesFixerShopSuperSubCategoryId(HomeActivity.this, "");
                }
                func_getEditValue();
                isChecked = false;
            }
        } else if (id == R.id.rbHybrid) {
            if (strPhoneType.equals("Jio")) {
                rbHybrid.setChecked(false);
            } else if (strPhoneType.equals("Other")) {
                rbHybrid.setChecked(false);
            } else {
                if (isChecked == true) {
                    rbHybrid.setChecked(true);
                    rbMoving.setChecked(false);
                    rbFix.setChecked(false);
                    rbTemporary.setChecked(false);
                    rbSeasonal.setChecked(false);
                    llDateSelction.setVisibility(View.GONE);
                    new GetLatLngActivity(getApplicationContext());
                    strUserType = "Hybrid";
                    strFixSeasonalyesno = "YES";
                    strSubUserType = "";
                    edt_shop_address_id.setText("");
                    strStartDate = "";
                    strEndDate = "";
                    btnStartDate.setText("");
                    btnEndDate.setText("");
                    llFixLayout.setVisibility(View.GONE);
                    llShop_Info.setVisibility(View.GONE);
                    edt_other_business_detail_id.setText("");
                    strHawker_type_Code = "13";
                    bindSubHawkerType(strHawker_type_Code);
                    btnStartDate.setHint("12/Aug/2019");
                    btnEndDate.setHint("12/Aug/2019");
                    strSelectedService = SharedPrefrence_Seller.getFixr_shop_categoryid().replace("[", "").replace("]", "");
                    strSelectedService = strSelectedService.replaceAll("\\s+", "");

                    if (!TextUtils.isEmpty(strSelectedService)) {
                        strSelectedService = "";
                        SharedPrefrence_Seller.clearCategoryData(HomeActivity.this);
                        SharedPrefrence_Seller.saveSharedPrefrencesFixerShopCategoryId(HomeActivity.this, "");
                        SharedPrefrence_Seller.saveSharedPrefrencesFixerShopSubCategoryId(HomeActivity.this, "");
                        SharedPrefrence_Seller.saveSharedPrefrencesFixerShopSuperSubCategoryId(HomeActivity.this, "");
                    }
                    func_getEditValue();
                    isChecked = false;
                }
            }

        } else if (id == R.id.rbTemporary) {
            if (isChecked == true) {
                rbTemporary.setChecked(true);
                rbMoving.setChecked(false);
                rbFix.setChecked(false);
                rbHybrid.setChecked(false);
                rbSeasonal.setChecked(false);
                llDateSelction.setVisibility(View.VISIBLE);
                llBottomLayout.setVisibility(View.VISIBLE);
                new GetLatLngActivity(getApplicationContext());
                strUserType = "Temporary";
                if (strSmartPhone.equals("NO")) {
                    strSubUserType = "Fix";
                } else {
                    strSubUserType = "Moving";
                }

                btnStartDate.setText("");
                btnEndDate.setText("");

                strFixSeasonalyesno = "YES";
                edt_shop_address_id.setText("");
                llFixLayout.setVisibility(View.GONE);
                llShop_Info.setVisibility(View.GONE);
                edt_other_business_detail_id.setText("");
                strStartDate = btnStartDate.getText().toString();
                strEndDate = btnEndDate.getText().toString();
                strHawker_type_Code = "14";
                btnStartDate.setHint("12/Aug/2019");
                btnEndDate.setHint("12/Aug/2019");
                strSelectedService = SharedPrefrence_Seller.getFixr_shop_categoryid().replace("[", "").replace("]", "");
                strSelectedService = strSelectedService.replaceAll("\\s+", "");

                if (!TextUtils.isEmpty(strSelectedService)) {
                    strSelectedService = "";
                    SharedPrefrence_Seller.clearCategoryData(HomeActivity.this);
                    SharedPrefrence_Seller.saveSharedPrefrencesFixerShopCategoryId(HomeActivity.this, "");
                    SharedPrefrence_Seller.saveSharedPrefrencesFixerShopSubCategoryId(HomeActivity.this, "");
                    SharedPrefrence_Seller.saveSharedPrefrencesFixerShopSuperSubCategoryId(HomeActivity.this, "");
                }
                bindSubHawkerType(strHawker_type_Code);
                func_getEditValue();

                isChecked = false;
            }
        } else if (id == R.id.rbSeasonal) {
            if (strPhoneType.equals("Jio")) {
                rbSeasonal.setChecked(false);
            } else if (strPhoneType.equals("Other")) {
                rbSeasonal.setChecked(false);
            } else {
                if (isChecked == true) {
                    rbSeasonal.setChecked(true);
                    rbMoving.setChecked(false);
                    rbFix.setChecked(false);
                    rbHybrid.setChecked(false);
                    rbTemporary.setChecked(false);
                    llDateSelction.setVisibility(View.VISIBLE);
                    llBottomLayout.setVisibility(View.VISIBLE);
                    new GetLatLngActivity(getApplicationContext());
                    strUserType = "Seasonal";
                    strFixSeasonalyesno = "YES";
                    btnStartDate.setText("");
                    btnEndDate.setText("");
                    if (strSmartPhone.equals("NO")) {
                        strSubUserType = "Fix";
                    } else {
                        strSubUserType = "Moving";
                    }
                    llFixLayout.setVisibility(View.GONE);
                    llShop_Info.setVisibility(View.GONE);
                    edt_shop_address_id.setText("");
                    edt_other_business_detail_id.setText("");
                    strStartDate = btnStartDate.getText().toString();
                    strEndDate = btnEndDate.getText().toString();
                    strHawker_type_Code = "15";
                    btnStartDate.setHint("12/Aug/2019");
                    btnEndDate.setHint("12/Aug/2019");
                    strSelectedService = SharedPrefrence_Seller.getFixr_shop_categoryid().replace("[", "").replace("]", "");
                    strSelectedService = strSelectedService.replaceAll("\\s+", "");

                    if (!TextUtils.isEmpty(strSelectedService)) {
                        strSelectedService = "";
                        SharedPrefrence_Seller.clearCategoryData(HomeActivity.this);
                        SharedPrefrence_Seller.saveSharedPrefrencesFixerShopCategoryId(HomeActivity.this, "");
                        SharedPrefrence_Seller.saveSharedPrefrencesFixerShopSubCategoryId(HomeActivity.this, "");
                        SharedPrefrence_Seller.saveSharedPrefrencesFixerShopSuperSubCategoryId(HomeActivity.this, "");
                    }
                    bindSubHawkerType(strHawker_type_Code);
                    func_getEditValue();
                    isChecked = false;
                }
            }
        } else if (id == R.id.rbSeasonalYes) {
            if (isChecked == true) {
                strFixSeasonalyesno = "YES";
                llDateSelction.setVisibility(View.VISIBLE);
                edt_other_business_detail_id.setVisibility(View.VISIBLE);
                llBottomLayout.setVisibility(View.GONE);
                func_getEditValue();

            }
        } else if (id == R.id.rbSeasonalNo) {
            if (isChecked == true) {
                strFixSeasonalyesno = "NO";
                btnStartDate.setText("");
                btnEndDate.setText("");
                llDateSelction.setVisibility(View.GONE);
                edt_other_business_detail_id.setVisibility(View.GONE);
                func_getEditValue();
            }
        } else if (id == R.id.rbSubMoving) {
            if (isChecked == true) {
                strSubUserType = "Moving";
            }
        } else if (id == R.id.rbSubFix) {
            if (isChecked == true) {
                strSubUserType = "Fix";
            }
        } else if (id == R.id.rbMale) {
            if (isChecked == true) {
                strGender = "Male";
            }
        } else if (id == R.id.rbFemale) {
            if (isChecked == true) {
                strGender = "Female";
            }

        } else if (id == R.id.rbApplication) {
            if (isChecked == true) {
                strGPSMODE = "App";
                strGPSID = android_id;
                edt_shop_gps_id.setText("");
                edt_shop_gps_id.setVisibility(View.GONE);
            }
        } else if (id == R.id.rbGPSTracker) {
            if (isChecked == true) {
                strGPSID = edt_shop_gps_id.getText().toString().trim();
                strGPSMODE = "GPS";
                edt_shop_gps_id.setVisibility(View.VISIBLE);
            }
        } else if (id == R.id.rbOwnar) {
            if (isChecked == true) {
                strNameType = "Owner";
                etHawkerTypeID.setVisibility(View.GONE);
                etHawkerTypeID.setText("");
            }
        } else if (id == R.id.rbPartner) {
            if (isChecked == true) {
                strNameType = "Partner";
                etHawkerTypeID.setVisibility(View.GONE);
                etHawkerTypeID.setText("");
            }
        } else if (id == R.id.rbEmployee) {
            if (isChecked == true) {
                strNameType = "Employee";
                etHawkerTypeID.setVisibility(View.GONE);
                etHawkerTypeID.setText("");
            }
        } else if (id == R.id.rbOther) {
            if (isChecked == true) {
                strNameType = "Other";
                etHawkerTypeID.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.ivNav) {
            if (aBoolean == true) {
                openDrawer();
                aBoolean = true;
            } else {
                closeDrawer();
                aBoolean = true;
            }
        } else if (id == R.id.ivMenu) {
            popupMenu = new PopupMenu(HomeActivity.this, v);
            popupMenu.setOnDismissListener(new OnDismissListener());
            popupMenu.setOnMenuItemClickListener(new OnMenuItemClickListener());
            popupMenu.inflate(R.menu.home);
            popupMenu.show();
        } else if (id == R.id.rlSelectService) {
            func_getEditValue();
            startActivityForResult(new Intent(getApplicationContext(), ServiceSelctionActivity.class), 22);
        } else if (id == R.id.rlProImage) {
            func_getEditValue();
            strShopImage = "Profile";
            imagePicker = new ImagePicker().chooseFromCamera(true);
            imagePicker.withActivity(this).withCompression(true).start();
        } else if (id == R.id.ivAddressProofImage) {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                strShopImage = "Address";
                func_getEditValue();
                imagePicker = new ImagePicker().chooseFromCamera(true);
                imagePicker.withActivity(this).withCompression(true).start();
            } else {
                turnGPSOn();
            }

        } else if (id == R.id.ivIdentityProofImage) {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                func_getEditValue();
                strShopImage = "Identity";
                imagePicker = new ImagePicker().chooseFromCamera(true);
                imagePicker.withActivity(this).withCompression(true).start();
            } else {
                turnGPSOn();
            }
        } else if (id == R.id.ivIdentityProofImage2) {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                func_getEditValue();
                strShopImage = "Identity2";
                imagePicker = new ImagePicker().chooseFromCamera(true);
                imagePicker.withActivity(this).withCompression(true).start();
            } else {
                turnGPSOn();
            }
        } else if (id == R.id.ivShopImageOne) {
            func_getEditValue();
            strShopImage = "Shop1";
            imagePicker = new ImagePicker().chooseFromCamera(true);
            imagePicker.withActivity(this).withCompression(true).start();
        } else if (id == R.id.ivShopImageTwo) {
            func_getEditValue();
            strShopImage = "Shop2";
            imagePicker = new ImagePicker().chooseFromCamera(true);
            imagePicker.withActivity(this).withCompression(true).start();
        } else if (id == R.id.ivMenuImage) {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                func_getEditValue();
                strShopImage = "MenuImage";
                imagePicker = new ImagePicker().chooseFromCamera(true);
                imagePicker.withActivity(this).withCompression(true).start();
            } else {
                turnGPSOn();
            }
        } else if (id == R.id.ivMenuImage1) {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                func_getEditValue();
                strShopImage = "MenuImage1";
                imagePicker = new ImagePicker().chooseFromCamera(true);
                imagePicker.withActivity(this).withCompression(true).start();
            } else {
                turnGPSOn();
            }
        } else if (id == R.id.ivMenuImage2) {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                func_getEditValue();
                strShopImage = "MenuImage2";
                imagePicker = new ImagePicker().chooseFromCamera(true);
                imagePicker.withActivity(this).withCompression(true).start();
            } else {
                turnGPSOn();
            }
        } else if (id == R.id.ivMenuImage3) {
            if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                func_getEditValue();
                strShopImage = "MenuImage3";
                imagePicker = new ImagePicker().chooseFromCamera(true);
                imagePicker.withActivity(this).withCompression(true).start();
            } else {
                turnGPSOn();
            }
        } else if (id == R.id.tv_auto_detect_id) {
            new GetLatLngActivity(getApplicationContext());
        } else if (id == R.id.btnSend) {
            if (ConnectionDetector.getConnectionDetector(getApplicationContext()).isConnectingToInternet() == true) {
                new GetLatLngActivity(getApplicationContext());
                String spin = tvPinCode.getText().toString();
                funbtnClick(spin);
            } else {
                FancyToast.makeText(getApplicationContext(), "Check your internet connection", FancyToast.LENGTH_LONG, FancyToast.WARNING, true).show();
                CommonDialog.Show_Internt_Dialog(HomeActivity.this, "");
            }
        } else if (id == R.id.btnStartDate) {
            if (strUserType.equals("Fix")) {
                if (strFixSeasonalyesno.equals("YES")) {
                    funMonthPicker1();
                    rackMonthPicker1.show();
                }
            } else {
                funStartDate(v);
            }

        } else if (id == R.id.btnEndDate) {
            if (strUserType.equals("Fix")) {
                if (strFixSeasonalyesno.equals("YES")) {
                    funMonthPicker2();
                    rackMonthPicker2.show();
                }
            } else {
                funEndDate(v);
            }
        }
    }

    private void funMonthPicker1() {
        rackMonthPicker1 = new RackMonthPicker(this).setPositiveButton(new DateMonthDialogListener() {
            @Override
            public void onDateMonth(int month, int startDate, int endDate, int year, String monthLabel) {
                if (year < iyear) {
                    CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Please Select Current Year", MessageConstant.toast_warning);
                } else {
                    btnStartDate.setText(monthLabel);
                }
            }
        }).setNegativeButton(new OnCancelMonthDialogListener() {
            @Override
            public void onCancel(android.app.AlertDialog dialog) {
                dialog.dismiss();
            }
        });
    }

    private void funMonthPicker2() {
        rackMonthPicker2 = new RackMonthPicker(this).setPositiveButton(new DateMonthDialogListener() {
            @Override
            public void onDateMonth(int month, int startDate, int endDate, int year, String monthLabel) {
                if (year < iyear) {
                    CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Please Select Current Year", MessageConstant.toast_warning);
                } else {
                    btnEndDate.setText(monthLabel);
                }
            }
        }).setNegativeButton(new OnCancelMonthDialogListener() {
            @Override
            public void onCancel(android.app.AlertDialog dialog) {
                dialog.dismiss();
            }
        });
    }

    /* Function For send Data*/
    private void funbtnClick(final String spincode) {
        //Toast.makeText(getApplicationContext(),"hello",Toast.LENGTH_LONG).show();
        // Toast.makeText(this, SharedPrefrence_Seller.getHAWKERCALLSTATUS(), Toast.LENGTH_SHORT).show();
        if (SharedPrefrence_Login.getONOFF().equals("1")) {
            if (strUserType.equals("Fix")) {
                if (strSmartPhone.equals("NO")) {
                    if (validation.checkEditText(getApplicationContext(), edt_name_id, "Enter Hawker Name") == true
                            && validation.checkEditText(getApplicationContext(), edt_bussiness_name, "Enter Bussiness Name") == true
                            && validation.checkEditText(getApplicationContext(), edt_residential_address_id, "Enter Residential Address") == true
                            && validation.checkEditText(getApplicationContext(), edt_shop_state_id, "Press Button") == true
                            && validation.checkEditText(getApplicationContext(), edt_shop_address_id, "Enter Shop Address") == true) {
                        if (!SharedPrefrence_Seller.getHawkerOtherService().equals("")) {
                          /*  if(strIndentityProofImage.equals("")){
                                CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Capture Identity Proof Image Front", MessageConstant.toast_warning);
                            }else if(strIndentityProofImage2.equals("")){
                                CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Capture Identity Proof Image Back", MessageConstant.toast_warning);
                            }else*/
                            if (strShopImageOne.equals("")) {
                                CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Capture Shop Image", MessageConstant.toast_warning);
                            } else if (strGPSMODE.equals("GPS")) {
                                if (validation.checkEditText(getApplicationContext(), edt_shop_gps_id, "Enter GPS ID") == true) {
                                    if (ConnectionDetector.getConnectionDetector(getApplicationContext()).isConnectingToInternet() == true) {
                                        func_getEditValue();
                                        if (strUserType.equals("Temporary") || strUserType.equals("Seasonal")) {
                                            if (strStartDate.equals("")) {
                                                CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Select Start Date", MessageConstant.toast_warning);
                                            } else if (strEndDate.equals("")) {
                                                CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Select End Date", MessageConstant.toast_warning);
                                            } else {
                                                requestAPI(spincode);
                                            }
                                        } else {
                                            requestAPI(spincode);
                                        }
                                    } else {
                                        FancyToast.makeText(getApplicationContext(), "Check your internet connection", FancyToast.LENGTH_LONG, FancyToast.WARNING, true).show();
                                    }
                                }
                            } else {
                                if (ConnectionDetector.getConnectionDetector(getApplicationContext()).isConnectingToInternet() == true) {
                                    func_getEditValue();
                                    if (strUserType.equals("Temporary") || strUserType.equals("Seasonal")) {
                                        if (strStartDate.equals("")) {
                                            CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Select Start Date", MessageConstant.toast_warning);
                                        } else if (strEndDate.equals("")) {
                                            CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Select End Date", MessageConstant.toast_warning);
                                        } else {
                                            requestAPI(spincode);
                                        }
                                    } else {
                                        requestAPI(spincode);
                                    }
                                } else {
                                    FancyToast.makeText(getApplicationContext(), "Check your internet connection", FancyToast.LENGTH_LONG, FancyToast.WARNING, true).show();
                                }
                            }
                        } else {
                            if (strSelectedService == null) {
                                CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Select Service", MessageConstant.toast_warning);
                            } else if (TextUtils.isEmpty(strSelectedService)) {
                                CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Select Service", MessageConstant.toast_warning);
                            } else if (strSelectedService.equals("[]")) {
                                CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Select Service", MessageConstant.toast_warning);
                            } else if (strSelectedService.equals("")) {
                                CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Select Service", MessageConstant.toast_warning);
                            }/*else if(strIndentityProofImage.equals("")){
                                CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Capture Identity Proof Image Front", MessageConstant.toast_warning);
                            }else if(strIndentityProofImage2.equals("")){
                                CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Capture Identity Proof Image Back", MessageConstant.toast_warning);
                            }*/ else if (strShopImageOne.equals("")) {
                                CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Capture Shop Image", MessageConstant.toast_warning);
                            } else if (strGPSMODE.equals("GPS")) {
                                if (validation.checkEditText(getApplicationContext(), edt_shop_gps_id, "Enter GPS ID") == true) {
                                    if (ConnectionDetector.getConnectionDetector(getApplicationContext()).isConnectingToInternet() == true) {
                                        func_getEditValue();
                                        if (strUserType.equals("Temporary") || strUserType.equals("Seasonal")) {
                                            if (strStartDate.equals("")) {
                                                CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Select Start Date", MessageConstant.toast_warning);
                                            } else if (strEndDate.equals("")) {
                                                CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Select End Date", MessageConstant.toast_warning);
                                            } else {
                                                requestAPI(spincode);
                                            }
                                        } else {
                                            requestAPI(spincode);
                                        }
                                    } else {
                                        FancyToast.makeText(getApplicationContext(), "Check your internet connection", FancyToast.LENGTH_LONG, FancyToast.WARNING, true).show();
                                    }
                                }
                            } else {
                                if (ConnectionDetector.getConnectionDetector(getApplicationContext()).isConnectingToInternet() == true) {
                                    func_getEditValue();
                                    if (strUserType.equals("Temporary") || strUserType.equals("Seasonal")) {
                                        if (strStartDate.equals("")) {
                                            CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Select Start Date", MessageConstant.toast_warning);
                                        } else if (strEndDate.equals("")) {
                                            CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Select End Date", MessageConstant.toast_warning);
                                        } else {
                                            requestAPI(spincode);
                                        }
                                    } else {
                                        requestAPI(spincode);
                                    }
                                } else {
                                    FancyToast.makeText(getApplicationContext(), "Check your internet connection", FancyToast.LENGTH_LONG, FancyToast.WARNING, true).show();
                                }
                            }
                            /*End*/
                        }
                    }

                } else {
                    func_getEditValue();
                    if (strFixSeasonalyesno.equals("YES")) {
                        if (edt_other_business_detail_id.getText().toString().equals("")) {
                            CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Enter other business detail when season off", MessageConstant.toast_warning);
                        } else if (strStartDate.equals("")) {
                            CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Select Start Date", MessageConstant.toast_warning);
                        } else if (strEndDate.equals("")) {
                            CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Select End Date", MessageConstant.toast_warning);
                        } else {
                            funFixDataserver(spincode);
                        }
                    } else {
                        funFixDataserver(spincode);
                    }
                }
            } else {
                if (strSmartPhone.equals("NO")) {
                    if (validation.checkEditText(getApplicationContext(), edt_name_id, "Enter Hawker Name") == true
                            && validation.checkEditText(getApplicationContext(), edt_bussiness_name, "Enter Bussiness Name") == true
                            && validation.checkEditText(getApplicationContext(), edt_residential_address_id, "Enter Residential Address") == true) {
                        if (strSelectedService == null) {
                            CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Select Service", MessageConstant.toast_warning);
                        } else if (TextUtils.isEmpty(strSelectedService)) {
                            CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Select Service", MessageConstant.toast_warning);
                        } else if (strSelectedService.equals("[]")) {
                            CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Select Service", MessageConstant.toast_warning);
                        } else if (strSelectedService.equals("")) {
                            CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Select Service", MessageConstant.toast_warning);
                        }/* else if(strIndentityProofImage.equals("")){
                            CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Capture Identity Proof Image Front", MessageConstant.toast_warning);
                        }else if(strIndentityProofImage2.equals("")){
                            CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Capture Identity Proof Image Back", MessageConstant.toast_warning);
                        }*/ else if (strShopImageOne.equals("")) {
                            CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Capture Shop Image", MessageConstant.toast_warning);
                        } else if (strGPSMODE.equals("GPS")) {
                            if (validation.checkEditText(getApplicationContext(), edt_shop_gps_id, "Enter GPS ID") == true) {
                                if (ConnectionDetector.getConnectionDetector(getApplicationContext()).isConnectingToInternet() == true) {
                                    edt_shop_address_id.setText("");
                                    func_getEditValue();
                                    if (strUserType.equals("Temporary") || strUserType.equals("Seasonal")) {
                                        if (strStartDate.equals("")) {
                                            CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Select Start Date", MessageConstant.toast_warning);
                                        } else if (strEndDate.equals("")) {
                                            CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Select End Date", MessageConstant.toast_warning);
                                        } else {
                                            requestAPI(spincode);
                                        }
                                    } else {
                                        requestAPI(spincode);
                                    }
                                } else {
                                    FancyToast.makeText(getApplicationContext(), "Check your internet connection", FancyToast.LENGTH_LONG, FancyToast.WARNING, true).show();
                                }
                            }
                        } else {
                            if (ConnectionDetector.getConnectionDetector(getApplicationContext()).isConnectingToInternet() == true) {
                                func_getEditValue();
                                if (strUserType.equals("Temporary") || strUserType.equals("Seasonal")) {
                                    if (strStartDate.equals("")) {
                                        CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Select Start Date", MessageConstant.toast_warning);
                                    } else if (strEndDate.equals("")) {
                                        CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Select End Date", MessageConstant.toast_warning);
                                    } else {
                                        requestAPI(spincode);
                                    }
                                } else {
                                    requestAPI(spincode);
                                }
                            } else {
                                FancyToast.makeText(getApplicationContext(), "Check your internet connection", FancyToast.LENGTH_LONG, FancyToast.WARNING, true).show();
                            }
                        }
                    }
                } else {
                    if (validation.checkEditText(getApplicationContext(), edt_name_id, "Enter Hawker Name") == true
                            && validation.checkEditText(getApplicationContext(), edt_bussiness_name, "Enter Bussiness Name") == true
                            && validation.checkNumber2(getApplicationContext(), edt_mobile_number_contact_id, "Enter Bussiness Contact Mobile Number") == true
                            && validation.checkEditText(getApplicationContext(), edt_residential_address_id, "Enter Residential Address") == true) {
                        /*&& validation.checkNumber1(getApplicationContext(), edt_shopMobileNumber_id, "Enter Bussiness Mobile Number") == true*/
                        if (!SharedPrefrence_Seller.getHawkerOtherService().equals("")) {
                             /*if(strIndentityProofImage.equals("")){
                                CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Capture Identity Proof Image Front", MessageConstant.toast_warning);
                            }else if(strIndentityProofImage2.equals("")){
                                CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Capture Identity Proof Image Back", MessageConstant.toast_warning);
                            }else*/
                            if (strShopImageOne.equals("")) {
                                CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Capture Shop Image", MessageConstant.toast_warning);
                            } else if (strGPSMODE.equals("GPS")) {
                                if (validation.checkEditText(getApplicationContext(), edt_shop_gps_id, "Enter GPS ID") == true) {
                                    if (ConnectionDetector.getConnectionDetector(getApplicationContext()).isConnectingToInternet() == true) {
                                        edt_shop_address_id.setText("");
                                        func_getEditValue();
                                        if (strUserType.equals("Temporary") || strUserType.equals("Seasonal")) {
                                            if (strStartDate.equals("")) {
                                                CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Select Start Date", MessageConstant.toast_warning);
                                            } else if (strEndDate.equals("")) {
                                                CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Select End Date", MessageConstant.toast_warning);
                                            } else {
                                                requestAPI(spincode);
                                            }
                                        } else {
                                            requestAPI(spincode);
                                        }
                                    } else {
                                        FancyToast.makeText(getApplicationContext(), "Check your internet connection", FancyToast.LENGTH_LONG, FancyToast.WARNING, true).show();
                                    }
                                }
                            } else {
                                if (ConnectionDetector.getConnectionDetector(getApplicationContext()).isConnectingToInternet() == true) {
                                    func_getEditValue();
                                    if (strUserType.equals("Temporary") || strUserType.equals("Seasonal")) {
                                        if (strStartDate.equals("")) {
                                            CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Select Start Date", MessageConstant.toast_warning);
                                        } else if (strEndDate.equals("")) {
                                            CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Select End Date", MessageConstant.toast_warning);
                                        } else {
                                            requestAPI(spincode);
                                        }
                                    } else {
                                        requestAPI(spincode);
                                    }
                                } else {
                                    FancyToast.makeText(getApplicationContext(), "Check your internet connection", FancyToast.LENGTH_LONG, FancyToast.WARNING, true).show();
                                }
                            }
                        } else {
                            if (strSelectedService == null) {
                                CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Select Service", MessageConstant.toast_warning);
                            } else if (TextUtils.isEmpty(strSelectedService)) {
                                CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Select Service", MessageConstant.toast_warning);
                            } else if (strSelectedService.equals("[]")) {
                                CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Select Service", MessageConstant.toast_warning);
                            } else if (strSelectedService.equals("")) {
                                CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Select Service", MessageConstant.toast_warning);
                            }/*else if(strIndentityProofImage.equals("")){
                                CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Capture Identity Proof Image Front", MessageConstant.toast_warning);
                            }else if(strIndentityProofImage2.equals("")){
                                CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Capture Identity Proof Image Back", MessageConstant.toast_warning);
                            }*/ else if (strShopImageOne.equals("")) {
                                CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Capture Shop Image", MessageConstant.toast_warning);
                            } else if (strGPSMODE.equals("GPS")) {
                                if (validation.checkEditText(getApplicationContext(), edt_shop_gps_id, "Enter GPS ID") == true) {
                                    if (ConnectionDetector.getConnectionDetector(getApplicationContext()).isConnectingToInternet() == true) {
                                        edt_shop_address_id.setText("");
                                        func_getEditValue();
                                        if (strUserType.equals("Temporary") || strUserType.equals("Seasonal")) {
                                            if (strStartDate.equals("")) {
                                                CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Select Start Date", MessageConstant.toast_warning);
                                            } else if (strEndDate.equals("")) {
                                                CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Select End Date", MessageConstant.toast_warning);
                                            } else {
                                                requestAPI(spincode);
                                            }
                                        } else {
                                            requestAPI(spincode);
                                        }
                                    } else {
                                        FancyToast.makeText(getApplicationContext(), "Check your internet connection", FancyToast.LENGTH_LONG, FancyToast.WARNING, true).show();
                                    }
                                }
                            } else {
                                if (ConnectionDetector.getConnectionDetector(getApplicationContext()).isConnectingToInternet() == true) {
                                    func_getEditValue();
                                    if (strUserType.equals("Temporary") || strUserType.equals("Seasonal")) {
                                        if (strStartDate.equals("")) {
                                            CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Select Start Date", MessageConstant.toast_warning);
                                        } else if (strEndDate.equals("")) {
                                            CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Select End Date", MessageConstant.toast_warning);
                                        } else {
                                            requestAPI(spincode);
                                        }
                                    } else {
                                        requestAPI(spincode);
                                    }
                                } else {
                                    FancyToast.makeText(getApplicationContext(), "Check your internet connection", FancyToast.LENGTH_LONG, FancyToast.WARNING, true).show();
                                }
                            }
                        }
                    }
                }
            }
        } else {
            openDialog("");
        }
    }


    private void funFixDataserver(final String spincode) {
        if (validation.checkEditText(getApplicationContext(), edt_name_id, "Enter Hawker Name") == true
                && validation.checkEditText(getApplicationContext(), edt_bussiness_name, "Enter Bussiness Name") == true
                && validation.checkNumber2(getApplicationContext(), edt_mobile_number_contact_id, "Enter Bussiness Contact Mobile Number") == true
                && validation.checkEditText(getApplicationContext(), edt_residential_address_id, "Enter Residential Address") == true
                && validation.checkEditText(getApplicationContext(), edt_shop_state_id, "Press Button") == true
                && validation.checkEditText(getApplicationContext(), edt_shop_address_id, "Enter Shop Address") == true) {

            /* && validation.checkNumber1(getApplicationContext(), edt_shopMobileNumber_id, "Enter Bussiness Mobile Number") == true*/

            if (!SharedPrefrence_Seller.getHawkerOtherService().equals("")) {
                /*if(strIndentityProofImage.equals("")){
                    CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Capture Identity Proof Image Front", MessageConstant.toast_warning);
                }else if(strIndentityProofImage.equals("")){
                    CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Capture Identity Proof Image Back", MessageConstant.toast_warning);
                }else*/
                if (strShopImageOne.equals("")) {
                    CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Capture Shop Image", MessageConstant.toast_warning);
                } else if (strGPSMODE.equals("GPS")) {
                    if (validation.checkEditText(getApplicationContext(), edt_shop_gps_id, "Enter GPS ID") == true) {
                        if (ConnectionDetector.getConnectionDetector(getApplicationContext()).isConnectingToInternet() == true) {
                            func_getEditValue();
                            if (strUserType.equals("Temporary") || strUserType.equals("Seasonal")) {
                                if (strStartDate.equals("")) {
                                    CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Select Start Date", MessageConstant.toast_warning);
                                } else if (strEndDate.equals("")) {
                                    CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Select End Date", MessageConstant.toast_warning);
                                } else {
                                    requestAPI(spincode);
                                }
                            } else {
                                requestAPI(spincode);
                            }
                        } else {
                            FancyToast.makeText(getApplicationContext(), "Check your internet connection", FancyToast.LENGTH_LONG, FancyToast.WARNING, true).show();
                        }
                    }
                } else {
                    if (ConnectionDetector.getConnectionDetector(getApplicationContext()).isConnectingToInternet() == true) {
                        func_getEditValue();
                        if (strUserType.equals("Temporary") || strUserType.equals("Seasonal")) {
                            if (strStartDate.equals("")) {
                                CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Select Start Date", MessageConstant.toast_warning);
                            } else if (strEndDate.equals("")) {
                                CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Select End Date", MessageConstant.toast_warning);
                            } else {
                                requestAPI(spincode);
                            }
                        } else {
                            requestAPI(spincode);
                        }
                    } else {
                        FancyToast.makeText(getApplicationContext(), "Check your internet connection", FancyToast.LENGTH_LONG, FancyToast.WARNING, true).show();
                    }
                }
            } else {
                if (strSelectedService == null) {
                    CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Select Service", MessageConstant.toast_warning);
                } else if (TextUtils.isEmpty(strSelectedService)) {
                    CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Select Service", MessageConstant.toast_warning);
                } else if (strSelectedService.equals("[]")) {
                    CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Select Service", MessageConstant.toast_warning);
                } else if (strSelectedService.equals("")) {
                    CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Select Service", MessageConstant.toast_warning);
                }/*else if(strIndentityProofImage.equals("")){
                    CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Capture Identity Proof Image Front", MessageConstant.toast_warning);
                }else if(strIndentityProofImage2.equals("")){
                    CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Capture Identity Proof Image Back", MessageConstant.toast_warning);
                }*/ else if (strShopImageOne.equals("")) {
                    CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Capture Shop Image", MessageConstant.toast_warning);
                } else if (strGPSMODE.equals("GPS")) {
                    if (validation.checkEditText(getApplicationContext(), edt_shop_gps_id, "Enter GPS ID") == true) {
                        if (ConnectionDetector.getConnectionDetector(getApplicationContext()).isConnectingToInternet() == true) {
                            func_getEditValue();
                            if (strUserType.equals("Temporary") || strUserType.equals("Seasonal")) {
                                if (strStartDate.equals("")) {
                                    CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Select Start Date", MessageConstant.toast_warning);
                                } else if (strEndDate.equals("")) {
                                    CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Select End Date", MessageConstant.toast_warning);
                                } else {
                                    requestAPI(spincode);
                                }
                            } else {
                                requestAPI(spincode);
                            }
                        } else {
                            FancyToast.makeText(getApplicationContext(), "Check your internet connection", FancyToast.LENGTH_LONG, FancyToast.WARNING, true).show();
                        }
                    }
                } else {
                    if (ConnectionDetector.getConnectionDetector(getApplicationContext()).isConnectingToInternet() == true) {
                        func_getEditValue();
                        if (strUserType.equals("Temporary") || strUserType.equals("Seasonal")) {
                            if (strStartDate.equals("")) {
                                CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Select Start Date", MessageConstant.toast_warning);
                            } else if (strEndDate.equals("")) {
                                CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Select End Date", MessageConstant.toast_warning);
                            } else {
                                requestAPI(spincode);
                            }
                        } else {
                            requestAPI(spincode);
                        }
                    } else {
                        FancyToast.makeText(getApplicationContext(), "Check your internet connection", FancyToast.LENGTH_LONG, FancyToast.WARNING, true).show();
                    }
                }
            }
        }
    }

    private void requestAPI(final String sspin) {
        //SharedPreferences prefs = getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE);
        strSelectedService = prefs.getString("catitem", "");
        strSubSelectedService = prefs.getString("subitem","");
        Log.d("strSelectedServicehome",strSelectedService);
        Log.d("strSubSelecServicehome",strSubSelectedService);
        if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (!location_update.LATTITUDE.equals("0.0")) {
                if (strNameType.equals("Other")) {
                    if (validation.checkEditText(getApplicationContext(), etHawkerTypeID, "If other (Enter Details)") == true) {
                        funSendDatatoServer(sspin);
                    }
                } else {
                    funSendDatatoServer(sspin);
                }
            } else {
                location_update = new Location_Update(HomeActivity.this);
                FancyToast.makeText(getApplicationContext(), "Yet! Your GPS not work properly.", FancyToast.LENGTH_LONG, FancyToast.WARNING, true).show();
            }
        } else {
            turnGPSOn();
        }
    }

    private void funDateIntialize() {
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat_two = new SimpleDateFormat("dd/MMM/yyyy");
        calendar = Calendar.getInstance();
        day = calendar.get(Calendar.DAY_OF_MONTH);
        currentmonth = calendar.get(Calendar.MONTH);
        iyear = calendar.get(Calendar.YEAR);
        currentmonth = currentmonth + 1;
        currentDateString = day + "/" + currentmonth + "/" + iyear;
        finalStringDate = currentDateString;
        try {
            currentDate = dateFormat.parse(currentDateString);
            finalStringDate = dateFormat_two.format(currentDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void funStartDate(View v) {
        if (dialogfrom == null)
            dialogfrom = new DatePickerDialog(v.getContext(), new PickDate(), calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1,
                    calendar.get(Calendar.DAY_OF_MONTH));
        dialogfrom.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        // dialogfrom.getDatePicker().setMinDate(System.currentTimeMillis());
        dialogfrom.show();

    }

    private void funEndDate(View v) {
        if (dialogEnd == null)
            dialogEnd = new DatePickerDialog(v.getContext(), new PickEndDate(), calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1,
                    calendar.get(Calendar.DAY_OF_MONTH));
        dialogEnd.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        //   dialogEnd.getDatePicker().setMinDate(System.currentTimeMillis());
        dialogEnd.show();
    }

    /*Date Picker*/

    private class PickDate implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            monthOfYear = monthOfYear + 1;
            String dateString = dayOfMonth + "/" + monthOfYear + "/" + year;
            try {
                selectedDateFrom = dateFormat.parse(dateString);
                if (selectedDateFrom.compareTo(currentDate) >= 0) {
                    finalStringDate = dateString;
                    /*  dateString = 10/8/2019  */
                    finalStringDate = dateFormat_two.format(selectedDateFrom);
                    btnStartDate.setText(finalStringDate);
                } else {
                    FancyToast.makeText(getApplicationContext(), "Selected date can not be greater than current date", FancyToast.LENGTH_LONG, FancyToast.WARNING, true).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class PickEndDate implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            monthOfYear = monthOfYear + 1;
            String dateString = dayOfMonth + "/" + monthOfYear + "/" + year;
            try {
                selectedDateFrom = dateFormat.parse(dateString);
                if (selectedDateFrom.compareTo(currentDate) >= 0) {
                    finalStringDate = dateString;
                    /*dateString = 10/8/2019*/
                    finalStringDate = dateFormat_two.format(selectedDateFrom);
                    btnEndDate.setText(finalStringDate);
                } else {
                    FancyToast.makeText(getApplicationContext(), "Selected date can not be greater than current date", FancyToast.LENGTH_LONG, FancyToast.WARNING, true).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void startJob() {
        Job job = jobDispatcher.newJobBuilder().
                setService(MyJobService.class).
                setLifetime(Lifetime.FOREVER).
                setRecurring(true).
                setTag(Job_Tag).
                setTrigger(Trigger.executionWindow(5, 10)).
                setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL).
                setReplaceCurrent(false).
                setConstraints(Constraint.ON_ANY_NETWORK).
                build();
        jobDispatcher.mustSchedule(job);
        /*  jobDispatcher.schedule(job);
            jobDispatcher.mustSchedule(job);  */
        // Toast.makeText(HomeActivity.this, "Job start", Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 101) {
            if (resultCode == 0) {
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                finish();
                finishAffinity();
            }
        } else if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                FancyToast.makeText(getApplicationContext(), "GPS ON", FancyToast.LENGTH_LONG, FancyToast.SUCCESS, true).show();
                /*Open GPS Dialog and get Lat/Lon*/
                new GetLatLngActivity(HomeActivity.this);
                location_update = new Location_Update(HomeActivity.this);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        } else if (requestCode == ImagePicker.SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                imagePicker.addOnCompressListener(new ImageCompressionListener() {
                    @Override
                    public void onStart() {
                    }

                    @Override
                    public void onCompressed(String filePath) {
                        if (strShopImage.equals("Profile")) {
                            latlonProfile = SharedPrefrence_Seller.getIMAGELATITUDELONGITUDE();
                            if (!latlonProfile.equals("")) {
                                String[] aa = latlonProfile.split(",");
                                PRO_LAT = aa[0];
                                PRO_LON = aa[1];
                                Bitmap selectedImage = BitmapFactory.decodeFile(filePath);
                                iv_profile_id.setImageBitmap(selectedImage);
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                byte[] b = baos.toByteArray();
                                str_profileImage = Base64.encodeToString(b, Base64.DEFAULT);
                                func_getEditValue();
                            } else {
                                FancyToast.makeText(getApplicationContext(), "Please Trun ON Camera location", FancyToast.LENGTH_LONG, FancyToast.WARNING, true).show();
                            }
                        }
                        if (strShopImage.equals("Shop1")) {
                            latlonShop1 = SharedPrefrence_Seller.getIMAGELATITUDELONGITUDE();
                            if (!latlonShop1.equals("")) {
                                String[] aa = latlonShop1.split(",");
                                SHOP1_LAT = aa[0];
                                SHOP1_LON = aa[1];
                                Bitmap selectedImage = BitmapFactory.decodeFile(filePath);
                                //uploadBitmap(selectedImage);
                                ivShopImageOne.setImageBitmap(selectedImage);
                                // ReadExif(filePath);
                                ivShopImageOne.setScaleType(ImageView.ScaleType.FIT_XY);
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                byte[] b = baos.toByteArray();
                                strShopImageOne = Base64.encodeToString(b, Base64.DEFAULT);
                                func_getEditValue();

                            } else {
                                FancyToast.makeText(getApplicationContext(), "Please Trun ON Camera location", FancyToast.LENGTH_LONG, FancyToast.WARNING, true).show();
                            }
                        } else if (strShopImage.equals("Shop2")) {
                            latlonShop2 = SharedPrefrence_Seller.getIMAGELATITUDELONGITUDE();
                            if (!latlonShop2.equals("")) {
                                String[] aa = latlonShop1.split(",");
                                SHOP2_LAT = aa[0];
                                SHOP2_LON = aa[1];
                                Bitmap selectedImage = BitmapFactory.decodeFile(filePath);
                                ivShopImageTwo.setImageBitmap(selectedImage);
                                ivShopImageTwo.setScaleType(ImageView.ScaleType.FIT_XY);
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                byte[] b = baos.toByteArray();
                                strShopImageTwo = Base64.encodeToString(b, Base64.DEFAULT);
                                func_getEditValue();
                            } else {
                                FancyToast.makeText(getApplicationContext(), "Please Trun ON Camera location", FancyToast.LENGTH_LONG, FancyToast.WARNING, true).show();
                            }
                        } else if (strShopImage.equals("Identity")) {
                            latlonShop3 = SharedPrefrence_Seller.getIMAGELATITUDELONGITUDE();
                            if (!latlonShop3.equals("")) {
                                String[] aa = latlonShop3.split(",");
                                IDENTITY_LAT = aa[0];
                                IDENTITY_LON = aa[1];

                               /* IDENTITY_LAT = location_update.LATTITUDE;
                                IDENTITY_LON = location_update.LONGITUDE;*/

                                Bitmap selectedImage = BitmapFactory.decodeFile(filePath);
                                ivIdentityProofImage.setImageBitmap(selectedImage);
                                ivIdentityProofImage.setScaleType(ImageView.ScaleType.FIT_XY);
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                byte[] b = baos.toByteArray();
                                strIndentityProofImage = Base64.encodeToString(b, Base64.DEFAULT);
                                func_getEditValue();
                            } else {
                                FancyToast.makeText(getApplicationContext(), "Please Trun ON Camera location", FancyToast.LENGTH_LONG, FancyToast.WARNING, true).show();
                            }
                        } else if (strShopImage.equals("Identity2")) {
                            latlonShop3 = SharedPrefrence_Seller.getIMAGELATITUDELONGITUDE();
                            if (!latlonShop3.equals("")) {
                                String[] aa = latlonShop3.split(",");
                                IDENTITY_LAT = aa[0];
                                IDENTITY_LON = aa[1];

                               /* IDENTITY_LAT = location_update.LATTITUDE;
                                IDENTITY_LON = location_update.LONGITUDE;*/

                                Bitmap selectedImage = BitmapFactory.decodeFile(filePath);
                                ivIdentityProofImage2.setImageBitmap(selectedImage);
                                ivIdentityProofImage2.setScaleType(ImageView.ScaleType.FIT_XY);
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                byte[] b = baos.toByteArray();
                                strIndentityProofImage2 = Base64.encodeToString(b, Base64.DEFAULT);
                                func_getEditValue();
                            } else {
                                FancyToast.makeText(getApplicationContext(), "Please Trun ON Camera location", FancyToast.LENGTH_LONG, FancyToast.WARNING, true).show();
                            }
                        } else if (strShopImage.equals("Address")) {
                            latlonShop3 = SharedPrefrence_Seller.getIMAGELATITUDELONGITUDE();
                            if (!latlonShop3.equals("")) {
                                String[] aa = latlonShop3.split(",");
                                ADDRESS_LAT = aa[0];
                                ADDRESS_LON = aa[1];
                                Bitmap selectedImage = BitmapFactory.decodeFile(filePath);
                                ivAddressProofImage.setImageBitmap(selectedImage);
                                ivAddressProofImage.setScaleType(ImageView.ScaleType.FIT_XY);
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                byte[] b = baos.toByteArray();
                                strAddressProofImage = Base64.encodeToString(b, Base64.DEFAULT);
                                func_getEditValue();
                            } else {
                                FancyToast.makeText(getApplicationContext(), "Please Trun ON Camera location", FancyToast.LENGTH_LONG, FancyToast.WARNING, true).show();
                            }
                        } else if (strShopImage.equals("MenuImage")) {
                            latlonShop3 = SharedPrefrence_Seller.getIMAGELATITUDELONGITUDE();
                            if (!latlonShop3.equals("")) {
                                String[] aa = latlonShop3.split(",");
                                MENU_LAT = aa[0];
                                MENU_LON = aa[1];
                                Bitmap selectedImage = BitmapFactory.decodeFile(filePath);
                                ivMenuImage.setImageBitmap(selectedImage);
                                ivMenuImage.setScaleType(ImageView.ScaleType.FIT_XY);
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                byte[] b = baos.toByteArray();
                                strMenuImage = Base64.encodeToString(b, Base64.DEFAULT);
                                func_getEditValue();
                            } else {
                                FancyToast.makeText(getApplicationContext(), "Please Trun ON Camera location", FancyToast.LENGTH_LONG, FancyToast.WARNING, true).show();
                            }
                        } else if (strShopImage.equals("MenuImage1")) {
                            latlonShop3 = SharedPrefrence_Seller.getIMAGELATITUDELONGITUDE();
                            if (!latlonShop3.equals("")) {
                                String[] aa = latlonShop3.split(",");
                                MENU_LAT = aa[0];
                                MENU_LON = aa[1];
                                Bitmap selectedImage = BitmapFactory.decodeFile(filePath);
                                ivMenuImage1.setImageBitmap(selectedImage);
                                ivMenuImage1.setScaleType(ImageView.ScaleType.FIT_XY);
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                byte[] b = baos.toByteArray();
                                strMenuImage1 = Base64.encodeToString(b, Base64.DEFAULT);
                                func_getEditValue();
                            } else {
                                FancyToast.makeText(getApplicationContext(), "Please Trun ON Camera location", FancyToast.LENGTH_LONG, FancyToast.WARNING, true).show();
                            }
                        } else if (strShopImage.equals("MenuImage2")) {
                            latlonShop3 = SharedPrefrence_Seller.getIMAGELATITUDELONGITUDE();
                            if (!latlonShop3.equals("")) {
                                String[] aa = latlonShop3.split(",");
                                MENU_LAT = aa[0];
                                MENU_LON = aa[1];
                                Bitmap selectedImage = BitmapFactory.decodeFile(filePath);
                                ivMenuImage2.setImageBitmap(selectedImage);
                                ivMenuImage2.setScaleType(ImageView.ScaleType.FIT_XY);
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                byte[] b = baos.toByteArray();
                                strMenuImage2 = Base64.encodeToString(b, Base64.DEFAULT);
                                func_getEditValue();
                            } else {
                                FancyToast.makeText(getApplicationContext(), "Please Trun ON Camera location", FancyToast.LENGTH_LONG, FancyToast.WARNING, true).show();
                            }
                        } else if (strShopImage.equals("MenuImage3")) {
                            latlonShop3 = SharedPrefrence_Seller.getIMAGELATITUDELONGITUDE();
                            if (!latlonShop3.equals("")) {
                                String[] aa = latlonShop3.split(",");
                                MENU_LAT = aa[0];
                                MENU_LON = aa[1];
                                Bitmap selectedImage = BitmapFactory.decodeFile(filePath);
                                ivMenuImage3.setImageBitmap(selectedImage);
                                ivMenuImage3.setScaleType(ImageView.ScaleType.FIT_XY);
                                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                                byte[] b = baos.toByteArray();
                                strMenuImage3 = Base64.encodeToString(b, Base64.DEFAULT);
                                func_getEditValue();
                            } else {
                                FancyToast.makeText(getApplicationContext(), "Please Trun ON Camera location", FancyToast.LENGTH_LONG, FancyToast.WARNING, true).show();
                            }
                        } else {
                            Bitmap selectedImage = BitmapFactory.decodeFile(filePath);
                            iv_profile_id.setImageBitmap(selectedImage);
                            ByteArrayOutputStream baos = new ByteArrayOutputStream();
                            selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                            byte[] b = baos.toByteArray();
                            str_profileImage = Base64.encodeToString(b, Base64.DEFAULT);
                            func_getEditValue();
                        }
                    }
                });
                String filePath = imagePicker.getImageFilePath(data);
                if (filePath != null) {
                    Bitmap selectedImage = BitmapFactory.decodeFile(filePath);
                    latlonProfile = SharedPrefrence_Seller.getIMAGELATITUDELONGITUDE();
                    if (!latlonProfile.equals("")) {
                        iv_profile_id.setImageBitmap(selectedImage);
                    } else {
                        Toast.makeText(HomeActivity.this, "Please allow camera location ", Toast.LENGTH_SHORT).show();
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
            }

        } else if (requestCode == 22) {
            fucn_ScreenDataRestore();

            myMultimap = ArrayListMultimap.create();
            strFromTime = data.getStringExtra("FROMTIME");
            strToTime = data.getStringExtra("TOTIME");
            strDays = data.getStringExtra("DAYS");
            strDays = spiltString(strDays);
            SharedPrefrence_Seller.saveWorkingDays(HomeActivity.this, strDays, strFromTime, strToTime);
            strSelectedService = SharedPrefrence_Seller.getFixr_shop_categoryid().replace("[", "").replace("]", "");
            strSelectedService = strSelectedService.replaceAll("\\s+", "");

            //strSubSelectedService = SharedPrefrence_Seller.getFixr_shop_Subcategoryid();

            //Toast.makeText(getApplicationContext(), strSelectedService + "", Toast.LENGTH_LONG).show();
            strSubSelectedService = spiltString(strSubSelectedService);
            strSuperSubSelectedService = SharedPrefrence_Seller.getFixr_shop_SuperSubcategoryid();
            strSuperSubSelectedService = spiltString(strSuperSubSelectedService);
            //Toast.makeText(this, strSubSelectedService, Toast.LENGTH_SHORT).show();

        }
    }

    private String spiltString(String strSubSelectedService) {
        Matcher matcher = Pattern.compile("\\[([^\\]]+)").matcher(strSubSelectedService);
        List<String> tags = new ArrayList<>();
        int pos = -1;
        while (matcher.find(pos + 1)) {
            pos = matcher.start();
            tags.add(matcher.group(1));
        }
        strString1 = String.valueOf(tags);
        strString1 = strString1.replace("[", "").replace("]", "");
        strString1 = strString1.replaceAll("\\s+", "");
        return strString1;
    }


    @Override
    public void onPause() {
        super.onPause();
        if (strClearData.equals("")) {
            initID();
            func_getEditValue();
        } else {

        }
    }

    private void func_getEditValue() {
        SharedPrefrence_Seller.setIncommingNumber(HomeActivity.this, "", "0");
        strHowkerName = edt_name_id.getText().toString().trim();
        strHowkerResidentialAddress = edt_residential_address_id.getText().toString().trim();
        strHowkerMobileNumber = "";
        strHowkerBussinessName = edt_bussiness_name.getText().toString().trim();
        strHowkerShopName = edt_shop_name_id.getText().toString().trim();
        strHowkerShopAdress = edt_shop_address_id.getText().toString().trim();
        strHowkerShopeMobileNumber = edt_shopMobileNumber_id.getText().toString().trim();
        strHowkerStateCity = edt_city_id.getText().toString().trim() + " (" + edt_shop_state_id.getText().toString().trim() + ")";
        strRegisterAddress = tvRegisteredAddress.getText().toString().trim();
        strHowkerGSTNUMBER = edt_gst_number.getText().toString().trim();
        strHowkerTypeOtherDetail = etHawkerTypeID.getText().toString().trim();
        strHowkerContactNumber = edt_mobile_number_contact_id.getText().toString().trim();
        strStartDate = btnStartDate.getText().toString().trim();
        strEndDate = btnEndDate.getText().toString().trim();
        strOther_Sub_HawkerType = edt_other_sub_hawker_type.getText().toString().trim();
        strOtherBusinessDetails = edt_other_business_detail_id.getText().toString().trim();

        SharedPrefrence_Seller.saveHowkerRegistration(getApplicationContext(), strHowkerName, strUserType, strHowkerResidentialAddress,
                strHowkerMobileNumber, "12345", strHowkerBussinessName, "", "", strHowkerShopName, strHowkerShopAdress,
                strHowkerShopeMobileNumber, strSelectedService, str_profileImage, strIndentityProofImage, strAddressProofImage, strShopImageOne,
                strShopImageTwo, strGPSID, latitude + "", longitude + "", strGPSMODE, SharedPrefrence_Login.getPid(), strRegisterAddress,
                strHowkerStateCity, strFromTime, strToTime, strNameType, strGender, strSubHawkerName, strSubUserType, strMobileType = "", strSmartPhone, strDays,
                strStartDate, strEndDate, strHowkerGSTNUMBER, strHowkerTypeOtherDetail, strHowkerContactNumber, iSubHawkerNamePosition, strHawker_type_Code,
                strOther_Sub_HawkerType, strPhoneType, PRO_LAT, PRO_LON, SHOP1_LAT, SHOP1_LON, SHOP2_LAT, SHOP2_LON, ADDRESS_LAT, ADDRESS_LON,
                IDENTITY_LAT, IDENTITY_LON, strFixSeasonalyesno, strOtherBusinessDetails, strMenuImage, MENU_LAT, MENU_LON, strIndentityProofImage2,
                strMenuImage1, strMenuImage2, strMenuImage3, "Main", PHONE_TYPE);
    }

    private void fucn_ScreenDataRestore() {
        SharedPrefrence_Seller.getHowkerRegistration(HomeActivity.this);
        edt_name_id.setText(SharedPrefrence_Seller.getHowkerName());
        edt_residential_address_id.setText(SharedPrefrence_Seller.getHowkerResidentialAddress());
        edt_bussiness_name.setText(SharedPrefrence_Seller.getHowkerBussinessName());
        edt_shop_name_id.setText(SharedPrefrence_Seller.getHowkerShopName());
        if (!SharedPrefrence_Seller.getHowkerShopAdress().equals("")) {
            edt_shop_address_id.setText(SharedPrefrence_Seller.getHowkerShopAdress());
        }

        if (!SharedPrefrence_Seller.getHowkerShopeMobileNumber().equals("")) {
            edt_shopMobileNumber_id.setText(SharedPrefrence_Seller.getHowkerShopeMobileNumber());
            edt_mobile_number_contact_id.setText(SharedPrefrence_Seller.getHowkerContactNumber());
        } else if (!SharedPrefrence_Seller.getHowkerContactNumber().equals("")) {
            edt_shopMobileNumber_id.setText(SharedPrefrence_Seller.getHowkerShopeMobileNumber());
            edt_mobile_number_contact_id.setText(SharedPrefrence_Seller.getHowkerContactNumber());
        } else {
            if (!SharedPrefrence_Seller.getHAWKERINCOMMINGNUMBER().equals("")) {
                edt_shopMobileNumber_id.setText(SharedPrefrence_Seller.getHAWKERINCOMMINGNUMBER());
                edt_mobile_number_contact_id.setText(SharedPrefrence_Seller.getHAWKERINCOMMINGNUMBER());
            } else {
                edt_shopMobileNumber_id.setText(SharedPrefrence_Seller.getHowkerShopeMobileNumber());
                edt_mobile_number_contact_id.setText(SharedPrefrence_Seller.getHowkerContactNumber());
            }
        }
        if (SharedPrefrence_Seller.getPHONE_TYPE().equals("PH")) {
            PHONE_TYPE = SharedPrefrence_Seller.getPHONE_TYPE();
            rbMobilePhone.setChecked(true);
            edt_mobile_number_contact_id.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        } else if (SharedPrefrence_Seller.getPHONE_TYPE().equals("LL")) {
            PHONE_TYPE = SharedPrefrence_Seller.getPHONE_TYPE();
            rbLandlinePhone.setChecked(true);
            edt_mobile_number_contact_id.setFilters(new InputFilter[]{new InputFilter.LengthFilter(13)});
        } else {
            PHONE_TYPE = "PH";
            rbMobilePhone.setChecked(true);
            edt_mobile_number_contact_id.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        }

        // edt_shop_address_id.setText(SharedPrefrence_Seller.getHowkerGPSID());
        String startdate = SharedPrefrence_Seller.getHowkerStartDate();
        String enddate = SharedPrefrence_Seller.getHowkerEndDate();
        btnStartDate.setText(startdate);
        btnEndDate.setText(enddate);
        PRO_LAT = SharedPrefrence_Seller.getHowkerPRO_LAT();
        PRO_LON = SharedPrefrence_Seller.getHowkerPRO_LON();
        SHOP1_LAT = SharedPrefrence_Seller.getHowkerSHOP1_LAT();
        SHOP1_LON = SharedPrefrence_Seller.getHowkerSHOP1_LON();
        SHOP2_LAT = SharedPrefrence_Seller.getHowkerSHOP2_LAT();
        SHOP2_LON = SharedPrefrence_Seller.getHowkerSHOP2_LON();
        ADDRESS_LAT = SharedPrefrence_Seller.getHowkerADDRESS_LAT();
        ADDRESS_LON = SharedPrefrence_Seller.getHowkerADDRESS_LON();
        IDENTITY_LAT = SharedPrefrence_Seller.getHowkerIDENTITY_LAT();
        IDENTITY_LON = SharedPrefrence_Seller.getHowkerIDENTITY_LON();
        MENU_LAT = SharedPrefrence_Seller.getMENU_LAT();
        MENU_LON = SharedPrefrence_Seller.getMENU_LON();
        String ProfileImg = SharedPrefrence_Seller.getHowkerProfileImage();
        String IndentityProofImage1 = SharedPrefrence_Seller.getHowkerIndentityProofImage();
        String IndentityProofImage2 = SharedPrefrence_Seller.getIndentityProofImage2();
        String AddressProofImage = SharedPrefrence_Seller.getHowkerAddressProofImage();
        String ShopImageOne = SharedPrefrence_Seller.getHowkerShopImageOne();
        String ShopImageTwo = SharedPrefrence_Seller.getHowkerShopImageTwo();
        String MenuImage1 = SharedPrefrence_Seller.getMenuImage();
        String MenuImage2 = SharedPrefrence_Seller.getMenuImage1();
        String MenuImage3 = SharedPrefrence_Seller.getMenuImage2();
        String MenuImage4 = SharedPrefrence_Seller.getMenuImage3();

        if (!TextUtils.isEmpty(ProfileImg)) {
            Bitmap sBitmap = StringToBitMap(ProfileImg);
            iv_profile_id.setImageBitmap(sBitmap);
            str_profileImage = ProfileImg;
        }
        if (!TextUtils.isEmpty(IndentityProofImage1)) {
            Bitmap sBitmap = StringToBitMap(IndentityProofImage1);
            ivIdentityProofImage.setImageBitmap(sBitmap);
            ivIdentityProofImage.setScaleType(ImageView.ScaleType.FIT_XY);
            strIndentityProofImage = IndentityProofImage1;
        }
        if (!TextUtils.isEmpty(IndentityProofImage2)) {
            Bitmap sBitmap = StringToBitMap(IndentityProofImage2);
            ivIdentityProofImage2.setImageBitmap(sBitmap);
            ivIdentityProofImage2.setScaleType(ImageView.ScaleType.FIT_XY);
            strIndentityProofImage2 = IndentityProofImage2;
        }
        if (!TextUtils.isEmpty(AddressProofImage)) {
            Bitmap sBitmap = StringToBitMap(AddressProofImage);
            ivAddressProofImage.setImageBitmap(sBitmap);
            ivAddressProofImage.setScaleType(ImageView.ScaleType.FIT_XY);
            strAddressProofImage = AddressProofImage;
        }
        if (!TextUtils.isEmpty(ShopImageOne)) {
            Bitmap sBitmap = StringToBitMap(ShopImageOne);
            ivShopImageOne.setImageBitmap(sBitmap);
            ivShopImageOne.setScaleType(ImageView.ScaleType.FIT_XY);
            strShopImageOne = ShopImageOne;
        }
        if (!TextUtils.isEmpty(ShopImageTwo)) {
            Bitmap sBitmap = StringToBitMap(ShopImageTwo);
            ivShopImageTwo.setImageBitmap(sBitmap);
            ivShopImageTwo.setScaleType(ImageView.ScaleType.FIT_XY);
            strShopImageTwo = ShopImageTwo;
        }

        if (!TextUtils.isEmpty(MenuImage1)) {
            Bitmap sBitmap = StringToBitMap(MenuImage1);
            ivMenuImage.setImageBitmap(sBitmap);
            ivMenuImage.setScaleType(ImageView.ScaleType.FIT_XY);
            strMenuImage = MenuImage1;
        }
        if (!TextUtils.isEmpty(MenuImage2)) {
            Bitmap sBitmap = StringToBitMap(MenuImage2);
            ivMenuImage1.setImageBitmap(sBitmap);
            ivMenuImage1.setScaleType(ImageView.ScaleType.FIT_XY);
            strMenuImage1 = MenuImage2;
        }
        if (!TextUtils.isEmpty(MenuImage3)) {
            Bitmap sBitmap = StringToBitMap(MenuImage3);
            ivMenuImage2.setImageBitmap(sBitmap);
            ivMenuImage2.setScaleType(ImageView.ScaleType.FIT_XY);
            strMenuImage2 = MenuImage3;
        }
        if (!TextUtils.isEmpty(MenuImage4)) {
            Bitmap sBitmap = StringToBitMap(MenuImage4);
            ivMenuImage3.setImageBitmap(sBitmap);
            ivMenuImage3.setScaleType(ImageView.ScaleType.FIT_XY);
            strMenuImage3 = MenuImage4;
        }

        edt_other_sub_hawker_type.setText(SharedPrefrence_Seller.getOther_Sub_HawkerType());

        if (SharedPrefrence_Seller.getHowkerSmartPhone().equals("Android")) {
            strPhoneType = "Android";
            rbPhoneAndroid.setChecked(true);
            strFixSeasonalyesno = SharedPrefrence_Seller.getHawkerFixSeasonalYesNo();
        } else if (SharedPrefrence_Seller.getHowkerSmartPhone().equals("Jio Phone")) {
            strPhoneType = "Jio Phone";
            rbPhoneJio.setChecked(true);
            strFixSeasonalyesno = SharedPrefrence_Seller.getHawkerFixSeasonalYesNo();
        } else if (SharedPrefrence_Seller.getHowkerSmartPhone().equalsIgnoreCase("NO")) {
            if (SharedPrefrence_Seller.getHowkerPhoneType().equals("Jio")) {
                strPhoneType = "Jio";
                rbPhoneJio.setChecked(true);
                strFixSeasonalyesno = SharedPrefrence_Seller.getHawkerFixSeasonalYesNo();
            } else if (SharedPrefrence_Seller.getHowkerPhoneType().equals("Other")) {
                strPhoneType = "Other";
                rbPhoneOther.setChecked(true);
                strFixSeasonalyesno = SharedPrefrence_Seller.getHawkerFixSeasonalYesNo();
            }
        } else if (SharedPrefrence_Seller.getHowkerSmartPhone().equals("Other")) {
            strPhoneType = "Other";
            rbPhoneOther.setChecked(true);
        } else {
            if (SharedPrefrence_Seller.getHowkerPhoneType().equals("Jio")) {
                strPhoneType = "Jio";
                rbPhoneJio.setChecked(true);
            } else if (SharedPrefrence_Seller.getHowkerPhoneType().equals("Other")) {
                strPhoneType = "Other";
                rbPhoneOther.setChecked(true);
            } else {
                rbPhoneAndroid.setChecked(true);
                strPhoneType = "Android";
            }
        }

        if (SharedPrefrence_Seller.getHowkerType().equals("Fix")) {
            rbFix.setChecked(true);
            new GetLatLngActivity(getApplicationContext());
            strUserType = "Fix";
            llShop_Info.setVisibility(View.VISIBLE);
            // bindSubHawkerType("12");
            String ss = SharedPrefrence_Seller.getiSubHawkerNamePosition();
            if (!ss.equals("")) {
                spHawkerSubType.setSelection(Integer.parseInt(ss));
            }

            String sStar = SharedPrefrence_Seller.getHawkerFixSeasonalYesNo();

            if (sStar.equals("YES")) {
                rbSeasonalYes.setChecked(true);
                rbSeasonalNo.setChecked(false);
                strFixSeasonalyesno = SharedPrefrence_Seller.getHawkerFixSeasonalYesNo();
                llDateSelction.setVisibility(View.VISIBLE);
                llBottomLayout.setVisibility(View.GONE);
                edt_other_business_detail_id.setVisibility(View.VISIBLE);
                edt_other_business_detail_id.setText(SharedPrefrence_Seller.getstrOtherBusinessDetails());
            } else if (SharedPrefrence_Seller.getHawkerFixSeasonalYesNo().equals("NO")) {
                rbSeasonalYes.setChecked(false);
                rbSeasonalNo.setChecked(true);
                btnStartDate.setText("");
                btnEndDate.setText("");
                llDateSelction.setVisibility(View.GONE);
                edt_other_business_detail_id.setVisibility(View.GONE);
                strFixSeasonalyesno = SharedPrefrence_Seller.getHawkerFixSeasonalYesNo();
            } else {
                rbSeasonalYes.setChecked(true);
                rbSeasonalNo.setChecked(false);
                strFixSeasonalyesno = "YES";
                llDateSelction.setVisibility(View.VISIBLE);
                llBottomLayout.setVisibility(View.GONE);
                edt_other_business_detail_id.setVisibility(View.VISIBLE);
            }
        } else if (SharedPrefrence_Seller.getHowkerType().equals("Hybrid")) {
            rbHybrid.setChecked(true);
            strUserType = "Hybrid";
            edt_shop_address_id.setText("");
            llShop_Info.setVisibility(View.GONE);
            //  bindSubHawkerType("13");
            String ss = SharedPrefrence_Seller.getiSubHawkerNamePosition();
            if (!ss.equals("")) {
                spHawkerSubType.setSelection(Integer.parseInt(ss));
            }
        } else if (SharedPrefrence_Seller.getHowkerType().equals("Moving")) {
            rbMoving.setChecked(true);
            strUserType = "Moving";
            edt_shop_address_id.setText("");
            llShop_Info.setVisibility(View.GONE);
            //   bindSubHawkerType("11");
            String ss = SharedPrefrence_Seller.getiSubHawkerNamePosition();
            if (!ss.equals("")) {
                spHawkerSubType.setSelection(Integer.parseInt(ss));
            }
        } else if (SharedPrefrence_Seller.getHowkerType().equals("Seasonal")) {
            rbSeasonal.setChecked(true);
            strUserType = "Seasonal";
            edt_shop_address_id.setText("");
            llShop_Info.setVisibility(View.GONE);
            // bindSubHawkerType("15");
            String ss = SharedPrefrence_Seller.getiSubHawkerNamePosition();
            if (!ss.equals("")) {
                spHawkerSubType.setSelection(Integer.parseInt(ss));
            }
        } else if (SharedPrefrence_Seller.getHowkerType().equals("Temporary")) {
            rbTemporary.setChecked(true);
            strUserType = "Temporary";
            edt_shop_address_id.setText("");
            llShop_Info.setVisibility(View.GONE);
            //  bindSubHawkerType("14");
            String ss = SharedPrefrence_Seller.getiSubHawkerNamePosition();
            if (!ss.equals("")) {
                spHawkerSubType.setSelection(Integer.parseInt(ss));
            }
        } else {
            rbMoving.setChecked(true);
            strUserType = "Moving";
            edt_shop_address_id.setText("");
            llShop_Info.setVisibility(View.GONE);
            bindSubHawkerType("11");
            String ss = SharedPrefrence_Seller.getiSubHawkerNamePosition();
            if (!ss.equals("")) {
                spHawkerSubType.setSelection(Integer.parseInt(ss));
            }
        }

        if (!SharedPrefrence_Seller.getHowkerType().equals("")) {
            bindSubHawkerType(SharedPrefrence_Seller.getstrHawker_type_Code());
        }
        if (SharedPrefrence_Seller.getstrNameType().equals("Owner")) {
            etHawkerTypeID.setVisibility(View.GONE);
            rbOwnar.setChecked(true);
            strNameType = "Owner";
        } else if (SharedPrefrence_Seller.getstrNameType().equals("Other")) {
            etHawkerTypeID.setVisibility(View.VISIBLE);
            etHawkerTypeID.setText(SharedPrefrence_Seller.getHowkerTypeOtherDetail());
            rbOther.setChecked(true);
            strNameType = "Other";
        } else if (SharedPrefrence_Seller.getstrNameType().equals("Partner")) {
            etHawkerTypeID.setVisibility(View.GONE);
            rbPartner.setChecked(true);
            strNameType = "Partner";
        } else if (SharedPrefrence_Seller.getstrNameType().equals("Employee")) {
            etHawkerTypeID.setVisibility(View.GONE);
            rbEmployee.setChecked(true);
            strNameType = "Employee";
        } else {
            rbOwnar.setChecked(true);
            strNameType = "Owner";
        }
        if (SharedPrefrence_Seller.getHowkerGender().equals("Male")) {
            rbMale.setChecked(true);
            strGender = "Male";
        } else if (SharedPrefrence_Seller.getHowkerGender().equals("Female")) {
            rbFemale.setChecked(true);
            strGender = "Female";
        } else {
            rbMale.setChecked(true);
            strGender = "Male";
        }

        if (SharedPrefrence_Seller.getHowkerSmartPhone().equals("YES")) {
            if (SharedPrefrence_Seller.getHowkerSubUserType().equals("Moving")) {
                strSmartPhone = "YES";
                rbSubMoving.setChecked(true);
                strSubUserType = "Moving";
            } else if (SharedPrefrence_Seller.getHowkerSubUserType().equals("Fix")) {
                strSmartPhone = "YES";
                rbSubFix.setChecked(true);
                strSubUserType = "Fix";
            }
        } else if (SharedPrefrence_Seller.getHowkerSmartPhone().equals("NO")) {
            if (SharedPrefrence_Seller.getHowkerSubUserType().equals("Moving")) {
                strSmartPhone = "NO";
                rbSubMoving.setChecked(true);
                strSubUserType = "Moving";
            } else if (SharedPrefrence_Seller.getHowkerSubUserType().equals("Fix")) {
                strSmartPhone = "NO";
                rbSubFix.setChecked(true);
                strSubUserType = "Fix";
            }
        } else {
            strSmartPhone = "YES";
            rbSubMoving.setChecked(true);
            strSubUserType = "Moving";
            rbSubMoving.setVisibility(View.VISIBLE);
        }
    }

    /**
     * @param encodedString
     * @return bitmap (from given string)
     */
    public Bitmap StringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
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
                                status.startResolutionForResult(HomeActivity.this, 1000);
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

    /////////////////////////////////////////////// DIALOG ALL START ///////////////////////////////////////////////////
    private void openDialogSSSS() {
        Dialog dutyDialog = new Dialog(HomeActivity.this);
        dutyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dutyDialog.setContentView(R.layout.dialog_active_inactive);
        dutyDialog.setCanceledOnTouchOutside(false);
        dutyDialog.setCancelable(false);
        dutyDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dutyDialog.findViewById(R.id.close_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    if (ConnectionDetector.getConnectionDetector(getApplicationContext()).isConnectingToInternet() == true) {
                        fun_DutyONOFFScreen("0", SharedPrefrence_Login.getPid());
                    } else {
                        FancyToast.makeText(getApplicationContext(), "Check your internet connection", FancyToast.LENGTH_LONG, FancyToast.WARNING, true).show();
                    }
                } else {
                    turnGPSOn();
                }
                HomeActivity.this.cancel(true);
                finish();
                finishAffinity();
            }
        });
        dutyDialog.show();
    }

    /* Dialog for Trun On Duty Status*/
    private void openDialog(final String temp) {
        final Dialog dutyDialog = new Dialog(HomeActivity.this);
        dutyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dutyDialog.setContentView(R.layout.dialog_underprocessing);
        dutyDialog.setCanceledOnTouchOutside(false);
        dutyDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView success_text = dutyDialog.findViewById(R.id.success_text);
        success_text.setText("Duty Status");
        TextView textView_success = dutyDialog.findViewById(R.id.textView_success);
        textView_success.setText("Turn on your duty");
        dutyDialog.findViewById(R.id.close_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dutyDialog.dismiss();
                dutyDialogSec(temp);
            }
        });
        dutyDialog.show();
    }

    private void dutyDialogSec(final String temp) {
        final Dialog dutyDialog = new Dialog(HomeActivity.this);
        dutyDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dutyDialog.setContentView(R.layout.dialog_duty_on_off);
        dutyDialog.setCanceledOnTouchOutside(false);
        dutyDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        swActiveInactive = dutyDialog.findViewById(R.id.swActiveInactive);
        swActiveInactive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    if (bSwitch == true) {
                        if (!location_update.LATTITUDE.equals("0.0")) {
                            Toast.makeText(HomeActivity.this, "Yet!." + location_update.LATTITUDE, Toast.LENGTH_SHORT).show();
                            swActiveInactive.setText("Duty ON");
                            fun_DutyONOFF("1", SharedPrefrence_Login.getPid(), temp);
                            SharedPrefrence_Login.saveONOFF(HomeActivity.this, "1");
                            bSwitch = false;
                        } else {
                            Toast.makeText(HomeActivity.this, "Yet! Your GPS not work properly." + location_update.LATTITUDE, Toast.LENGTH_SHORT).show();
                        }
                        dutyDialog.dismiss();
                    } else {
                        swActiveInactive.setText("Duty OFF");
                        fun_DutyONOFF("0", SharedPrefrence_Login.getPid(), temp);
                        SharedPrefrence_Login.saveONOFF(HomeActivity.this, "0");
                        bSwitch = true;
                        dutyDialog.dismiss();
                    }
                } else {
                    turnGPSOn();
                }
            }
        });
        fun_DutyONOFF("2", SharedPrefrence_Login.getPid(), temp);
        dutyDialog.show();
    }

    private void successDialog() {
        final Dialog successDialog = new Dialog(HomeActivity.this);
        successDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        successDialog.setContentView(R.layout.dialog_success_layout);
        successDialog.setCanceledOnTouchOutside(false);
        successDialog.setCancelable(false);
        successDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        successDialog.findViewById(R.id.close_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                successDialog.dismiss();
                strClearData = "Clear";
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                finish();
                finishAffinity();
            }
        });
        successDialog.show();
    }

    private void resetDialog() {
        final Dialog resetDialog = new Dialog(HomeActivity.this);
        resetDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        resetDialog.setContentView(R.layout.dialog_reset_layout);
        resetDialog.setCanceledOnTouchOutside(false);
        resetDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        resetDialog.findViewById(R.id.close_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetDialog.dismiss();
                strClearData = "Clear";
                SharedPrefrence_Seller.ClearSharedPrefrenc(HomeActivity.this);
                SharedPrefrence_Seller.ClearTempHawkerData(HomeActivity.this);
                startActivity(new Intent(getApplicationContext(), HomeActivity.class));
                finish();
                finishAffinity();

            }
        });
        resetDialog.show();
    }

//    _______________________________________________________________________________________________________

    public void confirmationNumber(Context context, final String number, String done) {
        this.mmContext = context;
        final Dialog confirmNumber = new Dialog(mmContext);
        confirmNumber.requestWindowFeature(Window.FEATURE_NO_TITLE);
        confirmNumber.setContentView(R.layout.dialog_confirmation_layout);
        confirmNumber.setCanceledOnTouchOutside(false);
        confirmNumber.setCancelable(false);
        confirmNumber.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView tvNumber = confirmNumber.findViewById(R.id.tvNumber);
        tvNumber.setText("Mobile number is : \n" + number);
        confirmNumber.findViewById(R.id.ok_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmNumber.dismiss();
                edt_shopMobileNumber_id.setText(number);
                edt_mobile_number_contact_id.setText(number);
                SharedPrefrence_Seller.setIncommingNumber(mmContext.getApplicationContext(), number, "1");
            }
        });
        confirmNumber.show();
    }

    private void cancel(boolean b) {
    }
////////////////////////////////////////  DIALOG ALL  END  //////////////////////////////////////////////////////////////////////

    //////////////////////////////////////////  API  REQUESTS ALL START   /////////////////////////////////////////////////////////////
    private void bindSubHawkerType(final String hawker_type_code) {
        if (ConnectionDetector.getConnectionDetector(getApplicationContext()).isConnectingToInternet() == true) {
            requestQueue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
            final ProgressDialog _progressDialog = ProgressDialog.show(HomeActivity.this, "", "Please wait...", true,
                    false, new DialogInterface.OnCancelListener() {
                        @Override
                        public void onCancel(DialogInterface dialog) {
                            HomeActivity.this.cancel(true);
                            finish();
                        }
                    }
            );
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.URL_HAWKER_TYPE_DATA,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.length() != 0) {
                                _progressDialog.dismiss();
                                parseHawkerTypeData(response);
                            }
                            _progressDialog.dismiss();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            _progressDialog.dismiss();
                            if (error.getClass().equals(TimeoutError.class)) {
                                FancyToast.makeText(getApplicationContext(), "It took longer than expected to get the response from Server.", FancyToast.LENGTH_LONG, FancyToast.WARNING, true).show();
                            } else {
                                FancyToast.makeText(getApplicationContext(), "Server Respond Error ! Please try again.", FancyToast.LENGTH_LONG, FancyToast.WARNING, true).show();
                            }
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String, String> params = new HashMap<>();
                    params.put("hawker_type_code", hawker_type_code);

                    return params;
                }
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            VolleySingleton.getInstance(getApplicationContext()).addToRequestQue(stringRequest);
        } else {
            FancyToast.makeText(getApplicationContext(), "Check your internet connection", FancyToast.LENGTH_LONG, FancyToast.WARNING, true).show();
            CommonDialog.Show_Internt_Dialog(HomeActivity.this, "");
        }
    }

    private void parseHawkerTypeData(String response) {
        try {
            JSONObject obj = new JSONObject(response);
            String strStatau = obj.getString("status");
            if (strStatau.equals("1")) {
                String str = obj.getString("data");
                JSONArray jsonArray = new JSONArray(str);
                sHawkerName = new String[jsonArray.length()];
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject sub_objects = jsonArray.getJSONObject(i);
                    sHawkerName[i] = sub_objects.getString("hawker__sub_type_name").toString();
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(HomeActivity.this, R.layout.support_simple_spinner_dropdown_item, sHawkerName);
                spHawkerSubType.setAdapter(adapter);
                String ss = SharedPrefrence_Seller.getiSubHawkerNamePosition();
                if (!ss.equals("")) {
                    spHawkerSubType.setSelection(Integer.parseInt(ss));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /* API For Profile*/
    private void func_ProfileAPI(final String sales_ID) {
        requestQueue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        //if everything is fine
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.URL_SALES_PROFILE,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            String str = obj.getString("data");
                            JSONObject jsoObject = new JSONObject(str);
                            if (jsoObject.getString("status").equals("1")) {
                                String s_name = jsoObject.getString("name");
                                // tvName.setText(s_name);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("sales_id", sales_ID);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQue(stringRequest);
    }

    /* API For Active /Inavtive Status*/
    private void fun_Active_Inactive(final String salesID, final String device_id, final String spinnn) {
        final RequestQueue requestQueue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        StringRequest getRequest = new StringRequest(Request.Method.POST, Urls.URL_USER_VALIDATION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            String str = obj.getString("data");
                            JSONObject jsoObject = new JSONObject(str);
                            if (jsoObject.getString("status").equals("0")) {
                                Log.i(TAG, "Query Failed");
                                String city_status = jsoObject.getString("city_status");
                                if (city_status.equals("0")) {
                                    Log.i(TAG, "Not Lauch! City Not Matched");
                                    // String message = jsoObject.getString("message");
                                    //  Dialog_.getInstanceDialog(HomeActivity.this).dialog_MessageAppStop(getApplicationContext(),message);
                                    CommonDialog.dialog_MessageAppStop(HomeActivity.this, "You current location is " + location_update.city + ". We are not providing service in this area.");
                                }
                            } else if (jsoObject.getString("status").equals("1")) {
                                String city_status = jsoObject.getString("city_status");
                                if (city_status.equals("0")) {
                                    Log.i(TAG, "Not Lauch! City Not Matched");
                                    String message = jsoObject.getString("message");
                                    //  Dialog_.getInstanceDialog(HomeActivity.this).dialog_MessageAppStop(getApplicationContext(),message);
                                    // CommonDialog.dialog_MessageAppStop(HomeActivity.this,message);
                                    CommonDialog.dialog_MessageAppStop(HomeActivity.this, "You current location is " + location_update.city + ". We are not providing service in this area.");

                                } else if (city_status.equals("1")) {
                                    Log.i(TAG, "App Launch");
                                    String active_status = jsoObject.getString("active_status");
                                    if (active_status.equals("0")) {
                                        //Temporary blocked
                                        openDialogSSSS();
                                        Log.i(TAG, "reject");
                                    } else if (active_status.equals("1")) {
                                        /* API For Profile*/
                                        func_ProfileAPI(SharedPrefrence_Login.getPid());
                                        Log.i(TAG, "active");

                                    } else if (active_status.equals("3")) {
                                        Log.i(TAG, "logout from other device");
                                        String message = jsoObject.getString("message");
                                        logout_status = "LogOut";
                                        fun_DutyONOFF("0", SharedPrefrence_Login.getPid(), "");
                                        CommonDialog.dialog_MessageAppStop(HomeActivity.this, message);
                                    } else if (active_status.equals("4")) {
                                        Log.i(TAG, "hawker deactive");
                                        String message = jsoObject.getString("message");
                                        CommonDialog.dialog_MessageAppStop(HomeActivity.this, message);
                                    }
                                } else if (city_status.equals("2")) {
                                    Log.i(TAG, "Coming Soon");
                                    String message = jsoObject.getString("message");
                                    CommonDialog.dialog_MessageAppStop(HomeActivity.this, "Coming Soon");
                                } else if (city_status.equals("3")) {
                                    Log.i(TAG, "Not Lauch Yet");
                                    String message = jsoObject.getString("message");
                                    CommonDialog.dialog_MessageAppStop(HomeActivity.this, message);
                                    //Dialog_.getInstanceDialog(HomeActivity.this).dialog_MessageAppStop(getApplicationContext(),message);
                                } else if (city_status.equals("4")) {
                                    Log.i(TAG, "Service Deactivated form this city");
                                    String message = jsoObject.getString("message");
                                    CommonDialog.dialog_MessageAppStop(HomeActivity.this, message);
                                    // Dialog_.getInstanceDialog(HomeActivity.this).dialog_MessageAppStop(getApplicationContext(),message);
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
                        /*if (error.getClass().equals(TimeoutError.class)) {
                            Toast.makeText(getApplicationContext(), "It took longer than expected to get the response from Server.", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getApplicationContext(), "Something went wrong ! Please try again.", Toast.LENGTH_SHORT).show();
                        }*/
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("sales_id", salesID);
                params.put("device_id", device_id);
                params.put("city", sCity);
                if (!TextUtils.isEmpty(spinnn)) {
                    params.put("sPin", spinnn);
                } else {
                    params.put("sPin", "");
                }

                return params;
            }
        };
        requestQueue.add(getRequest);
    }

    /* API for Update Duty Status*/

    private void fun_DutyONOFF(final String sStatus, final String Sales_ID, final String temp) {
        final RequestQueue requestQueue = VolleySingleton.getInstance(HomeActivity.this).getRequestQueue();
        StringRequest getRequest = new StringRequest(Request.Method.POST, Urls.URL_DUTY_ON_OFF_SALES,
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
                                if (strDuty_status.equals("1")) {
                                    startJob();
                                    swActiveInactive.setText("Duty ON");
                                    swActiveInactiveScreen.setText("Duty ON");
                                    swActiveInactiveScreen.setChecked(true);
                                    swActiveInactive.setChecked(true);
                                    SharedPrefrence_Login.saveONOFF(HomeActivity.this, "1");
                                    bSwitch = false;
                                    /* if(temp.equals("Temp")){
                                        startActivity(new Intent(getApplicationContext(),HawkerInfo.class));
                                    }else {
                                    } */
                                    // notif("Duty ","ON",true);
                                    // Toast.makeText(HomeActivity.this, "On", Toast.LENGTH_SHORT).show();
                                } else {
                                    if (logout_status.equals("LogOut")) {
                                        funLogout(SharedPrefrence_Login.getPid());
                                    } else {
                                        jobDispatcher.cancelAll();
                                        swActiveInactive.setText("Duty OFF");
                                        swActiveInactiveScreen.setText("Duty OFF");
                                        swActiveInactive.setChecked(false);
                                        swActiveInactiveScreen.setChecked(false);
                                        SharedPrefrence_Login.saveONOFF(HomeActivity.this, "0");
                                        bSwitch = true;
                                        // notif("Duty ","OFF",false);
                                        // Toast.makeText(HomeActivity.this, "OFF", Toast.LENGTH_SHORT).show();
                                    }

                                }
                            } else {
                                if (strDuty_status.equals("1")) {
                                    startJob();
                                    swActiveInactive.setText("Duty ON");
                                    swActiveInactiveScreen.setText("Duty ON");
                                    swActiveInactiveScreen.setChecked(true);
                                    swActiveInactive.setChecked(true);
                                    bSwitch = false;
                                    SharedPrefrence_Login.saveONOFF(HomeActivity.this, "1");
                                   /* if(temp.equals("Temp")){
                                        startActivity(new Intent(getApplicationContext(),HawkerInfo.class));
                                    }*/
                                    // notif("Duty ","ON",true);
                                    // Toast.makeText(HomeActivity.this, "On", Toast.LENGTH_SHORT).show();
                                } else {
                                    if (logout_status.equals("LogOut")) {
                                        funLogout(SharedPrefrence_Login.getPid());
                                    } else {
                                        jobDispatcher.cancelAll();
                                        swActiveInactive.setText("Duty OFF");
                                        swActiveInactiveScreen.setText("Duty OFF");
                                        swActiveInactive.setChecked(false);
                                        swActiveInactiveScreen.setChecked(false);
                                        bSwitch = true;
                                        SharedPrefrence_Login.saveONOFF(HomeActivity.this, "0");
                                        //  notif("Duty ","OFF",false);
                                        //  Toast.makeText(HomeActivity.this, "OFF", Toast.LENGTH_SHORT).show();
                                    }
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
                        if (error.getClass().equals(TimeoutError.class)) {
                            FancyToast.makeText(getApplicationContext(), "It took longer than expected to get the response from Server.", FancyToast.LENGTH_LONG, FancyToast.WARNING, true).show();
                        } else {
                            FancyToast.makeText(getApplicationContext(), "Server Respond Error ! Please try again.", FancyToast.LENGTH_LONG, FancyToast.WARNING, true).show();
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("sales_id", Sales_ID);
                params.put("longitude", location_update.LONGITUDE);
                params.put("latitude", location_update.LATTITUDE);
                params.put("duty_status", sStatus);
                params.put("device_id", SharedPrefrence_Login.getPdevice_id());
                params.put("notification_id", SharedPrefrence_Login.getPnotification_id());
                return params;
            }
        };
        requestQueue.add(getRequest);
    }

    /* Update Duty Status*/

    private void fun_DutyONOFFScreen(final String sStatus, final String Sales_ID) {
        final RequestQueue requestQueue = VolleySingleton.getInstance(HomeActivity.this).getRequestQueue();
        StringRequest getRequest = new StringRequest(Request.Method.POST, Urls.URL_DUTY_ON_OFF_SALES,
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
                                if (strDuty_status.equals("1")) {
                                    startJob();
                                    swActiveInactiveScreen.setText("Duty ON");
                                    swActiveInactiveScreen.setChecked(true);
                                    bSwitch = false;
                                    // Toast.makeText(HomeActivity.this, "On", Toast.LENGTH_SHORT).show();
                                    SharedPrefrence_Login.saveONOFF(HomeActivity.this, "1");
                                    // notif("Duty ","ON",true);
                                } else {
                                    jobDispatcher.cancelAll();
                                    swActiveInactiveScreen.setText("Duty OFF");
                                    swActiveInactiveScreen.setChecked(false);
                                    bSwitch = true;
                                    SharedPrefrence_Login.saveONOFF(HomeActivity.this, "0");
                                    // notif("Duty ","OFF",false);
                                    // Toast.makeText(HomeActivity.this, "OFF", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                if (strDuty_status.equals("1")) {
                                    startJob();
                                    swActiveInactiveScreen.setText("Duty ON");
                                    swActiveInactiveScreen.setChecked(true);
                                    bSwitch = false;
                                    SharedPrefrence_Login.saveONOFF(HomeActivity.this, "1");
                                    // notif("Duty ","ON",true);
                                    // Toast.makeText(HomeActivity.this, "On", Toast.LENGTH_SHORT).show();
                                } else {
                                    jobDispatcher.cancelAll();
                                    swActiveInactiveScreen.setText("Duty OFF");
                                    swActiveInactiveScreen.setChecked(false);
                                    bSwitch = true;
                                    SharedPrefrence_Login.saveONOFF(HomeActivity.this, "0");
                                    // notif("Duty ","OFF",false);
                                    // Toast.makeText(HomeActivity.this, "OFF", Toast.LENGTH_SHORT).show();
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
                        if (error.getClass().equals(TimeoutError.class)) {
                            FancyToast.makeText(getApplicationContext(), "It took longer than expected to get the response from Server.", FancyToast.LENGTH_LONG, FancyToast.WARNING, true).show();
                        } else {
                            FancyToast.makeText(getApplicationContext(), "Server Respond Error ! Please try again.", FancyToast.LENGTH_LONG, FancyToast.WARNING, true).show();
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("sales_id", Sales_ID);
                params.put("longitude", location_update.LONGITUDE);
                params.put("latitude", location_update.LATTITUDE);
                params.put("duty_status", sStatus);
                params.put("device_id", SharedPrefrence_Login.getPdevice_id());
                params.put("notification_id", SharedPrefrence_Login.getPnotification_id());
                return params;
            }
        };

        requestQueue.add(getRequest);
    }


    /* API for SEND ALL DATA ON SERVER*/

    private void funSendDatatoServer(final String sPinss) {
        //  Toast.makeText(HomeActivity.this, sCity+","+sState, Toast.LENGTH_SHORT).show();
        requestQueue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        _progressDialog = ProgressDialog.show(
                HomeActivity.this, "", "Please wait...", true, false, new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        HomeActivity.this.cancel(true);
                        finish();
                    }
                }
        );
        //if everything is fine
        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SALES_REGISTRATION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            String str = obj.getString("data");
                            JSONObject jsoObject = new JSONObject(str);
                            String status = jsoObject.getString("status");
                            if (status.equals("0")) {
                                _progressDialog.dismiss();
                                CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Server Internal Error! Try Again", MessageConstant.toast_warning);
                            } else if (status.equals("1")) {
                                _progressDialog.dismiss();
                                // Log.i("PIN_CODE",sPinss);
                                String hawker_code = jsoObject.getString("hawker_code");
                                String hawker_id = jsoObject.getString("id");
                                String verification_call_status = jsoObject.getString("verification_call_status");
                                /* Pervious Process  */
                                SharedPrefrence_Seller.saveSharedPrefrencesFixerShopId(getApplicationContext(), hawker_code, hawker_id);
                                startActivity(new Intent(getApplicationContext(), RegistrationConfirmationActivity.class));
                                /* if(verification_call_status .equals("1")){
                                         SharedPrefrence_Seller.ClearSharedPrefrenc(getApplicationContext());
                                         // registrationConfirmationActivity.successDialog(SharedPrefrence_Login.getPid(),hawker_code);
                                         successDialog();
                                    }else {
                                        SharedPrefrence_Seller.saveSharedPrefrencesFixerShopId(getApplicationContext(), hawker_code, hawker_id);
                                        startActivity(new Intent(getApplicationContext(),RegistrationConfirmationActivity.class));
                                   } */
                            } else if (status.equals("2")) {
                                _progressDialog.dismiss();
                                CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "You are already registered user", MessageConstant.toast_warning);
                            } else if (status.equals("3")) {
                                _progressDialog.dismiss();
                                CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Check your gps connection", MessageConstant.toast_warning);
                            } else if (status.equals("4")) {
                                _progressDialog.dismiss();
                                CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "You are not servicable area", MessageConstant.toast_warning);
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
                        if (error.getClass().equals(TimeoutError.class)) {
                            FancyToast.makeText(getApplicationContext(), "It took longer than expected to get the response from Server.", FancyToast.LENGTH_LONG, FancyToast.WARNING, true).show();
                        } else {
                            FancyToast.makeText(getApplicationContext(), "Server Respond Error ! Please try again.", FancyToast.LENGTH_LONG, FancyToast.WARNING, true).show();
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("name", strHowkerName);
                params.put("address", strHowkerResidentialAddress);
                params.put("business_name", strHowkerBussinessName);
                params.put("shop_name", strHowkerShopName);
                params.put("business_address", strHowkerShopAdress);
                params.put("user_type", strUserType);
                params.put("business_start_time", strFromTime);
                params.put("business_close_time", strToTime);
                /*Business Mobile Number*/
                params.put("business_mobile_no", strHowkerShopeMobileNumber);
                /* Business Contact Mobile Number */
                params.put("mobile_no_contact", strHowkerContactNumber);
                params.put("cat_id", strSelectedService);
                //params.put("sub_cat_id", SharedPrefrence_Seller.getFixr_shop_Subcategoryid());
                params.put("sub_cat_id", strSubSelectedService);
                //Toast.makeText(getApplicationContext(),SharedPrefrence_Seller.getFixr_shop_Subcategoryid(),Toast.LENGTH_LONG).show();
                params.put("super_sub_cat_id", strSuperSubSelectedService);
                params.put("profile_image", str_profileImage);
                params.put("aadhar_card_image", strIndentityProofImage);
                params.put("address_proof", strAddressProofImage);
                params.put("shop_image_1", strShopImageOne);
                params.put("shop_image_2", strShopImageTwo);

                if (!TextUtils.isEmpty(strGPSID)) {
                    params.put("shop_gps_id", strGPSID);
                } else {
                    params.put("shop_gps_id", "");
                }
                params.put("shop_latitude", location_update.LATTITUDE);
                params.put("shop_longitude", location_update.LONGITUDE);
                params.put("application_type", strGPSMODE);
                params.put("sales_id", SharedPrefrence_Login.getPid());
                params.put("hawker_register_address", tvRegisteredAddress.getText().toString().trim());
                params.put("city_address", strHowkerStateCity);
                params.put("name_type", strNameType);
                /* New Parameter*/
                params.put("gender", strGender);
                params.put("hawker_sub_type", strSubHawkerName);
                params.put("seasonal_temp_hawker_type", strSubUserType);
                params.put("mobile_no_type", strMobileType);
                params.put("has_smart_phone", strSmartPhone);
                params.put("gst_no", strHowkerGSTNUMBER);
                params.put("day_of_duty", strDays);

                if (!TextUtils.isEmpty(strStartDate)) {
                    params.put("start_date", strStartDate);
                } else {
                    params.put("start_date", "");
                }
                if (!TextUtils.isEmpty(strEndDate)) {
                    params.put("end_date", strEndDate);
                } else {
                    params.put("end_date", "");
                }
                params.put("other_detail", strHowkerTypeOtherDetail);
                // params.put("city",sCity);
                params.put("city", "Maruti kunj");
                params.put("state", sState);
                params.put("other_sub_hawker_type", strOther_Sub_HawkerType);
                params.put("phone_type", strPhoneType);
                params.put("device_id", SharedPrefrence_Login.getPdevice_id());
                params.put("verification_by_call", SharedPrefrence_Seller.getHAWKERCALLSTATUS());
                params.put("profile_image_latitude", PRO_LAT);
                params.put("profile_image_longitude", PRO_LON);
                params.put("aadhar_card_image_latitude", IDENTITY_LAT);
                params.put("aadhar_card_image_longitude", IDENTITY_LON);
                params.put("address_proof_image_latitude", ADDRESS_LAT);
                params.put("address_proof_image_longitude", ADDRESS_LON);
                params.put("shop_image_1_latitude", SHOP1_LAT);
                params.put("shop_image_1_longitude", SHOP1_LON);
                params.put("shop_image_2_latitude", SHOP2_LAT);
                params.put("shop_image_2_longitude", SHOP2_LON);
                params.put("is_it_seasonal", strFixSeasonalyesno);
                /*try {
                    params.put("if_seasonal_other_business_detail",URLEncoder.encode(strOtherBusinessDetails,"UTF-8") );
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }*/
                params.put("if_seasonal_other_business_detail", strOtherBusinessDetails);
                params.put("other_service_type", SharedPrefrence_Seller.getHawkerOtherService());
                params.put("menu_image", strMenuImage);
                params.put("menu_image_latitude", MENU_LAT);
                params.put("menu_image_longitude", MENU_LON);
                params.put("aadhar_card_image_back", strIndentityProofImage2);
                params.put("menu_image_2", strMenuImage1);
                params.put("menu_image_3", strMenuImage2);
                params.put("menu_image_4", strMenuImage3);
                params.put("other_mobile_type", PHONE_TYPE);
                if (!TextUtils.isEmpty(sPinss)) {
                    params.put("sPin", sPinss);
                } else {
                    if (!TextUtils.isEmpty(sPin)) {
                        params.put("sPin", sPin);
                    } else {
                        params.put("sPin", "");
                    }

                }

                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQue(stringRequest);
    }

    /* API Log OUT By Actvity */
    private void funLogout(final String sSalesID) {
        final String device_id = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);
        final RequestQueue requestQueue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        final ProgressDialog logout_progressDialog = ProgressDialog.show(
                HomeActivity.this, "", "Please wait...", true, false,
                new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        HomeActivity.this.cancel(true);
                        finish();
                    }
                }
        );
        StringRequest getRequest = new StringRequest(Request.Method.POST, Urls.URL_SALES_LOGOUT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            String str = obj.getString("data");
                            JSONObject jsoObject = new JSONObject(str);
                            strStatus = jsoObject.getString("status");
                            if (strStatus.equals("1")) {
                                logout_progressDialog.dismiss();
                                jobDispatcher.cancelAll();
                                SharedPrefrence_Seller.ClearSharedPrefrenc(HomeActivity.this);
                                SharedPrefrence_Login.ClearSharedPrefrenc(HomeActivity.this);
                                startActivity(new Intent(getApplicationContext(), SplashActivity.class));
                                finish();
                                finishAffinity();
                            } else {
                                logout_progressDialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            logout_progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        logout_progressDialog.dismiss();
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("sales_id", sSalesID);
                params.put("latitude", location_update.LATTITUDE);
                params.put("longitude", location_update.LONGITUDE);
                params.put("device_id", device_id);
                return params;
            }
        };
        requestQueue.add(getRequest);
    }


    /* API Logout from FCm Notification*/
    public void funLogoutSSSS(Context context, final String sSalesID, final String sDeviceID) {
        this.mcontext = context;
        final Location_Update location_class = new Location_Update(mcontext);
        final RequestQueue requestQueue = VolleySingleton.getInstance(mcontext).getRequestQueue();
        StringRequest getRequest = new StringRequest(Request.Method.POST, Urls.URL_SALES_LOGOUT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            String str = obj.getString("data");
                            JSONObject jsoObject = new JSONObject(str);
                            strStatus = jsoObject.getString("status");
                            if (strStatus.equals("1")) {
                                //   jobDispatcher.cancelAll();
                                Toast.makeText(mcontext, "Logout Success", Toast.LENGTH_SHORT).show();
                                SharedPrefrence_Seller.ClearSharedPrefrenc(mcontext);
                                SharedPrefrence_Login.ClearSharedPrefrenc(mcontext);
                                SharedPrefrence_Seller.ClearTempHawkerData(mcontext);
                                Intent i = new Intent(mcontext, SplashActivity.class);
                                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                finish();
                                mcontext.startActivity(i);

                            } else {

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("sales_id", sSalesID);
                params.put("latitude", location_class.LATTITUDE);
                params.put("longitude", location_class.LONGITUDE);
                params.put("device_id", sDeviceID);
                return params;
            }
        };
        requestQueue.add(getRequest);
    }
/////////////////////////////////////////  API RESQUEST ALL END  /////////////////////////////////////////////////////////////
    /*_________________________________________________________________________________________________________________________________*/

    private class OnDismissListener implements PopupMenu.OnDismissListener {
        @Override
        public void onDismiss(PopupMenu menu) {
            // TODO Auto-generated method stub
        }
    }

    private class OnMenuItemClickListener implements PopupMenu.OnMenuItemClickListener {
        @Override
        public boolean onMenuItemClick(MenuItem item) {
            // TODO Auto-generated method stub
            switch (item.getItemId()) {
                case R.id.item_reset:
                    resetDialog();
                    return true;
                case R.id.item_logout:
                    if (ConnectionDetector.getConnectionDetector(getApplicationContext()).isConnectingToInternet() == true) {
                        logout_status = "LogOut";
                        fun_DutyONOFF("0", SharedPrefrence_Login.getPid(), "");
                    } else {
                        FancyToast.makeText(getApplicationContext(), "Check your internet connection", FancyToast.LENGTH_LONG, FancyToast.WARNING, true).show();
                    }
                    return true;
            }
            return false;
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////

    private void notif(String duty_on, String hawker, boolean b) {
        int notifyID = 1;
        String CHANNEL_ID = "my_channel_01";
        CharSequence name = getString(R.string.channel_name);// The user-visible name of the channel.
        int importance = NotificationManager.IMPORTANCE_HIGH;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //mChannel = new NotificationChannel(CHANNEL_ID, "Hawker", importance);
        }
//        Notification builder = new NotificationCompat.Builder(getApplicationContext(), CHANNEL_ID)
//                .setSmallIcon(R.mipmap.ic_launcher_round)
//                .setContentTitle(duty_on)
//                .setContentText(hawker)
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
//                .setOngoing(true).build();

        if (b == false) {
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                // mNotificationManager.createNotificationChannel(mChannel);
                //mNotificationManager.notify(notifyID, builder);
            } else {
                //mNotificationManager.notify(0, builder);

            }
            //mNotificationManager.cancelAll();
        } else {
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                //mNotificationManager.createNotificationChannel(mChannel);
                //mNotificationManager.notify(notifyID, builder);
            } else {
                //mNotificationManager.notify(0, builder);
            }
        }
    }

    private void uploadBitmap(final Bitmap bitmap) {

        VolleyMultipartRequest volleyMultipartRequest = new VolleyMultipartRequest(Request.Method.POST, URL_SALES_REGISTRATION,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        try {
                            JSONObject obj = new JSONObject(new String(response.data));
                            Toast.makeText(getApplicationContext(), "image upload", Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                        Log.e("GotError",""+error.getMessage());
                    }
                }) {


            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("menu_image", new DataPart(imagename + ".png", getFileDataFromDrawable(bitmap)));


                return params;
            }
        };

        //adding the request to volley
        Volley.newRequestQueue(this).add(volleyMultipartRequest);
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 80, byteArrayOutputStream);
        return byteArrayOutputStream.toByteArray();
    }

}