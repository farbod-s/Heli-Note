package technology.heli.helinote.core.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import technology.heli.helinote.core.domain.model.Note
import technology.heli.helinote.core.domain.repository.NoteRepository
import technology.heli.helinote.core.domain.repository.ReminderRepository
import javax.inject.Inject

class GetNotesUseCase @Inject constructor(
    private val noteRepository: NoteRepository,
    private val reminderRepository: ReminderRepository,
) {

    operator fun invoke(): Flow<List<Note>> = noteRepository.getNotes()
        .combine(reminderRepository.getReminders()) { notes, reminders ->
            notes.map { note ->
                val associatedReminders = reminders.filter { it.noteId == note.id }
                note.copy(reminders = associatedReminders)
            }
        }
}