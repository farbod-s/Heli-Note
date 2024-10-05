package technology.heli.helinote.feature.notification.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import dagger.hilt.android.AndroidEntryPoint
import technology.heli.helinote.feature.notification.NotificationHelper
import technology.heli.helinote.feature.notification.scheduler.DefaultReminderScheduler.Companion.EXTRA_REMINDER_ID
import technology.heli.helinote.feature.notification.scheduler.DefaultReminderScheduler.Companion.EXTRA_REMINDER_MESSAGE
import technology.heli.helinote.feature.notification.scheduler.DefaultReminderScheduler.Companion.EXTRA_REMINDER_TITLE
import javax.inject.Inject

@AndroidEntryPoint
class ReminderBroadcastReceiver : BroadcastReceiver() {

    @Inject
    lateinit var notificationHelper: NotificationHelper

    override fun onReceive(context: Context?, intent: Intent?) {
        intent?.let {
            val reminderId = it.getLongExtra(EXTRA_REMINDER_ID, 0L)
            val title = it.getStringExtra(EXTRA_REMINDER_TITLE)
            val message = it.getStringExtra(EXTRA_REMINDER_MESSAGE)

            notificationHelper.showReminderNotification(
                title = title,
                content = message,
                notificationId = reminderId.toInt()
            )
        }
    }
}