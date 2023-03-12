package com.hidenobi.myhealth.data.stepcounter

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "stepCounter")
data class StepCounter(
    @PrimaryKey @ColumnInfo(name = "dateStepCounter") var date: String,
    @ColumnInfo(name = "stepCounter") var stepCounter: Int
) : java.io.Serializable