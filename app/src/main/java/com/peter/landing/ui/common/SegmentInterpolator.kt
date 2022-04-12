package com.peter.landing.ui.common

import android.animation.TimeInterpolator

class SegmentInterpolator(
    val base: TimeInterpolator,
    val start: Float = 0f,
    val end: Float = 1f
) : TimeInterpolator {

    private val offset = base.getInterpolation(start)
    private val xRatio = (end - start) / 1f
    private val yRatio = (base.getInterpolation(end) - offset) / 1f

    override fun getInterpolation(input: Float): Float {
        return (base.getInterpolation(start + (input * xRatio)) - offset) / yRatio
    }
}
