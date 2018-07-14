package bath.run.database;


/**
 * Created by mradl on 07/07/2018.
 */

public class User {
    private static User instance = null;
    private int week = 0;
    private boolean monday = false;
    private boolean tuesday = false;
    private boolean wednesday = false;
    private boolean thursday = false;
    private boolean friday = false;
    private boolean saturday = false;
    private boolean sunday = false;
    private int streak = 0;
    private int yesterdayStreak = 0;
    private int currentday = 0;
    private int lastday = 0;


    private User(){

    }

    public static User getInstance(){
        if (instance == null) {
           instance = new User();
        }
        return instance;
    }

    public void setWeek(int week) {
        this.week = week;
    }

    public int getWeek() {
        return week;
    }

    public boolean isMonday() {
        return monday;
    }

    public void setMonday(boolean monday) {
        this.monday = monday;
    }

    public boolean isTuesday() {
        return tuesday;
    }

    public void setTuesday(boolean tuesday) {
        this.tuesday = tuesday;
    }

    public boolean isWednesday() {
        return wednesday;
    }

    public void setWednesday(boolean wednesday) {
        this.wednesday = wednesday;
    }

    public boolean isThursday() {
        return thursday;
    }

    public void setThursday(boolean thursday) {
        this.thursday = thursday;
    }

    public boolean isFriday() {
        return friday;
    }

    public void setFriday(boolean friday) {
        this.friday = friday;
    }

    public boolean isSaturday() {
        return saturday;
    }

    public void setSaturday(boolean saturday) {
        this.saturday = saturday;
    }

    public boolean isSunday() {
        return sunday;
    }

    public void setSunday(boolean sunday) {
        this.sunday = sunday;
    }

    public int getStreak() {
        return streak;
    }

    public void setStreak(int streak) {
        this.streak = streak;
    }


    public void setDay(boolean stepsComplete, int day) {
        switch (day) {
            case 1:
                setMonday(stepsComplete);
                break;

            case 2:
                setTuesday(stepsComplete);
                break;

            case 3:
                setWednesday(stepsComplete);
                break;

            case 4:
                setThursday(stepsComplete);
                break;

            case 5:
                setFriday(stepsComplete);
                break;

            case 6:
                setSaturday(stepsComplete);
                break;

            case 7:
                setSunday(stepsComplete);
                break;

            default:
                //
                break;
        }
    }



    public int getLastday() {
        return lastday;
    }

    public void setLastday(int lastday) {
        this.lastday = lastday;
    }

    @Override
    public String toString() {
        return "Monday steps: "
                + isMonday() + ". Tuesday steps: "
                + isTuesday() + ". Wednesday steps: "
                + isWednesday() + ". Thursday steps: "
                + isThursday() + ". Friday steps: "
                + isFriday() + ". Saturday steps: "
                + isSaturday() + ". Sunday steps: "
                + isSunday() + ". Week of year: " + getWeek();
    }

}
