package bath.run.model;

import android.util.Log;

import bath.run.database.User;

public class NotificationModel {
    public static final int ID_DISSONANCE = 0;
    public static final int ID_NO_DISSONANCE = 1;
    public static final int ID_GOAL_COMPLETE = 2;
    private static final String TAG = "NotificationModel";
    UserProfileModel userProfileModel = UserProfileModel.getInstance();
    User user = User.getInstance();
    StepsModel stepsModel = StepsModel.getInstance();
    private String response = "";

    //Individual who is considered as : LOW in terms of exercise.
    private String userTired = "Are you feeling tired? Perhaps it's time for a run!";
    private String userClose = "You are almost there " + userProfileModel.getName() + ". Finish strong!";
    private String userTooFar = "Your daily step goal has been adjusted. What are you waiting for?!";

    private String userMidSlump = "Are you experiencing the mid day slump? Perhaps it is time to get some fresh air.";
    private String userMidEnergy = "Are you lacking energy? Go outside and stretch your legs.";
    private String userMidProductivity = "Did you know a change of scenery is known to boost productivity?";
    private String userMidMessage = "Been sat still for a while? Take a few minutes break and come back refreshed.";

    private String userMild = "Start your morning outdoors.";
    private String userMildStillInBed = "Still tired and want to go back to bed? Exercise is a healthy way to combat this.";
    private String userMildSelfEfficacy = "Say to yourself - I can achieve my goals today";
    private String userMildSelfEfficacy2 = "Say to yourself - I will achieve my goals today";

    //Individual who is considered as : MED in terms of exercise.
    private String userMedMidUrgent = "Finish your remaining steps to cross off one more day of exercise this week";

    private String userMedMid = "Are you lacking energy? Go outside for a walk.";

    private String userMedMidSelfEfficiacy = "An early morning walk will start your day positive.";

    //
    private String userHighUrgent1 = "You have not met your daily goal. To continue your progress you should go for a run.";
    private String userHighUrgent2 = "To remain a physically active individual, you should complete your goal before the day is up. ";

    private String userHighMid = "You are currently under your step per hour goal. It would be advised to correct this in order to maintain your daily streak";
    private String userHighMid2 = "The weather conditions in your local area are perfect for a run!";

    private String userHighMild = "Planning to run today? Why not get it over and done with!";
    private String userHighMild2 = "Why not try some morning stretches? Athletes often perform these to enhance their exercise.";

    //INTENTIONALLY CAUSE DISSONANCE
    private String dissonanceHighNegative = "Those who conduct infrequent exercise are more at risk of heart conditions";
    private String dissonanceHighNegative2= "";
    //REDUCE DISSONANCE
    private String completeSteps = "Woo! Your streak has increased to " + user.getStreak() + ".";
    private String completeSteps2 = "With taking that final step you have reached your daily step goal!";
    private String completeSteps3 = stepsModel.getDailyStepsGoal() + " steps? " + " Completed.";
    private String dissonanceHighPositive = "By continuously meeting your fitness goals, you are decreasing your chances of experiencing a heart condition later in life.";


    //TODO implement these below and add to Notification helper in motivation section where dissonance is not detected.
    //USER HAS NO DISSONANCE BUT NEED TO SUSTAIN MOTIVATION && LOW MOTIVATION
    private String lowstring = "Consistency with exercise is the key to securing a healthy lifestyle.";
    private String lowstring2 = "Work hard to meet your goals today. Rest tomorrow.";
    private String lowstring3 = "If you live for today and save for tomorrow you are more likely to have a short filled life.";



    //USER HAS NO DISSONANCE BUT NEED TO SUSTAIN MOTIVATION && HIGH MOTIVATION
    private String highstring = "You are currently ranked in 5th place out our 10 active users"; //causes dissonance for someone competitive and will motivate exercise
    private String highstring2 = "Today, you have travelled the most distance out of all of our users."; //this message will be receieved after the previous one as feedback to the user.
    private String highstring3 = "Finish your remaining steps to join our 3 other daily victors!";

    public String lowUserUrgent(int value) {
        switch (value) {
            case 1:
                response = userTired;
                break;

            case 2:
                response = userClose;
                break;
            case 3:
                response = dissonanceHighNegative;
                break;
            case 10:
                response = userTooFar;
                break;
            default:
                Log.e(TAG, "lowUserUrgent: Value does not exist: " + value);
        }
        return response;
    }

    public String lowUserMiddle(int value) {
        switch (value) {
            case 1:
                response = userMidSlump;
                break;

            case 2:
                response = userMidEnergy;
                break;

            case 3:
                response = userMidProductivity;
                break;
            case 4:
                response = dissonanceHighNegative;
                break;
            case 5:
                response = userMidMessage;
                break;
            default:
                Log.e(TAG, "lowUserUrgent: Value does not exist: " + value);
        }
        return response;
    }

    public String lowUserMild(int value) {
        switch (value) {
            case 1:
                response = userMild;
                break;

            case 2:
                response = userClose;
                break;

            case 3:
                response = userMildSelfEfficacy;
                break;

            case 4:
                response = userMildSelfEfficacy2;
                break;
            case 10:
                response = userMildStillInBed;
                break;
            default:
                Log.e(TAG, "lowUserUrgent: Value does not exist: " + value);
        }
        return response;
    }

    /*
    Med Level User
     */

    public String medUserUrgent(int value) {
        switch (value) {
            case 1:
                response = userTired;
                break;

            case 2:
                response = userClose;
                break;

            case 3:
                response = userMedMidUrgent;
                break;
            case 4:
                response = dissonanceHighNegative;
                break;
            case 10:
                response = userTooFar;
                break;
            default:
                Log.e(TAG, "lowUserUrgent: Value does not exist: " + value);
        }
        return response;
    }

    public String medUserMiddle(int value) {
        switch (value) {
            case 1:
                response = userMidSlump;
                break;

            case 2:
                response = userMidEnergy;
                break;

            case 3:
                response = userMidProductivity;
                break;

            case 4:
                response = userMidMessage;
                break;

            case 5:
                response = dissonanceHighNegative;
                break;
            case 6:
                response = userMedMid;
                break;

            default:
                Log.e(TAG, "lowUserUrgent: Value does not exist: " + value);
        }
        return response;
    }

    public String medUserMild(int value) {
        switch (value) {
            case 1:
                response = userMild;
                break;

            case 2:
                response = userClose;
                break;
            case 3:
                response = userMildSelfEfficacy;
                break;

            case 4:
                response = userMildSelfEfficacy2;
                break;
            case 5:
                response = userMedMidSelfEfficiacy;
                break;
            case 10:
                response = userMildStillInBed;
                break;
            default:
                Log.e(TAG, "lowUserUrgent: Value does not exist: " + value);
        }
        return response;
    }

    /*
    High end users
     */

    public String highUserUrgent(int value) {
        switch (value) {
            case 1:
                response = userTired;
                break;

            case 2:
                response = userClose;
                break;

            case 3:
                response = userMedMidUrgent;
                break;

            case 4:
                response = userHighUrgent1;
                break;
            case 5:
                response = userHighUrgent2;
                break;
            case 6:
                response = dissonanceHighNegative;
                break;
            case 10:
                response = userTooFar;
                break;
            default:
                Log.e(TAG, "lowUserUrgent: Value does not exist: " + value);
        }
        return response;
    }

    public String highUserMiddle(int value) {
        switch (value) {
            case 1:
                response = userMidSlump;
                break;

            case 2:
                response = userMidEnergy;
                break;

            case 3:
                response = userMidProductivity;
                break;

            case 4:
                response = userMidMessage;
                break;

            case 5:
                response = userMedMid;
                break;

            case 6:
                response = userHighMid;
                break;
            case 7:
                response = userHighMid2;
                break;
            case 8:
                response = dissonanceHighNegative;
                break;
            default:
                Log.e(TAG, "lowUserUrgent: Value does not exist: " + value);
        }
        return response;
    }

    public String highUserMild(int value) {
        switch (value) {
            case 1:
                response = userMild;
                break;

            case 2:
                response = userClose;
                break;
            case 3:
                response = userMildSelfEfficacy;
                break;

            case 4:
                response = userMildSelfEfficacy2;
                break;
            case 5:
                response = userMedMidSelfEfficiacy;
                break;
            case 6:
                response = userHighMild;
                break;
            case 7:
                response = userHighMild2;
                break;
            case 10:
                response = userMildStillInBed;
                break;
            default:
                Log.e(TAG, "lowUserUrgent: Value does not exist: " + value);
        }
        return response;
    }

    public String goalReached(int value) {
        switch (value) {
            case 1:
                response = completeSteps;
                break;

            case 2:
                response = completeSteps2;
                break;
            case 3:
                response = completeSteps3;
                break;
            case 4:
                response = dissonanceHighPositive;
                break;
            default:
                //
        }
        return response;
    }

    public String lowMotivation(int value){
        switch(value) {
            case 1:
                response = lowstring;
                break;
            case 2:
                response = lowstring2;
                break;
            case 3:
                response = lowstring3;
                break;
            default:
                //
        }
        return response;
    }

    public String highMotivation(int value){
        switch(value) {
            case 1:
                response = lowstring;
                break;
            case 2:
                response = lowstring2;
                break;
            case 3:
                response = lowstring3;
                break;

            case 4:
                response = highstring;
                break;
            case 5:
                response = highstring2;
                break;
            case 6:
                response = highstring3;
                break;

            default:
                //
        }
        return response;
    }
}
