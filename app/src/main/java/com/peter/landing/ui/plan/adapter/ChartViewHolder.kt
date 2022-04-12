package com.peter.landing.ui.plan.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.ChartData
import com.github.mikephil.charting.data.PieData
import com.peter.landing.R
import com.peter.landing.databinding.ItemChartTodayBinding
import com.peter.landing.databinding.ItemChartTotalBinding

sealed class ChartViewHolder (view: View) : RecyclerView.ViewHolder(view) {

    open fun bind(chartData: ChartData<*>) {}

    class BarChartViewHolder(private val binding: ItemChartTodayBinding) :
        ChartViewHolder(binding.root)
    {

        override fun bind(chartData: ChartData<*>) {
            binding.itemChartTodayBarChart.description.isEnabled = false
            binding.itemChartTodayBarChart.setDrawBarShadow(false)
            binding.itemChartTodayBarChart.setTouchEnabled(false)

            val color = ContextCompat.getColor(
                itemView.context,
                R.color.color_on_surface
            )

            val xl = binding.itemChartTodayBarChart.xAxis
            xl.position = XAxis.XAxisPosition.BOTTOM
            xl.setDrawAxisLine(false)
            xl.setDrawGridLines(false)
            xl.granularity = 1f
            xl.valueFormatter = BarValueFormatter()
            xl.labelCount = 4
            xl.textSize = 14f
            xl.textColor = color

            val yl = binding.itemChartTodayBarChart.axisLeft
            yl.setDrawAxisLine(false)
            yl.setDrawGridLines(false)
            yl.axisMaximum = 100f
            yl.axisMinimum = 0f
            yl.labelCount = 2
            yl.isEnabled = false

            val yr = binding.itemChartTodayBarChart.axisRight
            yr.setDrawAxisLine(false)
            yr.setDrawGridLines(false)
            yr.axisMaximum = 100f
            yr.axisMinimum = 0f
            yr.labelCount = 2
            yr.textColor = color
            yr.isEnabled = false

            binding.itemChartTodayBarChart.data = chartData as BarData
            binding.itemChartTodayBarChart.legend.isEnabled = false
        }

    }

    class PieChartViewHolder(private val binding: ItemChartTotalBinding)
        : ChartViewHolder(binding.root)
    {
        override fun bind(chartData: ChartData<*>) {
            binding.itemChartTotalPieChart.description.isEnabled = false
            binding.itemChartTotalPieChart.holeRadius = 56f
            binding.itemChartTotalPieChart.transparentCircleRadius = 60f
            binding.itemChartTotalPieChart.setTouchEnabled(false)

            binding.itemChartTotalPieChart.setDrawEntryLabels(false)

            binding.itemChartTotalPieChart.setCenterTextSize(20f)
            binding.itemChartTotalPieChart.centerText = "work smart\nwork hard"

            chartData.setDrawValues(false)
            binding.itemChartTotalPieChart.data = chartData as PieData
            val legend = binding.itemChartTotalPieChart.legend
            legend.isEnabled = false

            val remember = "已学：${chartData.dataSets[0].getEntryForIndex(0).y.toInt()}个"
            binding.itemChartTotalRememberData.text = remember
            val revise = "复习：${chartData.dataSets[0].getEntryForIndex(1).y.toInt()}个"
            binding.itemChartTotalReviseData.text = revise
            val notLearn = "未学：${chartData.dataSets[0].getEntryForIndex(2).y.toInt()}个"
            binding.itemChartTotalNotLearnData.text = notLearn
        }
    }

    companion object {
        fun create(parent: ViewGroup, viewType: Int): ChartViewHolder {
            return when (ChartItem.Type.values()[viewType]) {
                ChartItem.Type.BAR -> {
                    val binding = ItemChartTodayBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                    BarChartViewHolder(binding)
                }
                ChartItem.Type.PIE -> {
                    val binding = ItemChartTotalBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                    PieChartViewHolder(binding)
                }
            }
        }
    }

}