package com.hidenobi.myhealth.data.stepcounter

class StepCounterRepository(private val stepCounterDao: StepCounterDao) {

    suspend fun getAllStepCounter(): MutableList<StepCounter> {
        return stepCounterDao.getAllStepCounter()
    }

    suspend fun addStepCounter(stepCounter: StepCounter) {
        stepCounterDao.addStepCounter(stepCounter)
    }

    suspend fun updateStepCounter(stepCounter: StepCounter) {
        stepCounterDao.updateStepCounter(stepCounter)
    }

    suspend fun searchByDate(searchDateStepCounter: String): StepCounter {
        return stepCounterDao.searchByDate(searchDateStepCounter)
    }
}