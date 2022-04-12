package com.peter.landing.ui.home.revise

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.peter.landing.R
import com.peter.landing.databinding.FragmentReviseBinding
import com.peter.landing.ui.home.revise.adapter.ReviseWordAdapter
import com.peter.landing.ui.util.Sound
import com.peter.landing.ui.util.listStateChangeListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ReviseFragment : Fragment() {

    private var reviseBinding: FragmentReviseBinding? = null
    private val binding get() = reviseBinding!!
    private val viewModel: ReviseViewModel by viewModels()
    private val adapter = ReviseWordAdapter()
    private lateinit var sound: Sound

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        reviseBinding = FragmentReviseBinding.inflate(
            layoutInflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sound = Sound(view.context)

        adapter.setSoundOnClickListener(soundListener)
        binding.reviseWordList.layoutManager = LinearLayoutManager(
            context, LinearLayoutManager.HORIZONTAL, false
        )
        binding.reviseWordList.adapter = adapter
        binding.reviseWordList.addOnAttachStateChangeListener(listStateChangeListener)
        val pagerSnapHelper = PagerSnapHelper()
        pagerSnapHelper.attachToRecyclerView(binding.reviseWordList)

        binding.reviseStartButton.setOnClickListener {
            findNavController().navigate(
                ReviseFragmentDirections
                    .actionReviseFragmentToExerciseFragment()
            )
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.initTodayRevise()

            val reviseWordList = viewModel.getTodayReviseWordList()
            binding.reviseTotalNum.text = getString(R.string.total_num, reviseWordList.size)
            adapter.setData(reviseWordList, childFragmentManager)

            binding.reviseLoading.visibility = View.INVISIBLE
            binding.reviseGroup.visibility = View.VISIBLE
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
        reviseBinding = null
    }

    private val soundListener = View.OnClickListener {
        val viewHolder = it.tag as RecyclerView.ViewHolder
        val word = adapter.getDataItem(viewHolder.absoluteAdapterPosition)
        sound.playAudio(word.pronName)
    }

}
