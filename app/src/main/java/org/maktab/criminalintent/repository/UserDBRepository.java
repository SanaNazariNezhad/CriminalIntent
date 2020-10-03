package org.maktab.criminalintent.repository;

import android.content.Context;
import androidx.room.Room;
import org.maktab.criminalintent.database.CrimeDatabase;
import org.maktab.criminalintent.database.CrimeDatabaseDAO;
import org.maktab.criminalintent.model.User;
import java.util.List;

public class UserDBRepository implements IUserRepository {

    private static UserDBRepository sInstance;

    private CrimeDatabaseDAO mCrimeDAO;
    private Context mContext;

    public static UserDBRepository getInstance(Context context) {
        if (sInstance == null)
            sInstance = new UserDBRepository(context);

        return sInstance;
    }

    private UserDBRepository(Context context) {
        mContext = context.getApplicationContext();
        CrimeDatabase crimeDatabase = Room.databaseBuilder(mContext,
                CrimeDatabase.class,
                "crime.db")
                .allowMainThreadQueries()
                .build();

        mCrimeDAO = crimeDatabase.getCrimeDatabaseDAO();
    }
    @Override
    public List<User> getUsers() {
        return mCrimeDAO.getUsers();

    }

    @Override
    public User getUser(String username) {
       return mCrimeDAO.getUser(username);
    }

    @Override
    public void insertUser(User user) {
        mCrimeDAO.insertUser(user);
    }

}
