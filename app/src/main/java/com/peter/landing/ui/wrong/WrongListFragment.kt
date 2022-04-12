package com.peter.landing.ui.wrong

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import com.peter.landing.R
import com.peter.landing.databinding.FragmentWrongListBinding
import com.peter.landing.ui.common.adapter.ExplainItem
import com.peter.landing.ui.common.showExplainZoomDialog
import com.peter.landing.ui.util.Sound
import com.peter.landing.ui.util.isDarkTheme
import com.peter.landing.ui.util.listStateChangeListener
import com.peter.landing.ui.wrong.adapter.WrongWordAdapter
import com.peter.landing.ui.wrong.adapter.WrongWordViewHolder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@ExperimentalCoroutinesApi
@AndroidEntryPoint
class WrongListFragment : Fragment() {

    private var wrongListBinding: FragmentWrongListBinding? = null
    private val binding get() = wrongListBinding!!
    private val viewModel: WrongListViewModel by viewModels()
    private val adapter = WrongWordAdapter()
    private lateinit var sound: Sound

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        wrongListBinding = FragmentWrongListBinding
            .inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isDarkTheme()) {
            binding.wrongListEmptyImg.setImageResource(R.drawable.empty_img_dark)
        } else {
            binding.wrongListEmptyImg.setImageResource(R.drawable.empty_img_light)
        }

        sound = Sound(view.context)

        adapter.addLoadStateListener { loadState ->
            binding.wrongListLoading.isVisible = loadState.source.refresh is LoadState.Loading
            binding.wrongListGroup.isVisible = loadState.source.refresh is LoadState.NotLoading
            if (loadState.append.endOfPaginationReached) {
                binding.wrongListEmptyGroup.isVisible = adapter.itemCount < 1
            }
        }
        adapter.setItemListener(noteListener, soundListener, enListener, cnListener)
        binding.wrongListWordList.adapter = adapter
        binding.wrongListWordList.addItemDecoration(
            DividerItemDecoration(
                binding.wrongListWordList.context, DividerItemDecoration.VERTICAL
            )
        )
        binding.wrongListWordList.addOnAttachStateChangeListener(listStateChangeListener)

        binding.wrongListFilter.setOnClickListener {
            viewModel.todayOrAllSwitcher()
            adapter.notifyDataSetChanged()
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.wrongWordList.collectLatest {
                adapter.submitData(it)
            }
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.isAllValue.collectLatest {
                if (it) {
                    binding.wrongListFilter.text = getString(R.string.wrong_list_show_today)
                } else {
                    binding.wrongListFilter.text = getString(R.string.wrong_list_show_all)
                }
            }
        }

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
        wrongListBinding = null
    }

    private val noteListener = View.OnClickListener { view ->
        val viewHolder = view.tag as WrongWordViewHolder.ItemWrongWordViewHolder
        val wrongAndWord = viewHolder.wrongAndWord
        wrongAndWord?.let {
            if (it.wrong.isNoted) {
                viewModel.removeNote(it.word.id)
            } else {
                viewModel.addNote(it.word.id)
            }
            adapter.notifyDataSetChanged()
        }
    }

    private val soundListener = View.OnClickListener {
        val viewHolder = it.tag as WrongWordViewHolder.ItemWrongWordViewHolder
        viewHolder.wrongAndWord?.run {
            sound.playAudio(word.pronName)
        }
    }

    private val enListener = View.OnClickListener {
        val viewHolder = it.tag as WrongWordViewHolder.ItemWrongWordViewHolder
        viewHolder.wrongAndWord?.run {
            val enExplain = ExplainItem.getWordExplainSingleType(word.en)
            showExplainZoomDialog(
                word.spelling,
                ExplainItem.addItemLanguageTypeHeaderEN(enExplain),
                childFragmentManager
            )
        }
    }

    private val cnListener = View.OnClickListener {
        val viewHolder = it.tag as WrongWordViewHolder.ItemWrongWordViewHolder
        viewHolder.wrongAndWord?.run {
            val cnExplain = ExplainItem.getWordExplainSingleType(word.cn)
            showExplainZoomDialog(
                word.spelling,
                ExplainItem.addItemLanguageTypeHeaderCN(cnExplain),
                childFragmentManager
            )
        }
    }
}
