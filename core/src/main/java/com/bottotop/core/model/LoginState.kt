package com.bottotop.core.model


sealed class LoginState{
    object Suceess : LoginState()
    object Register : LoginState()
    object NoCompany : LoginState()
    object NoToken : LoginState()
}
