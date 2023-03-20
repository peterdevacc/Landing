package com.peter.landing.ui.study.list

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.peter.landing.R
import com.peter.landing.ui.navigation.LandingDestination
import com.peter.landing.ui.util.ErrorNotice
import com.peter.landing.ui.util.LandingTopBar

@Composable
fun WordListScreen(
    viewModel: WordListViewModel,
    navigateToLearn: () -> Unit,
    navigateUp: () -> Unit
) {
    WordListContent(
        uiState = viewModel.uiState.value,
        startLearning = viewModel::startLearning,
        navigateToLearn = navigateToLearn,
        navigateUp = navigateUp
    )
}

@Composable
private fun WordListContent(
    uiState: WordListUiState,
    startLearning: () -> Unit,
    navigateToLearn: () -> Unit,
    navigateUp: () -> Unit
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(insets = WindowInsets.systemBars)
            .fillMaxSize()
    ) {
        LandingTopBar(
            currentDestination = LandingDestination.General.WordList,
            navigateUp = navigateUp
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            when (uiState) {
                is WordListUiState.Error -> {
                    ErrorNotice(uiState.code)
                }
                is WordListUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp, 24.dp)
                    )
                }
                is WordListUiState.Success -> {
                    if (uiState.start) {
                        LaunchedEffect(Unit) {
                            navigateToLearn()
                        }
                    }

                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        LazyVerticalGrid(
                            columns = GridCells.Fixed(2),
                            contentPadding = PaddingValues(16.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth()
                        ) {
                            items(uiState.wordList) {
                                Box(
                                    contentAlignment = Alignment.Center,
                                    modifier = Modifier
                                        .border(1.dp, MaterialTheme.colorScheme.secondary)
                                        .background(MaterialTheme.colorScheme.tertiaryContainer)
                                        .padding(8.dp)
                                ) {
                                    Text(
                                        text = it.spelling,
                                        style = MaterialTheme.typography.titleMedium,
                                        color = MaterialTheme.colorScheme.onTertiaryContainer,
                                    )
                                }

                            }
                        }
                        Spacer(modifier = Modifier.padding(vertical = 6.dp))
                        Button(
                            onClick = startLearning,
                            shape = MaterialTheme.shapes.medium,
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                                .height(46.dp)
                        ) {
                            Text(
                                text = stringResource(R.string.start_learning),
                                style = MaterialTheme.typography.bodyLarge
                            )
                        }
                    }
                }
            }
        }
    }
}
