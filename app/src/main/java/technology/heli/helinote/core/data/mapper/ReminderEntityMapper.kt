package technology.heli.helinote.core.data.mapper

import technology.heli.helinote.core.domain.model.Reminder
import technology.heli.helinote.core.database.entity.ReminderEntity
import technology.heli.helinote.core.domain.mapper.DataMapper

class ReminderEntityMapper : DataMapper<ReminderEntity, Reminder> {

    override fun mapTo(input: ReminderEntity) = Reminder(
        id = input.id,
        timestamp = input.timestamp,
        repeatType = input.repeatType,
        noteId = input.noteId,
    )

    override fun mapFrom(output: Reminder) = ReminderEntity(
        id = output.id,
        timestamp = output.timestamp,
        repeatType = output.repeatType,
        noteId = output.noteId,
    )
}