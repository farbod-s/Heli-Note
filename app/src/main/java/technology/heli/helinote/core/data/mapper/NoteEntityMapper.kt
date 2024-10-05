package technology.heli.helinote.core.data.mapper

import technology.heli.helinote.core.domain.model.Note
import technology.heli.helinote.core.database.entity.NoteEntity
import technology.heli.helinote.core.domain.mapper.DataMapper

class NoteEntityMapper : DataMapper<NoteEntity, Note> {

    override fun mapTo(input: NoteEntity) = Note(
        id = input.id,
        title = input.title,
        content = input.content,
        timestamp = input.timestamp,
        reminders = emptyList(),
    )

    override fun mapFrom(output: Note) = NoteEntity(
        id = output.id,
        title = output.title,
        content = output.content,
        timestamp = output.timestamp,
    )
}