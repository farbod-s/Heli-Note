package technology.heli.helinote.core.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import technology.heli.helinote.core.database.converter.RepeatTypeConverter
import technology.heli.helinote.core.database.dao.NoteDao
import technology.heli.helinote.core.database.dao.ReminderDao
import technology.heli.helinote.core.database.entity.NoteEntity
import technology.heli.helinote.core.database.entity.ReminderEntity

@Database(entities = [NoteEntity::class, ReminderEntity::class], version = 1)
@TypeConverters(RepeatTypeConverter::class)
abstract class NoteDatabase : RoomDatabase() {
    abstract fun noteDao(): NoteDao

    abstract fun reminderDao(): ReminderDao

    companion object {
        @Volatile
        private var INSTANCE: NoteDatabase? = null

        fun getInstance(context: Context): NoteDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE
                    ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                NoteDatabase::class.java,
                "note_database",
            ).build()
    }
}