package org.maktab.criminalintent.view.fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.maktab.criminalintent.R;
import org.maktab.criminalintent.databinding.FragmentSignUpBinding;
import org.maktab.criminalintent.model.User;
import org.maktab.criminalintent.repository.UserDBRepository;
import org.maktab.criminalintent.viewmodel.LoginViewModel;

import java.util.Objects;

public class SignUpFragment extends Fragment {

    public static final String EXTRA_USERNAME_SIGN_UP = "extraUsername";
    public static final String EXTRA_PASSWORD_SIGN_UP = "EXTRA_password";
   private FragmentSignUpBinding mSignUpBinding;
   private LoginViewModel mLoginViewModel;
    /*private UserDBRepository mUserRepository;*/

    private static final String ARG_USERNAME = "username";
    private static final String ARG_PASSWORD = "password";

    private String mUser;
    private String mPass;

    public SignUpFragment() {
        // Required empty public constructor
    }

    public static SignUpFragment newInstance(String username, String password) {
        SignUpFragment fragment = new SignUpFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USERNAME, username);
        args.putString(ARG_PASSWORD, password);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mUser = getArguments().getString(ARG_USERNAME);
        mPass = getArguments().getString(ARG_PASSWORD);
        mLoginViewModel = new LoginViewModel(getContext());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mSignUpBinding = DataBindingUtil.inflate(inflater,
                R.layout.fragment_sign_up,
                container,
                false  );

        mSignUpBinding.usernameSignUp.setText(mUser);
        mSignUpBinding.passwordSignUp.setText(mPass);
        listener();
        return mSignUpBinding.getRoot();
    }

    private void listener() {
        mSignUpBinding.btnSignUpSignUP.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSignUpBinding.usernameFormSignUp.setErrorEnabled(false);
                mSignUpBinding.passwordFormSignUp.setErrorEnabled(false);
                if (validateInput()) {
                    setUserPassResult();
                    getActivity().finish();
                }


            }
        });
    }

    private void setUserPassResult() {
        String username = Objects.requireNonNull(mSignUpBinding.usernameSignUp.getText()).toString();
        String password = Objects.requireNonNull(mSignUpBinding.passwordSignUp.getText()).toString();
        User user = new User(username,password);
        mLoginViewModel.insertUser(user);
        Intent intent = new Intent();
        intent.putExtra(EXTRA_USERNAME_SIGN_UP, mSignUpBinding.usernameSignUp.getText().toString());
        intent.putExtra(EXTRA_PASSWORD_SIGN_UP, mSignUpBinding.passwordSignUp.getText().toString());
        getActivity().setResult(getActivity().RESULT_OK, intent);
    }

    private boolean validateInput() {
        if (mSignUpBinding.usernameSignUp.getText().toString().trim().isEmpty()
                && mSignUpBinding.passwordSignUp.getText().toString().trim().isEmpty()) {
            mSignUpBinding.usernameFormSignUp.setErrorEnabled(true);
            mSignUpBinding.usernameFormSignUp.setError("Field cannot be empty!");
            mSignUpBinding.passwordFormSignUp.setErrorEnabled(true);
            mSignUpBinding.passwordFormSignUp.setError("Field cannot be empty!");
            return false;
        } else if (mSignUpBinding.usernameSignUp.getText().toString().trim().isEmpty()) {
            mSignUpBinding.usernameFormSignUp.setErrorEnabled(true);
            mSignUpBinding.usernameFormSignUp.setError("Field cannot be empty!");
            return false;
        } else if (mSignUpBinding.passwordSignUp.getText().toString().trim().isEmpty()) {
            mSignUpBinding.passwordFormSignUp.setErrorEnabled(true);
            mSignUpBinding.passwordFormSignUp.setError("Field cannot be empty!");
            return false;
        }
        mSignUpBinding.usernameFormSignUp.setErrorEnabled(false);
        mSignUpBinding.passwordFormSignUp.setErrorEnabled(false);
        return true;
    }
}