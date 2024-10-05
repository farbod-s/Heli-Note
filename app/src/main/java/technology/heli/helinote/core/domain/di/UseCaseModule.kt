package technology.heli.helinote.core.domain.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import technology.heli.helinote.core.domain.repository.NoteRepository
import technology.heli.helinote.core.domain.repository.ReminderRepository
import technology.heli.helinote.core.domain.usecase.AddRemindersUseCase
import technology.heli.helinote.core.domain.usecase.GetNoteUseCase
import technology.heli.helinote.core.domain.usecase.GetNotesUseCase
import technology.heli.helinote.core.domain.usecase.RemovePastRemindersUseCase
import technology.heli.helinote.core.domain.usecase.RemoveRemindersUseCase
import technology.heli.helinote.core.domain.usecase.UpdateNoteUseCase
import technology.heli.helinote.feature.notification.scheduler.ReminderScheduler

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {

    @Provides
    fun provideGetNotesUseCase(
        notesRepository: NoteRepository,
        reminderRepository: ReminderRepository
    ): GetNotesUseCase =
        GetNotesUseCase(noteRepository = notesRepository, reminderRepository = reminderRepository)

    @Provides
    fun provideGetNoteUseCase(
        notesRepository: NoteRepository,
        reminderRepository: ReminderRepository
    ): GetNoteUseCase =
        GetNoteUseCase(noteRepository = notesRepository, reminderRepository = reminderRepository)

    @Provides
    fun provideUpdateNoteUseCase(
        notesRepository: NoteRepository,
        addReminderUseCase: AddRemindersUseCase,
        removeReminderUseCase: RemoveRemindersUseCase
    ): UpdateNoteUseCase =
        UpdateNoteUseCase(
            noteRepository = notesRepository,
            addReminderUseCase = addReminderUseCase,
            removeReminderUseCase = removeReminderUseCase
        )

    @Provides
    fun provideAddReminderUseCase(
        reminderRepository: ReminderRepository,
        reminderScheduler: ReminderScheduler
    ): AddRemindersUseCase =
        AddRemindersUseCase(
            reminderRepository = reminderRepository,
            reminderScheduler = reminderScheduler
        )

    @Provides
    fun provideRemoveReminderUseCase(
        reminderRepository: ReminderRepository,
        reminderScheduler: ReminderScheduler
    ): RemoveRemindersUseCase =
        RemoveRemindersUseCase(
            reminderRepository = reminderRepository,
            reminderScheduler = reminderScheduler
        )

    @Provides
    fun provideRemovePastRemindersUseCase(
        reminderRepository: ReminderRepository,
        removeReminderUseCase: RemoveRemindersUseCase
    ): RemovePastRemindersUseCase =
        RemovePastRemindersUseCase(
            reminderRepository = reminderRepository,
            removeReminderUseCase = removeReminderUseCase
        )
}