package technology.heli.helinote.core.domain.usecase

import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mockito.mock
import org.mockito.kotlin.any
import org.mockito.kotlin.never
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import technology.heli.helinote.core.domain.model.Note
import technology.heli.helinote.core.domain.model.Reminder
import technology.heli.helinote.core.domain.repository.ReminderRepository
import technology.heli.helinote.feature.notification.scheduler.ReminderScheduler

class AddRemindersUseCaseTest {

    private lateinit var reminderRepository: ReminderRepository
    private lateinit var reminderScheduler: ReminderScheduler
    private lateinit var addRemindersUseCase: AddRemindersUseCase

    @Before
    fun setUp() {
        reminderRepository = mock()
        reminderScheduler = mock()
        addRemindersUseCase = AddRemindersUseCase(reminderRepository, reminderScheduler)
    }

    @Test
    fun should_add_reminders_when_reminders_list_is_not_empty() = runTest {
        val note = Note()
        val remindersToAdd = listOf(Reminder())

        whenever(reminderRepository.insertReminder(any())).thenReturn(1L)

        addRemindersUseCase(note, remindersToAdd)

        verify(reminderRepository).insertReminder(any())
        verify(reminderScheduler).schedule(any(), any(), any())
    }

    @Test
    fun should_rollback_if_exception_occurs() = runTest {
        val note = Note()
        val remindersToAdd = listOf(Reminder())

        whenever(reminderRepository.insertReminder(any())).thenReturn(1L)
        whenever(reminderScheduler.schedule(any(), any(), any())).thenThrow(RuntimeException())

        try {
            addRemindersUseCase(note, remindersToAdd)
        } catch (e: Exception) {
            // Rollback should occur
        }

        verify(reminderRepository).deleteRemindersById(any())
    }

    @Test
    fun should_not_add_reminders_when_list_is_empty() = runTest {
        val note = Note()
        val remindersToAdd = emptyList<Reminder>()

        addRemindersUseCase(note, remindersToAdd)

        verify(reminderRepository, never()).insertReminder(any())
        verify(reminderScheduler, never()).schedule(any(), any(), any())
    }
}