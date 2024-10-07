package technology.heli.helinote.core.domain.model

enum class RepeatType(
    val value: Int,
    val displayText: String
) {
    NONE(0, "Does not repeat"),
    DAILY(1, "Repeat daily"),
    WEEKLY(2, "Repeat weekly"),
    MONTHLY(3, "Repeat monthly"),
    YEARLY(4, "Repeat yearly");

    override fun toString(): String = displayText
}