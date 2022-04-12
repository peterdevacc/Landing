package com.peter.landing.ui.common

import android.animation.TimeInterpolator
import androidx.core.view.animation.PathInterpolatorCompat
import androidx.transition.ChangeBounds
import androidx.transition.Fade
import androidx.transition.Transition
import androidx.transition.TransitionSet

fun fadeThrough(): Transition {
    return transitionTogether {
        interpolator = FAST_OUT_SLOW_IN
        this += ChangeBounds()
        this += transitionSequential {
            addTransition(Fade(Fade.OUT))
            addTransition(Fade(Fade.IN))
        }
    }
}

inline fun transitionTogether(crossinline body: TransitionSet.() -> Unit): TransitionSet {
    return TransitionSet().apply {
        ordering = TransitionSet.ORDERING_TOGETHER
        body()
    }
}

inline fun transitionSequential(
    crossinline body: SequentialTransitionSet.() -> Unit
): SequentialTransitionSet {
    return SequentialTransitionSet().apply(body)
}

val FAST_OUT_SLOW_IN: TimeInterpolator by lazy(LazyThreadSafetyMode.NONE) {
    PathInterpolatorCompat.create(0.4f, 0f, 0.2f, 1f)
}

inline fun TransitionSet.forEach(action: (transition: Transition) -> Unit) {
    for (i in 0 until transitionCount) {
        action(getTransitionAt(i) ?: throw IndexOutOfBoundsException())
    }
}

inline fun TransitionSet.forEachIndexed(action: (index: Int, transition: Transition) -> Unit) {
    for (i in 0 until transitionCount) {
        action(i, getTransitionAt(i) ?: throw IndexOutOfBoundsException())
    }
}

operator fun TransitionSet.plusAssign(transition: Transition) {
    addTransition(transition)
}

const val FADE_DURATION = 200L
