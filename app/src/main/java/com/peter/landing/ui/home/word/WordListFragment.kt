package com.peter.landing.ui.home.word

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.peter.landing.databinding.FragmentWordListBinding
import com.peter.landing.ui.home.word.adapter.WordListAdapter
import com.peter.landing.ui.util.listStateChangeListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class WordListFragment : Fragment() {

    private var wordListBinding: FragmentWordListBinding? = null
    private val binding get() = wordListBinding!!
    private val viewModel: WordListViewModel by viewModels()
    private val adapter = WordListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        wordListBinding = FragmentWordListBinding
            .inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.wordListWordList.adapter = adapter
        binding.wordListWordList.addOnAttachStateChangeListener(listStateChangeListener)

        binding.wordListStartButton.setOnClickListener {
            findNavController().navigate(
                WordListFragmentDirections
                    .actionWordListFragmentToLearnFragment()
            )
        }

        viewModel.setStudyState(true)

        viewLifecycleOwner.lifecycleScope.launch {
            val wordList = viewModel.getWordList()
            if (wordList.isNotEmpty()) {
                adapter.setData(wordList)
            }
            binding.wordListLoading.visibility = View.GONE
            binding.wordListGroup.visibility = View.VISIBLE
        }

    }

    override fun onStop() {
        super.onStop()
        viewModel.setStudyState(false)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        wordListBinding = null
    }

}
