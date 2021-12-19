package com.bottotop.model.wrapper



sealed class APIResult {
    object Success : APIResult()
    data class Error(val error: APIError) : APIResult()
}

