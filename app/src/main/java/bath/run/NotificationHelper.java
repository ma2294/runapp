package bath.run;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import bath.run.model.DayOfTheWeekModel;
import bath.run.model.DissonanceFormModel;
import bath.run.model.GoalCompletion;
import bath.run.model.StepsModel;

public class NotificationHelper {
    private static String TAG = "NotificiationHelper";
    DissonanceFormModel dissonanceFormModel = DissonanceFormModel.getInstance();
    StepsModel stepsModel = StepsModel.getInstance();
    DayOfTheWeekModel dotw = new DayOfTheWeekModel();
    GoalCompletion gc = new GoalCompletion();
    private Context mContext;

    public NotificationHelper(Context mContext) {
        this.mContext = mContext;
    }

    public void createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.i(TAG, "createNotificationChannel: ");
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            CharSequence name = "Run";
            String description = "Random notification from Run";
            NotificationChannel channel = new NotificationChannel("msg", name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = mContext.getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    //TODO push tailored notificaion based on singleton info.
    public void pushNotification() {
        int total = dissonanceFormModel.getAvg();

            /*
                if (currentsteps   goal)
                    ------------ < ----    - dissonance = true.
                    time passed     24
             */

            //TODO implement this method as described below
            //Returns true = dissonance. False = expected performance and no dissonance.
            /*if certain hour remaining > 10 i.e. still early in the day (AND DISSONANCE= TURE)
             then send message aimed to change perception of user first. This will start their day
             positively.

             If hours remaining are between 4-7. the attempt to change belief. Perhaps adjusting the
             users current daily goal will result in increased motivation to succeed.

             If all fails and current hours left are between 0-2, then attempt to change the users
             action. For example, prompt they have a few hours remaining, or haven't done many steps
             recently.
              */
            if(workDissonance(stepsModel.getDailysteps(), dotw.getHour(), stepsModel.getDailyStepsGoal(), dotw.HOURS_IN_DAY)) {
                switch (total) {
                    case 0:
                        System.out.println("Set to 0");
                        System.out.println(Notifications.lowAverage.LOW_TEST);
                        break;
                    case 1:
                        System.out.println("Set to 1");
                        System.out.println(Notifications.medAverage.changeAction.IS_USER_TIRED);
                        break;
                    case 2:
                        System.out.println("Set to 2");
                        System.out.println(Notifications.highAverage.HIGH_TEST);
                        break;
                    default:
                        System.out.println("there must be more..");
                }
                //dissonance = true
            } else {

            }



        NotificationCompat.Builder mBuilder = new NotificationCompat
                .Builder(mContext, "msg")
                .setSmallIcon(R.drawable.ic_launcher_background)
                .setContentTitle("Test notification")
                .setContentText("Test..................")
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText("much longer text that cannot fit one line."))
                .setPriority(NotificationCompat.PRIORITY_MAX);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(2, mBuilder.build());
    }

    private boolean workDissonance(int dailysteps, int hour, int dailyStepsGoal, int hours_in_day) {
        int t = dailysteps/hour;
        int u = dailyStepsGoal/hours_in_day;

        if ( t < u) {
            return true;
        } else {
            return false;
        }
    }

}
