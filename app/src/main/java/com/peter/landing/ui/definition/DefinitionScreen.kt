package com.peter.landing.ui.definition

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.peter.landing.R
import com.peter.landing.data.local.word.Word
import com.peter.landing.ui.navigation.LandingDestination
import com.peter.landing.ui.util.ErrorNotice
import com.peter.landing.ui.util.ExplainSection
import com.peter.landing.ui.util.ImageNotice
import com.peter.landing.ui.util.LandingTopBar

@Composable
fun DefinitionScreen(
    viewModel: DefinitionViewModel,
    playPron: (String) -> Unit,
    navigateUp: () -> Unit
) {
    DefinitionContent(
        uiState = viewModel.uiState.value,
        addNote = viewModel::addNote,
        removeNote = viewModel::removeNote,
        playPron = playPron,
        navigateUp = navigateUp
    )
}

@Composable
private fun DefinitionContent(
    uiState: DefinitionUiState,
    addNote: (Long) -> Unit,
    removeNote: (Long) -> Unit,
    playPron: (String) -> Unit,
    navigateUp: () -> Unit
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(insets = WindowInsets.systemBars)
            .fillMaxSize()
    ) {
        LandingTopBar(
            currentDestination = LandingDestination.General.Definition,
            navigateUp = navigateUp
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(start = 16.dp, end = 16.dp, top = 8.dp, bottom = 16.dp)
                .weight(1f)
                .fillMaxWidth()
        ) {
            when (uiState) {
                is DefinitionUiState.Error -> {
                    ErrorNotice(uiState.code)
                }
                is DefinitionUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp, 24.dp)
                    )
                }
                is DefinitionUiState.WordExisted -> {
                    SpellingExisted(
                        word = uiState.word,
                        addNote = addNote,
                        removeNote = removeNote,
                        isNoted = uiState.isNoted,
                        playPron = playPron
                    )
                }
                is DefinitionUiState.WordNotExisted -> {
                    SpellingNotExisted(uiState.spelling)
                }
            }
        }
    }
}

@Composable
fun SpellingExisted(
    word: Word,
    addNote: (Long) -> Unit,
    removeNote: (Long) -> Unit,
    isNoted: Boolean,
    playPron: (String) -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Text(
            text = word.spelling,
            style = MaterialTheme.typography.headlineLarge,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.padding(vertical = 1.dp))
        Text(
            text = word.ipa,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.outline,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
        )
        Spacer(modifier = Modifier.padding(vertical = 6.dp))
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            ExplainSection(
                spelling = word.spelling,
                explain = word.cn,
                modifier = Modifier
                    .border(1.dp, MaterialTheme.colorScheme.onBackground, MaterialTheme.shapes.medium)
                    .padding(8.dp)
                    .weight(1f)
                    .fillMaxWidth()
            )
            Spacer(modifier = Modifier.padding(vertical = 8.dp))
            ExplainSection(
                spelling = word.spelling,
                explain = word.en,
                isCN = false,
                modifier = Modifier
                    .border(1.dp, MaterialTheme.colorScheme.onBackground, MaterialTheme.shapes.medium)
                    .padding(8.dp)
                    .weight(1f)
                    .fillMaxWidth()
            )
        }
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .padding(vertical = 16.dp)
                .fillMaxWidth()
        ) {
            FloatingActionButton(
                onClick = {
                    if (isNoted) {
                        removeNote(word.id)
                    } else {
                        addNote(word.id)
                    }
                }
            ) {
                val iconPainter = if (isNoted) {
                    painterResource(R.drawable.ic_star_24dp)
                } else {
                    painterResource(R.drawable.ic_star_border_24dp)
                }
                Icon(
                    painter = iconPainter,
                    contentDescription = "",
                )
            }
            Spacer(modifier = Modifier.padding(horizontal = 4.dp))
            FloatingActionButton(
                onClick = { playPron(word.pronName) },
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_sound_24dp),
                    contentDescription = "",
                )
            }
        }
    }
}

@Composable
private fun SpellingNotExisted(spelling: String) {
    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        Spacer(modifier = Modifier.weight(1.2f))
        ImageNotice(
            imageId = R.drawable.search_not_found,
            text = stringResource(id = R.string.search_not_exist_hint, spelling),
            Modifier
                .weight(3.2f)
                .fillMaxWidth()
        )
        Spacer(modifier = Modifier.weight(2.5f))
    }
}
