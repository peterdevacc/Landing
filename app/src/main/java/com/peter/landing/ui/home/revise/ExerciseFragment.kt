package com.peter.landing.ui.home.revise

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
import com.peter.landing.databinding.FragmentExerciseBinding
import com.peter.landing.domain.study.StudySection
import com.peter.landing.ui.common.adapter.ExplainAdapter
import com.peter.landing.ui.common.adapter.ExplainItem
import com.peter.landing.ui.common.adapter.ExplainViewHolder
import com.peter.landing.ui.util.Sound
import com.peter.landing.ui.util.listStateChangeListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ExerciseFragment : Fragment() {

    private var exerciseBinding: FragmentExerciseBinding? = null
    private val binding get() = exerciseBinding!!
    private val viewModel: ExerciseViewModel by viewModels()
    private lateinit var navController: NavController
    private val explainAdapter = ExplainAdapter()
    private lateinit var sound: Sound

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        exerciseBinding = FragmentExerciseBinding.inflate(
            layoutInflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.exerciseButtonSection.leftButton.visibility = View.GONE
        binding.exerciseButtonSection.rightButton.isEnabled = false

        sound = Sound(view.context)

        navController = findNavController()
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        explainAdapter.setSystemDensity(resources.displayMetrics.densityDpi)
        binding.exerciseView.adapter = explainAdapter
        binding.exerciseView.addItemDecoration(explainAdapter.itemSpaceDecor)
        binding.exerciseView.addOnAttachStateChangeListener(listStateChangeListener)

        viewLifecycleOwner.lifecycleScope.launch {
            initRevise()
        }

        viewModel.currentWrongAndWord.observe(viewLifecycleOwner) { wrongAndWord ->
            val currentNum = viewModel.getCurrentNum()
            binding.exerciseCounter.currentNum.text = currentNum.toString()

            val explain = ExplainItem.getWordExplain(wrongAndWord.word.cn, wrongAndWord.word.en)
            val explainWithExercise =
                ExplainItem.addWordRearrangeExercise(explain, wrongAndWord.word.spelling)
            explainAdapter.setData(explainWithExercise, true)
            binding.exerciseButtonSection.middleFab.setOnClickListener {
                sound.playAudio(wrongAndWord.word.pronName)
            }

            if (viewModel.isLastWord()) {
                binding.exerciseButtonSection.rightButton.text = getString(R.string.complete)
            } else {
                binding.exerciseButtonSection.rightButton.text = getString(R.string.next_one)
            }

            viewModel.setArranged(false)

            binding.exerciseLoading.visibility = View.GONE
            binding.exerciseContent.visibility = View.VISIBLE
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            if (callback.isEnabled) {
                navController.popBackStack(R.id.home_fragment, false)
            } else {
                navController.navigateUp()
            }
        }
        return true
    }

    override fun onResume() {
        super.onResume()
        viewLifecycleOwner.lifecycle.addObserver(sound)
    }

    override fun onStop() {
        super.onStop()
        viewLifecycleOwner.lifecycle.removeObserver(sound)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        exerciseBinding = null
    }

    private suspend fun initRevise() {
        viewModel.initTodayRevise()
        binding.exerciseCounter.totalNum.text = viewModel.getTotalNum().toString()
        explainAdapter.addRearrangeExercise(
            submitListener = exerciseSubmitListener,
            resetListener = exerciseResetListener
        )
        binding.exerciseButtonSection.rightButton.setOnClickListener {
            if (viewModel.isLastWord()) {
                findNavController().navigate(
                    ExerciseFragmentDirections
                        .actionExerciseFragmentToStateFragment(StudySection.REVISE)
                )
            } else {
                binding.exerciseButtonSection.rightButton.isEnabled = false
                viewModel.getNextWord()
                binding.exerciseView.scrollToPosition(0)
            }
        }
    }

    private val exerciseSubmitListener = View.OnClickListener { view ->
        val viewHolder = view.tag as ExplainViewHolder.ItemWordRearrangeExerciseViewHolder
        viewHolder.binding.rearrangeDelButton.isEnabled = false
        viewHolder.adapter.checkData()
        viewHolder.binding.rearrangeSpellingAnswer.visibility = View.VISIBLE
        val input = viewHolder.binding.rearrangeInput.text.toString()
        if (viewModel.isCorrect(input)) {
            viewHolder.binding.rearrangeTips.text =
                getString(R.string.rearrange_spelling_correct)
            if (!viewModel.isArranged()) {
                viewModel.revised()
                viewModel.setArranged(true)
                binding.exerciseButtonSection.rightButton.isEnabled = true
                if (viewModel.isLastWord()) {
                    callback.isEnabled = true
                }
            }
        } else {
            viewHolder.binding.rearrangeTips.text =
                getString(R.string.rearrange_spelling_wrong)
            val wrongColor = requireContext().getColor(R.color.color_wrong)
            viewHolder.binding.rearrangeInput.setTextColor(wrongColor)
        }
        viewHolder.binding.submitButton.isEnabled = false
    }

    private val exerciseResetListener = View.OnClickListener {
        val viewHolder = it.tag as ExplainViewHolder.ItemWordRearrangeExerciseViewHolder
        viewHolder.binding.rearrangeSpellingAnswer.visibility = View.GONE
        viewHolder.adapter.resetData()
        viewHolder.binding.rearrangeTips.text =
            getString(R.string.rearrange_spelling_tips)
        viewHolder.binding.rearrangeInput.text = ""
        val textDefaultColor = requireContext().getColor(R.color.color_on_surface)
        viewHolder.binding.rearrangeInput.setTextColor(textDefaultColor)
        viewHolder.binding.rearrangeDelButton.isEnabled = true
        viewHolder.binding.submitButton.isEnabled = true
    }

    private val callback = object : OnBackPressedCallback(false) {
        override fun handleOnBackPressed() {
            navController.popBackStack(R.id.home_fragment, false)
        }
    }

}
