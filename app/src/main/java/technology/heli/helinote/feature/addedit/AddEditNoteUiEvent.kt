package technology.heli.helinote.feature.addedit

sealed class AddEditNoteUiEvent {
    data object Idle : AddEditNoteUiEvent()
    data class Error(val message: String, val action: UiEventAction? = null) : AddEditNoteUiEvent()
    data object NoteSaved : AddEditNoteUiEvent()
}

sealed class UiEventAction(val label: String) {
    data object OpenExactAlarmPermission : UiEventAction("Open Setting")
    data object ShowPostNotificationPermission : UiEventAction("Grant Permission")
}