package org.maktab.criminalintent.repository;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import org.maktab.criminalintent.CrimeCursorWrapper;
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
        List<Crime> crimes = new ArrayList<>();

        CrimeCursorWrapper crimeCursorWrapper = queryCrimeCursor(null, null);

        if (crimeCursorWrapper == null || crimeCursorWrapper.getCount() == 0)
            return crimes;

        try {
            crimeCursorWrapper.moveToFirst();

            while (!crimeCursorWrapper.isAfterLast()) {
                crimes.add(crimeCursorWrapper.getCrime());
                crimeCursorWrapper.moveToNext();
            }
        } finally {
            crimeCursorWrapper.close();
        }

        return crimes;
    }

    @Override
    public Crime getCrime(UUID crimeId) {
        String where = Cols.UUID + " = ?";
        String[] whereArgs = new String[]{crimeId.toString()};

        CrimeCursorWrapper crimeCursorWrapper = queryCrimeCursor(where, whereArgs);

        if (crimeCursorWrapper == null || crimeCursorWrapper.getCount() == 0)
            return null;

        try {
            crimeCursorWrapper.moveToFirst();
            return crimeCursorWrapper.getCrime();
        } finally {
            crimeCursorWrapper.close();
        }
    }

    private CrimeCursorWrapper queryCrimeCursor(String where, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                CrimeDBSchema.CrimeTable.NAME,
                null,
                where,
                whereArgs,
                null,
                null,
                null);

        CrimeCursorWrapper crimeCursorWrapper = new CrimeCursorWrapper(cursor);
        return crimeCursorWrapper;
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
        List<Crime> crimes = getCrimes();
        for (int i = 0; i < crimes.size(); i++) {
            if (crimes.get(i).getId().equals(crimeId))
                return i;
        }
        return -1;
    }

    @Override
    public UUID nextPosition(UUID crimeId) {
        List<Crime> crimes = getCrimes();
        for (int i = 0; i < crimes.size(); i++) {
            if (crimes.get(i).getId().equals(crimeId))
                return crimes.get(i + 1).getId();
        }

        return null;
    }

    @Override
    public UUID pervPosition(UUID crimeId) {
        List<Crime> crimes = getCrimes();
        for (int i = 0; i < crimes.size(); i++) {
            if (crimes.get(i).getId().equals(crimeId))
                return crimes.get(i - 1).getId();
        }

        return null;
    }

    @Override
    public void setCrimesSelected(){
        List<Crime> crimes = getCrimes();
        for (Crime crime : crimes) {
            crime.setCheck_Select(true);
        }
    }

    @Override
    public void setCrimesUnSelected(){
        List<Crime> crimes = getCrimes();
        for (Crime crime : crimes) {
            crime.setCheck_Select(false);
        }
    }

    @Override
    public int repositorySize() {
        List<Crime> crimes = getCrimes();
        return crimes.size();
    }

    @Override
    public int getIndexOfCrime(Crime crime) {
        List<Crime> crimes = getCrimes();
        for (int i = 0; i < crimes.size(); i++) {
            if (crimes.get(i).equals(crime))
                return i;
        }
        return -1;
    }

    @Override
    public Crime getCrimeWithIndex(int index) {
        List<Crime> crimes = getCrimes();
        return crimes.get(index);
    }


    private ContentValues getContentValues(Crime crime) {
        ContentValues values = new ContentValues();
        values.put(Cols.UUID, crime.getId().toString());
        values.put(Cols.TITLE, crime.getTitle());
        values.put(Cols.DATE, crime.getDate().getTime());
        values.put(Cols.SUSPECT, crime.getSuspect());
        values.put(Cols.SOLVED, crime.isSolved() ? 1 : 0);
        return values;
    }
}
