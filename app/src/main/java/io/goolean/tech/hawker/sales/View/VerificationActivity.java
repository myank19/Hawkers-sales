package io.goolean.tech.hawker.sales.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import io.goolean.tech.hawker.sales.Constant.ConnectionDetector;
import io.goolean.tech.hawker.sales.Constant.Dialog_;
import io.goolean.tech.hawker.sales.Constant.MessageConstant;
import io.goolean.tech.hawker.sales.Constant.Singleton.CallbackSnakebarModel;
import io.goolean.tech.hawker.sales.Constant.Urls;
import io.goolean.tech.hawker.sales.Networking.VolleySingleton;
import io.goolean.tech.hawker.sales.R;
import io.goolean.tech.hawker.sales.Storage.SharedPrefrence_Login;
import io.goolean.tech.hawker.sales.Utililty.OtpReader.SmsListener;
import io.goolean.tech.hawker.sales.Utililty.OtpReader.SmsReceiver;


public class VerificationActivity extends AppCompatActivity implements View.OnFocusChangeListener, View.OnKeyListener, TextWatcher {

    private EditText mPinFirstDigitEditText;
    private EditText mPinSecondDigitEditText;
    private EditText mPinThirdDigitEditText;
    private EditText mPinForthDigitEditText;
    private EditText mPinFifthDigitEditText;
    private EditText mPinHiddenEditText;
    private Button btnSubmit;
    private TextView tvCount;
    private TextView tvResendOTP;
    private RequestQueue requestQueue;
    private String otp1, otp2, otp3, otp4;
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
        btnSubmit = (Button) findViewById(R.id.btn_submit_id);
        tvCount = (TextView) findViewById(R.id.tv_timer);
        tvResendOTP = (TextView) findViewById(R.id.tv_resend_otp);
        mPinFirstDigitEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        mPinSecondDigitEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        mPinThirdDigitEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        mPinForthDigitEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
        SharedPrefrence_Login.getDataLogin(getApplicationContext());

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(new MainLayout(this, null));

        init();
        setPINListeners();
        counterTimeStart();
        SharedPrefrence_Login.getConfirmStatus(VerificationActivity.this);

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
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (ConnectionDetector.getConnectionDetector(getApplicationContext()).isConnectingToInternet() == true) {
                            fun_OTP(messageText, SharedPrefrence_Login.getPdevice_id(), SharedPrefrence_Login.getPnumber());
                        } else {
                            CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Please check your internet connection", MessageConstant.toast_warning);
                        }
                    }
                }, 1000);

            }
        });
        btnSubmit.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                String otp = mPinFirstDigitEditText.getText().toString() + mPinSecondDigitEditText.getText().toString() + mPinThirdDigitEditText.getText().toString()
                        + mPinForthDigitEditText.getText().toString();
                if (ConnectionDetector.getConnectionDetector(getApplicationContext()).isConnectingToInternet() == true) {

                    fun_OTP(otp, SharedPrefrence_Login.getPdevice_id(), SharedPrefrence_Login.getPnumber());
                } else {
                    CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Please check your internet connection", MessageConstant.toast_warning);
                }
            }
        });

        tvResendOTP.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                counterStart();
                //tvResendOTP.setVisibility(View.GONE);
                if (ConnectionDetector.getConnectionDetector(getApplicationContext()).isConnectingToInternet() == true) {
                    fun_OTPResend(SharedPrefrence_Login.getPdevice_id(), SharedPrefrence_Login.getPnumber());
                } else {
                    CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Please check your internet connection", MessageConstant.toast_warning);

                }
            }
        });
    }

    private void counterStart() {
        yourCountDownTimer = new CountDownTimer(60000, 1000) { // adjust the milli seconds here

            public void onTick(long millisUntilFinished) {
                tvCount.setText("" + String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {
                tvCount.setText("");
                tvResendOTP.setVisibility(View.VISIBLE);
                // btnSubmit.setVisibility(View.GONE);
                if (ConnectionDetector.getConnectionDetector(getApplicationContext()).isConnectingToInternet() == true) {
                    fun_OTPExpire(getApplicationContext(), SharedPrefrence_Login.getPdevice_id(), SharedPrefrence_Login.getPnumber());
                } else {
                    Dialog_.getInstanceDialog(VerificationActivity.this).dialog_OTPInternet(VerificationActivity.this, SharedPrefrence_Login.getPdevice_id(), SharedPrefrence_Login.getPnumber());
                    //CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Please check your internet connection", MessageConstant.toast_warning);
                }
            }
        }.start();
    }

    private void counterTimeStart() {
        yourCountDownTimer = new CountDownTimer(60000, 1000) { // adjust the milli seconds here
            public void onTick(long millisUntilFinished) {
                tvCount.setText("" + String.format("%d min, %d sec",
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) -
                                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))));
            }

            public void onFinish() {
                tvCount.setText("");
                tvResendOTP.setVisibility(View.VISIBLE);
                if (ConnectionDetector.getConnectionDetector(getApplicationContext()).isConnectingToInternet() == true) {
                    fun_OTPExpire(getApplicationContext(), SharedPrefrence_Login.getPdevice_id(), SharedPrefrence_Login.getPnumber());
                } else {
                    Dialog_.getInstanceDialog(VerificationActivity.this).dialog_OTPInternet(VerificationActivity.this, SharedPrefrence_Login.getPdevice_id(), SharedPrefrence_Login.getPnumber());
                    //CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Please check your internet connection", MessageConstant.toast_warning);
                }
            }
        }.start();
    }

    //-----------------------SUBMIT OTP-----------------------------------------------------
    private void fun_OTP(final String otp, final String pdevice_id, final String number) {
        requestQueue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        //if everything is fine
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.URL_SALES_VERIFICATION_OTP,
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
                                // SharedPrefrence_Login.fun_checkOTP(getApplicationContext(),jsoObject.getString("status"));
                                SharedPrefrence_Login.saveConfirmStatus(VerificationActivity.this, "");
                                CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Enter vaild 4 digit code", MessageConstant.toast_warning);
                            } else {
                                yourCountDownTimer.cancel();
                                /*OTP MATCHED*/
                                SharedPrefrence_Login.saveConfirmStatus(VerificationActivity.this, "1");
                                Intent ii = new Intent(getApplicationContext(), HomeActivity.class);
                                finish();
                                ii.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(ii);
                            }
                        } catch (JSONException e) {
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
                return params;
            }
        };
        VolleySingleton.getInstance(this).addToRequestQue(stringRequest);
    }


    //-----------------------OTP RESEND-----------------------------------------------------
    private void fun_OTPResend(final String pdevice_id, final String number) {

        requestQueue = VolleySingleton.getInstance(getApplicationContext()).getRequestQueue();
        //if everything is fine
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.URL_SALES_RESEND_OTP,
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

                            } else {
                                /*OTP MATCHED*/
                                CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "OTP has been sent to you registered mobile number", MessageConstant.toast_warning);

                            }

                        } catch (JSONException e) {
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
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Urls.URL_SALES_OTP_EXPIRE,
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
                                fun_OTPExpire(getApplicationContext(), SharedPrefrence_Login.getPdevice_id(), SharedPrefrence_Login.getPnumber());
                            } else {
                                /*OTP MATCHED*/
                                // CallbackSnakebarModel.getInstance().SnakebarMessage(getApplicationContext(), "Your OTP Code has been expired", MessageConstant.toast_warning);
                            }
                        } catch (JSONException e) {
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
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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
            //btnSubmit.setVisibility(View.GONE);

        } else if (s.length() == 1) {
            setFocusedPinBackground(mPinSecondDigitEditText);
            mPinFirstDigitEditText.setText(s.charAt(0) + "");
            mPinSecondDigitEditText.setText("");
            mPinThirdDigitEditText.setText("");
            mPinForthDigitEditText.setText("");
            //  btnSubmit.setVisibility(View.GONE);
//            mPinFifthDigitEditText.setText("");
        } else if (s.length() == 2) {
            setFocusedPinBackground(mPinThirdDigitEditText);
            mPinSecondDigitEditText.setText(s.charAt(1) + "");
            mPinThirdDigitEditText.setText("");
            mPinForthDigitEditText.setText("");
            // btnSubmit.setVisibility(View.GONE);
            // mPinFifthDigitEditText.setText("");
        } else if (s.length() == 3) {
            setFocusedPinBackground(mPinForthDigitEditText);
            mPinThirdDigitEditText.setText(s.charAt(2) + "");
            mPinForthDigitEditText.setText("");
            //btnSubmit.setVisibility(View.GONE);
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
            inflater.inflate(R.layout.activity_verification, this);
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
