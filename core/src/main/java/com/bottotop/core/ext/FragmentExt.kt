package com.bottotop.core.ext

import android.content.Context
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.bottotop.core.model.Event
import com.google.android.material.snackbar.Snackbar

fun Fragment.showToast(resourceId: Int, isLongToast: Boolean = false) {
    context?.run {
        showToast(resourceId, isLongToast)
    }
}

fun Fragment.showToast(message: String, isLongToast: Boolean = false) {
    context?.run {
        showToast(message, isLongToast)
    }
}

fun Fragment.close() = fragmentManager?.popBackStack()

val Fragment.appContext: Context get() = activity?.applicationContext!!

fun Fragment.showSnackbar(snackbarText: String, timeLength: Int) {
    activity?.let { Snackbar.make(it.findViewById<View>(android.R.id.content), snackbarText, timeLength).show() }
}

fun Fragment.setupSnackbar(lifecycleOwner: LifecycleOwner, snackbarEvent: LiveData<Event<Int>>, timeLength: Int) {
    snackbarEvent.observe(lifecycleOwner, Observer { event ->
        event.getContentIfNotHandled()?.let { res ->
            context?.let { showSnackbar(it.getString(res), timeLength) }
        }
    })
}