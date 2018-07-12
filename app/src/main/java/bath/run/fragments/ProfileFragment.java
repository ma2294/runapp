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
import android.widget.Spinner;

import bath.run.R;
import bath.run.model.UserProfileModel;

/**
 * Created by mradl on 09/07/2018.
 */

public class ProfileFragment extends Fragment {
    private static final String TAG = "ProfileFragment";
    UserProfileModel userProfileModel = UserProfileModel.getInstance();
    onProfileCompleteListener mCallback;
    private EditText name;
    private EditText weight;
    private EditText height;
    private Button btnUpdate;
    private Spinner spinnerWeight;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.profile_fragment, container, false);
        Log.i(TAG, "onCreateView: Profile fragment");

        name = (EditText) view.findViewById(R.id.editName);
        weight = (EditText) view.findViewById(R.id.editWeight);
        height = (EditText) view.findViewById(R.id.editHeight);
        btnUpdate = (Button) view.findViewById(R.id.btnUpdateProfile);
        spinnerWeight = (Spinner) view.findViewById(R.id.spinnerWeightArray);
        // Pull from database in MainActvity on start/ on stop?

        name.setText(userProfileModel.getName());
        weight.setText(String.valueOf(userProfileModel.getWeight()));
        height.setText(String.valueOf(userProfileModel.getHeight()));

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                String a = weight.getText().toString();
                int b = Integer.parseInt(a);
                String c = height.getText().toString();
                int d = Integer.parseInt(c);
                userProfileModel.setName(name.getText().toString());
                userProfileModel.setWeight(b);
                userProfileModel.setHeight(d);
                userProfileModel.setWeightPrompt((int) spinnerWeight
                        .getSelectedItemId());
                System.out.println(userProfileModel.toString());
                mCallback.onProfileUpdated();
            }
        });
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.i(TAG, "onDestroy: Profile Fragment");
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mCallback = (ProfileFragment.onProfileCompleteListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString() + " onProfileCompletion" +
                    "is not implemented");
        }
    }


    public interface onProfileCompleteListener {
        public void onProfileUpdated();
    }
}
