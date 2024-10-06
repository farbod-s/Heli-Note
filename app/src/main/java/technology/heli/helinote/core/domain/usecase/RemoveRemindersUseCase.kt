package technology.heli.helinote.core.domain.usecase

import technology.heli.helinote.core.domain.model.Reminder
import technology.heli.helinote.core.domain.repository.ReminderRepository
import technology.heli.helinote.feature.notification.scheduler.ReminderScheduler
import javax.inject.Inject

open class RemoveRemindersUseCase @Inject constructor(
    private val reminderRepository: ReminderRepository,
    private val reminderScheduler: ReminderScheduler,
) {

    suspend operator fun invoke(remindersToRemove: List<Reminder>) {
        if (remindersToRemove.isEmpty()) {
            return
        }
        reminderRepository.deleteRemindersById(
            remindersToRemove.map { reminder ->
                reminderScheduler.cancel(reminder.id)
                reminder.id
            }
        )
    }
}