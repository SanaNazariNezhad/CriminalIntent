package org.maktab.criminalintent.controller.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.maktab.criminalintent.R;
import org.maktab.criminalintent.controller.activity.CrimeListActivity;
import org.maktab.criminalintent.controller.activity.SignUpActivity;

public class LoginFragment extends Fragment {
    public static final String BUNDLE_KEY_USERNAME = "UserBundle";
    public static final String BUNDLE_KEY_PASSWORD = "passBundle";
    private Button mButtonLogin, mButtonSignUp;
    public static final int REQUEST_CODE_SIGN_UP = 0;
    private String user, pass;
    private ViewGroup mViewGroupRootLayout;

    private TextInputLayout mUsernameForm;
    private TextInputLayout mPasswordForm;
    private TextInputEditText mUsername;
    private TextInputEditText mPassword;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


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

        if (savedInstanceState != null) {
            user = savedInstanceState.getString(BUNDLE_KEY_USERNAME);
            pass = savedInstanceState.getString(BUNDLE_KEY_PASSWORD);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        findViews(view);
        listeners();
        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(BUNDLE_KEY_USERNAME, user);
        outState.putString(BUNDLE_KEY_PASSWORD, pass);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != Activity.RESULT_OK || data == null)
            return;
        if (requestCode == REQUEST_CODE_SIGN_UP) {
            user = data.getStringExtra(SignUpFragment.EXTRA_USERNAME_SIGN_UP);
            pass = data.getStringExtra(SignUpFragment.EXTRA_PASSWORD_SIGN_UP);
            mUsername.setText(user);
            mPassword.setText(pass);
        }
    }

    private void listeners() {
        mButtonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUsernameForm.setErrorEnabled(false);
                mPasswordForm.setErrorEnabled(false);
                if (validateInput()) {
                    Intent intent = CrimeListActivity.newIntent(getActivity());
                    startActivity(intent);
                }


            }
        });
        mButtonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = SignUpActivity.newIntent(getActivity(), mUsername.getText().toString(), mPassword.getText().toString());
                startActivityForResult(intent,REQUEST_CODE_SIGN_UP);

            }
        });

    }

    private boolean validateInput() {
        if (mUsername.getText().toString().trim().isEmpty() && mPassword.getText().toString().trim().isEmpty()) {
            mUsernameForm.setErrorEnabled(true);
            mUsernameForm.setError("Field cannot be empty!");
            mPasswordForm.setErrorEnabled(true);
            mPasswordForm.setError("Field cannot be empty!");
            return false;
        } else if (mUsername.getText().toString().trim().isEmpty()) {
            mUsernameForm.setErrorEnabled(true);
            mUsernameForm.setError("Field cannot be empty!");
            return false;
        } else if (mPassword.getText().toString().trim().isEmpty()) {
            mPasswordForm.setErrorEnabled(true);
            mPasswordForm.setError("Field cannot be empty!");
            return false;
        } else if (!mUsername.getText().toString().equals(user) ||
                !mPassword.getText().toString().equals(pass)) {
            callToast(R.string.toast_login);
            return false;
        }
        mUsernameForm.setErrorEnabled(false);
        mPasswordForm.setErrorEnabled(false);
        return true;
    }

    private void findViews(View view) {
        mButtonLogin = view.findViewById(R.id.btnLogin_Login);
        mButtonSignUp = view.findViewById(R.id.btnSignUp_Login);
        mUsernameForm = view.findViewById(R.id.username_form_login);
        mPasswordForm = view.findViewById(R.id.password_form_login);
        mUsername = view.findViewById(R.id.username_login);
        mPassword = view.findViewById(R.id.password_login);
        mViewGroupRootLayout = view.findViewById(R.id.rootLayout);

    }

    private void callToast(int stringId) {
        Toast toast = Toast.makeText(getActivity(), stringId, Toast.LENGTH_SHORT);
        toast.show();
    }
}