package technology.heli.helinote.core.database.converter

import androidx.room.TypeConverter
import technology.heli.helinote.core.domain.model.RepeatType

class RepeatTypeConverter {

    @TypeConverter
    fun fromRepeatType(repeatType: RepeatType): Int = repeatType.value

    @TypeConverter
    fun toRepeatType(value: Int): RepeatType =
        RepeatType.entries.first { it.value == value }
}