package technology.heli.helinote.core.domain.usecase

import technology.heli.helinote.core.domain.repository.ReminderRepository
import javax.inject.Inject

class RemovePastRemindersUseCase @Inject constructor(
    private val reminderRepository: ReminderRepository,
    private val removeReminderUseCase: RemoveRemindersUseCase,
) {

    suspend operator fun invoke(now: Long = System.currentTimeMillis()) {
        val remindersToRemove = reminderRepository.getPastReminders(timestamp = now)
        removeReminderUseCase(remindersToRemove = remindersToRemove)
    }
}