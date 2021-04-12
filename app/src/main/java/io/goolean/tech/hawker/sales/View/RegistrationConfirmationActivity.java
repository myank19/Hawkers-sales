package io.goolean.tech.hawker.sales.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
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
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import io.goolean.tech.hawker.sales.Constant.ConnectionDetector;
import io.goolean.tech.hawker.sales.Constant.Dialog_;
import io.goolean.tech.hawker.sales.Constant.MessageConstant;
import io.goolean.tech.hawker.sales.Constant.Singleton.CallbackSnakebarModel;
import io.goolean.tech.hawker.sales.Constant.Urls;
import io.goolean.tech.hawker.sales.Controller.Adapter_Category;
import io.goolean.tech.hawker.sales.Controller.Question_Adapter;
import io.goolean.tech.hawker.sales.Lib.FancyToast.FancyToast;
import io.goolean.tech.hawker.sales.Model.Model_Item_Category;
import io.goolean.tech.hawker.sales.Model.QuestionCheckModel;
import io.goolean.tech.hawker.sales.Networking.VolleySingleton;
import io.goolean.tech.hawker.sales.R;
import io.goolean.tech.hawker.sales.Storage.SharedPrefrence_Login;
import io.goolean.tech.hawker.sales.Storage.SharedPrefrence_Seller;
import io.goolean.tech.hawker.sales.Utililty.OtpReader.SmsListener;
import io.goolean.tech.hawker.sales.Utililty.OtpReader.SmsReceiver;

public class RegistrationConfirmationActivity extends AppCompatActivity implements View.OnFocusChangeListener, View.OnKeyListener, TextWatcher
{

    private EditText mPinFirstDigitEditText;
    private EditText mPinSecondDigitEditText;
    private EditText mPinThirdDigitEditText;
    private EditText mPinForthDigitEditText;
    private EditText mPinFifthDigitEditText;
    private EditText mPinHiddenEditText;
    private Button btnSubmit;
    private TextView tvCount;
    private TextView tvResendOTP;
    private CheckBox cbNumberInfo;
    private RequestQueue requestQueue;
    private String iChecked = "",hawker_code,CheckQuestion,temp;
    private List<QuestionCheckModel> questionCheckModels;
    ListMultimap<String,String> questionMultimap ;
    private DividerItemDecoration dividerItemDecoration;
    private RecyclerView rvChecklist;
    private Question_Adapter questionAdapter;
    private ProgressDialog _progressDialog;
    public static Button btn_verifybycall;
    public static TextView tvMobileNumber;
    private String callNumber, storenumber;
    private String otp1,otp2,otp3,otp4;
    CountDownTimer yourCountDownTimer;

    @Override
    public void afterTextChanged(Editable s) {
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
    }

    /**
     * Hides soft keyboard.
     *
     * @param editText EditText which has focus
     */
    public void hideSoftKeyboard(EditText editText) {
        if (editText == null)
            return;

        InputMethodManager imm = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
    }

    /**
     * Initialize EditText fields.
     */
    private void init() {
        mPinFirstDigitEditText = (EditText) findViewById(R.id.pin_first_edittext);
        mPinSecondDigitEditText = (EditText) findViewById(R.id.pin_second_edittext);
        mPinThirdDigitEditText = (EditText) findViewById(R.id.pin_third_edittext);
        mPinForthDigitEditText = (EditText) findViewById(R.id.pin_forth_edittext);
        mPinHiddenEditText = (EditText) findViewById(R.id.pin_hidden_edittext);
        btnSubmit = (Button)findViewById(R.id.btn_submit_id);
        btn_verifybycall = (Button)findViewById(R.id.btn_verifybycall);
        tvCount =(TextView)findViewById(R.id.tv_timer);
        tvResendOTP =(TextView)findViewById(R.id.tv_resend_otp);
        tvMobileNumber =(TextView)findViewById(R.id.tvMobileNumber);
        cbNumberInfo =findViewById(R.id.cbNumberInfo);
        mPinFirstDigitEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        mPinSecondDigitEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        mPinThirdDigitEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        mPinForthDigitEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        SharedPrefrence_Login.getDataLogin(getApplicationContext());
        SharedPrefrence_Seller.getHowkerRegistration(getApplicationContext());
        SharedPrefrence_Seller.saveHawkerNumberMatch(RegistrationConfirmationActivity.this,"Verfication");
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MainLayout(this, null));

        init();

        setPINListeners();
        counterTimeStart();
        SmsReceiver.bindListener(new SmsListener() {
            @Override
            public void messageReceived(final String messageText) {
                otp1 = String.valueOf(messageText.charAt(0));
                otp2 = String.valueOf(messageText.charAt(1));
                otp3 = String.valueOf(messageText.charAt(2));
                otp4 = String.valueOf(messageText.charAt(3));

                mPinFirstDigitEditText.setText(otp1);
                mPinSecondDigitEditText.setText(otp2);
                mPinThirdDigitEditText.setText(otp3);
                mPinForthDigitEditText.setText(otp4);
                btnSubmit.setVisibility(View.VISIBLE);
                String otp = mPinFirstDigitEditText.getText().toString()+ mPinSecondDigitEditText.getText().toString()+ mPinThirdDigitEditText.getText().toString()
                        + mPinForthDigitEditText.getText().toString();
                if(ConnectionDetector.getConnectionDetector(getApplicationContext()).isConnectingToInternet() == true) {
                    fun_OTP("",otp, SharedPrefrence_Login.getPdevice_id(), SharedPrefrence_Seller.getHowkerContactNumber());
                }else {
                    CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Please check your internet connection", MessageConstant.toast_warning);
                }

            }
        });

        cbNumberInfo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b == true){
                    iChecked = "1";
                }else {
                    iChecked = "";
                }
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String otp = mPinFirstDigitEditText.getText().toString()+ mPinSecondDigitEditText.getText().toString()+ mPinThirdDigitEditText.getText().toString()
                        + mPinForthDigitEditText.getText().toString();
                if(!TextUtils.isEmpty(otp)){
                    if(ConnectionDetector.getConnectionDetector(getApplicationContext()).isConnectingToInternet() == true) {
                        fun_OTP("",otp, SharedPrefrence_Login.getPdevice_id(), SharedPrefrence_Seller.getHowkerContactNumber());
                    }else {
                        CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Please check your internet connection", MessageConstant.toast_warning);
                    }
                }else {
                    CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Enter vaild 4 digit code", MessageConstant.toast_warning);
                }
            }
        });
        tvResendOTP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                counterStart();
                tvResendOTP.setVisibility(View.VISIBLE);
                if(ConnectionDetector.getConnectionDetector(getApplicationContext()).isConnectingToInternet() == true) {
                    fun_OTPResend(SharedPrefrence_Login.getPdevice_id(), SharedPrefrence_Seller.getHowkerContactNumber());
                }else {
                    CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Please check your internet connection", MessageConstant.toast_warning);
                }
            }
        });

        btn_verifybycall.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fun_OTP("2","", SharedPrefrence_Login.getPdevice_id(), SharedPrefrence_Seller.getHowkerContactNumber());
               /* SharedPrefrence_Seller.ClearSharedPrefrenc(getApplicationContext());
                successDialog(SharedPrefrence_Login.getPid(),SharedPrefrence_Seller.getFixr_shop_id());*/
            }
        });
    }

    private void counterStart() {
        yourCountDownTimer = new CountDownTimer(120000, 1000) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {
                tvCount.setText(""+String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }
            public void onFinish() {
                tvCount.setText("");
                tvResendOTP.setVisibility(View.VISIBLE);
                // btnSubmit.setVisibility(View.GONE);
                if(ConnectionDetector.getConnectionDetector(getApplicationContext()).isConnectingToInternet()==true) {
                    fun_OTPExpire(getApplicationContext(),SharedPrefrence_Login.getPdevice_id(),SharedPrefrence_Seller.getHowkerContactNumber());
                }else {
                    Dialog_.getInstanceDialog(RegistrationConfirmationActivity.this).dialog_OTPInternet(RegistrationConfirmationActivity.this,
                            SharedPrefrence_Login.getPdevice_id(),SharedPrefrence_Seller.getHowkerContactNumber());
                    //CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Please check your internet connection", MessageConstant.toast_warning);
                }
            }
        }.start();
    }
    private void counterTimeStart(){
        yourCountDownTimer =  new CountDownTimer(120000, 1000) { // adjust the milli seconds here
            public void onTick(long millisUntilFinished) {
                tvCount.setText(""+String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes( millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {
                tvCount.setText("");
                tvResendOTP.setVisibility(View.VISIBLE);
                if(ConnectionDetector.getConnectionDetector(getApplicationContext()).isConnectingToInternet()==true) {
                    fun_OTPExpire(getApplicationContext(),SharedPrefrence_Login.getPdevice_id(),SharedPrefrence_Seller.getHowkerContactNumber());
                }else {
                    Dialog_.getInstanceDialog(RegistrationConfirmationActivity.this).dialog_OTPInternet(RegistrationConfirmationActivity.this,
                            SharedPrefrence_Login.getPdevice_id(),SharedPrefrence_Seller.getHowkerContactNumber());
                    //CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Please check your internet connection", MessageConstant.toast_warning);
                }
            }
        }.start();
    }


    //-----------------------SUBMIT OTP-----------------------------------------------------
    private void fun_OTP(final String call_status,final String otp, final String pdevice_id, final String number) {
        requestQueue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        //if everything is fine
        StringRequest stringRequest = new StringRequest(Request.Method.POST,Urls.URL_HAWKER_VERIFICATION_OTP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            String str = obj.getString("data");
                            JSONObject jsoObject = new JSONObject(str);
                            if (jsoObject.getString("status").equals("0")) {
                                /*OTP NOT MATCHED*/
                                CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Enter vaild 4 digit code", MessageConstant.toast_warning);
                            }else if (jsoObject.getString("status").equals("1")){
                                yourCountDownTimer.cancel();
                                /*OTP MATCHED*/
                                hawker_code = jsoObject.getString("hawker_code");
                                SharedPrefrence_Seller.ClearSharedPrefrenc(getApplicationContext());
                                successDialog(SharedPrefrence_Login.getPid(),hawker_code);
                            }
                        } catch(JSONException e){
                            e.printStackTrace();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Server Respond Error! Please Try Later", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("device_id", pdevice_id);
                params.put("mobile_no", number);
                params.put("otp", otp);
                params.put("call_verification", call_status);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQue(stringRequest);
    }

    private String spiltString(String strSubSelectedService) {
        Matcher matcher = Pattern.compile("\\[([^\\]]+)").matcher(strSubSelectedService);
        List<String> tags = new ArrayList<>();
        int pos = -1;
        while (matcher.find(pos+1)){
            pos = matcher.start();
            tags.add(matcher.group(1));
        }
        temp = String.valueOf(tags);
        temp =  temp.replace("[","").replace("]","");
        temp = temp.replaceAll("\\s+","");
        return temp;
    }
    public void successDialog(final String pid, final String hawker_code) {
        final Dialog successDialog = new Dialog(RegistrationConfirmationActivity.this);
        successDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        successDialog.setContentView(R.layout.dialog_checklist_questions_layout);
        successDialog.setCanceledOnTouchOutside(false);
        successDialog.setCancelable(false);
        successDialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        SharedPrefrence_Seller.getHawkerQuestionData(RegistrationConfirmationActivity.this);
        rvChecklist = successDialog.findViewById(R.id.rvChecklist);
        questionMultimap = ArrayListMultimap.create();
        questionCheckModels = new ArrayList<>();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(RegistrationConfirmationActivity.this,1);
        dividerItemDecoration = new DividerItemDecoration(rvChecklist.getContext(), gridLayoutManager.getOrientation());
        rvChecklist.setHasFixedSize(true);
        rvChecklist.setLayoutManager(gridLayoutManager);
        rvChecklist.addItemDecoration(dividerItemDecoration);
        questionAdapter = new Question_Adapter(getApplicationContext(),questionCheckModels,questionMultimap);
        funQuestionList(rvChecklist);
        successDialog.findViewById(R.id.btn_Confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckQuestion  = spiltString(SharedPrefrence_Seller.getHawkerQuestion());
                if(CheckQuestion.equals("")) {
                    FancyToast.makeText(getApplicationContext(),"Please select option",FancyToast.LENGTH_LONG,FancyToast.WARNING,true).show();
                }else if(CheckQuestion.equals("{}")){
                    FancyToast.makeText(getApplicationContext(),"Please select option",FancyToast.LENGTH_LONG,FancyToast.WARNING,true).show();
                }else if(CheckQuestion.equals("[]")){
                    FancyToast.makeText(getApplicationContext(),"Please select option",FancyToast.LENGTH_LONG,FancyToast.WARNING,true).show();
                }else {
                    if(CheckQuestion.isEmpty()){
                        CheckQuestion = "1,2";
                        funSendData(pid,hawker_code,successDialog,CheckQuestion);
                    }else {
                        funSendData(pid,hawker_code,successDialog,CheckQuestion);
                    }

                }
            }
        });
        successDialog.show();
    }

    private void funSendData(final String pid, final String hawker_cod,final Dialog successDialog,final String CheckQuestion) {
        //  Toast.makeText(HomeActivity.this, sCity+","+sState, Toast.LENGTH_SHORT).show();
        requestQueue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        _progressDialog = ProgressDialog.show(
                RegistrationConfirmationActivity.this, "", "Please wait...", true, false, new DialogInterface.OnCancelListener(){
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        RegistrationConfirmationActivity.this.cancel(true);finish();
                    }
                }
        );
        //if everything is fine
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.URL_HAWKER_SALES_UPDATE_Sale,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            String str = obj.getString("data");
                            JSONObject jsoObject = new JSONObject(str);
                            String status = jsoObject.getString("status");
                            if(status.equals("1")) {
                                SharedPrefrence_Seller.ClearSharedPrefrenc(getApplicationContext());
                                _progressDialog.dismiss();
                                successDialog.dismiss();
                                startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                                finish();
                                finishAffinity();
                            }else if(status.equals("0")){
                                _progressDialog.dismiss();
                                Toast.makeText(RegistrationConfirmationActivity.this, "Faild", Toast.LENGTH_LONG).show();
                            }
                            //
                            SharedPrefrence_Seller.ClearSharedPrefrenc(getApplicationContext());
                            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                            finish();
                            finishAffinity();
                            //successDialog.dismiss();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            _progressDialog.dismiss();
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
                    params.put("sales_id", pid);
                    params.put("hawker_code", hawker_cod);
                    params.put("check_list", CheckQuestion);
                    return params;
            }
        };
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQue(stringRequest);
    }

    private void cancel(boolean b) {

    }

  ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void funQuestionList(final RecyclerView rvChecklist) {
        requestQueue = VolleySingleton.getInstance(RegistrationConfirmationActivity.this).getRequestQueue();
        //if everything is fine
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.URL_HAWKER_SALES_QUESTION_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            String sData=  obj.getString("data");
                                JSONArray jsonArray = new JSONArray(sData);
                                for (int i =0; i<jsonArray.length();i++) {
                                    JSONObject jsoObject = jsonArray.getJSONObject(i);
                                    QuestionCheckModel questionCheckModel = new QuestionCheckModel();
                                    questionCheckModel.setsQuestionID(jsoObject.getString("id"));
                                    questionCheckModel.setsQuestion(jsoObject.getString("question"));
                                    questionCheckModel.setsQuestionSatatus(jsoObject.getString("status"));
                                    questionCheckModel.setSelected(false);
                                    questionCheckModels.add(questionCheckModel);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        rvChecklist.setAdapter(questionAdapter);
                        questionAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.getClass().equals(TimeoutError.class)) {
                            Toast.makeText(getApplicationContext(), "It took longer than expected to get the response from Server.", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getApplicationContext(), "Something went wrong ! Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(20000, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleySingleton.getInstance(getApplicationContext()).addToRequestQue(stringRequest);
    }


    //-----------------------OTP RESEND-----------------------------------------------------
    private void fun_OTPResend( final String pdevice_id, final String number) {

        requestQueue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        //if everything is fine
        StringRequest stringRequest = new StringRequest(Request.Method.POST,Urls.URL_HAWKER_RESEND_OTP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            String str = obj.getString("data");
                            JSONObject jsoObject = new JSONObject(str);

                            if (jsoObject.getString("status").equals("0")) {
                                /*OTP NOT MATCHED*/

                            }else {

                                /*OTP MATCHED*/
                                CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(),
                                        "OTP has been sent to you registered mobile number", MessageConstant.toast_warning);
                            }

                        } catch(JSONException e){
                            e.printStackTrace();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Server Respond Error! Please Try Later", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("device_id", pdevice_id);
                params.put("mobile_no", number);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQue(stringRequest);
    }

    //-----------------------OTP WILL Expire AFTER 2 MIN.-----------------------------------------------------

    public void fun_OTPExpire(final Context applicationContext, final String pdevice_id, final String number) {

        requestQueue = VolleySingleton.getInstance(applicationContext).getRequestQueue();
        //if everything is fine
        StringRequest stringRequest = new StringRequest(Request.Method.POST,Urls.URL_HAWKER_EXPIRE_OTP,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            //converting response to json object
                            JSONObject obj = new JSONObject(response);
                            String str = obj.getString("data");
                            JSONObject jsoObject = new JSONObject(str);
                            if (jsoObject.getString("status").equals("0")) {
                                /*OTP NOT MATCHED*/
                                fun_OTPExpire(getApplicationContext(),SharedPrefrence_Login.getPdevice_id(),SharedPrefrence_Seller.getHowkerContactNumber());
                            }else {
                                /*OTP MATCHED*/
                                // CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Your OTP Code has been expired", MessageConstant.toast_warning);
                            }
                        } catch(JSONException e){
                            e.printStackTrace();
                        }
                    }
                },

                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "Server Respond Error! Please Try Later", Toast.LENGTH_SHORT).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("device_id", pdevice_id);
                params.put("mobile_no", number);
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQue(stringRequest);
    }


    @Override
    public void onFocusChange(View v, boolean hasFocus) {
        final int id = v.getId();
        switch (id) {
            case R.id.pin_first_edittext:
                if (hasFocus) {
                    setFocus(mPinHiddenEditText);
                    showSoftKeyboard(mPinHiddenEditText);
                }
                break;

            case R.id.pin_second_edittext:
                if (hasFocus) {
                    setFocus(mPinHiddenEditText);
                    showSoftKeyboard(mPinHiddenEditText);
                }
                break;

            case R.id.pin_third_edittext:
                if (hasFocus) {
                    setFocus(mPinHiddenEditText);
                    showSoftKeyboard(mPinHiddenEditText);
                }
                break;

            case R.id.pin_forth_edittext:
                if (hasFocus) {
                    setFocus(mPinHiddenEditText);
                    showSoftKeyboard(mPinHiddenEditText);
                }
                break;

            default:
                break;
        }
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            final int id = v.getId();
            switch (id) {
                case R.id.pin_hidden_edittext:
                    if (keyCode == KeyEvent.KEYCODE_DEL) {
                        if (mPinHiddenEditText.getText().length() == 4)
                            mPinForthDigitEditText.setText("");
                        else if (mPinHiddenEditText.getText().length() == 3)
                            mPinThirdDigitEditText.setText("");
                        else if (mPinHiddenEditText.getText().length() == 2)
                            mPinSecondDigitEditText.setText("");
                        else if (mPinHiddenEditText.getText().length() == 1)
                            mPinFirstDigitEditText.setText("");

                        if (mPinHiddenEditText.length() > 0)
                            mPinHiddenEditText.setText(mPinHiddenEditText.getText().subSequence(0, mPinHiddenEditText.length() - 1));

                        return true;
                    }

                    break;

                default:
                    return false;
            }
        }

        return false;
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        setDefaultPinBackground(mPinFirstDigitEditText);
        setDefaultPinBackground(mPinSecondDigitEditText);
        setDefaultPinBackground(mPinThirdDigitEditText);
        setDefaultPinBackground(mPinForthDigitEditText);
        if (s.length() == 0) {
            setFocusedPinBackground(mPinFirstDigitEditText);
            mPinFirstDigitEditText.setText("");
            btnSubmit.setVisibility(View.VISIBLE);
        } else if (s.length() == 1) {
            setFocusedPinBackground(mPinSecondDigitEditText);
            mPinFirstDigitEditText.setText(s.charAt(0) + "");
            mPinSecondDigitEditText.setText("");
            mPinThirdDigitEditText.setText("");
            mPinForthDigitEditText.setText("");
            btnSubmit.setVisibility(View.VISIBLE);
//            mPinFifthDigitEditText.setText("");
        } else if (s.length() == 2) {
            setFocusedPinBackground(mPinThirdDigitEditText);
            mPinSecondDigitEditText.setText(s.charAt(1) + "");
            mPinThirdDigitEditText.setText("");
            mPinForthDigitEditText.setText("");
            btnSubmit.setVisibility(View.VISIBLE);
            // mPinFifthDigitEditText.setText("");
        } else if (s.length() == 3) {
            setFocusedPinBackground(mPinForthDigitEditText);
            mPinThirdDigitEditText.setText(s.charAt(2) + "");
            mPinForthDigitEditText.setText("");
            btnSubmit.setVisibility(View.VISIBLE);
            //   mPinFifthDigitEditText.setText("");
        } else if (s.length() == 4) {
            setFocusedPinBackground(mPinFifthDigitEditText);
            mPinForthDigitEditText.setText(s.charAt(3) + "");
            btnSubmit.setVisibility(View.VISIBLE);
            //  mPinFifthDigitEditText.setText("");
            hideSoftKeyboard(mPinFifthDigitEditText);
        } /*else if (s.length() == 5) {
            setDefaultPinBackground(mPinFifthDigitEditText);
            mPinFifthDigitEditText.setText(s.charAt(4) + "");

            hideSoftKeyboard(mPinFifthDigitEditText);
        }*/
    }

    /**
     * Sets default PIN background.
     *
     * @param editText edit text to change
     */
    private void setDefaultPinBackground(EditText editText) {
        //   setViewBackground(editText, getResources().getDrawable(R.drawable.textfield_default_holo_light));
    }

    /**
     * Sets focus on a specific EditText field.
     *
     * @param editText EditText to set focus on
     */
    public static void setFocus(EditText editText) {
        if (editText == null)
            return;

        editText.setFocusable(true);
        editText.setFocusableInTouchMode(true);
        editText.requestFocus();
    }

    /**
     * Sets focused PIN field background.
     *
     * @param editText edit text to change
     */
    private void setFocusedPinBackground(EditText editText) {
        //  setViewBackground(editText, getResources().getDrawable(R.drawable.textfield_focused_holo_light));
    }

    /**
     * Sets listeners for EditText fields.
     */
    private void setPINListeners() {
        mPinHiddenEditText.addTextChangedListener(this);

        mPinFirstDigitEditText.setOnFocusChangeListener(this);
        mPinSecondDigitEditText.setOnFocusChangeListener(this);
        mPinThirdDigitEditText.setOnFocusChangeListener(this);
        mPinForthDigitEditText.setOnFocusChangeListener(this);
        //  mPinFifthDigitEditText.setOnFocusChangeListener(this);

        mPinFirstDigitEditText.setOnKeyListener(this);
        mPinSecondDigitEditText.setOnKeyListener(this);
        mPinThirdDigitEditText.setOnKeyListener(this);
        mPinForthDigitEditText.setOnKeyListener(this);
        //  mPinFifthDigitEditText.setOnKeyListener(this);
        mPinHiddenEditText.setOnKeyListener(this);
    }

    /**
     * Sets background of the view.
     * This method varies in implementation depending on Android SDK version.
     *
     * @param view       View to which set background
     * @param background Background to set to view
     */
    @SuppressWarnings("deprecation")
    public void setViewBackground(View view, Drawable background) {
        if (view == null || background == null)
            return;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackground(background);
        } else {
            view.setBackgroundDrawable(background);
        }
    }

    /**
     * Shows soft keyboard.
     *
     * @param editText EditText which has focus
     */
    public void showSoftKeyboard(EditText editText) {
        if (editText == null)
            return;

        InputMethodManager imm = (InputMethodManager) getSystemService(Service.INPUT_METHOD_SERVICE);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        imm.showSoftInput(editText, 0);
    }

    /**
     * Custom LinearLayout with overridden onMeasure() method
     * for handling software keyboard show and hide events.
     */
    public class MainLayout extends LinearLayout {

        public MainLayout(Context context, AttributeSet attributeSet) {
            super(context, attributeSet);
            getSupportActionBar().hide();
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            inflater.inflate(R.layout.activity_registration_confirmation, this);
        }

        @Override
        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            final int proposedHeight = MeasureSpec.getSize(heightMeasureSpec);
            final int actualHeight = getHeight();

            Log.d("TAG", "proposed: " + proposedHeight + ", actual: " + actualHeight);

            if (actualHeight >= proposedHeight) {
                // Keyboard is shown
                if (mPinHiddenEditText.length() == 0)
                    setFocusedPinBackground(mPinFirstDigitEditText);
                else
                    setDefaultPinBackground(mPinFirstDigitEditText);
            }

            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        yourCountDownTimer.cancel();
    }
}
