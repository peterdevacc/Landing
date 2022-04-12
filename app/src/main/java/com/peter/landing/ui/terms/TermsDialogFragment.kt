package com.peter.landing.ui.terms

import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.peter.landing.R
import com.peter.landing.databinding.FragmentTermsDialogBinding
import com.peter.landing.ui.terms.adapter.TermsAdapter
import com.peter.landing.ui.terms.adapter.TermsType
import com.peter.landing.ui.util.listStateChangeListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TermsDialogFragment : DialogFragment() {

    private var termsDialogBinding: FragmentTermsDialogBinding? = null
    private val binding get() = termsDialogBinding!!
    private val viewModel: TermsViewModel by viewModels()
    private val adapter = TermsAdapter()
    private var type = TermsType.SERVICE

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        return activity?.let {
            val builder = AlertDialog.Builder(it)
            termsDialogBinding = FragmentTermsDialogBinding.inflate(
                layoutInflater, null, false
            )

            val termsTypeData = getTermsTypeData(type)

            binding.termsDialogTitle.text = termsTypeData.first
            binding.termsDialogList.adapter = adapter
            binding.termsDialogList.addOnAttachStateChangeListener(listStateChangeListener)

            lifecycleScope.launch {
                val bufferReaderDeferred = async(Dispatchers.IO) {
                    val jsonInputStream = resources.assets.open(termsTypeData.second)
                    jsonInputStream.bufferedReader()
                }
                val bufferReader = bufferReaderDeferred.await()
                viewModel.initTerms(bufferReader.readText())
                launch(Dispatchers.IO) {
                    bufferReader.close()
                }
                val terms = viewModel.getTerms()
                terms?.run {
                    adapter.setData(data)
                }
            }

            builder.setView(binding.root)
            builder.setNegativeButton(R.string.cancel) { _, _ -> }
            builder.create()
        } ?: throw IllegalStateException("Activity cannot be null")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        termsDialogBinding = null
    }

    fun setContent(type: TermsType) {
        this.type = type
    }

    private fun getTermsTypeData(type: TermsType): Pair<String, String> {
        val titleName: String
        val fileName: String
        when (type) {
            TermsType.SERVICE -> {
                titleName = getString(R.string.about_service_terms)
                fileName = "service_terms.json"
            }
            TermsType.PRIVACY -> {
                titleName = getString(R.string.about_privacy_policy_terms)
                fileName = "privacy_terms.json"
            }
            TermsType.ACKNOWLEDGE -> {
                titleName = getString(R.string.about_acknowledge_terms)
                fileName = "acknowledge_terms.json"
            }
        }
        return Pair(titleName, fileName)
    }

}