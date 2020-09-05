package org.maktab.criminalintent.model;

import android.os.Build;

import org.maktab.criminalintent.utils.DateUtils;

import java.util.Date;
import java.util.UUID;

public class Crime {
    private String mTitle;
    private Date mDate;
    private boolean mSolved;
    private UUID mId;

    public Crime(){
        mId = UUID.randomUUID();
        mDate = DateUtils.randomDate();
    }


    public UUID getId() {
        return mId;
    }

    public String getTitle() {
        return mTitle;
    }

    public void setTitle(String title) {
        mTitle = title;
    }

    public Date getDate() {
        return mDate;
    }

    public void setDate(Date date) {
        mDate = date;
    }

    public boolean isSolved() {
        return mSolved;
    }

    public void setSolved(boolean solved) {
        mSolved = solved;
    }

}
