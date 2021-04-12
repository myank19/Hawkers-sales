package io.goolean.tech.hawker.sales.View;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import io.goolean.tech.hawker.sales.Constant.Urls;
import io.goolean.tech.hawker.sales.Networking.VolleySingleton;
import io.goolean.tech.hawker.sales.R;
import io.goolean.tech.hawker.sales.Storage.SharedPrefrence_Login;

public class ProfileActivity extends AppCompatActivity {
    private CircularImageView profile_img;
    private TextView tvName,tvSalesID,parent_email,tvMobileNumber,tvAddress ;
    private RequestQueue requestQueue;
    ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        if (savedInstanceState == null) {
            getSupportActionBar().hide();
            initValue();
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

    private void initValue() {
        profile_img = findViewById(R.id.profile_img);
        tvName = findViewById(R.id.tvName);
        tvSalesID = findViewById(R.id.tvSalesID);
        parent_email = findViewById(R.id.parent_email);
        tvMobileNumber = findViewById(R.id.tvMobileNumber);
        tvAddress = findViewById(R.id.tvAddress);
        ivBack = findViewById(R.id.ivBack);
        SharedPrefrence_Login.getDataLogin(getApplicationContext());
        func_ProfileAPI(SharedPrefrence_Login.getPid());
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
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
                            String str=  obj.getString("data");
                            JSONObject jsoObject = new JSONObject(str);
                            if(jsoObject.getString("status").equals("1")){
                                String s_id= jsoObject.getString("id");
                                String s_sales_id= jsoObject.getString("sales_id");
                                String s_name= jsoObject.getString("name");
                                String s_email_id= jsoObject.getString("email_id");
                                String s_image_url= jsoObject.getString("image_url");
                                String s_address= jsoObject.getString("address");
                                String s_mobile_no= jsoObject.getString("mobile_no");
                                Picasso.with(getApplicationContext()).load(s_image_url).centerCrop().fit().into(profile_img);
                                tvName.setText(s_name);
                                tvSalesID.setText(s_sales_id);
                                parent_email.setText(s_email_id);
                                tvMobileNumber.setText(s_mobile_no);
                                tvAddress.setText(s_address);
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
}
