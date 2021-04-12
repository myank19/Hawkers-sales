package io.goolean.tech.hawker.sales.Constant.InterfacModel;

import android.content.Context;
import android.text.TextUtils;
import android.widget.EditText;

import io.goolean.tech.hawker.sales.Constant.MessageConstant;
import io.goolean.tech.hawker.sales.Constant.Singleton.CallbackSnakebarModel;


public class Validation implements Callback_Validation {

    boolean success = true;

    @Override
    public boolean checkNumber(Context context, EditText mEditNumber, String msg) {
        if(TextUtils.isEmpty(mEditNumber.getText().toString())) {
            CallbackSnakebarModel.getInstance().SnakebarMessage(context, msg, MessageConstant.toast_warning);
            return false;
        }else if (mEditNumber.getText().length() != 10)  {
            CallbackSnakebarModel.getInstance().SnakebarMessage(context, "Enter 10 digit mobile number", MessageConstant.toast_warning);
            return false;
        }else {
            return success;
        }
    }


    @Override
    public boolean checkNumber1(Context context, EditText mEditNumber, String msg) {
        if(TextUtils.isEmpty(mEditNumber.getText().toString())) {
            CallbackSnakebarModel.getInstance().SnakebarMessage(context, msg, MessageConstant.toast_warning);
            return false;
        }else if (mEditNumber.getText().length() != 10)  {
            CallbackSnakebarModel.getInstance().SnakebarMessage(context, "Enter 10 Digit Bussiness Mobile Number", MessageConstant.toast_warning);
            return false;
        }else {
            return success;
        }
    }

    @Override
    public boolean checkNumber2(Context context, EditText mEditNumber, String msg) {
        if(TextUtils.isEmpty(mEditNumber.getText().toString())) {
            CallbackSnakebarModel.getInstance().SnakebarMessage(context, msg, MessageConstant.toast_warning);
            return false;
        }else if (mEditNumber.getText().length() != 10)  {
            CallbackSnakebarModel.getInstance().SnakebarMessage(context, "Enter 10 Digit Bussiness Contact Mobile Number", MessageConstant.toast_warning);
            return false;
        }else {
            return success;
        }
    }


    @Override
    public boolean checkPassword(Context context, EditText mEditPassword, String msg) {
        if(TextUtils.isEmpty(mEditPassword.getText().toString()))
        {
            CallbackSnakebarModel.getInstance().SnakebarMessage(context, "Please Enter Login", MessageConstant.toast_warning);
            return false;
        }else if (mEditPassword.getText().length() < 5)  {
            CallbackSnakebarModel.getInstance().SnakebarMessage(context, "Enter Login Minimum 6 Digit", MessageConstant.toast_warning);
            return false;
        }
        return success;
    }

    @Override
    public boolean checkEditText(Context context, EditText edt, String msg) {

        if(TextUtils.isEmpty(edt.getText().toString()))
        {
            CallbackSnakebarModel.getInstance().SnakebarMessage(context, msg, MessageConstant.toast_warning);
            return false;
        }else if (edt.getText().length() < 3)  {
            CallbackSnakebarModel.getInstance().SnakebarMessage(context, msg, MessageConstant.toast_warning);
            return false;
        }
        return success;
    }
    @Override
    public boolean checkEditPostal(Context context, EditText mEditPassword, String msg) {
        if(TextUtils.isEmpty(mEditPassword.getText().toString()))
        {
            CallbackSnakebarModel.getInstance().SnakebarMessage(context, "Please Enter Pin", MessageConstant.toast_warning);
            return false;
        }else if (mEditPassword.getText().length()!=6)  {
            CallbackSnakebarModel.getInstance().SnakebarMessage(context, "Enter 6 digit Pin", MessageConstant.toast_warning);
            return false;
        }
        return success;
    }

    @Override
    public boolean checkTime(Context context, EditText edt, String msg) {
        if(TextUtils.isEmpty(edt.getText().toString()))
        {
                CallbackSnakebarModel.getInstance().SnakebarMessage(context, "Enter Time in Hours", MessageConstant.toast_warning);
                return false;
        }else if (Integer.parseInt(msg)>=24)  {
            CallbackSnakebarModel.getInstance().SnakebarMessage(context, "Enter time between 24 hours", MessageConstant.toast_warning);
            return false;
        }
        return success;
    }

    @Override
    public boolean stringCompare(Context context, EditText edt, String stringValue, String msg) {
        if(!edt.getText().toString().equals(stringValue))
        {
            CallbackSnakebarModel.getInstance().SnakebarMessage(context, msg, MessageConstant.toast_warning);
            return false;
        }
        return success;
    }

    @Override
    public boolean stringEqual(Context context, String str1, String str2, String msg) {
        if(str1.equals(str2))
        {
            CallbackSnakebarModel.getInstance().SnakebarMessage(context, msg, MessageConstant.toast_warning);
            return false;
        }
        return success;
    }
}
