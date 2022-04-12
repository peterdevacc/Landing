package com.peter.landing.ui.home.choice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.ColorInt
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.peter.landing.R
import com.peter.landing.databinding.FragmentChoiceBinding
import com.peter.landing.databinding.ItemChoiceBinding
import com.peter.landing.domain.study.StudySection
import com.peter.landing.ui.common.adapter.ExplainAdapter
import com.peter.landing.ui.common.adapter.ExplainItem
import com.peter.landing.ui.util.Sound
import com.peter.landing.ui.util.listStateChangeListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ChoiceFragment : Fragment() {

    private var choiceBinding: FragmentChoiceBinding? = null
    private val binding get() = choiceBinding!!
    private lateinit var navController: NavController
    private val viewModel: ChoiceViewModel by viewModels()
    private val adapterA = ExplainAdapter()
    private val adapterB = ExplainAdapter()
    private val adapterC = ExplainAdapter()
    private val adapterD = ExplainAdapter()
    private var btnList = mutableListOf<ItemChoiceBinding>()
    private val adapterList: List<ExplainAdapter> = listOf(adapterA, adapterB, adapterC, adapterD)

    @ColorInt
    private var correctColor: Int = 0

    @ColorInt
    private var wrongColor: Int = 0

    private lateinit var sound: Sound

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        choiceBinding = FragmentChoiceBinding.inflate(
            layoutInflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        binding.choiceButtonSection.leftButton.visibility = View.GONE

        correctColor = requireContext().getColor(R.color.color_correct)
        wrongColor = requireContext().getColor(R.color.color_wrong)

        btnList.add(binding.choiceA)
        btnList.add(binding.choiceB)
        btnList.add(binding.choiceC)
        btnList.add(binding.choiceD)
        btnList.withIndex().forEach {
            adapterList[it.index].setSystemDensity(resources.displayMetrics.densityDpi)
            it.value.itemChoiceList.adapter = adapterList[it.index]
            it.value.itemChoiceList.addItemDecoration(adapterList[it.index].itemSpaceDecor)
            it.value.itemChoiceList.addOnAttachStateChangeListener(listStateChangeListener)
        }

        viewModel.setStudyState(true)

        sound = Sound(view.context)

        viewLifecycleOwner.lifecycleScope.launch {
            initChoice()
        }

        val defaultColor = requireContext().getColor(R.color.color_surface)
        viewModel.currentChoiceWord.observe(viewLifecycleOwner) { word ->
            binding.tips.text = getString(R.string.choice_tips)
            viewModel.setOptions()
            val options = viewModel.getOptions()
            for ((index, option) in btnList.zip(options).withIndex()) {
                option.first.itemChoiceContainer.setBackgroundColor(defaultColor)
                adapterList[index].setData(ExplainItem.getWordExplainSingleType(option.second))
                when (index) {
                    0 -> option.first.itemChoiceTitle.text = "A"
                    1 -> option.first.itemChoiceTitle.text = "B"
                    2 -> option.first.itemChoiceTitle.text = "C"
                    3 -> option.first.itemChoiceTitle.text = "D"
                }
                option.first.root.isEnabled = true
            }
            binding.spelling.text = word.spelling
            binding.ipa.text = word.ipa

            binding.choiceButtonSection.middleFab.setOnClickListener {
                sound.playAudio(word.pronName)
            }

            val currentNum = viewModel.getCurrentNum()
            binding.choiceCounter.currentNum.text = currentNum.toString()
            binding.choiceButtonSection.rightButton.isEnabled = false
            if (viewModel.isLastWord()) {
                binding.choiceButtonSection.rightButton.text = getString(R.string.complete)
            } else {
                binding.choiceButtonSection.rightButton.text = getString(R.string.next_one)
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            navController.popBackStack(R.id.home_fragment, false)
        }
        return true
    }

    override fun onResume() {
        super.onResume()
        viewLifecycleOwner.lifecycle.addObserver(sound)
    }

    override fun onStop() {
        super.onStop()
        viewModel.setStudyState(false)
        viewLifecycleOwner.lifecycle.removeObserver(sound)
    }

    override fun onDestroyView() {
        btnList.clear()
        super.onDestroyView()
        choiceBinding = null
    }

    private suspend fun initChoice() {
        viewModel.initTodayChoice()
        binding.choiceCounter.totalNum.text = viewModel.getTotalNum().toString()

        binding.choiceA.root.setOnClickListener(chooseListener)
        binding.choiceB.root.setOnClickListener(chooseListener)
        binding.choiceC.root.setOnClickListener(chooseListener)
        binding.choiceD.root.setOnClickListener(chooseListener)

        binding.choiceButtonSection.rightButton.setOnClickListener {
            if (viewModel.isLastWord()) {
                findNavController().navigate(
                    ChoiceFragmentDirections
                        .actionChoiceFragmentToStateFragment(StudySection.CHOICE)
                )
            } else {
                viewModel.getNextWord()
            }
        }

        binding.choiceLoading.visibility = View.GONE
        binding.choiceGroup.visibility = View.VISIBLE
    }

    private val chooseListener = View.OnClickListener { optionView ->
        val answer = viewModel.getAnswer()
        val options = viewModel.getOptions()
        btnList.forEach {
            it.root.isEnabled = false
        }
        val chosenIndex = btnList.map { it.root }.indexOf(optionView)
        if (viewModel.choose(options[chosenIndex])) {
            binding.tips.text = getString(R.string.choice_correct)
        } else {
            binding.tips.text = getString(R.string.choice_wrong)
        }
        for ((index, button) in btnList.withIndex()) {
            if (options[index] == answer) {
                button.itemChoiceContainer.setBackgroundColor(correctColor)
            } else {
                button.itemChoiceContainer.setBackgroundColor(wrongColor)
            }
        }
        btnList[chosenIndex].itemChoiceTitle.text = getString(R.string.choice_chosen)
        binding.choiceButtonSection.rightButton.isEnabled = true
    }

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            navController.popBackStack(R.id.home_fragment, false)
        }
    }
}
