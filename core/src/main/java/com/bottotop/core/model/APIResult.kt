
package com.bottotop.core.model


sealed class APIResult<out R> {
    data class Success<out T>(val data: T) : APIResult<T>()
    object NotFound : APIResult<Nothing>()
    object ServerError : APIResult<Nothing>()
}
