package com.hidenobi.myhealth.data.waterintake

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface WaterIntakeDao {

    @Query("SELECT * FROM waterIntake")
    suspend fun getAllCountWaterIntake(): MutableList<WaterIntake>

    @Insert
    suspend fun addCountWaterIntake(waterIntake: WaterIntake)

    @Update
    suspend fun updateWaterIntake(waterIntake: WaterIntake)

    @Query("SELECT * FROM waterIntake WHERE  dateCountWaterIntake=:searchDateCountWaterIntake")
    fun searchByDate(searchDateCountWaterIntake: String): WaterIntake

}