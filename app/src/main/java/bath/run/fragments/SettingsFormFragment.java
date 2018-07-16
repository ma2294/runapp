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
import android.widget.Spinner;

import bath.run.model.DissonanceFormModel;
import bath.run.R;

/**
 * Created by mradl on 09/07/2018.
 */


public class SettingsFormFragment extends Fragment {

    private static final String TAG = "SettingsFormFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_fragment, container, false);
        Log.i(TAG, "onCreateView: Dissonance fragment");

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
          //  mCallback = (onFormCompletionListener) activity;
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


