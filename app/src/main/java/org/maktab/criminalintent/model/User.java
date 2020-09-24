package org.maktab.criminalintent.model;

public class User {
    private String mUsername;
    private String mPassword;

    public String getUsername() {
        return mUsername;
    }

    public void setUsername(String username) {
        mUsername = username;
    }

    public String getPassword() {
        return mPassword;
    }

    public void setPassword(String password) {
        mPassword = password;
    }

    public User(String username, String password) {
        mUsername = username;
        mPassword = password;
    }
}
