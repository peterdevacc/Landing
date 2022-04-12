package com.peter.landing.ui.plan.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.peter.landing.data.local.plan.StudyPlan
import com.peter.landing.ui.plan.PlanDetailFragment
import com.peter.landing.ui.plan.PlanProgressFragment

class PlanTabAdapter(
    fragment: Fragment,
    private val size: Int,
    private val plan: StudyPlan
) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int {
        return size
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> PlanProgressFragment(plan)
            else -> PlanDetailFragment(plan)
        }
    }

}