# FastFlow — Intermittent Fasting Timer & Metabolic Tracker

FastFlow is a modern, privacy-first intermittent fasting and metabolic wellness tracker built with **Kotlin** and **Jetpack Compose (Material 3)**. It helps you track your fasts, monitor your body's metabolic milestones (from fat burn to deep autophagy), log daily hydration, and stay consistent with streak analytics.

---

## Features

### ⏱️ Adaptive Fasting Timer
- **Live Circular Progress Arc**: Real-time second-by-second countdown with goal percentage and overtime tracking.
- **Popular Fasting Protocols**:
  - **16:8 LeanGains** — The gold standard for fat oxidation and daily flexibility.
  - **14:10 Gentle Reset** — Beginner-friendly circadian alignment.
  - **18:6 Keto Booster** — Extended ketosis and enhanced mental clarity.
  - **20:4 Warrior Fast** — 4-hour eating window with early autophagy activation.
  - **24:0 OMAD (One Meal A Day)** — Potent cellular cleanup and insulin reset.
  - **36:0 Monk Fast** — Multi-day metabolic rejuvenation.
  - **Custom Duration** — Set any fast target between 1 and 72 hours.
- **Flexible Start Time Editing**: Started fasting earlier or forgot to press start after dinner? Adjust your start time with a single tap.

### 🔬 7 Science-Backed Metabolic Stages
Track physiological transitions throughout your fast:
1. **Digestion & Glucose Spike (0–4h)** — Nutrient absorption and digestive conversion.
2. **Blood Sugar Normalization (4–8h)** — Digestive rest and insulin stabilization.
3. **Glycogen Depletion (8–12h)** — Liver glycogen burnout preparing for fat oxidation.
4. **Fat Burning State (12–16h)** — Shift to lipid metabolism and growth hormone elevation.
5. **Ketosis & Mental Clarity (16–20h)** — Beta-hydroxybutyrate (BHB) ketones fueling brain cells.
6. **Autophagy Peak (20–24h)** — Cellular cleanup, protein recycling, and mitochondrial renewal.
7. **Immune Rejuvenation (24h+)** — Stem cell activation and reduction in systemic inflammation markers.

### 💧 Hydration Tracker
- Quick-add buttons for **+250 ml** (glass) and **+500 ml** (bottle).
- Real-time progress bar toward your daily hydration goal.
- Undo button to correct accidental logs.
- Configurable daily water targets.

### 📜 History & Feeling Logs
- When ending a fast, log how you feel (**⚡ Energetic**, **🌟 Great**, **😊 Good**, **😴 Tired**, **🤤 Hungry**).
- Optional body weight check-in (kg) and personal reflection notes.
- Detailed historical session logs with dates, durations, and goal attainment badges.

### 📊 Analytics & Consistency Streaks
- **Day Streaks**: Visual flame counter tracking consecutive daily fasts.
- **Key Metrics**: Total fasts, total fasting hours, average fast duration, and longest fast record.
- **7-Day Bar Chart**: Visual comparison of daily hours fasted against your fasting goal.
- **Achievement Milestones**: Earn badges as you hit major fasting milestones.

### 📚 Science & Educational Guide
- Comprehensive reference on **what breaks a fast** (clean fasting vs. insulin triggers).
- Gentle refeeding protocols to avoid digestive discomfort.
- Electrolyte guide to prevent the "fasting flu" and headaches.
- Safety disclaimers and fasting best practices.

---

## Tech Stack & Architecture

- **Language**: Kotlin
- **UI Toolkit**: Jetpack Compose (Material 3)
- **Design System**: Centralized M3 theming (`Theme.kt`, `Color.kt`, `Type.kt`) with dynamic color support and dark/light modes
- **Architecture**: MVVM (Model-View-ViewModel) with Kotlin Coroutines and StateFlow
- **Local Persistence**: Android Room Database with KSP (`FastingRecord`, `WaterLog`, `UserSettings`)
- **Testing**: JUnit 4 and Robolectric JVM test coverage
- **Adaptive Icon**: Custom adaptive launcher icon with vector foreground layer and density-specific mipmaps

---

## Project Structure

```
├── app
│   ├── src
│   │   ├── main
│   │   │   ├── AndroidManifest.xml
│   │   │   ├── java/com/example
│   │   │   │   ├── MainActivity.kt
│   │   │   │   ├── data
│   │   │   │   │   ├── dao (FastingDao, WaterDao, SettingsDao)
│   │   │   │   │   ├── db (AppDatabase)
│   │   │   │   │   ├── entity (FastingRecord, WaterLog, UserSettings)
│   │   │   │   │   └── repository (FastingRepository)
│   │   │   │   ├── model (FastingPlan, FastingStage)
│   │   │   │   └── ui
│   │   │   │       ├── FastFlowViewModel.kt
│   │   │   │       ├── MainAppScaffold.kt
│   │   │   │       ├── components (TimerCircle, WaterHydrationCard, FastingDialogs)
│   │   │   │       ├── screens (TimerScreen, StagesScreen, HistoryScreen, StatsScreen, LearnScreen)
│   │   │   │       └── theme (Color, Theme, Type)
│   │   │   └── res
│   │   │       ├── drawable (ic_fastflow_logo, ic_fasting_hero, launcher drawables)
│   │   │       └── values (strings, colors, themes)
│   │   └── test/java/com/example
│   │       ├── ExampleUnitTest.kt
│   │       └── ExampleRobolectricTest.kt
│   └── build.gradle.kts
├── metadata.json
└── settings.gradle.kts
```

---

## Build & Test

To build the project and execute local unit tests:

```bash
# Run unit and Robolectric tests
gradle :app:testDebugUnitTest

# Assemble debug APK
gradle :app:assembleDebug
```
