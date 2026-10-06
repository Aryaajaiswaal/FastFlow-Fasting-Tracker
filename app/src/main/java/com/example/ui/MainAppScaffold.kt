package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material.icons.outlined.BarChart
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.History
import androidx.compose.material.icons.outlined.MenuBook
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.CustomPlanDialog
import com.example.ui.components.EditStartTimeDialog
import com.example.ui.components.FinishFastDialog
import com.example.ui.components.PlanSelectionDialog
import com.example.ui.components.WaterGoalDialog
import com.example.ui.screens.HistoryScreen
import com.example.ui.screens.LearnScreen
import com.example.ui.screens.StagesScreen
import com.example.ui.screens.StatsScreen
import com.example.ui.screens.TimerScreen

@Composable
fun MainAppScaffold(
    viewModel: FastFlowViewModel = viewModel()
) {
    val currentScreen by viewModel.currentScreen.collectAsStateWithLifecycle()
    val activeFast by viewModel.activeFast.collectAsStateWithLifecycle()
    val completedFasts by viewModel.completedFasts.collectAsStateWithLifecycle()
    val allFasts by viewModel.allFasts.collectAsStateWithLifecycle()
    val settings by viewModel.settings.collectAsStateWithLifecycle()
    val waterTotal by viewModel.todayWaterTotal.collectAsStateWithLifecycle()
    val currentTime by viewModel.currentTime.collectAsStateWithLifecycle()

    val showPlanDialog by viewModel.showPlanDialog.collectAsStateWithLifecycle()
    val showEditStartDialog by viewModel.showEditStartDialog.collectAsStateWithLifecycle()
    val showFinishDialog by viewModel.showFinishDialog.collectAsStateWithLifecycle()
    val showWaterGoalDialog by viewModel.showWaterGoalDialog.collectAsStateWithLifecycle()
    val showCustomHoursDialog by viewModel.showCustomHoursDialog.collectAsStateWithLifecycle()

    val elapsedMillis = viewModel.getActiveElapsedMillis()
    val elapsedHours = viewModel.getActiveElapsedHours()
    val currentStage = viewModel.getCurrentStage()
    val stats = viewModel.calculateStats(allFasts)

    // Back handler to navigate back to Timer screen from other tabs
    if (currentScreen != AppScreen.Timer) {
        BackHandler {
            viewModel.navigateTo(AppScreen.Timer)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                modifier = Modifier.testTag("main_bottom_nav"),
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 8.dp
            ) {
                NavigationBarItem(
                    selected = currentScreen == AppScreen.Timer,
                    onClick = { viewModel.navigateTo(AppScreen.Timer) },
                    icon = {
                        Icon(
                            imageVector = if (currentScreen == AppScreen.Timer) Icons.Filled.Timer else Icons.Outlined.Timer,
                            contentDescription = "Timer Tab"
                        )
                    },
                    label = { Text("Timer") },
                    modifier = Modifier.testTag("nav_timer_tab")
                )

                NavigationBarItem(
                    selected = currentScreen == AppScreen.Stages,
                    onClick = { viewModel.navigateTo(AppScreen.Stages) },
                    icon = {
                        Icon(
                            imageVector = if (currentScreen == AppScreen.Stages) Icons.Filled.Bolt else Icons.Outlined.Bolt,
                            contentDescription = "Stages Tab"
                        )
                    },
                    label = { Text("Stages") },
                    modifier = Modifier.testTag("nav_stages_tab")
                )

                NavigationBarItem(
                    selected = currentScreen == AppScreen.History,
                    onClick = { viewModel.navigateTo(AppScreen.History) },
                    icon = {
                        Icon(
                            imageVector = if (currentScreen == AppScreen.History) Icons.Filled.History else Icons.Outlined.History,
                            contentDescription = "History Tab"
                        )
                    },
                    label = { Text("History") },
                    modifier = Modifier.testTag("nav_history_tab")
                )

                NavigationBarItem(
                    selected = currentScreen == AppScreen.Stats,
                    onClick = { viewModel.navigateTo(AppScreen.Stats) },
                    icon = {
                        Icon(
                            imageVector = if (currentScreen == AppScreen.Stats) Icons.Filled.BarChart else Icons.Outlined.BarChart,
                            contentDescription = "Stats Tab"
                        )
                    },
                    label = { Text("Stats") },
                    modifier = Modifier.testTag("nav_stats_tab")
                )

                NavigationBarItem(
                    selected = currentScreen == AppScreen.Learn,
                    onClick = { viewModel.navigateTo(AppScreen.Learn) },
                    icon = {
                        Icon(
                            imageVector = if (currentScreen == AppScreen.Learn) Icons.Filled.MenuBook else Icons.Outlined.MenuBook,
                            contentDescription = "Learn Tab"
                        )
                    },
                    label = { Text("Learn") },
                    modifier = Modifier.testTag("nav_learn_tab")
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Crossfade(targetState = currentScreen, label = "screenTransition") { screen ->
                when (screen) {
                    AppScreen.Timer -> {
                        TimerScreen(
                            activeFast = activeFast,
                            settings = settings,
                            currentTime = currentTime,
                            elapsedMillis = elapsedMillis,
                            currentStage = currentStage,
                            waterTotalMl = waterTotal,
                            onStartFast = { viewModel.startFast() },
                            onEndFastClick = { viewModel.setFinishDialogVisible(true) },
                            onEditStartTimeClick = { viewModel.setEditStartDialogVisible(true) },
                            onPlanClick = { viewModel.setPlanDialogVisible(true) },
                            onAddWater = { ml -> viewModel.addWater(ml) },
                            onUndoWater = { viewModel.undoWater() },
                            onEditWaterGoal = { viewModel.setWaterGoalDialogVisible(true) },
                            onNavigateToStages = { viewModel.navigateTo(AppScreen.Stages) }
                        )
                    }
                    AppScreen.Stages -> {
                        StagesScreen(
                            elapsedHours = elapsedHours,
                            isFastingActive = activeFast != null
                        )
                    }
                    AppScreen.History -> {
                        HistoryScreen(
                            fasts = allFasts,
                            onDeleteFast = { id -> viewModel.deleteFast(id) },
                            onStartFastClick = {
                                viewModel.navigateTo(AppScreen.Timer)
                                viewModel.startFast()
                            }
                        )
                    }
                    AppScreen.Stats -> {
                        StatsScreen(stats = stats)
                    }
                    AppScreen.Learn -> {
                        LearnScreen()
                    }
                }
            }
        }
    }

    // Dialogs
    if (showPlanDialog) {
        PlanSelectionDialog(
            currentPlanName = settings.selectedPlanName,
            onSelectPlan = { plan -> viewModel.selectPlan(plan) },
            onCustomPlanClick = { viewModel.setCustomHoursDialogVisible(true) },
            onDismiss = { viewModel.setPlanDialogVisible(false) }
        )
    }

    if (showCustomHoursDialog) {
        CustomPlanDialog(
            initialHours = settings.targetFastHours,
            onSave = { fastHours, eatHours ->
                viewModel.setCustomPlan(fastHours, eatHours)
            },
            onDismiss = { viewModel.setCustomHoursDialogVisible(false) }
        )
    }

    if (showEditStartDialog && activeFast != null) {
        EditStartTimeDialog(
            currentStartTimeMillis = activeFast!!.startTimeMillis,
            onConfirmNewStartTime = { newStart -> viewModel.updateStartTime(newStart) },
            onDismiss = { viewModel.setEditStartDialogVisible(false) }
        )
    }

    if (showFinishDialog && activeFast != null) {
        FinishFastDialog(
            elapsedHours = elapsedHours,
            targetHours = activeFast?.targetDurationHours ?: settings.targetFastHours,
            onComplete = { feeling, note, weight ->
                viewModel.endFast(feeling, note, weight)
            },
            onCancelFast = { viewModel.cancelFast() },
            onDismiss = { viewModel.setFinishDialogVisible(false) }
        )
    }

    if (showWaterGoalDialog) {
        WaterGoalDialog(
            currentGoalMl = settings.dailyWaterGoalMl,
            onSaveGoal = { goal -> viewModel.updateWaterGoal(goal) },
            onDismiss = { viewModel.setWaterGoalDialogVisible(false) }
        )
    }
}
