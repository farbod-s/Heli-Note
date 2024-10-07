package technology.heli.helinote.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import technology.heli.helinote.core.database.dao.ReminderDao
import technology.heli.helinote.core.database.entity.ReminderEntity
import technology.heli.helinote.core.domain.mapper.DataMapper
import technology.heli.helinote.core.domain.model.Reminder
import technology.heli.helinote.core.domain.model.RepeatType
import technology.heli.helinote.core.domain.repository.ReminderRepository
import javax.inject.Inject

class DefaultReminderRepository @Inject constructor(
    private val reminderDao: ReminderDao,
    private val reminderMapper: DataMapper<ReminderEntity, Reminder>
) : ReminderRepository {

    override fun getReminders(): Flow<List<Reminder>> =
        reminderDao.getReminders().map { reminders -> reminders.map { reminderMapper.mapTo(it) } }

    override fun getRemindersByNoteId(noteId: Long): Flow<List<Reminder>> =
        reminderDao.getRemindersByNoteId(noteId)
            .map { reminders -> reminders.map { reminderMapper.mapTo(it) } }

    override suspend fun getPastReminders(timestamp: Long, type: RepeatType): List<Reminder> =
        reminderDao.getPastReminders(timestamp, type).map { reminderMapper.mapTo(it) }

    override suspend fun insertReminder(reminder: Reminder) =
        reminderDao.insertReminder(reminderMapper.mapFrom(reminder))

    override suspend fun deleteRemindersById(ids: List<Long>) =
        reminderDao.deleteRemindersById(ids)
}