package org.maktab.criminalintent.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import static org.maktab.criminalintent.database.CrimeDBSchema.*;

import androidx.annotation.Nullable;

public class CrimeDBHelper extends SQLiteOpenHelper {
    public CrimeDBHelper(@Nullable Context context) {
        super(context, CrimeDBSchema.NAME, null, CrimeDBSchema.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder sbQuery_crime = new StringBuilder();
        sbQuery_crime.append("CREATE TABLE " + CrimeDBSchema.CrimeTable.NAME + " (");
        sbQuery_crime.append(CrimeTable.Cols.ID + " INTEGER PRIMARY KEY AUTOINCREMENT,");
        sbQuery_crime.append(CrimeTable.Cols.UUID + " TEXT NOT NULL,");
        sbQuery_crime.append(CrimeTable.Cols.TITLE + " TEXT,");
        sbQuery_crime.append(CrimeTable.Cols.DATE + " TEXT,");
        sbQuery_crime.append(CrimeTable.Cols.SOLVED + " INTEGER");
        sbQuery_crime.append(");");
        db.execSQL(sbQuery_crime.toString());

        StringBuilder sbQuery_user = new StringBuilder();
        sbQuery_user.append("CREATE TABLE " + CrimeDBSchema.UserTable.NAME + " (");
        sbQuery_user.append(UserTable.Cols.ID + " INTEGER PRIMARY KEY AUTOINCREMENT,");
        sbQuery_user.append(UserTable.Cols.USERNAME + " TEXT NOT NULL,");
        sbQuery_user.append(UserTable.Cols.PASSWORD + " TEXT");
        sbQuery_user.append(");");

        db.execSQL(sbQuery_user.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
