package bath.run.model;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by mradl on 04/07/2018.
 */

public class DayOfTheWeekModel {

    private String today = "";
    private int day = 0;
    public final int DAYS_IN_WEEK = 7;
    public final int HOURS_IN_DAY = 24;



    Calendar sCalendar = Calendar.getInstance();
    public int getHour() {
        System.out.println(sCalendar.get(Calendar.HOUR_OF_DAY));
        return sCalendar.get(Calendar.HOUR_OF_DAY);
    }
    public int getWeek() {

        return sCalendar.get(Calendar.WEEK_OF_YEAR);
    }
    public int getCurrentDay(){
        return sCalendar.DAY_OF_YEAR;
    }
      public int getDay(){
        today = sCalendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, Locale.getDefault());
        switch (today) {
            case "Monday":
                day = 1;
                break;

            case "Tuesday":
                day = 2;
                break;

            case "Wednesday":
                day = 3;
                break;

            case "Thursday":
                day = 4;
                break;

            case "Friday":
                day = 5;
                break;

            case "Saturday":
                day = 6;
                break;

            case "Sunday":
                day = 7;
                break;

            default:
                day = 1;
        }

        return day;

    }



}
