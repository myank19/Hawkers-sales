package io.goolean.tech.hawker.sales.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
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
import com.google.common.collect.ListMultimap;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.goolean.tech.hawker.sales.Constant.ConnectionDetector;
import io.goolean.tech.hawker.sales.Constant.Urls;
import io.goolean.tech.hawker.sales.Lib.ImageCompression.imageCompression.ImageCompressionListener;
import io.goolean.tech.hawker.sales.Lib.ImageCompression.imagePicker.ImagePicker;
import io.goolean.tech.hawker.sales.Location.Location_Update;
import io.goolean.tech.hawker.sales.Networking.VolleySingleton;
import io.goolean.tech.hawker.sales.R;
import io.goolean.tech.hawker.sales.Storage.Constants;
import io.goolean.tech.hawker.sales.Storage.LocalStoreData;
import io.goolean.tech.hawker.sales.Storage.SharedPrefrence_Login;

public class UpdateDistributionDetail extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{
    private RequestQueue requestQueue;
    private ProgressDialog _progressDialog;
    private LocalStoreData localStoreData;

    private ImageView ivNav;
    private TextView tvEntryBy,tvHawkerCode, tvDateTime,tvHawkerName,tvLocation,tvMobileNumber,tvPhoneType;
    private ImageView img1,img2,img3,img4,img5,img6, img11,img22,img33,img44,img_1,img_2,img_3,img_4;
    private String str_img1 = "",str_img2 = "",str_img3 = "",str_img4 = "",str_img5 = "",str_img6 = "";
    private String img1LatLon,img2LatLon,img3LatLon,img4LatLon;
    private ImageView click_img1,click_img2,click_img3,click_img4;
    private String sIdentityValidationImg, sShopValidationImg ;
    private CheckBox rbCup, rbShirt, rbCarryBag, rbCap, rbAppranne;
    private String clcikImage = "",strCheckBox;
    private ImagePicker imagePicker;
    ListMultimap<String,String> listMultimap;
    private Button btnSubmit;
    private EditText edt_Quantity,edt_Discription;
    private String hawker_code, name ,user_type,mobile_no_contact,hawker_register_address, city, sales_id, sPin;
    private Location_Update location_update;
    private LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_distribution_detail);
        getSupportActionBar().hide();

        SharedPrefrence_Login.getDataLogin(getApplicationContext());

        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        location_update = new Location_Update(UpdateDistributionDetail.this);


        localStoreData = new LocalStoreData(getApplicationContext());
        LocalStoreData.init(getApplicationContext());
        listMultimap = ArrayListMultimap.create();

        requestPermission();
    }

    private void requestPermission() {

        Dexter.withActivity(UpdateDistributionDetail.this)
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
                                    if(locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
                                        if(ConnectionDetector.getConnectionDetector(getApplicationContext()).isConnectingToInternet()==true){
                                            initID();
                                        }else {
                                            Toast.makeText(UpdateDistributionDetail.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
                                        }
                                    }else{
                                        //  Toast.makeText(HomeActivity.this, "GPS STATUS", Toast.LENGTH_SHORT).show();
                                        turnGPSOn();
                                    }

                                }
                            },500);

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
                                status.startResolutionForResult(UpdateDistributionDetail.this, 1000);
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
            Log.i("Hawker_Gps","Window has been closed");
        }
    }

    private void initID() {
        ivNav = findViewById(R.id.ivNav);

        tvEntryBy = findViewById(R.id.tvEntryBy);
        tvHawkerCode = findViewById(R.id.tvHawkerCode);
        tvDateTime = findViewById(R.id.tvDateTime);
        tvHawkerName = findViewById(R.id.tvHawkerName);
        tvLocation = findViewById(R.id.tvLocation);
        tvMobileNumber = findViewById(R.id.tvMobileNumber);
        tvPhoneType = findViewById(R.id.tvPhoneType);
        img1 = findViewById(R.id.img1);
        img2 = findViewById(R.id.img2);
        img3 = findViewById(R.id.img3);
        img4 = findViewById(R.id.img4);
        img5 = findViewById(R.id.img5);
        img6 = findViewById(R.id.img6);

        img11 = findViewById(R.id.img11);
        img22 = findViewById(R.id.img22);
        img33 = findViewById(R.id.img33);
        img44 = findViewById(R.id.img44);

        img_1 = findViewById(R.id.img_1);
        img_2 = findViewById(R.id.img_2);
        img_3 = findViewById(R.id.img_3);
        img_4 = findViewById(R.id.img_4);

        click_img1 = findViewById(R.id.click_img1);
        click_img2 = findViewById(R.id.click_img2);
        click_img3 = findViewById(R.id.click_img3);
        click_img4 = findViewById(R.id.click_img4);

        rbCup = findViewById(R.id.rbCup);
        rbShirt = findViewById(R.id.rbShirt);
        rbCarryBag = findViewById(R.id.rbCarryBag);
        rbCap = findViewById(R.id.rbCap);
        rbAppranne = findViewById(R.id.rbAppranne);
        edt_Quantity = findViewById(R.id.edt_Quantity);
        edt_Discription = findViewById(R.id.edt_Discription);

        rbCup.setOnCheckedChangeListener(this);
        rbShirt.setOnCheckedChangeListener(this);
        rbCarryBag.setOnCheckedChangeListener(this);
        rbCap.setOnCheckedChangeListener(this);
        rbAppranne.setOnCheckedChangeListener(this);
        btnSubmit = findViewById(R.id.btnSubmit);



        ivNav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        click_img1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //clcikImage = "one";
                runtimePermission("one");

            }
        });
        click_img2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //clcikImage = "two";
                runtimePermission("two");
            }
        });
        click_img3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //clcikImage = "three";
                runtimePermission("three");
            }
        });
        click_img4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //clcikImage = "four";
                runtimePermission("four");
            }
        });
        img5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runtimePermission("five");
            }
        });
        img6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                runtimePermission("six");
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strCheckBox = spiltString(listMultimap+"") ;
                if(!TextUtils.isEmpty(strCheckBox)){
                    if(TextUtils.isEmpty(edt_Quantity.getText().toString())){
                        Toast.makeText(UpdateDistributionDetail.this, "Enter Quantity", Toast.LENGTH_SHORT).show();
                    }else if(TextUtils.isEmpty(edt_Discription.getText().toString())){
                        Toast.makeText(UpdateDistributionDetail.this, "Enter Detail", Toast.LENGTH_SHORT).show();
                    }else {
                        if(sIdentityValidationImg.equals("2") || sIdentityValidationImg.equals("3")){
                            if(str_img3.equals("")){
                                Toast.makeText(UpdateDistributionDetail.this, "Click Identity Image First", Toast.LENGTH_SHORT).show();
                            }else if(str_img4.equals("")){
                                Toast.makeText(UpdateDistributionDetail.this, "Click Identity Image Second", Toast.LENGTH_SHORT).show();
                            }else if(sShopValidationImg.equals("2") || sShopValidationImg.equals("3")) {
                                if (str_img1.equals("")) {
                                    Toast.makeText(UpdateDistributionDetail.this, "Click Shop Image First", Toast.LENGTH_SHORT).show();
                                }else if (str_img2.equals("")) {
                                    Toast.makeText(UpdateDistributionDetail.this, "Click Shop Image Second", Toast.LENGTH_SHORT).show();
                                }else {
                                    updateData();
                                }
                            } else {
                                updateData();
                            }
                        }else if(sShopValidationImg.equals("2") || sShopValidationImg.equals("3")){
                            if (str_img1.equals("")) {
                                Toast.makeText(UpdateDistributionDetail.this, "Click Shop Image First", Toast.LENGTH_SHORT).show();
                            }else if (str_img2.equals("")) {
                                Toast.makeText(UpdateDistributionDetail.this, "Click Shop Image Second", Toast.LENGTH_SHORT).show();
                            }else if(sIdentityValidationImg.equals("2") || sIdentityValidationImg.equals("3")){
                                if(str_img3.equals("")){
                                    Toast.makeText(UpdateDistributionDetail.this, "Click Identity Image First", Toast.LENGTH_SHORT).show();
                                }else if(str_img4.equals("")) {
                                    Toast.makeText(UpdateDistributionDetail.this, "Click Identity Image Second", Toast.LENGTH_SHORT).show();
                                }else {
                                    updateData();
                                }
                            } else {
                                updateData();
                            }
                        }else {
                            updateData();
                        }
                    }
                }else {
                    Toast.makeText(UpdateDistributionDetail.this, "Please select what you want distribute for vendor", Toast.LENGTH_SHORT).show();
                }

            }
        });

        funHawkerDetail(LocalStoreData.getValue(Constants.HAWKER_CODE,""));
    }

    private void updateData() {
        if(str_img5.equals("")){
            Toast.makeText(UpdateDistributionDetail.this, "Click Distribution Image First", Toast.LENGTH_SHORT).show();
        }else if(str_img6.equals("")){
            Toast.makeText(UpdateDistributionDetail.this, "Click Distribution Image Second", Toast.LENGTH_SHORT).show();
        }else {
           // sales_id = LocalStoreData.getValue(Constants.ID,"");
            sales_id = SharedPrefrence_Login.getPid();
            if(ConnectionDetector.getConnectionDetector(getApplicationContext()).isConnectingToInternet()==true){
                funUpdateDetail(hawker_code, name ,user_type,mobile_no_contact,hawker_register_address, city,str_img3,str_img4,str_img1,
                        str_img2,str_img5, str_img6,"1", location_update.LATTITUDE,location_update.LONGITUDE, sales_id, location_update.postalCode,
                        strCheckBox, edt_Quantity.getText().toString(), edt_Discription.getText().toString());
            }else {
                Toast.makeText(UpdateDistributionDetail.this, "Check your internet connection", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void runtimePermission(final String sclickImage) {
        Dexter.withActivity(UpdateDistributionDetail.this)
                .withPermissions(Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
                            String sclcikImage = sclickImage;
                            clickImage(sclcikImage);
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
    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(UpdateDistributionDetail.this);
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

    private void clickImage(String sclcikImage) {
        clcikImage = sclcikImage;
        imagePicker = new ImagePicker().chooseFromCamera(true);
        imagePicker.withActivity(this).withCompression(true).start();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 101) {
            if(resultCode == 0){
                finish();
            }
        }else if (requestCode == 1000) {
            if(resultCode == Activity.RESULT_OK){
                Toast.makeText(this, "GPS ON", Toast.LENGTH_SHORT).show();
                location_update = new Location_Update(UpdateDistributionDetail.this);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }else if (requestCode == ImagePicker.SELECT_IMAGE ) {
            if (resultCode == Activity.RESULT_OK) {
                imagePicker.addOnCompressListener(new ImageCompressionListener() {
                    @Override
                    public void onStart() {
                    }
                    @Override
                    public void onCompressed(String filePath) {
                        Bitmap selectedImage = BitmapFactory.decodeFile(filePath);
                        if (clcikImage.equals("one")) {
                            img11.setImageBitmap(selectedImage);
                        }else if(clcikImage.equals("two")){
                            img22.setImageBitmap(selectedImage);
                        }else if(clcikImage.equals("three")){
                            img33.setImageBitmap(selectedImage);
                        }else if(clcikImage.equals("four")){
                            img44.setImageBitmap(selectedImage);
                        }else if(clcikImage.equals("five")){
                            img5.setImageBitmap(selectedImage);
                        }else if(clcikImage.equals("six")){
                            img6.setImageBitmap(selectedImage);
                        }
                        ByteArrayOutputStream baos = new ByteArrayOutputStream();
                        selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                        byte[] b = baos.toByteArray();
                        if (clcikImage.equals("one")) {
                            str_img1 = Base64.encodeToString(b, Base64.DEFAULT);
                        }else if(clcikImage.equals("two")){
                            str_img2 = Base64.encodeToString(b, Base64.DEFAULT);
                        }else if(clcikImage.equals("three")){
                            str_img3 = Base64.encodeToString(b, Base64.DEFAULT);
                        }else if(clcikImage.equals("four")){
                            str_img4 = Base64.encodeToString(b, Base64.DEFAULT);
                        }else if(clcikImage.equals("five")){
                            str_img5 = Base64.encodeToString(b, Base64.DEFAULT);
                        }else if(clcikImage.equals("six")){
                            str_img6 = Base64.encodeToString(b, Base64.DEFAULT);
                        }
                    }
                });

                String filePath = imagePicker.getImageFilePath(data);
                if (filePath != null) {
                    Bitmap selectedImage = BitmapFactory.decodeFile(filePath);
                    img11.setImageBitmap(selectedImage);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                    byte[] b = baos.toByteArray();
                    str_img1 = Base64.encodeToString(b, Base64.DEFAULT);

                }

            } else if (resultCode == Activity.RESULT_CANCELED) {
                Toast.makeText(this, "Cancel", Toast.LENGTH_SHORT).show();
            }
        }
    }


    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        int id = buttonView.getId();
        if(id == R.id.rbCup){
            if(isChecked== true){
                listMultimap.put("Apron",rbCup.getText().toString());
            }else {
                listMultimap.remove("Apron",rbCup.getText().toString());
            }
        }else  if(id == R.id.rbShirt){
            if(isChecked== true){
                listMultimap.put("Apron",rbShirt.getText().toString());
            }else {
                listMultimap.remove("Apron",rbShirt.getText().toString());
            }
        }else  if(id == R.id.rbCarryBag){
            if(isChecked== true){
                listMultimap.put("Apron",rbCarryBag.getText().toString());
            }else {
                listMultimap.remove("Apron",rbCarryBag.getText().toString());
            }
        }else  if(id == R.id.rbCap){
            if(isChecked== true){
                listMultimap.put("Apron",rbCap.getText().toString());
            }else {
                listMultimap.remove("Apron",rbCap.getText().toString());
            }
        }else  if(id == R.id.rbAppranne){
            if(isChecked== true){
                listMultimap.put("Apron",rbAppranne.getText().toString());
            }else {
                listMultimap.remove("Apron",rbAppranne.getText().toString());
            }
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
        String temp = String.valueOf(tags).replace("[","").replace("]","").replaceAll("\\s+","");
        return temp;
    }


    private void cancel(boolean b) {
    }

    private void funHawkerDetail(final String shawkercode) {
        requestQueue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        _progressDialog = ProgressDialog.show(
                UpdateDistributionDetail.this, "", "Please wait...", true, false, new DialogInterface.OnCancelListener(){
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        UpdateDistributionDetail.this.cancel(true);
                        finish();
                    }
                }
        );
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.URL_HAWKER_FULL_DETAIL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null && response.length() > 0) {
                            _progressDialog.dismiss();
                            parseData(response);
                        }else {
                            Toast.makeText(UpdateDistributionDetail.this, "Something Wrong!", Toast.LENGTH_SHORT).show();
                            _progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        _progressDialog.dismiss();
                        if (error.getClass().equals(TimeoutError.class)) {
                            Toast.makeText(UpdateDistributionDetail.this, Constants.RESPONSE_FAILURE_MORE_TIME, Toast.LENGTH_LONG).show();
                        }else if (error.getClass().equals(ServerError.class)) {
                            Toast.makeText(UpdateDistributionDetail.this, Constants.RESPONSE_FAILURE_NOT_RESPOND, Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(UpdateDistributionDetail.this,Constants.RESPONSE_FAILURE_SOMETHING_WENT_WRONG ,Toast.LENGTH_LONG).show();
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("hawker_code", shawkercode);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.REPEAT_TIME, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQue(stringRequest);
    }

    private void parseData(String response) {
        try {
            JSONObject obj = new JSONObject(response);
            JSONObject jsoObject = new JSONObject(obj.getString("data"));
            String sStatus = jsoObject.getString("status");
            if(sStatus.equals("1")){
                hawker_code = jsoObject.getString("hawker_code");
                name = jsoObject.getString("name");
                user_type = jsoObject.getString("user_type");
                mobile_no_contact = jsoObject.getString("mobile_no");
                hawker_register_address = jsoObject.getString("business_address");
                city = jsoObject.getString("city");
                tvEntryBy.setText(jsoObject.getString("sales_name"));
                tvHawkerCode.setText(hawker_code);
                tvHawkerName.setText(name);
                tvDateTime.setText(jsoObject.getString("registered_time"));
                tvLocation.setText(hawker_register_address);
                tvMobileNumber.setText("("+mobile_no_contact+")");
                tvPhoneType.setText("("+jsoObject.getString("phone_type")+")");
                img1.setImageBitmap(StringToBitMap(jsoObject.getString("shop_image_1")));
                img2.setImageBitmap(StringToBitMap(jsoObject.getString("shop_image_2")));
                img3.setImageBitmap(StringToBitMap(jsoObject.getString("aadhar_card_image")));
                img4.setImageBitmap(StringToBitMap(jsoObject.getString("aadhar_card_image_back")));

                sIdentityValidationImg = jsoObject.getString("Identity_validation_status");
                sShopValidationImg = jsoObject.getString("shop_image_validation_status");
                if(sIdentityValidationImg.equals("1")){
                    img_3.setImageDrawable(getResources().getDrawable(getResources().getIdentifier("@drawable/ic_checked", null, getPackageName())));
                    img_4.setImageDrawable(getResources().getDrawable(getResources().getIdentifier("@drawable/ic_checked", null, getPackageName())));
                }else if(sIdentityValidationImg.equals("2")){
                    img_3.setImageDrawable(getResources().getDrawable(getResources().getIdentifier("@drawable/ic_cancel", null, getPackageName())));
                    img_4.setImageDrawable(getResources().getDrawable(getResources().getIdentifier("@drawable/ic_cancel", null, getPackageName())));
                }else if(sIdentityValidationImg.equals("3")){
                    img_3.setImageDrawable(getResources().getDrawable(getResources().getIdentifier("@drawable/ic_data", null, getPackageName())));
                    img_4.setImageDrawable(getResources().getDrawable(getResources().getIdentifier("@drawable/ic_data", null, getPackageName())));
                }

                if(sShopValidationImg.equals("1")){
                    img_1.setImageDrawable(getResources().getDrawable(getResources().getIdentifier("@drawable/ic_checked", null, getPackageName())));
                    img_2.setImageDrawable(getResources().getDrawable(getResources().getIdentifier("@drawable/ic_checked", null, getPackageName())));
                }else if(sShopValidationImg.equals("2")){
                    img_1.setImageDrawable(getResources().getDrawable(getResources().getIdentifier("@drawable/ic_cancel", null, getPackageName())));
                    img_2.setImageDrawable(getResources().getDrawable(getResources().getIdentifier("@drawable/ic_cancel", null, getPackageName())));
                }else if(sShopValidationImg.equals("3")){
                    img_1.setImageDrawable(getResources().getDrawable(getResources().getIdentifier("@drawable/ic_data", null, getPackageName())));
                    img_2.setImageDrawable(getResources().getDrawable(getResources().getIdentifier("@drawable/ic_data", null, getPackageName())));
                }

            }else if(sStatus.equals("0")){
                Toast.makeText(UpdateDistributionDetail.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public Bitmap StringToBitMap(String image){
        try{
            byte [] encodeByte=Base64.decode(image, Base64.DEFAULT);
            InputStream inputStream  = new ByteArrayInputStream(encodeByte);
            Bitmap bitmap  = BitmapFactory.decodeStream(inputStream);
            return bitmap;
        }catch(Exception e){
            e.getMessage();
            return null;
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void funUpdateDetail(final String hawker_code,final String name,final String user_type,final String mobile_no_contact,final String hawker_register_address,
                                 final String city,final String str_img3,final String str_img4,final String str_img1,final String str_img2,final String str_img5,
                                 final String str_img6,final String sStatus,final String shop_latitude,final String shop_longitude,final String sales_id,
                                 final String sPin,final String strCheckBox,final String sQuantity,final String sDetail) {
        requestQueue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        _progressDialog = ProgressDialog.show(
                UpdateDistributionDetail.this, "", "Please wait...", true, false, new DialogInterface.OnCancelListener(){
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        UpdateDistributionDetail.this.cancel(true);
                        finish();
                    }
                }
        );
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.URL_UPDATE_HAWKER_REGISTRATION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null && response.length() > 0) {
                            _progressDialog.dismiss();
                            updateparseData(response);
                        }else {
                            Toast.makeText(UpdateDistributionDetail.this, "Something Wrong!", Toast.LENGTH_SHORT).show();
                            _progressDialog.dismiss();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        _progressDialog.dismiss();
                        if (error.getClass().equals(TimeoutError.class)) {
                            Toast.makeText(UpdateDistributionDetail.this, Constants.RESPONSE_FAILURE_MORE_TIME, Toast.LENGTH_LONG).show();
                        }else if (error.getClass().equals(ServerError.class)) {
                            Toast.makeText(UpdateDistributionDetail.this, Constants.RESPONSE_FAILURE_NOT_RESPOND, Toast.LENGTH_LONG).show();
                        }else {
                            Toast.makeText(UpdateDistributionDetail.this,Constants.RESPONSE_FAILURE_SOMETHING_WENT_WRONG ,Toast.LENGTH_LONG).show();
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("hawker_code", hawker_code);
                params.put("name", name);
                params.put("user_type", user_type);
                params.put("mobile_no_contact", mobile_no_contact);
                params.put("hawker_register_address", hawker_register_address);
                params.put("city", city);
                params.put("aadhar_card_image", str_img3);
                params.put("aadhar_card_image_back", str_img4);
                params.put("shop_image_1", str_img1);
                params.put("shop_image_2", str_img2);
                params.put("distribute_image_1", str_img5);
                params.put("distribute_image_2", str_img6);
                params.put("distribute_status", sStatus);
                params.put("shop_latitude", shop_latitude);
                params.put("shop_longitude", shop_longitude);
                params.put("distributor_id", sales_id);
                if(!TextUtils.isEmpty(sPin)){
                    params.put("sPin", sPin);
                }else {
                    params.put("sPin", "");
                }
                params.put("distribute_type", strCheckBox);
                params.put("quantity", sQuantity);
                params.put("detail", sDetail);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(Constants.REPEAT_TIME, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQue(stringRequest);
    }

    private void updateparseData(String response) {
        try {
            JSONObject obj = new JSONObject(response);
            JSONObject jsoObject = new JSONObject(obj.getString("data"));
            String sStatus = jsoObject.getString("status");
            if(sStatus.equals("1")){
                String message = jsoObject.getString("message");
                Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                finish();
            }else if(sStatus.equals("0")){
                Toast.makeText(UpdateDistributionDetail.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                finish();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
