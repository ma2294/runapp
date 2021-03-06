package bath.run.fragments.Landing_page;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;

import bath.run.model.DissonanceFormModel;
import bath.run.R;

/**
 * Created by mradl on 09/07/2018.
 */


public class WelcomeDissonanceFragment extends Fragment {

    private static final String TAG = "WelcomeDissonance";
    onFormCompletionListener mCallback;
    DissonanceFormModel dissonanceFormModel = DissonanceFormModel.getInstance();
    private Button btnUpdate;
    private Spinner spinnerQ1;
    private Spinner spinnerQ2;
    private Spinner spinnerQ3;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.welcome_dissonance_form, container, false);
        Log.i(TAG, "onCreateView: Dissonance fragment");

        btnUpdate = (Button) view.findViewById(R.id.btnUpdateDissonanceForm);
        spinnerQ1 = (Spinner) view.findViewById(R.id.spinnerQ1);
        spinnerQ2 = (Spinner) view.findViewById(R.id.spinnerQ2);
        spinnerQ3 = (Spinner) view.findViewById(R.id.spinnerQ3);

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println(spinnerQ1.getSelectedItemId());
                dissonanceFormModel.setCare((int) spinnerQ1.getSelectedItemId());
                dissonanceFormModel.setFrequency((int) spinnerQ2.getSelectedItemId());
                dissonanceFormModel.setCompetitiveness((int) spinnerQ3.getSelectedItemId());
                dissonanceFormModel.setAnswered(true);
                mCallback.onFormCompletion();
            }
        });
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (onFormCompletionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " onFormCompletion" +
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


    public interface onFormCompletionListener {
        public void onFormCompletion();
    }
}


