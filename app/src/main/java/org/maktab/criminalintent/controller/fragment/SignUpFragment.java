package org.maktab.criminalintent.controller.fragment;

import android.content.Intent;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import org.maktab.criminalintent.R;
import org.maktab.criminalintent.model.User;
import org.maktab.criminalintent.repository.UserDBRepository;
import java.util.Objects;

public class SignUpFragment extends Fragment {

    public static final String EXTRA_USERNAME_SIGN_UP = "extraUsername";
    public static final String EXTRA_PASSWORD_SIGN_UP = "EXTRA_password";
    private Button mBtnSignUp;
    private TextInputLayout mUsernameForm;
    private TextInputLayout mPasswordForm;
    private TextInputEditText mUsername;
    private TextInputEditText mPassword;
    private UserDBRepository mUserRepository;

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
        mUserRepository = UserDBRepository.getInstance(getActivity());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_sign_up, container, false);

        findViews(view);
        mUsername.setText(mUser);
        mPassword.setText(mPass);
        listener();
        return view;
    }

    private void listener() {
        mBtnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUsernameForm.setErrorEnabled(false);
                mPasswordForm.setErrorEnabled(false);
                if (validateInput()) {
                    setUserPassResult();
                    getActivity().finish();
                }


            }
        });
    }

    private void setUserPassResult() {
        String username = Objects.requireNonNull(mUsername.getText()).toString();
        String password = Objects.requireNonNull(mPassword.getText()).toString();
        User user = new User(username,password);
        mUserRepository.insertUser(user);
        Intent intent = new Intent();
        intent.putExtra(EXTRA_USERNAME_SIGN_UP, mUsername.getText().toString());
        intent.putExtra(EXTRA_PASSWORD_SIGN_UP, mPassword.getText().toString());
        getActivity().setResult(getActivity().RESULT_OK, intent);
    }

    private void findViews(View view) {
        mBtnSignUp = view.findViewById(R.id.btnSignUp_SignUP);
        mUsernameForm = view.findViewById(R.id.username_form_signUp);
        mUsername = view.findViewById(R.id.username_signUp);
        mPasswordForm = view.findViewById(R.id.password_form_signUp);
        mPassword = view.findViewById(R.id.password_signUp);
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
        }
        mUsernameForm.setErrorEnabled(false);
        mPasswordForm.setErrorEnabled(false);
        return true;
    }
}