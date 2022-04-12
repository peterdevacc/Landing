package com.peter.landing.ui.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.peter.landing.R
import com.peter.landing.databinding.FragmentHomeBinding
import com.peter.landing.domain.study.PlanState
import com.peter.landing.domain.study.StudyProgressState
import com.peter.landing.domain.study.StudySection
import com.peter.landing.ui.MainActivity
import com.peter.landing.ui.util.*
import com.peter.landing.util.getTodayDateTime
import com.peter.landing.util.getTomorrowDateTime
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledFuture
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var homeBinding: FragmentHomeBinding? = null
    private val binding get() = homeBinding!!
    private val viewModel: HomeViewModel by viewModels()
    private val dateChangedTaskExecutor = Executors.newSingleThreadScheduledExecutor()
    private lateinit var dateChangedTask: ScheduledFuture<*>
    private var themeChecked = 2
    private var x1 = 0f
    private var x2 = 0f
    private var minDistance = 80f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeBinding = FragmentHomeBinding
            .inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        childFragmentManager.setFragmentResultListener(THEME_REQ, viewLifecycleOwner)
        { _, bundle ->
            val themeCheckedResult = bundle.getInt(THEME_CHECKED)
            themeChecked = themeCheckedResult
            val list = resources.getStringArray(R.array.theme_entry_values).toList()
            val theme = list[themeChecked]
            viewModel.saveCurrentTheme(theme)
            val mode = ThemeMode.valueOf(theme)
            setThemeMode(mode)
        }

        binding.homeLayout.setOnTouchListener(swipeOpenListener)
        binding.homeFab.setOnClickListener(fabListener)
        binding.homeReviseButton.setOnClickListener {
            findNavController().navigate(
                HomeFragmentDirections
                    .actionHomeFragmentToReviseFragment()
            )
        }

    }

    override fun onResume() {
        super.onResume()

        viewModel.refreshDate()

        dateChangedTask = dateChangedTaskExecutor.scheduleAtFixedRate({
            viewLifecycleOwner.lifecycleScope.launch {
                val learnState = viewModel.getCurrentStudyState()
                if (!learnState) {
                    viewModel.refreshDate()
                    configure()
                }
            }
        }, timeInMillisUntilTomorrow(), timeInMillisForWholeDay(), TimeUnit.MILLISECONDS)

        viewLifecycleOwner.lifecycleScope.launch {
            configure()
        }
    }

    override fun onStop() {
        super.onStop()
        dateChangedTask.cancel(false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        homeBinding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_options, menu)
        val themeMenu = menu.findItem(R.id.action_theme)
        themeMenu.isVisible = true

        viewLifecycleOwner.lifecycleScope.launch {
            val themePref = viewModel.getCurrentTheme()
            themeChecked = when (ThemeMode.valueOf(themePref)) {
                ThemeMode.LIGHT -> 0
                ThemeMode.DARK -> 1
                ThemeMode.DEFAULT -> 2
            }
            themeMenu.setOnMenuItemClickListener(themeListener)
        }

    }

    private suspend fun configure() {
        val planState = viewModel.initStudy()
        delay(MAGIC_HOME_TRANSITION_NUMBER)
        when (planState) {
            PlanState.NONE,
            PlanState.FINISHED, PlanState.COMPLETED_BUT_HAVE_REVISE_REMAIN,
            PlanState.NON_START -> {
                binding.homeReviseButton.visibility = View.GONE
                val searchIcon = ContextCompat
                    .getDrawable(requireContext(), R.drawable.ic_search_24dp)
                binding.homeFab.setImageDrawable(searchIcon)
                if (planState == PlanState.NONE || planState == PlanState.NON_START) {
                    if (isDarkTheme()) {
                        binding.homeImg.setImageResource(R.drawable.home_page_default_dark)
                    } else {
                        binding.homeImg.setImageResource(R.drawable.home_page_default_light)
                    }
                }
                if (planState == PlanState.FINISHED) {
                    binding.homeImg.setImageResource(R.drawable.home_page_finished)
                    binding.homePlanFinishedMsg.isVisible = true
                }
                if (planState == PlanState.COMPLETED_BUT_HAVE_REVISE_REMAIN) {
                    if (isDarkTheme()) {
                        binding.homeImg.setImageResource(R.drawable.home_page_learn_dark)
                    } else {
                        binding.homeImg.setImageResource(R.drawable.home_page_learn_light)
                    }
                }
                binding.homeLoading.visibility = View.GONE
                binding.homeFab.visibility = View.VISIBLE
                binding.homeContainer.visibility = View.VISIBLE
            }
            PlanState.LEARN -> {
                binding.homeReviseButton.visibility = View.GONE
                if (viewModel.getProgressState() == StudyProgressState.TodayFinished) {
                    val searchIcon = ContextCompat
                        .getDrawable(requireContext(), R.drawable.ic_search_24dp)
                    binding.homeFab.setImageDrawable(searchIcon)
                }
                if (isDarkTheme()) {
                    binding.homeImg.setImageResource(R.drawable.home_page_learn_dark)
                } else {
                    binding.homeImg.setImageResource(R.drawable.home_page_learn_light)
                }
                binding.homeLoading.visibility = View.GONE
                binding.homeContainer.visibility = View.VISIBLE
                binding.homeFab.visibility = View.VISIBLE
            }
            PlanState.REVISE -> {
                binding.homeFab.visibility = View.GONE
                if (isDarkTheme()) {
                    binding.homeImg.setImageResource(R.drawable.home_page_revise_dark)
                } else {
                    binding.homeImg.setImageResource(R.drawable.home_page_revise_light)
                }
                binding.homeLoading.visibility = View.GONE
                binding.homeContainer.visibility = View.VISIBLE
                binding.homeReviseButton.visibility = View.VISIBLE
            }
        }
    }

    private val fabListener = View.OnClickListener {
        viewLifecycleOwner.lifecycleScope.launch {
            when (viewModel.getProgressState()) {
                StudyProgressState.None, StudyProgressState.TodayFinished ->
                    findNavController().navigate(
                        HomeFragmentDirections
                            .actionHomeFragmentToSearchFragment()
                    )
                StudyProgressState.Learn ->
                    findNavController().navigate(
                        HomeFragmentDirections
                            .actionHomeFragmentToLearnFragment()
                    )
                StudyProgressState.LearnCompleted ->
                    findNavController().navigate(
                        HomeFragmentDirections
                            .actionHomeFragmentToStateFragment(StudySection.LEARN)
                    )
                StudyProgressState.Choice ->
                    findNavController().navigate(
                        HomeFragmentDirections
                            .actionHomeFragmentToChoiceFragment()
                    )
                StudyProgressState.ChoiceCompleted ->
                    findNavController().navigate(
                        HomeFragmentDirections
                            .actionHomeFragmentToStateFragment(StudySection.CHOICE)
                    )
                StudyProgressState.Typing ->
                    findNavController().navigate(
                        HomeFragmentDirections
                            .actionHomeFragmentToTypingFragment()
                    )
                StudyProgressState.TypingCompleted ->
                    findNavController().navigate(
                        HomeFragmentDirections
                            .actionHomeFragmentToStateFragment(StudySection.TYPING)
                    )
                StudyProgressState.Spelling ->
                    findNavController().navigate(
                        HomeFragmentDirections
                            .actionHomeFragmentToSpellingFragment()
                    )
                StudyProgressState.SpellingCompleted ->
                    findNavController().navigate(
                        HomeFragmentDirections
                            .actionHomeFragmentToStateFragment(StudySection.SPELLING)
                    )
                StudyProgressState.FreshStart -> {
                    findNavController().navigate(
                        HomeFragmentDirections
                            .actionHomeFragmentToWordListFragment()
                    )
                }

            }
        }
    }

    private val themeListener = MenuItem.OnMenuItemClickListener { menuItem ->
        menuItem.isEnabled = false
        val dialog = ThemeDialogFragment()
        dialog.setOnDismissCallback {
            menuItem.isEnabled = true
        }
        dialog.setThemeChecked(themeChecked)
        dialog.show(childFragmentManager, "ThemeDialogFragment")
        true
    }

    private val swipeOpenListener = View.OnTouchListener { view, event ->
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                x1 = event.x
            }
            MotionEvent.ACTION_UP -> {
                x2 = event.x
                val gap = x2 - x1
                if (gap > minDistance) {
                    view.performClick()
                    val main = activity as MainActivity
                    main.openDrawer()
                }
            }
        }
        true
    }

    private fun timeInMillisUntilTomorrow(): Long {
        val current = Calendar.getInstance().timeInMillis
        val tomorrow = getTomorrowDateTime().timeInMillis
        return tomorrow - current
    }

    private fun timeInMillisForWholeDay(): Long {
        val start = getTodayDateTime().timeInMillis
        val end = getTomorrowDateTime().timeInMillis
        return end - start
    }
}
