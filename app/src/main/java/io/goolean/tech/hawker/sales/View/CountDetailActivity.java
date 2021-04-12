package io.goolean.tech.hawker.sales.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTabHost;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.goolean.tech.hawker.sales.Constant.ConnectionDetector;
import io.goolean.tech.hawker.sales.Constant.MessageConstant;
import io.goolean.tech.hawker.sales.Constant.Singleton.CallbackSnakebarModel;
import io.goolean.tech.hawker.sales.Constant.Urls;
import io.goolean.tech.hawker.sales.Controller.Adapter_Hawkers_Details;
import io.goolean.tech.hawker.sales.Lib.FancyToast.FancyToast;
import io.goolean.tech.hawker.sales.Model.HawkersDetails;
import io.goolean.tech.hawker.sales.Networking.VolleySingleton;
import io.goolean.tech.hawker.sales.R;
import io.goolean.tech.hawker.sales.Storage.SharedPrefrence_Login;

public class CountDetailActivity extends AppCompatActivity implements View.OnClickListener {
    private RelativeLayout relative_total_reg_id,relative_today_reg_id;
    private RecyclerView rv_hawkerlist;
    private RequestQueue requestQueue;
    private TextView tv_todayregistartion, tv_today_verified, tv_today_unverified,tv_total_registartion, tv_total_verified,tv_total_unverified;
    private Button btn_verified, btn_unverfied;

    private ArrayList<HawkersDetails> arrayhawkersDetails;
    private HawkersDetails hawkersDetails;
    private Adapter_Hawkers_Details adapter_hawkers_details;
    private TextView tv_notFound;
    private ImageView ivNav;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_count_detail);
        getSupportActionBar().hide();
        SharedPrefrence_Login.getDataLogin(CountDetailActivity.this);
        //Intial ID
        initID();
    }

    private void initID() {

        ivNav = findViewById(R.id.ivNav);
        ivNav.setOnClickListener(this);
        relative_total_reg_id = findViewById(R.id.relative_total_reg_id);
        //relative_total_reg_id.setOnClickListener(this);

        relative_today_reg_id = findViewById(R.id.relative_today_reg_id);
        relative_today_reg_id.setOnClickListener(this);

        tv_todayregistartion = findViewById(R.id.tv_todayregistartion);
        tv_today_verified = findViewById(R.id.tv_today_verified);
        tv_today_unverified = findViewById(R.id.tv_today_unverified);

        tv_total_registartion = findViewById(R.id.tv_total_registartion);
        tv_total_verified = findViewById(R.id.tv_total_verified);
        tv_total_unverified = findViewById(R.id.tv_total_unverified);

        btn_verified = findViewById(R.id.btn_verified);
        btn_verified.setOnClickListener(this);

        btn_unverfied = findViewById(R.id.btn_unverfied);
        btn_unverfied.setOnClickListener(this);


        tv_notFound = findViewById(R.id.tv_notFound);
        rv_hawkerlist = findViewById(R.id.rv_hawkerlist);



        arrayhawkersDetails = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        rv_hawkerlist.setLayoutManager(linearLayoutManager);
        rv_hawkerlist.setHasFixedSize(true);
        adapter_hawkers_details = new Adapter_Hawkers_Details(getApplicationContext(), arrayhawkersDetails);

        if(ConnectionDetector.getConnectionDetector(getApplicationContext()).isConnectingToInternet()==true){
            funCount(SharedPrefrence_Login.getPid());
            funDetailList(SharedPrefrence_Login.getPid(),"1");
        }else {
            FancyToast.makeText(getApplicationContext(),"Check your internet connection",FancyToast.LENGTH_LONG,FancyToast.WARNING,true).show();
        }

    }

    @Override
    public void onClick(View view) {

        int id = view.getId();

        if(id == R.id.ivNav){
            finish();
        }else if(id == R.id.relative_total_reg_id){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                relative_total_reg_id.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.greylight)));
                relative_today_reg_id.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
            }
        }else if(id == R.id.relative_today_reg_id){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                relative_total_reg_id.setBackgroundTintList(ColorStateList.valueOf(Color.WHITE));
                relative_today_reg_id.setBackgroundTintList(ColorStateList.valueOf(getResources().getColor(R.color.greylight)));
            }
        }else if(id == R.id.btn_verified){
            if(ConnectionDetector.getConnectionDetector(getApplicationContext()).isConnectingToInternet()==true){
                funDetailList(SharedPrefrence_Login.getPid(),"1");
            }else {
                FancyToast.makeText(getApplicationContext(),"Check your internet connection",FancyToast.LENGTH_LONG,FancyToast.WARNING,true).show();
            }
        }else if(id == R.id.btn_unverfied){
            if(ConnectionDetector.getConnectionDetector(getApplicationContext()).isConnectingToInternet()==true){
                funDetailList(SharedPrefrence_Login.getPid(),"0");
            }else {
                FancyToast.makeText(getApplicationContext(),"Check your internet connection",FancyToast.LENGTH_LONG,FancyToast.WARNING,true).show();
            }
        }
    }

    private void funCount(final String sales_id) {

        final ProgressDialog _progressDialog = ProgressDialog.show(
                CountDetailActivity.this, "", "Please wait...", true, false,
                new DialogInterface.OnCancelListener(){
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        CountDetailActivity.this.cancel(true);
                        finish();
                    }
                }
        );
        requestQueue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.URL_TOTAL_HAWKER_ADD,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject obj = new JSONObject(response);
                            String str = obj.getString("data");
                            JSONObject jsoObject = new JSONObject(str);
                            if (jsoObject.getString("status").equals("1")) {
                                _progressDialog.dismiss();
                                tv_todayregistartion.setText("Today Registration : "+jsoObject.getString("Today_total_registration"));
                                tv_today_verified.setText("Verified Registration : "+jsoObject.getString("Today_total_verified_hawker"));
                                tv_today_unverified.setText("Un-Verified Registration : "+jsoObject.getString("Today_total_unverified_hawker"));
                                tv_total_registartion.setText("Total Registration : "+jsoObject.getString("total_registration"));
                                tv_total_verified.setText("Verified Registration : "+jsoObject.getString("verified_hawker"));
                                tv_total_unverified.setText("Un-Verified Registration : "+jsoObject.getString("unverified_hawker"));

                            }else if (jsoObject.getString("status").equals("0")) {
                                _progressDialog.dismiss();
                                CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Internal Error ! Try Again Later", MessageConstant.toast_warning);
                            }
                        } catch(JSONException e){
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        _progressDialog.dismiss();
                        if (error.getClass().equals(TimeoutError.class)) {
                            CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "It took longer than expected to get the response from Server.",
                                    MessageConstant.toast_warning);
                        }else {
                            CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Something went wrong ! Please try again.",
                                    MessageConstant.toast_warning);
                        }
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("sales_id", sales_id);
                return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(this).addToRequestQue(stringRequest);
    }

    private void cancel(boolean b) {
    }



    private void funDetailList(final String salesID,final String code) {
        requestQueue = VolleySingleton.getInstance(CountDetailActivity.this).getRequestQueue();
        //if everything is fine
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.URL_TOTAL_HAWKER_ADD_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            arrayhawkersDetails.clear();
                            JSONObject obj = new JSONObject(response);
                            String strData=  obj.getString("data");
                            JSONArray jsonArrayDetail = new JSONArray(strData);
                            for(int i = 0;i<jsonArrayDetail.length();i++) {
                                JSONObject jsonObjectFav = jsonArrayDetail.getJSONObject(i);
                                if(jsonObjectFav.getString("status").equals("1")) {
                                    tv_notFound.setVisibility(View.GONE);
                                    rv_hawkerlist.setVisibility(View.VISIBLE);
                                    hawkersDetails = new HawkersDetails();
                                    hawkersDetails.setHawkername(jsonObjectFav.getString("name"));
                                    hawkersDetails.setHawker_mobilenum(jsonObjectFav.getString("mobile_no_contact"));
                                    hawkersDetails.setHawkercaode(jsonObjectFav.getString("hawker_code"));
                                    hawkersDetails.setRegdate(jsonObjectFav.getString("registered_time"));
                                    arrayhawkersDetails.add(hawkersDetails);
                                }else {
                                    rv_hawkerlist.setVisibility(View.GONE);
                                    tv_notFound.setVisibility(View.VISIBLE);
                                }
                            }
                            rv_hawkerlist.setAdapter(adapter_hawkers_details);
                            adapter_hawkers_details.notifyDataSetChanged();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.getClass().equals(TimeoutError.class)) {
                            CallbackSnakebarModel.getInstance().SnakebarMessage(CountDetailActivity.this, "It took longer than expected to get the response from Server.",
                                    MessageConstant.toast_warning);
                        }else {
                            CallbackSnakebarModel.getInstance().SnakebarMessage(CountDetailActivity.this, "Something went wrong ! Please try again.",
                                    MessageConstant.toast_warning);
                        }
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("sales_id", salesID);
                params.put("verification_status", code);
                return params;
            }};
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(CountDetailActivity.this).addToRequestQue(stringRequest);
    }


}
