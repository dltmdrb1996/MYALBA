package com.bottotop.core.ext

import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.bottotop.core.model.Event
import com.google.android.material.snackbar.Snackbar


fun Fragment.showToast(message: String, isLongToast: Boolean = false) {
    context?.run {
        showToast(message, isLongToast)
    }
}

val Fragment.appContext: Context get() = activity?.applicationContext!!

fun Fragment.showSnackbar(snackbarText: String, timeLength: Int) {
    activity?.let { Snackbar.make(it.findViewById<View>(android.R.id.content), snackbarText, timeLength).show() }
}
