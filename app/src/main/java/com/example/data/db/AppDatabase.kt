package com.example.data.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.dao.FastingDao
import com.example.data.dao.SettingsDao
import com.example.data.dao.WaterDao
import com.example.data.entity.FastingRecord
import com.example.data.entity.UserSettings
import com.example.data.entity.WaterLog

@Database(
    entities = [FastingRecord::class, WaterLog::class, UserSettings::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun fastingDao(): FastingDao
    abstract fun waterDao(): WaterDao
    abstract fun settingsDao(): SettingsDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "fastflow_database"
                ).fallbackToDestructiveMigration().build()
                INSTANCE = instance
                instance
            }
        }
    }
}
