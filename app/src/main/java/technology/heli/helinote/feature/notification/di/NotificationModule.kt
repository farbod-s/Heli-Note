package technology.heli.helinote.feature.notification.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import technology.heli.helinote.feature.notification.scheduler.DefaultReminderScheduler
import technology.heli.helinote.feature.notification.scheduler.ReminderScheduler
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class NotificationModule {

    @Binds
    @Singleton
    abstract fun bindReminderScheduler(reminderScheduler: DefaultReminderScheduler): ReminderScheduler
}