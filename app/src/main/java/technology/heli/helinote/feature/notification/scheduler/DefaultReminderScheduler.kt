package technology.heli.helinote.feature.notification.scheduler

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import dagger.hilt.android.qualifiers.ApplicationContext
import technology.heli.helinote.core.domain.model.Reminder
import technology.heli.helinote.core.domain.model.RepeatType
import technology.heli.helinote.core.domain.model.exception.ExactAlarmPermissionRequiredException
import technology.heli.helinote.core.domain.model.exception.PostNotificationPermissionNotGrantedException
import technology.heli.helinote.feature.notification.NotificationHelper
import technology.heli.helinote.feature.notification.receiver.ReminderBroadcastReceiver
import javax.inject.Inject

class DefaultReminderScheduler @Inject constructor(
    @ApplicationContext private val context: Context,
    private val notificationHelper: NotificationHelper,
) : ReminderScheduler {
    companion object {
        const val EXTRA_REMINDER_ID = "EXTRA_REMINDER_ID"
        const val EXTRA_REMINDER_TITLE = "EXTRA_REMINDER_TITLE"
        const val EXTRA_REMINDER_MESSAGE = "EXTRA_REMINDER_MESSAGE"
    }

    override fun schedule(reminder: Reminder, reminderTitle: String, reminderMessage: String) {
        if (notificationHelper.areNotificationsEnabled().not()
            || notificationHelper.isPostNotificationPermissionGranted().not()
        ) {
            throw PostNotificationPermissionNotGrantedException("Notification permission is not granted.")
        }

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent().apply {
            setClassName(context.packageName, ReminderBroadcastReceiver::class.java.name)
            putExtra(EXTRA_REMINDER_ID, reminder.id)
            putExtra(EXTRA_REMINDER_TITLE, reminderTitle)
            putExtra(EXTRA_REMINDER_MESSAGE, reminderMessage)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            reminder.id.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        if (reminder.repeatType == RepeatType.NONE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S
                && !alarmManager.canScheduleExactAlarms()
            ) {
                throw ExactAlarmPermissionRequiredException("Exact alarm permission required.")
            }
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.RTC_WAKEUP,
                reminder.timestamp,
                pendingIntent
            )
        } else {
            // Repeatable reminder, using an appropriate interval
            val interval = when (reminder.repeatType) {
                RepeatType.DAILY -> AlarmManager.INTERVAL_DAY
                RepeatType.WEEKLY -> AlarmManager.INTERVAL_DAY * 7
                RepeatType.MONTHLY -> AlarmManager.INTERVAL_DAY * 30
                RepeatType.YEARLY -> AlarmManager.INTERVAL_DAY * 365
                else -> 0L
            }
            alarmManager.setRepeating(
                AlarmManager.RTC_WAKEUP, reminder.timestamp, interval, pendingIntent
            )
        }
    }

    override fun cancel(reminderId: Long) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent().apply {
            setClassName(context.packageName, ReminderBroadcastReceiver::class.java.name)
        }
        val pendingIntent = PendingIntent.getBroadcast(
            context,
            reminderId.toInt(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        alarmManager.cancel(pendingIntent)
    }
}