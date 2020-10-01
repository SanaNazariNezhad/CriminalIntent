package org.maktab.criminalintent.model;

import android.os.Build;

import org.maktab.criminalintent.utils.DateUtils;

import java.util.Date;
import java.util.UUID;

public class Crime {
    private String mTitle;
    private Date mDate;
    private boolean mSolved;
    private String mSuspect;
    private boolean mCheck_Select;
    private UUID mId;

    public Crime(){
        this(UUID.randomUUID());
        /*mId = UUID.randomUUID();
        mDate = DateUtils.randomDate();
        mCheck_Select = false;*/
    }

    public Crime(UUID id) {
        mId = id;
        mDate = new Date();
        mCheck_Select = false;
    }

    public Crime(UUID id, String title, Date date, boolean solved,String suspect) {
        mId = id;
        mTitle = title;
        mDate = date;
        mSolved = solved;
        mCheck_Select = false;
        mSuspect = suspect;
    }

    public String getSuspect() {
        return mSuspect;
    }

    public void setSuspect(String suspect) {
        mSuspect = suspect;
    }

    public boolean isCheck_Select() {
        return mCheck_Select;
    }

    public void setCheck_Select(boolean check_Select) {
        mCheck_Select = check_Select;
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
