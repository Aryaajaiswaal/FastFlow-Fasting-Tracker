package com.example.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.entity.WaterLog
import kotlinx.coroutines.flow.Flow

@Dao
interface WaterDao {
    @Query("SELECT * FROM water_logs WHERE dateKey = :dateKey ORDER BY timestampMillis DESC")
    fun getWaterLogsForDate(dateKey: String): Flow<List<WaterLog>>

    @Query("SELECT SUM(amountMl) FROM water_logs WHERE dateKey = :dateKey")
    fun getTotalWaterForDate(dateKey: String): Flow<Int?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWaterLog(waterLog: WaterLog): Long

    @Query("DELETE FROM water_logs WHERE id = (SELECT id FROM water_logs WHERE dateKey = :dateKey ORDER BY timestampMillis DESC LIMIT 1)")
    suspend fun removeLastWaterLogForDate(dateKey: String)
}
