package com.example.model

data class FastingPlan(
    val name: String,
    val fastHours: Float,
    val eatHours: Float,
    val tag: String,
    val description: String,
    val difficulty: String
) {
    val displayRatio: String
        get() = "${fastHours.toInt()}:${eatHours.toInt()}"

    companion object {
        val PRESETS = listOf(
            FastingPlan(
                name = "16:8 LeanGains",
                fastHours = 16f,
                eatHours = 8f,
                tag = "Most Popular",
                description = "The gold standard of intermittent fasting. Ideal balance of fat oxidation and lifestyle flexibility.",
                difficulty = "Beginner - Intermediate"
            ),
            FastingPlan(
                name = "14:10 Gentle Reset",
                fastHours = 14f,
                eatHours = 10f,
                tag = "Easy Start",
                description = "Perfect introduction for beginners aligning with circadian sleep rhythms without stress.",
                difficulty = "Beginner"
            ),
            FastingPlan(
                name = "18:6 Keto Booster",
                fastHours = 18f,
                eatHours = 6f,
                tag = "Fat Burn",
                description = "Extends ketosis duration and provides enhanced mental focus and mitochondrial efficiency.",
                difficulty = "Intermediate"
            ),
            FastingPlan(
                name = "20:4 Warrior Fast",
                fastHours = 20f,
                eatHours = 4f,
                tag = "Advanced",
                description = "Four-hour eating window. Maximizes fat burning and initiates cellular autophagy early.",
                difficulty = "Advanced"
            ),
            FastingPlan(
                name = "24:0 OMAD",
                fastHours = 24f,
                eatHours = 1f,
                tag = "One Meal A Day",
                description = "Single daily feast. Triggers potent autophagy, insulin sensitivity, and immune cleanup.",
                difficulty = "Expert"
            ),
            FastingPlan(
                name = "36:0 Monk Fast",
                fastHours = 36f,
                eatHours = 0f,
                tag = "Extended Reset",
                description = "Full 36-hour reset. Profound cellular renewal and metabolic rejuvenation.",
                difficulty = "Expert"
            )
        )
    }
}
