package com.peter.landing.ui.help

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.peter.landing.ui.navigation.LandingDestination
import com.peter.landing.ui.util.ErrorNotice
import com.peter.landing.ui.util.LandingTopBar

@Composable
fun HelpScreen(
    viewModel: HelpViewModel,
    navigateTo: (String) -> Unit,
) {
    HelpContent(
        uiState = viewModel.uiState.value,
        navigateTo = navigateTo
    )
}

@Composable
private fun HelpContent(
    uiState: HelpUiState,
    navigateTo: (String) -> Unit,
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(insets = WindowInsets.systemBars)
            .fillMaxSize()
    ) {
        LandingTopBar(
            currentDestination = LandingDestination.Main.Help,
            navigateTo = navigateTo,
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
        ) {
            when (uiState) {
                is HelpUiState.Error -> {
                    ErrorNotice(uiState.code)
                }
                is HelpUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp, 24.dp)
                    )
                }
                is HelpUiState.Success -> {
                    val helpSection = uiState.helpMap.toList()
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(helpSection) {
                            HelpSection(
                                helpCatalog = it.first,
                                helpList = it.second
                            )
                        }
                    }

                }
            }
        }
    }

}
