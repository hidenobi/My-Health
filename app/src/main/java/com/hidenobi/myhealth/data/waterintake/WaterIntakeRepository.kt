package com.hidenobi.myhealth.data.waterintake

class WaterIntakeRepository(private val waterIntakeDao: WaterIntakeDao) {


    suspend fun getAllCountWaterIntake(): MutableList<WaterIntake> {
        return waterIntakeDao.getAllCountWaterIntake()
    }

    suspend fun addCountWaterIntake(waterIntake: WaterIntake) {
        waterIntakeDao.addCountWaterIntake(waterIntake)
    }

    suspend fun updateWaterIntake(waterIntake: WaterIntake) {
        waterIntakeDao.updateWaterIntake(waterIntake)
    }

    fun searchByDate(searchDateCountWaterIntake: String): WaterIntake {
        return waterIntakeDao.searchByDate(searchDateCountWaterIntake)
    }
}