package bath.run.database;

/**
 * Created by mradl on 07/07/2018.
 */

public class UserDbSchema {

    public static final class UserTable {
        //Set name of table
        public static final String TABLE_NAME_USER = "user";
        public static final String TABLE_NAME_DISSONANCE = "dissonance";
        public static final String TABLE_NAME_PROFILE = "profile";
        public static final String TABLE_NAME_STEPS_GOAL = "stepsgoal";
        public static final String DATABASE_NAME = "user.db";

        public static final String USER_DISSONANCE_ENTRIES =
                "CREATE TABLE "
                        + TABLE_NAME_DISSONANCE + "(question1 INTEGER, question2 INTEGER, question3 INTEGER, answered boolean)";

        public static final String USER_PROFILE_ENTRIES =
                "CREATE TABLE "
                        + TABLE_NAME_PROFILE + "(name TEXT, weight INTEGER, height INTEGER, weightprompt INTEGER, streak INTEGER, lastday INTEGER, beststreak INTEGER)";
        public static final String USER_STEPS_GOAL_ENTRIES =
                "CREATE TABLE "
                        + TABLE_NAME_STEPS_GOAL + "(stepgoal INTEGER)";

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE "
                        + TABLE_NAME_USER + "(week integer, "
                        + Cols.MONDAY + " boolean, "
                        + Cols.TUESDAY + " boolean, "
                        + Cols.WEDNESDAY + " boolean," +
                        "" + Cols.THURSDAY + " boolean, "
                        + Cols.FRIDAY + " boolean, "
                        + Cols.SATURDAY + " boolean, "
                        + Cols.SUNDAY + " boolean)";


        public static final class Cols {
            public static final String WEEK = "week";
            public static final String MONDAY = "monday";
            public static final String TUESDAY = "tuesday";
            public static final String WEDNESDAY = "wednesday";
            public static final String THURSDAY = "thursday";
            public static final String FRIDAY = "friday";
            public static final String SATURDAY = "saturday";
            public static final String SUNDAY = "sunday";
          }

        public static final class DissonanceCols {
            public static final String Q1 = "question1";
            public static final String Q2 = "question2";
            public static final String Q3 = "question3";
        }


    }
}
