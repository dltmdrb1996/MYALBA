package com.bottotop.model.wrapper

sealed class APIError {
    object SeverError : APIError()
    object KeyValueError : APIError()
    object NullValueError : APIError()
    data class Error(val e: Throwable) : APIError()
}
