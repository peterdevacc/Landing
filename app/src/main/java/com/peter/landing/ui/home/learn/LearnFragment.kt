package com.peter.landing.ui.home.learn

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import com.peter.landing.R
import com.peter.landing.databinding.FragmentLearnBinding
import com.peter.landing.domain.study.StudySection
import com.peter.landing.ui.common.adapter.ExplainAdapter
import com.peter.landing.ui.common.adapter.ExplainItem
import com.peter.landing.ui.common.showExplainZoomDialog
import com.peter.landing.ui.util.Sound
import com.peter.landing.ui.util.listStateChangeListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class LearnFragment : Fragment() {

    private var learnBinding: FragmentLearnBinding? = null
    private val binding get() = learnBinding!!
    private val viewModel: LearnViewModel by viewModels()
    private lateinit var navController: NavController
    private val cnExplainAdapter = ExplainAdapter()
    private val enExplainAdapter = ExplainAdapter()
    private lateinit var sound: Sound

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        learnBinding = FragmentLearnBinding.inflate(
            layoutInflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        navController = findNavController()
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        viewModel.setStudyState(true)

        sound = Sound(view.context)

        viewLifecycleOwner.lifecycleScope.launch {
            initLearn()
        }

        binding.learnWordExplain.cnExplain.itemExplainTitle.text = getString(R.string.language_cn)
        binding.learnWordExplain.enExplain.itemExplainTitle.text = getString(R.string.language_en)

        cnExplainAdapter.setSystemDensity(resources.displayMetrics.densityDpi)
        enExplainAdapter.setSystemDensity(resources.displayMetrics.densityDpi)
        binding.learnWordExplain.cnExplain.itemExplainList.adapter = cnExplainAdapter
        binding.learnWordExplain.cnExplain.itemExplainList.addItemDecoration(cnExplainAdapter.itemSpaceDecor)
        binding.learnWordExplain.cnExplain.itemExplainList.addOnAttachStateChangeListener(listStateChangeListener)
        binding.learnWordExplain.enExplain.itemExplainList.adapter = enExplainAdapter
        binding.learnWordExplain.enExplain.itemExplainList.addItemDecoration(enExplainAdapter.itemSpaceDecor)
        binding.learnWordExplain.enExplain.itemExplainList.addOnAttachStateChangeListener(listStateChangeListener)

        viewModel.currentLearnWord.observe(viewLifecycleOwner) { word ->
            val cnExplain = ExplainItem.getWordExplainSingleType(word.cn)
            cnExplainAdapter.setData(cnExplain)
            binding.learnWordExplain.cnExplain.itemExplainZoomButton.setOnClickListener {
                showExplainZoomDialog(
                    word.spelling,
                    ExplainItem.addItemLanguageTypeHeaderCN(cnExplain),
                    childFragmentManager
                )
            }
            val enExplain = ExplainItem.getWordExplainSingleType(word.en)
            enExplainAdapter.setData(enExplain)
            binding.learnWordExplain.enExplain.itemExplainZoomButton.setOnClickListener {
                showExplainZoomDialog(
                    word.spelling,
                    ExplainItem.addItemLanguageTypeHeaderEN(enExplain),
                    childFragmentManager
                )
            }
            binding.learnWordExplain.spelling.text = word.spelling
            binding.learnWordExplain.ipa.text = word.ipa

            val currentNum = viewModel.getCurrentNum()
            binding.learnCounter.currentNum.text = currentNum.toString()
            if (currentNum > 1) {
                binding.learnButtonSection.leftButton.visibility = View.VISIBLE
            } else {
                binding.learnButtonSection.leftButton.visibility = View.INVISIBLE
            }

            binding.learnButtonSection.middleFab.setOnClickListener {
                sound.playAudio(word.pronName)
            }

            if (viewModel.isLastWord()) {
                binding.learnButtonSection.rightButton.text = getString(R.string.complete)
            } else {
                binding.learnButtonSection.rightButton.text = getString(R.string.next_one)
            }

            binding.learnButtonSection.rightButton.isEnabled = true
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
        learnBinding = null
    }

    private suspend fun initLearn() {
        viewModel.initTodayLearn()
        binding.learnCounter.totalNum.text = viewModel.getTotalNum().toString()
        binding.learnButtonSection.leftButton.setOnClickListener {
            viewModel.getPreviousWord()
        }
        binding.learnButtonSection.rightButton.setOnClickListener {
            binding.learnButtonSection.rightButton.isEnabled = false
            if (viewModel.isLastWord()) {
                viewModel.learn()
                findNavController().navigate(
                    LearnFragmentDirections
                        .actionLearnFragmentToStateFragment(StudySection.LEARN)
                )
            } else {
                viewModel.getNextWord()
                if (viewModel.isCurrentPositionBiggerThanLearned()) {
                    viewModel.learn()
                }
            }
        }
        binding.learnLoading.visibility = View.GONE
        binding.learnGroup.visibility = View.VISIBLE
    }

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            navController.popBackStack(R.id.home_fragment, false)
        }
    }
}
