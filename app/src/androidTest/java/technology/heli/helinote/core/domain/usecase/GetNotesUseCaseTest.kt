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

class GetNotesUseCaseTest {

    private lateinit var noteRepository: NoteRepository
    private lateinit var reminderRepository: ReminderRepository
    private lateinit var getNotesUseCase: GetNotesUseCase

    @Before
    fun setUp() {
        noteRepository = mock()
        reminderRepository = mock()
        getNotesUseCase = GetNotesUseCase(noteRepository, reminderRepository)
    }

    @Test
    fun should_return_notes_with_associated_reminders() = runTest {
        val note = Note()
        val reminder = Reminder(noteId = note.id)

        whenever(noteRepository.getNotes()).thenReturn(flowOf(listOf(note)))
        whenever(reminderRepository.getReminders()).thenReturn(flowOf(listOf(reminder)))

        val result = getNotesUseCase().first()

        assert(result.first().reminders.contains(reminder))
    }

    @Test
    fun should_return_empty_reminders_when_no_associated_reminders_found() = runTest {
        val note = Note()

        whenever(noteRepository.getNotes()).thenReturn(flowOf(listOf(note)))
        whenever(reminderRepository.getReminders()).thenReturn(flowOf(emptyList()))

        val result = getNotesUseCase().first()

        assert(result.first().reminders.isEmpty())
    }
}