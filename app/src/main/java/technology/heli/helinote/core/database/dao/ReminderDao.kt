package technology.heli.helinote.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import technology.heli.helinote.core.database.entity.ReminderEntity
import technology.heli.helinote.core.domain.model.RepeatType

@Dao
interface ReminderDao {

    @Query("SELECT * FROM reminder_table ORDER BY timestamp ASC")
    fun getReminders(): Flow<List<ReminderEntity>>

    @Query("SELECT * FROM reminder_table WHERE noteId = :noteId ORDER BY timestamp ASC")
    fun getRemindersByNoteId(noteId: Long): Flow<List<ReminderEntity>>

    @Query("SELECT * FROM reminder_table WHERE timestamp < :timestamp AND repeatType = :repeatType ORDER BY timestamp ASC")
    suspend fun getPastReminders(timestamp: Long, repeatType: RepeatType): List<ReminderEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReminder(reminder: ReminderEntity): Long

    @Query("DELETE FROM reminder_table WHERE id IN (:ids)")
    suspend fun deleteRemindersById(ids: List<Long>)
}