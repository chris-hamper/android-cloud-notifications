package com.chrishamper.statusnotifications.data

import android.content.Context
import androidx.room.*
import java.util.*

@Database(entities = [Message::class], version = 1, exportSchema = true)
@TypeConverters(DateConverter::class)
public abstract class MessageRoomDatabase : RoomDatabase() {
    abstract fun messageDao(): MessageDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: MessageRoomDatabase? = null

        fun getDatabase(context: Context): MessageRoomDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    MessageRoomDatabase::class.java,
                    "word_database"
                ).build()

                INSTANCE = instance
                instance
            }
        }
    }
}

class DateConverter {
    @TypeConverter
    fun toDate(dateLong: Long?): Date? {
        return dateLong?.let { Date(it) }
    }

    @TypeConverter
    fun fromDate(date: Date?): Long? {
        return date?.time
    }
}