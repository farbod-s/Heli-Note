package technology.heli.helinote.core.data.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.map
import technology.heli.helinote.core.database.dao.NoteDao
import technology.heli.helinote.core.database.entity.NoteEntity
import technology.heli.helinote.core.domain.mapper.DataMapper
import technology.heli.helinote.core.domain.model.Note
import technology.heli.helinote.core.domain.repository.NoteRepository
import javax.inject.Inject

class DefaultNoteRepository @Inject constructor(
    private val noteDao: NoteDao,
    private val noteMapper: DataMapper<NoteEntity, Note>
) : NoteRepository {

    override fun getNotes(query: String?): Flow<List<Note>> =
        noteDao.getNotes(query).map { notes -> notes.map { noteMapper.mapTo(it) } }

    override fun getNoteById(id: Long): Flow<Note?> =
        noteDao.getNoteById(id).map { note -> note?.let { noteMapper.mapTo(it) } }

    override suspend fun isNoteExists(id: Long): Boolean =
        noteDao.getNoteById(id).firstOrNull()?.let { true } ?: false

    override suspend fun insertNote(note: Note) =
        noteDao.insertNote(noteMapper.mapFrom(note))

    override suspend fun updateNote(note: Note) =
        noteDao.updateNote(noteMapper.mapFrom(note))

    override suspend fun deleteNoteById(id: Long) =
        noteDao.deleteNoteById(id)
}