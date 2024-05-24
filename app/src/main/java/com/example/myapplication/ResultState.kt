package com.example.myapplication

sealed class ResultState<T>(
    val data: T? = null,
    val errors: List<String>? = listOf(),
    val message: String = "",
    val code: Int = 0
) {
    class Success<T>(data: T) : ResultState<T>(data)

    class Loading<T>(previous: T? = null) : ResultState<T>(previous)

    class Idle<T>(previous: T? = null) : ResultState<T>(previous)

    class Error<T>(
        code: Int,
        message: String,
        previous: T? = null,
        errors: List<String>? = listOf()
    ) :
        ResultState<T>(previous, errors, message, code)
}
