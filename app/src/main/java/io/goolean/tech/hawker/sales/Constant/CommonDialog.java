package io.goolean.tech.hawker.sales.Constant;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import io.goolean.tech.hawker.sales.Constant.Singleton.CallbackSnakebarModel;
import io.goolean.tech.hawker.sales.R;
import io.goolean.tech.hawker.sales.Utililty.AppStatus;
import io.goolean.tech.hawker.sales.View.HomeActivity;

public class CommonDialog {

    public static void dialog_MessageAppStop(final Activity activity,final String message){
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.dialog_message_info);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setCancelable(false);
        TextView tv_Info =(TextView)dialog.findViewById(R.id.tv_info_id);
        tv_Info.setText(message);
        TextView btn_info_ok = (TextView)dialog.findViewById(R.id.button_ok_id);
        btn_info_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.exit(0);
            }
        });
        try {
            dialog.show();
        }catch(Exception e){

        }
    }


    public static void Show_Internt_Dialog(final Activity activity,String status) {
        final Dialog  internet_dialog = new Dialog(activity);
        internet_dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        internet_dialog.setContentView(R.layout.dialog_internet_layout);
        internet_dialog.setCanceledOnTouchOutside(false);
        internet_dialog.setCancelable(false);
        internet_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView success_text = (TextView) internet_dialog.findViewById(R.id.success_text);
        Button close = (Button) internet_dialog.findViewById(R.id.close_button);
        ImageView correct_image  = (ImageView)internet_dialog.findViewById(R.id.correct_image);
        TextView textView = (TextView) internet_dialog.findViewById(R.id.textView_success);
        correct_image.setBackgroundResource(R.drawable.ic_no_wifi);
        textView.setText(activity.getResources().getString(R.string.N1));
        success_text.setText(activity.getResources().getString(R.string.internet));

        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (AppStatus.getInstance(activity).isOnline()) {
                    internet_dialog.dismiss();
                    CallbackSnakebarModel.getInstance().SnakebarMessage(activity, "You are online", MessageConstant.toast_success);
                    //activity.finish();
                   /* activity.overridePendingTransition( 0, 0);
                    activity.startActivity(activity.getIntent());
                    activity.overridePendingTransition( 0, 0);*/
                } else {
                    CallbackSnakebarModel.getInstance().SnakebarMessage(activity, "Please check your internet connection", MessageConstant.toast_warning);
                }
            }
        });
        try {
            internet_dialog.show();
            if(status.equals("1")){
                internet_dialog.dismiss();
            }

        }catch(Exception e){

        }
    }

}
