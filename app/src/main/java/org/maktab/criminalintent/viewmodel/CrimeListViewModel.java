package org.maktab.criminalintent.viewmodel;

import android.content.Context;

import org.maktab.criminalintent.model.Crime;
import org.maktab.criminalintent.repository.CrimeDBRepository;
import org.maktab.criminalintent.repository.IRepository;

import java.io.File;
import java.util.List;
import java.util.UUID;

public class CrimeListViewModel {

    private IRepository mIRepository;

    public CrimeListViewModel(Context context) {
        mIRepository = CrimeDBRepository.getInstance(context);
    }

    public List<Crime> getCrimes() {
        return mIRepository.getCrimes();
    }

    public Crime getCrime(UUID crimeId) {
        return mIRepository.getCrime(crimeId);
    }

    public void insertCrime(Crime crime) {
        mIRepository.insertCrime(crime);
    }

    public void updateCrime(Crime crime) {
        mIRepository.updateCrime(crime);
    }

    public void deleteCrime(Crime crime) {
        mIRepository.deleteCrime(crime);
    }

    public int getPosition(UUID crimeId) {
        return mIRepository.getPosition(crimeId);
    }

    public int getIndexOfCrime(Crime crime){
        return mIRepository.getIndexOfCrime(crime);
    }

    public Crime getCrimeWithIndex(int index){
        return mIRepository.getCrimeWithIndex(index);
    }

    public int repositorySize(){
        return mIRepository.repositorySize();
    }

    public void setCrimesSelected() {
        mIRepository.setCrimesSelected();
    }

    public void setCrimesUnSelected() {
        mIRepository.setCrimesUnSelected();
    }

    public void deleteSelectedCrime() {
        mIRepository.deleteSelectedCrime();
    }

    public File getPhotoFile(Crime crime) {
        return mIRepository.getPhotoFile(crime);
    }
}
