package bath.run.model;

import android.util.Log;

import bath.run.MainActivity;
import bath.run.NotificationHelper;
import bath.run.database.DatabaseHelper;
import bath.run.database.User;

/**
 * Created by mradl on 04/07/2018.
 */

public class GoalCompletion {

    private static double total = 0;
    DayOfTheWeekModel dotw = new DayOfTheWeekModel();
    StepsModel stepsModel = StepsModel.getInstance();
    DissonanceFormModel dissonanceFormModel = DissonanceFormModel.getInstance();
    User user = User.getInstance();

    public static double workOutRemainingPercentage(double currentValue, double goal) {
        total = ((currentValue / goal) * 100);

        if (total > 100) {
            total = 100;
        }
        return total;
    }

    public void newDailyGoal(int callType) {
        int i = stepsModel.getDailyStepsGoal() / 100;
        int totalGoal = (i + (dotw.HOURS_IN_DAY - dotw.getHour())) + dissonanceFormModel.getAvg();
        totalGoal = totalGoal / 5;
        if (callType == 1) { //user reaches step goal
            stepsModel.setDailyStepsGoal(stepsModel.getDailyStepsGoal() + totalGoal);
        } else { //user loses streak and thus does not reach step goal
            stepsModel.setDailyStepsGoal(stepsModel.getDailyStepsGoal() - totalGoal);
        }
    }

    public void goalReached(DatabaseHelper db) {
        int day = dotw.getDay();
        //if week resets this first conditional statement prevents bugs from occurring.
        if (dotw.getDay() > user.getLastday() + 1) { //2 days gap = reset streak.
            user.setStreak(0);
        }
        if (dotw.getDay() < user.getLastday() && stepsModel.getDailysteps() >= stepsModel.getDailyStepsGoal()) {
            newDailyGoal(1); // Will get overwritten each day the user reaches daily steps.
            user.setLastday(dotw.getDay());
            user.setStreak(user.getStreak() + 1);
            db.updateStepGoal();
            db.updateProfile(2);

            NotificationHelper notificationHelper = new NotificationHelper(MainActivity.mContext);
            notificationHelper.pushGoalReachedNotification();
            user.setDay(true, day);
            for (int i = 0; i <= dotw.DAYS_IN_WEEK; i++) {
                if (day == i) {
                    db.updateRow(day);
                }
            }
        }
        //this is the normal statement that will occur. but only when current day > lastday. i.e. fails and requires statement above when week resets.
        if (stepsModel.getDailysteps() >= stepsModel.getDailyStepsGoal()) {
            if (user.getLastday() < dotw.getDay()) {
                newDailyGoal(1); // Will get overwritten each day the user reaches daily steps.
                System.out.println(user.getLastday() + " < " + dotw.getDay());
                user.setLastday(dotw.getDay());
                user.setStreak(user.getStreak() + 1);
                db.updateStepGoal();
                db.updateProfile(2);

                NotificationHelper notificationHelper = new NotificationHelper(MainActivity.mContext);
                notificationHelper.pushGoalReachedNotification();
                user.setDay(true, day);
                for (int i = 0; i <= dotw.DAYS_IN_WEEK; i++) {
                    if (day == i) {
                        db.updateRow(day);
                    }
                }
            } else {
                System.out.println("GoalReached method: last day is not < current day (" + user.getLastday() + " > " + dotw.getDay());
            }
        }

        //finally check streak against beststreak
        if (user.getStreak() > user.getBestStreak()){
            user.setBestStreak(user.getStreak());
            db.updateProfile(0); //0 because there is no need to update anything other than best streak.
            Log.e("GoalCompletion", "New best streak ="+user.getBestStreak());
        }
    }
}
