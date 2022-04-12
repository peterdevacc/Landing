package com.peter.landing.ui.terms

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.navArgs
import com.peter.landing.R
import com.peter.landing.databinding.FragmentTermsBinding
import com.peter.landing.ui.MainActivity
import com.peter.landing.ui.terms.adapter.TermsAdapter
import com.peter.landing.ui.terms.adapter.TermsType
import com.peter.landing.ui.util.listStateChangeListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

@AndroidEntryPoint
class TermsFragment : Fragment() {

    private var termsBinding: FragmentTermsBinding? = null
    private val binding get() = termsBinding!!
    private val viewModel: TermsViewModel by viewModels()
    private val args: TermsFragmentArgs by navArgs()
    private val adapter = TermsAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        termsBinding = FragmentTermsBinding
            .inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val termsTypeData = getTermsTypeData(args.termsType)
        (requireActivity() as MainActivity).setToolbarTitle(termsTypeData.first)

        binding.termsList.adapter = adapter
        binding.termsList.addOnAttachStateChangeListener(listStateChangeListener)

        viewLifecycleOwner.lifecycleScope.launch {
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

    }

    override fun onDestroyView() {
        super.onDestroyView()
        termsBinding = null
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