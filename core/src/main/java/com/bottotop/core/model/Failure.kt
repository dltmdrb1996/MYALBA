
package com.bottotop.core.model


sealed class Failure {
    object NetworkConnection : Failure()
    object ServerError : Failure()
    class Throwable(val throwable: kotlin.Throwable) : Failure()
    class Code(val code: String?) : Failure()
}
