package com.bottotop.core.util.test.utils3.utils

import android.content.Context
import android.content.res.Resources

var FLAG = "Util"

fun setFlag(flag: String) {
    FLAG = flag
}

fun Context.getResources(): Resources {
    return this.resources
}