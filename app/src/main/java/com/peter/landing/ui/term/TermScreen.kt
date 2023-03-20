package com.peter.landing.ui.term

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import com.peter.landing.ui.navigation.LandingDestination
import com.peter.landing.ui.util.ErrorNotice
import com.peter.landing.ui.util.LandingTopBar
import com.peter.landing.ui.util.TermsDocument

@Composable
fun TermScreen(
    viewModel: TermViewModel,
    navigateUp: () -> Unit
) {
    TermContent(
        uiState = viewModel.uiState.value,
        navigateUp = navigateUp
    )
}

@Composable
private fun TermContent(
    uiState: TermUiState,
    navigateUp: () -> Unit
) {
    Column(
        modifier = Modifier
            .background(MaterialTheme.colorScheme.background)
            .windowInsetsPadding(insets = WindowInsets.systemBars)
            .fillMaxSize()
    ) {
        LandingTopBar(
            currentDestination = LandingDestination.General.Terms,
            navigateUp = navigateUp
        )
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(16.dp)
                .weight(1f)
                .fillMaxWidth()
        ) {
            when (uiState) {
                is TermUiState.Error -> {
                    ErrorNotice(uiState.code)
                }
                is TermUiState.Success -> {
                    TermsDocument(
                        terms = uiState.terms,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(MaterialTheme.shapes.large)
                    )
                }
                is TermUiState.Loading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp, 24.dp)
                    )
                }
            }
        }
    }

}
