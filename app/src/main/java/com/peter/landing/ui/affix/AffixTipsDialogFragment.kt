package com.peter.landing.ui.affix

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.peter.landing.R
import com.peter.landing.databinding.FragmentAffixTipsDialogBinding

class AffixTipsDialogFragment : BottomSheetDialogFragment() {

    private var affixTipsBinding: FragmentAffixTipsDialogBinding? = null
    private val binding get() = affixTipsBinding!!

    private var onDismissCallback: (() -> Unit)? = null

    fun setOnDismissCallback(onDismissCallback: () -> Unit) {
        this.onDismissCallback = onDismissCallback
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        affixTipsBinding = FragmentAffixTipsDialogBinding.inflate(
            layoutInflater, container, false
        )
        binding.tipsHeading.text = getString(R.string.affix_tips_heading)
        binding.tips.text = getString(R.string.affix_tips)
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        affixTipsBinding = null
    }

    override fun onDismiss(dialog: DialogInterface) {
        onDismissCallback?.invoke()
        super.onDismiss(dialog)
    }

}