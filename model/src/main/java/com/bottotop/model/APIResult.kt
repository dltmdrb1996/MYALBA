
package com.bottotop.model


sealed class APIResult {
    object Success : APIResult()
    object NotFound : APIResult()
    object ServerError : APIResult()
}
