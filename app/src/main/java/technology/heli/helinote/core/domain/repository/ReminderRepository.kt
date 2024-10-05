package technology.heli.helinote.core.domain.repository

import kotlinx.coroutines.flow.Flow
import technology.heli.helinote.core.domain.model.Reminder

interface ReminderRepository {
    fun getReminders(): Flow<List<Reminder>>

    fun getRemindersByNoteId(noteId: Long): Flow<List<Reminder>>

    suspend fun getPastReminders(timestamp: Long): List<Reminder>

    suspend fun insertReminder(reminder: Reminder): Long

    suspend fun deleteRemindersById(ids: List<Long>)
}