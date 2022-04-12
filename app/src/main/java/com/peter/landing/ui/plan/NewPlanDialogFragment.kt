package com.peter.landing.ui.plan

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.peter.landing.R
import com.peter.landing.databinding.ComponentBottomSheetNewPlanBinding
import com.peter.landing.ui.plan.adapter.VocabularyAdapter
import com.peter.landing.ui.plan.adapter.WordListNumAdapter
import com.peter.landing.ui.plan.adapter.WordListNumViewHolder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class NewPlanDialogFragment : BottomSheetDialogFragment() {

    private var newPlanBinding: ComponentBottomSheetNewPlanBinding? = null
    private val binding get() = newPlanBinding!!
    private val viewModel: NewPlanViewModel by viewModels()
    private val vocabularyAdapter = VocabularyAdapter()
    private val wordListNumAdapter = WordListNumAdapter()

    private var onDismissCallback: (() -> Unit)? = null

    fun setOnDismissCallback(onDismissCallback: () -> Unit) {
        this.onDismissCallback = onDismissCallback
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        newPlanBinding = ComponentBottomSheetNewPlanBinding
            .inflate(layoutInflater, container, false)

        binding.newPlanVocabularyList.layoutManager = LinearLayoutManager(
            context, LinearLayoutManager.HORIZONTAL, false
        )
        val pagerSnapHelper = PagerSnapHelper()
        pagerSnapHelper.attachToRecyclerView(binding.newPlanVocabularyList)
        binding.newPlanVocabularyList.adapter = vocabularyAdapter

        binding.newPlanWordListNumList.layoutManager = LinearLayoutManager(
            context, LinearLayoutManager.HORIZONTAL, false
        )
        wordListNumAdapter.setSystemDensity(resources.displayMetrics.densityDpi)
        binding.newPlanWordListNumList.addItemDecoration(wordListNumAdapter.itemSpaceDecor)
        binding.newPlanWordListNumList.adapter = wordListNumAdapter

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.initVocabularyList()
            vocabularyAdapter.setData(viewModel.getVocabularyList())
            vocabularyAdapter.setItemListener(vocabularyListener)
        }

        binding.newPlanStartDateRadioGroup.setOnCheckedChangeListener(dateListener)

        val entryList = resources.getStringArray(R.array.word_list_num_entries).toList()
        val valueList = resources.getIntArray(R.array.word_list_num_entry_values).toList()
        wordListNumAdapter.setData(entryList.zip(valueList).map {
            Pair(it.first, it.second)
        })
        wordListNumAdapter.setItemListener(wordListNumListener)

        binding.newPlanCancelButton.setOnClickListener {
            dismiss()
        }
        binding.newPlanCompleteButton.setOnClickListener(completeListener)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        newPlanBinding = null
    }

    override fun onDismiss(dialog: DialogInterface) {
        onDismissCallback?.invoke()
        super.onDismiss(dialog)
    }

    private val completeListener = View.OnClickListener {
        if (!(binding.newPlanTodayRadioButton.isChecked ||
                binding.newPlanTomorrowRadioButton.isChecked)) {
            binding.newPlanErrorMsg.text = getString(R.string.new_plan_date_toast)
            return@OnClickListener
        }
        if (viewModel.isPlanVocabularyExist()) {
            binding.newPlanErrorMsg.text = getString(R.string.new_plan_vocabulary_toast)
            return@OnClickListener
        }
        if (viewModel.getPlanWordListSize() == 0) {
            binding.newPlanErrorMsg.text = getString(R.string.new_plan_words_num_toast)
            return@OnClickListener
        }
        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.setPlanStartDate()
            viewModel.addPlan()
        }

        dismiss()
    }

    private val dateListener = RadioGroup.OnCheckedChangeListener { _, checkedId ->
        when (checkedId) {
            R.id.new_plan_today_radio_button -> {
                viewModel.setPlanStartToday(true)
                viewModel.setPlanStartDate()
                binding.newPlanEndDate.text = viewModel.getPlanEndDate()
            }
            R.id.new_plan_tomorrow_radio_button -> {
                viewModel.setPlanStartToday(false)
                viewModel.setPlanStartDate()
                binding.newPlanEndDate.text = viewModel.getPlanEndDate()
            }
        }
    }

    private val vocabularyListener = View.OnClickListener {
        val viewHolder = it.tag as RecyclerView.ViewHolder
        viewModel.setPlanVocabulary(viewHolder.bindingAdapterPosition)
        binding.newPlanVocabularyChosen.text = viewModel.getPlanVocabularyName()
        binding.newPlanEndDate.text = viewModel.getPlanEndDate()
    }

    private val wordListNumListener = View.OnClickListener {
        val viewHolder = it.tag as WordListNumViewHolder
        viewModel.setPlanWordListSize(viewHolder.wordListNumValue)
        binding.newPlanWordListNum.text = viewHolder.wordListNumEntry
        binding.newPlanEndDate.text = viewModel.getPlanEndDate()
    }
}