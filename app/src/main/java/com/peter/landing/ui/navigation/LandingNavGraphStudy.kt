package com.peter.landing.ui.navigation

import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.peter.landing.ui.study.choice.ChoiceScreen
import com.peter.landing.ui.study.choice.ChoiceViewModel
import com.peter.landing.ui.study.learn.LearnScreen
import com.peter.landing.ui.study.learn.LearnViewModel
import com.peter.landing.ui.study.list.WordListScreen
import com.peter.landing.ui.study.list.WordListViewModel
import com.peter.landing.ui.study.spelling.SpellingScreen
import com.peter.landing.ui.study.spelling.SpellingViewModel


fun NavGraphBuilder.landingNavGraphStudy(
    isDarkMode: Boolean,
    playPron: (String) -> Unit,
    navHostController: NavHostController,
    startDestination: String = LandingDestination.General.WordList.route
) {
    navigation(
        startDestination = startDestination,
        route = "study"
    ) {
        composable(LandingDestination.General.Spelling.route) {
            val viewModel = hiltViewModel<SpellingViewModel>()
            SpellingScreen(
                viewModel = viewModel,
                playPron = playPron,
                navigateUp = {
                    navHostController.navigateUp()
                }
            )
        }
        composable(LandingDestination.General.Choice.route) {
            val viewModel = hiltViewModel<ChoiceViewModel>()
            ChoiceScreen(
                isDarkMode = isDarkMode,
                viewModel = viewModel,
                playPron = playPron,
                navigateToSpelling = {
                    navHostController.navigate(LandingDestination.General.Spelling.route) {
                        popUpTo(LandingDestination.General.Choice.route) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                navigateUp = {
                    navHostController.navigateUp()
                }
            )
        }
        composable(LandingDestination.General.Learn.route) {
            val viewModel = hiltViewModel<LearnViewModel>()
            LearnScreen(
                viewModel = viewModel,
                playPron = playPron,
                navigateToChoice = {
                    navHostController.navigate(LandingDestination.General.Choice.route) {
                        popUpTo(LandingDestination.General.Learn.route) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                navigateUp = {
                    navHostController.navigateUp()
                }
            )
        }
        composable(LandingDestination.General.WordList.route) {
            val viewModel = hiltViewModel<WordListViewModel>()
            WordListScreen(
                viewModel = viewModel,
                navigateToLearn = {
                    navHostController.navigate(LandingDestination.General.Learn.route) {
                        popUpTo(LandingDestination.General.WordList.route) {
                            inclusive = true
                        }
                        launchSingleTop = true
                    }
                },
                navigateUp = {
                    navHostController.navigateUp()
                }
            )
        }
    }
}
