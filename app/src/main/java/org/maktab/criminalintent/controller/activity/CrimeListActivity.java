package org.maktab.criminalintent.controller.activity;

import android.content.Context;
import android.content.Intent;
import androidx.fragment.app.Fragment;

import org.maktab.criminalintent.R;
import org.maktab.criminalintent.controller.fragment.CrimeDetailFragment;
import org.maktab.criminalintent.controller.fragment.CrimeListFragment;
import org.maktab.criminalintent.model.Crime;

public class CrimeListActivity extends SingleFragmentActivity implements
        CrimeListFragment.Callbacks, CrimeDetailFragment.Callbacks{

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

    @Override
    public void onCrimeSelected(Crime crime) {
        if (findViewById(R.id.detail_fragment_container) == null) {
            Intent intent = CrimePagerActivity.newIntent(this, crime.getId(),mUsername);
            startActivity(intent);
        } else {
            //add fragment to container
            CrimeDetailFragment crimeDetailFragment =
                    CrimeDetailFragment.newInstance(crime.getId(),mUsername);

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.detail_fragment_container, crimeDetailFragment)
                    .commit();
        }
    }

    @Override
    public void onCrimeUpdated(Crime crime) {
        CrimeListFragment crimeListFragment = (CrimeListFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_container);

        crimeListFragment.updateUI();
    }

}