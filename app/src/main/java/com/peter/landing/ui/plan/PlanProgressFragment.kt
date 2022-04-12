package com.peter.landing.ui.plan

import android.content.res.Configuration
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.Utils
import com.peter.landing.data.local.plan.StudyPlan
import com.peter.landing.databinding.FragmentPlanProgressBinding
import com.peter.landing.ui.plan.adapter.ChartAdapter
import com.peter.landing.ui.plan.adapter.ChartItem
import com.peter.landing.ui.util.MAGIC_TRANSITION_NUMBER
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlanProgressFragment constructor(
    private val plan: StudyPlan
) : Fragment() {

    private var planProgressBinding: FragmentPlanProgressBinding? = null
    private val binding get() = planProgressBinding!!
    private var isDarkMode = false
    private lateinit var viewModel: PlanViewModel
    private val adapter = ChartAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        planProgressBinding = FragmentPlanProgressBinding
            .inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = parentFragment?.run {
            ViewModelProvider(this)[PlanViewModel::class.java]
        } ?: throw Exception("Invalid parentFragment")

        adapter.setSystemDensity(resources.displayMetrics.densityDpi)
        binding.planProgressChartList.addItemDecoration(adapter.itemSpaceDecor)
        binding.planProgressChartList.adapter = adapter

        isDarkMode = when (resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) {
            Configuration.UI_MODE_NIGHT_YES -> true
            else -> false
        }

        viewLifecycleOwner.lifecycleScope.launch {
            Utils.init(context)

            binding.planProgressChartList
                .addOnAttachStateChangeListener(chartListStateChangeListener)
            val list = getChartList(plan)
            adapter.setContent(list)

            binding.planProgressWrongListButton.setOnClickListener {
                findNavController().navigate(
                    PlanFragmentDirections
                        .actionPlanFragmentToWrongListFragment()
                )
            }

            delay(MAGIC_TRANSITION_NUMBER)
            binding.planProgressGroup.visibility = View.VISIBLE
            binding.planProgressLoading.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        planProgressBinding = null
    }

    private val chartListStateChangeListener = object : View.OnAttachStateChangeListener {
        override fun onViewAttachedToWindow(v: View?) {
            v?.let {
                (it as RecyclerView).adapter = adapter
            }
        }

        override fun onViewDetachedFromWindow(v: View?) {
            v?.let {
                (it as RecyclerView).adapter = null
            }
        }
    }

    private suspend fun getChartList(plan: StudyPlan): List<ChartItem> {
        val dailyPercentage = viewModel.getDailyPercentage(plan)
        val totalPercentage = viewModel.getTotalPercentage(plan)
        val charList = mutableListOf<ChartItem>()
        dailyPercentage?.let {
            charList.add(
                ChartItem(
                    ChartItem.Type.BAR,
                    generateDataBar(
                        it[0],
                        it[1],
                        it[2],
                        it[3]
                    )
                ),
            )
        }
        charList.add(
            ChartItem(
                ChartItem.Type.PIE,
                generateDataPie(
                    totalPercentage.first,
                    totalPercentage.second,
                    totalPercentage.third
                )
            )
        )
        return charList
    }

    private fun generateDataBar(
        learned: Float, chosen: Float, typed: Float, spelled: Float
    ): BarData {
        val entries = listOf(
            BarEntry(0f, spelled),
            BarEntry(1f, typed),
            BarEntry(2f, chosen),
            BarEntry(3f, learned)
        )

        val dataSet = BarDataSet(entries, "")
        val colorList = getColorsForBarChart()
        dataSet.colors = colorList
        dataSet.highLightAlpha = 255
        val barData = BarData(dataSet)
        barData.setDrawValues(false)
        barData.isHighlightEnabled = false
        return barData
    }

    private fun generateDataPie(remember: Float, revise: Float, notLearned: Float): PieData {
        val entries = listOf(
            PieEntry(remember),
            PieEntry(revise),
            PieEntry(notLearned)
        )

        val pieDataSet = PieDataSet(entries, "")
        pieDataSet.sliceSpace = 2f
        pieDataSet.colors = getColorsForPieChart()
        pieDataSet.isHighlightEnabled = false

        return PieData(pieDataSet)
    }

    private fun getColorsForBarChart(): List<Int> {
        return if (isDarkMode) {
            listOf(
                ColorTemplate.rgb("#e2e281"),
                ColorTemplate.rgb("#6cbc8d"),
                ColorTemplate.rgb("#80cccc"),
                ColorTemplate.rgb("#53a8cc")
            )
        } else {
            listOf(
                ColorTemplate.rgb("#ffd600"),
                ColorTemplate.rgb("#15ad61"),
                ColorTemplate.rgb("#59dfd4"),
                ColorTemplate.rgb("#026ba9")
            )
        }
    }

    private fun getColorsForPieChart(): List<Int> {
        return if (isDarkMode) {
            listOf(
                ColorTemplate.rgb("#009a44"),
                ColorTemplate.rgb("#e5e56c"),
                ColorTemplate.rgb("#ff7e45"),
                ColorTemplate.rgb("#548ecc")
            )
        } else {
            listOf(
                ColorTemplate.rgb("#2ecc71"),
                ColorTemplate.rgb("#f1c40f"),
                ColorTemplate.rgb("#e74c3c"),
                ColorTemplate.rgb("#3498db")
            )
        }
    }
}
