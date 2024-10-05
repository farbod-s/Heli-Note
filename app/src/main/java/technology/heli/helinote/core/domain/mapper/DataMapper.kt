package technology.heli.helinote.core.domain.mapper

interface DataMapper<Input, Output> {
    fun mapTo(input: Input): Output {
        throw NotImplementedError("mapTo() is not implemented!")
    }

    fun mapFrom(output: Output): Input {
        throw NotImplementedError("mapFrom() is not implemented!")
    }
}