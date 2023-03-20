package com.peter.landing.data.util

sealed interface DataResult<out T> {

    data class Success<T>(
        val data: T
    ): DataResult<T>

    data class Error(
        val code: Code
    ): DataResult<Nothing> {

        enum class Code {
            UNKNOWN,
            IO
        }

    }

}
