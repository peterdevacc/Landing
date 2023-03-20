package com.peter.landing.ui.search

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.peter.landing.R
import com.peter.landing.ui.navigation.LandingDestination
import com.peter.landing.ui.util.LandingKeyboard
import com.peter.landing.ui.util.LandingTopBar

@Composable
fun SearchScreen(
    isDarkMode: Boolean,
    viewModel: SearchViewModel,
    navigateToDefinition: (String) -> Unit,
    navigateTo: (String) -> Unit,
) {
    SearchContent(
        isDarkMode = isDarkMode,
        uiState = viewModel.uiState.value,
        search = viewModel::search,
        write = viewModel::write,
        remove = viewModel::remove,
        setWord = viewModel::setWord,
        removeSearchHistory = viewModel::removeSearchHistory,
        openHistoryDialog = viewModel::openHistoryDialog,
        closeDialog = viewModel::closeDialog,
        navigateToDefinition = navigateToDefinition,
        navigateTo = navigateTo
    )
}

@Composable
private fun SearchContent(
    isDarkMode: Boolean,
    uiState: SearchUiState,
    search: () -> Unit,
    write: (String) -> Unit,
    remove: () -> Unit,
    setWord: (String) -> Unit,
    removeSearchHistory: () -> Unit,
    openHistoryDialog: () -> Unit,
    closeDialog: () -> Unit,
    navigateToDefinition: (String) -> Unit,
    navigateTo: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(insets = WindowInsets.systemBars)
            .fillMaxSize()
    ) {
        LandingTopBar(
            currentDestination = LandingDestination.Main.Search,
            navigateTo = navigateTo,
            actions = {
                IconButton(
                    onClick = openHistoryDialog
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_search_history_24dp),
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.onBackground
                    )
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
                is SearchUiState.Default -> {

                    when (uiState.dialog) {
                        SearchUiState.Default.Dialog.History -> {
                            SearchHistoryDialog(
                                searchHistory = uiState.searchHistory,
                                setWord = setWord,
                                removeSearchHistory = removeSearchHistory,
                                onDismiss = closeDialog
                            )
                        }
                        SearchUiState.Default.Dialog.None -> Unit
                    }

                    SearchDefaultContent(
                        isDarkMode = isDarkMode,
                        search = search,
                        spelling = uiState.spelling,
                        write = write,
                        clearAlphabet = remove,
                        suggestionList = uiState.suggestionList,
                        setWord = setWord,
                        navigateToDefinition = navigateToDefinition
                    )
                }
                is SearchUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp, 24.dp)
                    )
                }
            }
        }
    }

}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SearchDefaultContent(
    isDarkMode: Boolean,
    search: () -> Unit,
    spelling: String,
    write: (String) -> Unit,
    clearAlphabet: () -> Unit,
    suggestionList: List<String>,
    setWord: (String) -> Unit,
    navigateToDefinition: (String) -> Unit,
) {
    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier
                .weight(5.8f)
                .fillMaxWidth()
        ) {
            if (spelling == "" || spelling == "请输入要搜索的单词") {
                Spacer(modifier = Modifier.weight(1.3f))
                val image = if (isDarkMode) {
                    R.drawable.search_dark
                } else {
                    R.drawable.search_light
                }
                Image(
                    painter = painterResource(image),
                    contentDescription = "",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .weight(3.7f)
                        .fillMaxWidth()
                )
            } else {
                FlowRow(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                ) {
                    suggestionList.forEach { suggestion ->
                        OutlinedButton(
                            onClick = { setWord(suggestion) },
                            contentPadding = PaddingValues(8.dp),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.secondary),
                            modifier = Modifier.padding(4.dp)
                        ) {
                            Text(text = suggestion)
                        }
                    }
                }
            }
        }
        Spacer(modifier = Modifier.padding(vertical = 8.dp))
        Row(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .weight(1f)
                    .height(48.dp)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.tertiary,
                        shape = MaterialTheme.shapes.extraSmall,
                    )
            ) {
                Text(
                    text = spelling,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .fillMaxWidth()
                )
            }
            Button(
                onClick = {
                    search()
                    navigateToDefinition(spelling)
                },
                contentPadding = PaddingValues(4.dp),
                shape = MaterialTheme.shapes.extraSmall,
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary
                ),
                modifier = Modifier.size(48.dp)
            ) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_search_launch_28dp),
                    contentDescription = ""
                )
            }
        }
        Spacer(modifier = Modifier.padding(vertical = 4.dp))
        LandingKeyboard(
            write = write,
            remove = clearAlphabet,
            modifier = Modifier
                .padding(bottom = 32.dp)
                .weight(4.2f)
                .fillMaxWidth()
        )
    }
}
