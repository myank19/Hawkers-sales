package io.goolean.tech.hawker.sales.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.multidex.MultiDex;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
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
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import io.goolean.tech.hawker.sales.Location.GetLatLngActivity;
import io.goolean.tech.hawker.sales.Location.Location_Update;
import io.goolean.tech.hawker.sales.Networking.VolleySingleton;
import io.goolean.tech.hawker.sales.R;
import io.goolean.tech.hawker.sales.Storage.SharedPrefrence_Login;
import io.goolean.tech.hawker.sales.Storage.SharedPrefrence_Seller;


public class HawkerInfo extends AppCompatActivity implements View.OnClickListener, CompoundButton.OnCheckedChangeListener ,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    private ImageView ivBack;
    private RadioButton rbMoving, rbFix, rbHybrid, rbSeasonal, rbTemporary, rbMale, rbFemale,rbSubMoving,rbSubFix, rbSeasonalYes, rbSeasonalNo;
    private Spinner spHawkerSubType;
    private EditText edt_hawker_name_id,edt_MobileNumber_id,edt_mobile_model_type_id,edt_other_business_detail_id;
    private Button btn_Save,btnStartDate,btnEndDate;
    private String strHawkerType = "Fix",strSubHawkerType= "",sGender = "Male",strSubHawkerName,iSubHawkerNamePosition,hawker_code,id,strFromTime = "",
            strToTime = "",strDays,strSelectedService,strSubSelectedService,strSuperSubSelectedService,strtemp,strHawker_type_Code = "12",
            strFixSeasonalyesno = "";
    private RequestQueue requestQueue;
    private ProgressDialog _progressDialog;
    private String[] sHawkerName;
    private Location_Update location_update;
    private LocationManager locationManager;
    private static String sState, sCity,sLatitude,sLongitude,sAddress;
    private Validation validation;
    private RelativeLayout rlSelectService;
    private LinearLayout llDateSelction,llFixLayout,llBottomLayout;
    Multimap<String,Object> myMultimap ;

    Date currentDate ,selectedDateFrom;
    SimpleDateFormat dateFormat ,dateFormat_two;
    String finalStringDate,strStartDate,strEndDate;
    private DatePickerDialog dialogfrom = null;
    private DatePickerDialog dialogEnd = null;
    Calendar calendar;
    int currentmonth ;
    String currentDateString;
    private String strHawkerOtherBisinessDetail;
    private LinearLayout llSelectService_Info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hawker_info);
        getSupportActionBar().hide();
        MultiDex.install(this);
        if (savedInstanceState == null) {
            locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
            location_update = new Location_Update(HawkerInfo.this);
            validation = new Validation();
            SharedPrefrence_Login.getPhoneType(HawkerInfo.this);
            SharedPrefrence_Login.getDataLogin(HawkerInfo.this);
            SharedPrefrence_Seller.getTempHowkerRegistration(HawkerInfo.this);
            SharedPrefrence_Seller.getHawkerOtherServiceData(HawkerInfo.this);
            if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                initID();
                DataRestore();
            }else{
                turnGPSOn();
            }
        }

    }


    @Override
    protected void onSaveInstanceState(Bundle outState ) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }


    private void initID() {
        ivBack = findViewById(R.id.ivBack);
        rbMoving = findViewById(R.id.rbMoving);
        rbFix = findViewById(R.id.rbFix);
        rbHybrid = findViewById(R.id.rbHybrid);
        rbSeasonal = findViewById(R.id.rbSeasonal);
        rbTemporary = findViewById(R.id.rbTemporary);
        rbMale = findViewById(R.id.rbMale);
        rbFemale = findViewById(R.id.rbFemale);
        spHawkerSubType = findViewById(R.id.spHawkerSubType);
        edt_hawker_name_id = findViewById(R.id.edt_hawker_name_id);
        edt_MobileNumber_id = findViewById(R.id.edt_MobileNumber_id);
        llDateSelction = findViewById(R.id.llDateSelction);
        rlSelectService = findViewById(R.id.rlSelectService);
        btnStartDate = findViewById(R.id.btnStartDate);
        btnEndDate = findViewById(R.id.btnEndDate);
        btn_Save = findViewById(R.id.btn_Save);
        rbSubMoving = findViewById(R.id.rbSubMoving);
        rbSubFix = findViewById(R.id.rbSubFix);
        llFixLayout = findViewById(R.id.llFixLayout);
        llBottomLayout = findViewById(R.id.llBottomLayout);
        rbSeasonalYes = findViewById(R.id.rbSeasonalYes);
        rbSeasonalNo = findViewById(R.id.rbSeasonalNo);
        edt_mobile_model_type_id = findViewById(R.id.edt_mobile_model_type_id);
        edt_other_business_detail_id = findViewById(R.id.edt_other_business_detail_id);
        llSelectService_Info = findViewById(R.id.llSelectService_Info);


        rbFix.setChecked(true);
        ivBack.setOnClickListener(this);
        rbFix.setOnCheckedChangeListener(this);
        rbMoving.setOnCheckedChangeListener(this);
        rbHybrid.setOnCheckedChangeListener(this);
        rbSeasonal.setOnCheckedChangeListener(this);
        rbTemporary.setOnCheckedChangeListener(this);
        rbFemale.setOnCheckedChangeListener(this);
        rbSubMoving.setOnCheckedChangeListener(this);
        rbSubFix.setOnCheckedChangeListener(this);
        rlSelectService.setOnClickListener(this);
        rbMale.setOnCheckedChangeListener(this);
        rbSeasonalYes.setOnCheckedChangeListener(this);
        rbSeasonalNo.setOnCheckedChangeListener(this);
        btnStartDate.setOnClickListener(this);
        btnEndDate.setOnClickListener(this);
        btn_Save.setOnClickListener(this);
        llDateSelction.setVisibility(View.GONE);
        new GetLatLngActivity(HawkerInfo.this);
        funDateIntialize();
        if(ConnectionDetector.getConnectionDetector(getApplicationContext()).isConnectingToInternet()==true){
            bindSubHawkerType("11");
        }else {
            FancyToast.makeText(getApplicationContext(),"Check your internet connection",FancyToast.LENGTH_LONG,FancyToast.WARNING,true).show();
        }

        spHawkerSubType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                strSubHawkerName = sHawkerName[i];
                iSubHawkerNamePosition = i+"";
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        if(SharedPrefrence_Login.getsPhoneType().equals("Jio")){
            llSelectService_Info.setVisibility(View.VISIBLE);
            edt_mobile_model_type_id.setVisibility(View.GONE);
            // Toast.makeText(this, "Jio", Toast.LENGTH_SHORT).show();
        }else if(SharedPrefrence_Login.getsPhoneType().equals("Other")){
            llSelectService_Info.setVisibility(View.GONE);
            edt_mobile_model_type_id.setVisibility(View.VISIBLE);
            // Toast.makeText(this, "Other", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        int id = compoundButton.getId();
        if(id == R.id.rbMoving){
            if(b ==true){
                rbMoving.setChecked(true);
                rbFix.setChecked(false);
                rbHybrid.setChecked(false);
                rbTemporary.setChecked(false);
                rbSeasonal.setChecked(false);
                strHawkerType = "Moving";
                strSubHawkerType= "";
                strHawker_type_Code = "11";
                SubHawkerType(strHawker_type_Code);
                llDateSelction.setVisibility(View.GONE);
                llFixLayout.setVisibility(View.GONE);
                llBottomLayout.setVisibility(View.GONE);
            }

        }else if(id == R.id.rbFix){
            if(b ==true){
                rbFix.setChecked(true);
                rbMoving.setChecked(false);
                rbHybrid.setChecked(false);
                rbTemporary.setChecked(false);
                rbSeasonal.setChecked(false);
                strHawkerType = "Fix";
                strSubHawkerType= "";
                strFixSeasonalyesno = "YES";
                llDateSelction.setVisibility(View.GONE);
                edt_other_business_detail_id.setVisibility(View.VISIBLE);
                llBottomLayout.setVisibility(View.GONE);
                strHawker_type_Code = "12";
                SubHawkerType(strHawker_type_Code);
                llFixLayout.setVisibility(View.VISIBLE);
            }
        }else if(id == R.id.rbHybrid){
            if(b ==true){
                rbHybrid.setChecked(true);
                rbMoving.setChecked(false);
                rbFix.setChecked(false);
                rbTemporary.setChecked(false);
                rbSeasonal.setChecked(false);
                strHawkerType = "Hybrid";
                strSubHawkerType= "";
                strHawker_type_Code = "13";
                SubHawkerType(strHawker_type_Code);
                llDateSelction.setVisibility(View.GONE);
                llFixLayout.setVisibility(View.GONE);
                llBottomLayout.setVisibility(View.GONE);
            }

        }else if(id == R.id.rbTemporary){
            if(b ==true){
                rbSeasonal.setChecked(false);
                rbMoving.setChecked(false);
                rbFix.setChecked(false);
                rbHybrid.setChecked(false);
                rbTemporary.setChecked(true);
                strHawkerType = "Seasonal";
                strSubHawkerType= "Moving";
                strHawker_type_Code = "14";
                SubHawkerType(strHawker_type_Code);
                llDateSelction.setVisibility(View.VISIBLE);
                llBottomLayout.setVisibility(View.VISIBLE);
                llFixLayout.setVisibility(View.GONE);
            }

        }else if(id == R.id.rbSeasonal){
            if(b ==true){
                rbTemporary.setChecked(false);
                rbMoving.setChecked(false);
                rbFix.setChecked(false);
                rbHybrid.setChecked(false);
                rbSeasonal.setChecked(true);
                strHawkerType = "Temporary";
                strSubHawkerType= "Moving";
                strHawker_type_Code = "15";
                SubHawkerType(strHawker_type_Code);
                llDateSelction.setVisibility(View.VISIBLE);
                llBottomLayout.setVisibility(View.VISIBLE);
                llFixLayout.setVisibility(View.GONE);
            }

        }else if(id == R.id.rbSeasonalYes){
            if(b ==true){
                strFixSeasonalyesno = "YES";
                llDateSelction.setVisibility(View.GONE);
                edt_other_business_detail_id.setVisibility(View.VISIBLE);
                llBottomLayout.setVisibility(View.GONE);
            }
        }else if(id == R.id.rbSeasonalNo){
            if(b ==true){
                strFixSeasonalyesno = "NO";
                btnStartDate.setText("");
                btnEndDate.setText("");
                llDateSelction.setVisibility(View.GONE);
                edt_other_business_detail_id.setVisibility(View.GONE);
            }
        }else if(id == R.id.rbMale){
            if(b ==true) {
                sGender = "Male";
            }
        }else if(id == R.id.rbFemale){
            if(b ==true) {
                sGender = "Female";
            }
        }else if(id == R.id.rbSubMoving){
            if(b ==true) {
                strSubHawkerType= "Moving";
            }
        }else if(id == R.id.rbSubFix){
            if(b ==true) {
                strSubHawkerType= "Fix";
            }
        }

    }


    @Override
    public void onClick(View view) {
        int id = view.getId();

        if(id == R.id.ivBack){
            finish();
        }else if(id == R.id.rlSelectService){
            storeData();
            startActivityForResult(new Intent(getApplicationContext(),ServiceSelctionActivity.class),22);
        }else if(id == R.id.btnStartDate){
            storeData();
            funStartDate(view);
        }else if(id == R.id.btnEndDate){
            storeData();
            funEndDate(view);
        } else if(id == R.id.btn_Save){
            storeData();
            if(SharedPrefrence_Login.getsPhoneType().equals("Jio")){
                sendDataJio();
            }else if(SharedPrefrence_Login.getsPhoneType().equals("Other")){
                sendDataOther();
            }


        }
    }

    private void sendDataOther() {
        strStartDate = btnStartDate.getText().toString().trim();
        strEndDate = btnEndDate.getText().toString().trim();
        if(ConnectionDetector.getConnectionDetector(getApplicationContext()).isConnectingToInternet()==true){

                if(strHawkerType.equals("Temporary")) {
                    if (strStartDate.equals("")) {
                        CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Select Start Date", MessageConstant.toast_warning);
                    } else if (strEndDate.equals("")) {
                        CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Select End Date", MessageConstant.toast_warning);
                    } else {
                        if(validation.checkEditText(getApplicationContext(), edt_hawker_name_id, "Enter Hawker Name") == true &&
                                validation.checkNumber(getApplicationContext(), edt_MobileNumber_id, "Enter Mobile Number") == true &&
                                validation.checkEditText(getApplicationContext(),edt_mobile_model_type_id,"Enter Mobile Model Type") == true){
                            sendDataonServer();
                        }

                    }
                }else if(strHawkerType.equals("Seasonal")) {
                    if (strStartDate.equals("")) {
                        CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Select Start Date", MessageConstant.toast_warning);
                    } else if (strEndDate.equals("")) {
                        CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Select End Date", MessageConstant.toast_warning);
                    } else {
                        if(validation.checkEditText(getApplicationContext(), edt_hawker_name_id, "Enter Hawker Name") == true &&
                                validation.checkNumber(getApplicationContext(), edt_MobileNumber_id, "Enter Mobile Number") == true&&
                                validation.checkEditText(getApplicationContext(),edt_mobile_model_type_id,"Enter Mobile Model Type")== true){
                            sendDataonServer();
                        }
                    }
                }else if(strHawkerType.equals("Fix")){
                    if(strFixSeasonalyesno.equals("YES")){
                        if(edt_other_business_detail_id.getText().toString().trim().equals("")){
                            CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Enter other business detail when season off.", MessageConstant.toast_warning);
                        }else {
                            if(validation.checkEditText(getApplicationContext(), edt_hawker_name_id, "Enter Hawker Name") == true &&
                                    validation.checkNumber(getApplicationContext(), edt_MobileNumber_id, "Enter Mobile Number") == true&&
                                    validation.checkEditText(getApplicationContext(),edt_mobile_model_type_id,"Enter Mobile Model Type") == true){
                                sendDataonServer();
                            }
                        }
                    }else {
                        if(validation.checkEditText(getApplicationContext(), edt_hawker_name_id, "Enter Hawker Name") == true &&
                                validation.checkNumber(getApplicationContext(), edt_MobileNumber_id, "Enter Mobile Number") == true&&
                                validation.checkEditText(getApplicationContext(),edt_mobile_model_type_id,"Enter Mobile Model Type") == true){
                            sendDataonServer();
                        }
                    }
                }else {
                    if(validation.checkEditText(getApplicationContext(), edt_hawker_name_id, "Enter Hawker Name") == true &&
                            validation.checkNumber(getApplicationContext(), edt_MobileNumber_id, "Enter Mobile Number") == true&&
                            validation.checkEditText(getApplicationContext(),edt_mobile_model_type_id,"Enter Mobile Model Type") == true){
                        sendDataonServer();
                    }
               //     sendDataonServer();
                }
        }else {
            FancyToast.makeText(getApplicationContext(),"Check your internet connection",FancyToast.LENGTH_LONG,FancyToast.WARNING,true).show();
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void sendDataJio() {
        strStartDate = btnStartDate.getText().toString().trim();
        strEndDate = btnEndDate.getText().toString().trim();
        if(ConnectionDetector.getConnectionDetector(getApplicationContext()).isConnectingToInternet()==true){

            if(strHawkerType.equals("Temporary")) {
                if (strStartDate.equals("")) {
                    CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Select Start Date", MessageConstant.toast_warning);
                } else if (strEndDate.equals("")) {
                    CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Select End Date", MessageConstant.toast_warning);
                } else {
                    if(validation.checkEditText(getApplicationContext(), edt_hawker_name_id, "Enter Hawker Name") == true &&
                            validation.checkNumber(getApplicationContext(), edt_MobileNumber_id, "Enter Mobile Number") == true){
                        sendDataonServer();
                    }

                }
            }else if(strHawkerType.equals("Seasonal")) {
                if (strStartDate.equals("")) {
                    CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Select Start Date", MessageConstant.toast_warning);
                } else if (strEndDate.equals("")) {
                    CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Select End Date", MessageConstant.toast_warning);
                } else {
                    if(validation.checkEditText(getApplicationContext(), edt_hawker_name_id, "Enter Hawker Name") == true &&
                            validation.checkNumber(getApplicationContext(), edt_MobileNumber_id, "Enter Mobile Number") == true){
                        sendDataonServer();
                    }
                }
            }else if(strHawkerType.equals("Fix")){
                if(strFixSeasonalyesno.equals("YES")){
                    if(edt_other_business_detail_id.getText().toString().trim().equals("")){
                        CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Enter other business detail when season off.", MessageConstant.toast_warning);
                    }else {
                        if(validation.checkEditText(getApplicationContext(), edt_hawker_name_id, "Enter Hawker Name") == true &&
                                validation.checkNumber(getApplicationContext(), edt_MobileNumber_id, "Enter Mobile Number") == true){
                            sendDataonServer();
                        }
                    }
                }else {
                    if(validation.checkEditText(getApplicationContext(), edt_hawker_name_id, "Enter Hawker Name") == true &&
                            validation.checkNumber(getApplicationContext(), edt_MobileNumber_id, "Enter Mobile Number") == true){
                        sendDataonServer();
                    }
                }
            }else {
                if(validation.checkEditText(getApplicationContext(), edt_hawker_name_id, "Enter Hawker Name") == true &&
                        validation.checkNumber(getApplicationContext(), edt_MobileNumber_id, "Enter Mobile Number") == true){
                    sendDataonServer();
                }
                //     sendDataonServer();
            }
        }else {
            FancyToast.makeText(getApplicationContext(),"Check your internet connection",FancyToast.LENGTH_LONG,FancyToast.WARNING,true).show();
        }
    }

    /*else if(strStartDate.equals("")){
                        CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Select Start Date", MessageConstant.toast_warning);
                    }else if(strEndDate.equals("")){
                        CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Select End Date", MessageConstant.toast_warning);
                    }*/

    private void sendDataonServer() {
        if(SharedPrefrence_Login.getsPhoneType().equals("Jio")){
            if(!SharedPrefrence_Seller.getHawkerOtherService().equals("")){
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                    storeData();;
                    funSendDatatoServer(SharedPrefrence_Login.getPid(),edt_hawker_name_id.getText().toString(),edt_MobileNumber_id.getText().toString(),
                            "Jio");
                }else{
                    turnGPSOn();
                }
            }else {
                if(strSelectedService == null){
                    CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Select Service", MessageConstant.toast_warning);
                }else if(strSelectedService.equals("[]")){
                    CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Select Service", MessageConstant.toast_warning);
                }else if(strSelectedService.equals("")){
                    CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Select Service", MessageConstant.toast_warning);
                }else {
                    if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                        storeData();;
                        funSendDatatoServer(SharedPrefrence_Login.getPid(),edt_hawker_name_id.getText().toString(),edt_MobileNumber_id.getText().toString(),
                                "Jio");
                    }else{
                        turnGPSOn();
                    }
                }
            }
        }else if(SharedPrefrence_Login.getsPhoneType().equals("Other")){
            if(!SharedPrefrence_Seller.getHawkerOtherService().equals("")){
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                    storeData();;
                    funSendDatatoServer(SharedPrefrence_Login.getPid(),edt_hawker_name_id.getText().toString(),edt_MobileNumber_id.getText().toString(),
                            edt_mobile_model_type_id.getText().toString().trim());
                }else{
                    turnGPSOn();
                }
            }else {
                if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                    storeData();
                    funSendDatatoServer(SharedPrefrence_Login.getPid(),edt_hawker_name_id.getText().toString(),edt_MobileNumber_id.getText().toString(),
                            edt_mobile_model_type_id.getText().toString().trim());
                }else{
                    turnGPSOn();
                }
            }
        }
    }

    private void storeData() {
        strStartDate = btnStartDate.getText().toString().trim();
        strEndDate = btnEndDate.getText().toString().trim();
        strHawkerOtherBisinessDetail = edt_other_business_detail_id.getText().toString().trim();
        SharedPrefrence_Seller.saveTempHowkerRegistration(HawkerInfo.this,SharedPrefrence_Login.getPid(),edt_hawker_name_id.getText().toString(),sGender
                ,strHawkerType,strSubHawkerName,sAddress,edt_MobileNumber_id.getText().toString(),sCity,sState,sLatitude,sLongitude,iSubHawkerNamePosition,
                strSubHawkerType,strStartDate,strEndDate,strHawker_type_Code,"Temp",edt_mobile_model_type_id.getText().toString().trim(),
                strHawkerOtherBisinessDetail,"Temp",strFixSeasonalyesno);
    }


    private void DataRestore() {
        edt_hawker_name_id.setText(SharedPrefrence_Seller.getTempHowkerName());
        edt_MobileNumber_id.setText(SharedPrefrence_Seller.getTempHawkerMobileNumber());
        edt_mobile_model_type_id.setText(SharedPrefrence_Seller.getTempHawkerMobilemodelType());

        if(SharedPrefrence_Seller.getTempHowkerType().equals("Fix")){
            rbFix.setChecked(true);
            new GetLatLngActivity(getApplicationContext());
            strHawkerType = "Fix";
            if(SharedPrefrence_Seller.getTempFixSeasonalyesno().equals("YES")){
                /*llDateSelction.setVisibility(View.GONE);
                edt_other_business_detail_id.setVisibility(View.VISIBLE);
                edt_other_business_detail_id.setText(SharedPrefrence_Seller.getTempHawkerOtherBisinessDetail());
                llBottomLayout.setVisibility(View.GONE);*/
                rbFix.setChecked(true);
                rbMoving.setChecked(false);
                rbHybrid.setChecked(false);
                rbTemporary.setChecked(false);
                rbSeasonal.setChecked(false);
                strHawkerType = "Fix";
                strSubHawkerType= "";
                strFixSeasonalyesno = "YES";
                llDateSelction.setVisibility(View.GONE);
                edt_other_business_detail_id.setVisibility(View.VISIBLE);
                llBottomLayout.setVisibility(View.GONE);
                strHawker_type_Code = "12";
                edt_other_business_detail_id.setText(SharedPrefrence_Seller.getTempHawkerOtherBisinessDetail());
                SubHawkerType(strHawker_type_Code);
                llFixLayout.setVisibility(View.VISIBLE);

            }else if(SharedPrefrence_Seller.getTempFixSeasonalyesno().equals("NO")){
                btnStartDate.setText("");
                btnEndDate.setText("");
                edt_other_business_detail_id.setText("");
                llDateSelction.setVisibility(View.GONE);
                edt_other_business_detail_id.setVisibility(View.GONE);
            }else {
                rbFix.setChecked(true);
                rbMoving.setChecked(false);
                rbHybrid.setChecked(false);
                rbTemporary.setChecked(false);
                rbSeasonal.setChecked(false);
                strHawkerType = "Fix";
                strSubHawkerType= "";
                strFixSeasonalyesno = "YES";
                llDateSelction.setVisibility(View.GONE);
                edt_other_business_detail_id.setVisibility(View.VISIBLE);
                llBottomLayout.setVisibility(View.GONE);
                strHawker_type_Code = "12";
                edt_other_business_detail_id.setText(SharedPrefrence_Seller.getTempHawkerOtherBisinessDetail());
                SubHawkerType(strHawker_type_Code);
                llFixLayout.setVisibility(View.VISIBLE);
            }
            if(!SharedPrefrence_Seller.getTempHawkerSubTypePosition().equals("")){
                spHawkerSubType.setSelection(Integer.parseInt(SharedPrefrence_Seller.getTempHawkerSubTypePosition()));
            }
        }/*else if(SharedPrefrence_Seller.getTempHowkerType().equals("Hybrid")){
            rbHybrid.setChecked(true);
            strHawkerType = "Hybrid";
            llDateSelction.setVisibility(View.GONE);
            if(!SharedPrefrence_Seller.getTempHawkerSubTypePosition().equals("")){
                spHawkerSubType.setSelection(Integer.parseInt(SharedPrefrence_Seller.getTempHawkerSubTypePosition()));
            }
        }else if(SharedPrefrence_Seller.getTempHowkerType().equals("Moving")){
            rbMoving.setChecked(true);
            strHawkerType = "Moving";
            llDateSelction.setVisibility(View.GONE);
            if(!SharedPrefrence_Seller.getTempHawkerSubTypePosition().equals("")){
                spHawkerSubType.setSelection(Integer.parseInt(SharedPrefrence_Seller.getTempHawkerSubTypePosition()));
            }
        }*/else if(SharedPrefrence_Seller.getTempHowkerType().equals("Seasonal")){
            rbSeasonal.setChecked(true);
            strHawkerType = "Seasonal";
            llDateSelction.setVisibility(View.VISIBLE);
            if(!SharedPrefrence_Seller.getTempHawkerSubTypePosition().equals("")){
                spHawkerSubType.setSelection(Integer.parseInt(SharedPrefrence_Seller.getTempHawkerSubTypePosition()));
            }
        }else if(SharedPrefrence_Seller.getTempHowkerType().equals("Temporary")){
            rbTemporary.setChecked(true);
            strHawkerType = "Temporary";
            llDateSelction.setVisibility(View.VISIBLE);
            if(!SharedPrefrence_Seller.getTempHawkerSubTypePosition().equals("")){
                spHawkerSubType.setSelection(Integer.parseInt(SharedPrefrence_Seller.getTempHawkerSubTypePosition()));
            }
        }else {
            rbFix.setChecked(true);
            new GetLatLngActivity(getApplicationContext());
            strHawkerType = "Fix";
            if(SharedPrefrence_Seller.getHawkerFixSeasonalYesNo().equals("YES")){
                llDateSelction.setVisibility(View.GONE);
                edt_other_business_detail_id.setVisibility(View.VISIBLE);
                edt_other_business_detail_id.setText(SharedPrefrence_Seller.getTempHawkerOtherBisinessDetail());
                llBottomLayout.setVisibility(View.GONE);
            }else if(SharedPrefrence_Seller.getHawkerFixSeasonalYesNo().equals("NO")){
                btnStartDate.setText("");
                btnEndDate.setText("");
                edt_other_business_detail_id.setText("");
                llDateSelction.setVisibility(View.GONE);
                edt_other_business_detail_id.setVisibility(View.GONE);
            }else {
                /*llDateSelction.setVisibility(View.GONE);
                edt_other_business_detail_id.setVisibility(View.VISIBLE);
                edt_other_business_detail_id.setText(SharedPrefrence_Seller.getTempHawkerOtherBisinessDetail());
                llBottomLayout.setVisibility(View.GONE);*/
                rbFix.setChecked(true);
                rbMoving.setChecked(false);
                rbHybrid.setChecked(false);
                rbTemporary.setChecked(false);
                rbSeasonal.setChecked(false);
                strHawkerType = "Fix";
                strSubHawkerType= "";
                strFixSeasonalyesno = "YES";
                llDateSelction.setVisibility(View.GONE);
                edt_other_business_detail_id.setVisibility(View.VISIBLE);
                llBottomLayout.setVisibility(View.GONE);
                strHawker_type_Code = "12";
                edt_other_business_detail_id.setText(SharedPrefrence_Seller.getTempHawkerOtherBisinessDetail());
                SubHawkerType(strHawker_type_Code);
                llFixLayout.setVisibility(View.VISIBLE);

            }
            if(!SharedPrefrence_Seller.getTempHawkerSubTypePosition().equals("")){
                spHawkerSubType.setSelection(Integer.parseInt(SharedPrefrence_Seller.getTempHawkerSubTypePosition()));
            }

           /* rbMoving.setChecked(true);
            strHawkerType = "Moving";
            llDateSelction.setVisibility(View.GONE);
            SubHawkerType("11");
            if(!SharedPrefrence_Seller.getTempHawkerSubTypePosition().equals("")){
                spHawkerSubType.setSelection(Integer.parseInt(SharedPrefrence_Seller.getTempHawkerSubTypePosition()));
            }*/
        }


    }

    private void funDateIntialize() {
        dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        dateFormat_two = new SimpleDateFormat("dd/MMM/yyyy");
        calendar = Calendar.getInstance();
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        currentmonth = calendar.get(Calendar.MONTH);
        int year = calendar.get(Calendar.YEAR);
        currentmonth= currentmonth+1;
        currentDateString = day+"/"+currentmonth+"/"+year;
        finalStringDate = currentDateString;
        try {
            currentDate = dateFormat.parse(currentDateString);
            finalStringDate = dateFormat_two.format(currentDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // btnStartDate.setText(finalStringDate);
        // btnEndDate.setText(finalStringDate);
    }

    private void funStartDate(View v) {
        if (dialogfrom == null)
            dialogfrom = new DatePickerDialog(v.getContext(), new PickDate(), calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1,
                    calendar.get(Calendar.DAY_OF_MONTH));
        dialogfrom.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialogfrom.getDatePicker().setMinDate(System.currentTimeMillis());
        dialogfrom.show();

    }

    private void funEndDate(View v) {
        if (dialogEnd == null)
            dialogEnd = new DatePickerDialog(v.getContext(), new PickEndDate(), calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH) + 1,
                    calendar.get(Calendar.DAY_OF_MONTH));
        dialogEnd.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        dialogEnd.getDatePicker().setMinDate(System.currentTimeMillis());
        dialogEnd.show();
    }

    /*Date Picker*/

    private class PickDate implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            monthOfYear=monthOfYear+1;
            String   dateString = dayOfMonth + "/" + monthOfYear + "/" + year ;
            try {
                selectedDateFrom = dateFormat.parse(dateString);

                    if (selectedDateFrom.compareTo(currentDate) >= 0) {
                        finalStringDate = dateString;
                        /*  dateString = 10/8/2019  */
                        finalStringDate = dateFormat_two.format(selectedDateFrom);
                        btnStartDate.setText(finalStringDate);
                    } else {
                        FancyToast.makeText(getApplicationContext(),"Selected date can not be greater than current date",FancyToast.LENGTH_LONG,FancyToast.WARNING,true).show();

                    }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private class PickEndDate implements DatePickerDialog.OnDateSetListener {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            monthOfYear=monthOfYear+1;
            String   dateString = dayOfMonth + "/" + monthOfYear + "/" + year ;
            try {
                selectedDateFrom = dateFormat.parse(dateString);

                    if (selectedDateFrom.compareTo(currentDate) >= 0) {
                        finalStringDate = dateString;
                        /*dateString = 10/8/2019*/
                        finalStringDate = dateFormat_two.format(selectedDateFrom);
                        btnEndDate.setText(finalStringDate);
                    } else {
                        FancyToast.makeText(getApplicationContext(),"Selected date can not be greater than current date",FancyToast.LENGTH_LONG,FancyToast.WARNING,true).show();

                    }
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    private void SubHawkerType(final String s) {
        if(ConnectionDetector.getConnectionDetector(getApplicationContext()).isConnectingToInternet()==true){
            bindSubHawkerType(s);
        }else {
            FancyToast.makeText(getApplicationContext(),"Check your internet connection",FancyToast.LENGTH_LONG,FancyToast.WARNING,true).show();
        }
    }


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void cancel(boolean b) {
    }
    private void bindSubHawkerType(final String hawker_type_code) {
        if(ConnectionDetector.getConnectionDetector(getApplicationContext()).isConnectingToInternet()==true){
            requestQueue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.URL_HAWKER_TYPE_DATA,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            if (response.length() != 0) {

                                parseHawkerTypeData(response);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error.getClass().equals(TimeoutError.class)) {
                                FancyToast.makeText(getApplicationContext(),"It took longer than expected to get the response from Server.",FancyToast.LENGTH_LONG,FancyToast.WARNING,true).show();
                            }else {
                                FancyToast.makeText(getApplicationContext(),"Server Respond Error ! Please try again.",FancyToast.LENGTH_LONG,FancyToast.WARNING,true).show();
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
        }else {
            FancyToast.makeText(getApplicationContext(),"Check your internet connection",FancyToast.LENGTH_LONG,FancyToast.WARNING,true).show();
            CommonDialog.Show_Internt_Dialog(HawkerInfo.this,"");
        }
    }

    private void parseHawkerTypeData(String response) {
        try {
            JSONObject obj = new JSONObject(response);
            String strStatau=  obj.getString("status");
            if(strStatau.equals("1")){
                String str=  obj.getString("data");
                JSONArray jsonArray = new JSONArray(str);
                sHawkerName = new String[jsonArray.length()];
                for (int i =0; i<jsonArray.length();i++) {
                    JSONObject sub_objects = jsonArray.getJSONObject(i);
                    sHawkerName[i] = sub_objects.getString("hawker__sub_type_name").toString();
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(HawkerInfo.this,R.layout.support_simple_spinner_dropdown_item,sHawkerName);
                spHawkerSubType.setAdapter(adapter);
                String ss = SharedPrefrence_Seller.getTempHawkerSubTypePosition();
                if(!ss.equals("")){
                    spHawkerSubType.setSelection(Integer.parseInt(ss));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void funSendDatatoServer(final String sId, final String name, final String number,final String modeltype) {

        requestQueue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        _progressDialog = ProgressDialog.show(
                HawkerInfo.this, "", "Please wait...", true, false, new DialogInterface.OnCancelListener(){
                    @Override
                    public void onCancel(DialogInterface dialog) { HawkerInfo.this.cancel(true);finish();
                    }
                }
        );

        //if everything is fine
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.URL_HAWKER_REGISTRATION_TEMP,
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
                                hawker_code= jsoObject.getString("hawker_code");
                                id = jsoObject.getString("id");
                                SharedPrefrence_Seller.ClearTempHawkerData(HawkerInfo.this);
                                successDialog();

                                /* String verification_call_status  = jsoObject.getString("verification_call_status");
                                if(verification_call_status .equals("1")){
                                    SharedPrefrence_Seller.ClearTempHawkerData(HawkerInfo.this);
                                    successDialog();
                                }else {
                                    SharedPrefrence_Seller.saveSharedPrefrencesFixerShopId(getApplicationContext(), hawker_code, id);
                                    startActivity(new Intent(getApplicationContext(),RegistrationConfirmationActivity.class));
                                }*/
                            } else if (status.equals("2")) {
                                _progressDialog.dismiss();
                                CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "You are already registered user", MessageConstant.toast_warning);
                            }else if (status.equals("3")){
                                _progressDialog.dismiss();
                                CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "You are already registered user", MessageConstant.toast_warning);
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
                            FancyToast.makeText(getApplicationContext(),"It took longer than expected to get the response from Server.",FancyToast.LENGTH_LONG,FancyToast.WARNING,true).show();
                        }else {
                            FancyToast.makeText(getApplicationContext(),"Server Respond Error ! Please try again.",FancyToast.LENGTH_LONG,FancyToast.WARNING,true).show();
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("sales_id", sId);
                params.put("name",name);

               /* try {
                    params.put("name",URLEncoder.encode(name,"UTF-8") );
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }*/
                params.put("gender", sGender);
                params.put("hawker_type",strHawkerType);
                params.put("sub_hawker_type",strSubHawkerName);
                params.put("address",sAddress);
                params.put("mobile_no", number);
                params.put("city", sCity);
                params.put("state", sState);
                params.put("latitude", sLatitude);
                params.put("longitude", sLongitude);
                if(strSelectedService == null){
                    params.put("cat_id", "");
                    params.put("sub_cat_id", "");
                    params.put("super_sub_cat_id", "");
                }else {
                    params.put("cat_id", strSelectedService);
                    params.put("sub_cat_id", strSubSelectedService);
                    params.put("super_sub_cat_id", strSuperSubSelectedService);
                }
                params.put("seasonal_temp_hawker_type",strSubHawkerType);
                if(strStartDate == null){
                    params.put("start_date","");
                }else {
                    params.put("start_date",strStartDate);
                }
                if(strEndDate == null){
                    params.put("end_date","");
                }else {
                    params.put("end_date",strEndDate);
                }
                params.put("is_it_seasonal",strFixSeasonalyesno);
                params.put("model_type",modeltype );
                params.put("if_seasonal_other_business_detail", strHawkerOtherBisinessDetail);

               /* try {
                    params.put("model_type",URLEncoder.encode(modeltype,"UTF-8") );
                    params.put("if_seasonal_other_business_detail", URLEncoder.encode(strHawkerOtherBisinessDetail,"UTF-8") );
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }*/
                params.put("other_service_type",SharedPrefrence_Seller.getHawkerOtherService());
                params.put("business_start_time", strFromTime);
                params.put("business_close_time", strToTime);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQue(stringRequest);

    }

    private void successDialog() {
        final Dialog successDialog = new Dialog(HawkerInfo.this);
        successDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        successDialog.setContentView(R.layout.dialog_success_layout);
        successDialog.setCanceledOnTouchOutside(false);
        successDialog.setCancelable(false);
        successDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        successDialog.findViewById(R.id.close_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                successDialog.dismiss();
                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                finish();
                finishAffinity();
            }
        });
        successDialog.show();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    public void funLatLng(Context context, String state, String city, String sector, String feature, String pin,
                          double curr_lati, double curr_long, String full_address) {
        sState = state;
        sCity = city;
        sLatitude =curr_lati+"";
        sLongitude=curr_long+"";
        sAddress = full_address;
    }

    private void turnGPSOn(){
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
                                status.startResolutionForResult(HawkerInfo.this, 1000);
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == 22){
            DataRestore();
            myMultimap = ArrayListMultimap.create();
            strFromTime=data.getStringExtra("FROMTIME");
            strToTime=data.getStringExtra("TOTIME");
            strDays = data.getStringExtra("DAYS");
            strDays = spiltString(strDays);
            SharedPrefrence_Seller.saveWorkingDays(HawkerInfo.this,strDays,strFromTime,strToTime);
            strSelectedService = SharedPrefrence_Seller.getFixr_shop_categoryid().replace("[","").replace("]","");
            strSelectedService = strSelectedService.replaceAll("\\s+","");
            strSubSelectedService = SharedPrefrence_Seller.getFixr_shop_Subcategoryid();
            strSubSelectedService = spiltString(strSubSelectedService);
            strSuperSubSelectedService = SharedPrefrence_Seller.getFixr_shop_SuperSubcategoryid();
            strSuperSubSelectedService = spiltString(strSuperSubSelectedService);
        }
    }

    private String spiltString(String strSubSelectedService) {
        Matcher matcher = Pattern.compile("\\[([^\\]]+)").matcher(strSubSelectedService);
        List<String> tags = new ArrayList<>();
        int pos = -1;
        while (matcher.find(pos+1)){
            pos = matcher.start();
            tags.add(matcher.group(1));
        }
        strtemp = String.valueOf(tags);
        strtemp =  strtemp.replace("[","").replace("]","");
        strtemp = strtemp.replaceAll("\\s+","");
        return strtemp;
    }
}
