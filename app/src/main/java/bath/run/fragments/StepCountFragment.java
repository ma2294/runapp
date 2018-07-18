package bath.run.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

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
    private ProgressBar progressBarDailySteps;


    public Runnable uiUpdater = new Runnable() {
        @Override
        public void run() {
            tvDailySteps.setText(String.valueOf(stepsModel.getDailysteps()));
            total = GoalCompletion.workOutRemainingPercentage(stepsModel.getDailysteps(), stepsModel.getDailyStepsGoal());
            progress = ((int) total);
            tvDailyStepsPercentage.setText(String.valueOf(progress) + "%");
            progressBarDailySteps.setProgress(progress);
            handler.postDelayed(uiUpdater, delayMillis);
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
        tvMotivationalMessage.setText(mm.getMotivationalMessage());
        total = GoalCompletion.workOutRemainingPercentage(stepsModel.getDailysteps(), stepsModel.getDailyStepsGoal());
        Log.d(TAG, "onCreateView: started..");


        // fade out view nicely after 5 seconds
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
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.i(TAG, "onDestroy: Step Fragment");
    }
}

