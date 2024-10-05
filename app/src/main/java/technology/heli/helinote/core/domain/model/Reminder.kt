package technology.heli.helinote.core.domain.model

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class Reminder(
    val id: Long = 0,
    val timestamp: Long = 0,
    val repeatType: RepeatType = RepeatType.NONE,
    val noteId: Long = 0
) {

    fun formatAsReadableDateTime(): String {
        val formatter = SimpleDateFormat("MMMM dd, yyyy, HH:mm", Locale.getDefault())
        val reminderDate = Date(this.timestamp)
        return formatter.format(reminderDate)
    }
}