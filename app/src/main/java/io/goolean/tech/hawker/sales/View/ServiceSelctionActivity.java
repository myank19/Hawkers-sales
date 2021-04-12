package io.goolean.tech.hawker.sales.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.multidex.MultiDex;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.Service;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.goolean.tech.hawker.sales.Constant.ConnectionDetector;
import io.goolean.tech.hawker.sales.Constant.Urls;
import io.goolean.tech.hawker.sales.Controller.Adapter_Category;
import io.goolean.tech.hawker.sales.Controller.ServiceCatsubcatAdapter;
import io.goolean.tech.hawker.sales.Lib.FancyToast.FancyToast;
import io.goolean.tech.hawker.sales.Model.Model_Item_Category;
import io.goolean.tech.hawker.sales.Model.ServiceCategory;
import io.goolean.tech.hawker.sales.Model.ServiceSubCategory;
import io.goolean.tech.hawker.sales.Model.SubCategoryModel;
import io.goolean.tech.hawker.sales.Networking.VolleySingleton;
import io.goolean.tech.hawker.sales.R;
import io.goolean.tech.hawker.sales.Storage.SharedPrefrence_Seller;

public class ServiceSelctionActivity extends AppCompatActivity implements CompoundButton.OnCheckedChangeListener {
    //private RecyclerView rvSelectService;
    private Adapter_Category adapter_category;
    private GridLayoutManager gridLayoutManager;
    private List<Model_Item_Category> arrayListCatItem;
    private ArrayList<SubCategoryModel> arrayListsubCatItem;

    private DividerItemDecoration dividerItemDecoration;
    private Button btnFromTime, btnToTime, close_button;
    private String strAMPM1, strAMPM2;
    private String strFromTime = "", strToTime = "", strCheckBox;
    private RequestQueue requestQueue;
    ListMultimap<String, String> dayMultimap;
    private CheckBox cbMonday, cbTuesday, cbWednesday, cbThursday, cbFriday, cbSaturday, cbSunday;
    private String strHowkerType, strSelectedService, strSelectedStatus, strSubSelectedService, strWorkingDays, strHawkerStartTime, strHawkerEndTime, strString1;
    private HashMap<String, String> hashMapListChooseCat;
    private HashMap<String, String> hashMapListChooseCatStatus;
    TimePicker picker;
    TextView tvService;
    private EditText edt_other_service_id;
    private String strOtherService, sMinute, sMinutes;

    List<Model_Item_Category> item_categoryList;
    ArrayList<SubCategoryModel> subCategoryList;
    public static ArrayList<Model_Item_Category> itemgrpList = new ArrayList<Model_Item_Category>();
    private HashMap<String, List<SubCategoryModel>> listChildData = new HashMap<>();
    public static ArrayList<ArrayList<SubCategoryModel>> list2;
    ExpandableListView rvSelectService;
    ServiceCatsubcatAdapter serviceCatsubcatAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_selction);
        MultiDex.install(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();
        dayMultimap = ArrayListMultimap.create();

        SharedPrefrence_Seller.getHowkerRegistration(ServiceSelctionActivity.this);
        SharedPrefrence_Seller.getSharedPrefrencesFixerShopCategoryId(ServiceSelctionActivity.this);
        SharedPrefrence_Seller.getSharedPrefrencesFixerShopCategoryIdStatus(ServiceSelctionActivity.this);
        SharedPrefrence_Seller.getWorkingDays(ServiceSelctionActivity.this);
        SharedPrefrence_Seller.getStartTime(ServiceSelctionActivity.this);
        SharedPrefrence_Seller.getStopTime(ServiceSelctionActivity.this);
        SharedPrefrence_Seller.getTempHowkerRegistration(ServiceSelctionActivity.this);
        SharedPrefrence_Seller.getHawkerOtherServiceData(ServiceSelctionActivity.this);

        rvSelectService = findViewById(R.id.rvSelectService);
        //rvSelectService.setIndicatorBounds(rvSelectService.getRight()- 1, rvSelectService.getWidth());
        tvService = findViewById(R.id.tvService);
        arrayListCatItem = new ArrayList<>();
        arrayListsubCatItem = new ArrayList<>();
        hashMapListChooseCat = new HashMap<>();
        hashMapListChooseCatStatus = new HashMap<>();
       /* GridLayoutManager gridLayoutManager = new GridLayoutManager(ServiceSelctionActivity.this, 1);
        dividerItemDecoration = new DividerItemDecoration(rvSelectService.getContext(), gridLayoutManager.getOrientation());
        rvSelectService.setHasFixedSize(true);
        rvSelectService.setLayoutManager(gridLayoutManager);
        rvSelectService.addItemDecoration(dividerItemDecoration);*/
        //adapter_category = new Adapter_Category(getApplicationContext(), arrayListCatItem, hashMapListChooseCat, hashMapListChooseCatStatus, tvService);
        btnFromTime = findViewById(R.id.btnFromTime);
        btnToTime = findViewById(R.id.btnToTime);
        close_button = findViewById(R.id.close_button);
        cbMonday = findViewById(R.id.cbMonday);
        cbTuesday = findViewById(R.id.cbTuesday);
        cbWednesday = findViewById(R.id.cbWednesday);
        cbThursday = findViewById(R.id.cbThursday);
        cbFriday = findViewById(R.id.cbFriday);
        cbSaturday = findViewById(R.id.cbSaturday);
        cbSunday = findViewById(R.id.cbSunday);
        edt_other_service_id = findViewById(R.id.edt_other_service_id);

        cbMonday.setOnCheckedChangeListener(this);
        cbTuesday.setOnCheckedChangeListener(this);
        cbWednesday.setOnCheckedChangeListener(this);
        cbThursday.setOnCheckedChangeListener(this);
        cbFriday.setOnCheckedChangeListener(this);
        cbSaturday.setOnCheckedChangeListener(this);
        cbSunday.setOnCheckedChangeListener(this);

        if (SharedPrefrence_Seller.getActivityStatus().equals("Main")) {
            if (SharedPrefrence_Seller.getTempString().equals("Temp")) {
                strHowkerType = SharedPrefrence_Seller.getHowkerType();
            } else {
                strHowkerType = SharedPrefrence_Seller.getHowkerType();
            }
        } else {
            if (SharedPrefrence_Seller.getTempString().equals("Temp")) {
                strHowkerType = SharedPrefrence_Seller.getTempHowkerType();
            } else {
                strHowkerType = SharedPrefrence_Seller.getHowkerType();
            }
        }

        btnFromTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
                startTimeDialog();
            }
        });
        btnToTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopTimeDialog();
            }
        });
        close_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                SharedPrefrence_Seller.getSharedPrefrencesFixerShopCategoryIdStatus(ServiceSelctionActivity.this);
                SharedPrefrence_Seller.getSharedPrefrencesFixerShopSubCategoryId(ServiceSelctionActivity.this);
                strCheckBox = dayMultimap + "";
                //strSelectedService = SharedPrefrence_Seller.getFixr_shop_Subcategoryid();
                strSelectedStatus = SharedPrefrence_Seller.getFixr_shop_categoryStatus();
                //strSubSelectedService = SharedPrefrence_Seller.getFixr_shop_Subcategoryid();
                strFromTime = btnFromTime.getText().toString().trim();
                strToTime = btnToTime.getText().toString().trim();
                strOtherService = edt_other_service_id.getText().toString().trim();
                SharedPreferences prefs = getSharedPreferences("MY_PREFS_NAME", MODE_PRIVATE);
                strSelectedService = prefs.getString("catitem", "");
                strSubSelectedService = prefs.getString("subitem","");
                Log.d("strSelectedService","service: "+strSelectedService);
                if (strFromTime.equals("")) {
                    FancyToast.makeText(getApplicationContext(), "Select Start Time", FancyToast.LENGTH_LONG, FancyToast.WARNING, true).show();
                } else if (strToTime.equals("")) {
                    FancyToast.makeText(getApplicationContext(), "Select End Time", FancyToast.LENGTH_LONG, FancyToast.WARNING, true).show();
                } else if (strCheckBox.equals("{}")) {
                    FancyToast.makeText(getApplicationContext(), "Select Days", FancyToast.LENGTH_LONG, FancyToast.WARNING, true).show();
                } else if (strOtherService.length() > 4) {
                    SharedPrefrence_Seller.saveHawkerOtherService(ServiceSelctionActivity.this, strOtherService);
                    Intent intent = new Intent();
                    intent.putExtra("FROMTIME", strFromTime);
                    intent.putExtra("TOTIME", strToTime);
                    intent.putExtra("DAYS", "" + dayMultimap);
                    setResult(22, intent);
                    finish();
                } else {
                    if (strSelectedService.equals("")) {
                        FancyToast.makeText(getApplicationContext(), "Select Service", FancyToast.LENGTH_LONG, FancyToast.WARNING, true).show();
                    } else if (strSelectedService.equals("{}")) {
                        FancyToast.makeText(getApplicationContext(), "Select Service", FancyToast.LENGTH_LONG, FancyToast.WARNING, true).show();
                    } else if (strSelectedService.equals("[]")) {
                        FancyToast.makeText(getApplicationContext(), "Select Service", FancyToast.LENGTH_LONG, FancyToast.WARNING, true).show();
                    } else {
                        strSelectedStatus = SharedPrefrence_Seller.getFixr_shop_categoryStatus().replace("[", "").replace("]", "");
                        strSelectedStatus = strSelectedStatus.replaceAll("\\s+", "");
                        if (strSelectedStatus.equals("1")) {
                            //strSubSelectedService = SharedPrefrence_Seller.getFixr_shop_Subcategoryid();
                            strSubSelectedService = spiltStringSub(strSubSelectedService);
//                            if (TextUtils.isEmpty(strSubSelectedService)) {
//                                Toast.makeText(ServiceSelctionActivity.this, "Select Sub Service", Toast.LENGTH_SHORT).show();
//                            } else {
                                SharedPrefrence_Seller.saveHawkerOtherService(ServiceSelctionActivity.this, strOtherService);
                                Intent intent = new Intent();
                                intent.putExtra("FROMTIME", strFromTime);
                                intent.putExtra("TOTIME", strToTime);
                                intent.putExtra("DAYS", "" + dayMultimap);
                                setResult(22, intent);
                                finish();
                            //}
                        } else {
                            SharedPrefrence_Seller.saveHawkerOtherService(ServiceSelctionActivity.this, strOtherService);
                            Intent intent = new Intent();
                            intent.putExtra("FROMTIME", strFromTime);
                            intent.putExtra("TOTIME", strToTime);
                            intent.putExtra("DAYS", "" + dayMultimap);
                            setResult(22, intent);
                            finish();
                        }
                       /* strSelectedService = SharedPrefrence_Seller.getFixr_shop_categoryid().replace("[","").replace("]","");
                        strSelectedService = strSelectedService.replaceAll("\\s+","");
                        func_Check_CategoryAPI(strSelectedService);*/

                    }
                }
            }
        });

        if (ConnectionDetector.getConnectionDetector(getApplicationContext()).isConnectingToInternet() == true) {
            if (strHowkerType.equals("Seasonal")) {
                func_fetch_CategoryAPI("Festive", "");
            } else if (strHowkerType.equals("Temporary")) {
                func_fetch_CategoryAPI("Events", "");
            } else if (strHowkerType.equals("Fix")) {
                func_fetch_CategoryAPI("", "Fix");
            } else {
                func_fetch_CategoryAPI("", "Moving");
            }
        } else {
            FancyToast.makeText(getApplicationContext(), "Check your internet connection", FancyToast.LENGTH_LONG, FancyToast.WARNING, true).show();
        }
        restoreData();

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if(android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
            rvSelectService.setIndicatorBounds(100, 100);
        } else {
            rvSelectService.setIndicatorBoundsRelative(100, 100);
        }
    }

    private String spiltStringSub(String strSubSelectedService) {
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

    private void startTimeDialog() {
        final Dialog startTimeDialog = new Dialog(ServiceSelctionActivity.this);
        startTimeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        startTimeDialog.setContentView(R.layout.dialog_start_time_layout);
        startTimeDialog.setCanceledOnTouchOutside(false);
        startTimeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        picker = startTimeDialog.findViewById(R.id.timepicker);
        Button btnOk = startTimeDialog.findViewById(R.id.btnOk);
        picker.setIs24HourView(false);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int hour, minute;
                String am_pm;
                if (Build.VERSION.SDK_INT >= 23) {
                    hour = picker.getHour();
                    minute = picker.getMinute();
                } else {
                    hour = picker.getCurrentHour();
                    minute = picker.getCurrentMinute();
                }
                if (hour > 12) {
                    am_pm = "PM";
                    hour = hour - 12;
                } else {
                    am_pm = "AM";
                }
                if (minute < 10) {
                    sMinute = "0" + minute;
                } else {
                    sMinute = minute + "";
                }
                strAMPM1 = hour + ":" + sMinute + " " + am_pm;
                SharedPrefrence_Seller.saveStartTime(ServiceSelctionActivity.this, strAMPM1);
                btnFromTime.setText(strAMPM1);
                startTimeDialog.dismiss();
            }
        });
        startTimeDialog.show();
    }

    private void stopTimeDialog() {
        final Dialog stopTimeDialog = new Dialog(ServiceSelctionActivity.this);
        stopTimeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        stopTimeDialog.setContentView(R.layout.dialog_start_time_layout);
        stopTimeDialog.setCanceledOnTouchOutside(false);
        stopTimeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        picker = stopTimeDialog.findViewById(R.id.timepicker);
        Button btnOk = stopTimeDialog.findViewById(R.id.btnOk);
        picker.setIs24HourView(false);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int hour, minute;
                String am_pm;
                if (Build.VERSION.SDK_INT >= 23) {
                    hour = picker.getHour();
                    minute = picker.getMinute();
                } else {
                    hour = picker.getCurrentHour();
                    minute = picker.getCurrentMinute();
                }
                if (hour > 12) {
                    am_pm = "PM";
                    hour = hour - 12;
                } else {
                    am_pm = "AM";
                }
                if (minute < 10) {
                    sMinutes = "0" + minute;
                } else {
                    sMinutes = minute + "";
                }
                strAMPM2 = hour + ":" + sMinutes + " " + am_pm;
                btnToTime.setText(strAMPM2);
                SharedPrefrence_Seller.saveStopTime(ServiceSelctionActivity.this, strAMPM2);
                stopTimeDialog.dismiss();
            }
        });
        stopTimeDialog.show();
    }

    private void restoreData() {
        strWorkingDays = SharedPrefrence_Seller.getHawkerWorkingDay();
        strHawkerStartTime = SharedPrefrence_Seller.getHawkerStartTime();
        strHawkerEndTime = SharedPrefrence_Seller.getHawkerEndTime();
        String startTime = SharedPrefrence_Seller.getHAWKERSTARTTIME();
        String stopTime = SharedPrefrence_Seller.getHAWKERSTOPTIME();
        if (!SharedPrefrence_Seller.getHawkerOtherService().equals("")) {
            edt_other_service_id.setText(SharedPrefrence_Seller.getHawkerOtherService());
        }
        if (!startTime.equals("") || startTime != null) {
            btnFromTime.setText(startTime);
        }
        if (!stopTime.equals("") || stopTime != null) {
            btnToTime.setText(stopTime);
        }
        if (strWorkingDays == null || strWorkingDays.equals("")) {
        } else if (!strWorkingDays.equals("")) {
            spiltString(strWorkingDays);
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("FROMTIME", strFromTime);
        intent.putExtra("TOTIME", strToTime);
        intent.putExtra("DAYS", "" + dayMultimap);
        setResult(22, intent);
        finish();
    }

    @Override
    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
        int id = compoundButton.getId();

        if (id == R.id.cbMonday) {
            if (b == true) {
                dayMultimap.put("Days", cbMonday.getText().toString());
            } else {
                dayMultimap.remove("Days", cbMonday.getText().toString());
            }
        } else if (id == R.id.cbTuesday) {
            if (b == true) {
                dayMultimap.put("Days", cbTuesday.getText().toString());
            } else {
                dayMultimap.remove("Days", cbTuesday.getText().toString());
            }
        } else if (id == R.id.cbWednesday) {
            if (b == true) {
                dayMultimap.put("Days", cbWednesday.getText().toString());
            } else {
                dayMultimap.remove("Days", cbWednesday.getText().toString());
            }
        } else if (id == R.id.cbThursday) {
            if (b == true) {
                dayMultimap.put("Days", cbThursday.getText().toString());
            } else {
                dayMultimap.remove("Days", cbThursday.getText().toString());
            }
        } else if (id == R.id.cbFriday) {
            if (b == true) {
                dayMultimap.put("Days", cbFriday.getText().toString());
            } else {
                dayMultimap.remove("Days", cbFriday.getText().toString());
            }
        } else if (id == R.id.cbSaturday) {
            if (b == true) {
                dayMultimap.put("Days", cbSaturday.getText().toString());
            } else {
                dayMultimap.remove("Days", cbSaturday.getText().toString());
            }
        } else if (id == R.id.cbSunday) {
            if (b == true) {
                dayMultimap.put("Days", cbSunday.getText().toString());
            } else {
                dayMultimap.remove("Days", cbSunday.getText().toString());
            }
        }
    }

    private String spiltString(String strSubSelectedService) {
        String[] parts = strSubSelectedService.split(",");
        for (String part : parts) {
            if (part.equals("Monday")) {
                cbMonday.setChecked(true);
            } else if (part.equals("Tuesday")) {
                cbTuesday.setChecked(true);
            } else if (part.equals("Wednesday")) {
                cbWednesday.setChecked(true);
            } else if (part.equals("Thursday")) {
                cbThursday.setChecked(true);
            } else if (part.equals("Friday")) {
                cbFriday.setChecked(true);
            } else if (part.equals("Saturday")) {
                cbSaturday.setChecked(true);
            } else if (part.equals("Sunday")) {
                cbSunday.setChecked(true);
            }
        }
        return strString1;
    }


    /* API for Category*/

    /*private void func_fetch_CategoryAPI(final String sCategoryNAme, final String stype) {
        requestQueue = VolleySingleton.getInstance(ServiceSelctionActivity.this).getRequestQueue();
        //if everything is fine
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.URL_SALES_FETCH_CATEGORY_ITEMS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting response to json object
                            Log.d("catsubcatres",response);
                            JSONObject obj = new JSONObject(response);
                            String strStatau = obj.getString("status");
                            String strcheck_level = obj.getString("check_level");
                            if (strStatau.equals("1")) {
                                String str = obj.getString("data");
                                arrayListCatItem.clear();
                                arrayListsubCatItem.clear();
                                JSONArray jsonArray = new JSONArray(str);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsoObject = jsonArray.getJSONObject(i);
                                    Model_Item_Category model_item_category = new Model_Item_Category();
                                    hashMapListChooseCat = new HashMap<>();
                                    hashMapListChooseCatStatus = new HashMap<>();
                                    model_item_category.setCatId(jsoObject.getString("id"));
                                    model_item_category.setPosition_key(jsoObject.getString("position_key"));
                                    model_item_category.setCatName(jsoObject.getString("cat_name"));
                                    model_item_category.setCatIconName(jsoObject.getString("image_url"));
                                    model_item_category.setSub_cat_status(jsoObject.getString("sub_cat_status"));
 //finally
                                    model_item_category.setCheckLevel(strcheck_level);
                                    strSelectedService = SharedPrefrence_Seller.getFixr_shop_categoryid();
                                    if (strSelectedService != null || !strSelectedService.equals("")) {
                                        // hashMapListChooseCat = convertToHashMap(strSelectedService);
                                        strSelectedService = strSelectedService.replace("[", "").replace("]", "");
                                        strSelectedService = strSelectedService.replaceAll("\\s+", "");
                                        //Toast.makeText(getApplicationContext(),model_item_category.getId()+"",Toast.LENGTH_LONG).show();
                                        spiltStringCategory(model_item_category, strSelectedService, jsoObject.getString("id"));
                                    } else {
                                        model_item_category.setSelected(false);
                                    }
                                    if (jsoObject.getJSONArray("subCat").length() != 0) {
                                        model_item_category.setArrayStatus("1");
                                        model_item_category.setSubCategorysetJsonArray(jsoObject.getJSONArray("subCat"));
                                    } else {
                                        model_item_category.setArrayStatus("0");
                                        model_item_category.setSubCategorysetJsonArray(jsoObject.getJSONArray("subCat"));
                                    }
                                    arrayListCatItem.add(model_item_category);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        rvSelectService.setAdapter(adapter_category);
                        adapter_category.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.getClass().equals(TimeoutError.class)) {
                            Toast.makeText(getApplicationContext(), "It took longer than expected to get the response from Server.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Something went wrong ! Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("cat_name", sCategoryNAme);
                params.put("type", stype);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQue(stringRequest);
    }*/

    private void func_fetch_CategoryAPI(final String sCategoryNAme, final String stype) {

        item_categoryList = new ArrayList<Model_Item_Category>();
        final List<SubCategoryModel> list = new ArrayList<>();
        list2 = new ArrayList<ArrayList<SubCategoryModel>>();
        List<SubCategoryModel> list3 = new ArrayList<>();
        item_categoryList.clear();
        list2.clear();
        item_categoryList.clear();


        requestQueue = VolleySingleton.getInstance(ServiceSelctionActivity.this).getRequestQueue();
        //if everything is fine
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.URL_SALES_FETCH_CATEGORY_ITEMS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting response to json object
                            Log.d("catsubcatres",response);
                            JSONObject obj = new JSONObject(response);
                            String strStatau = obj.getString("status");
                            String strcheck_level = obj.getString("check_level");
                            if (strStatau.equals("1")) {
                                JSONArray array = obj.getJSONArray("data");
                                for (int i = 0; i < array.length(); i++) {
                                    JSONObject jsoObject = array.getJSONObject(i);
                                    Model_Item_Category model_item_category = new Model_Item_Category();
                                    model_item_category.setCatId(jsoObject.getString("id"));
                                    model_item_category.setPosition_key(jsoObject.getString("position_key"));
                                    model_item_category.setCatName(jsoObject.getString("cat_name"));
                                    model_item_category.setCatIconName(jsoObject.getString("image_url"));
                                    model_item_category.setSub_cat_status(jsoObject.getString("sub_cat_status"));

                                    item_categoryList.add(model_item_category);
                                    subCategoryList = new ArrayList<SubCategoryModel>();

                                    if (jsoObject.getString("sub_cat_status").equalsIgnoreCase("1")) {
                                        JSONArray childArray = jsoObject.getJSONArray("subCat");

                                        for (int j = 0; j < childArray.length(); j++) {

                                            JSONObject childObj = childArray.getJSONObject(j);
                                            SubCategoryModel subCategoryModel = new SubCategoryModel();
                                            subCategoryModel.setsSubCatName(childObj.getString("sub_cat_name"));
                                            subCategoryModel.setsSubID(childObj.getString("id"));
                                            subCategoryModel.setStrSubPosition_key(childObj.getString("position_key"));
                                            subCategoryModel.setCheckLevel(childObj.getString("check_level"));
                                            subCategoryModel.setsCatId(childObj.getString("category"));
                                            subCategoryList.add(subCategoryModel);
                                        }
                                    }
                                    /*else if (jsoObject.getString("sub_cat_status").equalsIgnoreCase("0")) {
                                    }*/
                                    list2.add(subCategoryList);
                                }
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        serviceCatsubcatAdapter = new ServiceCatsubcatAdapter(ServiceSelctionActivity.this, item_categoryList, list2, rvSelectService);
                        rvSelectService.setAdapter(serviceCatsubcatAdapter);




                        /*rvSelectService.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                            @Override
                            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition,
                                                        long id) {
                                //Model_Item_Category model_item_category = (Model_Item_Category) parent.getItemAtPosition(groupPosition);

                                try {

                                    int childCountWay2 = parent.getExpandableListAdapter().getChildrenCount(groupPosition);
                                if (childCountWay2<1) {
                                    // do whatever you want
                                    Toast.makeText(getApplicationContext(),"Child List is Empty",Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(getApplicationContext(),"Child List not is Empty",Toast.LENGTH_SHORT).show();

                                }
                                }
                                catch (NullPointerException e) {
                                    e.printStackTrace();
                                }

                                return true;
                            }
                        });*/
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.getClass().equals(TimeoutError.class)) {
                            Toast.makeText(getApplicationContext(), "It took longer than expected to get the response from Server.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Something went wrong ! Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("cat_name", sCategoryNAme);
                params.put("type", stype);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQue(stringRequest);
    }

    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void func_Check_CategoryAPI(final String sCategoryNAme) {
        requestQueue = VolleySingleton.getInstance(ServiceSelctionActivity.this).getRequestQueue();
        //if everything is fine
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.URL_SALES_FETCH_CATEGORY_ITEMS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            String str = obj.getString("data");
                            String strStatau = obj.getString("status");
                            if (strStatau.equals("1")) {

                            } else {
                                SharedPrefrence_Seller.saveHawkerOtherService(ServiceSelctionActivity.this, strOtherService);
                                Intent intent = new Intent();
                                intent.putExtra("FROMTIME", strFromTime);
                                intent.putExtra("TOTIME", strToTime);
                                intent.putExtra("DAYS", "" + dayMultimap);
                                setResult(22, intent);
                                finish();
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
                            Toast.makeText(getApplicationContext(), "It took longer than expected to get the response from Server.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Something went wrong ! Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("cat_id", sCategoryNAme);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQue(stringRequest);
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void func_Check_SubCategoryAPI(final String sCategoryNAme) {
        requestQueue = VolleySingleton.getInstance(ServiceSelctionActivity.this).getRequestQueue();
        //if everything is fine
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.URL_SALES_FETCH_CATEGORY_ITEMS,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            String str = obj.getString("data");
                            String strStatau = obj.getString("status");
                            if (strStatau.equals("1")) {

                            } else {
                                SharedPrefrence_Seller.saveHawkerOtherService(ServiceSelctionActivity.this, strOtherService);
                                Intent intent = new Intent();
                                intent.putExtra("FROMTIME", strFromTime);
                                intent.putExtra("TOTIME", strToTime);
                                intent.putExtra("DAYS", "" + dayMultimap);
                                setResult(22, intent);
                                finish();
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
                            Toast.makeText(getApplicationContext(), "It took longer than expected to get the response from Server.", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "Something went wrong ! Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("cat_id", sCategoryNAme);
                params.put("sub_cat_id", sCategoryNAme);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQue(stringRequest);
    }


    public HashMap<String, String> convertToHashMap(String jsonString) {
        HashMap<String, String> myHashMap = new HashMap<String, String>();
        try {
            JSONArray jArray = new JSONArray(jsonString);
            JSONObject jObject = null;
            String keyString = null;
            for (int i = 0; i < jArray.length(); i++) {
                jObject = jArray.getJSONObject(i);
                // beacuse you have only one key-value pair in each object so I have used index 0
                keyString = (String) jObject.names().get(0);
                myHashMap.put(keyString, keyString);
            }
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return myHashMap;
    }

    private String spiltStringCategory(Model_Item_Category model_item_category, String SelectedService, String id) {
        String strpart = "";
        String[] parts = SelectedService.split(",");
        for (String part : parts) {
            strpart = part;
            if (strpart.equals(id)) {
                model_item_category.setSelected(true);
                /* hashMapListChooseCat.put(id,id);
                List<String> l = new ArrayList<String>(hashMapListChooseCat.keySet());
                Toast.makeText(context, l+"", Toast.LENGTH_SHORT).show();
                SharedPrefrence_Seller.saveSharedPrefrencesFixerShopCategoryId(context, String.valueOf(l));*/
            }
        }
        return strpart;
    }
}
