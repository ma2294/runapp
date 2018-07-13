package bath.run.database;

/**
 * Created by mradl on 07/07/2018.
 */

public class UserDbSchema {

    public static final class UserTable {
        //Set name of table
        public static final String NAME = "user";
        public static final String TABLE_NAME_DISSONANCE = "dissonance";
        public static final String TABLE_NAME_PROFILE = "profile";

        public static final String USER_DISSONANCE_ENTRIES =
                "CREATE TABLE "
                        + TABLE_NAME_DISSONANCE + "(question1 INTEGER, question2 INTEGER, question3 INTEGER, answered boolean)";

        public static final String USER_PROFILE_ENTRIES =
                "CREATE TABLE "
                        + TABLE_NAME_PROFILE + "(name TEXT, weight INTEGER, height INTEGER, weightprompt INTEGER)";

        public static final String SQL_CREATE_ENTRIES =
                "CREATE TABLE "
                        + NAME + "(week integer, "
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
