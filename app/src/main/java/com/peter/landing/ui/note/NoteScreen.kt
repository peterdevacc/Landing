package com.peter.landing.ui.note

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import com.peter.landing.R
import com.peter.landing.data.local.word.Word
import com.peter.landing.ui.navigation.LandingDestination
import com.peter.landing.ui.note.pager.NotePager
import com.peter.landing.ui.note.pager.WrongPager
import com.peter.landing.ui.util.DictionaryDialog
import com.peter.landing.ui.util.ExplainZoomInDialog
import com.peter.landing.ui.util.LandingTopBar

@Composable
fun NoteScreen(
    isDarkMode: Boolean,
    viewModel: NoteViewModel,
    playPron: (String) -> Unit,
    navigateTo: (String) -> Unit,
) {
    NoteContent(
        isDarkMode = isDarkMode,
        uiState = viewModel.uiState.value,
        removeNote = viewModel::removeNote,
        addNote = viewModel::addNote,
        openDictionaryDialog = viewModel::openDictionaryDialog,
        openReverseDialog = viewModel::openReverseDialog,
        openExplainDialog = viewModel::openExplainDialog,
        closeDialog = viewModel::closeDialog,
        playPron = playPron,
        navigateTo = navigateTo
    )
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun NoteContent(
    isDarkMode: Boolean,
    uiState: NoteUiState,
    removeNote: (String, Long) -> Unit,
    addNote: (String, Long) -> Unit,
    openDictionaryDialog: (Word) -> Unit,
    openReverseDialog: () -> Unit,
    openExplainDialog: (String, Map<String, List<String>>) -> Unit,
    closeDialog: () -> Unit,
    playPron: (String) -> Unit,
    navigateTo: (String) -> Unit,
) {
    val pagerState = rememberPagerState()
    val titleList = listOf("生词列表", "错词列表")

    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(insets = WindowInsets.systemBars)
            .fillMaxSize()
    ) {
        LandingTopBar(
            currentDestination = LandingDestination.Main.Note,
            navigateTo = navigateTo,
            actions = {
                if (pagerState.currentPage == 0){
                    IconButton(
                        onClick = openReverseDialog
                    ) {
                        Icon(
                            painter = painterResource(R.drawable.ic_reverse_24dp),
                            contentDescription = "",
                            tint = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            },
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            when (uiState) {
                is NoteUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp, 24.dp)
                    )
                }
                is NoteUiState.Default -> {

                    when (uiState.dialog) {
                        NoteUiState.Default.Dialog.Dictionary -> {
                            if (uiState.dictionaryWord != null) {
                                DictionaryDialog(
                                    word = uiState.dictionaryWord,
                                    playPron = playPron,
                                    onDismiss = closeDialog
                                )
                            }
                        }
                        NoteUiState.Default.Dialog.Reverse -> {
                            NoteReverseDialog(
                                removedNoteList = uiState.removedNoteList,
                                addNote = addNote,
                                onDismiss = closeDialog
                            )
                        }
                        NoteUiState.Default.Dialog.Explain -> {
                            ExplainZoomInDialog(
                                spelling = uiState.wrongWordSpelling,
                                explain = uiState.wrongWordExplain,
                                onDismiss = closeDialog
                            )
                        }
                        NoteUiState.Default.Dialog.None -> Unit
                    }

                    Column(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        TabRow(
                            contentColor = MaterialTheme.colorScheme.tertiary,
                            indicator = { tabPositions ->
                                TabRowDefaults.Indicator(
                                    color = MaterialTheme.colorScheme.tertiary,
                                    modifier = Modifier.tabIndicatorOffset(
                                        tabPositions[pagerState.currentPage]
                                    )
                                )
                            },
                            selectedTabIndex = pagerState.currentPage
                        ) {
                            titleList.forEachIndexed { index, title ->
                                Tab(
                                    selected = index == pagerState.currentPage,
                                    enabled = false,
                                    onClick = { },
                                    modifier = Modifier.height(38.dp)
                                ) {
                                    Text(
                                        text = title,
                                        style = MaterialTheme.typography.titleSmall
                                    )
                                }
                            }
                        }
                        HorizontalPager(
                            pageCount = titleList.size,
                            state = pagerState,
                            pageSpacing = 16.dp,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            when (it) {
                                0 -> {
                                    NotePager(
                                        isDarkMode = isDarkMode,
                                        noteList = uiState.noteList,
                                        openDictionaryDialog = openDictionaryDialog,
                                        removeNote = removeNote,
                                        playPron = playPron
                                    )
                                }
                                1 -> {
                                    WrongPager(
                                        isDarkMode = isDarkMode,
                                        wrongWordList = uiState.wrongWordList,
                                        openExplainDialog = openExplainDialog,
                                        playPron = playPron
                                    )
                                }
                                else -> Unit
                            }
                        }
                    }
                }
            }
        }
    }

}


