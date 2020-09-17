package org.maktab.criminalintent.repository;

import org.maktab.criminalintent.model.Crime;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class CrimeRepository implements IRepository {

    private static final int CRIME_SIZE = 5;
    private static CrimeRepository sInstance;

    private List<Crime> mCrimes;

    public static CrimeRepository getInstance() {
        if (sInstance == null)
            sInstance = new CrimeRepository();

        return sInstance;
    }


    private CrimeRepository() {
        mCrimes = new ArrayList<>();

        for (int i = 0; i < CRIME_SIZE; i++) {
            Crime crime = new Crime();
            crime.setTitle("Crime#" + (i + 1));
            crime.setSolved((i % 2 == 0));

            mCrimes.add(crime);
        }
       /* mCrimes.add(0,getCrimeWithIndex(mCrimes.size()));
        mCrimes.add(getCrimeWithIndex(1));*/
    }

    public List<Crime> getCrimes() {
        return mCrimes;
    }

    public void setCrimes(List<Crime> crimes) {
        mCrimes = crimes;
    }

    public Crime getCrime(UUID id) {
        for (Crime crime : mCrimes) {
            if (crime.getId().equals(id))
                return crime;
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
        return CRIME_SIZE;
    }

    @Override
    public int getIndexOfCrime(Crime crime) {
        for (int i = 0; i < CRIME_SIZE; i++) {
            if (mCrimes.get(i).equals(crime))
                return i;
        }
        return -1;
    }

    @Override
    public Crime getCrimeWithIndex(int index) {
        return mCrimes.get(index);
    }

    public void insertCrime(Crime crime) {
        mCrimes.add(crime);
    }


    @Override
    public void deleteCrime(Crime crime) {
        for (int i = 0; i < mCrimes.size(); i++) {
            if (mCrimes.get(i).getId().equals(crime.getId())) {
                mCrimes.remove(i);
                return;
            }
        }
    }

    @Override
    public void updateCrime(Crime crime) {
        Crime findCrime = getCrime(crime.getId());
        findCrime.setTitle(crime.getTitle());
        findCrime.setSolved(crime.isSolved());
        findCrime.setDate(crime.getDate());
    }

    @Override
    public int getPosition(UUID crimeId) {
        for (int i = 0; i < mCrimes.size(); i++) {
            if (mCrimes.get(i).getId().equals(crimeId))
                return i;
        }

        return 0;
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

}
