package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_settings")
data class UserSettings(
    @PrimaryKey
    val id: Int = 1,
    val selectedPlanName: String = "16:8 LeanGains",
    val targetFastHours: Float = 16f,
    val eatingWindowHours: Float = 8f,
    val dailyWaterGoalMl: Int = 2500,
    val enableNotifications: Boolean = true
)
