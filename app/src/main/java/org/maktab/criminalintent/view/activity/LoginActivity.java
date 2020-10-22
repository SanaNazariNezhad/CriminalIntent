package org.maktab.criminalintent.view.activity;

import androidx.fragment.app.Fragment;
import android.content.Context;
import android.content.Intent;
import org.maktab.criminalintent.view.fragment.LoginFragment;

public class LoginActivity extends SingleFragmentActivity {

    public static Intent newIntent(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        return intent;
    }


    @Override
    public Fragment createFragment() {
        return LoginFragment.newInstance();
    }
}