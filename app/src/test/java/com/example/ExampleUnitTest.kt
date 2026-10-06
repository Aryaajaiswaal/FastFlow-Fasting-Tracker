package com.example

import com.example.data.entity.FastingRecord
import com.example.data.entity.WaterLog
import com.example.model.FastingPlan
import com.example.model.FastingStage
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ExampleUnitTest {
    @Test
    fun fastingStage_progressCalculation_isCorrect() {
        val stage = FastingStage.ALL_STAGES[0] // 0 to 4 hours
        assertEquals(0f, stage.progress(0f), 0.01f)
        assertEquals(0.5f, stage.progress(2f), 0.01f)
        assertEquals(1f, stage.progress(4f), 0.01f)
        assertTrue(stage.isActive(2f))
        assertFalse(stage.isActive(5f))
        assertTrue(stage.isPassed(5f))
    }

    @Test
    fun fastingRecord_goalAchievement_isCorrect() {
        val record = FastingRecord(
            startTimeMillis = 1000L,
            endTimeMillis = 1000L + (16 * 3600 * 1000L),
            targetDurationHours = 16f,
            status = "COMPLETED"
        )
        assertTrue(record.isGoalAchieved)
        assertEquals(16f, record.durationHours, 0.01f)
    }

    @Test
    fun fastingPlan_presets_areAvailable() {
        assertTrue(FastingPlan.PRESETS.isNotEmpty())
        val defaultPlan = FastingPlan.PRESETS.first()
        assertEquals("16:8 LeanGains", defaultPlan.name)
        assertEquals(16f, defaultPlan.fastHours, 0.01f)
    }

    @Test
    fun waterLog_entity_creationAndSum() {
        val log1 = WaterLog(amountMl = 250, dateKey = "2026-10-06")
        val log2 = WaterLog(amountMl = 500, dateKey = "2026-10-06")
        assertEquals(250, log1.amountMl)
        assertEquals(500, log2.amountMl)
        assertEquals(750, log1.amountMl + log2.amountMl)
        assertEquals("2026-10-06", log1.dateKey)
    }
}
