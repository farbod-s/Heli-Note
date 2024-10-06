package technology.heli.helinote.core.domain.usecase

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import technology.heli.helinote.core.domain.model.Note
import technology.heli.helinote.core.domain.model.Reminder
import technology.heli.helinote.core.domain.model.exception.InvalidNoteException
import technology.heli.helinote.core.domain.repository.NoteRepository

@ExperimentalCoroutinesApi
class UpdateNoteUseCaseTest {

    private lateinit var updateNoteUseCase: UpdateNoteUseCase
    private lateinit var noteRepository: NoteRepository
    private lateinit var addReminderUseCase: AddRemindersUseCase
    private lateinit var removeReminderUseCase: RemoveRemindersUseCase

    @Before
    fun setUp() {
        noteRepository = mock()
        addReminderUseCase = mock()
        removeReminderUseCase = mock()

        updateNoteUseCase = UpdateNoteUseCase(
            noteRepository = noteRepository,
            addReminderUseCase = addReminderUseCase,
            removeReminderUseCase = removeReminderUseCase
        )
    }

    @Test(expected = InvalidNoteException::class)
    fun should_throw_exception_if_note_title_is_empty() = runTest {
        val note = Note(title = "", content = "Some content")

        updateNoteUseCase(note, remindersToAdd = emptyList(), remindersToRemove = emptyList())
    }

    @Test(expected = InvalidNoteException::class)
    fun should_throw_exception_if_note_content_is_empty() = runTest {
        val note = Note(title = "Title", content = "")

        updateNoteUseCase(note, remindersToAdd = emptyList(), remindersToRemove = emptyList())
    }

    @Test
    fun should_insert_new_note_and_add_reminders_when_note_does_not_exist() = runTest {
        val note = Note(id = 1L, title = "Note Title", content = "Some content")
        val remindersToAdd = listOf(Reminder(timestamp = 123L))

        whenever(noteRepository.isNoteExists(note.id)).thenReturn(false)
        whenever(noteRepository.insertNote(note)).thenReturn(note.id)

        updateNoteUseCase(note, remindersToAdd = remindersToAdd, remindersToRemove = emptyList())

        verify(noteRepository).insertNote(note)
        verify(addReminderUseCase).invoke(note.copy(id = note.id), remindersToAdd)
        verify(removeReminderUseCase).invoke(emptyList())
    }

    @Test
    fun should_update_existing_note_and_add_reminders() = runTest {
        val note = Note(id = 1L, title = "Note Title", content = "Some content")
        val remindersToAdd = listOf(Reminder(timestamp = 123L))

        whenever(noteRepository.isNoteExists(note.id)).thenReturn(true)

        updateNoteUseCase(note, remindersToAdd = remindersToAdd, remindersToRemove = emptyList())

        verify(noteRepository).updateNote(note)
        verify(addReminderUseCase).invoke(note, remindersToAdd)
        verify(removeReminderUseCase).invoke(emptyList())
    }

    @Test
    fun should_remove_reminders_when_updating_note() = runTest {
        val note = Note(id = 1L, title = "Note Title", content = "Some content")
        val remindersToRemove = listOf(Reminder(id = 2L, timestamp = 123L))

        whenever(noteRepository.isNoteExists(note.id)).thenReturn(true)

        updateNoteUseCase(note, remindersToAdd = emptyList(), remindersToRemove = remindersToRemove)

        verify(removeReminderUseCase).invoke(remindersToRemove)
    }

    @Test
    fun should_rollback_note_creation_if_adding_reminders_fails() = runTest {
        val note = Note(id = 1L, title = "Note Title", content = "Some content")
        val remindersToAdd = listOf(Reminder(timestamp = 123L))

        whenever(noteRepository.isNoteExists(note.id)).thenReturn(false)
        whenever(noteRepository.insertNote(note)).thenReturn(note.id)
        whenever(addReminderUseCase.invoke(note.copy(id = note.id), remindersToAdd))
            .thenThrow(RuntimeException())

        try {
            updateNoteUseCase(
                note,
                remindersToAdd = remindersToAdd,
                remindersToRemove = emptyList()
            )
        } catch (e: Exception) {
            // Expected
        }

        verify(noteRepository).deleteNoteById(note.id)
    }

    @Test
    fun should_not_rollback_if_note_was_already_existing_and_adding_reminders_fails() = runTest {
        val note = Note(id = 1L, title = "Note Title", content = "Some content")
        val remindersToAdd = listOf(Reminder(timestamp = 123L))

        whenever(noteRepository.isNoteExists(note.id)).thenReturn(true)
        whenever(addReminderUseCase.invoke(note, remindersToAdd))
            .thenThrow(RuntimeException())

        try {
            updateNoteUseCase(
                note,
                remindersToAdd = remindersToAdd,
                remindersToRemove = emptyList()
            )
        } catch (e: Exception) {
            // Expected
        }

        verify(noteRepository, never()).deleteNoteById(note.id)
    }
}