package bath.run.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import bath.run.R;

/**
 * Created by mradl on 18/06/2018.
 */

public class CalorieFragment extends Fragment {
    private static final String TAG = "CalorieFragment";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.calories_fragment, container, false);
        Log.d(TAG, "onCreateView: started..");

        return view;
    }
}
