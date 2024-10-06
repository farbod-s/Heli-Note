package technology.heli.helinote.core.domain.usecase

import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.whenever
import technology.heli.helinote.core.domain.model.Note
import technology.heli.helinote.core.domain.model.Reminder
import technology.heli.helinote.core.domain.repository.NoteRepository
import technology.heli.helinote.core.domain.repository.ReminderRepository

class GetNoteUseCaseTest {

    private lateinit var noteRepository: NoteRepository
    private lateinit var reminderRepository: ReminderRepository
    private lateinit var getNoteUseCase: GetNoteUseCase

    @Before
    fun setUp() {
        noteRepository = mock()
        reminderRepository = mock()
        getNoteUseCase = GetNoteUseCase(noteRepository, reminderRepository)
    }

    @Test
    fun should_return_note_with_reminders() = runTest {
        val noteId = 1L
        val note = Note(id = noteId)
        val reminder = Reminder(noteId = noteId)

        whenever(noteRepository.getNoteById(noteId)).thenReturn(flowOf(note))
        whenever(reminderRepository.getRemindersByNoteId(noteId)).thenReturn(flowOf(listOf(reminder)))

        val result = getNoteUseCase(noteId).first()

        assert(result?.reminders?.contains(reminder) == true)
    }

    @Test
    fun should_return_null_when_note_is_not_found() = runTest {
        val noteId = 1L

        whenever(noteRepository.getNoteById(noteId)).thenReturn(flowOf(null))
        whenever(reminderRepository.getRemindersByNoteId(noteId)).thenReturn(flowOf(emptyList()))

        val result = getNoteUseCase(noteId).first()

        assert(result == null)
    }
}