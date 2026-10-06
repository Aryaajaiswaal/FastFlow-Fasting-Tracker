package com.example.model

import androidx.compose.ui.graphics.Color
import com.example.ui.theme.AutophagyCyan
import com.example.ui.theme.FastingAmber
import com.example.ui.theme.FastingOrange
import com.example.ui.theme.KetosisPurple
import com.example.ui.theme.RenewalEmerald
import com.example.ui.theme.TealAccent

data class FastingStage(
    val stageNumber: Int,
    val title: String,
    val startHours: Float,
    val endHours: Float,
    val color: Color,
    val headline: String,
    val description: String,
    val scientificBenefits: List<String>
) {
    fun isPassed(elapsedHours: Float): Boolean = elapsedHours >= endHours
    fun isActive(elapsedHours: Float): Boolean = elapsedHours >= startHours && elapsedHours < endHours
    fun progress(elapsedHours: Float): Float {
        if (elapsedHours < startHours) return 0f
        if (elapsedHours >= endHours) return 1f
        return (elapsedHours - startHours) / (endHours - startHours)
    }

    companion object {
        val ALL_STAGES = listOf(
            FastingStage(
                stageNumber = 1,
                title = "Digestion & Glucose Spike",
                startHours = 0f,
                endHours = 4f,
                color = FastingAmber,
                headline = "Digesting your last meal",
                description = "Nutrients are absorbed, blood glucose and insulin levels peak before steadily beginning their descent.",
                scientificBenefits = listOf(
                    "Nutrient delivery to tissues",
                    "Initial digestive breakdown",
                    "Glycogen replenishment in liver"
                )
            ),
            FastingStage(
                stageNumber = 2,
                title = "Blood Sugar Normalization",
                startHours = 4f,
                endHours = 8f,
                color = FastingOrange,
                headline = "Insulin levels drop",
                description = "Your digestive system rests. Blood sugar stabilizes, and the pancreas eases insulin production.",
                scientificBenefits = listOf(
                    "Resting gut microbiome",
                    "Stabilizing insulin sensitivity",
                    "Transitioning from external energy"
                )
            ),
            FastingStage(
                stageNumber = 3,
                title = "Glycogen Depletion",
                startHours = 8f,
                endHours = 12f,
                color = TealAccent,
                headline = "Burning stored liver carbs",
                description = "Stored liver glycogen is actively tapped for glucose. The body starts preparing to unlock stored fat.",
                scientificBenefits = listOf(
                    "Depletion of liver glycogen",
                    "Shift to cellular fatty acid oxidation",
                    "Decreased systemic bloating"
                )
            ),
            FastingStage(
                stageNumber = 4,
                title = "Fat Burning State",
                startHours = 12f,
                endHours = 16f,
                color = FastingOrange,
                headline = "Entering the metabolic switch",
                description = "Fat oxidation skyrockets! Adipose tissue breaks down fatty acids into energy, stimulating ketones.",
                scientificBenefits = listOf(
                    "Accelerated lipid burn",
                    "Human Growth Hormone (HGH) rises",
                    "Early ketone generation"
                )
            ),
            FastingStage(
                stageNumber = 5,
                title = "Ketosis & Mental Clarity",
                startHours = 16f,
                endHours = 20f,
                color = KetosisPurple,
                headline = "Ketone brain fuel online",
                description = "Beta-hydroxybutyrate (BHB) ketones power neurons. Cravings dissipate, replaced by calm mental sharpness.",
                scientificBenefits = listOf(
                    "Substantial ketone blood concentration",
                    "Enhanced neuroplasticity & BDNF",
                    "Significant reduction in inflammation"
                )
            ),
            FastingStage(
                stageNumber = 6,
                title = "Autophagy Activation",
                startHours = 20f,
                endHours = 24f,
                color = AutophagyCyan,
                headline = "Cellular self-cleaning",
                description = "Autophagy peaks: cells break down damaged proteins, misfolded components, and dysfunctional organelles.",
                scientificBenefits = listOf(
                    "Cellular self-renewal and cleanup",
                    "Mitochondrial biogenesis",
                    "Anti-aging cellular repair"
                )
            ),
            FastingStage(
                stageNumber = 7,
                title = "Immune Rejuvenation",
                startHours = 24f,
                endHours = 72f,
                color = RenewalEmerald,
                headline = "Deep cellular reset",
                description = "Prolonged fasting prompts white blood cell recycling and triggers hematopoietic stem cell regeneration.",
                scientificBenefits = listOf(
                    "Immune system rebuilding",
                    "Maximum insulin sensitivity reset",
                    "Profound metabolic flexibility"
                )
            )
        )
    }
}
