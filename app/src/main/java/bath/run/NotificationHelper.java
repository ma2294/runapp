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

import bath.run.database.DatabaseHelper;
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
    DatabaseHelper dbHelper = new DatabaseHelper(MainActivity.mContext);
    boolean userProgress = stepsModel.getDailyStepsGoal() - stepsModel.getDailysteps() > stepsModel.getDailysteps() * 0.5;
    String response = "";
    private Context mContext;
    int notificationId = 0;

    String s = "";
    int remaining = stepsModel.getDailyStepsGoal() - stepsModel.getDailysteps();

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

       /*
                if (currentsteps   goal)
                    ------------ < ----    - dissonance = true.
                    time passed     24
             */

    public void pushScheduledNotification() {
        int total = dissonanceFormModel.getAvg();

        if (remaining < 0) {
            remaining = 0;
        }

        if(userInRange()){
            Log.e(TAG, "pushScheduledNotification: USER IN RANGE "+dotw.getHour());
        Bitmap largeIcon = BitmapFactory.decodeResource(MainActivity.mContext.getResources(), R.drawable.question);
        if (workDissonance(stepsModel.getDailysteps(), dotw.getHour(), stepsModel.getDailyStepsGoal(), dotw.HOURS_IN_DAY)) {
            int t = dotw.HOURS_IN_DAY - dotw.getHour();

            if (dissonanceFormModel.getAvg() == 0) {   //USER IS TYPE: LOW
                s = lowUser(t);
            } else if (dissonanceFormModel.getAvg() == 1) {   //USER IS TYPE: MED
                s = medUser(t);
                largeIcon = largeIconSelector(t);
            } else if (dissonanceFormModel.getAvg() == 2) {   //USER IS TYPE: HIGH
                s = highUser(t);
                largeIcon = largeIconSelector(t);
            } else {
                Toast.makeText(mContext, "Please complete the form under dissonance to access this feature.", Toast.LENGTH_SHORT).show();
                return;
            }
            notificationId = NotificationModel.ID_DISSONANCE; //dissonance id
        } else {//dissonance = false
            if (dissonanceFormModel.getCompetitiveness() <= 1) { //Low/ Med competitiveness
                s = notificationModel.lowMotivation(MotivationalMessages.getRandomNumberInRange(1,3));
                largeIcon = BitmapFactory.decodeResource(MainActivity.mContext.getResources(), R.drawable.run);
            } else if (dissonanceFormModel.getCompetitiveness() > 1) { //Highly competitive individual
                s = notificationModel.highMotivation(MotivationalMessages.getRandomNumberInRange(1,6));
                largeIcon = BitmapFactory.decodeResource(MainActivity.mContext.getResources(), R.drawable.winner);
            }
            notificationId = NotificationModel.ID_NO_DISSONANCE; // no dissonance id
        }
        sendNotification(s, largeIcon, remaining, notificationId);
    } else {
            Log.e(TAG, "pushScheduledNotification: USER NOT IN RANGE "+dotw.getHour());
        }
    }

    private boolean userInRange() {
        //TODO set these values in DB and Pull from them in Model class.
        int boundaryStart = 8;
        int boundaryFinish = 22;
        //If time is within set bounds OR user has met their daily step goal
        if(dotw.getHour() >= boundaryStart && dotw.getHour() <= boundaryFinish ||
                !(stepsModel.getDailysteps() > stepsModel.getDailyStepsGoal())) {
            return true; // sending notifications
        } else{
            return  false; //stop sending notifications
        }
    }

    public void pushGoalReachedNotification() {
        s = notificationModel.goalReached(MotivationalMessages.getRandomNumberInRange(1,4));
        Bitmap largeIcon = BitmapFactory.decodeResource(MainActivity.mContext.getResources(), R.drawable.fireworks);
        notificationId =  NotificationModel.ID_GOAL_COMPLETE;
        if(remaining < 0){
            remaining = 0;
        }
        sendNotification(s, largeIcon, remaining, notificationId);
    }

    private void sendNotification(String s, Bitmap largeIcon, int remaining, int notificationId) {
        Intent intent = new Intent(MainActivity.mContext, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(MainActivity.mContext, 0, intent, 0);

        String stepsRemainingMsg = "";
        if (stepsModel.getDailysteps() >= stepsModel.getDailyStepsGoal()) {
            stepsRemainingMsg = "You have completed your daily steps.";
        } else {
            stepsRemainingMsg = "You have " + remaining + " steps remaining.";
        }

        NotificationCompat.Builder mBuilder = new NotificationCompat
                .Builder(mContext, "msg")
                .setSmallIcon(R.drawable.run)
                .setLargeIcon(largeIcon)
                .setContentTitle(stepsRemainingMsg)
                .setContentText(s)
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(s))
                .setContentIntent(pendingIntent)
                .setColor(Color.BLUE)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_MAX);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(mContext);

        // notificationId is a unique int for each notification that you must define
        notificationManager.notify(notificationId, mBuilder.build());
    }


    private Bitmap largeIconSelector(int t) {
        switch (t) {
            case 0:
            case 1:
            case 2:
            case 3:
                return BitmapFactory.decodeResource(MainActivity.mContext.getResources(), R.drawable.question);
            case 4:
            case 5:
            case 6:
                return BitmapFactory.decodeResource(MainActivity.mContext.getResources(), R.drawable.muscle);
            case 7:
            case 8:
            case 9:
                return BitmapFactory.decodeResource(MainActivity.mContext.getResources(), R.drawable.medal);
            case 10:
            case 11:
            case 12:
            case 13:
            case 14:
                return BitmapFactory.decodeResource(MainActivity.mContext.getResources(), R.drawable.run);

            default:
                return BitmapFactory.decodeResource(MainActivity.mContext.getResources(), R.drawable.run);
        }

    }

    private String lowUser(int t) {
        if (t <= 6) { //3 hours remaining - CHECK IF USER IS CLOSE OR FAR FROM GOAL
            if (userProgress) {
                //user is too far away.
                response = notificationModel.lowUserUrgent(10);
                stepsModel.setDailyStepsGoal(generateNewUserGoal());
                dbHelper.updateStepGoal();
            } else { //user can make it
                response = notificationModel.lowUserUrgent(MotivationalMessages.getRandomNumberInRange(1, 3));
            }
        } else if (t >= 7 && t <= 10) { //4-8
            response = notificationModel.lowUserMiddle(MotivationalMessages.getRandomNumberInRange(1, 5));
        } else if (t >= 11) { //9+
            if (stepsModel.getDailysteps() < stepsModel.getDailyStepsGoal() * 0.03) {
                response = notificationModel.lowUserMild(10);
            } else {
                response = notificationModel.lowUserMild(MotivationalMessages.getRandomNumberInRange(1, 4));
            }
        }
        return response;
    }

    private String medUser(int t) {
        if (t <= 6) { //3 hours remaining - CHECK IF USER IS CLOSE OR FAR FROM GOAL
            if (userProgress) {
                //user is too far away.
                response = notificationModel.medUserUrgent(10);
                stepsModel.setDailyStepsGoal(generateNewUserGoal());
                dbHelper.updateStepGoal();
            } else { //user can make it
                response = notificationModel.medUserUrgent(MotivationalMessages.getRandomNumberInRange(1, 4));
            }
        } else if (t >= 7 && t <= 10) { //4-8
            response = notificationModel.medUserMiddle(MotivationalMessages.getRandomNumberInRange(1, 6));
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
        if (t <= 6) { //3 hours remaining - CHECK IF USER IS CLOSE OR FAR FROM GOAL
            if (userProgress) {
                //user is too far away.
                response = notificationModel.highUserUrgent(10);
                stepsModel.setDailyStepsGoal(generateNewUserGoal());
                dbHelper.updateStepGoal();
            } else { //user can make it
                response = notificationModel.highUserUrgent(MotivationalMessages.getRandomNumberInRange(1, 6));
            }
        } else if (t >= 7 && t <= 10) { //4-8
            response = notificationModel.highUserMiddle(MotivationalMessages.getRandomNumberInRange(1, 8));
        } else if (t >= 11) { //9+
            if (stepsModel.getDailysteps() < stepsModel.getDailyStepsGoal() * 0.03) {
                response = notificationModel.highUserMild(10);
            } else {
                response = notificationModel.highUserMild(MotivationalMessages.getRandomNumberInRange(1, 7));
            }
        }
        return response;
    }

    private int generateNewUserGoal() {
        //Inverse User Type and Add 1 to get a scaled new goal result.
        int inverseAvg = 1;
        if (dissonanceFormModel.getAvg() == 0) {
            inverseAvg = 3;
        } else if (dissonanceFormModel.getAvg() == 1) {
            inverseAvg = 2;
        } else if (dissonanceFormModel.getAvg() == 2) {
            inverseAvg = 1;
        }
        double result = ((stepsModel.getDailyStepsGoal() - stepsModel.getDailysteps()) / (Math.pow(2, inverseAvg))) + ((stepsModel.getDailyStepsGoal() - stepsModel.getDailysteps()) / 10);
        System.out.println("Goal was " + stepsModel.getDailyStepsGoal());
        int intResult = stepsModel.getDailyStepsGoal() - (int) result;
        System.out.println("Minused " + intResult);
        return intResult;
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
