package com.peter.landing.ui.about

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.peter.landing.databinding.FragmentAboutBinding
import com.peter.landing.ui.terms.adapter.TermsType

class AboutFragment : Fragment() {

    private var aboutBinding: FragmentAboutBinding? = null
    private val binding get() = aboutBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        aboutBinding = FragmentAboutBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.aboutServiceTerms.setOnClickListener {
            findNavController().navigate(
                AboutFragmentDirections
                    .actionAboutFragmentToTermsFragment(TermsType.SERVICE)
            )
        }
        binding.aboutPrivacyTerms.setOnClickListener {
            findNavController().navigate(
                AboutFragmentDirections
                    .actionAboutFragmentToTermsFragment(TermsType.PRIVACY)
            )
        }
        binding.aboutAcknowledgeTerms.setOnClickListener {
            findNavController().navigate(
                AboutFragmentDirections
                    .actionAboutFragmentToTermsFragment(TermsType.ACKNOWLEDGE)
            )
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        aboutBinding = null
    }

}
