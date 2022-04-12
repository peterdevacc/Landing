package com.peter.landing.ui.plan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.google.android.material.tabs.TabLayoutMediator
import com.peter.landing.R
import com.peter.landing.databinding.FragmentPlanBinding
import com.peter.landing.ui.plan.adapter.PlanTabAdapter
import com.peter.landing.ui.util.isDarkTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PlanFragment : Fragment() {

    private var planBinding: FragmentPlanBinding? = null
    private val binding get() = planBinding!!
    private val viewModel: PlanViewModel by viewModels()
    private var tabLayoutMediator: TabLayoutMediator? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        planBinding = FragmentPlanBinding
            .inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        if (isDarkTheme()) {
            binding.planEmptyImg.setImageResource(R.drawable.empty_img_dark)
        } else {
            binding.planEmptyImg.setImageResource(R.drawable.empty_img_light)
        }

        viewModel.studyPlan.observe(viewLifecycleOwner) {
            if (it != null) {
                val tabListEntries = resources.getStringArray(R.array.plan_tab_list)
                val adapter = PlanTabAdapter(this, tabListEntries.size, it)
                binding.planPager.adapter = adapter

                tabLayoutMediator = TabLayoutMediator(
                    binding.planTabs, binding.planPager
                ) { tab, position ->
                    tab.text = tabListEntries[position]
                }
                tabLayoutMediator?.attach()

                binding.planEmptyMsg.visibility = View.GONE
                binding.planEmptyCard.visibility = View.GONE
                binding.planTabs.visibility = View.VISIBLE
                binding.planPager.visibility = View.VISIBLE
                binding.planNewButton.visibility = View.GONE
            } else {
                tabLayoutMediator?.detach()
                binding.planPager.adapter = null

                binding.planTabs.visibility = View.GONE
                binding.planPager.visibility = View.GONE
                binding.planEmptyCard.visibility = View.VISIBLE
                binding.planEmptyMsg.visibility = View.VISIBLE
                binding.planNewButton.setOnClickListener { newPlanButton ->
                    newPlanButton.isEnabled = false
                    val dialog = NewPlanDialogFragment()
                    dialog.setOnDismissCallback {
                        newPlanButton.isEnabled = true
                    }
                    dialog.show(childFragmentManager, "NewPlanDialogFragment")
                }
                binding.planNewButton.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        tabLayoutMediator = null
        planBinding = null
    }

}
