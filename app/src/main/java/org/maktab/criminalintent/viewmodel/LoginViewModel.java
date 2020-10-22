package org.maktab.criminalintent.viewmodel;

import android.content.Context;

import org.maktab.criminalintent.model.User;
import org.maktab.criminalintent.repository.IUserRepository;
import org.maktab.criminalintent.repository.UserDBRepository;

import java.util.List;

public class LoginViewModel {

    private IUserRepository mUserRepository;

    public LoginViewModel(Context context) {
        mUserRepository = UserDBRepository.getInstance(context);
    }

    public List<User> getUsers(){
        return mUserRepository.getUsers();
    }
    public User getUser(String username){
        return mUserRepository.getUser(username);
    }
    public void insertUser(User user){
        mUserRepository.insertUser(user);
    }
}
