package technology.heli.helinote.core.domain.usecase

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import technology.heli.helinote.core.domain.model.Note
import technology.heli.helinote.core.domain.repository.NoteRepository
import technology.heli.helinote.core.domain.repository.ReminderRepository
import javax.inject.Inject

class GetNoteUseCase @Inject constructor(
    private val noteRepository: NoteRepository,
    private val reminderRepository: ReminderRepository,
) {

    operator fun invoke(noteId: Long): Flow<Note?> = noteRepository.getNoteById(noteId)
        .combine(reminderRepository.getRemindersByNoteId(noteId)) { note, reminders ->
            note?.copy(reminders = reminders)
        }
}