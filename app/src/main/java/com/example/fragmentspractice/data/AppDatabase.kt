package com.example.fragmentspractice.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Challenge::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun challengeDao(): ChallengeDao

    companion object {
        @Volatile
        private var instance: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return instance ?: synchronized(this) {
                instance ?: Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "pico_botella_database"
                ).build().also { instance = it }
            }
        }
    }
}
