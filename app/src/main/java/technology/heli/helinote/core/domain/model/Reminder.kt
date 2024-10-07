package technology.heli.helinote.core.domain.model

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

data class Reminder(
    val id: Long = 0,
    val timestamp: Long = 0,
    val repeatType: RepeatType = RepeatType.NONE,
    val noteId: Long = 0
) {

    fun formatAsReadableDateTime(): String {
        val now = Calendar.getInstance()
        val targetDate = Calendar.getInstance().apply { timeInMillis = timestamp }

        val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
        val time = timeFormat.format(timestamp)

        val sameDay = now[Calendar.DAY_OF_YEAR] == targetDate[Calendar.DAY_OF_YEAR] &&
                now[Calendar.YEAR] == targetDate[Calendar.YEAR]
        val tomorrow = now[Calendar.DAY_OF_YEAR] + 1 == targetDate[Calendar.DAY_OF_YEAR] &&
                now[Calendar.YEAR] == targetDate[Calendar.YEAR]
        val nextWeek =
            targetDate[Calendar.WEEK_OF_YEAR] == now[Calendar.WEEK_OF_YEAR] + 1 &&
                    now[Calendar.YEAR] == targetDate[Calendar.YEAR]
        val nextMonth = now[Calendar.MONTH] + 1 == targetDate[Calendar.MONTH] &&
                now[Calendar.YEAR] == targetDate[Calendar.YEAR]
        val nextYear = now[Calendar.YEAR] + 1 == targetDate[Calendar.YEAR]

        return when {
            sameDay -> "today $time"
            tomorrow -> "tomorrow $time"
            nextWeek -> "next week $time"
            nextMonth -> "next month $time"
            nextYear -> "next year $time"
            else -> SimpleDateFormat("MMM d", Locale.getDefault()).format(timestamp) + " $time"
        }
    }
}