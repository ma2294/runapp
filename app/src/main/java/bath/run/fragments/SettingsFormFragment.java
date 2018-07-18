package bath.run.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import bath.run.R;
import bath.run.model.StepsModel;

/**
 * Created by mradl on 09/07/2018.
 */


public class SettingsFormFragment extends Fragment {

    private static final String TAG = "SettingsFragment";
    StepsModel stepsModel = StepsModel.getInstance();
    private Button btnSubmitStepGoal;
    private EditText editTextStepGoal;
    private RadioGroup radioGroupGoal;
    private RadioGroup radioGroupNotifications;
    private RadioButton radioStatic;
    private RadioButton radioTailored;
    private RadioButton radioNotifyDefault;
    private RadioButton radioNotifyScheduled;
    onSettingsCompletion mCallback;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_fragment, container, false);

        btnSubmitStepGoal = (Button) view.findViewById(R.id.btnSubmitStepGoal);
        editTextStepGoal = (EditText) view.findViewById(R.id.editStepGoal);
        radioGroupGoal = (RadioGroup) view.findViewById(R.id.radioGroupGoal);
        radioGroupNotifications = (RadioGroup) view.findViewById(R.id.radioGroupTimeRange);
        radioStatic = (RadioButton) view.findViewById(R.id.radio_static_goal);
        radioTailored = (RadioButton) view.findViewById(R.id.radio_tailored_goal);
        radioNotifyDefault = (RadioButton) view.findViewById(R.id.radio_static_range);
        radioNotifyScheduled = (RadioButton) view.findViewById(R.id.radio_tailored_range);


        btnSubmitStepGoal.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if (!radioStatic.isChecked() && !radioTailored.isChecked() ||
                        !radioNotifyDefault.isChecked() && !radioNotifyScheduled.isChecked()) {
                    Toast.makeText(getContext(), "Please complete the form.", Toast.LENGTH_SHORT).show();
                    return;
                }
                String value = editTextStepGoal.getText().toString();
                if (value.length() < 1) {
                    Toast.makeText(getContext(), "Please complete the form", Toast.LENGTH_SHORT).show();
                    return;
                }
                int finalValue = Integer.parseInt(value);
                stepsModel.setDailyStepsGoal(finalValue);

                switch (radioGroupGoal.getCheckedRadioButtonId()) {
                    case R.id.radio_static_goal:
                        System.out.println("Radio static " + radioStatic.getId());
                        break;
                    case R.id.radio_tailored_goal:
                        System.out.println("Radio tailored " + radioTailored.getId());
                        break;

                    default:
                        //
                }

                switch (radioGroupNotifications.getCheckedRadioButtonId()) {
                    case R.id.radio_static_range:
                        System.out.println("Radio Static range");
                        break;
                    case R.id.radio_tailored_range:
                        System.out.println("Radio tailored range");
                        break;

                    default:
                        //
                }
           mCallback.onSettingsComplete();
            }
        });
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (SettingsFormFragment.onSettingsCompletion) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " onSettingsCompletion" +
                    "is not implemented");
        }
    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.i(TAG, "onDestroy: Dissonance form Fragment");
    }
    public interface onSettingsCompletion {
        public void onSettingsComplete();
    }
}


