package bath.run;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import bath.run.model.DayOfTheWeekModel;
import bath.run.model.DissonanceFormModel;
import bath.run.model.GoalCompletion;
import bath.run.model.MotivationalMessages;
import bath.run.model.NotificationModel;
import bath.run.model.StepsModel;

public class NotificationHelper {
    private static String TAG = "NotificiationHelper";
    DissonanceFormModel dissonanceFormModel = DissonanceFormModel.getInstance();
    StepsModel stepsModel = StepsModel.getInstance();
    DayOfTheWeekModel dotw = new DayOfTheWeekModel();
    NotificationModel notificationModel = new NotificationModel();
    GoalCompletion gc = new GoalCompletion();
    boolean userProgress = stepsModel.getDailyStepsGoal() - stepsModel.getDailysteps() > stepsModel.getDailysteps() * 0.5;
    String response = "";
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
        Bitmap largeIcon = BitmapFactory.decodeResource(MainActivity.mContext.getResources(), R.drawable.fireworks);


        String s = "";
        int remaining = stepsModel.getDailyStepsGoal() - stepsModel.getDailysteps();
        if (remaining < 0) {
            remaining = 0;
        }

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
    largeIcon = BitmapFactory.decodeResource(MainActivity.mContext.getResources(), R.drawable.success);
             If hours remaining are between 4-7. the attempt to change belief. Perhaps adjusting the
             users current daily goal will result in increased motivation to succeed.

             If all fails and current hours left are between 0-4, then attempt to change the users
             action. For example, prompt they have a few hours remaining, or haven't done many steps
             recently.
             /11+ hours remaining - Change Perception
             //5-10 hours remaining - Change Goal
             // less than 5 hours remaining - Change action
             //TODO CHECKS DISSONANCE
              */
        if (workDissonance(stepsModel.getDailysteps(), dotw.getHour(), stepsModel.getDailyStepsGoal(), dotw.HOURS_IN_DAY)) {

            //TODO CHECKS HOURS REMAINING 0-24

            int t = dotw.HOURS_IN_DAY - dotw.getHour();

            //TODO CHECK_USER_TYPE
            if (dissonanceFormModel.getAvg() == 0) {
                //USER IS TYPE: LOW
                s = lowUser(t);
            } else if (dissonanceFormModel.getAvg() == 1) {
                Log.e(TAG, "pushNotification: Med user");
                //USER IS TYPE: MED
                s = medUser(t);
            } else if (dissonanceFormModel.getAvg() == 2) {
                //USER IS TYPE: HIGH
                s = highUser(t);
                Log.e(TAG, "pushNotification: High user");
            } else {
                Toast.makeText(mContext, "Please complete the form under dissonance to access this feature.", Toast.LENGTH_SHORT).show();
                return;
            }

        } else {//dissonance = false
            if (total < 1) {
                s = Notifications.lowCompetitiveness.LOW_STRING;
            } else if (total >= 1) {
                s = Notifications.highCompetitiveness.HIGH_STRING;
            }
        }


        //TODO intent must link with notification. I.e. Prompting user to change goal opens that activity.


        Intent intent = new Intent(MainActivity.mContext, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.mContext, 0, intent, 0);

        //TODO check msg type and assign appropriate bitmap icon.


        NotificationCompat.Builder mBuilder = new NotificationCompat
                .Builder(mContext, "msg")
                .setSmallIcon(R.drawable.run)
                .setLargeIcon(largeIcon)
                .setContentTitle(s)
                .setContentText("You have " + remaining + " steps remaining.")
                .setContentIntent(pendingIntent)
                .setColor(Color.BLUE)
                .setPriority(NotificationCompat.PRIORITY_MAX);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);

// notificationId is a unique int for each notification that you must define
        notificationManager.notify(2, mBuilder.build());
    }

    private String lowUser(int t) {
        if (t <= 3) { //3 hours remaining - CHECK IF USER IS CLOSE OR FAR FROM GOAL
            if (userProgress) {
                //user is too far away.
                response = notificationModel.lowUserUrgent(10);
            } else { //user can make it
                response = notificationModel.lowUserUrgent(MotivationalMessages.getRandomNumberInRange(1, 2));
            }
        } else if (t >= 4 && t <= 10) { //4-8
            response = notificationModel.lowUserMiddle(MotivationalMessages.getRandomNumberInRange(1, 3));
        } else if (t >= 11) { //9+
            if (stepsModel.getDailysteps() < stepsModel.getDailyStepsGoal() * 0.03) {
                response = notificationModel.lowUserMild(10);
            } else {
                response = notificationModel.lowUserMild(MotivationalMessages.getRandomNumberInRange(1, 2));
            }
        }
        return response;
    }

    private String medUser(int t) {
        if (t <= 3) { //3 hours remaining - CHECK IF USER IS CLOSE OR FAR FROM GOAL
            if (userProgress) {
                //user is too far away.
                response = notificationModel.medUserUrgent(10);
            } else { //user can make it
                response = notificationModel.medUserUrgent(MotivationalMessages.getRandomNumberInRange(1, 3));
            }
        } else if (t >= 4 && t <= 10) { //4-8
            response = notificationModel.medUserMiddle(MotivationalMessages.getRandomNumberInRange(1, 5));
        } else if (t >= 11) { //9+
            if (stepsModel.getDailysteps() < stepsModel.getDailyStepsGoal() * 0.03) {
                response = notificationModel.medUserMild(10);
            } else {
                response = notificationModel.medUserMild(MotivationalMessages.getRandomNumberInRange(1, 5));
            }
        }
        return response;
    }

    private String highUser(int t) {
        if (t <= 3) { //3 hours remaining - CHECK IF USER IS CLOSE OR FAR FROM GOAL
            if (userProgress) {
                //user is too far away.
                response = notificationModel.highUserUrgent(10);
            } else { //user can make it
                response = notificationModel.highUserUrgent(MotivationalMessages.getRandomNumberInRange(1, 5));
            }
        } else if (t >= 4 && t <= 10) { //4-8
            response = notificationModel.highUserMiddle(MotivationalMessages.getRandomNumberInRange(1, 7));
        } else if (t >= 11) { //9+
            if (stepsModel.getDailysteps() < stepsModel.getDailyStepsGoal() * 0.03) {
                response = notificationModel.highUserMild(10);
            } else {
                response = notificationModel.highUserMild(MotivationalMessages.getRandomNumberInRange(1, 7));
            }
        }
        return response;
    }

    private boolean workDissonance(int dailysteps, int hour, int dailyStepsGoal, int hours_in_day) {
        if (hour == 0) {
            hour = 1;
        }
        int t = dailysteps / hour;
        int u = dailyStepsGoal / hours_in_day;

        if (t < u) {
            return true;
        } else {
            return false;
        }
    }

}
