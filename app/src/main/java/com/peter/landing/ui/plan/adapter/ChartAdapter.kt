package com.peter.landing.ui.plan.adapter

import android.graphics.Rect
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.peter.landing.ui.util.getPxFromDp

class ChartAdapter : RecyclerView.Adapter<ChartViewHolder>() {

    private var chartList = emptyList<ChartItem>()

    private var systemDensity: Int = -1

    fun setContent(chartList: List<ChartItem>) {
        this.chartList = chartList
        notifyDataSetChanged()
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
            val spacing = getPxFromDp(16f, systemDensity)
            val position = parent.getChildAdapterPosition(view)
            if (position >= 0) {
                outRect.top = spacing
                outRect.left = spacing
                outRect.right = spacing
                if (position == chartList.lastIndex) {
                    outRect.bottom = spacing
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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChartViewHolder {
        return ChartViewHolder.create(parent, viewType)
    }

    override fun getItemViewType(position: Int): Int {
        return chartList.elementAt(position).type.ordinal
    }

    override fun getItemCount(): Int = chartList.size

    override fun onBindViewHolder(holder: ChartViewHolder, position: Int) {
        holder.bind(chartList.elementAt(position).chartData)
    }

}