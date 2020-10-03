package org.maktab.criminalintent.database;

import androidx.room.TypeConverter;
import java.util.Date;
import java.util.UUID;

public class Converters {

    @TypeConverter
    public static Date LongToDate(long timestamp){
        return new Date(timestamp);
    }
    @TypeConverter
    public static Long DateToLong(Date date){
        return date.getTime();
    }

    @TypeConverter
    public static UUID StringToUUID(String value){
        return UUID.fromString(value);
    }

    @TypeConverter

    public static String  UUIDTOString(UUID uuid){
        return uuid.toString();
    }
}
