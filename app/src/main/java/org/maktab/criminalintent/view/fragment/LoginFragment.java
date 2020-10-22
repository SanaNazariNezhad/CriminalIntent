package org.maktab.criminalintent.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.maktab.criminalintent.R;
import org.maktab.criminalintent.view.activity.CrimeListActivity;
import org.maktab.criminalintent.view.activity.SignUpActivity;
import org.maktab.criminalintent.databinding.FragmentLoginBinding;
import org.maktab.criminalintent.model.User;
import org.maktab.criminalintent.repository.UserDBRepository;
import org.maktab.criminalintent.viewmodel.CrimeListViewModel;
import org.maktab.criminalintent.viewmodel.LoginViewModel;

import java.util.Objects;

public class LoginFragment extends Fragment {
    public static final String BUNDLE_KEY_USERNAME = "UserBundle";
    public static final String BUNDLE_KEY_PASSWORD = "passBundle";
    public static final int REQUEST_CODE_SIGN_UP = 0;
    private String username, password;

    private FragmentLoginBinding mLoginBinding;
    private LoginViewModel mLoginViewModel;
    /*private UserDBRepository mUserRepository;*/

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        LoginFragment fragment = new LoginFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mLoginViewModel = new LoginViewModel(getContext());
        if (savedInstanceState != null) {
            username = savedInstanceState.getString(BUNDLE_KEY_USERNAME);
            password = savedInstanceState.getString(BUNDLE_KEY_PASSWORD);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mLoginBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_login,
                container,
                false );

        listeners();
        return mLoginBinding.getRoot();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(BUNDLE_KEY_USERNAME, username);
        outState.putString(BUNDLE_KEY_PASSWORD, password);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK || data == null)
            return;
        if (requestCode == REQUEST_CODE_SIGN_UP) {
            username = data.getStringExtra(SignUpFragment.EXTRA_USERNAME_SIGN_UP);
            password = data.getStringExtra(SignUpFragment.EXTRA_PASSWORD_SIGN_UP);
            mLoginBinding.usernameLogin.setText(username);
            mLoginBinding.passwordLogin.setText(password);
        }
    }

    private void listeners() {
        mLoginBinding.btnLoginLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mLoginBinding.usernameFormLogin.setErrorEnabled(false);
                mLoginBinding.passwordFormLogin.setErrorEnabled(false);
                if (validateInput()) {
                    Intent intent = CrimeListActivity.newIntent(getActivity(),
                            mLoginBinding.usernameLogin.getText().toString());
                    startActivity(intent);
                }
            }
        });
        mLoginBinding.btnSignUpLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = SignUpActivity.newIntent(getActivity(),
                        mLoginBinding.usernameLogin.getText().toString(),
                        mLoginBinding.passwordLogin.getText().toString());
                startActivityForResult(intent, REQUEST_CODE_SIGN_UP);

            }
        });

    }

    private boolean validateInput() {
        User user = mLoginViewModel.getUser(Objects.requireNonNull(
                mLoginBinding.usernameLogin.getText()).toString());
        if ( mLoginBinding.usernameLogin.getText().toString().trim().isEmpty()
                && mLoginBinding.passwordLogin.getText().toString().trim().isEmpty()) {
            mLoginBinding.usernameFormLogin.setErrorEnabled(true);
            mLoginBinding.usernameFormLogin.setError("Field cannot be empty!");
            mLoginBinding.passwordFormLogin.setErrorEnabled(true);
            mLoginBinding.passwordFormLogin.setError("Field cannot be empty!");
            return false;
        } else if ( mLoginBinding.usernameLogin.getText().toString().trim().isEmpty()) {
            mLoginBinding.usernameFormLogin.setErrorEnabled(true);
            mLoginBinding.usernameFormLogin.setError("Field cannot be empty!");
            return false;
        } else if (mLoginBinding.passwordLogin.getText().toString().trim().isEmpty()) {
            mLoginBinding.passwordFormLogin.setErrorEnabled(true);
            mLoginBinding.passwordFormLogin.setError("Field cannot be empty!");
            return false;
        }
        if (user == null){
            callToast(R.string.toast_login);
            return false;
        }else {
            String inputUsername = user.getUsername();
            String inputPassword = user.getPassword();
            if (! mLoginBinding.usernameLogin.getText().toString().equals(inputUsername) ||
                    !mLoginBinding.passwordLogin.getText().toString().equals(inputPassword)) {
                callToast(R.string.toast_login);
                return false;
            }
        }
        mLoginBinding.usernameFormLogin.setErrorEnabled(false);
        mLoginBinding.passwordFormLogin.setErrorEnabled(false);
        return true;
    }

    private void callToast(int stringId) {
        Toast toast = Toast.makeText(getActivity(), stringId, Toast.LENGTH_SHORT);
        toast.show();
    }
}