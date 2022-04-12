package com.peter.landing.ui.plan.adapter

import com.github.mikephil.charting.data.ChartData

data class ChartItem(
    val type: Type,
    val chartData: ChartData<*>
) {
    enum class Type {
        BAR, PIE
    }
}