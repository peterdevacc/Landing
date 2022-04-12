package com.peter.landing.ui.home.state

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.peter.landing.R
import com.peter.landing.data.local.wrong.WrongWord
import com.peter.landing.databinding.FragmentStateBinding
import com.peter.landing.domain.study.StudySection
import com.peter.landing.ui.common.showDefinitionZoomDialog
import com.peter.landing.ui.home.state.adapter.DailyWrongWordAdapter
import com.peter.landing.ui.home.state.adapter.DailyWrongWordViewHolder
import com.peter.landing.ui.util.listStateChangeListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class StateFragment : Fragment() {

    private var stateBinding: FragmentStateBinding? = null
    private val binding get() = stateBinding!!
    private val viewModel: StateViewModel by viewModels()
    private val safeArgs: StateFragmentArgs by navArgs()
    private lateinit var navController: NavController
    private lateinit var adapter: DailyWrongWordAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        stateBinding = FragmentStateBinding.inflate(
            layoutInflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val notedColor = requireContext().getColor(R.color.color_correct)
        val unNotedColor = requireContext().getColor(R.color.color_wrong)
        adapter = DailyWrongWordAdapter(notedColor, unNotedColor)
        adapter.setSystemDensity(resources.displayMetrics.densityDpi)
        adapter.setListener(noteListener, detailListener)
        binding.stateExamCardWrongList.setHasFixedSize(true)
        binding.stateExamCardWrongList.addItemDecoration(adapter.itemSpaceDecor)
        binding.stateExamCardWrongList.adapter = adapter
        binding.stateExamCardWrongList.addOnAttachStateChangeListener(listStateChangeListener)

        navController = findNavController()
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)

        viewModel.setStudyState(true)

        uiNavigation(safeArgs.studySection)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            navController.popBackStack(R.id.home_fragment, false)
        }
        return true
    }

    override fun onStop() {
        super.onStop()
        viewModel.setStudyState(false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        stateBinding = null
    }

    private val detailListener = View.OnClickListener { view ->
        val viewHolder = view.tag as DailyWrongWordViewHolder
        showDefinitionZoomDialog(viewHolder.wrongWord.word, childFragmentManager)
    }

    private val noteListener = View.OnClickListener { view ->
        val viewHolder = view.tag as DailyWrongWordViewHolder
        viewHolder.wrongWord.run {
            if (wrong.isNoted) {
                viewModel.removeNote(this.wrong.wordId)
            } else {
                viewModel.addNote(this.wrong.wordId)
            }
        }
    }

    private fun stateAllButton(allNoted: Boolean, wrongWordList: List<WrongWord>) {
        if (allNoted) {
            binding.stateExamCardWrongListAllButton.icon = ContextCompat
                .getDrawable(requireContext(), R.drawable.ic_add_30dp)
            binding.stateExamCardWrongListAllButton.setOnClickListener {
                viewModel.addNoteList(wrongWordList)
            }
        } else {
            binding.stateExamCardWrongListAllButton.icon = ContextCompat
                .getDrawable(requireContext(), R.drawable.ic_remove_30dp)
            binding.stateExamCardWrongListAllButton.setOnClickListener {
                viewModel.removeNoteList(wrongWordList)
            }
        }
    }

    private fun uiNavigation(studySection: StudySection) {
        when (studySection) {
            StudySection.LEARN -> {
                stateWorkCard(
                    getString(R.string.complete_learn),
                    getString(R.string.start_choice)
                ) {
                    findNavController().navigate(
                        StateFragmentDirections
                            .actionStateFragmentToChoiceFragment()
                    )
                }
            }
            StudySection.CHOICE -> {
                stateExamCard(
                    getString(R.string.complete_choice),
                    getString(R.string.start_typing)
                ) {
                    findNavController().navigate(
                        StateFragmentDirections
                            .actionStateFragmentToTypingFragment()
                    )
                }
            }
            StudySection.TYPING -> {
                stateWorkCard(
                    getString(R.string.complete_typing),
                    getString(R.string.start_spell)
                ) {
                    findNavController().navigate(
                        StateFragmentDirections
                            .actionStateFragmentToSpellingFragment()
                    )
                }
            }
            StudySection.SPELLING -> {
                stateExamCard(
                    getString(R.string.complete_spell),
                    getString(R.string.back_to_home_page)
                ) {
                    viewLifecycleOwner.lifecycleScope.launch {
                        viewModel.finishToday()
                    }
                    navController.popBackStack(R.id.home_fragment, false)
                }
            }
            StudySection.REVISE -> {
                stateWorkCard(
                    getString(R.string.complete_revise),
                    getString(R.string.back_to_home_page)
                ) {
                    navController.popBackStack(R.id.home_fragment, false)
                }
            }
        }
    }

    private fun stateWorkCard(
        cardTitleText: String,
        startButtonText: String,
        navigate: () -> Unit
    ) {
        binding.stateWorkCardTitle.text = cardTitleText
        binding.stateWorkCard.visibility = View.VISIBLE
        binding.stateWorkStartButton.text = startButtonText
        binding.stateWorkStartButton.visibility = View.VISIBLE
        binding.stateWorkStartButton.setOnClickListener {
            navigate()
        }
    }

    private fun stateExamCard(
        cardTitle: String,
        startButtonText: String,
        navigate: () -> Unit
    ) {
        val wrongWordList = viewModel.getTodayWrongAndWordList(safeArgs.studySection)
        wrongWordList.observe(viewLifecycleOwner) { list ->
            if (list.isEmpty()) {
                binding.stateWorkCardTitle.text = cardTitle
                binding.stateWorkCard.visibility = View.VISIBLE
                binding.stateWorkStartButton.text = startButtonText
                binding.stateWorkStartButton.visibility = View.VISIBLE
                binding.stateWorkStartButton.setOnClickListener {
                    navigate()
                }
            } else {
                binding.stateExamCardTitle.text = cardTitle
                val notNoted = list.find { !it.wrong.isNoted }
                stateAllButton(notNoted != null, list)
                adapter.setData(list)
                val title = "错词列表（共${list.size}个）"
                binding.stateExamCardWrongListTitle.text = title
                binding.stateExamCard.visibility = View.VISIBLE
                binding.stateExamStartButton.text = startButtonText
                binding.stateExamStartButton.visibility = View.VISIBLE
                binding.stateExamStartButton.setOnClickListener {
                    navigate()
                }
            }
        }
    }

    private val callback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            navController.popBackStack(R.id.home_fragment, false)
        }
    }
}
