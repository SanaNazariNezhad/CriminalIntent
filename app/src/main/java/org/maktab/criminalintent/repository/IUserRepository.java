package org.maktab.criminalintent.repository;

import org.maktab.criminalintent.model.Crime;
import org.maktab.criminalintent.model.User;

import java.util.List;
import java.util.UUID;

public interface IUserRepository {
    List<User> getUsers();
    User getUser(String username);
    void insertUser(User user);
}
