package com.peter.landing.ui.common

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.peter.landing.R
import com.peter.landing.databinding.FragmentExplainDialogBinding
import com.peter.landing.ui.common.adapter.ExplainAdapter
import com.peter.landing.ui.common.adapter.ExplainItem

class ExplainDialogFragment : DialogFragment() {

    private var explainDialogBinding: FragmentExplainDialogBinding? = null
    private val binding get() = explainDialogBinding!!
    private val adapter = ExplainAdapter()
    private var wordExplain = emptyList<ExplainItem>()
    private var title: String = ""

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        adapter.setData(wordExplain, true)
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            explainDialogBinding = FragmentExplainDialogBinding.inflate(
                layoutInflater, null, false
            )

            binding.explainDialogTitle.text = title
            adapter.setSystemDensity(resources.displayMetrics.densityDpi)
            binding.explainList.adapter = adapter
            binding.explainList.addItemDecoration(adapter.itemSpaceDecor)

            builder.setView(binding.root)
            builder.setNegativeButton(R.string.cancel) { _, _ -> }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        explainDialogBinding = null
    }

    fun setContent(wordExplain: List<ExplainItem>, wordSpelling: String) {
        this.wordExplain = wordExplain
        title = wordSpelling
    }

}