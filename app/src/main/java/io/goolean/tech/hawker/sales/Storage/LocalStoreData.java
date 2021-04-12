package io.goolean.tech.hawker.sales.Storage;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;


public class LocalStoreData {
    static SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context context;
    int PRIVATE_MODE = 0;

    public LocalStoreData(Context context) {
        this.context = context;
        pref = context.getSharedPreferences(Constants.PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public static void init(Context context) {
        if (pref == null)
            pref = context.getSharedPreferences(context.getPackageName(), Activity.MODE_PRIVATE);
    }

    public void savefcmtoken(String sToken) {
        editor.putString(Constants.FCM_TOKEN, sToken);
        editor.commit();
    }

    public void savemobilenumber(String sMobileNumber) {
        editor.putString(Constants.MOBILE_NUMBER, sMobileNumber);
        editor.commit();
    }

    public void saveverifyData(String sMobileNumber, String sId, String sName, String sType, String sStatus) {
        editor.putString(Constants.MOBILE_NUMBER, sMobileNumber);
        editor.putString(Constants.ID, sId);
        editor.putString(Constants.NAME, sName);
        editor.putString(Constants.TYPE, sType);
        editor.putString(Constants.STATUS, sStatus);
        editor.commit();
    }


    public void saveHawkerCode(String sHawkerCode) {
        editor.putString(Constants.HAWKER_CODE, sHawkerCode);
        editor.commit();
    }

    public void saveImgLatLon(String slatsLon) {
        editor.putString(Constants.IMG_LAT_LON, slatsLon);
        editor.commit();
    }


    public static String getValue(String key, String defValue) {
        return pref.getString(key, defValue);
    }


    public void clearAllData() {
        editor.putString(Constants.MOBILE_NUMBER, null);
        editor.putString(Constants.ID, null);
        editor.putString(Constants.NAME, null);
        editor.putString(Constants.TYPE, null);
        editor.putString(Constants.STATUS, null);
        editor.commit();
        editor.clear();
    }


}
