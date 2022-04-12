package com.peter.landing.ui.agreement

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.NavHostFragment
import com.peter.landing.R
import com.peter.landing.databinding.FragmentAgreementBinding
import com.peter.landing.ui.terms.TermsDialogFragment
import com.peter.landing.ui.terms.adapter.TermsType
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AgreementFragment : Fragment() {

    private var agreementBinding: FragmentAgreementBinding? = null
    private val binding get() = agreementBinding!!
    private val viewModel: AgreementViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        agreementBinding = FragmentAgreementBinding
            .inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.agreementUserServiceButton.setOnClickListener {
            val dialog = TermsDialogFragment()
            dialog.setContent(TermsType.SERVICE)
            dialog.show(childFragmentManager, "TermsDialogFragment")
        }

        binding.agreementPrivacyButton.setOnClickListener {
            val dialog = TermsDialogFragment()
            dialog.setContent(TermsType.PRIVACY)
            dialog.show(childFragmentManager, "TermsDialogFragment")
        }

        binding.agreementDisagreeButton.setOnClickListener {
            requireActivity().finishAndRemoveTask()
        }

        binding.agreementAgreeButton.setOnClickListener {
            viewModel.acceptAgreement()

            val activity = requireActivity()
            val navHostFragment = activity.supportFragmentManager
                .findFragmentById(R.id.main_nav_host_fragment) as NavHostFragment
            val navController = navHostFragment.navController

            navController.graph.setStartDestination(R.id.home_fragment)
            activity.supportFragmentManager
                .beginTransaction()
                .setPrimaryNavigationFragment(navHostFragment)
                .commit()

            val navOption = NavOptions.Builder()
                .setPopUpTo(R.id.agreement_fragment, true)
                .setEnterAnim(androidx.navigation.ui.R.anim.nav_default_enter_anim)
                .setExitAnim(androidx.navigation.ui.R.anim.nav_default_exit_anim)
                .setPopEnterAnim(androidx.navigation.ui.R.anim.nav_default_pop_enter_anim)
                .setPopExitAnim(androidx.navigation.ui.R.anim.nav_default_pop_exit_anim)
                .build()
            binding.agreementGroup.isVisible = false
            binding.agreementLoading.isVisible = true
            navController.navigate(R.id.home_fragment, null, navOption)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        agreementBinding = null
    }
}