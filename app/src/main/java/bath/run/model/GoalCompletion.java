package bath.run.model;

import bath.run.database.DatabaseHelper;
import bath.run.database.User;

/**
 * Created by mradl on 04/07/2018.
 */

public class GoalCompletion {

    public static double dailyStepsGoal = 200;
    private static double total = 0;
    DayOfTheWeekModel dotw = new DayOfTheWeekModel();
    StepsModel stepsModel = StepsModel.getInstance();

    public static double workOutRemainingPercentage(double currentValue, double goal) {
        total = ((currentValue / goal) * 100);

        if (total > 100) {
            total = 100;
        }
        return total;
    }

    public void goalReached(DatabaseHelper db) {
        int day = dotw.getDay();
        if (stepsModel.getDailysteps()>= stepsModel.getDailyStepsGoal()) {
            User.setDay(true, day);
            for (int i = 0; i <= dotw.DAYS_IN_WEEK; i++) {
                if (day == i) {
                    db.updateRow(day);
                }
            }
        }
    }
}
