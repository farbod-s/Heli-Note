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

        // Check if the reminder time has passed for today
        val currentTimePassed = now.after(targetDate)

        // Adjust the target date based on repeat type if the time has passed
        if (currentTimePassed) {
            when (repeatType) {
                RepeatType.DAILY -> targetDate.add(Calendar.DAY_OF_YEAR, 1)
                RepeatType.WEEKLY -> targetDate.add(Calendar.WEEK_OF_YEAR, 1)
                RepeatType.MONTHLY -> targetDate.add(Calendar.MONTH, 1)
                RepeatType.YEARLY -> targetDate.add(Calendar.YEAR, 1)
                RepeatType.NONE -> {}
            }
        }

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
            sameDay -> "Today $time"
            tomorrow -> "Tomorrow $time"
            nextWeek -> "Next week $time"
            nextMonth -> "Next month $time"
            nextYear -> "Next year $time"
            else -> SimpleDateFormat("MMM d, yyyy", Locale.getDefault()).format(timestamp) + " $time"
        }
    }
}