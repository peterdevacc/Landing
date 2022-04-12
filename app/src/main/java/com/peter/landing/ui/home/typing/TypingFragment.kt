package com.peter.landing.ui.home.typing

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.annotation.ColorInt
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.peter.landing.R
import com.peter.landing.databinding.FragmentTypingBinding
import com.peter.landing.domain.study.StudySection
import com.peter.landing.ui.common.adapter.ExplainItem
import com.peter.landing.ui.common.showExplainZoomDialog
import com.peter.landing.ui.util.Sound
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TypingFragment : Fragment() {
    private var typingBinding: FragmentTypingBinding? = null
    private val binding get() = typingBinding!!
    private val viewModel: TypingViewModel by viewModels()
    private lateinit var navController: NavController
    private lateinit var sound: Sound

    @ColorInt
    private var correctColor: Int = 0
    @ColorInt
    private var wrongColor: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        typingBinding = FragmentTypingBinding.inflate(
            layoutInflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        binding.typingBottomButtonGroup.leftButton.text = getString(R.string.submit)

        val inputHint = getString(R.string.spell_input_hint)
        binding.typingKeyboard.setInputTarget(binding.typingInput, inputHint)

        viewModel.setStudyState(true)

        sound = Sound(view.context)

        correctColor = requireContext().getColor(R.color.color_correct)
        wrongColor = requireContext().getColor(R.color.color_wrong)

        viewLifecycleOwner.lifecycleScope.launch {
            initTyping()
        }

        viewModel.currentTypingWord.observe(viewLifecycleOwner) { word ->
            viewModel.setAnswer(word.spelling)
            binding.typingResult.text = ""
            binding.typingResetButton.isVisible = false

            binding.typingWordSpelling.text = word.spelling
            binding.typingIpa.text = word.ipa
            binding.typingCnZoomButton.setOnClickListener {
                val cnExplain = ExplainItem.getWordExplainSingleType(word.cn)
                showExplainZoomDialog(
                    word.spelling,
                    ExplainItem.addItemLanguageTypeHeaderCN(cnExplain),
                    childFragmentManager
                )
            }
            binding.typingEnZoomButton.setOnClickListener {
                val enExplain = ExplainItem.getWordExplainSingleType(word.en)
                showExplainZoomDialog(
                    word.spelling,
                    ExplainItem.addItemLanguageTypeHeaderEN(enExplain),
                    childFragmentManager
                )
            }

            binding.typingBottomButtonGroup.middleFab.setOnClickListener {
                sound.playAudio(word.pronName)
            }

            val currentNum = viewModel.getCurrentNum()
            binding.typingCounter.currentNum.text = currentNum.toString()
            if (viewModel.isLastWord()) {
                binding.typingBottomButtonGroup.rightButton.text = getString(R.string.complete)
            } else {
                binding.typingBottomButtonGroup.rightButton.text = getString(R.string.next_one)
            }
            binding.typingBottomButtonGroup.leftButton.isEnabled = true
            binding.typingBottomButtonGroup.rightButton.isEnabled = false
            binding.typingInput.textSize = 18f
            binding.typingInput.text = getString(R.string.spell_input_hint)
            binding.typingInput.setTextColor(
                requireContext().getColor(R.color.color_on_primary_fade)
            )
            binding.typingKeyboard.setEnable(true)
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
        super.onDestroyView()
        typingBinding = null
    }

    private suspend fun initTyping() {
        viewModel.initTodayTyping()

        binding.typingCounter.totalNum.text = viewModel.getTotalNum().toString()
        binding.typingResetButton.setOnClickListener(resetListener)
        binding.typingBottomButtonGroup.leftButton.setOnClickListener(submitListener)
        binding.typingBottomButtonGroup.rightButton.setOnClickListener(rightListener)

        binding.typingLoading.visibility = View.GONE
        binding.typingGroup.visibility = View.VISIBLE
    }

    private val submitListener = View.OnClickListener {
        binding.typingKeyboard.setEnable(false)
        binding.typingBottomButtonGroup.leftButton.isEnabled = false

        if (viewModel.typing(binding.typingInput.text.toString())) {
            binding.typingResult.text = getString(R.string.spell_correct)
            binding.typingResult.setTextColor(correctColor)
            binding.typingBottomButtonGroup.rightButton.isEnabled = true
        } else {
            binding.typingResult.text = getString(R.string.spell_wrong)
            binding.typingResult.setTextColor(wrongColor)
            if (binding.typingInput.text == getString(R.string.typing_input_hint)) {
                binding.typingInput.text = ""
            }
            binding.typingResetButton.isVisible = true
        }
    }

    private val rightListener = View.OnClickListener {
        if (viewModel.isLastWord()) {
            findNavController().navigate(
                    TypingFragmentDirections
                            .actionTypingFragmentToStateFragment(StudySection.TYPING)
            )
        } else {
            viewModel.getNextWord()
        }
    }

    private val resetListener = View.OnClickListener {
        binding.typingResult.text = ""
        binding.typingInput.textSize = 18f
        binding.typingInput.text = getString(R.string.spell_input_hint)
        binding.typingInput.setTextColor(
                requireContext().getColor(R.color.color_on_primary_fade)
        )
        binding.typingKeyboard.setEnable(true)
        binding.typingBottomButtonGroup.leftButton.isEnabled = true
        binding.typingResetButton.isVisible = false
    }

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            navController.popBackStack(R.id.home_fragment, false)
        }
    }

}