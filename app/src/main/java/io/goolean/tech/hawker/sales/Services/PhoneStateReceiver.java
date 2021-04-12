package io.goolean.tech.hawker.sales.Services;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Handler;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import io.goolean.tech.hawker.sales.Constant.ConnectionDetector;
import io.goolean.tech.hawker.sales.Constant.Dialog_;
import io.goolean.tech.hawker.sales.Storage.SharedPrefrence_Login;
import io.goolean.tech.hawker.sales.Storage.SharedPrefrence_Seller;
import io.goolean.tech.hawker.sales.View.HomeActivity;
import io.goolean.tech.hawker.sales.View.RegistrationConfirmationActivity;
import io.goolean.tech.hawker.sales.View.VerificationActivity;
import io.michaelrocks.libphonenumber.android.NumberParseException;
import io.michaelrocks.libphonenumber.android.PhoneNumberUtil;
import io.michaelrocks.libphonenumber.android.Phonenumber;

public class PhoneStateReceiver extends BroadcastReceiver {
    String phoneNumber;
    TelephonyManager telephony;
    HomeActivity homeActivity = new HomeActivity();
    RegistrationConfirmationActivity registrationConfirmationActivity = new RegistrationConfirmationActivity();
    public int counter=0;
    private Timer timer;
    private TimerTask timerTask;
    int service_time =  10000;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    CountDownTimer cTimer = null;
    @Override
    public void onReceive(final Context context, Intent intent) {
        SharedPrefrence_Seller.getDataHawkerNumberMatch(context);
        SharedPrefrence_Seller.getHowkerRegistration(context);
        SharedPrefrence_Seller.getIncommingNumber(context);
         sharedPreferences= context.getSharedPreferences("TIMER",Context.MODE_PRIVATE);
         editor = sharedPreferences.edit();
        try {
            String state = intent.getStringExtra(TelephonyManager.EXTRA_STATE);
            TelephonyManager telephony = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
            telephony.listen(new PhoneStateListener(){
                @Override
                public void onCallStateChanged(int state, String incomingNumber) {
                super.onCallStateChanged(state, incomingNumber);
                System.out.println("incomingNumber : "+incomingNumber);
                try {
                    // phone must begin with '+'
                    PhoneNumberUtil phoneUtil = PhoneNumberUtil.createInstance(context);
                    Phonenumber.PhoneNumber numberProto = phoneUtil.parse(incomingNumber, "");
                    int countryCode = numberProto.getCountryCode();
                    long nationalNumber = numberProto.getNationalNumber();
                    phoneNumber = nationalNumber+"";
                    SharedPrefrence_Seller.setIncommingNumber(context,phoneNumber,"1");
                    Log.i("CODE",  countryCode+"");
                    Log.i("MOBILE NUMBER", nationalNumber+"");
                } catch (NumberParseException e) {
                    System.err.println("NumberParseException was thrown: " + e.toString());
                }
                }
            },PhoneStateListener.LISTEN_CALL_STATE);

            if(state.equals(TelephonyManager.EXTRA_STATE_RINGING)){
                startTimer();
            }
            if(state.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)){

            }
            if (state.equals(TelephonyManager.EXTRA_STATE_IDLE)){
                cancelTimer();
                String s = sharedPreferences.getString("timing","");
                int counter = Integer.parseInt(s);
                if(counter==1||counter==2||counter==3||counter==4) {
                    SharedPrefrence_Seller.setIncommingNumber(context,SharedPrefrence_Seller.getHowkerShopeMobileNumber(),"1");
                    homeActivity.edt_shopMobileNumber_id.setText(SharedPrefrence_Seller.getHowkerShopeMobileNumber());
                    homeActivity.edt_mobile_number_contact_id.setText(SharedPrefrence_Seller.getHowkerShopeMobileNumber());
                }else if (counter== -9) {
                    if(SharedPrefrence_Seller.getHawkerNumberMatch().equals("Home")){
                        SharedPrefrence_Seller.setIncommingNumber(context,SharedPrefrence_Seller.getHAWKERINCOMMINGNUMBER(),"1");
                        homeActivity.edt_shopMobileNumber_id.setText(SharedPrefrence_Seller.getHAWKERINCOMMINGNUMBER());
                        homeActivity.edt_mobile_number_contact_id.setText(SharedPrefrence_Seller.getHAWKERINCOMMINGNUMBER());
                    }else if(SharedPrefrence_Seller.getHawkerNumberMatch().equals("Verfication")){
                        if(SharedPrefrence_Seller.getHowkerContactNumber().equals(SharedPrefrence_Seller.getHAWKERINCOMMINGNUMBER())){
                            registrationConfirmationActivity.tvMobileNumber.setText("Number is verified");
                            registrationConfirmationActivity.tvMobileNumber.setTextColor(Color.parseColor("#00b47c"));
                            registrationConfirmationActivity.btn_verifybycall.setVisibility(View.VISIBLE);
                            SharedPrefrence_Seller.setIncommingNumber(context,SharedPrefrence_Seller.getHAWKERINCOMMINGNUMBER(),"1");
                        }else {
                            registrationConfirmationActivity.tvMobileNumber.setText("Number is not verified");
                        }
                    }else {
                        homeActivity.edt_shopMobileNumber_id.setText(SharedPrefrence_Seller.getHAWKERINCOMMINGNUMBER());
                        homeActivity.edt_mobile_number_contact_id.setText(SharedPrefrence_Seller.getHAWKERINCOMMINGNUMBER());
                    }
                }
                //======Open Dialog
                homeActivity.confirmationNumber(context,phoneNumber,"DONE");
            }
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }


// **********************************************************************************************************************


    void startTimer() {
        cTimer = new CountDownTimer(4000, 1000) {
            public void onTick(long millisUntilFinished) {
                String str_timer =String.format("%d",
                TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                editor.putString("timing",str_timer);
                editor.commit();
            }
            public void onFinish() {
                editor.putString("timing","-9");
                editor.commit();
            }
        };
        cTimer.start();
    }
    void cancelTimer() {
        if(cTimer!=null)
            cTimer.cancel();
    }
}
