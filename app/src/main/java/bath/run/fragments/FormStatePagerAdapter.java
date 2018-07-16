package bath.run.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mradl on 18/06/2018.
 */

public class FormStatePagerAdapter extends FragmentStatePagerAdapter {


    private final List<Fragment> mFragmentList = new ArrayList<>();

    //Not needed for my application, but handy for keeping track of fragments if in the future I were to add more forms
    private final List<String> mFragmentTitleList = new ArrayList<>();

    //Default constructor
    public FormStatePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    public void addFragment(Fragment fragment, String title) {
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }
}
