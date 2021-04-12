package io.goolean.tech.hawker.sales.Utililty.OtpReader;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.text.TextUtils;

public class SmsReceiver extends BroadcastReceiver {
    private static SmsListener mListener;
    Boolean b;
    String abcd;
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle data  = intent.getExtras();
        Object[] pdus = (Object[]) data.get("pdus");
        for(int i=0;i<pdus.length;i++){
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);
            String sender = smsMessage.getDisplayOriginatingAddress();
            b=sender.endsWith("HAWKER");  //Just to fetch otp sent from WNRCRP
            String messageBody = smsMessage.getMessageBody();
            abcd=messageBody.replaceAll("[^0-9]","");   // here abcd contains otp
            if(!TextUtils.isEmpty(abcd)){
                if(b==true) {
                    mListener.messageReceived(abcd);  // attach value to interface
                } else {
                }
            }else {

            }
        }
    }
    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }
}
