package org.maktab.criminalintent.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import static org.maktab.criminalintent.database.UserDBSchema.UserTable.Cols;

import androidx.annotation.Nullable;

public class UserDBHelper extends SQLiteOpenHelper {

    public UserDBHelper(@Nullable Context context) {
        super(context, UserDBSchema.NAME, null, UserDBSchema.VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder sbQuery = new StringBuilder();
        sbQuery.append("CREATE TABLE " + UserDBSchema.UserTable.NAME + " (");
        sbQuery.append(Cols.ID + " INTEGER PRIMARY KEY AUTOINCREMENT,");
        sbQuery.append(Cols.USERNAME + " TEXT NOT NULL,");
        sbQuery.append(Cols.PASSWORD + " TEXT NOT NULL");
        sbQuery.append(");");

        db.execSQL(sbQuery.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
