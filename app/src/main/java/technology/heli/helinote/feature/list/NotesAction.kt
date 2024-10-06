package technology.heli.helinote.feature.list

sealed class NotesAction {
    data object OnLayoutToggled : NotesAction()
}