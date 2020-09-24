package org.maktab.criminalintent.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.maktab.criminalintent.database.CrimeDBHelper;
import org.maktab.criminalintent.database.CrimeDBSchema;
import org.maktab.criminalintent.model.Crime;
import static org.maktab.criminalintent.database.CrimeDBSchema.CrimeTable.Cols;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class CrimeDBRepository implements IRepository {

    private static CrimeDBRepository sInstance;

    private SQLiteDatabase mDatabase;
    private Context mContext;
    private List<Crime> mCrimes;

    public static CrimeDBRepository getInstance(Context context) {
        if (sInstance == null)
            sInstance = new CrimeDBRepository(context);

        return sInstance;
    }

    private CrimeDBRepository(Context context) {
        mContext = context.getApplicationContext();
        CrimeDBHelper crimeDBHelper = new CrimeDBHelper(mContext);

        //all 4 checks happens on getDataBase
        mDatabase = crimeDBHelper.getWritableDatabase();
    }

    @Override
    public List<Crime> getCrimes() {
        mCrimes = new ArrayList<>();

        Cursor cursor = mDatabase.query(
                CrimeDBSchema.CrimeTable.NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        if (cursor == null || cursor.getCount() == 0)
            return mCrimes;

        try {
            cursor.moveToFirst();

            while (!cursor.isAfterLast()) {
                Crime crime = extractCrimeFromCursor(cursor);
                mCrimes.add(crime);

                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return mCrimes;
    }

    @Override
    public Crime getCrime(UUID crimeId) {
        String where = Cols.UUID + " = ?";
        String[] whereArgs = new String[]{crimeId.toString()};

        Cursor cursor = mDatabase.query(
                CrimeDBSchema.CrimeTable.NAME,
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
            Crime crime = extractCrimeFromCursor(cursor);

            return crime;
        } finally {
            cursor.close();
        }
    }

    @Override
    public void insertCrime(Crime crime) {
        ContentValues values = getContentValues(crime);
        mDatabase.insert(CrimeDBSchema.CrimeTable.NAME, null, values);
    }

    @Override
    public void updateCrime(Crime crime) {
        ContentValues values = getContentValues(crime);
        String whereClause = Cols.UUID + " = ?";
        String[] whereArgs = new String[]{crime.getId().toString()};
        mDatabase.update(CrimeDBSchema.CrimeTable.NAME, values, whereClause, whereArgs);
    }

    @Override
    public void deleteCrime(Crime crime) {
        String whereClause = Cols.UUID + " = ?";
        String[] whereArgs = new String[]{crime.getId().toString()};
        mDatabase.delete(CrimeDBSchema.CrimeTable.NAME, whereClause, whereArgs);
    }

    @Override
    public int getPosition(UUID crimeId) {
        for (int i = 0; i < mCrimes.size(); i++) {
            if (mCrimes.get(i).getId().equals(crimeId))
                return i;
        }
        return -1;
    }

    @Override
    public UUID nextPosition(UUID crimeId) {
        for (int i = 0; i < mCrimes.size(); i++) {
            if (mCrimes.get(i).getId().equals(crimeId))
                return mCrimes.get(i + 1).getId();
        }

        return null;
    }

    @Override
    public UUID pervPosition(UUID crimeId) {
        for (int i = 0; i < mCrimes.size(); i++) {
            if (mCrimes.get(i).getId().equals(crimeId))
                return mCrimes.get(i - 1).getId();
        }

        return null;
    }

    @Override
    public void setCrimesSelected(){
        for (Crime crime : mCrimes) {
            crime.setCheck_Select(true);
        }
    }

    @Override
    public void setCrimesUnSelected(){
        for (Crime crime : mCrimes) {
            crime.setCheck_Select(false);
        }
    }

    @Override
    public int repositorySize() {
        return mCrimes.size();
    }

    @Override
    public int getIndexOfCrime(Crime crime) {
        for (int i = 0; i < mCrimes.size(); i++) {
            if (mCrimes.get(i).equals(crime))
                return i;
        }
        return -1;
    }

    @Override
    public Crime getCrimeWithIndex(int index) {
        return mCrimes.get(index);
    }


    private ContentValues getContentValues(Crime crime) {
        ContentValues values = new ContentValues();
        values.put(Cols.UUID, crime.getId().toString());
        values.put(Cols.TITLE, crime.getTitle());
        values.put(Cols.DATE, crime.getDate().getTime());
        values.put(Cols.SOLVED, crime.isSolved() ? 1 : 0);
        return values;
    }

    private Crime extractCrimeFromCursor(Cursor cursor) {
        UUID uuid = UUID.fromString(cursor.getString(cursor.getColumnIndex(Cols.UUID)));
        String title = cursor.getString(cursor.getColumnIndex(Cols.TITLE));
        Date date = new Date(cursor.getLong(cursor.getColumnIndex(Cols.DATE)));
        boolean solved = cursor.getInt(cursor.getColumnIndex(Cols.SOLVED)) == 0 ? false : true;

        return new Crime(uuid, title, date, solved);
    }
}
