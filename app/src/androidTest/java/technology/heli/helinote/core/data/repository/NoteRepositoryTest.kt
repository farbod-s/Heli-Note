package technology.heli.helinote.core.data.repository

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify
import org.mockito.kotlin.whenever
import technology.heli.helinote.core.database.dao.NoteDao
import technology.heli.helinote.core.database.entity.NoteEntity
import technology.heli.helinote.core.domain.mapper.DataMapper
import technology.heli.helinote.core.domain.model.Note
import technology.heli.helinote.core.domain.repository.NoteRepository

class NoteRepositoryTest {

    private lateinit var noteDao: NoteDao
    private lateinit var noteRepository: NoteRepository
    private lateinit var noteMapper: DataMapper<NoteEntity, Note>

    @Before
    fun setUp() {
        noteDao = mock()
        noteMapper = mock()
        noteRepository = DefaultNoteRepository(noteDao, noteMapper)
    }

    @Test
    fun testGetNotes() = runBlocking {
        val noteEntities = listOf(NoteEntity(1, "Title", "Content", 123L))
        whenever(noteDao.getNotes()).thenReturn(flowOf(noteEntities))

        val notes = noteRepository.getNotes().first()
        verify(noteDao).getNotes()
        assertEquals(1, notes.size)
    }

    @Test
    fun testInsertNote(): Unit = runBlocking {
        val note = Note(0, "New Note", "New Content", System.currentTimeMillis())
        val noteEntity = NoteEntity(0, "New Note", "New Content", System.currentTimeMillis())
        whenever(noteMapper.mapFrom(note)).thenReturn(noteEntity)

        noteRepository.insertNote(note)
        verify(noteDao).insertNote(noteEntity)
    }
}
