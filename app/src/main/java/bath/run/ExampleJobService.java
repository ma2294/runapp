package bath.run;

import android.app.Activity;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.util.Log;

import com.google.android.gms.common.api.GoogleApiClient;

import bath.run.fragments.DissonanceFormFragment;

/**
 * Created by mradl on 08/07/2018.
 */

public class ExampleJobService extends JobService {
    private static final String TAG = "ExampleJobService";
    MainActivity mainActivity = new MainActivity();
    NotificationHelper notificationHelper;
    private boolean jobcancelled = false;
    private Context context;

    @Override
    public boolean onStartJob(JobParameters jobParameters) {
        Log.i(TAG, "onStartJob");
        doBackgroundWork(jobParameters);
        return true; //this runs in ui thread so must be handled upon completion
    }

    private void doBackgroundWork(final JobParameters jobParameters) {
        new Thread(new Runnable() {

            @Override
            public void run() {
                if (jobcancelled) {
                    return; //leave immediately.
                }
                context = MainActivity.mContext;
                notificationHelper = new NotificationHelper(context);
                notificationHelper.createNotificationChannel();
                notificationHelper.pushNotification();
                mainActivity.setSteps();
                Log.e(TAG, "Job Finished");
                jobFinished(jobParameters, false);
            }
        }).start();
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.i(TAG, "onStopJob: Job cancelled before Job Finished");
        jobcancelled = true;
        return true; //we want the job to reattempt to finish.
    }
}
