package org.maktab.criminalintent.repository;

import org.maktab.criminalintent.model.User;
import java.util.List;

public interface IUserRepository {
    List<User> getUsers();
    User getUser(String username);
    void insertUser(User user);
}
