package technology.heli.helinote.feature.notification.worker

import android.content.Context
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import technology.heli.helinote.core.domain.usecase.RemovePastRemindersUseCase

@HiltWorker
class RemovePastRemindersWorker @AssistedInject constructor(
    @Assisted appContext: Context,
    @Assisted workerParams: WorkerParameters,
    private val removePastRemindersUseCase: RemovePastRemindersUseCase
) : CoroutineWorker(appContext, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            removePastRemindersUseCase()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}