package bath.run;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by mradl on 09/07/2018.
 */

public class ProfileFragment extends Fragment {
        private static final String TAG = "ProfileFragment";

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.profile_fragment, container, false);
            Log.i(TAG, "onCreateView: Profile fragment");
            return view;
        }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.i(TAG, "onDestroy: Profile Fragment");
    }
}
