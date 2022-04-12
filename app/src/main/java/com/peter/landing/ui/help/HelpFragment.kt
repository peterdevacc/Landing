package com.peter.landing.ui.help

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.peter.landing.databinding.FragmentHelpBinding
import com.peter.landing.ui.help.adapter.HelpAdapter
import com.peter.landing.ui.util.listStateChangeListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HelpFragment : Fragment() {

    private var helpBinding: FragmentHelpBinding? = null
    private val binding get() = helpBinding!!
    private val viewModel: HelpViewModel by viewModels()
    private val adapter = HelpAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        helpBinding = FragmentHelpBinding
            .inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.helpItemList.adapter = adapter
        binding.helpItemList.addOnAttachStateChangeListener(listStateChangeListener)

        viewLifecycleOwner.lifecycleScope.launch {
            val helpItemList = viewModel.getHelpItemList()
            adapter.setData(helpItemList)
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        helpBinding = null
    }

}
