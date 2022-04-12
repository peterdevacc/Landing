package com.peter.landing.ui

import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.view.updatePadding
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.peter.landing.R
import com.peter.landing.databinding.ActivityMainBinding
import com.peter.landing.ui.util.ThemeMode
import com.peter.landing.ui.util.setThemeMode
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.LandingTheme_DayNight)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        lifecycleScope.launch {
            val theme = viewModel.getCurrentTheme()
            val mode = ThemeMode.valueOf(theme)
            setThemeMode(mode)
            configureUI()
        }

    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if (binding.mainDrawerLayout.isOpen) {
            binding.mainDrawerLayout.close()
        } else {
            super.onBackPressed()
        }
    }

    fun openDrawer() {
        binding.mainDrawerLayout.openDrawer(GravityCompat.START)
    }

    fun setToolbarTitle(title: String) {
        binding.mainToolbar.title = title
    }

    private suspend fun configureUI() {

        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.main_nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        val graph = navController.navInflater.inflate(R.navigation.main_nav_graph)
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.home_fragment,
                R.id.plan_fragment,
                R.id.ipa_fragment, R.id.search_fragment, R.id.note_fragment,
                R.id.affix_fragment,
                R.id.about_fragment, R.id.help_fragment,
                R.id.agreement_fragment
            ), binding.mainDrawerLayout
        )

        val currentAgreementValue = viewModel.getCurrentAgreementValue()
        if (currentAgreementValue) {
            graph.setStartDestination(R.id.home_fragment)
        } else {
            graph.setStartDestination(R.id.agreement_fragment)
        }
        setNavStartDestination(graph, navHostFragment)

        binding.mainDrawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            binding.mainNavView.setOnApplyWindowInsetsListener { v, insets ->
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    v.updatePadding(
                        left = v.paddingLeft +
                                insets.getInsets(WindowInsets.Type.systemBars()).left
                    )
                } else {
                    v.rootWindowInsets.displayCutout?.let {
                        v.updatePadding(
                            left = v.paddingLeft + it.safeInsetLeft
                        )
                    }
                }
                insets
            }
        }
    }

    private fun setNavStartDestination(graph: NavGraph, navHostFragment: NavHostFragment) {
        navController.graph = graph
        setSupportActionBar(binding.mainToolbar)
        setupActionBarWithNavController(navController, appBarConfiguration)
        binding.mainNavView.setupWithNavController(navController)

        supportFragmentManager.beginTransaction()
            .setPrimaryNavigationFragment(navHostFragment).commit()
    }

}
