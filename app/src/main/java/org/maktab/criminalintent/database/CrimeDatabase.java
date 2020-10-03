package org.maktab.criminalintent.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import org.maktab.criminalintent.model.Crime;
import org.maktab.criminalintent.model.User;

@Database(entities = {Crime.class, User.class}, version = 1)
@TypeConverters({Converters.class})
public abstract class CrimeDatabase extends RoomDatabase {

    public abstract CrimeDatabaseDAO getCrimeDatabaseDAO();
}
