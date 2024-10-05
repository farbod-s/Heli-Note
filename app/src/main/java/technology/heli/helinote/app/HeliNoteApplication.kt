package technology.heli.helinote.app

import android.app.Application
import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.HiltAndroidApp
import technology.heli.helinote.feature.notification.worker.RemovePastRemindersWorker
import java.util.concurrent.TimeUnit

@HiltAndroidApp
class HeliNoteApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        schedulePeriodicWork(this)
    }

    private fun schedulePeriodicWork(context: Context) {
        val workRequest: PeriodicWorkRequest =
            PeriodicWorkRequestBuilder<RemovePastRemindersWorker>(1, TimeUnit.DAYS)
                .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "MyPeriodicWork",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }
}