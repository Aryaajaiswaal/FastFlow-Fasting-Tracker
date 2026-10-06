package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "water_logs")
data class WaterLog(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val amountMl: Int,
    val timestampMillis: Long = System.currentTimeMillis(),
    val dateKey: String // Format: YYYY-MM-DD
)
