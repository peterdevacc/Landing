package com.peter.landing.ui.dict

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.DividerItemDecoration
import com.peter.landing.R
import com.peter.landing.databinding.FragmentSearchHistoryBinding
import com.peter.landing.ui.dict.adapter.SearchHistoryAdapter
import com.peter.landing.ui.dict.adapter.SearchHistoryViewHolder
import com.peter.landing.ui.util.isDarkTheme
import com.peter.landing.ui.util.listStateChangeListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class SearchHistoryFragment : Fragment() {

    private var searchHistoryBinding: FragmentSearchHistoryBinding? = null
    private val binding get() = searchHistoryBinding!!
    private val viewModel: SearchHistoryViewModel by viewModels()
    private val adapter = SearchHistoryAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        searchHistoryBinding = FragmentSearchHistoryBinding
            .inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isDarkTheme()) {
            binding.searchHistoryEmptyImg.setImageResource(R.drawable.empty_img_dark)
        } else {
            binding.searchHistoryEmptyImg.setImageResource(R.drawable.empty_img_light)
        }

        adapter.setItemOnClickListener(definitionListener)
        binding.searchHistoryList.adapter = adapter
        binding.searchHistoryList.addItemDecoration(
            DividerItemDecoration(
                binding.searchHistoryList.context, DividerItemDecoration.VERTICAL
            )
        )
        binding.searchHistoryList.addOnAttachStateChangeListener(listStateChangeListener)

        adapter.addLoadStateListener { loadState ->
            binding.searchHistoryLoading.isVisible = loadState.source.refresh is LoadState.Loading
            binding.searchHistoryList.isVisible = loadState.source.refresh is LoadState.NotLoading
        }

        viewLifecycleOwner.lifecycleScope.launch {
            viewModel.searchHistoryList.collectLatest {
                adapter.submitData(it)
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_options, menu)
        viewLifecycleOwner.lifecycleScope.launch {
            if (viewModel.getTotalSearchHistoryNum() > 0) {
                val clearSearchHistoryMenu = menu.findItem(R.id.action_empty_search_history)
                clearSearchHistoryMenu.setOnMenuItemClickListener(clearSearchHistoryListener)
                clearSearchHistoryMenu.isVisible = true
            } else {
                binding.searchHistoryEmptyCard.visibility = View.VISIBLE
                binding.searchHistoryEmptyMsg.visibility = View.VISIBLE
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        searchHistoryBinding = null
    }

    private val clearSearchHistoryListener = MenuItem.OnMenuItemClickListener { menuItem ->
        menuItem.isEnabled = false
        val alertDialog: AlertDialog? = activity?.let {
            val builder = AlertDialog.Builder(it)
            builder.apply {
                setMessage(R.string.empty_search_history_msg)
                setPositiveButton(R.string.confirm) { _, _ ->
                    binding.searchHistoryList.visibility = View.GONE
                    viewModel.emptySearchHistory()
                    menuItem.isVisible = false
                    binding.searchHistoryEmptyCard.visibility = View.VISIBLE
                    binding.searchHistoryEmptyMsg.visibility = View.VISIBLE
                }
                setNegativeButton(R.string.cancel) { _, _ -> }
                setOnDismissListener {
                    menuItem.isEnabled = true
                }
            }
            builder.create()
        }
        alertDialog?.show()
        true
    }

    private val definitionListener = View.OnClickListener {
        val viewHolder = it.tag as SearchHistoryViewHolder.ItemSearchHistoryViewHolder
        findNavController().navigate(
            SearchHistoryFragmentDirections
                .actionSearchHistoryFragmentToDefinitionFragment(viewHolder.getInput())
        )
    }
}