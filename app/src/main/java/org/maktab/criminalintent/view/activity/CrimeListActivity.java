package org.maktab.criminalintent.view.activity;

import android.content.Context;
import android.content.Intent;
import androidx.fragment.app.Fragment;

import org.maktab.criminalintent.R;
import org.maktab.criminalintent.adapter.CrimeAdapter;
import org.maktab.criminalintent.view.fragment.CrimeDetailFragment;
import org.maktab.criminalintent.view.fragment.CrimeListFragment;
import org.maktab.criminalintent.model.Crime;
import org.maktab.criminalintent.viewmodel.CrimeViewModel;

public class CrimeListActivity extends SingleFragmentActivity implements
        CrimeListFragment.Callbacks, CrimeDetailFragment.Callbacks, CrimeViewModel.Callbacks {

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