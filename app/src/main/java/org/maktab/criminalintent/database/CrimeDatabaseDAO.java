package org.maktab.criminalintent.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import org.maktab.criminalintent.model.Crime;
import org.maktab.criminalintent.model.User;
import java.util.List;
import java.util.UUID;

@Dao
public interface CrimeDatabaseDAO {

    @Update
    void updateCrime(Crime crime);

    @Insert
    void insertCrime(Crime crime);

    @Insert
    void insertCrimes(Crime... crimes);

    @Delete
    void deleteCrime(Crime crime);

    @Query("SELECT * FROM crimeTable")
    List<Crime> getCrimes();

    @Query("SELECT * FROM crimeTable WHERE uuid =:inputUUID")
    Crime getCrime(UUID inputUUID);

    @Query("SELECT * FROM crimeTable ORDER BY id ASC LIMIT 1")
    Crime getLastCrime();

    @Insert
    void insertUser(User user);

    @Query("SELECT * FROM userTable")
    List<User> getUsers();

    @Query("SELECT * FROM userTable WHERE  username=:name")
    User getUser(String name);

}
