package technology.heli.helinote.feature.notification.scheduler

import technology.heli.helinote.core.domain.model.Reminder

interface ReminderScheduler {
    fun schedule(reminder: Reminder, reminderTitle: String, reminderMessage: String)

    fun cancel(reminderId: Long)
}