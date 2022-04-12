package com.peter.landing.ui.note.adapter

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.peter.landing.R
import com.peter.landing.ui.util.bitmapFromVectorDrawable
import com.peter.landing.ui.util.getPxFromDp

class SwipeDeleteCallback(
    val context: Context,
    swipeColor: Int,
    swipeIcon: Int,
    swipeIconTint: Int?
) : ItemTouchHelper.Callback() {

    private val swipeBackgroundPaint = Paint()
    private val swipeIconPaint = Paint()
    private val itemBackgroundPaint = Paint()
    private var swipeIcon: Bitmap
    private var swipeEnable = false
    private val density: Int
    private val pad: Int
    private val round: Float
    private val iconPadding: Int

    constructor(
        context: Context
    ) : this(
        context, R.color.red50, R.drawable.ic_delete_forever_24dp, android.R.color.white
    )

    init {
        swipeBackgroundPaint.style = Paint.Style.FILL
        swipeBackgroundPaint.color = ContextCompat.getColor(context, swipeColor)
        itemBackgroundPaint.style = Paint.Style.FILL
        itemBackgroundPaint.color = ContextCompat.getColor(context, R.color.color_background)
        this.swipeIcon = bitmapFromVectorDrawable(context, swipeIcon, swipeIconTint)
        density = context.resources.displayMetrics.densityDpi
        pad = getPxFromDp(16f, density)
        round = getPxFromDp(4f, density).toFloat()
        iconPadding = getPxFromDp(16f, density)
    }

    interface Callback {
        fun onSwipe()
    }

    fun setSwipeEnable(enabled: Boolean) {
        swipeEnable = enabled
    }

    override fun isItemViewSwipeEnabled(): Boolean {
        return swipeEnable
    }

    override fun isLongPressDragEnabled(): Boolean {
        return false
    }

    override fun getMovementFlags(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder
    ): Int {
        val dragFlag = 0 //ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        val swipeFlags =
            if (viewHolder is Callback) {
                ItemTouchHelper.START //left swipe only
            } else {
                0
            }
        return makeMovementFlags(dragFlag, swipeFlags)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return viewHolder.itemViewType == target.itemViewType
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        if (viewHolder is Callback) {
            viewHolder.onSwipe()
        }
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            var top = viewHolder.itemView.top.toFloat()
            val width = viewHolder.itemView.width.toFloat()
            val right = viewHolder.itemView.width + dX
            val bottom = (viewHolder.itemView.top + viewHolder.itemView.height).toFloat()

            c.drawRoundRect(
                0f, top, width + pad, bottom,
                round, round,
                swipeBackgroundPaint
            )
            c.drawRect(dX, top, right, bottom, itemBackgroundPaint)

            if (dX < 0) {
                val left = viewHolder.itemView.right - swipeIcon.width - iconPadding
                top = (viewHolder.itemView.top +
                        (viewHolder.itemView.height / 2 - swipeIcon.height / 2)
                        ).toFloat()
                c.drawBitmap(swipeIcon, left.toFloat(), top, swipeIconPaint)
            }

            viewHolder.itemView.translationX = dX
        } else {
            super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        }
    }
}