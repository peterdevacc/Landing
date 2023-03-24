package com.peter.landing.ui.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.peter.landing.R
import com.peter.landing.data.local.progress.ProgressState
import com.peter.landing.data.util.ThemeMode
import com.peter.landing.ui.navigation.LandingDestination
import com.peter.landing.ui.util.ErrorNotice
import com.peter.landing.ui.util.LandingTopBar

@Composable
fun HomeScreen(
    isDarkMode: Boolean,
    viewModel: HomeViewModel,
    navigateTo: (String) -> Unit,
) {
    val uiState by viewModel.homeUiState.collectAsStateWithLifecycle()
    val themeMenuState = remember { mutableStateOf(false) }

    HomeContent(
        isDarkMode = isDarkMode,
        uiState = uiState,
        themeMenuState = themeMenuState,
        setTheme = viewModel::setThemeMode,
        navigateTo = navigateTo,
    )
}

@Composable
private fun HomeContent(
    isDarkMode: Boolean,
    uiState: HomeUiState,
    themeMenuState: MutableState<Boolean>,
    setTheme: (ThemeMode) -> Unit,
    navigateTo: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(insets = WindowInsets.systemBars)
            .fillMaxSize()
    ) {
        LandingTopBar(
            currentDestination = LandingDestination.Main.Home,
            navigateTo = navigateTo,
            actions = {
                if (uiState is HomeUiState.Success) {
                    Box {
                        IconButton(
                            onClick = { themeMenuState.value = true }
                        ) {
                            Icon(
                                painter = painterResource(R.drawable.ic_theme_24dp),
                                contentDescription = "",
                                tint = MaterialTheme.colorScheme.onBackground
                            )
                        }
                        DropdownMenu(
                            expanded = themeMenuState.value,
                            onDismissRequest = { themeMenuState.value = false }
                        ) {
                            ThemeMode.values().forEach {
                                DropdownMenuItem(
                                    text = { Text(text = it.cnValue) },
                                    onClick = {
                                        setTheme(it)
                                        themeMenuState.value = false
                                    }
                                )
                            }
                        }
                    }
                }
            },
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(16.dp)
                .weight(1f)
                .fillMaxWidth()
        ) {
            when (uiState) {
                is HomeUiState.Error -> {
                    ErrorNotice(uiState.code)
                }
                is HomeUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp, 24.dp)
                    )
                }
                is HomeUiState.Success -> {
                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        val stateInfo = getStateActionInfo(
                            uiState.studyState, isDarkMode
                        )

                        Spacer(modifier = Modifier.weight(1.7f))
                        Box(
                            modifier = Modifier
                                .weight(3.5f)
                                .fillMaxWidth()
                        ) {
                            Image(
                                painter = painterResource(stateInfo.first),
                                contentDescription = "",
                                contentScale = ContentScale.FillBounds,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        Row(
                            verticalAlignment = Alignment.Bottom,
                            modifier = Modifier
                                .weight(3.8f)
                                .fillMaxWidth()
                        ) {
                            Spacer(modifier = Modifier.weight(1f))
                            FloatingActionButton(
                                onClick = { navigateTo(stateInfo.third) },
                                modifier = Modifier.padding(end = 16.dp, bottom = 16.dp)
                            ) {
                                Icon(
                                    painter = painterResource(stateInfo.second),
                                    contentDescription = "",
                                )
                            }
                        }
                    }

                }
            }
        }
    }

}

fun getStateActionInfo(
    studyState: StudyState,
    isDarkMode: Boolean
): Triple<Int, Int, String> {
    return when (studyState) {
        is StudyState.Learning -> {
            val image = if (studyState.progressState == ProgressState.FINISHED) {
                R.drawable.home_studying
            } else {
                if (isDarkMode) {
                    R.drawable.home_learn_dark
                } else {
                    R.drawable.home_learn_light
                }
            }
            val iconAndRoute = when (studyState.progressState) {
                ProgressState.LEARN -> {
                    R.drawable.ic_learn_24dp to LandingDestination.General.Learn.route
                }
                ProgressState.CHOICE -> {
                    R.drawable.ic_choice_24dp to LandingDestination.General.Choice.route
                }
                ProgressState.SPELLING -> {
                    R.drawable.ic_spelling_24dp to LandingDestination.General.Spelling.route
                }
                ProgressState.WORD_LIST -> {
                    R.drawable.ic_start_24dp to LandingDestination.General.WordList.route
                }
                ProgressState.FINISHED -> {
                    R.drawable.ic_search_24dp to LandingDestination.Main.Search.route
                }
            }

            Triple(image, iconAndRoute.first, iconAndRoute.second)
        }
        is StudyState.None, StudyState.PlanFinished -> {
            val image = if (studyState is StudyState.PlanFinished) {
                R.drawable.home_finished
            } else {
                if (isDarkMode) {
                    R.drawable.home_default_dark
                } else {
                    R.drawable.home_default_light
                }
            }

            Triple(image, R.drawable.ic_search_24dp, LandingDestination.Main.Search.route)
        }
    }
}
