package com.peter.landing.ui.plan

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.peter.landing.R
import com.peter.landing.data.local.plan.StudyPlan
import com.peter.landing.databinding.FragmentPlanDetailBinding
import com.peter.landing.ui.util.isDarkTheme
import com.peter.landing.util.getDate
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class PlanDetailFragment constructor(
    private val plan: StudyPlan
) : Fragment() {

    private var planDetailBinding: FragmentPlanDetailBinding? = null
    private val binding get() = planDetailBinding!!
    private lateinit var viewModel: PlanViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        planDetailBinding = FragmentPlanDetailBinding
            .inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = parentFragment?.run {
            ViewModelProvider(this)[PlanViewModel::class.java]
        } ?: throw Exception("Invalid parentFragment")

        if (isDarkTheme()) {
            binding.planDetailImg.setImageResource(R.drawable.plan_page_exist_dark)
        } else {
            binding.planDetailImg.setImageResource(R.drawable.plan_page_exist_light)
        }

        viewLifecycleOwner.lifecycleScope.launch {
            binding.planDetailStartDate.text = getDate(plan.startDate!!.time)
            binding.planDetailVocabulary.text = viewModel.getPlanVocabularyStrName(plan)
            val numStr = "${plan.wordListSize}个"
            binding.planDetailWordListSize.text = numStr
            binding.planDetailEndDate.text = viewModel.getPlanEndDate(plan)
            binding.planDetailContainer.visibility = View.VISIBLE

            binding.planDetailDeleteButton.setOnClickListener(deletePlanListener)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        planDetailBinding = null
    }

    private val deletePlanListener = View.OnClickListener {
        val alertDialog: AlertDialog? = activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setMessage(R.string.plan_delete_msg)
                setPositiveButton(R.string.confirm) { _, _ ->
                    viewModel.deletePlan()
                }
                setNegativeButton(R.string.cancel) { _, _ -> }
                setOnDismissListener { }
            }
            builder.create()
        }
        alertDialog?.show()
    }

}
