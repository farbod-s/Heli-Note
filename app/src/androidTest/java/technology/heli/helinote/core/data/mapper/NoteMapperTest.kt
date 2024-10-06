package technology.heli.helinote.core.data.mapper

import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import technology.heli.helinote.core.database.entity.NoteEntity
import technology.heli.helinote.core.domain.mapper.DataMapper
import technology.heli.helinote.core.domain.model.Note

class NoteMapperTest {

    private lateinit var noteMapper: DataMapper<NoteEntity, Note>

    @Before
    fun setUp() {
        noteMapper = NoteEntityMapper()
    }

    @Test
    fun testMapTo() {
        val entity = NoteEntity(1, "Test", "Content", 123L)
        val domain = noteMapper.mapTo(entity)

        assertEquals(entity.id, domain.id)
        assertEquals(entity.title, domain.title)
    }

    @Test
    fun testMapFrom() {
        val domain = Note(1, "Test", "Content", 123L)
        val entity = noteMapper.mapFrom(domain)

        assertEquals(domain.id, entity.id)
        assertEquals(domain.title, entity.title)
    }
}