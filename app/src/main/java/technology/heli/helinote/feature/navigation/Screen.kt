package technology.heli.helinote.feature.navigation

sealed class Screen(val route: String) {
    data object NoteListScreen : Screen("note_list")
    data object NoteAddEditScreen : Screen("add_edit_note/{noteId}") {
        fun createRoute(noteId: Long? = null) = "add_edit_note/${noteId ?: 0L}"
    }
}