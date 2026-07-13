package com.example.fragmentspractice.utils

import android.view.View

private const val TOUCH_SCALE = 0.9f
private const val TOUCH_ANIMATION_DURATION = 100L

/**
 * Subtle scale-down/scale-up feedback for touch targets (e.g. edit/delete buttons)
 * before continuing with navigation or another action.
 */
fun View.animateTouch(onEnd: () -> Unit = {}) {
    animate()
        .scaleX(TOUCH_SCALE)
        .scaleY(TOUCH_SCALE)
        .setDuration(TOUCH_ANIMATION_DURATION)
        .withEndAction {
            animate()
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(TOUCH_ANIMATION_DURATION)
                .withEndAction { onEnd() }
                .start()
        }
        .start()
}
