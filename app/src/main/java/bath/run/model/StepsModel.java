package bath.run.model;

public class StepsModel {
    private static StepsModel instance = null;
    private int dailysteps;
    private int dailyStepsGoal;

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
