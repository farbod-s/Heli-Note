package technology.heli.helinote.core.database.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import technology.heli.helinote.core.database.NoteDatabase
import technology.heli.helinote.core.database.dao.NoteDao
import technology.heli.helinote.core.database.dao.ReminderDao
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    fun provideNoteDatabase(
        @ApplicationContext context: Context,
    ): NoteDatabase = NoteDatabase.getInstance(context)

    @Provides
    @Singleton
    fun provideNoteDao(database: NoteDatabase): NoteDao = database.noteDao()

    @Provides
    @Singleton
    fun provideReminderDao(database: NoteDatabase): ReminderDao = database.reminderDao()
}