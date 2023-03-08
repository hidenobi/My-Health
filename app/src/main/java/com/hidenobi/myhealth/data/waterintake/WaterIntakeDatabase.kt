package com.hidenobi.myhealth.data.waterintake

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [WaterIntake::class], version = 1, exportSchema = false)
abstract class WaterIntakeDatabase : RoomDatabase() {

    companion object {
        private var waterIntakeDatabase: WaterIntakeDatabase? = null

        @Synchronized
        fun getDatabase(context: Context): WaterIntakeDatabase {
            if (waterIntakeDatabase == null) {
                waterIntakeDatabase = Room.databaseBuilder(
                    context,
                    WaterIntakeDatabase::class.java,
                    "waterIntake.db"
                ).fallbackToDestructiveMigration().allowMainThreadQueries().build()
            }
            return waterIntakeDatabase!!
        }

    }


    abstract fun waterIntakeDao(): WaterIntakeDao

}