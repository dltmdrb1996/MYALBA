package com.bottotop.core.model


sealed class LoginState{
    object Success : LoginState()
    object Register : LoginState()
    object NoCompany : LoginState()
    object NoData : LoginState()
}
