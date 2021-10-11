package com.bottotop.core.util

import android.content.Context
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View


abstract class OnSwipeTouchListener(context: Context?) : View.OnTouchListener {
    companion object {
        private const val SWIPE_DISTANCE_THRESHOLD = 300
        private const val SWIPE_VELOCITY_THRESHOLD = 100
    }
    private val gestureDetector: GestureDetector
    abstract fun onSwipeLeft()
    abstract fun onSwipeRight()
    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        return gestureDetector.onTouchEvent(event)
    }
    private inner class GestureListener : GestureDetector.SimpleOnGestureListener() {
        override fun onDown(e: MotionEvent): Boolean {
            return true
        }
        override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            val distanceX = e2.x - e1.x
            val distanceY = e2.y - e1.y
            if (Math.abs(distanceX) > Math.abs(distanceY)
                && Math.abs(distanceX) > SWIPE_DISTANCE_THRESHOLD
                && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                if (distanceX > 0) onSwipeRight() else onSwipeLeft()
                return true
            }
            return false
        }
    }
    init {
        gestureDetector = GestureDetector(context, GestureListener())
    }
}
//    @SuppressLint("ClickableViewAccessibility")
//    fun setSwipe() {
//        _binding?.apply {
//            this.homeLayout.setOnTouchListener(object : OnSwipeTouchListener(context) {
//                override fun onSwipeLeft() {
//                    (requireActivity() as CurrentBottomNav).currentBottomNav(3)
//                    (requireActivity() as ToFlowNavigatable).navigateToFlow(NavigationFlow.ScheduleFlow("test"))
//                }
//                override fun onSwipeRight() {
//                    (requireActivity() as CurrentBottomNav).currentBottomNav(1)
//                    (requireActivity() as ToFlowNavigatable).navigateToFlow(NavigationFlow.CommunityFlow("test"))
//                }
//            })
//        }
//    }