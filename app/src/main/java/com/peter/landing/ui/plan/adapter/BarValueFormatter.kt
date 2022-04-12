package com.peter.landing.ui.plan.adapter

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter

class BarValueFormatter : ValueFormatter() {

    private val sections = arrayOf("默写", "拼写", "单选", "识词")

    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        return sections.getOrNull(value.toInt()) ?: value.toString()
    }

}