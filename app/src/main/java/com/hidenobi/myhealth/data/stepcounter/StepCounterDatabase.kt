package com.hidenobi.myhealth.data.stepcounter

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase


@Database(entities = [StepCounter::class], version = 1, exportSchema = false)
abstract class StepCounterDatabase : RoomDatabase() {

    companion object {
        private var stepCounterDatabase: StepCounterDatabase? = null

        @Synchronized
        fun getDatabase(context: Context): StepCounterDatabase {
            if (stepCounterDatabase == null) {
                stepCounterDatabase = Room.databaseBuilder(
                    context,
                    StepCounterDatabase::class.java,
                    "stepCounter.db"
                ).fallbackToDestructiveMigration().allowMainThreadQueries().build()
            }
            return stepCounterDatabase!!
        }
    }

    abstract fun stepCounterDao(): StepCounterDao
}