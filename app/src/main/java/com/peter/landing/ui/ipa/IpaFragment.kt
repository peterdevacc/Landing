package com.peter.landing.ui.ipa

import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.peter.landing.R
import com.peter.landing.data.local.ipa.Ipa
import com.peter.landing.databinding.FragmentIpaBinding
import com.peter.landing.domain.ipa.IpaItem
import com.peter.landing.ui.MainActivity
import com.peter.landing.ui.ipa.adapter.IpaAdapter
import com.peter.landing.ui.util.MAGIC_TRANSITION_NUMBER
import com.peter.landing.ui.util.Sound
import com.peter.landing.ui.util.listStateChangeListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@AndroidEntryPoint
class IpaFragment : Fragment() {

    private var ipaBinding: FragmentIpaBinding? = null
    private val binding get() = ipaBinding!!
    private val viewModel: IpaViewModel by viewModels()
    private val adapter = IpaAdapter()
    private lateinit var sound: Sound

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        ipaBinding = FragmentIpaBinding
            .inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.ipaList.setHasFixedSize(true)

        sound = Sound(view.context)

        adapter.setSoundListener(soundListener)
        binding.ipaList.addItemDecoration(
            DividerItemDecoration(
                binding.ipaList.context, DividerItemDecoration.VERTICAL
            )
        )
        binding.ipaList.adapter = adapter
        binding.ipaList.addOnAttachStateChangeListener(listStateChangeListener)
        binding.ipaList.addOnScrollListener(ipaListScrollListener)

        viewModel.ipaAndExampleList.observe(viewLifecycleOwner) {
            adapter.setData(it)
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Main) {
                delay(MAGIC_TRANSITION_NUMBER)
                binding.ipaLoading.visibility = View.GONE
                binding.ipaList.visibility = View.VISIBLE
            }
        }

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_options, menu)
        val ipaFilterMenu = menu.findItem(R.id.action_ipa_filter)
        ipaFilterMenu.isVisible = true
        ipaFilterMenu.setOnMenuItemClickListener(ipaFilterListener)
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
        ipaBinding = null
    }

    private val soundListener = View.OnClickListener { view ->
        val viewHolder = view.tag as RecyclerView.ViewHolder
        val item = adapter.getDataItem(viewHolder.absoluteAdapterPosition)
        val data = item.data as IpaItem.Data.ItemIpa
        data.ipa.exampleWordPronName.let {
            sound.playAudio(it)
        }
    }

    private val ipaFilterListener = MenuItem.OnMenuItemClickListener { menuItem ->
        menuItem.isEnabled = false
        val popupMenu = PopupMenu(
            context,
            activity?.findViewById(menuItem.itemId)
        )
        popupMenu.setOnDismissListener {
            menuItem.isEnabled = true
        }
        popupMenu.inflate(R.menu.ipa_filter_options)
        popupMenu.setOnMenuItemClickListener {
            var isHeaderVisible = true
            var isConsonants = false
            when (it.itemId) {
                R.id.action_ipa_consonants -> viewModel.setIpaType(Ipa.Type.CONSONANTS)
                R.id.action_ipa_vowels -> viewModel.setIpaType(Ipa.Type.VOWELS)
            }
            binding.ipaList.adapter?.let { adapter ->
                val layoutManager = binding.ipaList.layoutManager as LinearLayoutManager
                val firstPosition = layoutManager.findFirstVisibleItemPosition()
                val ipaAdapter = adapter as IpaAdapter
                if (ipaAdapter.getItemViewType(firstPosition) !=
                    IpaItem.Type.IpaTypeHeader.ordinal) {
                    isHeaderVisible = false
                    val item = ipaAdapter.getDataItem(0)
                    val data = item.data as IpaItem.Data.IpaTypeHeader
                    if (data.type == getString(R.string.ipa_consonants)) {
                        isConsonants = true
                    }
                }
            }
            if (isHeaderVisible) {
                setToolbarTitle(getString(R.string.page_ipa))
            } else {
                if (isConsonants) {
                    setToolbarTitle(getString(R.string.ipa_consonants))
                } else {
                    setToolbarTitle(getString(R.string.ipa_vowels))
                }
            }
            true
        }
        popupMenu.show()
        true
    }

    private val ipaListScrollListener = object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            val layoutManager = recyclerView.layoutManager as LinearLayoutManager
            val firstItemVisible = layoutManager.findFirstVisibleItemPosition()
            recyclerView.adapter?.let {
                if (it.getItemViewType(firstItemVisible) != IpaItem.Type.IpaTypeHeader.ordinal) {
                    val ipaAdapter = it as IpaAdapter
                    val item = ipaAdapter.getDataItem(0)
                    val data = item.data as IpaItem.Data.IpaTypeHeader
                    setToolbarTitle(data.type)
                } else {
                    setToolbarTitle(getString(R.string.page_ipa))
                }
            }
        }
    }

    private fun setToolbarTitle(title: String) =
        (requireActivity() as MainActivity).setToolbarTitle(title)

}