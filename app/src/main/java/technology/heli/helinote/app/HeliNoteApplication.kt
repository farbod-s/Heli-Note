package technology.heli.helinote.app

import android.app.Application
import android.content.Context
import androidx.hilt.work.HiltWorkerFactory
import androidx.work.Configuration
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequest
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.HiltAndroidApp
import technology.heli.helinote.feature.notification.worker.RemovePastRemindersWorker
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltAndroidApp
class HeliNoteApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var workerFactory: HiltWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(workerFactory)
            .build()

    override fun onCreate() {
        super.onCreate()

        schedulePeriodicWork(this)
    }

    private fun schedulePeriodicWork(context: Context) {
        val workRequest: PeriodicWorkRequest =
            PeriodicWorkRequestBuilder<RemovePastRemindersWorker>(1, TimeUnit.DAYS)
                .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "RemovePastRemindersWorker",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }
}