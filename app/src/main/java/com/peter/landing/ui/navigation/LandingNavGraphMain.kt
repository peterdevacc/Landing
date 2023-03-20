package com.peter.landing.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.peter.landing.ui.about.AboutScreen
import com.peter.landing.ui.affix.AffixScreen
import com.peter.landing.ui.affix.AffixViewModel
import com.peter.landing.ui.definition.DefinitionScreen
import com.peter.landing.ui.definition.DefinitionViewModel
import com.peter.landing.ui.greeting.GreetingScreen
import com.peter.landing.ui.greeting.GreetingViewModel
import com.peter.landing.ui.help.HelpScreen
import com.peter.landing.ui.help.HelpViewModel
import com.peter.landing.ui.home.HomeScreen
import com.peter.landing.ui.home.HomeViewModel
import com.peter.landing.ui.ipa.IpaScreen
import com.peter.landing.ui.ipa.IpaViewModel
import com.peter.landing.ui.note.NoteScreen
import com.peter.landing.ui.note.NoteViewModel
import com.peter.landing.ui.plan.PlanScreen
import com.peter.landing.ui.plan.PlanViewModel
import com.peter.landing.ui.search.SearchScreen
import com.peter.landing.ui.search.SearchViewModel
import com.peter.landing.ui.term.TermScreen
import com.peter.landing.ui.term.TermViewModel

@Composable
fun LandingNavGraphMain(
    isDarkMode: Boolean,
    playPron: (String) -> Unit,
    modifier: Modifier = Modifier,
    navHostController: NavHostController,
    exitApp: () -> Unit,
    startDestination: String = LandingDestination.Main.Home.route
) {
    NavHost(
        navController = navHostController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(LandingDestination.Main.Home.route) {
            val viewModel = hiltViewModel<HomeViewModel>()
            HomeScreen(
                isDarkMode = isDarkMode,
                viewModel = viewModel,
                navigateTo = {
                    navHostController.navigate(it) {
                        popUpTo(LandingDestination.Main.Home.route) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
            )
        }
        composable(LandingDestination.Main.Plan.route) {
            val viewModel = hiltViewModel<PlanViewModel>()
            PlanScreen(
                isDarkMode = isDarkMode,
                viewModel = viewModel,
                navigateTo = {
                    navHostController.navigate(it) {
                        popUpTo(LandingDestination.Main.Home.route) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
        composable(LandingDestination.Main.Search.route) {
            val viewModel = hiltViewModel<SearchViewModel>()
            SearchScreen(
                isDarkMode = isDarkMode,
                viewModel = viewModel,
                navigateToDefinition = {
                    navHostController.navigate(
                        LandingDestination.Main.Search.getNavDefinitionRoute(it)
                    ) {
                        popUpTo(LandingDestination.Main.Search.route) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                navigateTo = {
                    navHostController.navigate(it) {
                        popUpTo(LandingDestination.Main.Home.route) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
        composable(LandingDestination.Main.Note.route) {
            val viewModel = hiltViewModel<NoteViewModel>()
            NoteScreen(
                isDarkMode = isDarkMode,
                viewModel = viewModel,
                playPron = playPron,
                navigateTo = {
                    navHostController.navigate(it) {
                        popUpTo(LandingDestination.Main.Home.route) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
        composable(LandingDestination.Main.Ipa.route) {
            val viewModel = hiltViewModel<IpaViewModel>()
            IpaScreen(
                viewModel = viewModel,
                playPron = playPron,
                navigateTo = {
                    navHostController.navigate(it) {
                        popUpTo(LandingDestination.Main.Home.route) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
        composable(LandingDestination.Main.Affix.route) {
            val viewModel = hiltViewModel<AffixViewModel>()
            AffixScreen(
                viewModel = viewModel,
                navigateTo = {
                    navHostController.navigate(it) {
                        popUpTo(LandingDestination.Main.Home.route) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
        composable(LandingDestination.Main.Help.route) {
            val viewModel = hiltViewModel<HelpViewModel>()
            HelpScreen(
                viewModel = viewModel,
                navigateTo = {
                    navHostController.navigate(it) {
                        popUpTo(LandingDestination.Main.Home.route) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
        composable(LandingDestination.Main.About.route) {
            AboutScreen(
                navigateToTerms = {
                    navHostController.navigate(
                        LandingDestination.Main.About.getNavTermsRoute(it)
                    ) {
                        launchSingleTop = true
                    }
                },
                navigateTo = {
                    navHostController.navigate(it) {
                        popUpTo(LandingDestination.Main.Home.route) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
        landingNavGraphStudy(
            isDarkMode = isDarkMode,
            playPron = playPron,
            navHostController = navHostController
        )
        composable(LandingDestination.General.Definition.destRoute) {
            val viewModel = hiltViewModel<DefinitionViewModel>()
            DefinitionScreen(
                viewModel = viewModel,
                playPron = playPron,
                navigateUp = {
                    navHostController.navigateUp()
                }
            )
        }
        composable(LandingDestination.General.Terms.destRoute) {
            val viewModel = hiltViewModel<TermViewModel>()
            TermScreen(
                viewModel = viewModel,
                navigateUp = {
                    navHostController.navigateUp()
                }
            )
        }
        composable(LandingDestination.General.Greeting.route) {
            val viewModel = hiltViewModel<GreetingViewModel>()
            GreetingScreen(
                viewModel = viewModel,
                exitApp = exitApp,
                navigateToHome = {
                    navHostController.navigate(LandingDestination.Main.Home.route) {
                        popUpTo(LandingDestination.General.Greeting.route) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}
