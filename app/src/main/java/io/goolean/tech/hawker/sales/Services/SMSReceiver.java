package io.goolean.tech.hawker.sales.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import com.firebase.jobdispatcher.Constraint;
import com.firebase.jobdispatcher.FirebaseJobDispatcher;
import com.firebase.jobdispatcher.GooglePlayDriver;
import com.firebase.jobdispatcher.Job;
import com.firebase.jobdispatcher.Lifetime;
import com.firebase.jobdispatcher.RetryStrategy;
import com.firebase.jobdispatcher.Trigger;


public class SMSReceiver extends BroadcastReceiver {
    private static final String Job_Tag = "my_job_tag";
    FirebaseJobDispatcher jobDispatcher;

    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context, "hello", Toast.LENGTH_LONG).show();
        try {


        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            jobDispatcher = new FirebaseJobDispatcher(new GooglePlayDriver(context));
            Job job = jobDispatcher.newJobBuilder().
                    setService(MyJobService.class).
                    setLifetime(Lifetime.FOREVER).
                    setRecurring(true).
                    setTag(Job_Tag).
                    setTrigger(Trigger.executionWindow(0, 5)).
                    // setTrigger(JobDispatcherUtils.periodicTrigger(20,1)).
                            setRetryStrategy(RetryStrategy.DEFAULT_EXPONENTIAL).
                            setReplaceCurrent(false)
                    .setConstraints(Constraint.ON_ANY_NETWORK)
                    .build();
            jobDispatcher.mustSchedule(job);

        }

        }

        catch (Exception ex)
        {

        }
    }


}
