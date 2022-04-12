package com.peter.landing.ui.common

import android.animation.TimeInterpolator
import androidx.transition.Transition
import androidx.transition.TransitionSet

class SequentialTransitionSet : TransitionSet() {

    init {
        ordering = ORDERING_SEQUENTIAL
    }

    private var _duration: Long = -1
    private var _interpolator: TimeInterpolator? = null

    private val weights = mutableListOf<Float>()

    override fun setOrdering(ordering: Int): TransitionSet {
        if (ordering != ORDERING_SEQUENTIAL) {
            throw IllegalArgumentException(
                "SequentialTransitionSet only supports ORDERING_SEQUENTIAL"
            )
        }
        return super.setOrdering(ordering)
    }

    fun addTransition(transition: Transition, weight: Float): TransitionSet {
        super.addTransition(transition)
        weights += weight
        distributeDuration()
        distributeInterpolator()
        return this
    }

    override fun addTransition(transition: Transition): TransitionSet {
        return addTransition(transition, 1f)
    }

    override fun setDuration(duration: Long): TransitionSet {
        // Don't call super.
        _duration = duration
        distributeDuration()
        return this
    }

    override fun getDuration(): Long {
        return _duration
    }

    private fun distributeDuration() {
        if (_duration < 0) {
            forEach { transition ->
                transition.duration = -1
            }
            return
        }
        val totalWeight = weights.sum()
        forEachIndexed { i, transition ->
            transition.duration = (_duration * weights[i] / totalWeight).toLong()
        }
    }

    override fun setInterpolator(interpolator: TimeInterpolator?): TransitionSet {
        // Don't call super.
        _interpolator = interpolator
        distributeInterpolator()
        return this
    }

    override fun getInterpolator(): TimeInterpolator? {
        return _interpolator
    }

    private fun distributeInterpolator() {
        val interpolator = _interpolator
        if (interpolator == null) {
            forEach { transition ->
                transition.interpolator = null
            }
            return
        }
        val totalWeight = weights.sum()
        var start = 0f
        forEachIndexed { i, transition ->
            val range = weights[i] / totalWeight
            transition.interpolator = SegmentInterpolator(
                interpolator,
                start,
                start + range
            )
            start += range
        }
    }
}
