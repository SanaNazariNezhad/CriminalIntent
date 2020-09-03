package org.maktab.criminalintent.controller.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import org.maktab.criminalintent.controller.fragment.CrimeDetailFragment;

import java.util.UUID;

public class CrimeDetailActivity extends SingleFragmentActivity {
    public static final String EXTRA_CRIME_ID = "org.maktab.criminalintent.controller.activity.crimeID";

    public static Intent newIntent(Context context, UUID crimeId) {
        Intent intent = new Intent(context, CrimeDetailActivity.class);
        intent.putExtra(EXTRA_CRIME_ID,crimeId);
        return intent;
    }

    @Override
    public Fragment createFragment() {
        UUID crimeId = (UUID) getIntent().getSerializableExtra(EXTRA_CRIME_ID);
        /*CrimeDetailFragment crimeDetailFragment = new CrimeDetailFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARGUMENT_CRIME_ID,crimeId);
        crimeDetailFragment.setArguments(args);*/

        CrimeDetailFragment crimeDetailFragment = CrimeDetailFragment.newInstance(crimeId);

        return new CrimeDetailFragment();
    }
}