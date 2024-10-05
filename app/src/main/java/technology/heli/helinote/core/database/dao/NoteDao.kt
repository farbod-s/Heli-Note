package technology.heli.helinote.core.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow
import technology.heli.helinote.core.database.entity.NoteEntity

@Dao
interface NoteDao {

    @Query("SELECT * FROM note_table")
    fun getNotes(): Flow<List<NoteEntity>>

    @Query("SELECT * FROM note_table WHERE id = :id")
    fun getNoteById(id: Long): Flow<NoteEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNote(note: NoteEntity): Long

    @Query("DELETE FROM note_table WHERE id = :id")
    suspend fun deleteNoteById(id: Long)
}