package com.example.data.repository

import com.example.data.dao.FastingDao
import com.example.data.dao.SettingsDao
import com.example.data.dao.WaterDao
import com.example.data.entity.FastingRecord
import com.example.data.entity.UserSettings
import com.example.data.entity.WaterLog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class FastingRepository(
    private val fastingDao: FastingDao,
    private val waterDao: WaterDao,
    private val settingsDao: SettingsDao
) {
    val activeFast: Flow<FastingRecord?> = fastingDao.getActiveFast()
    val allFasts: Flow<List<FastingRecord>> = fastingDao.getAllFasts()
    val completedFasts: Flow<List<FastingRecord>> = fastingDao.getCompletedFasts()

    val settings: Flow<UserSettings> = settingsDao.getSettings().map {
        it ?: UserSettings()
    }

    private fun getTodayDateKey(): String {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        return sdf.format(Date())
    }

    fun getTodayWaterLogs(): Flow<List<WaterLog>> {
        return waterDao.getWaterLogsForDate(getTodayDateKey())
    }

    fun getTodayWaterTotal(): Flow<Int> {
        return waterDao.getTotalWaterForDate(getTodayDateKey()).map { it ?: 0 }
    }

    suspend fun startFast(
        planName: String,
        targetHours: Float,
        customStartTime: Long? = null
    ): Long {
        // First end or cancel any existing active fast if one somehow exists
        val currentActive = fastingDao.getActiveFastDirect()
        if (currentActive != null) {
            fastingDao.updateFast(
                currentActive.copy(
                    endTimeMillis = System.currentTimeMillis(),
                    status = "COMPLETED"
                )
            )
        }

        val startTime = customStartTime ?: System.currentTimeMillis()
        val record = FastingRecord(
            startTimeMillis = startTime,
            targetDurationHours = targetHours,
            planName = planName,
            status = "ACTIVE"
        )
        return fastingDao.insertFast(record)
    }

    suspend fun endFast(
        fastId: Long,
        endTime: Long = System.currentTimeMillis(),
        feeling: String? = null,
        note: String? = null,
        weightKg: Float? = null
    ) {
        val fast = fastingDao.getFastById(fastId) ?: return
        val updated = fast.copy(
            endTimeMillis = endTime,
            status = "COMPLETED",
            feeling = feeling ?: fast.feeling,
            note = note ?: fast.note,
            weightKg = weightKg ?: fast.weightKg
        )
        fastingDao.updateFast(updated)
    }

    suspend fun cancelFast(fastId: Long) {
        val fast = fastingDao.getFastById(fastId) ?: return
        val updated = fast.copy(
            endTimeMillis = System.currentTimeMillis(),
            status = "CANCELLED"
        )
        fastingDao.updateFast(updated)
    }

    suspend fun updateFastStartTime(fastId: Long, newStartTime: Long) {
        val fast = fastingDao.getFastById(fastId) ?: return
        fastingDao.updateFast(fast.copy(startTimeMillis = newStartTime))
    }

    suspend fun updateFastDetails(
        fastId: Long,
        feeling: String?,
        note: String?,
        weightKg: Float?
    ) {
        val fast = fastingDao.getFastById(fastId) ?: return
        fastingDao.updateFast(
            fast.copy(
                feeling = feeling,
                note = note,
                weightKg = weightKg
            )
        )
    }

    suspend fun deleteFast(fastId: Long) {
        fastingDao.deleteFastById(fastId)
    }

    suspend fun addWater(amountMl: Int) {
        val log = WaterLog(
            amountMl = amountMl,
            dateKey = getTodayDateKey()
        )
        waterDao.insertWaterLog(log)
    }

    suspend fun undoWater() {
        waterDao.removeLastWaterLogForDate(getTodayDateKey())
    }

    suspend fun updatePlan(planName: String, targetHours: Float, eatHours: Float) {
        val current = settingsDao.getSettingsDirect() ?: UserSettings()
        settingsDao.saveSettings(
            current.copy(
                selectedPlanName = planName,
                targetFastHours = targetHours,
                eatingWindowHours = eatHours
            )
        )
    }

    suspend fun updateWaterGoal(goalMl: Int) {
        val current = settingsDao.getSettingsDirect() ?: UserSettings()
        settingsDao.saveSettings(current.copy(dailyWaterGoalMl = goalMl))
    }

    suspend fun toggleNotifications(enabled: Boolean) {
        val current = settingsDao.getSettingsDirect() ?: UserSettings()
        settingsDao.saveSettings(current.copy(enableNotifications = enabled))
    }

    suspend fun initializeDefaultSettingsIfNeeded() {
        if (settingsDao.getSettingsDirect() == null) {
            settingsDao.saveSettings(UserSettings())
        }
    }
}
