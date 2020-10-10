package org.maktab.criminalintent.model;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Entity(tableName = "crimeTable")
public class Crime {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private long primaryId;

    @ColumnInfo(name = "uuid")
    private UUID mId;

    @ColumnInfo(name = "title")
    private String mTitle;

    @ColumnInfo(name = "date")
    private Date mDate;

    @ColumnInfo(name = "solved")
    private boolean mSolved;

    @ColumnInfo(name = "suspect")
    private String mSuspect;

    @ColumnInfo(name = "suspectPhone")
    private String mSuspectPhoneNumber;

    private boolean mCheck_Select;

    public long getPrimaryId() {
        return primaryId;
    }

    public void setPrimaryId(long primaryId) {
        this.primaryId = primaryId;
    }

    public UUID getId() {
        return mId;
    }

    public void setId(UUID id) {
        mId = id;
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

    public String getSuspect() {
        return mSuspect;
    }

    public void setSuspect(String suspect) {
        mSuspect = suspect;
    }

    public String getSuspectPhoneNumber() {
        return mSuspectPhoneNumber;
    }

    public void setSuspectPhoneNumber(String suspectPhoneNumber) {
        mSuspectPhoneNumber = suspectPhoneNumber;
    }

    public boolean isCheck_Select() {
        return mCheck_Select;
    }

    public void setCheck_Select(boolean check_Select) {
        mCheck_Select = check_Select;
    }


    public Crime(){
        this(UUID.randomUUID());
    }

    public Crime(UUID id) {
        mId = id;
        mDate = new Date();
        mCheck_Select = false;
    }

    public Crime(UUID id, String title, Date date, boolean solved,String suspect,String suspectPhoneNumber) {
        mId = id;
        mTitle = title;
        mDate = date;
        mSolved = solved;
        mCheck_Select = false;
        mSuspect = suspect;
        mSuspectPhoneNumber = suspectPhoneNumber;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Crime crime = (Crime) o;
        return primaryId == crime.primaryId &&
                mSolved == crime.mSolved &&
                mCheck_Select == crime.mCheck_Select &&
                Objects.equals(mId, crime.mId) &&
                Objects.equals(mTitle, crime.mTitle) &&
                Objects.equals(mDate, crime.mDate) &&
                Objects.equals(mSuspect, crime.mSuspect) &&
                Objects.equals(mSuspectPhoneNumber, crime.mSuspectPhoneNumber);
    }

    @Override
    public int hashCode() {
        return Objects.hash(primaryId, mId, mTitle, mDate, mSolved, mSuspect, mSuspectPhoneNumber, mCheck_Select);
    }

    public String getPhotoFileName() {
        return "IMG_" + getId().toString() + ".jpg";
    }
}
