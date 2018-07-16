package bath.run.model;

public class StepsModel {
    private static StepsModel instance = null;
    private int dailysteps;
    private int dailyStepsGoal = 150000; //TODO add contextualised step goals- must store in db.. or start at 5000, 7500 and 10000 based on user type and then accomodate based on previous day performance. If reached +10% if not -10%.

    private StepsModel() {
    }

    //Singleton. Enables me to change specific instance from notfications in future update.
    public static StepsModel getInstance() {
        if (instance == null) {
            instance = new StepsModel();
        }
        return instance;
    }

    public void setDailysteps(int dailysteps) {
        this.dailysteps = dailysteps;
    }
    public int getDailysteps(){
        return dailysteps;
    }


    public int getDailyStepsGoal() {
        return dailyStepsGoal;
    }

    public void setDailyStepsGoal(int dailyStepsGoal) {
        this.dailyStepsGoal = dailyStepsGoal;
    }
}
