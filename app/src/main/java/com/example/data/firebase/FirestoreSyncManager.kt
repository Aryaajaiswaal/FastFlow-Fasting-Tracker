package com.example.data.firebase

import android.content.Context
import android.util.Log
import com.example.R
import com.example.data.entity.FastingRecord
import com.example.data.entity.UserSettings
import com.example.data.entity.WaterLog
import com.google.firebase.Firebase
import com.google.firebase.auth.auth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.tasks.await

class FirestoreSyncManager(private val context: Context) {

    // Mandatory: Always resolve named database ID from R.string.firestore_database_id
    private val db: FirebaseFirestore by lazy {
        val databaseId = context.getString(R.string.firestore_database_id)
        FirebaseFirestore.getInstance(databaseId)
    }

    private fun requireUserId(): String {
        return Firebase.auth.currentUser?.uid
            ?: throw IllegalStateException("User must be signed in with Google before accessing Firestore.")
    }

    suspend fun syncFastingRecord(record: FastingRecord) {
        val uid = try {
            requireUserId()
        } catch (e: Exception) {
            return // Skip cloud sync when offline or unauthenticated
        }

        try {
            val docRef = db.collection("users")
                .document(uid)
                .collection("fasting_records")
                .document(record.id.toString())

            val data = hashMapOf(
                "userId" to uid,
                "recordId" to record.id,
                "startTimeMillis" to record.startTimeMillis,
                "endTimeMillis" to record.endTimeMillis,
                "targetDurationHours" to record.targetDurationHours,
                "planName" to record.planName,
                "status" to record.status,
                "feeling" to record.feeling,
                "note" to record.note,
                "weightKg" to record.weightKg,
                "updatedAt" to FieldValue.serverTimestamp()
            )

            docRef.set(data, SetOptions.merge()).await()
            Log.d("FirestoreSyncManager", "Synced fasting record ${record.id} to Firestore")
        } catch (e: Exception) {
            Log.w("FirestoreSyncManager", "Failed to sync fasting record: ${e.message}")
        }
    }

    suspend fun syncUserSettings(settings: UserSettings) {
        val uid = try {
            requireUserId()
        } catch (e: Exception) {
            return
        }

        try {
            val docRef = db.collection("users")
                .document(uid)
                .collection("settings")
                .document("user_settings")

            val data = hashMapOf(
                "userId" to uid,
                "selectedPlanName" to settings.selectedPlanName,
                "targetFastHours" to settings.targetFastHours,
                "eatingWindowHours" to settings.eatingWindowHours,
                "dailyWaterGoalMl" to settings.dailyWaterGoalMl,
                "updatedAt" to FieldValue.serverTimestamp()
            )

            docRef.set(data, SetOptions.merge()).await()
            Log.d("FirestoreSyncManager", "Synced user settings to Firestore")
        } catch (e: Exception) {
            Log.w("FirestoreSyncManager", "Failed to sync settings: ${e.message}")
        }
    }

    suspend fun deleteFastingRecord(recordId: Long) {
        val uid = try {
            requireUserId()
        } catch (e: Exception) {
            return
        }

        try {
            db.collection("users")
                .document(uid)
                .collection("fasting_records")
                .document(recordId.toString())
                .delete()
                .await()
        } catch (e: Exception) {
            Log.w("FirestoreSyncManager", "Failed to delete fasting record from Firestore: ${e.message}")
        }
    }
}
