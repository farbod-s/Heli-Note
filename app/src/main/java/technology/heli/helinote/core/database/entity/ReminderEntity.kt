package technology.heli.helinote.core.database.entity

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import technology.heli.helinote.core.domain.model.RepeatType

@Entity(
    tableName = "reminder_table",
    foreignKeys = [ForeignKey(
        entity = NoteEntity::class,
        parentColumns = ["id"],
        childColumns = ["noteId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["noteId"])]
)
data class ReminderEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val timestamp: Long,
    val repeatType: RepeatType = RepeatType.NONE,
    val noteId: Long
)