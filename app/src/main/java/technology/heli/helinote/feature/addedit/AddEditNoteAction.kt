package technology.heli.helinote.feature.addedit

import technology.heli.helinote.core.domain.model.RepeatType

sealed class AddEditNoteAction {
    data class OnTitleChanged(val newValue: String) : AddEditNoteAction()
    data class OnTitleFocusChanged(val focused: Boolean) : AddEditNoteAction()
    data class OnContentChanged(val newValue: String) : AddEditNoteAction()
    data class OnContentFocusChanged(val focused: Boolean) : AddEditNoteAction()
    data class OnReminderAdded(val date: Long, val time: Long, val repeatType: RepeatType) :
        AddEditNoteAction()

    data class OnReminderRemoved(val reminderId: Long) : AddEditNoteAction()
    data object OnSaveClicked : AddEditNoteAction()
    data object OnNotificationPermissionRejected : AddEditNoteAction()
    data object OnNotificationPermissionAccepted : AddEditNoteAction()
}