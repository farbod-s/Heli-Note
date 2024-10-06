package technology.heli.helinote.core.domain.usecase

import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import technology.heli.helinote.core.domain.model.Reminder
import technology.heli.helinote.core.domain.repository.ReminderRepository
import technology.heli.helinote.feature.notification.scheduler.ReminderScheduler

class RemoveRemindersUseCaseTest {

    private lateinit var reminderRepository: ReminderRepository
    private lateinit var reminderScheduler: ReminderScheduler
    private lateinit var removeRemindersUseCase: RemoveRemindersUseCase

    @Before
    fun setUp() {
        reminderRepository = mock()
        reminderScheduler = mock()
        removeRemindersUseCase = RemoveRemindersUseCase(reminderRepository, reminderScheduler)
    }

    @Test
    fun should_remove_reminders_and_cancel_schedules() = runTest {
        val reminder = Reminder(id = 1L)

        removeRemindersUseCase(listOf(reminder))

        verify(reminderScheduler).cancel(reminder.id)
        verify(reminderRepository).deleteRemindersById(listOf(reminder.id))
    }

    @Test
    fun should_do_nothing_if_reminders_to_remove_is_empty() = runTest {
        removeRemindersUseCase(emptyList())

        verify(reminderRepository, never()).deleteRemindersById(any())
        verify(reminderScheduler, never()).cancel(any())
    }
}