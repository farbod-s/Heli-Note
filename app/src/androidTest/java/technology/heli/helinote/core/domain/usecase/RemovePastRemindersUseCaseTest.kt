package technology.heli.helinote.core.domain.usecase

import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import technology.heli.helinote.core.domain.model.Reminder
import technology.heli.helinote.core.domain.repository.ReminderRepository

class RemovePastRemindersUseCaseTest {

    private lateinit var reminderRepository: ReminderRepository
    private lateinit var removeRemindersUseCase: RemoveRemindersUseCase
    private lateinit var removePastRemindersUseCase: RemovePastRemindersUseCase

    @Before
    fun setUp() {
        reminderRepository = mock()
        removeRemindersUseCase = mock()
        removePastRemindersUseCase =
            RemovePastRemindersUseCase(reminderRepository, removeRemindersUseCase)
    }

    @Test
    fun should_remove_past_reminders() = runTest {
        val pastReminders = listOf(Reminder(timestamp = System.currentTimeMillis() - 1000))

        whenever(reminderRepository.getPastReminders(any())).thenReturn(pastReminders)

        removePastRemindersUseCase()

        verify(removeRemindersUseCase).invoke(pastReminders)
    }

    @Test
    fun should_do_nothing_if_no_past_reminders() = runTest {
        whenever(reminderRepository.getPastReminders(any())).thenReturn(emptyList())

        removePastRemindersUseCase()

        verify(removeRemindersUseCase, never()).invoke(any())
    }
}