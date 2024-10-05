package technology.heli.helinote.core.domain.usecase

import technology.heli.helinote.core.domain.model.Note
import technology.heli.helinote.core.domain.model.Reminder
import technology.heli.helinote.core.domain.repository.ReminderRepository
import technology.heli.helinote.feature.notification.scheduler.ReminderScheduler
import javax.inject.Inject

class AddRemindersUseCase @Inject constructor(
    private val reminderRepository: ReminderRepository,
    private val reminderScheduler: ReminderScheduler,
) {

    suspend operator fun invoke(note: Note, remindersToAdd: List<Reminder>) {
        if (remindersToAdd.isEmpty()) {
            return
        }
        remindersToAdd.map { reminder ->
            reminder.copy(noteId = note.id)
        }.forEach { reminder ->
            var reminderId: Long? = null
            try {
                reminderId = reminderRepository.insertReminder(reminder)
                reminderScheduler.schedule(
                    reminder = reminder.copy(id = reminderId),
                    reminderTitle = note.title,
                    reminderMessage = note.content
                )
            } catch (exception: Exception) {
                // Rollback
                reminderId?.let {
                    reminderRepository.deleteRemindersById(listOf(it))
                }
                throw exception
            }
        }
    }
}