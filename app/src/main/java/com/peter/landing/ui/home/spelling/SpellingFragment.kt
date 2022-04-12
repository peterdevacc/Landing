package com.peter.landing.ui.home.spelling

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
import androidx.transition.TransitionManager
import com.peter.landing.R
import com.peter.landing.databinding.FragmentSpellingBinding
import com.peter.landing.domain.study.StudySection
import com.peter.landing.ui.common.FADE_DURATION
import com.peter.landing.ui.common.adapter.ExplainAdapter
import com.peter.landing.ui.common.adapter.ExplainItem
import com.peter.landing.ui.common.fadeThrough
import com.peter.landing.ui.common.showExplainZoomDialog
import com.peter.landing.ui.util.Sound
import com.peter.landing.ui.util.listStateChangeListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SpellingFragment : Fragment() {

    private var spellingBinding: FragmentSpellingBinding? = null
    private val binding get() = spellingBinding!!
    private val viewModel: SpellingViewModel by viewModels()
    private lateinit var navController: NavController
    private val fadeThrough = fadeThrough()
    private val cnExplainAdapter = ExplainAdapter()
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
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        spellingBinding = FragmentSpellingBinding.inflate(
            layoutInflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        binding.spellBottomButtonGroup.leftButton.text = getString(R.string.submit)

        binding.spellQuestion.itemExplainTitle.text = getString(R.string.language_cn)

        cnExplainAdapter.setSystemDensity(resources.displayMetrics.densityDpi)
        binding.spellQuestion.itemExplainList.adapter = cnExplainAdapter
        binding.spellQuestion.itemExplainList.addItemDecoration(cnExplainAdapter.itemSpaceDecor)
        binding.spellQuestion.itemExplainList.addOnAttachStateChangeListener(listStateChangeListener)

        val inputHint = getString(R.string.spell_input_hint)
        binding.spellKeyboard.setInputTarget(binding.spellInput, inputHint)

        viewModel.setStudyState(true)

        sound = Sound(view.context)

        correctColor = requireContext().getColor(R.color.color_correct)
        wrongColor = requireContext().getColor(R.color.color_wrong)

        viewLifecycleOwner.lifecycleScope.launch {
            initSpelling()
        }

        viewModel.currentSpellWord.observe(viewLifecycleOwner) { word ->
            TransitionManager.beginDelayedTransition(
                binding.spellQuestionPart,
                fadeThrough.setDuration(FADE_DURATION)
            )
            binding.spellGlHorizontalA.setGuidelinePercent(0.12f)
            binding.spellAnswerPart.visibility = View.GONE
            binding.spellTips.visibility = View.VISIBLE
            binding.spellWord.text = word.spelling
            binding.spellIpa.text = word.ipa
            val cnExplain = ExplainItem.getWordExplainSingleType(word.cn)
            cnExplainAdapter.setData(cnExplain)
            binding.spellQuestion.itemExplainZoomButton.setOnClickListener {
                showExplainZoomDialog(
                    word.spelling,
                    ExplainItem.addItemLanguageTypeHeaderCN(cnExplain),
                    childFragmentManager
                )
            }
            viewModel.setAnswer(word.spelling)

            binding.spellBottomButtonGroup.middleFab.setOnClickListener {
                sound.playAudio(word.pronName)
            }

            val currentNum = viewModel.getCurrentNum()
            binding.spellCounter.currentNum.text = currentNum.toString()
            if (viewModel.isLastWord()) {
                binding.spellBottomButtonGroup.rightButton.text = getString(R.string.complete)
            } else {
                binding.spellBottomButtonGroup.rightButton.text = getString(R.string.next_one)
            }

            binding.spellBottomButtonGroup.leftButton.isEnabled = true
            binding.spellBottomButtonGroup.rightButton.isEnabled = false
            binding.spellBottomButtonGroup.middleFab.visibility = View.INVISIBLE
            binding.spellInput.textSize = 18f
            binding.spellInput.text = inputHint
            binding.spellInput.setTextColor(
                requireContext().getColor(R.color.color_on_primary_fade)
            )
            binding.spellKeyboard.setEnable(true)
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
        spellingBinding = null
    }

    private suspend fun initSpelling() {
        viewModel.initTodaySpelling()

        binding.spellCounter.totalNum.text = viewModel.getTotalNum().toString()
        binding.spellBottomButtonGroup.leftButton.setOnClickListener(submitListener)
        binding.spellBottomButtonGroup.rightButton.setOnClickListener(rightListener)
        binding.spellLoading.visibility = View.GONE
        binding.spellGroup.visibility = View.VISIBLE
    }

    private val submitListener = View.OnClickListener {
        binding.spellGlHorizontalA.setGuidelinePercent(0.23f)

        TransitionManager.beginDelayedTransition(
            binding.spellQuestionPart,
            fadeThrough.setDuration(FADE_DURATION)
        )
        binding.spellAnswerPart.visibility = View.VISIBLE
        binding.spellTips.visibility = View.GONE
        binding.spellBottomButtonGroup.middleFab.visibility = View.VISIBLE

        if (viewModel.spell(binding.spellInput.text.toString())) {
            binding.spellResult.text = getString(R.string.spell_correct)
            binding.spellResult.setTextColor(correctColor)
        } else {
            binding.spellResult.text = getString(R.string.spell_wrong)
            binding.spellResult.setTextColor(wrongColor)
        }

        binding.spellKeyboard.setEnable(false)
        binding.spellBottomButtonGroup.leftButton.isEnabled = false
        binding.spellBottomButtonGroup.rightButton.isEnabled = true
    }

    private val rightListener = View.OnClickListener {
        if (viewModel.isLastWord()) {
            findNavController().navigate(
                    SpellingFragmentDirections
                            .actionSpellingFragmentToStateFragment(StudySection.SPELLING)
            )
        } else {
            viewModel.getNextWord()
            binding.spellQuestion.itemExplainList.smoothScrollToPosition(0)
        }
    }

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            navController.popBackStack(R.id.home_fragment, false)
        }
    }
}
