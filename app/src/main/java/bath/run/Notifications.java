package bath.run;

public class Notifications {

    //Where User Care + User Frequency / 2 = 0. This user thus has little motivation to exercise.
    public static final class lowAverage {
        public static final String LOW_TEST = "Testing, 1,2 3...";
    }

    //Where User Care + User Frequency / 2 = 1. This user has some motivation to exercise
    public static final class medAverage {
        public static final class changeAction {
            public static final String IS_USER_TIRED = "Are you feeling tired? Perhaps it's" +
                    " time for a run!";
            public static final String NO_RECENT_MOVEMENT = "It seems you haven't moved for a " +
                    "while, did you know those who keep active are more likely to live a longer" +
                    " life?";
        }

        public static final class changePerception {
            public static final String ENHANCE_SELF_EFFICACY = "Looking at your past performance," +
                    " it seems that today's daily goal will be a breeze!";
        }

        public static final class changeBelief {
            public static final String IS_GOAL_TOO_HIGH = "Does your daily steps goal seem too" +
                    " high today?";
        }
    }

    //Where User Care + User Frequency / 2 = 2
    public static final class highAverage {
        public static final String HIGH_TEST = "Testing, 1,2 3...";
    }

    //Where is is no dissonance and the user is not competitive (in response to questionnaire).
    public static final class lowCompetitiveness {
        public static final String LOW_STRING = "You are doing amazing keep up the great work!";
        public static final String LOW_STRING_TWO = "You are on track with meeting your fitness targets.";
    }
    //Where there is no dissonance and the user is competitive (in response to questionnaire.
    public static final class highCompetitiveness {
        public static final String HIGH_STRING = "You are in the top 10% of our users!";
        public static final String HIGH_STRING_TWO = "Today, you have travelled the most distance" +
                "out of all of our users.";
    }
}
