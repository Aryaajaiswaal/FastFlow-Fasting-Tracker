package com.example.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.entity.FastingRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface FastingDao {
    @Query("SELECT * FROM fasting_records WHERE status = 'ACTIVE' ORDER BY startTimeMillis DESC LIMIT 1")
    fun getActiveFast(): Flow<FastingRecord?>

    @Query("SELECT * FROM fasting_records WHERE status = 'ACTIVE' ORDER BY startTimeMillis DESC LIMIT 1")
    suspend fun getActiveFastDirect(): FastingRecord?

    @Query("SELECT * FROM fasting_records ORDER BY startTimeMillis DESC")
    fun getAllFasts(): Flow<List<FastingRecord>>

    @Query("SELECT * FROM fasting_records WHERE status = 'COMPLETED' ORDER BY startTimeMillis DESC")
    fun getCompletedFasts(): Flow<List<FastingRecord>>

    @Query("SELECT * FROM fasting_records WHERE id = :id")
    suspend fun getFastById(id: Long): FastingRecord?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFast(fast: FastingRecord): Long

    @Update
    suspend fun updateFast(fast: FastingRecord)

    @Delete
    suspend fun deleteFast(fast: FastingRecord)

    @Query("DELETE FROM fasting_records WHERE id = :id")
    suspend fun deleteFastById(id: Long)
}
