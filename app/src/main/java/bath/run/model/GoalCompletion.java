package bath.run.model;

import android.util.Log;

import bath.run.database.DatabaseHelper;
import bath.run.database.User;

/**
 * Created by mradl on 04/07/2018.
 */

public class GoalCompletion {

    private static double total = 0;
    DayOfTheWeekModel dotw = new DayOfTheWeekModel();
    StepsModel stepsModel = StepsModel.getInstance();
    User user = User.getInstance();

    public static double workOutRemainingPercentage(double currentValue, double goal) {
        total = ((currentValue / goal) * 100);

        if (total > 100) {
            total = 100;
        }
        return total;
    }

    public void goalReached(DatabaseHelper db) {
        int day = dotw.getDay();
        //if week resets this first conditional statement prevents bugs from occurring.
        if(dotw.getDay() < user.getLastday() && stepsModel.getDailysteps() >= stepsModel.getDailyStepsGoal()) {
            user.setLastday(dotw.getCurrentDay());
            user.setStreak(user.getStreak() + 1);
            db.updateProfile();
            System.out.println("curent day" +dotw.getCurrentDay());
            System.out.println("streak ==== "+user.getStreak());
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
                System.out.println(user.getLastday() + " < "+dotw.getDay());
                user.setLastday(dotw.getCurrentDay());
                user.setStreak(user.getStreak() + 1);
                db.updateProfile();
                System.out.println("curent day" +dotw.getCurrentDay());
                System.out.println("streak ==== "+user.getStreak());
                user.setDay(true, day);
                for (int i = 0; i <= dotw.DAYS_IN_WEEK; i++) {
                    if (day == i) {
                        db.updateRow(day);
                    }
                }
            } else {
                System.out.println("GoalReached method: last day is not < current day ("+user.getLastday()+" > "+dotw.getDay());
            }
        }
    }
}
