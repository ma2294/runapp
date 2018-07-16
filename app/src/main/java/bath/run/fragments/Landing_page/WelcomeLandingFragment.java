package bath.run.fragments.Landing_page;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import bath.run.MainActivity;
import bath.run.R;

/**
 * Created by mradl on 09/07/2018.
 */


public class WelcomeLandingFragment extends Fragment {

    private static final String TAG = "WelcomeLandingPage";

    private Button btnLandingPage;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.landing_page_fragment, container, false);
        Log.i(TAG, "onCreateView: Dissonance fragment");

        btnLandingPage = (Button) view.findViewById(R.id.btnSubmitStepGoal);


        btnLandingPage.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
              //TODO go to next fragment (WelcomeDissonanceFragment)
                ((MainActivity)getActivity()).setViewPagerWelcome(1);
                Log.i(TAG, "onClick: ");
            }
        });
        return view;
    }
}
