
package com.bottotop.core.ext

import android.animation.Animator
import android.animation.AnimatorSet
import android.graphics.drawable.Drawable
import android.os.SystemClock
import android.view.View
import android.view.ViewTreeObserver
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.target.BaseTarget
import com.bumptech.glide.request.target.SizeReadyCallback
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition

fun View.isInvisible() {
    this.visibility = View.INVISIBLE
}

fun View.isVisible() {
    this.visibility = View.VISIBLE
}

fun View.inGone() {
    this.visibility = View.GONE
}

fun ImageView.loadFromUrl(url: String) =
    Glide.with(this.context.applicationContext)
        .load(url)
        .transition(DrawableTransitionOptions.withCrossFade())
        .into(this)


fun ImageView.loadUrlAndPostponeEnterTransition(url: String, activity: FragmentActivity) {
    val target: Target<Drawable> = ImageViewBaseTarget(this, activity)
    Glide.with(context.applicationContext).load(url).into(target)
}

private typealias OnMeasuredCallback = (view: View, width: Int, height: Int , x : Float , y : Float) -> Unit

inline fun View.waitForMeasure(crossinline callback: OnMeasuredCallback) {
    if(isInEditMode) {
        return
    }
    val view = this
    val width = view.getWidth()
    val height = view.getHeight()
    val x = view.x
    val y = view.y
    if (width > 0 && height > 0 && x > 0 && y > 0) {
        callback(view, width, height , x , y)
        return
    }
    view.performClick()

    view.getViewTreeObserver().addOnPreDrawListener(object : ViewTreeObserver.OnPreDrawListener {
        override fun onPreDraw(): Boolean {
            val observer = view.getViewTreeObserver()
            if (observer.isAlive()) {
                observer.removeOnPreDrawListener(this)
            }

            callback(view, view.width, view.height, view.x , view.y)

            return true
        }
    })
}

inline fun AnimatorSet.onAnimationEnd(crossinline onAnimationEnd: (Animator) -> Unit): AnimatorSet = apply {
    addListener(createAnimatorListener(onAnimationEnd = onAnimationEnd))
}

private const val MIN_CLICK_INTERVAL = 500L

fun View.setOnSingleClickListener(onSingleClick: (View) -> Unit) {
    var lastClickTime = 0L

    setOnClickListener {
        val elapsedTime = SystemClock.elapsedRealtime() - lastClickTime

        if (elapsedTime < MIN_CLICK_INTERVAL) return@setOnClickListener

        lastClickTime = SystemClock.elapsedRealtime()

        onSingleClick(this)
    }
}



fun animateTogether(vararg animators: Animator) = AnimatorSet().apply {
    playTogether(*animators)
}

inline fun createAnimatorListener(
    crossinline onAnimationEnd: (Animator) -> Unit = {},
    crossinline onAnimationStart: (Animator) -> Unit = {},
    crossinline onAnimationCancel: (Animator) -> Unit = {},
    crossinline onAnimationRepeat: (Animator) -> Unit = {}
) = object : Animator.AnimatorListener {
    override fun onAnimationRepeat(animation: Animator) = onAnimationRepeat(animation)

    override fun onAnimationEnd(animation: Animator) = onAnimationEnd(animation)

    override fun onAnimationCancel(animation: Animator) = onAnimationCancel(animation)

    override fun onAnimationStart(animation: Animator) = onAnimationStart(animation)
}


private class ImageViewBaseTarget(var imageView: ImageView?, var activity: FragmentActivity?) :
    BaseTarget<Drawable>() {
    override fun onLoadFailed(errorDrawable: Drawable?) {
        super.onLoadFailed(errorDrawable)
        activity?.supportStartPostponedEnterTransition()
    }

    override fun getSize(cb: SizeReadyCallback) = cb.onSizeReady(SIZE_ORIGINAL, SIZE_ORIGINAL)

    override fun removeCallback(cb: SizeReadyCallback) {
        imageView = null
        activity = null
    }

    override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
        imageView?.setImageDrawable(resource)
        activity?.supportStartPostponedEnterTransition()
    }

}

