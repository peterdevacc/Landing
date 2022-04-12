package com.peter.landing.ui.dict

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.google.android.flexbox.FlexDirection
import com.google.android.flexbox.FlexboxLayoutManager
import com.google.android.flexbox.JustifyContent
import com.google.android.material.snackbar.Snackbar
import com.peter.landing.R
import com.peter.landing.databinding.FragmentSearchBinding
import com.peter.landing.ui.dict.adapter.SuggestionsAdapter
import com.peter.landing.ui.util.isDarkTheme
import com.peter.landing.ui.util.listStateChangeListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.ExperimentalCoroutinesApi

@AndroidEntryPoint
@ExperimentalCoroutinesApi
class SearchFragment : Fragment() {

    private var searchBinding: FragmentSearchBinding? = null
    private val binding get() = searchBinding!!
    private val viewModel: SearchViewModel by viewModels()
    private val adapter = SuggestionsAdapter()
    private var snackBar: Snackbar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        searchBinding = FragmentSearchBinding
            .inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        if (isDarkTheme()) {
            binding.searchImg.setImageResource(R.drawable.search_page_dark)
        } else {
            binding.searchImg.setImageResource(R.drawable.search_page_light)
        }

        snackBar = Snackbar.make(view, "", Snackbar.LENGTH_SHORT)

        binding.searchKeyboard.setInputTarget(
            binding.searchInput,
            getString(R.string.search_input_hint)
        )
        adapter.setItemOnClickListener(itemListener)

        val layoutManager = FlexboxLayoutManager(context)
        layoutManager.flexDirection = FlexDirection.ROW
        layoutManager.justifyContent = JustifyContent.FLEX_START
        binding.searchSuggestionList.layoutManager = layoutManager

        binding.searchSuggestionList.adapter = adapter
        binding.searchSuggestionList.addOnAttachStateChangeListener(listStateChangeListener)

        binding.searchInput.addTextChangedListener(textChangedListener)

        binding.searchButton.setOnClickListener {
            val input = binding.searchInput.text.toString()
            viewModel.setSearchWordSpelling(input)
            if (viewModel.validSearchWordSpelling(binding.searchKeyboard.getTextHint())) {
                viewModel.saveSearchHistory()
                findNavController().navigate(
                    SearchFragmentDirections
                        .actionSearchFragmentToDefinitionFragment(
                            viewModel.getSearchWordSpelling()
                        )
                )
            } else {
                snackBar?.run {
                    setText(getString(R.string.search_input_hint))
                    show()
                }
            }
        }

        viewModel.inputSuggestions.observe(viewLifecycleOwner) { result ->
            if (result != null) {
                adapter.setData(result)
            } else {
                adapter.setData(emptyList())
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_options, menu)
        val searchHistoryMenu = menu.findItem(R.id.action_search_history)
        searchHistoryMenu.isVisible = true
        searchHistoryMenu.setOnMenuItemClickListener {
            findNavController().navigate(
                SearchFragmentDirections
                    .actionSearchFragmentToSearchHistoryFragment()
            )
            true
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        snackBar?.dismiss()
        searchBinding = null
    }

    private val itemListener = View.OnClickListener {
        val viewHolder = it.tag as RecyclerView.ViewHolder
        binding.searchInput.text = adapter.getDataItem(viewHolder.absoluteAdapterPosition)
    }

    private val textChangedListener = object : TextWatcher {
        override fun afterTextChanged(p0: Editable?) {}

        override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

        override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            if (p0 != null && p0.isNotEmpty() && p0.isNotBlank()) {
                val input = p0.toString()
                viewModel.setInput(input)
                binding.searchImgCard.visibility = View.GONE
                binding.searchSuggestionList.visibility = View.VISIBLE
            } else {
                viewModel.setInput("")
                viewModel.setSearchWordSpelling("")
                binding.searchImgCard.visibility = View.VISIBLE
                binding.searchSuggestionList.visibility = View.GONE
            }
        }
    }
}
