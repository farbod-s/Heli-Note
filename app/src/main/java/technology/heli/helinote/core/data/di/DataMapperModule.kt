package technology.heli.helinote.core.data.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import technology.heli.helinote.core.data.mapper.NoteEntityMapper
import technology.heli.helinote.core.data.mapper.ReminderEntityMapper
import technology.heli.helinote.core.database.entity.NoteEntity
import technology.heli.helinote.core.database.entity.ReminderEntity
import technology.heli.helinote.core.domain.mapper.DataMapper
import technology.heli.helinote.core.domain.model.Note
import technology.heli.helinote.core.domain.model.Reminder
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataMapperModule {

    @Provides
    @Singleton
    fun provideNoteMapper(): DataMapper<NoteEntity, Note> = NoteEntityMapper()

    @Provides
    @Singleton
    fun provideReminderMapper(): DataMapper<ReminderEntity, Reminder> = ReminderEntityMapper()
}