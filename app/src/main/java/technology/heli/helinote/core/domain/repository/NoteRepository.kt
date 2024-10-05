package technology.heli.helinote.core.domain.repository

import kotlinx.coroutines.flow.Flow
import technology.heli.helinote.core.domain.model.Note

interface NoteRepository {
    fun getNotes(): Flow<List<Note>>

    fun getNoteById(id: Long): Flow<Note?>

    suspend fun insertNote(note: Note): Long

    suspend fun deleteNoteById(id: Long)
}