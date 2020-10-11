package org.maktab.criminalintent.repository;

import android.content.Context;
import androidx.room.Room;
import org.maktab.criminalintent.database.CrimeDatabase;
import org.maktab.criminalintent.database.CrimeDatabaseDAO;
import org.maktab.criminalintent.model.Crime;

import java.io.File;
import java.util.List;
import java.util.UUID;

public class CrimeDBRepository implements IRepository {

    private static CrimeDBRepository sInstance;

    private CrimeDatabaseDAO mCrimeDAO;
    private Context mContext;

    public static CrimeDBRepository getInstance(Context context) {
        if (sInstance == null)
            sInstance = new CrimeDBRepository(context);

        return sInstance;
    }

    private CrimeDBRepository(Context context) {
        mContext = context.getApplicationContext();
        CrimeDatabase crimeDatabase = Room.databaseBuilder(mContext,
                CrimeDatabase.class,
                "crime.db")
                .allowMainThreadQueries()
                .build();

        mCrimeDAO = crimeDatabase.getCrimeDatabaseDAO();
    }

    @Override
    public List<Crime> getCrimes() {
        return mCrimeDAO.getCrimes();
    }

    @Override
    public Crime getCrime(UUID crimeId) {
       return mCrimeDAO.getCrime(crimeId);
    }

    @Override
    public void insertCrime(Crime crime) {
        mCrimeDAO.insertCrime(crime);
    }

    @Override
    public void updateCrime(Crime crime) {
       mCrimeDAO.updateCrime(crime);
    }

    @Override
    public void deleteCrime(Crime crime) {
       mCrimeDAO.deleteCrime(crime);
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
        mCrimeDAO.setCrimesSelected();
    }

    @Override
    public void setCrimesUnSelected(){
        mCrimeDAO.setCrimesUnSelected();
    }

    @Override
    public void deleteSelectedCrime(){
        mCrimeDAO.deleteSelectedCrime();
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

    @Override
    public File getPhotoFile(Crime crime) {
        // /data/data/com.example.criminalintent/files/
        File filesDir = mContext.getFilesDir();

        // /data/data/com.example.criminalintent/files/IMG_ktui4u544nmkfuy48485.jpg
        File photoFile = new File(filesDir, crime.getPhotoFileName());
        return photoFile;
    }

}
