package technology.heli.helinote.feature.list

import technology.heli.helinote.core.domain.model.Note

data class NotesState(
    val notes: List<Note> = emptyList(),
    val cells: Int = 1
)