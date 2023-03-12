package com.hidenobi.myhealth.data.stepcounter

import androidx.room.*

@Dao
interface StepCounterDao {

    @Query("SELECT * FROM stepCounter")
    suspend fun getAllStepCounter(): MutableList<StepCounter>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addStepCounter(stepCounter: StepCounter)

    @Update
    suspend fun updateStepCounter(stepCounter: StepCounter)

    @Query("SELECT * FROM stepCounter WHERE  dateStepCounter=:searchDateStepCounter")
    suspend fun searchByDate(searchDateStepCounter: String): StepCounter

}