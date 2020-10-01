package org.maktab.criminalintent;

import android.database.Cursor;
import android.database.CursorWrapper;

import org.maktab.criminalintent.database.CrimeDBSchema.CrimeTable.Cols;
import org.maktab.criminalintent.model.Crime;

import java.util.Date;
import java.util.UUID;

public class CrimeCursorWrapper extends CursorWrapper {

    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public CrimeCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Crime getCrime() {
        UUID uuid = UUID.fromString(getString(getColumnIndex(Cols.UUID)));
        String title = getString(getColumnIndex(Cols.TITLE));
        Date date = new Date(getLong(getColumnIndex(Cols.DATE)));
        boolean solved = getInt(getColumnIndex(Cols.SOLVED)) == 0 ? false : true;
        String suspect = getString(getColumnIndex(Cols.SUSPECT));

        return new Crime(uuid, title, date, solved, suspect);
    }
}
