package com.peter.landing.ui.study.learn

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.pager.*
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPagerIndicator
import com.peter.landing.R
import com.peter.landing.ui.navigation.LandingDestination
import com.peter.landing.ui.study.Counter
import com.peter.landing.ui.study.InputButtonRow
import com.peter.landing.ui.util.ErrorNotice
import com.peter.landing.ui.util.LandingTopBar
import kotlinx.coroutines.launch

@Composable
fun LearnScreen(
    viewModel: LearnViewModel,
    playPron: (String) -> Unit,
    navigateToChoice: () -> Unit,
    navigateUp: () -> Unit
) {
    LearnContent(
        uiState = viewModel.uiState.value,
        getNextWord = viewModel::getNextWord,
        submit = viewModel::submit,
        reset = viewModel::reset,
        write = viewModel::write,
        remove = viewModel::remove,
        playPron = playPron,
        navigateToChoice = navigateToChoice,
        navigateUp = navigateUp
    )
}

@OptIn(ExperimentalFoundationApi::class, ExperimentalPagerApi::class)
@Composable
private fun LearnContent(
    uiState: LearnUiState,
    getNextWord: () -> Unit,
    submit: () -> Unit,
    reset: () -> Unit,
    write: (Char) -> Unit,
    remove: () -> Unit,
    playPron: (String) -> Unit,
    navigateToChoice: () -> Unit,
    navigateUp: () -> Unit
) {
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(insets = WindowInsets.systemBars)
            .fillMaxSize()
    ) {
        LandingTopBar(
            currentDestination = LandingDestination.General.Learn,
            navigateUp = navigateUp
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            when (uiState) {
                is LearnUiState.Error -> {
                    ErrorNotice(uiState.code)
                }
                is LearnUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp, 24.dp)
                    )
                }
                is LearnUiState.Success -> {
                    val pagerState = rememberPagerState()
                    val lastOne = (uiState.current + 1) == uiState.totalNum
                    val leftActionInfo = if (uiState.submitted) {
                        R.drawable.ic_reset_24dp to reset
                    } else {
                        R.drawable.ic_submit_24dp to submit
                    }
                    val rightActionInfo = if (uiState.learned && lastOne) {
                        R.drawable.ic_start_24dp to {
                            navigateToChoice()
                        }
                    } else {
                        R.drawable.ic_double_arrow_right_24dp to {
                            getNextWord()
                            scope.launch {
                                pagerState.animateScrollToPage(0)
                            }
                            Unit
                        }
                    }

                    Column(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxSize()
                    ) {
                        Counter(
                            current = uiState.current + 1,
                            totalNum = uiState.totalNum,
                            modifier = Modifier.fillMaxWidth()
                        )
                        HorizontalPager(
                            pageCount = 2,
                            state = pagerState,
                            pageSpacing = 16.dp,
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                        ) {
                            when (it) {
                                0 -> LearnPager(
                                    spelling = uiState.word.spelling,
                                    ipa = uiState.word.ipa,
                                    cnExplain = uiState.word.cn,
                                    enExplain = uiState.word.en,
                                )
                                1 -> QuizPager(
                                    quiz = uiState.quiz,
                                    answer = uiState.word.spelling,
                                    ipa = uiState.word.ipa,
                                    input = uiState.input,
                                    write = write,
                                    remove = remove,
                                    submitted = uiState.submitted,
                                    learned = uiState.correct,
                                )
                                else -> Unit
                            }
                        }
                        Spacer(modifier = Modifier.padding(vertical = 8.dp))
                        HorizontalPagerIndicator(
                            pagerState = pagerState,
                            pageCount = 2,
                            indicatorShape = MaterialTheme.shapes.medium,
                            indicatorWidth = 32.dp,
                            indicatorHeight = 4.dp,
                            spacing = 8.dp,
                            activeColor = MaterialTheme.colorScheme.onBackground,
                            inactiveColor = MaterialTheme.colorScheme.outline.copy(alpha = 0.8f),
                            modifier = Modifier
                                .align(Alignment.CenterHorizontally)
                        )
                        Spacer(modifier = Modifier.padding(vertical = 2.dp))
                        InputButtonRow(
                            pronName = uiState.word.pronName,
                            playPron = playPron,
                            leftButtonEnable = pagerState.currentPage == 1,
                            leftButtonIconId = leftActionInfo.first,
                            leftButtonAction = leftActionInfo.second,
                            rightButtonEnable = uiState.learned,
                            rightButtonIconId = rightActionInfo.first,
                            rightButtonAction = rightActionInfo.second,
                            modifier = Modifier
                                .padding(vertical = 16.dp)
                                .fillMaxWidth()
                        )
                    }
                }
            }
        }
    }
}
