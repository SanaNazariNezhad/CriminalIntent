package org.maktab.criminalintent.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.maktab.criminalintent.database.CrimeDBHelper;
import org.maktab.criminalintent.database.CrimeDBSchema;
import org.maktab.criminalintent.model.User;
import static org.maktab.criminalintent.database.CrimeDBSchema.UserTable.Cols;

import java.util.ArrayList;
import java.util.List;

public class UserDBRepository implements IUserRepository {

    private static UserDBRepository sInstance;

    private SQLiteDatabase mDatabase;
    private Context mContext;

    public static UserDBRepository getInstance(Context context) {
        if (sInstance == null)
            sInstance = new UserDBRepository(context);

        return sInstance;
    }

    private UserDBRepository(Context context) {
        mContext = context.getApplicationContext();
        CrimeDBHelper userDBHelper = new CrimeDBHelper(mContext);

        //all 4 checks happens on getDataBase
        mDatabase = userDBHelper.getWritableDatabase();
    }
    @Override
    public List<User> getUsers() {
        List<User> mUsers = new ArrayList<>();

        Cursor cursor = mDatabase.query(
                CrimeDBSchema.UserTable.NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        if (cursor == null || cursor.getCount() == 0)
            return mUsers;

        try {
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                User user = extractUserFromCursor(cursor);
                mUsers.add(user);

                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return mUsers;
    }

    private User extractUserFromCursor(Cursor cursor) {
        String username = cursor.getString(cursor.getColumnIndex(Cols.USERNAME));
        String password = cursor.getString(cursor.getColumnIndex(Cols.PASSWORD));

        return new User(username, password);
    }

    @Override
    public User getUser(String username) {
        String where = Cols.USERNAME + " = ?";
        String[] whereArgs = new String[]{username};

        Cursor cursor = mDatabase.query(
                CrimeDBSchema.UserTable.NAME,
                null,
                where,
                whereArgs,
                null,
                null,
                null);

        if (cursor == null || cursor.getCount() == 0)
            return null;

        try {
            cursor.moveToFirst();
            User user = extractUserFromCursor(cursor);

            return user;
        } finally {
            cursor.close();
        }
    }

    @Override
    public void insertUser(User user) {
        ContentValues values = getContentValues(user);
        mDatabase.insert(CrimeDBSchema.UserTable.NAME, null, values);
    }

    private ContentValues getContentValues(User user) {
        ContentValues values = new ContentValues();
        values.put(Cols.USERNAME, user.getUsername());
        values.put(Cols.PASSWORD, user.getPassword());

        return values;
    }

}
