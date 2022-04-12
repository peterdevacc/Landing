package com.peter.landing.ui.common.adapter

import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.peter.landing.ui.util.getPxFromDp

class ExplainAdapter : RecyclerView.Adapter<ExplainViewHolder>() {

    private var explainItemList = emptyList<ExplainItem>()

    private var isZoom = false

    private var exerciseSubmitListener: View.OnClickListener? = null
    private var exerciseResetListener: View.OnClickListener? = null

    private var systemDensity: Int = -1

    fun setData(explainItemList: List<ExplainItem>, isZoom: Boolean = false) {
        this.explainItemList = explainItemList
        notifyDataSetChanged()
        this.isZoom = isZoom
    }

    fun addRearrangeExercise(
        submitListener: View.OnClickListener,
        resetListener: View.OnClickListener
    ) {
        exerciseSubmitListener = submitListener
        exerciseResetListener = resetListener
    }

    fun setSystemDensity(systemDensity: Int) {
        this.systemDensity = systemDensity
    }

    val itemSpaceDecor = object : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
        ) {
            val spacing = getPxFromDp(4f, systemDensity)
            val position = parent.getChildAdapterPosition(view)
            if (position >= 0 && position < explainItemList.size) {
                val itemType = getItemViewType(position)
                if (itemType == ExplainItem.Type.ItemWordRearrangeExercise.ordinal) {
                    val exerciseTopSpacing = getPxFromDp(16f, systemDensity)
                    outRect.top = exerciseTopSpacing
                } else {
                    outRect.top = spacing
                }

                outRect.left = spacing
                outRect.right = spacing

                if (position == explainItemList.lastIndex) {
                    if (itemType == ExplainItem.Type.ItemWordRearrangeExercise.ordinal) {
                        val exerciseBottomSpacing = getPxFromDp(16f, systemDensity)
                        outRect.bottom = exerciseBottomSpacing
                    } else {
                        outRect.bottom = spacing
                    }
                } else {
                    outRect.bottom = 0
                }
            } else {
                outRect.top = 0
                outRect.left = 0
                outRect.right = 0
                outRect.bottom = 0
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExplainViewHolder {
        return ExplainViewHolder.create(parent, viewType)
    }

    override fun getItemViewType(position: Int): Int {
        return explainItemList[position].type.ordinal
    }

    override fun getItemCount(): Int = explainItemList.size

    override fun onBindViewHolder(holder: ExplainViewHolder, position: Int) {
        holder.bind(explainItemList[position].data, isZoom)
        if (holder is ExplainViewHolder.ItemWordRearrangeExerciseViewHolder) {
            holder.setSubmitListener(exerciseSubmitListener)
            holder.setResetListener(exerciseResetListener)
        }
    }

}