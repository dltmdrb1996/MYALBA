package com.bottotop.core.ext

import android.app.Activity

fun Activity.hideKeyboard() {
    currentFocus?.let {
        inputMethodManager.hideSoftInputFromWindow(it.windowToken, 0)
    }
}

