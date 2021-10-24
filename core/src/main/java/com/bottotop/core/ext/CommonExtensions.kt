package com.bottotop.core.ext

import android.content.Context
import android.os.Handler
import android.os.Looper

fun withDelayOnMain(delay: Long, block: () -> Unit) {
    Handler(Looper.getMainLooper()).postDelayed(Runnable(block), delay)
}

