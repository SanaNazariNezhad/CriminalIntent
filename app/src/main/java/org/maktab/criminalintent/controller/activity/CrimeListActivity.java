package org.maktab.criminalintent.controller.activity;

import android.content.Context;
import android.content.Intent;

import androidx.fragment.app.Fragment;

import org.maktab.criminalintent.controller.fragment.CrimeListFragment;

public class CrimeListActivity extends SingleFragmentActivity {

    public static final String EXTRA_USERNAME = "org.maktab.criminalintent.controller.activity.extra_username";
    private static String mUsername;

    public static Intent newIntent(Context context,String username) {
        mUsername = username;
        Intent intent = new Intent(context, CrimeListActivity.class);
        intent.putExtra(EXTRA_USERNAME,mUsername);
        return intent;
    }
    @Override
    public Fragment createFragment() {
        CrimeListFragment crimeListFragment = CrimeListFragment.newInstance(mUsername);
        return crimeListFragment;
    }

}