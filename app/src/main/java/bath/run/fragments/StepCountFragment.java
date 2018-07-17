package bath.run.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import bath.run.database.User;
import bath.run.model.GoalCompletion;
import bath.run.MainActivity;
import bath.run.model.MotivationalMessages;
import bath.run.R;
import bath.run.model.StepsModel;

/**
 * Created by mradl on 18/06/2018.
 */

public class StepCountFragment extends Fragment {
    private static final String TAG = "StepCountFragment";
    final Handler handler = new Handler();
    private final int delayMillis = 5000;
    MotivationalMessages mm = new MotivationalMessages();
    StepsModel stepsModel = StepsModel.getInstance();
    User user = User.getInstance();
    double total = 0;
    int progress = 0;
    private TextView tvDailyStepsPercentage;
    private TextView tvDailySteps;
    private TextView tvMotivationalMessage;
    private TextView txtStreak;
    private TextView txtBestStreak;
    private Button btnStreak;
    private Button btnBestStreak;
    private ProgressBar progressBarDailySteps;
    public Runnable uiUpdater = new Runnable() {
        @Override
        public void run() {
            tvDailySteps.setText(String.valueOf(stepsModel.getDailysteps()));
            total = GoalCompletion.workOutRemainingPercentage(stepsModel.getDailysteps(), stepsModel.getDailyStepsGoal());
            progress = ((int) total);
            tvDailyStepsPercentage.setText(String.valueOf(progress) + "%");
            progressBarDailySteps.setProgress(progress);
            txtStreak.setText(String.valueOf(user.getStreak()));
            txtBestStreak.setText(String.valueOf(user.getBestStreak()));
            handler.postDelayed(uiUpdater, delayMillis);
            System.out.println("");
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //create view object
        View view = inflater.inflate(R.layout.overview, container, false);

        tvDailySteps = (TextView) view.findViewById(R.id.tvDailyCalories);
        tvDailyStepsPercentage = (TextView) view.findViewById(R.id.tvDailyCaloriesPercentage);
        progressBarDailySteps = (ProgressBar) view.findViewById(R.id.progressBarDailyCalories);
        tvMotivationalMessage = (TextView) view.findViewById(R.id.tvMotivationalMessage);
        btnStreak = (Button) view.findViewById(R.id.btnStreak);
        btnBestStreak = (Button) view.findViewById(R.id.btnBestStreak);
        txtStreak = (TextView) view.findViewById(R.id.txtStreak);
        txtBestStreak = (TextView) view.findViewById(R.id.txtBestStreak);
        tvMotivationalMessage.setText(mm.getMotivationalMessage());
        total = GoalCompletion.workOutRemainingPercentage(stepsModel.getDailysteps(), stepsModel.getDailyStepsGoal());
        txtStreak.setText(String.valueOf(user.getStreak()));
        Log.d(TAG, "onCreateView: started..");

        return view;
    }

    public void startUpdatingUi() {
        uiUpdater.run();
    }

    public void stopUpdatingUi() {
        handler.removeCallbacks(uiUpdater);
    }

    @Override
    public void onPause() {
        super.onPause();
        System.out.println("pause");
        stopUpdatingUi();
    }

    @Override
    public void onResume() {
        super.onResume();
        startUpdatingUi();
        setStreakIcon(user.getStreak(), btnStreak);
        setStreakIcon(user.getBestStreak(), btnBestStreak);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.i(TAG, "onDestroy: Step Fragment");
    }

    // This is reusable in a sense that it is called to assign the icon to the current streak
    // as well as the users best streak.
    public void setStreakIcon(int streak, Button streakBtn) {

        switch (streak) {
            case 0:
            case 1:
            case 2:
                streakBtn.setBackgroundResource(R.drawable.fire);
                break;
            case 3:
            case 4:
                streakBtn.setBackgroundResource(R.drawable.silvermedal);
                break;
            case 5:
            case 6:
                streakBtn.setBackgroundResource(R.drawable.medal);
                break;
            case 7:
            case 8:
                streakBtn.setBackgroundResource(R.drawable.crown);
                break;
            case 9:
            case 10:
                streakBtn.setBackgroundResource(R.drawable.winner);
                break;
            default:
                streakBtn.setBackgroundResource(R.drawable.run);
        }
    }
}

