package technology.heli.helinote.core.domain.usecase

import technology.heli.helinote.core.domain.model.Note
import technology.heli.helinote.core.domain.model.Reminder
import technology.heli.helinote.core.domain.model.exception.InvalidNoteException
import technology.heli.helinote.core.domain.repository.NoteRepository
import javax.inject.Inject

class UpdateNoteUseCase @Inject constructor(
    private val noteRepository: NoteRepository,
    private val addReminderUseCase: AddRemindersUseCase,
    private val removeReminderUseCase: RemoveRemindersUseCase,
) {

    suspend operator fun invoke(
        note: Note,
        remindersToAdd: List<Reminder>,
        remindersToRemove: List<Reminder>
    ) {
        if (note.title.isBlank()) {
            throw InvalidNoteException("The title can't be empty.")
        }

        if (note.content.isBlank()) {
            throw InvalidNoteException("The content can't be empty.")
        }

        var isNewNote = false
        var noteId: Long? = null
        try {
            isNewNote = noteRepository.isNoteExists(note.id).not()
            if (isNewNote) {
                noteId = noteRepository.insertNote(note)
            } else {
                noteRepository.updateNote(note)
                noteId = note.id
            }
            addReminderUseCase(note = note.copy(id = noteId), remindersToAdd = remindersToAdd)
            removeReminderUseCase(remindersToRemove = remindersToRemove)
        } catch (exception: Exception) {
            // Rollback
            noteId?.let {
                if (isNewNote) {
                    noteRepository.deleteNoteById(it)
                }
            }
            throw exception
        }
    }
}