package io.goolean.tech.hawker.sales.Storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;


public class SharedPrefrence_Seller {

    public static Context context;
    public static String number,name,fixr_category_id,fixr_category_status,fixr_shop_id,fixr_id,fixr_subcategory_id,fixr_supersubcategory_id,fixr_profile_image,HawkerQuestion;
    public static SharedPreferences sharedPrefrencesFixerRegistration = null;
    public static String HowkerName, HowkerType, HowkerResidentialAddress, HowkerMobileNumber, HowkerPassword, HowkerBussinessName, HowkerStartTime,
            HowkerCloseTime, HowkerShopName, HowkerShopAdress, HowkerShopeMobileNumber, HowkerSelectedService, HowkerProfileImage,HowkerIndentityProofImage,
            HowkerAddressProofImage, HowkerShopImageOne, HowkerShopImageTwo, HowkerGPSID, HowkerLatitude, HowkerLongitude, HowkerGPSMODE, HowkerSalesID,
            HowkerRegisterAdress, HowkerStateCity,HowkerServiceStartTime,HowkerServiceEndTime,strNameType,HowkerGender,HowkerSubHawkerName, HowkerSubUserType,
            HowkerMobileType, HowkerSmartPhone,HowkerDays, HowkerStartDate,HowkerEndDate,HowkerGSTNumber,HowkerTypeOtherDetail,HowkerContactNumber,
            iSubHawkerNamePosition,strHawker_type_Code,strOther_Sub_HawkerType,strPhoneType,
            PRO_LAT ,PRO_LON ,SHOP1_LAT ,SHOP1_LON ,SHOP2_LAT ,SHOP2_LON,ADDRESS_LAT ,ADDRESS_LON ,IDENTITY_LAT ,IDENTITY_LON,HawkerFixSeasonalYesNo,
            strOtherBusinessDetails,MenuImage,MENU_LAT,MENU_LON;

    public static String hawker_working_day,HawkerStartTime,HawkerEndTime,HAWKERSTARTTIME,HAWKERSTOPTIME;
    public static String HawkerINCOMMINGNUMBER,HAWKERCALLSTATUS,IMAGELATITUDELONGITUDE;

    public static String HawkerSalesID, TempHowkerName,TempHowkerGender, TempHowkerType, TempHowkerSubType, TempHawkerAddress, TempHawkerMobileNumber,
            TempHawkerCity, TempHawkerState, TempHawkerLatitude, TempHawkerLongitude,TempHawkerSubTypePosition,TempHawkerSubType,TempHawkerTypeCode,
    TempHawkerStartDate, TempHawkerEndDate,TempString,TempHawkerMobilemodelType,TempHawkerOtherBisinessDetail,TempFixSeasonalyesno;

    public static String HawkerNumberMatch,HawkerOtherService;
    public static String strIndentityProofImage2,strMenuImage1,strMenuImage2,strMenuImage3,ActivityStatus;
    public static String PHONE_TYPE;


    public SharedPrefrence_Seller(Context context){
        this.context = context;
        sharedPrefrencesFixerRegistration = context.getSharedPreferences("HAWKER_SALES_BASIC_REGISTRATION", Context.MODE_PRIVATE);
    }


    public static void saveHawkerOtherService(Context applicationContext, String OtherService) {
        if(sharedPrefrencesFixerRegistration == null) {
            context = applicationContext;
            sharedPrefrencesFixerRegistration = context.getSharedPreferences("HAWKER_SALES_BASIC_REGISTRATION", Context.MODE_PRIVATE);
        }
        context=applicationContext;
        HawkerOtherService= OtherService;

        SharedPreferences.Editor editor = sharedPrefrencesFixerRegistration.edit();
        editor.putString("HawkerOtherService", HawkerOtherService);
        editor.commit();
    }

    public static void getHawkerOtherServiceData(Context applicationContext) {
        if(sharedPrefrencesFixerRegistration == null) {
            context = applicationContext;
            sharedPrefrencesFixerRegistration = context.getSharedPreferences("HAWKER_SALES_BASIC_REGISTRATION", Context.MODE_PRIVATE);
        }
        HawkerOtherService=  sharedPrefrencesFixerRegistration.getString("HawkerOtherService","");
    }

    public static String getHawkerOtherService() { return HawkerOtherService; }





    /*+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++*/


    public static void saveHawkerNumberMatch(Context applicationContext, String NumberMatch) {
        if(sharedPrefrencesFixerRegistration == null) {
            context = applicationContext;
            sharedPrefrencesFixerRegistration = context.getSharedPreferences("HAWKER_SALES_BASIC_REGISTRATION", Context.MODE_PRIVATE);
        }
        context=applicationContext;
        HawkerNumberMatch= NumberMatch;

        SharedPreferences.Editor editor = sharedPrefrencesFixerRegistration.edit();
        editor.putString("HawkerNumberMatch", HawkerNumberMatch);
        editor.commit();
    }

    public static void getDataHawkerNumberMatch(Context applicationContext) {
        if(sharedPrefrencesFixerRegistration == null) {
            context = applicationContext;
            sharedPrefrencesFixerRegistration = context.getSharedPreferences("HAWKER_SALES_BASIC_REGISTRATION", Context.MODE_PRIVATE);
        }
        HawkerNumberMatch=  sharedPrefrencesFixerRegistration.getString("HawkerNumberMatch","");
    }

    public static String getHawkerNumberMatch() { return HawkerNumberMatch; }






    /*_____________________________________________________________________________________________________________________*/


    public static void saveIMAGELATITUDELOGITUDE(Context applicationContext, String latlon) {
        if(sharedPrefrencesFixerRegistration == null) {
            context = applicationContext;
            sharedPrefrencesFixerRegistration = context.getSharedPreferences("HAWKER_SALES_BASIC_REGISTRATION", Context.MODE_PRIVATE);
        }
        context=applicationContext;
        IMAGELATITUDELONGITUDE= latlon;

        SharedPreferences.Editor editor = sharedPrefrencesFixerRegistration.edit();
        editor.putString("IMAGELATITUDELONGITUDE", IMAGELATITUDELONGITUDE);
        editor.commit();
    }

    public static void getIMAGELATITUDELOGITUDE(Context applicationContext) {
        if(sharedPrefrencesFixerRegistration == null) {
            context = applicationContext;
            sharedPrefrencesFixerRegistration = context.getSharedPreferences("HAWKER_SALES_BASIC_REGISTRATION", Context.MODE_PRIVATE);
        }
        IMAGELATITUDELONGITUDE=  sharedPrefrencesFixerRegistration.getString("IMAGELATITUDELONGITUDE","");
    }

    public static String getIMAGELATITUDELONGITUDE() { return IMAGELATITUDELONGITUDE; }





    /*_________________________________________________________________________________________________________________________________________________________*/

    public static void setIncommingNumber(Context applicationContext, String number,String call_status) {
        if(sharedPrefrencesFixerRegistration == null) {
            context = applicationContext;
            sharedPrefrencesFixerRegistration = context.getSharedPreferences("HAWKER_SALES_BASIC_REGISTRATION", Context.MODE_PRIVATE);
        }
        context=applicationContext;
        HawkerINCOMMINGNUMBER= number;
        HAWKERCALLSTATUS= call_status;

        SharedPreferences.Editor editor = sharedPrefrencesFixerRegistration.edit();
        editor.putString("HawkerINCOMMINGNUMBER", number);
        editor.putString("HAWKERCALLSTATUS", call_status);
        editor.commit();
    }

    public static void getIncommingNumber(Context applicationContext) {
        if(sharedPrefrencesFixerRegistration == null) {
            context = applicationContext;
            sharedPrefrencesFixerRegistration = context.getSharedPreferences("HAWKER_SALES_BASIC_REGISTRATION", Context.MODE_PRIVATE);
        }
        HawkerINCOMMINGNUMBER=  sharedPrefrencesFixerRegistration.getString("HawkerINCOMMINGNUMBER","");
        HAWKERCALLSTATUS=  sharedPrefrencesFixerRegistration.getString("HAWKERCALLSTATUS","");
    }

    public static String getHAWKERINCOMMINGNUMBER() { return HawkerINCOMMINGNUMBER; }
    public static String getHAWKERCALLSTATUS() { return HAWKERCALLSTATUS; }



    /*_________________________________________________________________________________________________________________________________________________________*/
    /*  Time  */


    public static void saveStartTime(Context applicationContext, String startTime) {
        if(sharedPrefrencesFixerRegistration == null) {
            context = applicationContext;
            sharedPrefrencesFixerRegistration = context.getSharedPreferences("HAWKER_SALES_BASIC_REGISTRATION", Context.MODE_PRIVATE);
        }
        context=applicationContext;
        HAWKERSTARTTIME= startTime;

        SharedPreferences.Editor editor = sharedPrefrencesFixerRegistration.edit();
        editor.putString("HAWKERSTARTTIME", HAWKERSTARTTIME);
        editor.commit();
    }

    public static void getStartTime(Context applicationContext) {
        if(sharedPrefrencesFixerRegistration == null) {
            context = applicationContext;
            sharedPrefrencesFixerRegistration = context.getSharedPreferences("HAWKER_SALES_BASIC_REGISTRATION", Context.MODE_PRIVATE);
        }
        HAWKERSTARTTIME=  sharedPrefrencesFixerRegistration.getString("HAWKERSTARTTIME","");
    }

    public static String getHAWKERSTARTTIME() { return HAWKERSTARTTIME; }




    public static void saveStopTime(Context applicationContext,String stopTime) {
        if(sharedPrefrencesFixerRegistration == null) {
            context = applicationContext;
            sharedPrefrencesFixerRegistration = context.getSharedPreferences("HAWKER_SALES_BASIC_REGISTRATION", Context.MODE_PRIVATE);
        }
        context=applicationContext;
        HAWKERSTOPTIME= stopTime;
        SharedPreferences.Editor editor = sharedPrefrencesFixerRegistration.edit();
        editor.putString("HAWKERSTOPTIME", HAWKERSTOPTIME);
        editor.commit();
    }

    public static void getStopTime(Context applicationContext) {
        if(sharedPrefrencesFixerRegistration == null) {
            context = applicationContext;
            sharedPrefrencesFixerRegistration = context.getSharedPreferences("HAWKER_SALES_BASIC_REGISTRATION", Context.MODE_PRIVATE);
        }
        HAWKERSTOPTIME=  sharedPrefrencesFixerRegistration.getString("HAWKERSTOPTIME","");
    }

    public static String getHAWKERSTOPTIME() {
        return HAWKERSTOPTIME;
    }




//===============================================FIXER BASIC DETAILS SHAREDPREFRENCES========================================================

    public static void saveWorkingDays(Context applicationContext, String hawker_working_day,String HawkerStartTime,String HawkerEndTime) {
        if(sharedPrefrencesFixerRegistration == null) {
            context = applicationContext;
            sharedPrefrencesFixerRegistration = context.getSharedPreferences("HAWKER_SALES_BASIC_REGISTRATION", Context.MODE_PRIVATE);
        }
        context=applicationContext;
        hawker_working_day= hawker_working_day;
        HawkerStartTime= HawkerStartTime;
        HawkerEndTime= HawkerEndTime;
        SharedPreferences.Editor editor = sharedPrefrencesFixerRegistration.edit();
        editor.putString("HAWKER_WORKING_DAYS", hawker_working_day);
        editor.putString("HAWKER_START_TIME", HawkerStartTime);
        editor.putString("HAWKER_END_TIME", HawkerEndTime);
        editor.commit();

    }

    public static void getWorkingDays(Context applicationContext) {
        if(sharedPrefrencesFixerRegistration == null) {
            context = applicationContext;
            sharedPrefrencesFixerRegistration = context.getSharedPreferences("HAWKER_SALES_BASIC_REGISTRATION", Context.MODE_PRIVATE);
        }
        hawker_working_day=  sharedPrefrencesFixerRegistration.getString("HAWKER_WORKING_DAYS","");
        HawkerStartTime=  sharedPrefrencesFixerRegistration.getString("HAWKER_START_TIME","");
        HawkerEndTime=  sharedPrefrencesFixerRegistration.getString("HAWKER_END_TIME","");
    }
    public static String getHawkerWorkingDay() {
        return hawker_working_day;
    }public static String getHawkerStartTime() {
        return HawkerStartTime;
    }public static String getHawkerEndTime() {
        return HawkerEndTime;
    }





    public static void saveHawkerQuestionData(Context applicationContext, String qustionMap) {
        if(sharedPrefrencesFixerRegistration == null)
        {
            context = applicationContext;
            sharedPrefrencesFixerRegistration = context.getSharedPreferences("HAWKER_SALES_BASIC_REGISTRATION", Context.MODE_PRIVATE);
        }

        context=applicationContext;
        HawkerQuestion= qustionMap;
        SharedPreferences.Editor editor = sharedPrefrencesFixerRegistration.edit();
        editor.putString("HawkerQuestion", HawkerQuestion);
        editor.commit();

    }

    public static void getHawkerQuestionData(Context applicationContext) {
        if(sharedPrefrencesFixerRegistration == null) {
            context = applicationContext;
            sharedPrefrencesFixerRegistration = context.getSharedPreferences("HAWKER_SALES_REGISTRATION", Context.MODE_PRIVATE);
        }
        HawkerQuestion=  sharedPrefrencesFixerRegistration.getString("HawkerQuestion","");
    }

    public static String getHawkerQuestion() {
        return HawkerQuestion;
    }








    //===================================FIXER SHOP PROFILE IMAGE DATA ==========================================================================

    public static void saveSharedPrefrencesFixerProfileImageData(Context applicationContext, String fixer_profile_image) {
        if(sharedPrefrencesFixerRegistration == null)
        {
            context = applicationContext;
            sharedPrefrencesFixerRegistration = context.getSharedPreferences("HAWKER_SALES_BASIC_REGISTRATION", Context.MODE_PRIVATE);
        }

        context=applicationContext;
        fixr_profile_image=fixer_profile_image;

        SharedPreferences.Editor editor = sharedPrefrencesFixerRegistration.edit();
        editor.putString("FIXER_PROFILE_IMAGE", fixr_profile_image);
        editor.commit();

    }
    public static void getSharedPrefrencesFixerProfileImageData(Context applicationContext) {

        if(sharedPrefrencesFixerRegistration == null)
        {
            context = applicationContext;
            sharedPrefrencesFixerRegistration = context.getSharedPreferences("HAWKER_SALES_BASIC_REGISTRATION", Context.MODE_PRIVATE);
        }
        fixr_profile_image=  sharedPrefrencesFixerRegistration.getString("FIXER_PROFILE_IMAGE","");
      }

    public static String getFixr_profile_image() {
        return fixr_profile_image;
    }








//==========================================FIXER SHOP ID AND SHOP ID ===============================================================


    public static void saveSharedPrefrencesFixerShopId(Context applicationContext, String fixer_shop_id, String fixer_id ) {
        if(sharedPrefrencesFixerRegistration == null)
        {
            context = applicationContext;
            sharedPrefrencesFixerRegistration = context.getSharedPreferences("HAWKER_SALES_BASIC_REGISTRATION", Context.MODE_PRIVATE);
        }

        context=applicationContext;
        fixr_shop_id= fixer_shop_id;
        fixr_id= fixer_id;
        SharedPreferences.Editor editor = sharedPrefrencesFixerRegistration.edit();
        editor.putString("FIXER_SHOP_ID", fixr_shop_id);
        editor.putString("FIXER_ID", fixr_id);
        editor.commit();

    }

    public static void getSharedPrefrencesFixerShopId(Context applicationContext) {

        if(sharedPrefrencesFixerRegistration == null)
        {
            context = applicationContext;
            sharedPrefrencesFixerRegistration = context.getSharedPreferences("HAWKER_SALES_BASIC_REGISTRATION", Context.MODE_PRIVATE);
        }
        fixr_shop_id=  sharedPrefrencesFixerRegistration.getString("FIXER_SHOP_ID","");
        fixr_id=  sharedPrefrencesFixerRegistration.getString("FIXER_ID","");

    }

    public static String getFixr_shop_id() {
        return fixr_shop_id;
    }

    public static String getFixr_id() {
        return fixr_id;
    }
    //===============================FIXER SHOP CATEGORY ID STORE======================================================


    public static void saveSharedPrefrencesFixerShopCategoryId(Context applicationContext, String fixer_category_id) {
        if(sharedPrefrencesFixerRegistration == null)
        {
            context = applicationContext;
            sharedPrefrencesFixerRegistration = context.getSharedPreferences("HAWKER_SALES_BASIC_REGISTRATION", Context.MODE_PRIVATE);
        }

        context=applicationContext;
        fixr_category_id= fixer_category_id;
        SharedPreferences.Editor editor = sharedPrefrencesFixerRegistration.edit();
        editor.putString("FIXER_SHOP_CATEGORY_ID", fixr_category_id);
        editor.commit();

    }



    public static void getSharedPrefrencesFixerShopCategoryId(Context applicationContext) {
        if(sharedPrefrencesFixerRegistration == null) {
            context = applicationContext;
            sharedPrefrencesFixerRegistration = context.getSharedPreferences("HAWKER_SALES_BASIC_REGISTRATION", Context.MODE_PRIVATE);
        }
        fixr_category_id=  sharedPrefrencesFixerRegistration.getString("FIXER_SHOP_CATEGORY_ID","");
    }
    public static String getFixr_shop_categoryid() {
        return fixr_category_id;
    }

    //=========================================================================================================================

    public static void saveSharedPrefrencesFixerShopCategoryIdStatus(Context applicationContext, String fixr_category_status) {
        if(sharedPrefrencesFixerRegistration == null) {
            context = applicationContext;
            sharedPrefrencesFixerRegistration = context.getSharedPreferences("HAWKER_SALES_BASIC_REGISTRATION", Context.MODE_PRIVATE);
        }
        context=applicationContext;
        fixr_category_status= fixr_category_status;
        SharedPreferences.Editor editor = sharedPrefrencesFixerRegistration.edit();
        editor.putString("FIXER_SHOP_CATEGORY_STATUS", fixr_category_status);
        editor.commit();

    }

    public static void getSharedPrefrencesFixerShopCategoryIdStatus(Context applicationContext) {
        if(sharedPrefrencesFixerRegistration == null) {
            context = applicationContext;
            sharedPrefrencesFixerRegistration = context.getSharedPreferences("HAWKER_SALES_BASIC_REGISTRATION", Context.MODE_PRIVATE);
        }
        fixr_category_status=  sharedPrefrencesFixerRegistration.getString("FIXER_SHOP_CATEGORY_STATUS","");
    }
    public static String getFixr_shop_categoryStatus() {
        return fixr_category_status;
    }




    //===============================FIXER SHOP SUB CATEGORY ID STORE======================================================


    public static void saveSharedPrefrencesFixerShopSubCategoryId(Context applicationContext, String fixer_category_id) {
        if(sharedPrefrencesFixerRegistration == null)
        {
            context = applicationContext;
            sharedPrefrencesFixerRegistration = context.getSharedPreferences("HAWKER_SALES_BASIC_REGISTRATION", Context.MODE_PRIVATE);
        }

        context=applicationContext;
        fixr_subcategory_id= fixer_category_id;
        SharedPreferences.Editor editor = sharedPrefrencesFixerRegistration.edit();
        editor.putString("FIXER_SHOP_SUB_CATEGORY_ID", fixr_subcategory_id);
        //Toast.makeText(context,fixr_subcategory_id,Toast.LENGTH_LONG).show();
        editor.commit();

    }

    public static void getSharedPrefrencesFixerShopSubCategoryId(Context applicationContext) {
        if(sharedPrefrencesFixerRegistration == null) {
            context = applicationContext;
            sharedPrefrencesFixerRegistration = context.getSharedPreferences("HAWKER_SALES_REGISTRATION", Context.MODE_PRIVATE);
        }
        fixr_subcategory_id=  sharedPrefrencesFixerRegistration.getString("FIXER_SHOP_SUB_CATEGORY_ID","");

        System.out.println(fixr_subcategory_id);


    }

    public static String getFixr_shop_Subcategoryid() {
        //Toast.makeText(context,fixr_subcategory_id+"",Toast.LENGTH_LONG).show();
        fixr_subcategory_id=fixr_subcategory_id+"";
        return fixr_subcategory_id;
    }




    //===============================FIXER SHOP SUPER SUB CATEGORY ID STORE======================================================


    public static void saveSharedPrefrencesFixerShopSuperSubCategoryId(Context applicationContext, String fixr_supsubcategory_id) {
        if(sharedPrefrencesFixerRegistration == null)
        {
            context = applicationContext;
            sharedPrefrencesFixerRegistration = context.getSharedPreferences("HAWKER_SALES_BASIC_REGISTRATION", Context.MODE_PRIVATE);
        }

        context=applicationContext;
        fixr_supersubcategory_id+= fixr_supsubcategory_id;
        SharedPreferences.Editor editor = sharedPrefrencesFixerRegistration.edit();
        //Toast.makeText(context,fixr_supsubcategory_id+"",Toast.LENGTH_LONG).show();
        editor.putString("FIXER_SHOP_SUPER_SUB_CATEGORY_ID", fixr_supersubcategory_id);
        editor.commit();

    }

    public static void getSharedPrefrencesFixerShopSuperSubCategoryId(Context applicationContext) {
        if(sharedPrefrencesFixerRegistration == null) {
            context = applicationContext;
            sharedPrefrencesFixerRegistration = context.getSharedPreferences("HAWKER_SALES_BASIC_REGISTRATION", Context.MODE_PRIVATE);
        }
        fixr_supersubcategory_id=  sharedPrefrencesFixerRegistration.getString("FIXER_SHOP_SUPER_SUB_CATEGORY_ID","");
    }

    public static String getFixr_shop_SuperSubcategoryid() {
        return fixr_supersubcategory_id;
    }


    //===============================================FIXER BASIC DETAILS SHAREDPREFRENCES========================================================


    public static void saveHowkerRegistration(Context applicationContext, String HowkerName, String HowkerType, String HowkerResidentialAddress,
                                              String HowkerMobileNumber, String HowkerPassword, String HowkerBussinessName, String HowkerStartTime,
                                              String HowkerCloseTime, String HowkerShopName, String HowkerShopAdress, String HowkerShopeMobileNumber,
                                              String HowkerSelectedService, String HowkerProfileImage , String HowkerIndentityProofImage,
                                              String HowkerAddressProofImage, String HowkerShopImageOne, String HowkerShopImageTwo, String HowkerGPSID,
                                              String HowkerLatitude, String HowkerLongitude, String HowkerGPSMODE, String HowkerSalesID, String HowkerRegisterAdress,
                                              String HowkerStateCity,String HowkerServiceStartTime,String HowkerServiceEndTime,String strNameType,
                                              String HowkerGender,String HowkerSubHawkerName,String HowkerSubUserType,String HowkerMobileType,String HowkerSmartPhone,
                                              String HowkerDays,String HowkerStartDate,String HowkerEndDate,String HowkerGSTNumber,String HowkerTypeOtherDetail,
                                              String HowkerContactNumber,String iSubHawkerNamePosition,String strHawker_type_Code,String strOther_Sub_HawkerType,
                                              String strPhoneType,String PRO_LAT ,String PRO_LON ,String SHOP1_LAT ,String SHOP1_LON ,String SHOP2_LAT ,
                                              String SHOP2_LON,String ADDRESS_LAT ,String ADDRESS_LON ,String IDENTITY_LAT ,String IDENTITY_LON,
                                              String strFixSeasonalyesno,String strOtherBusinessDetails,String strMenuImage,String MENU_LAT,
                                              String MENU_LON,String strIndentityProofImage2,String strMenuImage1,String strMenuImage2,
                                              String strMenuImage3,String ActivityStatus,String PHONE_TYPE) {

        if(sharedPrefrencesFixerRegistration == null) {
            context = applicationContext;
            sharedPrefrencesFixerRegistration = context.getSharedPreferences("HAWKER_SALES_BASIC_REGISTRATION", Context.MODE_PRIVATE);
        }
        context=applicationContext;
        HowkerName=HowkerName;
        HowkerType=HowkerType;
        HowkerResidentialAddress=HowkerResidentialAddress;
        HowkerMobileNumber=HowkerMobileNumber;
        HowkerPassword=HowkerPassword;
        HowkerBussinessName=HowkerBussinessName;
        HowkerStartTime=HowkerStartTime;
        HowkerCloseTime=HowkerCloseTime;
        HowkerShopName=HowkerShopName;
        HowkerShopAdress=HowkerShopAdress;
        HowkerShopeMobileNumber=HowkerShopeMobileNumber;
        HowkerSelectedService=HowkerSelectedService;
        HowkerProfileImage=HowkerProfileImage;
        HowkerIndentityProofImage=HowkerIndentityProofImage;
        HowkerAddressProofImage=HowkerAddressProofImage;
        HowkerShopImageOne=HowkerShopImageOne;
        HowkerShopImageTwo=HowkerShopImageTwo;
        HowkerGPSID=HowkerGPSID;
        HowkerLatitude=HowkerLatitude;
        HowkerLongitude=HowkerLongitude;
        HowkerGPSMODE=HowkerGPSMODE;
        HowkerSalesID=HowkerSalesID;
        HowkerRegisterAdress=HowkerRegisterAdress;
        HowkerStateCity=HowkerStateCity;
        HowkerServiceStartTime=HowkerServiceStartTime;
        HowkerServiceEndTime=HowkerServiceEndTime;
        strNameType=strNameType;
        HowkerGender= HowkerGender;
        HowkerSubHawkerName= HowkerSubHawkerName;
        HowkerSubUserType = HowkerSubUserType;
        HowkerMobileType = HowkerMobileType;
        HowkerSmartPhone = HowkerSmartPhone;
        HowkerDays = HowkerDays;
        HowkerStartDate= HowkerStartDate;
        HowkerEndDate = HowkerEndDate;
        HowkerGSTNumber = HowkerGSTNumber;
        HowkerTypeOtherDetail = HowkerTypeOtherDetail;
        HowkerContactNumber = HowkerContactNumber;
        iSubHawkerNamePosition = iSubHawkerNamePosition;
        strHawker_type_Code = strHawker_type_Code;
        strOther_Sub_HawkerType = strOther_Sub_HawkerType;
        strPhoneType = strPhoneType;
        PRO_LAT = PRO_LAT;
        PRO_LON = PRO_LON;
        SHOP1_LAT = SHOP1_LAT;
        SHOP1_LON = SHOP1_LON;
        SHOP2_LAT = SHOP2_LAT;
        SHOP2_LON = SHOP2_LON;
        ADDRESS_LAT = ADDRESS_LAT;
        ADDRESS_LON = ADDRESS_LON;
        IDENTITY_LAT = IDENTITY_LAT;
        IDENTITY_LON = IDENTITY_LON;
        HawkerFixSeasonalYesNo = strFixSeasonalyesno;
        strOtherBusinessDetails = strOtherBusinessDetails;
        MenuImage=strMenuImage;
        MENU_LAT=MENU_LAT;
        MENU_LON=MENU_LON;
        strIndentityProofImage2 = strIndentityProofImage2;
        strMenuImage1 = strMenuImage1;
        strMenuImage2 = strMenuImage2;
        strMenuImage3 = strMenuImage3;
        ActivityStatus = ActivityStatus;
        PHONE_TYPE = PHONE_TYPE;





        SharedPreferences.Editor editor = sharedPrefrencesFixerRegistration.edit();
        editor.putString("HowkerName", HowkerName);
        editor.putString("HowkerType", HowkerType);
        editor.putString("HowkerResidentialAddress", HowkerResidentialAddress);
        editor.putString("HowkerMobileNumber", HowkerMobileNumber);
        editor.putString("HowkerPassword", HowkerPassword);
        editor.putString("HowkerBussinessName", HowkerBussinessName);
        editor.putString("HowkerStartTime", HowkerStartTime);
        editor.putString("HowkerCloseTime", HowkerCloseTime);
        editor.putString("HowkerShopName", HowkerShopName);
        editor.putString("HowkerShopAdress", HowkerShopAdress);
        editor.putString("HowkerShopeMobileNumber", HowkerShopeMobileNumber);
        editor.putString("HowkerSelectedService", HowkerSelectedService);
        editor.putString("HowkerProfileImage", HowkerProfileImage);
        editor.putString("HowkerIndentityProofImage", HowkerIndentityProofImage);
        editor.putString("HowkerAddressProofImage", HowkerAddressProofImage);
        editor.putString("HowkerShopImageOne", HowkerShopImageOne);
        editor.putString("HowkerShopImageTwo", HowkerShopImageTwo);
        editor.putString("HowkerGPSID", HowkerGPSID);
        editor.putString("HowkerLatitude", HowkerLatitude);
        editor.putString("HowkerLongitude", HowkerLongitude);
        editor.putString("HowkerGPSMODE", HowkerGPSMODE);
        editor.putString("HowkerSalesID", HowkerSalesID);
        editor.putString("HowkerRegisterAdress", HowkerRegisterAdress);
        editor.putString("HowkerStateCity", HowkerStateCity);
        editor.putString("HowkerServiceStartTime", HowkerServiceStartTime);
        editor.putString("HowkerServiceEndTime", HowkerServiceEndTime);
        editor.putString("strNameType", strNameType);
        editor.putString("HowkerGender", HowkerGender);
        editor.putString("HowkerSubHawkerName", HowkerSubHawkerName);
        editor.putString("HowkerSubUserType", HowkerSubUserType);
        editor.putString("HowkerMobileType", HowkerMobileType);
        editor.putString("HowkerSmartPhone", HowkerSmartPhone);
        editor.putString("HowkerDays", HowkerDays);
        editor.putString("HowkerStartDate", HowkerStartDate);
        editor.putString("HowkerEndDate", HowkerEndDate);
        editor.putString("HowkerGSTNumber", HowkerGSTNumber);
        editor.putString("HowkerTypeOtherDetail", HowkerTypeOtherDetail);
        editor.putString("HowkerContactNumber", HowkerContactNumber);
        editor.putString("iSubHawkerNamePosition", iSubHawkerNamePosition);
        editor.putString("strHawker_type_Code", strHawker_type_Code);
        editor.putString("strOther_Sub_HawkerType", strOther_Sub_HawkerType);
        editor.putString("strPhoneType", strPhoneType);
        editor.putString("PRO_LAT", PRO_LAT);
        editor.putString("PRO_LON", PRO_LON);
        editor.putString("SHOP1_LAT", SHOP1_LAT);
        editor.putString("SHOP1_LON", SHOP1_LON);
        editor.putString("SHOP2_LAT", SHOP2_LAT);
        editor.putString("SHOP2_LON", SHOP2_LON);
        editor.putString("ADDRESS_LAT", ADDRESS_LAT);
        editor.putString("ADDRESS_LON", ADDRESS_LON);
        editor.putString("IDENTITY_LAT", IDENTITY_LAT);
        editor.putString("IDENTITY_LON", IDENTITY_LON);
        editor.putString("HawkerFixSeasonalYesNo", HawkerFixSeasonalYesNo);
        editor.putString("strOtherBusinessDetails", strOtherBusinessDetails);
        editor.putString("MenuImage", MenuImage);
        editor.putString("MENU_LAT", MENU_LAT);
        editor.putString("MENU_LON", MENU_LON);
        editor.putString("strIndentityProofImage2", strIndentityProofImage2);
        editor.putString("strMenuImage1", strMenuImage1);
        editor.putString("strMenuImage2", strMenuImage2);
        editor.putString("strMenuImage3", strMenuImage3);
        editor.putString("ActivityStatus", ActivityStatus);
        editor.putString("PHONE_TYPE", PHONE_TYPE);

        editor.commit();
    }



    public static void getHowkerRegistration(Context applicationContext) {
        if(sharedPrefrencesFixerRegistration == null) {
            context = applicationContext;
            sharedPrefrencesFixerRegistration = context.getSharedPreferences("HAWKER_SALES_BASIC_REGISTRATION", Context.MODE_PRIVATE);
        }

        HowkerName=  sharedPrefrencesFixerRegistration.getString("HowkerName","");
        HowkerType= sharedPrefrencesFixerRegistration.getString("HowkerType","");
        HowkerResidentialAddress= sharedPrefrencesFixerRegistration.getString("HowkerResidentialAddress","");
        HowkerMobileNumber= sharedPrefrencesFixerRegistration.getString("HowkerMobileNumber","");
        HowkerPassword=  sharedPrefrencesFixerRegistration.getString("HowkerPassword","");
        HowkerBussinessName= sharedPrefrencesFixerRegistration.getString("HowkerBussinessName","");
        HowkerStartTime=  sharedPrefrencesFixerRegistration.getString("HowkerStartTime","");
        HowkerCloseTime= sharedPrefrencesFixerRegistration.getString("HowkerCloseTime","");
        HowkerShopName= sharedPrefrencesFixerRegistration.getString("HowkerShopName","");
        HowkerShopAdress= sharedPrefrencesFixerRegistration.getString("HowkerShopAdress","");
        HowkerShopeMobileNumber=  sharedPrefrencesFixerRegistration.getString("HowkerShopeMobileNumber","");
        HowkerSelectedService= sharedPrefrencesFixerRegistration.getString("HowkerSelectedService","");
        HowkerProfileImage=  sharedPrefrencesFixerRegistration.getString("HowkerProfileImage","");
        HowkerIndentityProofImage= sharedPrefrencesFixerRegistration.getString("HowkerIndentityProofImage","");
        HowkerAddressProofImage= sharedPrefrencesFixerRegistration.getString("HowkerAddressProofImage","");
        HowkerShopImageOne= sharedPrefrencesFixerRegistration.getString("HowkerShopImageOne","");
        HowkerShopImageTwo=  sharedPrefrencesFixerRegistration.getString("HowkerShopImageTwo","");
        HowkerGPSID= sharedPrefrencesFixerRegistration.getString("HowkerGPSID","");
        HowkerLatitude=  sharedPrefrencesFixerRegistration.getString("HowkerLatitude","");
        HowkerLongitude= sharedPrefrencesFixerRegistration.getString("HowkerLongitude","");
        HowkerGPSMODE= sharedPrefrencesFixerRegistration.getString("HowkerGPSMODE","");
        HowkerSalesID= sharedPrefrencesFixerRegistration.getString("HowkerSalesID","");
        HowkerRegisterAdress=  sharedPrefrencesFixerRegistration.getString("HowkerRegisterAdress","");
        HowkerStateCity= sharedPrefrencesFixerRegistration.getString("HowkerStateCity","");
        HowkerServiceStartTime= sharedPrefrencesFixerRegistration.getString("HowkerServiceStartTime","");
        HowkerServiceEndTime= sharedPrefrencesFixerRegistration.getString("HowkerServiceStartTime","");
        strNameType= sharedPrefrencesFixerRegistration.getString("strNameType","");
        HowkerGender= sharedPrefrencesFixerRegistration.getString("HowkerGender","");
        HowkerSubHawkerName= sharedPrefrencesFixerRegistration.getString("HowkerSubHawkerName","");
        HowkerSubUserType= sharedPrefrencesFixerRegistration.getString("HowkerSubUserType","");
        HowkerMobileType= sharedPrefrencesFixerRegistration.getString("HowkerMobileType","");
        HowkerSmartPhone= sharedPrefrencesFixerRegistration.getString("HowkerSmartPhone","");
        HowkerDays= sharedPrefrencesFixerRegistration.getString("HowkerDays","");
        HowkerStartDate= sharedPrefrencesFixerRegistration.getString("HowkerStartDate","");
        HowkerEndDate= sharedPrefrencesFixerRegistration.getString("HowkerEndDate","");
        HowkerGSTNumber= sharedPrefrencesFixerRegistration.getString("HowkerGSTNumber","");
        HowkerTypeOtherDetail= sharedPrefrencesFixerRegistration.getString("HowkerTypeOtherDetail","");
        HowkerContactNumber= sharedPrefrencesFixerRegistration.getString("HowkerContactNumber","");
        iSubHawkerNamePosition= sharedPrefrencesFixerRegistration.getString("iSubHawkerNamePosition","");
        strHawker_type_Code= sharedPrefrencesFixerRegistration.getString("strHawker_type_Code","");
        strHawker_type_Code= sharedPrefrencesFixerRegistration.getString("strOther_Sub_HawkerType","");
        strPhoneType= sharedPrefrencesFixerRegistration.getString("strPhoneType","");

        PRO_LAT= sharedPrefrencesFixerRegistration.getString("PRO_LAT","");
        PRO_LON= sharedPrefrencesFixerRegistration.getString("PRO_LON","");
        SHOP1_LAT= sharedPrefrencesFixerRegistration.getString("SHOP1_LAT","");
        SHOP1_LON= sharedPrefrencesFixerRegistration.getString("SHOP1_LON","");
        SHOP2_LAT= sharedPrefrencesFixerRegistration.getString("SHOP2_LAT","");
        SHOP2_LON= sharedPrefrencesFixerRegistration.getString("SHOP2_LON","");
        ADDRESS_LAT= sharedPrefrencesFixerRegistration.getString("ADDRESS_LAT","");
        ADDRESS_LON= sharedPrefrencesFixerRegistration.getString("ADDRESS_LON","");
        IDENTITY_LAT= sharedPrefrencesFixerRegistration.getString("IDENTITY_LAT","");
        IDENTITY_LON= sharedPrefrencesFixerRegistration.getString("IDENTITY_LON","");
        HawkerFixSeasonalYesNo= sharedPrefrencesFixerRegistration.getString("HawkerFixSeasonalYesNo","");
        strOtherBusinessDetails= sharedPrefrencesFixerRegistration.getString("strOtherBusinessDetails","");

        MenuImage= sharedPrefrencesFixerRegistration.getString("MenuImage","");
        MENU_LAT= sharedPrefrencesFixerRegistration.getString("MENU_LAT","");
        MENU_LON= sharedPrefrencesFixerRegistration.getString("MENU_LON","");
        strIndentityProofImage2= sharedPrefrencesFixerRegistration.getString("strIndentityProofImage2","");
        strMenuImage1= sharedPrefrencesFixerRegistration.getString("strMenuImage1","");
        strMenuImage2= sharedPrefrencesFixerRegistration.getString("strMenuImage2","");
        strMenuImage3= sharedPrefrencesFixerRegistration.getString("strMenuImage3","");
        ActivityStatus= sharedPrefrencesFixerRegistration.getString("ActivityStatus","");
        PHONE_TYPE= sharedPrefrencesFixerRegistration.getString("PHONE_TYPE","");
    }



    public static String getPHONE_TYPE() {
        return PHONE_TYPE;
    }
    public static String getiSubHawkerNamePosition() {
        return iSubHawkerNamePosition;
    }
    public static String getOther_Sub_HawkerType() {
        return strOther_Sub_HawkerType;
    }
    public static String getstrHawker_type_Code() {
        return strHawker_type_Code;
    }

    public static String getHowkerName() { return HowkerName; }

    public static String getHowkerType() {
        return HowkerType;
    }

    public static String getHowkerResidentialAddress() {
        return HowkerResidentialAddress;
    }

    public static String getHowkerMobileNumber() {
        return HowkerMobileNumber;
    }

    public static String getHowkerPassword() {
        return HowkerPassword;
    }

    public static String getHowkerBussinessName() {
        return HowkerBussinessName;
    }

    public static String getHowkerStartTime() {
        return HowkerStartTime;
    }

    public static String getHowkerCloseTime() {
        return HowkerCloseTime;
    }

    public static String getHowkerShopName() {
        return HowkerShopName;
    }

    public static String getHowkerShopAdress() {
        return HowkerShopAdress;
    }

    public static String getHowkerShopeMobileNumber() {
        return HowkerShopeMobileNumber;
    }

    public static String getHowkerSelectedService() {
        return HowkerSelectedService;
    }

    public static String getHowkerProfileImage() {
        return HowkerProfileImage;
    }

    public static String getHowkerIndentityProofImage() {
        return HowkerIndentityProofImage;
    }

    public static String getHowkerAddressProofImage() {
        return HowkerAddressProofImage;
    }

    public static String getHowkerShopImageOne() {
        return HowkerShopImageOne;
    }

    public static String getHowkerShopImageTwo() {
        return HowkerShopImageTwo;
    }

    public static String getHowkerGPSID() {
        return HowkerGPSID;
    }

    public static String getHowkerLatitude() {
        return HowkerLatitude;
    }

    public static String getHowkerLongitude() {
        return HowkerLongitude;
    }

    public static String getHowkerGPSMODE() {
        return HowkerGPSMODE;
    }

    public static String getHowkerSalesID() {
        return HowkerSalesID;
    }

    public static String getHowkerRegisterAdress() {
        return HowkerRegisterAdress;
    }

    public static String getHowkerStateCity() {
        return HowkerStateCity;
    }

    public static String getHowkerServiceStartTime() { return HowkerServiceStartTime; }

    public static String getHowkerServiceEndTime() { return HowkerServiceEndTime; }

    public static String getstrNameType() { return strNameType; }

    public static String getHowkerGender() { return HowkerGender; }
    public static String getHowkerSubHawkerName() { return HowkerSubHawkerName; }
    public static String getHowkerSubUserType() { return HowkerSubUserType; }
    public static String getHowkerMobileType() { return HowkerMobileType; }
    public static String getHowkerSmartPhone() { return HowkerSmartPhone; }
    public static String getHowkerDays() { return HowkerDays; }
    public static String getHowkerStartDate() { return HowkerStartDate; }
    public static String getHowkerEndDate() { return HowkerEndDate; }
    public static String getHowkerGSTNumber() { return HowkerGSTNumber; }
    public static String getHowkerTypeOtherDetail() { return HowkerTypeOtherDetail; }
    public static String getHowkerContactNumber() { return HowkerContactNumber; }
    public static String getHowkerPhoneType() { return strPhoneType; }
    public static String getHowkerPRO_LAT() { return PRO_LAT; }
    public static String getHowkerPRO_LON() { return PRO_LON; }
    public static String getHowkerSHOP1_LAT() { return SHOP1_LAT; }
    public static String getHowkerSHOP1_LON() { return SHOP1_LON; }
    public static String getHowkerSHOP2_LAT() { return SHOP2_LAT; }
    public static String getHowkerSHOP2_LON() { return SHOP2_LON; }
    public static String getHowkerADDRESS_LAT() { return ADDRESS_LAT; }
    public static String getHowkerADDRESS_LON() { return ADDRESS_LON; }
    public static String getHowkerIDENTITY_LAT() { return IDENTITY_LAT; }
    public static String getHowkerIDENTITY_LON() { return IDENTITY_LON; }
    public static String getHawkerFixSeasonalYesNo() { return HawkerFixSeasonalYesNo; }
    public static String getstrOtherBusinessDetails() { return strOtherBusinessDetails; }
    public static String getMenuImage() {
        return MenuImage;
    }
    public static String getMENU_LAT() {
        return MENU_LAT;
    }
    public static String getMENU_LON() { return MENU_LON; }

    public static String getIndentityProofImage2() { return strIndentityProofImage2; }
    public static String getMenuImage1() { return strMenuImage1; }
    public static String getMenuImage2() { return strMenuImage2; }
    public static String getMenuImage3() { return strMenuImage3; }
    public static String getActivityStatus() { return ActivityStatus; }


    public static void ClearSharedPrefrenc(Context applicationContext) {
        context = applicationContext;
        sharedPrefrencesFixerRegistration = context.getSharedPreferences("HAWKER_SALES_BASIC_REGISTRATION", Context.MODE_PRIVATE);
        SharedPreferences.Editor editorFixer = sharedPrefrencesFixerRegistration.edit();
        editorFixer.putString("HowkerName", null);
        editorFixer.putString("HowkerType", null);
        editorFixer.putString("HowkerResidentialAddress", null);
        editorFixer.putString("HowkerMobileNumber", null);
        editorFixer.putString("HowkerPassword", null);
        editorFixer.putString("HowkerBussinessName", null);
        editorFixer.putString("HowkerStartTime", null);
        editorFixer.putString("HowkerCloseTime", null);
        editorFixer.putString("HowkerShopName", null);
        editorFixer.putString("HowkerShopAdress", null);
        editorFixer.putString("HowkerShopeMobileNumber", null);
        editorFixer.putString("HowkerSelectedService", null);
        editorFixer.putString("HowkerProfileImage", null);
        editorFixer.putString("HowkerIndentityProofImage", null);
        editorFixer.putString("HowkerAddressProofImage", null);
        editorFixer.putString("HowkerShopImageOne", null);
        editorFixer.putString("HowkerShopImageTwo", null);
        editorFixer.putString("HowkerGPSID", null);
        editorFixer.putString("HowkerLatitude", null);
        editorFixer.putString("HowkerLongitude", null);
        editorFixer.putString("HowkerGPSMODE", null);
        editorFixer.putString("HowkerSalesID", null);
        editorFixer.putString("HowkerRegisterAdress", null);
        editorFixer.putString("HowkerStateCity", null);
        editorFixer.putString("HowkerServiceStartTime", null);
        editorFixer.putString("HowkerServiceEndTime", null);
        editorFixer.putString("strNameType", null);
        editorFixer.putString("HowkerGender", null);
        editorFixer.putString("HowkerSubHawkerName", null);
        editorFixer.putString("HowkerSubUserType", null);
        editorFixer.putString("HowkerMobileType", null);
        editorFixer.putString("HowkerSmartPhone", null);
        editorFixer.putString("HowkerDays", null);
        editorFixer.putString("HowkerStartDate", null);
        editorFixer.putString("HowkerEndDate", null);
        editorFixer.putString("HowkerGSTNumber", null);
        editorFixer.putString("HowkerTypeOtherDetail", null);
        editorFixer.putString("HowkerContactNumber", null);
        editorFixer.putString("iSubHawkerNamePosition", null);
        editorFixer.putString("strHawker_type_Code", null);
        editorFixer.putString("strOther_Sub_HawkerType", null);
        editorFixer.putString("strPhoneType", null);
        /* Clear Category */
        editorFixer.putString("FIXER_SHOP_CATEGORY_ID", null);
        editorFixer.putString("FIXER_SHOP_SUB_CATEGORY_ID", null);
        editorFixer.putString("FIXER_SHOP_SUPER_SUB_CATEGORY_ID", null);

        /* Days and Time*/
        editorFixer.putString("HAWKER_WORKING_DAYS", null);
        editorFixer.putString("HAWKER_START_TIME", null);
        editorFixer.putString("HAWKER_END_TIME", null);


        editorFixer.putString("HAWKERSTARTTIME", null);
        editorFixer.putString("HAWKERSTOPTIME", null);
        editorFixer.putString("DATE_LIST", null);

        editorFixer.putString("HawkerINCOMMINGNUMBER", null);
        editorFixer.putString("HAWKERCALLSTATUS", null);

        editorFixer.putString("IMAGELATITUDELONGITUDE", null);
        editorFixer.putString("PRO_LAT", null);
        editorFixer.putString("PRO_LON", null);
        editorFixer.putString("SHOP1_LAT", null);
        editorFixer.putString("SHOP1_LON", null);
        editorFixer.putString("SHOP2_LAT", null);
        editorFixer.putString("SHOP2_LON", null);
        editorFixer.putString("ADDRESS_LAT", null);
        editorFixer.putString("ADDRESS_LON", null);
        editorFixer.putString("IDENTITY_LAT", null);
        editorFixer.putString("IDENTITY_LON", null);
        editorFixer.putString("HawkerFixSeasonalYesNo", null);
        editorFixer.putString("strOtherBusinessDetails", null);
        editorFixer.putString("HawkerQuestion", null);
        editorFixer.putString("HawkerNumberMatch", null);
        editorFixer.putString("HawkerOtherService", null);
        editorFixer.putString("MenuImage", null);
        editorFixer.putString("MENU_LAT", null);
        editorFixer.putString("MENU_LON", null);
        editorFixer.putString("strIndentityProofImage2", null);
        editorFixer.putString("strMenuImage1", null);
        editorFixer.putString("strMenuImage2", null);
        editorFixer.putString("strMenuImage3", null);
        editorFixer.putString("ActivityStatus", null);
        editorFixer.putString("FIXER_SHOP_CATEGORY_STATUS", null);
        editorFixer.putString("PHONE_TYPE", null);
        editorFixer.commit();
        editorFixer.clear();
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////


    //===============================================FIXER BASIC DETAILS SHAREDPREFRENCES========================================================


    public static void saveTempHowkerRegistration(Context applicationContext,String SalesID, String HowkerName, String HawkerGender,String HowkerType,
                                                  String HowkerSubType,String HawkerAddress,String HawkerMobileNumber,String HawkerCity,
                                                  String HawkerState, String HawkerLatitude, String HawkerLongitude,String HawkerSubTypePosition,
                                                  String HawkerSubType,String HawkerStartDate,String HawkerEndDate,String HawkerTypeCode,String sString,
                                                  String HawkerMobilemodelType,String HawkerOtherBisinessDetail,String ActivityStatus,String TempFixSeasonalyesno) {
        if(sharedPrefrencesFixerRegistration == null) {
            context = applicationContext;
            sharedPrefrencesFixerRegistration = context.getSharedPreferences("HAWKER_SALES_BASIC_REGISTRATION", Context.MODE_PRIVATE);
        }
        context=applicationContext;
        HawkerSalesID=SalesID;
        TempHowkerName=HowkerName;
        TempHowkerGender=HawkerGender;
        TempHowkerType=HowkerType;
        TempHowkerSubType=HowkerSubType;
        TempHawkerAddress=HawkerAddress;
        TempHawkerMobileNumber=HawkerMobileNumber;
        TempHawkerCity=HawkerCity;
        TempHawkerState=HawkerState;
        TempHawkerLatitude=HawkerLatitude;
        TempHawkerLongitude=HawkerLongitude;
        TempHawkerSubTypePosition=HawkerSubTypePosition;
        TempHawkerSubType=HawkerSubType;
        TempHawkerStartDate=HawkerStartDate;
        TempHawkerEndDate=HawkerEndDate;
        TempHawkerTypeCode=HawkerTypeCode;
        TempString=sString;
        TempHawkerMobilemodelType=HawkerMobilemodelType;
        TempHawkerOtherBisinessDetail=HawkerOtherBisinessDetail;
        ActivityStatus=ActivityStatus;
        TempFixSeasonalyesno=TempFixSeasonalyesno;

        SharedPreferences.Editor editor = sharedPrefrencesFixerRegistration.edit();
        editor.putString("HawkerSalesID", HawkerSalesID);
        editor.putString("TempHowkerName", TempHowkerName);
        editor.putString("TempHowkerGender", TempHowkerGender);
        editor.putString("TempHowkerType", TempHowkerType);
        editor.putString("TempHowkerSubType", TempHowkerSubType);
        editor.putString("TempHawkerAddress", TempHawkerAddress);
        editor.putString("TempHawkerMobileNumber", TempHawkerMobileNumber);
        editor.putString("TempHawkerCity", TempHawkerCity);
        editor.putString("TempHawkerState", TempHawkerState);
        editor.putString("TempHawkerLatitude", TempHawkerLatitude);
        editor.putString("TempHawkerLongitude", TempHawkerLongitude);
        editor.putString("TempHawkerSubTypePosition", TempHawkerSubTypePosition);
        editor.putString("TempHawkerSubType", TempHawkerSubType);
        editor.putString("TempHawkerStartDate", TempHawkerStartDate);
        editor.putString("TempHawkerEndDate", TempHawkerEndDate);
        editor.putString("TempHawkerTypeCode", TempHawkerTypeCode);
        editor.putString("TempString", TempString);
        editor.putString("TempHawkerMobilemodelType", TempHawkerMobilemodelType);
        editor.putString("TempHawkerOtherBisinessDetail", TempHawkerOtherBisinessDetail);
        editor.putString("ActivityStatus", ActivityStatus);
        editor.putString("TempFixSeasonalyesno", TempFixSeasonalyesno);
        editor.commit();
    }
    public static void getTempHowkerRegistration(Context applicationContext) {
        if(sharedPrefrencesFixerRegistration == null) {
            context = applicationContext;
            sharedPrefrencesFixerRegistration = context.getSharedPreferences("HAWKER_SALES_BASIC_REGISTRATION", Context.MODE_PRIVATE);
        }
        HawkerSalesID=  sharedPrefrencesFixerRegistration.getString("HawkerSalesID","");
        TempHowkerName= sharedPrefrencesFixerRegistration.getString("TempHowkerName","");
        TempHowkerGender= sharedPrefrencesFixerRegistration.getString("TempHowkerGender","");
        TempHowkerType= sharedPrefrencesFixerRegistration.getString("TempHowkerType","");
        TempHowkerSubType=  sharedPrefrencesFixerRegistration.getString("TempHowkerSubType","");
        TempHawkerAddress= sharedPrefrencesFixerRegistration.getString("TempHawkerAddress","");
        TempHawkerMobileNumber=  sharedPrefrencesFixerRegistration.getString("TempHawkerMobileNumber","");
        TempHawkerCity= sharedPrefrencesFixerRegistration.getString("TempHawkerCity","");
        TempHawkerState= sharedPrefrencesFixerRegistration.getString("TempHawkerState","");
        TempHawkerLatitude= sharedPrefrencesFixerRegistration.getString("TempHawkerLatitude","");
        TempHawkerLongitude=  sharedPrefrencesFixerRegistration.getString("TempHawkerLongitude","");
        TempHawkerSubTypePosition=  sharedPrefrencesFixerRegistration.getString("TempHawkerSubTypePosition","");
        TempHawkerSubType=  sharedPrefrencesFixerRegistration.getString("TempHawkerSubType","");
        TempHawkerStartDate=  sharedPrefrencesFixerRegistration.getString("TempHawkerStartDate","");
        TempHawkerEndDate=  sharedPrefrencesFixerRegistration.getString("TempHawkerEndDate","");
        TempHawkerTypeCode=  sharedPrefrencesFixerRegistration.getString("TempHawkerTypeCode","");
        TempString=  sharedPrefrencesFixerRegistration.getString("TempString","");
        TempHawkerMobilemodelType=  sharedPrefrencesFixerRegistration.getString("TempHawkerMobilemodelType","");
        TempHawkerOtherBisinessDetail=  sharedPrefrencesFixerRegistration.getString("TempHawkerOtherBisinessDetail","");
        ActivityStatus=  sharedPrefrencesFixerRegistration.getString("ActivityStatus","");
        TempFixSeasonalyesno=  sharedPrefrencesFixerRegistration.getString("TempFixSeasonalyesno","");
    }
    public static String getHawkerSalesID() {
        return HawkerSalesID;
    }
    public static String getTempHowkerName() {
        return TempHowkerName;
    }
    public static String getTempHowkerGender() {
        return TempHowkerGender;
    }
    public static String getTempHowkerType() { return TempHowkerType; }
    public static String getTempHowkerSubType() {
        return TempHowkerSubType;
    }
    public static String getTempHawkerAddress() {
        return TempHawkerAddress;
    }
    public static String getTempHawkerMobileNumber() {
        return TempHawkerMobileNumber;
    }
    public static String getTempHawkerCity() {
        return TempHawkerCity;
    }
    public static String getTempHawkerState() {
        return TempHawkerState;
    }
    public static String getTempHawkerLatitude() {
        return TempHawkerLatitude;
    }
    public static String getTempHawkerLongitude() {
        return TempHawkerLongitude;
    }
    public static String getTempHawkerSubTypePosition() { return TempHawkerSubTypePosition; }
    public static String getTempHawkerSubType() { return TempHawkerSubType; }
    public static String getTempHawkerStartDate() { return TempHawkerStartDate; }
    public static String getTempHawkerEndDate() { return TempHawkerEndDate; }
    public static String getTempHawkerTypeCode() { return TempHawkerTypeCode; }
    public static String getTempString() { return TempString; }
    public static String getTempHawkerMobilemodelType() { return TempHawkerMobilemodelType; }
    public static String getTempHawkerOtherBisinessDetail() { return TempHawkerOtherBisinessDetail; }
    public static String getTempFixSeasonalyesno() { return TempFixSeasonalyesno; }
   // public static String getActivityStatus() { return ActivityStatus; }


    public static void ClearTempHawkerData(Context applicationContext) {
        context = applicationContext;
        sharedPrefrencesFixerRegistration = context.getSharedPreferences("HAWKER_SALES_BASIC_REGISTRATION", Context.MODE_PRIVATE);
        SharedPreferences.Editor editorFixer = sharedPrefrencesFixerRegistration.edit();
        editorFixer.putString("HawkerSalesID", null);
        editorFixer.putString("TempHowkerName", null);
        editorFixer.putString("TempHowkerGender", null);
        editorFixer.putString("TempHowkerType", null);
        editorFixer.putString("TempHowkerSubType", null);
        editorFixer.putString("TempHawkerAddress", null);
        editorFixer.putString("TempHawkerMobileNumber", null);
        editorFixer.putString("TempHawkerCity", null);
        editorFixer.putString("TempHawkerState", null);
        editorFixer.putString("TempHawkerLatitude", null);
        editorFixer.putString("TempHawkerLongitude", null);
        editorFixer.putString("TempHawkerSubTypePosition", null);
        editorFixer.putString("TempHawkerSubType", null);
        editorFixer.putString("TempHawkerStartDate", null);
        editorFixer.putString("TempHawkerEndDate", null);
        editorFixer.putString("TempHawkerTypeCode", null);
        editorFixer.putString("TempString", null);
        /* Clear Category */
        editorFixer.putString("FIXER_SHOP_CATEGORY_ID", null);
        editorFixer.putString("FIXER_SHOP_SUB_CATEGORY_ID", null);
        editorFixer.putString("FIXER_SHOP_SUPER_SUB_CATEGORY_ID", null);
        /* Days and Time*/
        editorFixer.putString("HAWKER_WORKING_DAYS", null);
        editorFixer.putString("HAWKER_START_TIME", null);
        editorFixer.putString("HAWKER_END_TIME", null);

        editorFixer.putString("HAWKERSTARTTIME", null);
        editorFixer.putString("HAWKERSTOPTIME", null);
        editorFixer.putString("DATE_LIST", null);
        editorFixer.putString("HawkerFixSeasonalYesNo", null);
        editorFixer.putString("TempHawkerMobilemodelType", null);
        editorFixer.putString("strOtherBusinessDetails", null);
        editorFixer.putString("TempHawkerOtherBisinessDetail", null);
        editorFixer.putString("HawkerNumberMatch", null);
        editorFixer.putString("HawkerOtherService", null);
        editorFixer.putString("ActivityStatus", null);
        editorFixer.putString("TempFixSeasonalyesno", null);
        editorFixer.putString("FIXER_SHOP_CATEGORY_STATUS", null);

        editorFixer.commit();
        editorFixer.clear();
    }



    public static void clearCategoryData(Context applicationContext) {
        context = applicationContext;
        sharedPrefrencesFixerRegistration = context.getSharedPreferences("HAWKER_SALES_BASIC_REGISTRATION", Context.MODE_PRIVATE);
        SharedPreferences.Editor editorFixer = sharedPrefrencesFixerRegistration.edit();
        editorFixer.putString("FIXER_SHOP_CATEGORY_ID", null);
        editorFixer.putString("FIXER_SHOP_SUB_CATEGORY_ID", null);
        editorFixer.putString("FIXER_SHOP_SUPER_SUB_CATEGORY_ID", null);
        editorFixer.putString("FIXER_SHOP_CATEGORY_STATUS", null);
        editorFixer.commit();
        editorFixer.clear();
    }



}


/*editorFixer.putString("FIXER_SHOP_CATEGORY_ID", null);
        editorFixer.putString("FIXER_SHOP_SUB_CATEGORY_ID", null);
        editorFixer.putString("FIXER_SHOP_SUPER_SUB_CATEGORY_ID", null);*/