package com.hidenobi.myhealth.data.waterintake

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "waterIntake")
data class WaterIntake(
    @PrimaryKey @ColumnInfo(name = "dateCountWaterIntake") var date: String,
    @ColumnInfo(name = "countWaterIntake") var countWaterIntake: Int
) : java.io.Serializable
