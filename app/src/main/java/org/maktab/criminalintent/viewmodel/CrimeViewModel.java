package org.maktab.criminalintent.viewmodel;

import android.content.Context;

import androidx.databinding.BaseObservable;
import androidx.databinding.Bindable;

import org.maktab.criminalintent.adapter.CrimeAdapter;
import org.maktab.criminalintent.model.Crime;
import org.maktab.criminalintent.repository.CrimeDBRepository;
import org.maktab.criminalintent.repository.IRepository;
import org.maktab.criminalintent.view.fragment.CrimeListFragment;

public class CrimeViewModel extends BaseObservable {

    private Crime mCrime;
    private Callbacks mCallbacks;
    private IRepository mIRepository;

    public Crime getCrime() {
        return mCrime;
    }

    public void setCrime(Crime crime, Context context) {
        mCrime = crime;
        mIRepository = CrimeDBRepository.getInstance(context);
        notifyChange();
        if (context instanceof Callbacks)
            mCallbacks = (Callbacks) context;
        else {
            throw new ClassCastException(context.toString()
                    + " must implement Callbacks");
        }
    }

    @Bindable
    public String getTitle() {
        return mCrime.getTitle();
    }

    @Bindable
    public String getDate() {
        return mCrime.getDate().toString();
    }

    @Bindable
    public boolean getSolved() {
        return mCrime.isSolved();
    }

    public interface Callbacks {
        void onCrimeSelected(Crime crime);
    }

    public void click(){
        mCallbacks.onCrimeSelected(mCrime);
    }



    public void check(boolean isChecked) {
        Crime crime = mIRepository.getCrime(mCrime.getId());
        crime.setCheck_Select(isChecked ? 1 : 0);
        mIRepository.updateCrime(crime);
    }

    public boolean getCheck() {
        if (mCrime.getCheck_Select() == 0)
            return true;
        else
            return false;
    }
}
