package technology.heli.helinote.core.domain.repository

import kotlinx.coroutines.flow.Flow
import technology.heli.helinote.core.domain.model.Note

interface NoteRepository {
    fun getNotes(query: String? = null): Flow<List<Note>>

    fun getNoteById(id: Long): Flow<Note?>

    suspend fun isNoteExists(id: Long): Boolean

    suspend fun insertNote(note: Note): Long

    suspend fun updateNote(note: Note)

    suspend fun deleteNoteById(id: Long)
}