package bath.run.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import bath.run.model.GoalCompletion;
import bath.run.MainActivity;
import bath.run.model.MotivationalMessages;
import bath.run.R;
import bath.run.model.StepsModel;

/**
 * Created by mradl on 18/06/2018.
 */

public class CalorieFragment extends Fragment {
    private static final String TAG = "CalorieFragment";
    final Handler handler = new Handler();
    MotivationalMessages mm = new MotivationalMessages();
    StepsModel stepsModel = StepsModel.getInstance();
    double total = 0;
    int progress = 0;
    private final int delayMillis = 5000;
    private TextView tvDailyStepsPercentage;
    private TextView tvDailySteps;
    private TextView tvMotivationalMessage;
    private ProgressBar progressBarDailySteps;
    public Runnable uiUpdater = new Runnable() {
        @Override
        public void run() {
            //0.03 kcal per step
            if(stepsModel.getDailysteps() > 0) {
                int o = (int) (stepsModel.getDailysteps() * 0.03);
                String s = String.valueOf(o);

                tvDailySteps.setText(s+" kcal");
            }
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
        View view = inflater.inflate(R.layout.calories_fragment, container, false);

        tvDailySteps = (TextView) view.findViewById(R.id.tvDailyCalories);
        tvDailyStepsPercentage = (TextView) view.findViewById(R.id.tvDailyCaloriesPercentage);
        progressBarDailySteps = (ProgressBar) view.findViewById(R.id.progressBarDailyCalories);
        total = GoalCompletion.workOutRemainingPercentage(stepsModel.getDailysteps(), stepsModel.getDailyStepsGoal());
        Log.e(TAG, "onCreateView: started..");
        startUpdatingUi();

        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        System.out.println("started");
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
        stopUpdatingUi();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopUpdatingUi();
        Log.i(TAG, "onDestroy: Calories Fragment");
    }
}
