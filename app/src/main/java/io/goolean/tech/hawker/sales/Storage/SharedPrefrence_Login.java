package io.goolean.tech.hawker.sales.Storage;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefrence_Login {

    public static Context _context;
    public static SharedPreferences sharedPrefrencesLogin = null;
    public static SharedPreferences sharedPrefrencesCheckNumber = null;
    public static String number, type;
    public static String mnumber, mpassword, mdevice_id, mnotification_id, mname, mid, mtype;
    public static String sToken, sONOFF, HawkerStatus;
    public static String sPhoneType;


    public SharedPrefrence_Login(Context context) {
        this._context = context;
        sharedPrefrencesLogin = _context.getSharedPreferences("HAWKER_SALES_CHECK_NUMBER", Context.MODE_PRIVATE);
        sharedPrefrencesCheckNumber = _context.getSharedPreferences("HAWKER_SALES_LOGIN", Context.MODE_PRIVATE);
    }

    public static void savePhoneType(Context applicationContext, String PhoneType) {
        _context = applicationContext;
        sharedPrefrencesLogin = _context.getSharedPreferences("HAWKER_SALES_LOGIN", Context.MODE_PRIVATE);
        sPhoneType = PhoneType;
        SharedPreferences.Editor editor = sharedPrefrencesLogin.edit();
        editor.putString("sPhoneType", sPhoneType);
        editor.commit();
    }

    public static void getPhoneType(Context applicationContext) {
        _context = applicationContext;
        sharedPrefrencesLogin = _context.getSharedPreferences("HAWKER_SALES_LOGIN", Context.MODE_PRIVATE);
        sPhoneType = sharedPrefrencesLogin.getString("sPhoneType", "");
    }

    public static String getsPhoneType() {
        return sPhoneType;
    }

    //===============================================SALES LOGIN SHAREDPREFRENCES========================================================

    public static void saveDataLogin(Context applicationContext, String number, String device_id, String notification_id,
                                     String name, String id, String type) {
        _context = applicationContext;
        sharedPrefrencesLogin = _context.getSharedPreferences("HAWKER_SALES_LOGIN", Context.MODE_PRIVATE);
        mnumber = number;
        mdevice_id = device_id;
        mnotification_id = notification_id;
        mname = name;
        mid = id;
        mtype = type;
        SharedPreferences.Editor editor = sharedPrefrencesLogin.edit();
        editor.putString("NUMBER", mnumber);
        editor.putString("DEVICE_ID", mdevice_id);
        editor.putString("NOTIFICATION_ID", mnotification_id);
        editor.putString("NAME", mname);
        editor.putString("ID", mid);
        editor.putString("TYPE", mtype);
        editor.commit();
    }

    public static void getDataLogin(Context applicationContext) {
        _context = applicationContext;
        sharedPrefrencesLogin = _context.getSharedPreferences("HAWKER_SALES_LOGIN", Context.MODE_PRIVATE);
        mnumber = sharedPrefrencesLogin.getString("NUMBER", "");
        mdevice_id = sharedPrefrencesLogin.getString("DEVICE_ID", "");
        mnotification_id = sharedPrefrencesLogin.getString("NOTIFICATION_ID", "");
        mname = sharedPrefrencesLogin.getString("NAME", "");
        mid = sharedPrefrencesLogin.getString("ID", "");
        mtype = sharedPrefrencesLogin.getString("TYPE", "");
    }

    public static String getPnumber() {
        return mnumber;
    }

    public static String getPdevice_id() {
        return mdevice_id;
    }

    public static String getPnotification_id() {
        return mnotification_id;
    }

    public static String getPname() {
        return mname;
    }

    public static String getPid() {
        return mid;
    }

    public static String getPtype() {
        return mtype;
    }

    //===============================ALL SHARED PREFRENCES CLEAR==========================================================================

    public static void ClearSharedPrefrenc(Context applicationContext) {
        _context = applicationContext;
        sharedPrefrencesLogin = _context.getSharedPreferences("HAWKER_SALES_LOGIN", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPrefrencesLogin.edit();
        editor.putString("NUMBER", null);
        editor.putString("DEVICE_ID", null);
        editor.putString("NAME", null);
        editor.putString("ID", null);
        editor.putString("TYPE", null);
        editor.putString("NOTIFICATION_ID", null);
        editor.putString("HawkerStatus", null);
        editor.commit();
        editor.clear();
        editor.commit();
    }

    /*________________________________________________________________________________________________*/

    public static void saveConfirmStatus(Context applicationContext, String status) {
        _context = applicationContext;
        sharedPrefrencesLogin = _context.getSharedPreferences("HAWKER_SALES_LOGIN", Context.MODE_PRIVATE);
        HawkerStatus = status;
        SharedPreferences.Editor editor = sharedPrefrencesLogin.edit();
        editor.putString("HawkerStatus", HawkerStatus);
        editor.commit();
    }

    public static void getConfirmStatus(Context applicationContext) {
        _context = applicationContext;
        sharedPrefrencesLogin = _context.getSharedPreferences("HAWKER_SALES_LOGIN", Context.MODE_PRIVATE);
        HawkerStatus = sharedPrefrencesLogin.getString("HawkerStatus", "");

    }

    public static String getHawkerStatus() {
        return HawkerStatus;
    }

    /*________________________________________________________________________________________________*/

    public static void saveToken(Context applicationContext, String token) {
        _context = applicationContext;
        sharedPrefrencesLogin = _context.getSharedPreferences("HAWKER_SALES_LOGIN", Context.MODE_PRIVATE);
        sToken = token;
        SharedPreferences.Editor editor = sharedPrefrencesLogin.edit();
        editor.putString("FCM_TOKEN", sToken);
        editor.commit();
    }

    public static void getTokenS(Context applicationContext) {
        _context = applicationContext;
        sharedPrefrencesLogin = _context.getSharedPreferences("HAWKER_SALES_LOGIN", Context.MODE_PRIVATE);
        sToken = sharedPrefrencesLogin.getString("FCM_TOKEN", "");
    }

    public static String getTokenS() {
        return sToken;
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////

    public static void saveONOFF(Context applicationContext, String sStatus) {
        _context = applicationContext;
        sharedPrefrencesLogin = _context.getSharedPreferences("HAWKER_SALES_LOGIN", Context.MODE_PRIVATE);
        sONOFF = sStatus;
        SharedPreferences.Editor editor = sharedPrefrencesLogin.edit();
        editor.putString("S_ON_OFF", sONOFF);
        editor.commit();
    }


    public static void getONOFF(Context applicationContext) {
        _context = applicationContext;
        sharedPrefrencesLogin = _context.getSharedPreferences("HAWKER_SALES_LOGIN", Context.MODE_PRIVATE);
        sONOFF = sharedPrefrencesLogin.getString("S_ON_OFF", "");

    }

    public static String getONOFF() {
        return sONOFF;
    }
}
