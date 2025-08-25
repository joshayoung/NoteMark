package com.joshayoung.notemark.core.data.networking

sealed interface Result<out D, out E : Error> {
    data class Success<out D>(
        val data: D,
    ) : Result<D, Nothing>

    data class Error<out E : com.joshayoung.notemark.core.data.networking.Error>(
        val error: E,
    ) : Result<Nothing, E>
}

inline fun <T, E : Error, R> Result<T, E>.map(map: (T) -> R): Result<R, E> =
    when (this) {
        is Result.Error -> Result.Error(error)
        is Result.Success -> Result.Success(map(data))
    }

fun <T, E : Error> Result<T, E>.asEmptyDataResult(): EmptyDataResult<E> = map { }

typealias EmptyDataResult<E> = Result<Unit, E>

typealias EmptyResult<E> = Result<Unit, E>