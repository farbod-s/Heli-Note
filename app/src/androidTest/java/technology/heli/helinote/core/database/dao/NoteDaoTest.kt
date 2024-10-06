package technology.heli.helinote.core.database.dao

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import technology.heli.helinote.core.database.NoteDatabase
import technology.heli.helinote.core.database.entity.NoteEntity
import technology.heli.helinote.core.database.entity.ReminderEntity
import technology.heli.helinote.core.domain.model.RepeatType

@RunWith(AndroidJUnit4::class)
class NoteDaoTest {

    @get:Rule
    var instantTaskExecutorRule: TestRule = InstantTaskExecutorRule()

    private lateinit var database: NoteDatabase
    private lateinit var noteDao: NoteDao
    private lateinit var reminderDao: ReminderDao

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>()
        database = Room.inMemoryDatabaseBuilder(context, NoteDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        noteDao = database.noteDao()
        reminderDao = database.reminderDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

    @Test
    fun testInsertNoteAndRetrieveById() = runBlocking {
        val note = NoteEntity(0, "Test Note", "Test Content", System.currentTimeMillis())
        val noteId = noteDao.insertNote(note)

        val retrievedNote = noteDao.getNoteById(noteId).first()
        assertEquals(note.title, retrievedNote?.title)
    }

    @Test
    fun testInsertReminderAndRetrieveByNoteId() = runBlocking {
        val note = NoteEntity(0, "Test Note", "Test Content", System.currentTimeMillis())
        val noteId = noteDao.insertNote(note)

        val reminder = ReminderEntity(0, System.currentTimeMillis(), RepeatType.NONE, noteId)
        reminderDao.insertReminder(reminder)

        val retrievedReminders = reminderDao.getRemindersByNoteId(noteId).first()
        assertEquals(1, retrievedReminders.size)
        assertEquals(reminder.noteId, retrievedReminders[0].noteId)
    }

    @Test
    fun testDeleteNoteAndRemindersAreRemoved() = runBlocking {
        val note = NoteEntity(0, "Test Note", "Test Content", System.currentTimeMillis())
        val noteId = noteDao.insertNote(note)

        val reminder = ReminderEntity(0, System.currentTimeMillis(), RepeatType.NONE, noteId)
        reminderDao.insertReminder(reminder)

        noteDao.deleteNoteById(noteId)

        val reminders = reminderDao.getRemindersByNoteId(noteId).first()
        assertEquals(0, reminders.size)
    }
}