package technology.heli.helinote.core.domain.model

data class Note(
    val id: Long = 0,
    val title: String = "",
    val content: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val reminders: List<Reminder> = emptyList()
)