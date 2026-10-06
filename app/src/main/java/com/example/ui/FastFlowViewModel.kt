package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.AppDatabase
import com.example.data.entity.FastingRecord
import com.example.data.entity.UserSettings
import com.example.data.entity.WaterLog
import com.example.data.repository.FastingRepository
import com.example.model.FastingPlan
import com.example.model.FastingStage
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class FastingStats(
    val totalFasts: Int = 0,
    val totalFastingHours: Float = 0f,
    val averageFastingHours: Float = 0f,
    val longestFastHours: Float = 0f,
    val currentStreakDays: Int = 0,
    val weeklyChartBars: List<DailyFastBar> = emptyList()
)

data class DailyFastBar(
    val dayLabel: String,
    val dateKey: String,
    val hours: Float,
    val isToday: Boolean,
    val targetHours: Float
)

sealed class AppScreen {
    object Timer : AppScreen()
    object Stages : AppScreen()
    object History : AppScreen()
    object Stats : AppScreen()
    object Learn : AppScreen()
}

class FastFlowViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: FastingRepository

    init {
        val database = AppDatabase.getDatabase(application)
        repository = FastingRepository(
            database.fastingDao(),
            database.waterDao(),
            database.settingsDao()
        )
        viewModelScope.launch {
            repository.initializeDefaultSettingsIfNeeded()
        }
    }

    private val _currentScreen = MutableStateFlow<AppScreen>(AppScreen.Timer)
    val currentScreen: StateFlow<AppScreen> = _currentScreen.asStateFlow()

    fun navigateTo(screen: AppScreen) {
        _currentScreen.value = screen
    }

    // Ticker flow for real-time second-by-second updates
    private val _currentTime = MutableStateFlow(System.currentTimeMillis())
    val currentTime: StateFlow<Long> = _currentTime.asStateFlow()

    init {
        viewModelScope.launch {
            while (true) {
                delay(1000)
                _currentTime.value = System.currentTimeMillis()
            }
        }
    }

    val activeFast: StateFlow<FastingRecord?> = repository.activeFast
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val completedFasts: StateFlow<List<FastingRecord>> = repository.completedFasts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allFasts: StateFlow<List<FastingRecord>> = repository.allFasts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val settings: StateFlow<UserSettings> = repository.settings
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), UserSettings())

    val todayWaterTotal: StateFlow<Int> = repository.getTodayWaterTotal()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    val todayWaterLogs: StateFlow<List<WaterLog>> = repository.getTodayWaterLogs()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Dialog & UI interaction states
    var showPlanDialog = MutableStateFlow(false)
        private set

    var showEditStartDialog = MutableStateFlow(false)
        private set

    var showFinishDialog = MutableStateFlow(false)
        private set

    var showWaterGoalDialog = MutableStateFlow(false)
        private set

    var showCustomHoursDialog = MutableStateFlow(false)
        private set

    fun setPlanDialogVisible(visible: Boolean) { showPlanDialog.value = visible }
    fun setEditStartDialogVisible(visible: Boolean) { showEditStartDialog.value = visible }
    fun setFinishDialogVisible(visible: Boolean) { showFinishDialog.value = visible }
    fun setWaterGoalDialogVisible(visible: Boolean) { showWaterGoalDialog.value = visible }
    fun setCustomHoursDialogVisible(visible: Boolean) { showCustomHoursDialog.value = visible }

    // Fast actions
    fun startFast(customStartTime: Long? = null) {
        viewModelScope.launch {
            val userSettings = settings.value
            repository.startFast(
                planName = userSettings.selectedPlanName,
                targetHours = userSettings.targetFastHours,
                customStartTime = customStartTime
            )
        }
    }

    fun endFast(feeling: String?, note: String?, weightKg: Float?) {
        viewModelScope.launch {
            val current = activeFast.value ?: return@launch
            repository.endFast(
                fastId = current.id,
                endTime = System.currentTimeMillis(),
                feeling = feeling,
                note = note,
                weightKg = weightKg
            )
            setFinishDialogVisible(false)
        }
    }

    fun cancelFast() {
        viewModelScope.launch {
            val current = activeFast.value ?: return@launch
            repository.cancelFast(current.id)
            setFinishDialogVisible(false)
        }
    }

    fun updateStartTime(newStartTime: Long) {
        viewModelScope.launch {
            val current = activeFast.value ?: return@launch
            repository.updateFastStartTime(current.id, newStartTime)
            setEditStartDialogVisible(false)
        }
    }

    fun deleteFast(id: Long) {
        viewModelScope.launch {
            repository.deleteFast(id)
        }
    }

    fun updateFastRecord(id: Long, feeling: String?, note: String?, weightKg: Float?) {
        viewModelScope.launch {
            repository.updateFastDetails(id, feeling, note, weightKg)
        }
    }

    // Hydration actions
    fun addWater(amountMl: Int) {
        viewModelScope.launch {
            repository.addWater(amountMl)
        }
    }

    fun undoWater() {
        viewModelScope.launch {
            repository.undoWater()
        }
    }

    fun updateWaterGoal(goalMl: Int) {
        viewModelScope.launch {
            repository.updateWaterGoal(goalMl)
            setWaterGoalDialogVisible(false)
        }
    }

    // Plan selection
    fun selectPlan(plan: FastingPlan) {
        viewModelScope.launch {
            repository.updatePlan(
                planName = plan.name,
                targetHours = plan.fastHours,
                eatHours = plan.eatHours
            )
            setPlanDialogVisible(false)
        }
    }

    fun setCustomPlan(fastHours: Float, eatHours: Float) {
        viewModelScope.launch {
            repository.updatePlan(
                planName = "Custom ${fastHours.toInt()}:${eatHours.toInt()}",
                targetHours = fastHours,
                eatHours = eatHours
            )
            setCustomHoursDialogVisible(false)
            setPlanDialogVisible(false)
        }
    }

    // Fast calculation helpers
    fun getActiveElapsedMillis(): Long {
        val fast = activeFast.value ?: return 0L
        val now = currentTime.value
        return (now - fast.startTimeMillis).coerceAtLeast(0L)
    }

    fun getActiveElapsedHours(): Float {
        return getActiveElapsedMillis() / (1000f * 60f * 60f)
    }

    fun getActiveProgress(): Float {
        val fast = activeFast.value ?: return 0f
        val targetMillis = (fast.targetDurationHours * 3600 * 1000).toLong()
        if (targetMillis <= 0) return 0f
        return (getActiveElapsedMillis().toFloat() / targetMillis.toFloat()).coerceIn(0f, 2f)
    }

    fun getCurrentStage(): FastingStage {
        val hours = getActiveElapsedHours()
        return FastingStage.ALL_STAGES.find { it.isActive(hours) }
            ?: FastingStage.ALL_STAGES.last()
    }

    // Compute stats
    fun calculateStats(fasts: List<FastingRecord>): FastingStats {
        val completed = fasts.filter { it.status == "COMPLETED" }
        if (completed.isEmpty()) {
            return FastingStats(weeklyChartBars = generateEmptyWeeklyBars())
        }

        val totalFasts = completed.size
        val totalHours = completed.sumOf { it.durationHours.toDouble() }.toFloat()
        val avgHours = totalHours / totalFasts
        val maxHours = completed.maxOfOrNull { it.durationHours } ?: 0f

        // Calculate streaks (consecutive days with a completed fast)
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val daysWithFast = completed.map {
            sdf.format(Date(it.startTimeMillis))
        }.toSet()

        var streak = 0
        val cal = Calendar.getInstance()
        val todayStr = sdf.format(cal.time)
        cal.add(Calendar.DAY_OF_YEAR, -1)
        val yesterdayStr = sdf.format(cal.time)

        var checkCal = Calendar.getInstance()
        if (!daysWithFast.contains(todayStr) && !daysWithFast.contains(yesterdayStr)) {
            streak = 0
        } else {
            // Check backwards
            if (!daysWithFast.contains(todayStr)) {
                checkCal.add(Calendar.DAY_OF_YEAR, -1)
            }
            while (daysWithFast.contains(sdf.format(checkCal.time))) {
                streak++
                checkCal.add(Calendar.DAY_OF_YEAR, -1)
            }
        }

        // Weekly 7-day chart bars
        val dayLabelFormat = SimpleDateFormat("EEE", Locale.getDefault())
        val weeklyBars = mutableListOf<DailyFastBar>()
        val chartCal = Calendar.getInstance()
        chartCal.add(Calendar.DAY_OF_YEAR, -6) // 7 days ending today

        for (i in 0 until 7) {
            val dateKey = sdf.format(chartCal.time)
            val dayLabel = dayLabelFormat.format(chartCal.time)
            val isToday = dateKey == todayStr

            val dayTotalHours = completed
                .filter { sdf.format(Date(it.startTimeMillis)) == dateKey }
                .sumOf { it.durationHours.toDouble() }.toFloat()

            weeklyBars.add(
                DailyFastBar(
                    dayLabel = dayLabel,
                    dateKey = dateKey,
                    hours = dayTotalHours,
                    isToday = isToday,
                    targetHours = settings.value.targetFastHours
                )
            )
            chartCal.add(Calendar.DAY_OF_YEAR, 1)
        }

        return FastingStats(
            totalFasts = totalFasts,
            totalFastingHours = totalHours,
            averageFastingHours = avgHours,
            longestFastHours = maxHours,
            currentStreakDays = streak,
            weeklyChartBars = weeklyBars
        )
    }

    private fun generateEmptyWeeklyBars(): List<DailyFastBar> {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dayLabelFormat = SimpleDateFormat("EEE", Locale.getDefault())
        val chartCal = Calendar.getInstance()
        val todayStr = sdf.format(chartCal.time)
        chartCal.add(Calendar.DAY_OF_YEAR, -6)
        val bars = mutableListOf<DailyFastBar>()
        for (i in 0 until 7) {
            val dateKey = sdf.format(chartCal.time)
            bars.add(
                DailyFastBar(
                    dayLabel = dayLabelFormat.format(chartCal.time),
                    dateKey = dateKey,
                    hours = 0f,
                    isToday = dateKey == todayStr,
                    targetHours = settings.value.targetFastHours
                )
            )
            chartCal.add(Calendar.DAY_OF_YEAR, 1)
        }
        return bars
    }
}
