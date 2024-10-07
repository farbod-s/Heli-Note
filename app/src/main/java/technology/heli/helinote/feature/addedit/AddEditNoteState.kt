package technology.heli.helinote.feature.addedit

import technology.heli.helinote.core.domain.model.Reminder

data class AddEditNoteState(
    val title: String = "",
    val content: String = "",
    val isTitleHintVisible: Boolean = true,
    val isContentHintVisible: Boolean = true,
    val reminders: List<Reminder> = emptyList()
)