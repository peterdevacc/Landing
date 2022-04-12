package com.peter.landing.ui.affix

import android.os.Bundle
import android.view.*
import android.widget.PopupMenu
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.peter.landing.R
import com.peter.landing.data.local.affix.AffixCatalog
import com.peter.landing.databinding.FragmentAffixBinding
import com.peter.landing.ui.affix.adapter.AffixAdapter
import com.peter.landing.ui.util.listStateChangeListener
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AffixFragment : Fragment() {
    private var affixBinding: FragmentAffixBinding? = null
    private val binding get() = affixBinding!!
    private val viewModel: AffixViewModel by viewModels()
    private val adapter = AffixAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        affixBinding = FragmentAffixBinding.inflate(
            layoutInflater, container, false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.affixList.adapter = adapter
        binding.affixList.addOnAttachStateChangeListener(listStateChangeListener)

        viewModel.affixData.observe(viewLifecycleOwner) {
            adapter.setData(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_options, menu)
        val affixTipsMenu = menu.findItem(R.id.action_affix_tips)
        affixTipsMenu.isVisible = true
        affixTipsMenu.setOnMenuItemClickListener(affixTipsListener)
        val affixFilterMenu = menu.findItem(R.id.action_affix_filter)
        affixFilterMenu.isVisible = true
        affixFilterMenu.setOnMenuItemClickListener(affixFilterListener)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        affixBinding = null
    }

    private val affixFilterListener = MenuItem.OnMenuItemClickListener { menuItem ->
        menuItem.isEnabled = false
        val popupMenu = PopupMenu(
            context,
            activity?.findViewById(menuItem.itemId)
        )
        popupMenu.setOnDismissListener {
            menuItem.isEnabled = true
        }
        popupMenu.inflate(R.menu.affix_filter_options)
        popupMenu.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_affix_prefix ->
                    viewModel.setAffixCatalogType(AffixCatalog.Type.PREFIX)
                R.id.action_affix_suffix ->
                    viewModel.setAffixCatalogType(AffixCatalog.Type.SUFFIX)
                R.id.action_affix_mixed ->
                    viewModel.setAffixCatalogType(AffixCatalog.Type.MIXED)
            }
            true
        }
        popupMenu.show()
        true
    }

    private val affixTipsListener = MenuItem.OnMenuItemClickListener { menuItem ->
        menuItem.isEnabled = false
        val dialog = AffixTipsDialogFragment()
        dialog.setOnDismissCallback {
            menuItem.isEnabled = true
        }
        dialog.show(childFragmentManager, "AffixTipsFragmentDialog")
        true
    }
}