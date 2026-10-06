package com.example.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fasting_records")
data class FastingRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val startTimeMillis: Long,
    val endTimeMillis: Long? = null,
    val targetDurationHours: Float = 16f,
    val planName: String = "16:8 LeanGains",
    val status: String = "ACTIVE", // ACTIVE, COMPLETED, CANCELLED
    val feeling: String? = null, // GREAT, ENERGETIC, GOOD, TIRED, HUNGRY
    val note: String? = null,
    val weightKg: Float? = null
) {
    val durationMillis: Long
        get() = (endTimeMillis ?: System.currentTimeMillis()) - startTimeMillis

    val durationHours: Float
        get() = durationMillis / (1000f * 60f * 60f)

    val targetMillis: Long
        get() = (targetDurationHours * 60 * 60 * 1000).toLong()

    val isGoalAchieved: Boolean
        get() = durationHours >= targetDurationHours
}
