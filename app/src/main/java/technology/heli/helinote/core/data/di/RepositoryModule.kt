package technology.heli.helinote.core.data.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import technology.heli.helinote.core.data.repository.DefaultNoteRepository
import technology.heli.helinote.core.data.repository.DefaultReminderRepository
import technology.heli.helinote.core.domain.repository.NoteRepository
import technology.heli.helinote.core.domain.repository.ReminderRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindNoteRepository(noteRepository: DefaultNoteRepository): NoteRepository

    @Binds
    @Singleton
    abstract fun bindReminderRepository(
        reminderRepository: DefaultReminderRepository
    ): ReminderRepository
}